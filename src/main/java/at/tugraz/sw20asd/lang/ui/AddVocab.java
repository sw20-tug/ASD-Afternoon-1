package at.tugraz.sw20asd.lang.ui;

import at.tugraz.sw20asd.lang.model.Vocabulary;
import at.tugraz.sw20asd.lang.service.VocabularyDAO;
import at.tugraz.sw20asd.lang.service.VocabularyDAOFileImpl;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import at.tugraz.sw20asd.lang.model.Entry;
import javafx.scene.paint.Color;
import org.springframework.util.ObjectUtils;

public class AddVocab extends VBox {
    @FXML
    private TextField from_field;
    @FXML
    private TextField to_field;
    @FXML
    private Label from_label;
    @FXML
    private Label to_label;
    @FXML
    private Button submit_btn;
    @FXML
    private Button return_btn;
    @FXML
    private ComboBox<String> from_choice;
    @FXML
    private ComboBox<String> to_choice;
    @FXML
    private Label user_info;

    private String from_string;
    private String to_string;

    private VocabularyDAO vocabularyDAO;
    private File workingDirectory;

    private int vocabularyID;
    private Task<Integer> id_task;

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
        workingDirectory = new File("vocabs");
        vocabularyDAO = new VocabularyDAOFileImpl(workingDirectory.getName());
        vocabularyID = workingDirectory.listFiles().length;
        vocabularyID = getLatestID();

        user_info.setVisible(false);
        ObservableList<String> languages =
                FXCollections.observableArrayList("German", "English");

        from_choice.setItems(languages);
        to_choice.setItems(languages);

        HashMap<String, Locale> language_map = new HashMap<String, Locale>();
        language_map.put("German", Locale.GERMAN);
        language_map.put("English", Locale.ENGLISH);

        from_choice.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            from_label.setText(newValue + ":");

            if(!to_choice.getSelectionModel().isEmpty()  &&
                    (to_choice.getSelectionModel().getSelectedItem().equals(newValue))){

                updateUserInformation("equal_lang");
            }
            else{
                user_info.setVisible(false);
            }
        });

        to_choice.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            to_label.setText(newValue + ":");

            if(!from_choice.getSelectionModel().isEmpty() &&
                    from_choice.getSelectionModel().getSelectedItem().equals(newValue)){

                updateUserInformation("equal_lang");
            }
            else{
                user_info.setVisible(false);
            }
        });

        from_field.textProperty().addListener((observable, oldValue, newValue) -> {
            from_string = newValue;
        });
        to_field.textProperty().addListener((observable, oldValue, newValue) -> {
            to_string = newValue;
        });

        submit_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                if(from_choice.getSelectionModel().isEmpty()
                   || to_choice.getSelectionModel().isEmpty()
                   || from_field.getText().isEmpty() || to_field.getText().isEmpty()){
                    updateUserInformation("missing_param");
                }
                else if(from_choice.getSelectionModel().getSelectedItem().equals(to_choice.getSelectionModel().getSelectedItem())){
                    updateUserInformation("equal_lang");
                }
                else{
                    user_info.setVisible(false);

                    vocabularyID++;

                    String from_lang = from_choice.getSelectionModel().getSelectedItem();
                    String to_lang = to_choice.getSelectionModel().getSelectedItem();

                    Locale source_lang = language_map.get(from_lang);
                    Locale target_lang = language_map.get(to_lang);

                    Vocabulary vocab = new Vocabulary(vocabularyID, "My_Vocabs", source_lang, target_lang);
                    Entry entry = new Entry(from_string, to_string);
                    vocab.addPhrase(entry);
                    vocabularyDAO.addVocabulary(vocab);
                    updateUserInformation("added_vocab");
                }
            }
        });

        return_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                from_choice.getSelectionModel().clearSelection();
                to_choice.getSelectionModel().clearSelection();
                from_field.clear();
                to_field.clear();
                from_label.setText("From:");
                to_label.setText("To:");
                Controller con = new Controller();
                getScene().setRoot(con);
            }
        });


    }

    private void updateUserInformation(String code){
        user_info.setVisible(true);
        user_info.setTextFill(Color.RED);
        switch (code){
            case "added_vocab":
                user_info.setText("Vocabulary added!");
                break;
            case "equal_lang":
                user_info.setText("Please select another language");
                break;
            case "missing_param":
                user_info.setText("Please fill out everything");
                break;
            default:
                user_info.setText("Sorry, something went wrong");
        }
    }

    private int getLatestID(){

        File[] files = workingDirectory.listFiles(File::isFile);
        final String[] token = {null};
        final int[] id = {0};

        id_task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                if (files != null) {
                    for (File file : files) {

                        token[0] = file.getName().split("\\.")[0];

                        if (id[0] < Integer.parseInt(token[0]))
                            id[0] = Integer.parseInt(token[0]);

                    }
                    return id[0];
                }
                return 1;
            }
        };

        id_task.stateProperty().addListener(((observableValue, oldState, newState) -> {
            if(newState == Worker.State.CANCELLED){
                id_task = null;
            }
        }
        ));

        Thread th = new Thread(id_task);
        th.setDaemon(true);
        th.start();
         //ID = 1 if no files exist
        return 1;
    }
}