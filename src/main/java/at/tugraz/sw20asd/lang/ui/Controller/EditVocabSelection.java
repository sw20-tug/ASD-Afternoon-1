package at.tugraz.sw20asd.lang.ui.Controller;

import at.tugraz.sw20asd.lang.model.Entry;
import at.tugraz.sw20asd.lang.model.Vocabulary;
import at.tugraz.sw20asd.lang.ui.VocabularyAccess;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EditVocabSelection extends VBox {

    @FXML
    private Label user_info;
    @FXML
    private Button return_btn;
    @FXML
    private VBox button_list, delete_button_list;

    private VocabularyAccess vocab;

    private Task<List<Vocabulary>> getAllVocabsTask;
    private List<Vocabulary> Vocabularies;

    public EditVocabSelection(VocabularyAccess vocab) {
        this.vocab = vocab;
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(c -> this);
        loader.setRoot(this);
        try {
            loader.load(getClass().getResource("/editvocab.fxml").openStream());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void initialize(){
        getVocabs();
        return_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                clearEditVocabs();
                Controller con = new Controller();
                getScene().setRoot(con);
            }
        });
    }

    private void getVocabs() {
        getAllVocabsTask = new Task<>() {
            @Override
            protected List<Vocabulary> call() throws Exception {
                Vocabularies = vocab.getAllVocabularies();
                return Vocabularies;
            }
        };

        getAllVocabsTask.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.CANCELLED || newValue == Worker.State.FAILED || getAllVocabsTask.getValue() == null) {
                Platform.runLater(() -> {
                    updateUserInformation("");
                });
                getAllVocabsTask.cancel();
            }

            if (newValue == Worker.State.SUCCEEDED) {
                if (Vocabularies == null || Vocabularies.size() == 0) {
                    Platform.runLater(() -> {
                        updateUserInformation("No vocabs added");
                    });
                } else {
                    setVocabs();
                }
            }
        }));

        Thread th = new Thread(getAllVocabsTask);
        th.setDaemon(true);
        th.start();
    }

    private void updateUserInformation(String code) {
        user_info.setVisible(true);
        user_info.setTextFill(Color.RED);
        switch (code) {
            case "No vocabs added":
                user_info.setText("You haven`t added any vocab yet!");
                break;
            case "Server error":
                user_info.setText("Server not working");
                break;
            default:
                user_info.setText("");
        }
    }

    private void setVocabs() {
        int size = Vocabularies.size();
        for(int counter = 0; counter < size; counter++)
        {
            int current_counter = counter;
            Button vocab_button = new Button("vocab_button" + String.valueOf(1));
            vocab_button.setId("vocab_button" + String.valueOf(1));
            vocab_button.setMinHeight(40.0);
            vocab_button.setPrefWidth(376.0);
            vocab_button.setStyle("-fx-border-insets: 1;");
            vocab_button.setFont(new Font("Abyssinica SIL", 16));
            setMargin(vocab_button, new Insets(20,0,0,0));
            vocab_button.setText(Vocabularies.get(current_counter).getName());
            vocab_button.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    EditVocab edit = new EditVocab(vocab, Vocabularies.get(current_counter));
                    getScene().setRoot(edit);
                }
            });
            button_list.getChildren().add(vocab_button);

            Button delete_button = new Button("delete_button" + String.valueOf(1));
            delete_button.setId("delete_button" + String.valueOf(1));
            delete_button.setMinHeight(40.0);
            delete_button.setPrefWidth(10);
            delete_button.setStyle("-fx-border-insets: 1;");
            setMargin(delete_button, new Insets(20,0,0,0));
            delete_button.setText("-");
            delete_button.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    // TODO delete vocabulary
                    button_list.getChildren().remove(vocab_button);
                    delete_button_list.getChildren().remove(delete_button);
                }
            });
            delete_button_list.getChildren().add(delete_button);
        }

    }

    private void clearEditVocabs() {
        Vocabularies.clear();
    }
}