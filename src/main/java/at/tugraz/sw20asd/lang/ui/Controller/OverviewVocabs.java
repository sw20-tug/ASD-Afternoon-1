package at.tugraz.sw20asd.lang.ui.Controller;

import at.tugraz.sw20asd.lang.model.Vocabulary;
import at.tugraz.sw20asd.lang.ui.VocabularyAccess;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.*;

public class OverviewVocabs extends VBox {

    private VocabularyAccess vocab;

    public OverviewVocabs(VocabularyAccess vocab) {
        this.vocab = vocab;
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(c -> this);
        loader.setRoot(this);
        try {
            loader.load(getClass().getResource("/overview-vocabs.fxml").openStream());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void initialize() {
        for (Vocabulary v : vocab.getAllVocabularies()) {
            Button b = new Button("b" + v.getID());
            b.setId("b" + v.getID());
            b.setPrefHeight(40.0);
            b.setPrefWidth(376.0);
            b.setStyle("-fx-border-insets: 1;");
            b.setFont(new Font("Abyssinica SIL", 16));
            b.setLayoutX(318.0);
            b.setLayoutY(300.0);
            b.setText(v.getName());

            b.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    OverviewWords overview = new OverviewWords(vocab, v.getID());
                    getScene().setRoot(overview);
                }
            });
            getChildren().add(b);
        }
    }
}