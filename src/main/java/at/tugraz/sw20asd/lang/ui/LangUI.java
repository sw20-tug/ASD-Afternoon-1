package at.tugraz.sw20asd.lang.ui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;

import at.tugraz.sw20asd.lang.ui.Controller;


public class LangUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Vocabulary Trainer");

        final Parent root = new Controller();
        final Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }
}
