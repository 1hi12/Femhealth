package Controllers.User;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.User.Utilisateur;
import service.User.UtilisateurService;
import utils.PasswordUtils;
import javafx.scene.control.Hyperlink;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class AuthentificationController {

    @FXML
    private TextField EmailTF;

    @FXML
    private TextField mdpTF;

    @FXML
    private Button seConnecterTF;

    @FXML
    private Hyperlink mdpOublieTF;

    private UtilisateurService utilisateurService;

    @FXML
    private void initialize() {
        utilisateurService = new UtilisateurService();
        seConnecterTF.disableProperty().bind(EmailTF.textProperty().isEmpty()
                .or(mdpTF.textProperty().isEmpty()));

    }

    @FXML
    private void seConnecter() {
        System.out.println("Se connecter button clicked");
        String email = EmailTF.getText();
        String motDePasse = mdpTF.getText();

        if (!isValidEmail(email)) {
            showAlert("Adresse email invalide !");
            return;
        }

        Utilisateur utilisateur = utilisateurService.authentification(email);

        if (utilisateur != null && PasswordUtils.verifyPassword(motDePasse, utilisateur.getMdp())) {
            if (utilisateur.getRole().equals("[\"ROLE_ADMIN\"]")) {
                System.out.println("Redirecting to BaseAdmin");
                try {
                    redirectToBaseAdmin();
                } catch (Exception e) {
                    showAlert("Erreur lors de la redirection vers l'interface administrateur.");
                    e.printStackTrace();
                }
            } else if (utilisateur.getRole().equals("[\"ROLE_CLIENT\"]")) {
                System.out.println("Redirecting to HomePageClient");
                redirectToHomePageClient();
            }
        } else {
            showAlert("Email ou mot de passe incorrect !");
        }
    }

    private void redirectToBaseAdmin() {
        System.out.println("Redirecting to BaseAdmin");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/BaseAdmin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) seConnecterTF.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void redirectToHomePageClient() {
        System.out.println("Redirecting to HomePageClient");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/HomePageClient.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) seConnecterTF.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private boolean isValidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        return email.matches(emailPattern);
    }
    @FXML
    private void motDePasseOublie() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/ResetPassword.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) mdpOublieTF.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






}