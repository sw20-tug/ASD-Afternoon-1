package at.tugraz.sw20asd.lang.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;


public class Controller extends VBox {
    @FXML
    private Button overview_btn;
    @FXML
    private Button add_btn;
    @FXML
    private Button train_btn;
    @FXML
    private Button exit_btn;

    FXMLLoader loader = new FXMLLoader();



    public Controller() {

        loader.setControllerFactory(c -> this);
        loader.setRoot(this);
        try {
            loader.load(getClass().getResource("/main.fxml").openStream());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // this.getChildren().add(new Layout());
    }
    public void initialize(){

        add_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                AddVocab add = new AddVocab();
                getScene().setRoot(add);
            }
        });

        overview_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                System.out.println("overview");
            }
        });

        train_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                System.out.println("vocab trainer");
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