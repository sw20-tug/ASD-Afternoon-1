package at.tugraz.sw20asd.lang.ui.Controller;

import at.tugraz.sw20asd.lang.dto.EntryDto;
import at.tugraz.sw20asd.lang.dto.VocabularyDetailDto;
import at.tugraz.sw20asd.lang.ui.VocabularyAccess;
import at.tugraz.sw20asd.lang.ui.models.VocabularySelectionModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class StudyVocab extends VBox {
    @FXML
    private VBox given_list;
    @FXML
    private VBox answer_list;
    @FXML
    private VBox show_answer_list;
    @FXML
    private Button switch_btn;
    @FXML
    private Button return_btn;
    @FXML
    private Button show_all_btn;
    @FXML
    private Label study_label;
    @FXML
    private AnchorPane anchor_pane;
    @FXML
    private Label user_info;
    @FXML
    private Label choice_label;
    @FXML
    private Label given_label;
    @FXML
    private ComboBox<String> language_choice;
    @FXML
    private Button start_btn;

    private List<Button> answer_button_list = new ArrayList<>();
    private List<Label> given_label_list = new ArrayList<>();
    private List<Label> answer_label_list = new ArrayList<>();

    private List<String> languages_list = new ArrayList<>();
    private String language1;
    private String language2;
    private List<EntryDto> words = new ArrayList<>();

    private Task<List<VocabularyDetailDto>> getVocabsTask;
    private List<VocabularyDetailDto> vocabulary_list = new ArrayList<>();

    private VocabularyAccess vocab;
    private ObservableList<VocabularySelectionModel> voc;
    private String study_language;
    FXMLLoader loader = new FXMLLoader();

    public StudyVocab(VocabularyAccess vocab, ObservableList<VocabularySelectionModel> voc) {
        this.vocab = vocab;
        this.voc = voc;
        URL location = getClass().getResource("/study.fxml");
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
        study_label.setText(study_language);
        show_all_btn.setVisible(false);
        switch_btn.setVisible(false);
        given_label.setVisible(false);
        study_label.setVisible(false);
        language1 = voc.get(0).getVocabularySrc();
        language2 = voc.get(0).getVocabularyTarget();
        languages_list.add(language1);
        languages_list.add(language2);
        ObservableList<String> languages = FXCollections.observableList(languages_list);
        language_choice.setItems(languages);

        start_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if (language_choice.getSelectionModel().isEmpty()) {
                    user_info.setVisible(true);
                    user_info.setText("Please choose a language!");
                } else {
                    study_language = language_choice.getSelectionModel().getSelectedItem();
                    study_label.setText(study_language);
                    getEntryList();
                }
            }
        });

        return_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                OverviewVocabs overview = new OverviewVocabs(vocab);
                getScene().setRoot(overview);
            }
        });

    }

    private void showAllAnswers() {
        if (show_all_btn.getText().equals("Show All Answers")) {
            for (int counter = 0; counter < given_label_list.size(); counter++) {
                answer_label_list.get(counter).setVisible(true);
                answer_button_list.get(counter).setText("Hide Answer");
                show_all_btn.setText("Hide All Answers");
            }
        } else {
            for (int counter = 0; counter < given_label_list.size(); counter++) {
                answer_label_list.get(counter).setVisible(false);
                answer_button_list.get(counter).setText("Show Answer");
                show_all_btn.setText("Show All Answers");
            }
        }
    }

    private void switchLanguage() {
        show_all_btn.setText("Hide All Answers");
        showAllAnswers();
        for (int counter = 0; counter < given_label_list.size(); counter++) {
            String new_answer = given_label_list.get(counter).getText();
            String new_given = answer_label_list.get(counter).getText();
            String new_given_language = study_label.getText();
            String new_study_language = given_label.getText();

            given_label_list.get(counter).setText(new_given);
            answer_label_list.get(counter).setText(new_answer);
            given_label.setText(new_given_language);
            study_label.setText(new_study_language);
        }
    }

    private void populate() {
        user_info.setVisible(false);
        show_all_btn.setVisible(true);
        switch_btn.setVisible(true);
        given_label.setVisible(true);
        study_label.setVisible(true);
        anchor_pane.getChildren().remove(choice_label);
        anchor_pane.getChildren().remove(language_choice);
        anchor_pane.getChildren().remove(start_btn);

        int min_height = 30;
        int margin = 10;
        for (int counter = 0; counter < words.size(); counter++) {

            String given_str;
            String answer_str;
            if (study_language.equals("de")) {
                given_str = words.get(counter).getTranslation();
                answer_str = words.get(counter).getPhrase();
            } else {
                given_str = words.get(counter).getPhrase();
                answer_str = words.get(counter).getTranslation();
            }

            Label given = new Label(given_str);
            given.setId("given" + given_label_list.size());
            given.setMinHeight(min_height);
            setMargin(given, new Insets(margin, 0, 0, 0));
            given_label_list.add(given);
            given_list.getChildren().add(given);

            Label answer = new Label(answer_str);
            answer.setId("answer" + answer_label_list.size());
            answer.setVisible(false);
            answer.setMinHeight(min_height);
            setMargin(answer, new Insets(10, 0, 0, 0));
            answer_label_list.add(answer);
            answer_list.getChildren().add(answer);

            Button show_answer = new Button();
            show_answer.setText("Show Answer");
            show_answer.setId("btn" + answer_button_list.size());
            show_answer.setMinHeight(min_height);
            setMargin(show_answer, new Insets(10, 0, 0, 0));
            show_answer.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    if (show_answer.getText().equals("Show Answer")) {
                        answer.setVisible(true);
                        show_answer.setText("Hide Answer");
                    } else {
                        answer.setVisible(false);
                        show_answer.setText("Show Answer");
                    }
                }
            });
            answer_button_list.add(show_answer);
            show_answer_list.getChildren().add(show_answer);
        }

        switch_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                switchLanguage();
            }
        });

        show_all_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                showAllAnswers();
            }
        });
    }

    private void getEntryList() {
        //get Vocabulary group

        getVocabsTask = new Task<List<VocabularyDetailDto>>() {
            @Override
            protected List<VocabularyDetailDto> call() throws Exception {
                for(VocabularySelectionModel Model : voc) {
                    vocabulary_list.add(vocab.getVocabulary(Model.getVocabularyId()));
                }
                return vocabulary_list;
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
                if (vocabulary_list == null) {
                    Platform.runLater(() -> {
                        updateUserInformation("No words added");
                    });
                } else {
                    Platform.runLater(() -> {
                        for (VocabularyDetailDto v : vocabulary_list) {
                            for (EntryDto e : v.getEntries()) {
                                if (v.getSourceLanguage().toString().equals(language1)) {
                                    words.add(e);
                                } else {
                                    words.add(new EntryDto(e.getTranslation(), e.getPhrase()));
                                }
                            }
                        }
                        System.out.println(words);
                        populate();
                    });
                }
            }
        }));
        Thread th = new Thread(getVocabsTask);
        th.setDaemon(true);
        th.start();
    }

    private void updateUserInformation(String s) {
    }
}
