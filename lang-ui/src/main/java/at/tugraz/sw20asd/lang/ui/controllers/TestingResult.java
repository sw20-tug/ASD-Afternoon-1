package at.tugraz.sw20asd.lang.ui.controllers;

import at.tugraz.sw20asd.lang.EntryDto;
import at.tugraz.sw20asd.lang.ui.dataaccess.VocabularyAccess;
import at.tugraz.sw20asd.lang.ui.models.TestingResultModel;
import at.tugraz.sw20asd.lang.ui.models.VocabularySelectionModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class TestingResult extends VBox {


    private VocabularyAccess vocab;
    private ObservableList<VocabularySelectionModel> Vocabularies;
    private Map<Integer, EntryDto> entries;
    private Map<Long, Integer> counterMap;

    @FXML
    private Label title;
    @FXML
    private Button return_btn;
    @FXML
    private Button restart_btn;
    @FXML
    private Label user_info;
    @FXML
    private TableView<TestingResultModel> table_result;
    @FXML
    private TableColumn<TestingResultModel, String> phraseColumn;
    @FXML
    private TableColumn<TestingResultModel, String> translationColumn;
    @FXML
    private TableColumn<TestingResultModel, Integer> repetitionColumn;

    public TestingResult(VocabularyAccess vocab, ObservableList<VocabularySelectionModel> selection,
                         Map<Integer, EntryDto> entries, Map<Long, Integer> counterMap) {
        this.vocab = vocab;
        this.Vocabularies = FXCollections.observableArrayList(selection);
        this.entries = entries;
        this.counterMap = counterMap;

        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(c -> this);
        loader.setRoot(this);
        try {
            loader.load(getClass().getResource("/testingResult.fxml").openStream());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void initialize() {
        user_info.setVisible(false);
        fillTable();

        return_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                clear();
                SelectionOverview con = new SelectionOverview(vocab);
                getScene().setRoot(con);
            }
        });

        restart_btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                clear();
                Testing test = new Testing(vocab, Vocabularies);
                getScene().setRoot(test);
            }
        });

    }

    private void fillTable() {
        phraseColumn.setCellValueFactory(new PropertyValueFactory<>("phrase"));
        translationColumn.setCellValueFactory(new PropertyValueFactory<>("translation"));
        repetitionColumn.setCellValueFactory(new PropertyValueFactory<>("repetitions"));

        ObservableList<TestingResultModel> content = FXCollections.observableArrayList();

        for (Map.Entry<Integer, EntryDto> entry : entries.entrySet()) {
            EntryDto current = entry.getValue();
            long id = current.getId();
            for (Map.Entry<Long, Integer> e : counterMap.entrySet()) {
                if (e.getKey() == id) {
                    content.add(new TestingResultModel(entry.getValue().getPhrase(), entry.getValue().getTranslation(), e.getValue()));
                }
            }
        }
        content.sort((a, b) -> b.getRepetitions().compareTo(a.getRepetitions()));
        table_result.setItems(content);
    }

    private void clear() {
        entries.clear();
        counterMap.clear();
        user_info.setVisible(false);
        table_result.getItems().clear();
    }
}
