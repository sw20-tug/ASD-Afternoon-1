package at.tugraz.sw20asd.lang.ui.Controller;

import at.tugraz.sw20asd.lang.ui.VocabularyAccess;
import at.tugraz.sw20asd.lang.ui.VocabularyAccessRestImpl;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import javafx.scene.layout.VBox;

import java.io.IOException;


public class Controller extends VBox {
    @FXML
    private Button overview_btn;
    @FXML
    private Button add_btn;
    @FXML
    private Button train_btn;
    @FXML
    private Button edit_btn;
    @FXML
    private Button exit_btn;

    FXMLLoader loader = new FXMLLoader();

    private VocabularyAccess vocab;


    public Controller() {

        vocab = new VocabularyAccessRestImpl("localhost", 8080);

        loader.setControllerFactory(c -> this);
        loader.setRoot(this);
        try {
            loader.load(getClass().getResource("/main.fxml").openStream());

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

        overview_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                OverviewVocabs overview = new OverviewVocabs(vocab);
                getScene().setRoot(overview);
            }
        });

        train_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                System.out.println("vocab trainer");
            }
        });

        edit_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                EditVocabSelection edit = new EditVocabSelection(vocab);
                getScene().setRoot(edit);
            }
        });

        exit_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });
    }
}