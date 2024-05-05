package controller.front.Blog;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Blog.Publication;
import service.Blog.PublicationService;
import javafx.geometry.Insets;

import java.awt.*;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class FXMLDocumentController implements Initializable {

    @FXML
    private GridPane cardHolder;
    @FXML
    private Button categoryid;
    @FXML
    private AnchorPane idsidebar;
    private VBox labelContainer;

    ObservableList<CustomerCard> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PublicationService allpublication = new PublicationService();
        try {
            for (int i = 0; i < allpublication.select().size(); i++) {
                System.out.println(allpublication.select().get(i).getImage());
                SimpleDateFormat sdfNouveau = new SimpleDateFormat("yyyy-MM-dd");
                String dateresult= sdfNouveau.format(allpublication.select().get(i).getDatepub());
                list.add(new CustomerCard(allpublication.select().get(i).getId(),
                        allpublication.select().get(i).getTitre(),
                        dateresult,
                        allpublication.select().get(i).getImage()));
            }
            int count = 0;
            int maxCardsPerRow = 3;
            double topMargin = 15;
            for (int i = 0; i < allpublication.select().size(); i += maxCardsPerRow) {
                for (int j = 0; j < maxCardsPerRow && (i + j) < allpublication.select().size(); j++) {
                    cardHolder.setMargin(list.get(count), new Insets(20,15 , 0, 0));

                    cardHolder.add(list.get(count), j, i / maxCardsPerRow);
                    count++;
                }
            }
            VBox labelContainer = new VBox(20);
            idsidebar.getChildren().add(labelContainer);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    public void onSearch() {
        int count = 0;
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 1; j++) {
                cardHolder.add(list.get(count), j, i);
                count++;
            }
        }
    }
}