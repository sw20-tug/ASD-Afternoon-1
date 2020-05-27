package at.tugraz.sw20asd.lang.ui.Controller;

import at.tugraz.sw20asd.lang.dto.EntryDto;
import at.tugraz.sw20asd.lang.dto.VocabularyDetailDto;
import at.tugraz.sw20asd.lang.ui.VocabularyAccess;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javafx.scene.paint.Color;

public class EditVocab extends VBox {
    @FXML
    private VBox phrase_list;
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
    @FXML
    private ComboBox<String> from_choice;
    @FXML
    private ComboBox<String> to_choice;

    private List<TextField> phrase_field_list = new ArrayList<TextField>();
    private List<TextField> translation_field_list = new ArrayList<TextField>();
    private List<EntryDto> words = new ArrayList<>();
    private HashMap<String, Locale> language_map = new HashMap<String, Locale>();

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
        user_info.setVisible(false);

        ObservableList<String> languages =
                FXCollections.observableArrayList("German", "English");

        from_choice.setItems(languages);
        to_choice.setItems(languages);

        language_map.put("German", Locale.GERMAN);
        language_map.put("English", Locale.ENGLISH);

        getVocabsGroup();

        from_choice.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!to_choice.getSelectionModel().isEmpty() &&
                    (to_choice.getSelectionModel().getSelectedItem().equals(newValue))) {

                updateUserInformation("equal_lang");
            } else {
                user_info.setVisible(false);
            }
        });

        to_choice.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!from_choice.getSelectionModel().isEmpty() &&
                    from_choice.getSelectionModel().getSelectedItem().equals(newValue)) {

                updateUserInformation("equal_lang");
            } else {
                user_info.setVisible(false);
            }
        });

        add_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                HBox entry = new HBox();
                entry.setMinHeight(30);
                entry.setAlignment(Pos.TOP_CENTER);
                TextField phrase = new TextField();
                phrase.setText("");
                Label label = new Label("");
                label.setMinWidth(20);
                phrase.setId("phrase" + phrase_field_list.size());
                setMargin(entry, new Insets(5, 0, 0, 0));
                phrase_field_list.add(phrase);
                phrase_list.getChildren().add(entry);
                entry.getChildren().add(phrase);
                entry.getChildren().add(label);

                TextField translation = new TextField();
                translation.setText("");
                translation.setId("translation" + translation_field_list.size());
                translation_field_list.add(translation);
                setMargin(translation, new Insets(0, 0, 0, 20));
                entry.getChildren().add(translation);
            }
        });


        submit_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if (category.getText().isEmpty()) {
                    updateUserInformation("category");
                } else if (!checkEntries()) {
                    updateUserInformation("entry_missing");
                } else if (from_choice.getSelectionModel().isEmpty()
                        || to_choice.getSelectionModel().isEmpty()) {
                    updateUserInformation("missing_selection");
                } else if (from_choice.getSelectionModel().getSelectedItem().equals(to_choice.getSelectionModel().getSelectedItem())) {
                    updateUserInformation("equal_lang");
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
            case "entry_missing":
                user_info.setText("Please leave no textfields empty");
                break;
            case "category":
                user_info.setText("Please fill out the Name of your Vocabulary group");
                break;
            case "equal_lang":
                user_info.setText("Please select another language");
                break;
            default:
                user_info.setText("Sorry, something went wrong");
        }
    }

    private void getVocabsGroup() {
        //get Vocabulary group
        if (origin_scene == 3) {

            category.setText("Pets");

            from_choice.getSelectionModel().select(0);
            to_choice.getSelectionModel().select(1);
            words.add(new EntryDto("Hund", "dog"));
            words.add(new EntryDto("Katze", "cat"));
            words.add(new EntryDto("Fisch", "fish"));
            initiateGUI(words);
        } else {
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

                            from_choice.getSelectionModel().select(0);
                            to_choice.getSelectionModel().select(1);

                            for (EntryDto e : voc.getEntries()) {
                                words.add(e);
                            }
                            initiateGUI(words);
                        });
                    }
                }
            }));
            Thread th = new Thread(getVocabsTask);
            th.setDaemon(true);
            th.start();
        }
    }

    private void initiateGUI(List<EntryDto> words) {
        for (int counter = 0; counter < words.size(); counter++) {
            HBox entry = new HBox();
            entry.setMinHeight(30);
            entry.setAlignment(Pos.TOP_CENTER);
            TextField phrase = new TextField();
            phrase.setText(words.get(counter).getPhrase());
            phrase.setId("phrase" + phrase_field_list.size());
            Label label = new Label("");
            label.setMinWidth(20);
            setMargin(entry, new Insets(5, 0, 0, 0));
            phrase_field_list.add(phrase);
            phrase_list.getChildren().add(entry);
            entry.getChildren().add(phrase);
            entry.getChildren().add(label);

            TextField translation = new TextField();
            translation.setText(words.get(counter).getTranslation());
            translation.setId("translation" + translation_field_list.size());
            translation_field_list.add(translation);
            entry.getChildren().add(translation);
        }
    }

    private void sendEditCommand() {

        user_info.setVisible(false);
        List<EntryDto> entry_list = getEntryList();

        String from_lang = from_choice.getSelectionModel().getSelectedItem();
        String to_lang = to_choice.getSelectionModel().getSelectedItem();

        Locale source_lang = language_map.get(from_lang);
        Locale target_lang = language_map.get(to_lang);

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
                        if (origin_scene == 1) {
                            OverviewVocabs overview = new OverviewVocabs(vocab);
                            getScene().setRoot(overview);
                        } else {
                            EditVocabSelection editvocabs = new EditVocabSelection(vocab);
                            getScene().setRoot(editvocabs);
                        }
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
                    (phrase_field_list.get(counter).getText().isEmpty() && !translation_field_list.get(counter).getText().isEmpty())) {
                return false;
            }
        }
        return true;
    }
}