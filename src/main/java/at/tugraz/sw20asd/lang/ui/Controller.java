package at.tugraz.sw20asd.lang.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;

import java.io.IOException;


public class Controller extends VBox {

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

    public Controller() {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(c -> this);
        loader.setRoot(this);
        try {
            loader.load(getClass().getResource("/main.fxml").openStream());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }
}
