package at.tugraz.sw20asd.lang.ui.controllers;


import at.tugraz.sw20asd.lang.EntryDto;
import at.tugraz.sw20asd.lang.VocabularyDetailDto;
import at.tugraz.sw20asd.lang.ui.dataaccess.VocabularyAccess;
import at.tugraz.sw20asd.lang.ui.models.VocabularySelectionModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.*;

public class Testing extends VBox {

    private VocabularyAccess vocab;
    private int counter = 0;
    private String selectedLang;
    private String userInputString;

    private ObservableList<VocabularySelectionModel> Vocabularies;
    private ObservableList<String> languages = FXCollections.observableArrayList();

    private Map<Integer, EntryDto> entries = new HashMap<>();
    private Map<Long, Integer> counterMap = new HashMap<>();
    private Map<Integer, String> languageMap = new HashMap<>();

    private Task<List<VocabularyDetailDto>> getVocabsTask;

    private List<VocabularyDetailDto> vocabularyList = new ArrayList<>();
    private List<Integer> randomizedIntegerList = new ArrayList<>();

    private EntryDto currentTestVocabulary;

    @FXML
    private Label title;
    @FXML
    private Label lang_label;
    @FXML
    private ComboBox<String> language_choice;
    @FXML
    private Button start_button;
    @FXML
    private Label current_vocab;
    @FXML
    private TextField input_field;
    @FXML
    private Button return_btn;
    @FXML
    private Button continue_btn;
    @FXML
    private Label user_info;
    @FXML
    private ProgressIndicator progress_bar;


    public Testing(VocabularyAccess vocab, ObservableList<VocabularySelectionModel> selection) {
        this.vocab = vocab;
        this.Vocabularies = FXCollections.observableArrayList(selection);

        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(c -> this);
        loader.setRoot(this);
        try {
            loader.load(getClass().getResource("/testing.fxml").openStream());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void initialize() {
        buttonVisibleHandler("initialize");
        getLanguages();

        language_choice.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            selectedLang = newValue;
            buttonVisibleHandler("language");
            getEntryList();
            updateUserInformation("press_start");
        });

        start_button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                user_info.setVisible(false);
                startTraining();
                buttonVisibleHandler("start");
            }
        });

        input_field.textProperty().addListener((observable, oldValue, newValue) -> {
            userInputString = newValue;
        });

        return_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                clear();
                SelectionOverview con = new SelectionOverview(vocab);
                getScene().setRoot(con);
            }
        });

        continue_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                validateAnswer();
                input_field.clear();
                startTraining();
            }
        });
    }

    //function to get the entries of the selected vocabulary groups
    private void getEntryList() {
        entries = new HashMap<>();
        progress_bar.setVisible(true);
        List<Long> ids = new ArrayList<>();

        for (VocabularySelectionModel v : Vocabularies) {
            ids.add(v.getVocabularyId());
        }

        getVocabsTask = new Task<>() {
            @Override
            protected List<VocabularyDetailDto> call() throws Exception {
                vocabularyList = vocab.getVocabularyList(ids);
                return vocabularyList;
            }
        };

        getVocabsTask.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.CANCELLED || newValue == Worker.State.FAILED) {
                Platform.runLater(() -> {
                    updateUserInformation("");
                });
                getVocabsTask.cancel();
                progress_bar.progressProperty().unbind();
            }

            if (newValue == Worker.State.SUCCEEDED) {
                if (vocabularyList != null) {
                    int length = 0;
                    for (VocabularyDetailDto entryDto : vocabularyList) {
                        Set<EntryDto> entry = entryDto.getEntries();
                        for (EntryDto curEntry : entry) {
                            entries.put(length, curEntry);
                            languageMap.put(length, entryDto.getSourceLanguage().toString());
                            counterMap.put(curEntry.getId(), 0);
                            length++;
                        }
                    }
                    getRandomizeList(length);
                    progress_bar.progressProperty().unbind();
                    progress_bar.setVisible(false);
                    Platform.runLater(() -> {
                        start_button.setDisable(false);
                    });
                }
            }
        }));
        progress_bar.progressProperty().bind(getVocabsTask.progressProperty());
        Thread th = new Thread(getVocabsTask);
        th.setDaemon(true);
        th.start();

    }

    //check if answer was correct
    //check which part of the entry is selected as train language -> phrase vs translation
    // if not -> increase repetition counter
    // if -> remove the currently used randomized integer from the list -> don't test correct vocabularies twice
    private void validateAnswer() {
        if (languageMap.get(randomizedIntegerList.get(counter)).equals(selectedLang)) {
            if (userInputString == null || !userInputString.equals(currentTestVocabulary.getTranslation())) {
                increaseRepetitions();
                setOffset("failed");
            } else {
                randomizedIntegerList.remove(counter);
                setOffset("removed");
            }
        } else {
            if (userInputString == null || !userInputString.equals(currentTestVocabulary.getPhrase())) {
                increaseRepetitions();
                setOffset("failed");
            } else {
                randomizedIntegerList.remove(counter);
                setOffset("removed");
            }
        }
    }

    //function to adjust randomized integer list, depending on the result
    private void setOffset(String flag) {
        switch (flag) {
            case "removed":
                counter = randomizedIntegerList.size() - 1;
                break;
            case "failed":
                if (randomizedIntegerList.size() > 1 && counter > 0)
                    counter--;
                else {
                    Collections.reverse(randomizedIntegerList);
                    counter = randomizedIntegerList.size() - 1;
                }
                break;
        }
    }

    //wrong answer -> repetition counter have to be increased
    private void increaseRepetitions() {
        for (Map.Entry<Integer, EntryDto> entry : entries.entrySet()) {
            Long currentEntryId = entry.getValue().getId();

            if (currentEntryId.equals(currentTestVocabulary.getId())) {
                counterMap.put(entry.getValue().getId(), counterMap.get(currentEntryId) + 1);
            }
        }
    }

    //set the testing vocabulary on gui
    private void setCorrectVocabSet(EntryDto entry) {
        if (languageMap.get(randomizedIntegerList.get(counter)).equals(selectedLang)) {
            current_vocab.setText(entry.getPhrase());
        } else {
            current_vocab.setText(entry.getTranslation());
        }
        currentTestVocabulary = entry;
    }

    //start or go to next test entry
    private void startTraining() {
        if (randomizedIntegerList.size() > 0) {
            EntryDto entry = entries.get(randomizedIntegerList.get(counter));
            setCorrectVocabSet(entry);
        } else {
            System.out.println("change needed");
            //TODO switch to result page
        }

    }

    //create randomized list
    private void getRandomizeList(int upperBound) {

        List<Integer> tempList = new ArrayList<>();
        for (int i = 0; i < upperBound; i++) {
            tempList.add(i);
        }

        Random random = new Random();
        for (int i = 0; i < upperBound; i++) {
            int selection = random.nextInt(tempList.size());
            randomizedIntegerList.add(tempList.get(selection));
            tempList.remove(selection);
        }
    }

    //get possible language for the choiceBox
    private void getLanguages() {
        String source_lang = Vocabularies.get(0).getVocabularySrc();
        String target_lang = Vocabularies.get(0).getVocabularyTarget();

        languages.addAll(source_lang, target_lang);
        language_choice.setItems(languages);
    }

    //user information
    private void updateUserInformation(String code) {
        user_info.setVisible(true);
        user_info.setTextFill(Color.RED);
        switch (code) {
            case "press_start":
                user_info.setText("Please press the start button to continue!");
                user_info.setTextFill(Color.GREEN);
                break;
            default:
                user_info.setText("Sorry, something went wrong");
        }
    }

    //just to keep the overview in the functions, only changes the visibility and access possibility
    private void buttonVisibleHandler(String flag) {
        switch (flag) {
            case "initialize":
                continue_btn.setDisable(true);
                start_button.setDisable(true);
                user_info.setVisible(false);
                input_field.setVisible(false);
                current_vocab.setVisible(false);
                break;
            case "language":
                language_choice.setDisable(true);
                start_button.setDisable(true);
                input_field.setVisible(false);
                current_vocab.setVisible(false);
                break;
            case "start":
                start_button.setDisable(true);
                continue_btn.setDisable(false);
                language_choice.setDisable(true);
                input_field.setVisible(true);
                current_vocab.setVisible(true);
                break;
        }
    }

    private void clear() {
        Vocabularies.clear();
        if (vocabularyList.size() > 0) {
            selectedLang = null;
            userInputString = null;
            counter = 0;
            languages.clear();
            entries.clear();
            counterMap.clear();
            languageMap.clear();
            getVocabsTask.cancel();
            vocabularyList.clear();
            randomizedIntegerList.clear();
            user_info.setVisible(false);
        }
    }
}