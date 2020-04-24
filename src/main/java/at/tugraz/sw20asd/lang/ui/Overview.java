package at.tugraz.sw20asd.lang.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.IOException;

public class Overview extends VBox {

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


    public Overview(){
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(c -> this);
        loader.setRoot(this);
        try {
            loader.load(getClass().getResource("/overview.fxml").openStream());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void initialize(){

        add_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                System.out.println("AddVocab"); // Should go to the addVocab() tab
            }
        });

        edit_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                System.out.println("Edit"); // pop ups to the edit screen
            }
        });

        vocabtrainer_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                System.out.println("Vocab trainer"); // pop ups to the vocab trainer
            }
        });

    }
}

