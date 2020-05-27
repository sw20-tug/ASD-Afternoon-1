package at.tugraz.sw20asd.lang.ui.Controller;

import at.tugraz.sw20asd.lang.dto.VocabularyBaseDto;
import at.tugraz.sw20asd.lang.ui.VocabularyAccess;
import at.tugraz.sw20asd.lang.ui.models.VocabularySelectionModel;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
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
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SelectionOverview extends VBox {

    private VocabularyAccess vocab;
    private List<VocabularyBaseDto> Vocabularies;
    private Task<List<VocabularyBaseDto>> getAllVocabsTask;
    private ObservableList<VocabularySelectionModel> selectedItems = FXCollections.observableArrayList();
    private ObservableList<VocabularySelectionModel> possibleItems = FXCollections.observableArrayList();
    private ObservableMap<Integer, VocabularySelectionModel> indexMap = FXCollections.observableHashMap();

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
    private TableColumn<VocabularySelectionModel, String> vocabularyTranslation;
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
        initTableView();
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
                if (selectedItems.size() == 0) {
                    updateUserInformation("nothing_selected");
                }
                //TODO switch to study, use selectedItems as param type ObservableList<VocabularySelectionModel>
                //take care you get a mixed list (e.g. DE - EN and EN - DE are allowed)
                //think about how to handle it
            }
        });
        train_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                getSelection();
                if (selectedItems.size() == 0) {
                    updateUserInformation("nothing_selected");
                }
                //TODO switch to train, use selectedItems as param  type ObservableList<VocabularySelectionModel>
                //take care you get a mixed list (e.g. DE - EN and EN - DE are allowed)
                //think about how to handle it
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

                    possibleItems = (FXCollections.observableArrayList(
                            Vocabularies
                                    .stream()
                                    .map(VocabularySelectionModel::fromDto)
                                    .collect(Collectors.toList())
                    ));
                    //needed for selection
                    getCurrentIndexMap();

                    Platform.runLater(() -> {
                        vocab_list.setItems(possibleItems);
                    });

                }
            }
        }));

        Thread th = new Thread(getAllVocabsTask);
        th.setDaemon(true);
        th.start();
    }

    //Callback <ReturnType, ParameterType>
    //allows to pass a method indirectly to a class or method
    private void initTableView() {
        vocabularyNameColumn.setCellValueFactory(new PropertyValueFactory<>("vocabularyName"));
        vocabularyTranslation.setCellValueFactory(new PropertyValueFactory<>("vocabularyTranslation"));
        checkBoxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(new Callback<Integer, ObservableValue<Boolean>>() {

            //callback function -> cellValueFactory should return a boolean property (reset to default or other state)
            //checkbox state is bidirectionally bound to the boolean property
            @Override
            public ObservableValue<Boolean> call(Integer index) {

                //get my boolean property of my model
                ObservableValue<Boolean> itemBoolean = indexMap.get(index).selectedProperty();
                getCurrentIndexMap();
                //register listener to change my values
                itemBoolean.addListener(change -> {
                    getSelection();
                    VocabularySelectionModel currentElement = indexMap.get(index);

                    if (currentElement.isSelected()) {
                        String src = currentElement.getVocabularySrc();
                        String target = currentElement.getVocabularyTarget();
                        ObservableList<VocabularySelectionModel> buffer = FXCollections.observableArrayList(possibleItems);
                        possibleItems.clear();

                        for (VocabularySelectionModel v : buffer) {
                            if ((v.getVocabularySrc().equals(src) || v.getVocabularySrc().equals(target)) &&
                                    (v.getVocabularyTarget().equals(src) || v.getVocabularyTarget().equals(target))) {
                                possibleItems.add(v);
                            }
                        }
                    } else {
                        //if nothing is selected reset table
                        if (selectedItems.size() <= 0) {
                            possibleItems = FXCollections.observableArrayList(
                                    Vocabularies
                                            .stream()
                                            .map(VocabularySelectionModel::fromDto)
                                            .collect(Collectors.toList())
                            );
                        }

                    }
                    vocab_list.setItems(possibleItems);
                    getCurrentIndexMap();
                });
                return itemBoolean;
            }
        }));
        checkBoxColumn.setCellValueFactory(new PropertyValueFactory<>("selected"));
    }

    private void getSelection() {
        selectedItems.clear();
        List<VocabularySelectionModel> buffer = vocab_list.getItems();
        for (VocabularySelectionModel v : buffer) {
            if (v.isSelected()) {
                selectedItems.add(v);
            }
        }
    }

    private void getCurrentIndexMap() {
        int counter = 0;
        for (VocabularySelectionModel v : possibleItems) {
            indexMap.put(counter, v);
            counter++;
        }
    }

    private void updateUserInformation(String code) {
        user_info.setVisible(true);
        user_info.setTextFill(Color.RED);
        switch (code) {
            case "no_vocabs":
                user_info.setText("You haven`t added any vocab yet!");
                break;
            case "nothing_selected":
                user_info.setText("Please select at least one vocabulary group!");
                break;
            default:
                user_info.setText("Sorry, something went wrong");
        }
    }

    private void clear() {
        getAllVocabsTask.cancel();
        selectedItems.clear();
        possibleItems.clear();
        Vocabularies.clear();
        indexMap.clear();
        vocab_list.getItems().clear();
        user_info.setVisible(false);

    }
}