package at.tugraz.sw20asd.lang.ui.Controller;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import at.tugraz.sw20asd.lang.ui.Controller.Controller;


public class LangUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private Parent root;
    private Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Vocabulary Trainer");

        root = new Controller();
        scene = new Scene(root);
        stage.setScene(scene);

        stage.show();
    }
    //testing reasons
    public Parent getRoot(){
        return this.root;
    }

    public Scene getScene(){
        return this.scene;
    }
}
