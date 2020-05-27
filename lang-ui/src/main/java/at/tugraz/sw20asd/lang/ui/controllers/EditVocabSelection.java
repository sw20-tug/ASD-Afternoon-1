package at.tugraz.sw20asd.lang.ui.controllers;


import at.tugraz.sw20asd.lang.VocabularyBaseDto;
import at.tugraz.sw20asd.lang.ui.dataaccess.VocabularyAccess;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;
import java.util.List;

public class EditVocabSelection extends VBox {

    @FXML
    private Label user_info;
    @FXML
    private Button return_btn;
    @FXML
    private VBox button_list, delete_button_list;

    private VocabularyAccess vocab;

    private Task<Integer> deletetask;
    private Task<List<VocabularyBaseDto>> getAllVocabsTask;
    private List<VocabularyBaseDto> Vocabularies;
    private int id;

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
        user_info.setVisible(false);
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
            protected List<VocabularyBaseDto> call() throws Exception {
                Vocabularies = vocab.getAllVocabularies();
                return Vocabularies;
            }
        };

        getAllVocabsTask.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.CANCELLED || newValue == Worker.State.FAILED) {
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
            case "deleted_vocab":
                user_info.setText("Vocabulary deleted!");
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
                    EditVocab edit = new EditVocab(vocab, Vocabularies.get(current_counter).getId(), EditVocab.Mode.EDIT);
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
                    sendDeleteCommand(Vocabularies.get(current_counter).getId());
                    button_list.getChildren().remove(vocab_button);
                    delete_button_list.getChildren().remove(delete_button);
                }
            });
            delete_button_list.getChildren().add(delete_button);
        }

    }

    private void sendDeleteCommand(long vocab_id) {
        user_info.setVisible(false);
        id = -1;

        deletetask = new Task<Integer> () {
            @Override
            protected Integer call() throws Exception {
                int id = 0;
                vocab.deleteVocabulary(vocab_id);
                return id;
            }
        };

        deletetask.stateProperty().addListener(((observableValue, oldState, newState)->{
            if(newState == Worker.State.CANCELLED){
                deletetask.cancel();
            }
            if(newState == Worker.State.SUCCEEDED){
                id = deletetask.getValue();
                if(id != -1){
                    Platform.runLater(() -> {
                        updateUserInformation("deleted_vocab");
                    });
                }
                else{
                    Platform.runLater(() -> {
                        updateUserInformation("");
                    });
                }
            }
        }));

        Thread th = new Thread(deletetask);
        th.setDaemon(true);
        th.start();
    }

    private void clearEditVocabs() {
        Vocabularies.clear();
    }
}