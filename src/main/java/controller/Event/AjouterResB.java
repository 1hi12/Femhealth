package controller.Event;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.events.Evenement;
import model.events.Reservation;
import service.events.ReservationC;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterResB {

    @FXML
    private TextField id_evenement_idTF;

    @FXML
    private ComboBox<String> mode_paiementCB;

    @FXML
    private TextField statut_paiementTF;

    @FXML
    void AfficherResB(ActionEvent event) {
        // Fermer la fenêtre actuelle
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        try {
            // Charger la nouvelle vue
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Back.Event/AfficherResB.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Créer une nouvelle fenêtre
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'erreur de chargement de la nouvelle vue
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Failed to load EventAfficherResB.fxml");
            alert.show();
        }
    }


    @FXML
    void AjouterResB(ActionEvent event) {
        String id_evenement_idText = id_evenement_idTF.getText();
        String statut_paiement = statut_paiementTF.getText();
        String mode_paiement = mode_paiementCB.getValue();

        // Vérification des champs vides
        if (id_evenement_idText.isEmpty() || statut_paiement.isEmpty() || mode_paiement == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setContentText("Veuillez remplir tous les champs");
            alert.show();
        } else {
            // Tous les champs sont remplis, nous pouvons procéder à l'ajout
            int id_evenement_id = Integer.parseInt(id_evenement_idText);
            Reservation r = new Reservation();
            Evenement evenement = null;
            ReservationC rc = new ReservationC();

            try {
                // Vérifier si l'événement existe
                evenement = rc.getEvenementById(id_evenement_id);
                if (evenement != null) {
                    r.setId_evenement_id(evenement);
                    r.setStatut_paiement(statut_paiement);
                    r.setMode_paiement(mode_paiement);

                    rc.add(r);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Success");
                    alert.setContentText("Réservation ajoutée");
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setContentText("Cet événement n'existe pas");
                    alert.show();
                }
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText(e.getMessage());
                alert.show();
            }
        }
    }
}
