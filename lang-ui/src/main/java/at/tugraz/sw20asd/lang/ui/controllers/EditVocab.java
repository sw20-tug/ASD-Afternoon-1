package at.tugraz.sw20asd.lang.ui.controllers;

import at.tugraz.sw20asd.lang.EntryDto;
import at.tugraz.sw20asd.lang.VocabularyDetailDto;
import at.tugraz.sw20asd.lang.ui.dataaccess.VocabularyAccess;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private List<EntryDto> words = new ArrayList<>();

    private Task<VocabularyDetailDto> getVocabsTask;
    private Task<Integer> edittask1;
    private Task<Integer> edittask2;
    private VocabularyAccess vocab;
    private VocabularyDetailDto voc;
    private int origin_scene;

    private long id;
    FXMLLoader loader = new FXMLLoader();

    public EditVocab(VocabularyAccess vocab, long id, int origin_scene) {
        this.vocab = vocab;
        this.voc = voc;
        this.id = id;
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


    public void initialize() {
        getVocabsGroup();
        user_info.setVisible(false);
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
                if (category.getText().isEmpty()) {
                    updateUserInformation("category");
                } else if (!checkEntries()) {
                    updateUserInformation("entry_missing");
                } else {
                    sendEditCommand();
                    user_info.setVisible(true);
                }
            }
        });

        return_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if (origin_scene == 1) {
                    OverviewVocabs overview = new OverviewVocabs(vocab);
                    getScene().setRoot(overview);
                } else {
                    EditVocabSelection editvocabs = new EditVocabSelection(vocab);
                    getScene().setRoot(editvocabs);
                }
            }
        });
    }

    private void updateUserInformation(String code) {
        user_info.setVisible(true);
        user_info.setTextFill(Color.RED);
        switch (code) {
            case "edited_vocab":
                user_info.setText("Vocabulary edited!");
                break;
            case "amount_missing":
                user_info.setText("Please fill out the amount");
                break;
            case "entry_missing":
                user_info.setText("Please leave no textfields empty");
                break;
            case "empty_textfields":
                user_info.setText("Please check leave no textfields empty");
                break;
            case "category":
                user_info.setText("Please fill out the Name of your Vocabulary group");
                break;
            default:
                user_info.setText("Sorry, something went wrong");
        }
    }

    private void getVocabsGroup() {
        //get Vocabulary group
        getVocabsTask = new Task<VocabularyDetailDto>() {
            @Override
            protected VocabularyDetailDto call() throws Exception {
                voc = vocab.getVocabulary(id);
                return voc;
            }
        };

        getVocabsTask.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.CANCELLED || newValue == Worker.State.FAILED) {
                Platform.runLater(() -> {
                    updateUserInformation("");
                });
                getVocabsTask.cancel();
            }

            if (newValue == Worker.State.SUCCEEDED) {
                if (voc == null) {
                    Platform.runLater(() -> {
                        updateUserInformation("No words added");
                    });
                } else {
                    Platform.runLater(() -> {
                        category.setText(voc.getName());
                        for (EntryDto e : voc.getEntries()) {
                            words.add(e);
                        }
                        for (int counter = 0; counter < words.size(); counter++) {
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
                    });
                }
            }
        }));
        Thread th = new Thread(getVocabsTask);
        th.setDaemon(true);
        th.start();
    }

    private void sendEditCommand() {

        user_info.setVisible(false);
        List<EntryDto> entry_list = getEntryList();

        Locale source_lang = voc.getSourceLanguage();
        Locale target_lang = voc.getTargetLanguage();
        VocabularyDetailDto edited_vocabulary = new VocabularyDetailDto(null, category.getText(), source_lang, target_lang);

        for (int counter = 0; counter < entry_list.size(); counter++) {
            edited_vocabulary.addEntry(entry_list.get(counter));
        }

        edittask1 = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                int id = 0;
                vocab.deleteVocabulary(voc.getId());
                id = vocab.addVocabulary(edited_vocabulary);
                return id;
            }
        };

        edittask1.stateProperty().addListener(((observableValue, oldState, newState) -> {
            if (newState == Worker.State.CANCELLED) {
                edittask1.cancel();
            }
            if (newState == Worker.State.SUCCEEDED) {
                id = edittask1.getValue();

                if (id != -1) {
                    Platform.runLater(() -> {
                        updateUserInformation("edited_vocab");
                    });
                } else {
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

    private List<EntryDto> getEntryList() {
        List<EntryDto> entry_list = new ArrayList<>();
        for (int counter = 0; counter < translation_field_list.size(); counter++) {
            if (!phrase_field_list.get(counter).getText().isEmpty() && !translation_field_list.get(counter).getText().isEmpty()) {
                entry_list.add(new EntryDto(phrase_field_list.get(counter).getText(), translation_field_list.get(counter).getText()));
            }
        }
        return entry_list;
    }

    private boolean checkEntries() {
        for (int counter = 0; counter < phrase_field_list.size(); counter++) {
            if ((!phrase_field_list.get(counter).getText().isEmpty() && translation_field_list.get(counter).getText().isEmpty()) ||
                 (phrase_field_list.get(counter).getText().isEmpty() && !translation_field_list.get(counter).getText().isEmpty()) ||
                 (phrase_field_list.get(counter).getText().isEmpty() && translation_field_list.get(counter).getText().isEmpty())){
                return false;
            }
        }
        return true;
    }
}