package at.tugraz.sw20asd.lang.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.IOException;

public class AddVocab extends VBox {
    @FXML
    private TextField german_field;
    @FXML
    private TextField english_field;
    @FXML
    private Label german_label;
    @FXML
    private Label english_label;
    @FXML
    private Label title;
    @FXML
    private Button submit_btn;
    @FXML
    private Button return_btn;

    private String german_string;
    private String english_string;

    public AddVocab(){
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(c -> this);
        loader.setRoot(this);
        try {
            loader.load(getClass().getResource("/add.fxml").openStream());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void initialize(){

        german_field.textProperty().addListener((observable, oldValue, newValue) -> {
            german_string = newValue;
        });
        english_field.textProperty().addListener((observable, oldValue, newValue) -> {
            english_string = newValue;
        });

        submit_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                System.out.println(german_string + " " + english_string); // replace with add vocab
            }
        });

        return_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                System.out.println("Return"); // replace with return to overview(?) tab
            }
        });
    }
}
