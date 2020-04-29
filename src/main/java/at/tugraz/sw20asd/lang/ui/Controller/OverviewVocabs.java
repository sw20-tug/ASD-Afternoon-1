package at.tugraz.sw20asd.lang.ui.Controller;

import at.tugraz.sw20asd.lang.ui.VocabularyAccess;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;



import java.io.File;
import java.io.IOException;

public class OverviewVocabs extends VBox {


    public Button vocabtrainer_btn;
    @FXML
    private Label title;
    @FXML
    private Label name;


    private VocabularyAccess vocab;


    public OverviewVocabs(VocabularyAccess vocab) {
        this.vocab = vocab;
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(c -> this);
        loader.setRoot(this);
        try {
            loader.load(getClass().getResource("/overviewvocabs.fxml").openStream());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }



    public void initialize() {

        int num_but = new File("C:\\Users\\juan\\Desktop\\ASD-Afternoon-1\\vocabs").list().length;
        for (int i = 0; i < num_but; i++) {
            Button b = new Button("b" + String.valueOf(i));
            b.setId("b" + String.valueOf(i));
            b.setPrefHeight(40.0);
            b.setPrefWidth(376.0);
            b.setStyle("-fx-border-insets: 1;");
            b.setFont(new Font("Abyssinica SIL", 16));
            b.setLayoutX(318.0);
            b.setLayoutY(300.0);
            getChildren().add(b);
        }




    }
}