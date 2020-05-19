package at.tugraz.sw20asd.lang.ui.Controller;

import at.tugraz.sw20asd.lang.model.Vocabulary;
import at.tugraz.sw20asd.lang.ui.VocabularyAccess;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import at.tugraz.sw20asd.lang.model.Entry;
import javafx.scene.paint.Color;

public class StudyVocab extends VBox {
    @FXML
    private VBox given_list;
    @FXML
    private VBox answer_list;
    @FXML
    private VBox study_list;
    @FXML
    private Button submit_btn;
    @FXML
    private Button return_btn;
    @FXML
    private Label user_info;
    @FXML
    private Label study_label;
    @FXML
    private Label given_label;
    @FXML
    private Label category;
    private List<TextField> study_field_list = new ArrayList<TextField>();
    private List<Label> given_label_list = new ArrayList<Label>();
    private List<Label> answer_label_list = new ArrayList<Label>();

    private int id;

    private VocabularyAccess vocab;
    private Vocabulary voc;
    private String study_language;
    FXMLLoader loader = new FXMLLoader();

    public StudyVocab(VocabularyAccess vocab, Vocabulary voc, String study_language) {
        this.vocab = vocab;
        this.voc = voc;
        this.study_language = study_language;
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
            category.setText(voc.getName());
            study_label.setText(study_language);
            if(study_language == voc.getSourceLanguage().toLanguageTag()){
                given_label.setText(voc.getSourceLanguage().toLanguageTag());
            }
            else {
                given_label.setText(voc.getTargetLanguage().toLanguageTag());
            }
            List<Entry> words = voc.getEntries();
            for (int counter = 0; counter < words.size(); counter++) {

                TextField study = new TextField();
                study.setId("study" + study_field_list.size());
                study_field_list.add(study);
                study_list.getChildren().add(study);

                String given_str;
                String answer_str;
                if(study_language == voc.getSourceLanguage().toString()){
                    given_str = words.get(counter).getPhrase();
                    answer_str = words.get(counter).getTranslation();
                }
                else{
                    given_str = words.get(counter).getTranslation();
                    answer_str = words.get(counter).getPhrase();
                }

                Label given = new Label(given_str);
                given.setId("given" + given_label_list.size());
                given_label_list.add(given);
                given_list.getChildren().add(given);

                Label answer = new Label(answer_str);
                answer.setId("answer" + answer_label_list.size());
                answer.setVisible(false);
                answer_label_list.add(answer);
                answer_list.getChildren().add(answer);
            }

            submit_btn.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    checkAnswer();
                }
            });

            return_btn.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                        OverviewVocabs overview = new OverviewVocabs(vocab);
                        getScene().setRoot(overview);
                }
            });
        }

        private void updateUserInformation (String code){
            user_info.setVisible(true);
            user_info.setTextFill(Color.RED);
            switch (code) {
                default:
                    user_info.setText("Sorry, something went wrong");
            }
        }

        private void checkAnswer(){ //TODO
            if(submit_btn.getText() == "Check Answer") {
                for (int counter = 0; counter < answer_label_list.size(); counter++) {
                    answer_label_list.get(counter).setVisible(true);
                }
                submit_btn.setText("Hide Answer");
            }
            else{
                for (int counter = 0; counter < answer_label_list.size(); counter++) {
                    answer_label_list.get(counter).setVisible(false);
                }
                submit_btn.setText("Check Answer");
            }
        }
    }


