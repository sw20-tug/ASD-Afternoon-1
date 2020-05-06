package at.tugraz.sw20asd.lang.ui.Controller;

import at.tugraz.sw20asd.lang.model.Vocabulary;
import at.tugraz.sw20asd.lang.ui.VocabularyAccess;
import at.tugraz.sw20asd.lang.ui.models.EntryModel;
import javafx.collections.FXCollections;
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
import java.util.stream.Collectors;

public class OverviewWords extends VBox {

    private final Integer index;
    @FXML
    private Label title;
    @FXML
    private Button add_btn;
    @FXML
    private Button edit_btn;
    @FXML
    private Button return_btn;

    @FXML
    public TableColumn<EntryModel, String> phraseColumn;

    @FXML
    public TableColumn<EntryModel, String> translationColumn;

    @FXML
    private TableView<EntryModel> table;

    private VocabularyAccess vocab;

    public OverviewWords(VocabularyAccess vocab, Integer index) {
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
        Vocabulary v = vocab.getVocabulary(index);

        phraseColumn.setText(v.getSourceLanguage().toString());
        translationColumn.setText(v.getTargetLanguage().toString());

        phraseColumn.setCellValueFactory(new PropertyValueFactory<>("Phrase"));
        translationColumn.setCellValueFactory(new PropertyValueFactory<>("Translation"));

        table.setItems(FXCollections.observableArrayList(
                v.getEntries()
                        .stream()
                        .map(EntryModel::fromEntry).collect(Collectors.toList())));

        add_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                AddVocab add = new AddVocab(vocab);
                getScene().setRoot(add);
            }
        });

        edit_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                System.out.println("Edit"); // pop ups to the edit screen
            }
        });

        return_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                Controller vocab_menu = new Controller();
                getScene().setRoot(vocab_menu);
            }
        });
    }
}