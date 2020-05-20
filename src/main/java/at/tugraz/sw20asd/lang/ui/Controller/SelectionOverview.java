package at.tugraz.sw20asd.lang.ui.Controller;

import at.tugraz.sw20asd.lang.dto.VocabularyBaseDto;
import at.tugraz.sw20asd.lang.ui.VocabularyAccess;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SelectionOverview extends VBox {

    private VocabularyAccess vocab;
    private List<VocabularyBaseDto> Vocabularies;
    private Task<List<VocabularyBaseDto>> getAllVocabsTask;
    private ObservableList<VocabularySelectionModel> selectedItems = FXCollections.observableArrayList();


    @FXML
    private Button return_btn;
    @FXML
    private Button study_btn;
    @FXML
    private Button train_btn;
    @FXML
    private Label user_info;
    @FXML
    private TableView<VocabularySelectionModel> vocab_list;
    @FXML
    private TableColumn<VocabularySelectionModel, String> vocabularyNameColumn;
    @FXML
    private TableColumn<VocabularySelectionModel, Boolean> checkBoxColumn;

    public SelectionOverview(VocabularyAccess vocab) {
        this.vocab = vocab;
        this.Vocabularies = FXCollections.observableArrayList();

        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(c -> this);
        loader.setRoot(this);
        try {
            loader.load(getClass().getResource("/selection-overview.fxml").openStream());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void initialize() {
        user_info.setVisible(false);
        selectedItems.clear();
        //initialize TableView
        initListView();
        //get vocabs
        getVocabGroups();

        return_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                clear();
                Controller con = new Controller();
                getScene().setRoot(con);
            }
        });
        study_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                getSelection();
                //TODO switch to study, use selectedItems as param type ObservableList<VocabularySelectionModel>
            }
        });
        train_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                getSelection();
                //TODO switch to train, use selectedItems as param  type ObservableList<VocabularySelectionModel>
            }
        });
    }

    private void getVocabGroups() {
        //get Vocabularies
        getAllVocabsTask = new Task<>() {
            @Override
            protected List<VocabularyBaseDto> call() throws Exception {
                Vocabularies = vocab.getAllVocabularies();
                return Vocabularies;
            }
        };

        getAllVocabsTask.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.CANCELLED || newValue == Worker.State.FAILED) {
                Platform.runLater(() -> {
                    updateUserInformation("");
                });
                getAllVocabsTask.cancel();
            }

            if (newValue == Worker.State.SUCCEEDED) {
                if (Vocabularies == null || Vocabularies.size() == 0) {
                    Platform.runLater(() -> {
                        updateUserInformation("no_vocabs");
                    });
                } else {
                    Platform.runLater(() -> {
                        vocab_list.setItems(FXCollections.observableArrayList(
                                Vocabularies
                                        .stream()
                                        .map(VocabularySelectionModel::fromDto)
                                        .collect(Collectors.toList())
                        ));
                    });

                }
            }
        }));

        Thread th = new Thread(getAllVocabsTask);
        th.setDaemon(true);
        th.start();
    }

    private void initListView() {
        vocabularyNameColumn.setCellValueFactory(new PropertyValueFactory<>("VocabularyName"));
        checkBoxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkBoxColumn));
        checkBoxColumn.setCellValueFactory(new PropertyValueFactory<>("selected"));
    }

    private void getSelection() {
        List<VocabularySelectionModel> buffer = vocab_list.getItems();
        for (VocabularySelectionModel v : buffer) {
            if (v.isSelected())
                selectedItems.add(v);
        }
    }

    private void updateUserInformation(String code) {

        user_info.setVisible(true);
        user_info.setTextFill(Color.RED);
        switch (code) {
            case "no_vocabs":
                user_info.setText("You haven`t added any vocab yet!");
                break;
            default:
                user_info.setText("Sorry, something went wrong");
        }
    }

    private void clear() {
        getAllVocabsTask.cancel();
        selectedItems.clear();
        Vocabularies.clear();
        vocab_list.getItems().clear();
    }
}
