package at.tugraz.sw20asd.lang.ui.Controller;

import at.tugraz.sw20asd.lang.model.Vocabulary;
import at.tugraz.sw20asd.lang.ui.VocabularyAccess;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;

import at.tugraz.sw20asd.lang.model.Entry;

public class OverviewWords extends VBox {

    @FXML
    private Label title;
    @FXML
    private Button add_btn;
    @FXML
    private Button edit_btn;
    @FXML
    private Button vocabtrainer_btn;
    @FXML
    private TableView<String> table;

    private VocabularyAccess vocab;


    public OverviewWords(VocabularyAccess vocab){
        this.vocab= vocab;
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(c -> this);
        loader.setRoot(this);
        try {
            loader.load(getClass().getResource("/overviewwords.fxml").openStream());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void initialize(){

        add_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                    AddVocab add = new AddVocab(vocab);
                    getScene().setRoot(add);

            }
        });

        edit_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                System.out.println("Edit"); // pop ups to the edit screen
            }
        });

        vocabtrainer_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                Controller vocab_menu = new Controller();
                getScene().setRoot(vocab_menu);
            }
        });

    }
}