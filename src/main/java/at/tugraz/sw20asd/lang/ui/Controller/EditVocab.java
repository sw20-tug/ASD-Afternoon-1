package at.tugraz.sw20asd.lang.ui.Controller;

import at.tugraz.sw20asd.lang.model.Vocabulary;
import at.tugraz.sw20asd.lang.ui.VocabularyAccess;
import javafx.application.Platform;
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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import at.tugraz.sw20asd.lang.model.Entry;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class EditVocab extends VBox {
    @FXML
    private VBox phrase_list;
    @FXML
    private VBox translation_list;
    @FXML
    private Label from_label;
    @FXML
    private Label to_label;
    @FXML
    private Button add_btn;
    @FXML
    private Button submit_btn;
    @FXML
    private Button return_btn;
    @FXML
    private Label user_info;
    @FXML
    private TextField category;
    private List<TextField> phrase_field_list = new ArrayList<TextField>();
    private List<TextField> translation_field_list = new ArrayList<TextField>();

    private Task<Integer> edittask;
    private VocabularyAccess vocab;
    private Vocabulary voc;

    private String category_string;
    private ObservableList<String> vocabulary_list = FXCollections.observableArrayList();
    private int id;
    FXMLLoader loader = new FXMLLoader();

    public EditVocab(VocabularyAccess vocab, Vocabulary voc){

        this.vocab = vocab;
        this.voc = voc;
        URL location = getClass().getResource("/edit.fxml");
        loader.setControllerFactory(c -> this);
        loader.setRoot(this);
        try {
            loader.load(location.openStream());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }


    public void initialize(){

        user_info.setVisible(false);
        category.setText(voc.getName());
        List<Entry> words = voc.getEntries();
        for(int counter = 0; counter < words.size(); counter++)
        {
            TextField phrase = new TextField();
            phrase.setText(words.get(counter).getPhrase());
            phrase.textProperty().addListener((observable, oldValue, newValue) -> {
                category_string = newValue;
            });
            phrase_field_list.add(phrase);
            phrase_list.getChildren().add(phrase);

            TextField translation = new TextField();
            translation.setText(words.get(counter).getTranslation());
            translation.textProperty().addListener((observable, oldValue, newValue) -> {
                category_string = newValue;
            });
            translation_field_list.add(phrase);
            translation_list.getChildren().add(translation);
        }

        category.textProperty().addListener((observable, oldValue, newValue) -> {
            category_string = newValue;
        });


        submit_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if(category.getText().isEmpty()){
                    updateUserInformation("category");
                }
                else if(!checkEntries())
                {
                    updateUserInformation("entry_missing");
                }
                else{
                    sendEditCommand();
                }
            }
        });

        return_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                clearEditVocab();
                Controller con = new Controller();
                getScene().setRoot(con);
            }
        });

        add_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                TextField phrase = new TextField();
                phrase.textProperty().addListener((observable, oldValue, newValue) -> {
                    category_string = newValue;
                });
                phrase_field_list.add(phrase);
                phrase_list.getChildren().add(phrase);

                TextField translation = new TextField();
                translation.textProperty().addListener((observable, oldValue, newValue) -> {
                    category_string = newValue;
                });
                translation_field_list.add(phrase);
                translation_list.getChildren().add(translation);
            }
        });
    }

    private void updateUserInformation(String code){
        user_info.setVisible(true);
        user_info.setTextFill(Color.RED);
        switch (code){
            case "edited_vocab":
                user_info.setText("Vocabulary edited!");
                break;
            case "missing_selection":
                user_info.setText("Please select from and to language");
                break;
            case "amount_missing":
                user_info.setText("Please fill out the amount");
                break;
            case "entry_missing":
                user_info.setText("Please check if you have entered" + "\n" + "a matching To for each From in the same line");
                break;
            case "category":
                user_info.setText("Please fill out the Name of your Vocabulary group");
                break;
            default:
                user_info.setText("Sorry, something went wrong");
        }
    }

    private void sendEditCommand(){

        user_info.setVisible(false);
        int amount = getAmountOfEntries();
        id = -1;

        Locale source_lang = voc.getSourceLanguage();
        Locale target_lang = voc.getTargetLanguage();

        Vocabulary vocabulary = new Vocabulary(null, category_string, source_lang, target_lang);

        for(int i = 0; (i + 1) < amount; i++)
        {
            String from = vocabulary_list.get(i);
            String to = vocabulary_list.get(++i);
            Entry entry = new Entry(from, to);
            vocabulary.addPhrase(entry);
        }

        edittask = new Task<Integer> () {
            @Override
            protected Integer call() throws Exception {
                int id = 0;
                // TODO Edit vocab
                id = vocab.addVocabulary(vocabulary);
                return id;
            }
        };

        edittask.stateProperty().addListener(((observableValue, oldState, newState)->{
            if(newState == Worker.State.CANCELLED){
                edittask.cancel();
            }
            if(newState == Worker.State.SUCCEEDED){
                id = edittask.getValue();

                if(id != -1){
                    Platform.runLater(() -> {
                        updateUserInformation("edited_vocab");
                    });
                }
                else{
                    Platform.runLater(() -> {
                        updateUserInformation("");
                    });
                }
            }
        }));

        Thread th = new Thread(edittask);
        th.setDaemon(true);
        th.start();
    }

    private int getAmountOfEntries(){
        int amount = 0;
        // TODO
        /*
        if(!from_field.getText().isEmpty() && !to_field.getText().isEmpty()){
            amount += 2;
            vocabulary_list.addAll(from_string, to_string);
        } */
        return amount;

    }

    private boolean checkEntries(){
        // TODO
        /*
        if((!from_field.getText().isEmpty() && to_field.getText().isEmpty()) ||
                (from_field.getText().isEmpty() && !to_field.getText().isEmpty())){
            return false;
        }
        */
        return  true;
    }

    private void clearEditVocab(){
        // TODO
        /*
        from_field.clear();
        to_field.clear();
         */
        from_label.setText("From:");
        to_label.setText("To:");

        vocabulary_list.clear();
        category.clear();
    }
}