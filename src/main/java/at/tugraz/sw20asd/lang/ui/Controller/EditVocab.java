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

    private Task<Integer> edittask1;
    private Task<Integer> edittask2;
    private VocabularyAccess vocab;
    private Vocabulary voc;
    private int origin_scene;

    private int id;
    FXMLLoader loader = new FXMLLoader();

    public EditVocab(VocabularyAccess vocab, Vocabulary voc, int origin_scene){
        this.vocab = vocab;
        this.voc = voc;
        this.origin_scene = origin_scene;
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
            phrase.setId("phrase" + phrase_field_list.size());
            phrase_field_list.add(phrase);
            phrase_list.getChildren().add(phrase);

            TextField translation = new TextField();
            translation.setText(words.get(counter).getTranslation());
            translation.setId("translation" + translation_field_list.size());
            translation_field_list.add(translation);
            translation_list.getChildren().add(translation);
        }

        add_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                TextField phrase = new TextField();
                phrase.setText("");
                phrase.setId("phrase" + phrase_field_list.size());
                phrase_field_list.add(phrase);
                phrase_list.getChildren().add(phrase);

                TextField translation = new TextField();
                translation.setText("");
                translation.setId("translation" + translation_field_list.size());
                translation_field_list.add(translation);
                translation_list.getChildren().add(translation);
            }
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
                    updateUserInformation("edited_vocab");
                    sendEditCommand();
                    user_info.setVisible(true);
                }
            }
        });

        return_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if(origin_scene == 1){
                    OverviewVocabs overview = new OverviewVocabs(vocab);
                    getScene().setRoot(overview);
                }
                else{
                    EditVocabSelection editvocabs = new EditVocabSelection(vocab);
                    getScene().setRoot(editvocabs);
                }
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
        List<Entry> entry_list = getEntryList();

        Locale source_lang = voc.getSourceLanguage();
        Locale target_lang = voc.getTargetLanguage();
        Vocabulary edited_vocabulary = new Vocabulary(voc.getID(), category.getText(), source_lang, target_lang);

        for(int counter = 0; counter < entry_list.size(); counter++)
        {
            edited_vocabulary.addPhrase(entry_list.get(counter));
        }

        edittask1 = new Task<Integer> () {
            @Override
            protected Integer call() throws Exception {
                int id = 0;
                id = vocab.deleteVocabulary(voc.getID());
                return id;
            }
        };

        edittask1.stateProperty().addListener(((observableValue, oldState, newState)->{
            if(newState == Worker.State.CANCELLED){
                edittask1.cancel();
            }
            if(newState == Worker.State.SUCCEEDED){
                id = edittask1.getValue();

                if(id != -1){
                    Platform.runLater(() -> {
                        addVocabulary(edited_vocabulary);
                    });
                }
                else{
                    Platform.runLater(() -> {
                        updateUserInformation("");
                    });
                }
            }
        }));

        Thread th = new Thread(edittask1);
        th.setDaemon(true);
        th.start();
    }

    private void addVocabulary(Vocabulary vocabulary){
        user_info.setVisible(false);

        edittask2 = new Task<Integer> () {
            @Override
            protected Integer call() throws Exception {
                int id = 0;
                id = vocab.addVocabulary(vocabulary);
                return id;
            }
        };

        edittask2.stateProperty().addListener(((observableValue, oldState, newState)->{
            if(newState == Worker.State.CANCELLED){
                edittask2.cancel();
            }
            if(newState == Worker.State.SUCCEEDED){
                id = edittask2.getValue();
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
        Thread th = new Thread(edittask2);
        th.setDaemon(true);
        th.start();
    }

    private List<Entry> getEntryList(){
        List<Entry> entry_list = new ArrayList<>();
        for(int counter = 0; counter < translation_field_list.size(); counter++)
        {
            if(!phrase_field_list.get(counter).getText().isEmpty() && !translation_field_list.get(counter).getText().isEmpty()){
                entry_list.add(new Entry(phrase_field_list.get(counter).getText(), translation_field_list.get(counter).getText()));
            }
        }
        return entry_list;
    }

    private boolean checkEntries(){
        for(int counter = 0; counter < phrase_field_list.size(); counter++)
        {
            if((!phrase_field_list.get(counter).getText().isEmpty() && translation_field_list.get(counter).getText().isEmpty()) ||
                    (phrase_field_list.get(counter).getText().isEmpty() && !translation_field_list.get(counter).getText().isEmpty())){
                return false;
            }
        }
        return true;
    }
}