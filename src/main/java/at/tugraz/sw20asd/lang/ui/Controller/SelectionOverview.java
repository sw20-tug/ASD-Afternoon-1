package at.tugraz.sw20asd.lang.ui.Controller;

import at.tugraz.sw20asd.lang.dto.VocabularyBaseDto;
import at.tugraz.sw20asd.lang.server.domain.Vocabulary;
import at.tugraz.sw20asd.lang.ui.VocabularyAccess;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.springframework.boot.util.LambdaSafe;

import javafx.util.Callback;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SelectionOverview extends VBox {

    private VocabularyAccess vocab;
    private List<VocabularyBaseDto> Vocabularies;
    private Task<List<VocabularyBaseDto>> getAllVocabsTask;

    private ObservableList<VocabularyBaseDto> selectedItems = FXCollections.observableArrayList();


    @FXML
    private Button return_btn;
    @FXML
    private Button study_btn;
    @FXML
    private Button train_btn;
    @FXML
    private Label user_info;
    @FXML
    private ScrollPane scroll_pane;
    @FXML
    private TableView<String> vocab_list;

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
        initListView();


        getVocabGroups();

        return_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                //TODO clear
                Controller con = new Controller();
                getScene().setRoot(con);
            }
        });
        study_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                //TODO switch to study
            }
        });
        train_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                //TODO switch to train
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
                        updateUserInformation("No vocabs added");
                    });
                } else {

                    List<String> names = new LinkedList<>();

                    for(VocabularyBaseDto v : Vocabularies){
                        names.add(v.getName());
                    }

                    Platform.runLater(() -> {
                        vocab_list.setItems(FXCollections.observableArrayList(names));
                    });

                }
            }
        }));

        Thread th = new Thread(getAllVocabsTask);
        th.setDaemon(true);
        th.start();
    }


    private void initListView(){

        //TableView stuff
        TableColumn<String, String> vocabNamesCol = new TableColumn<>("Name");
        TableColumn<String, Void> checkBoxCol = new TableColumn<>("Selection");

        vocabNamesCol.setPrefWidth(250);
        checkBoxCol.setPrefWidth(100);


        checkBoxCol.setSortable(false);

        Callback<TableColumn<String, Void>, TableCell<String, Void>> cellFactory = new Callback<TableColumn<String, Void>, TableCell<String, Void>>() {
            @Override
            public TableCell<String, Void> call(TableColumn<String, Void> param) {
                final TableCell<String, Void> cell = new TableCell<String, Void>() {
                    private final CheckBox check = new CheckBox();
                    {
                        check.selectedProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                check.setSelected(true);
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            setGraphic(check);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
                return cell;
            }
        };
        checkBoxCol.setCellFactory(cellFactory);

        vocab_list.getColumns().addAll(vocabNamesCol, checkBoxCol);
    }


    private void updateUserInformation(String code) {

        user_info.setVisible(true);
        user_info.setTextFill(Color.RED);
        switch (code) {
            case "No vocabs added":
                user_info.setText("You haven`t added any vocab yet!");
                break;
            case "Server error":
                user_info.setText("Server not working");
                break;
            default:
                user_info.setText("Sorry, something went wrong");
        }
    }



}
