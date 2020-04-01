package at.tugraz.sw20asd.lang.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;

import java.io.IOException;


public class Controller extends VBox {


    public Controller() {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(c -> this);
        loader.setRoot(this);
        try {
            loader.load(getClass().getResource("/main.fxml").openStream());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        this.getChildren().add(new Layout());

    }
}
