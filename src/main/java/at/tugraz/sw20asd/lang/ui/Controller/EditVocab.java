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

    private Task<Integer> edittask;
    private VocabularyAccess vocab;
    private Vocabulary voc;

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
            phrase_field_list.add(phrase);
            phrase_list.getChildren().add(phrase);

            TextField translation = new TextField();
            translation.setText(words.get(counter).getTranslation());
            translation_field_list.add(translation);
            translation_list.getChildren().add(translation);
        }

        add_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                TextField phrase = new TextField();
                phrase.setText("");
                phrase_field_list.add(phrase);
                phrase_list.getChildren().add(phrase);

                TextField translation = new TextField();
                translation.setText("");
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
                    sendEditCommand();
                }
            }
        });

        return_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                OverviewWords overview = new OverviewWords(vocab, voc.getID());
                getScene().setRoot(overview);
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
        Vocabulary vocabulary = new Vocabulary(voc.getID(), category.getText(), source_lang, target_lang);

        for(int counter = 0; counter < entry_list.size(); counter++)
        {
            vocabulary.addPhrase(entry_list.get(counter));
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

    private List<Entry> getEntryList(){
        List<Entry> entry_list = new ArrayList<>();
        for(int counter = 0; counter < translation_field_list.size(); counter++)
        {
            if(!phrase_field_list.get(counter).getText().isEmpty() && !translation_field_list.get(counter).getText().isEmpty()){
                entry_list.add(new Entry(phrase_field_list.get(counter).getText(), translation_field_list.get(counter).getText()));

                System.out.println(translation_field_list.get(counter).getText());
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