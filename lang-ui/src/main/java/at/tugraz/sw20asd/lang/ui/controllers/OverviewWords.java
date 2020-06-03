package at.tugraz.sw20asd.lang.ui.controllers;

import at.tugraz.sw20asd.lang.VocabularyDetailDto;
import at.tugraz.sw20asd.lang.ui.models.EntryModel;
import at.tugraz.sw20asd.lang.ui.dataaccess.VocabularyAccess;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.stream.Collectors;

public class OverviewWords extends VBox {

    private final Long index;
    @FXML
    private Label title;
    @FXML
    private Button add_btn;
    @FXML
    private Button edit_btn;
    @FXML
    private Button return_btn;
    @FXML
    private Label user_info;

    @FXML
    public TableColumn<EntryModel, String> phraseColumn;

    @FXML
    public TableColumn<EntryModel, String> translationColumn;

    @FXML
    private TableView<EntryModel> table;

    private VocabularyAccess vocab;
    private Task<VocabularyDetailDto> getVocabsTask;
    private VocabularyDetailDto v;

    public OverviewWords(VocabularyAccess vocab, Long index) {
        this.vocab = vocab;
        this.index = index;
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(c -> this);
        loader.setRoot(this);
        try {
            loader.load(getClass().getResource("/overview-words.fxml").openStream());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void initialize() {
        getVocabsGroup();


        add_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                AddVocab add = new AddVocab(vocab);
                getScene().setRoot(add);
            }
        });

        edit_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                EditVocab edit = new EditVocab(vocab, v.getId(), EditVocab.Mode.OVERVIEW);
                getScene().setRoot(edit);
            }
        });

        return_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                clearOverviewWords();
                OverviewVocabs overview_vocabs = new OverviewVocabs(vocab);
                getScene().setRoot(overview_vocabs);
            }
        });


    }

    private void updateUserInformation(String code) {

        user_info.setVisible(true);
        user_info.setTextFill(Color.RED);
        switch (code) {
            case "No words added":
                user_info.setText("You haven`t added any word on that vocab!");
                break;
            default:
                user_info.setText("Sorry, something went wrong");
        }
    }

    private void getVocabsGroup() {
        //get Vocabulary group
        getVocabsTask = new Task<>() {
            @Override
            protected VocabularyDetailDto call() throws Exception {
                v = vocab.getVocabulary(index);
                return v;
            }
        };

        getVocabsTask.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.CANCELLED || newValue == Worker.State.FAILED ) {
                Platform.runLater(() -> {
                    updateUserInformation("");
                });
                getVocabsTask.cancel();
            }

            if (newValue == Worker.State.SUCCEEDED) {
                if (v == null) {
                    Platform.runLater(() -> {
                        updateUserInformation("No words added");
                    });
                } else {
                    Platform.runLater(() -> {
                        phraseColumn.setText(v.getSourceLanguage().toString());
                        translationColumn.setText(v.getTargetLanguage().toString());

                        phraseColumn.setCellValueFactory(new PropertyValueFactory<>("Phrase"));
                        translationColumn.setCellValueFactory(new PropertyValueFactory<>("Translation"));

                        table.setItems(FXCollections.observableArrayList(
                                v.getEntries()
                                        .stream()
                                        .map(EntryModel::fromEntry).collect(Collectors.toList())));
                    });
                }
            }
        }));

        Thread th = new Thread(getVocabsTask);
        th.setDaemon(true);
        th.start();
    }

    private void clearOverviewWords() {
        phraseColumn.setText("");
        translationColumn.setText("");
    }
}