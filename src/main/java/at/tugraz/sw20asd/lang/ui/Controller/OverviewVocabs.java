package at.tugraz.sw20asd.lang.ui.Controller;

import at.tugraz.sw20asd.lang.model.Vocabulary;
import at.tugraz.sw20asd.lang.ui.VocabularyAccess;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.List;

public class OverviewVocabs extends VBox {

    private VocabularyAccess vocab;

    private Task<List<Vocabulary>> getAllVocabsTask;
    private List<Vocabulary> Vocabularies;

    @FXML
    private Button return_btn;
    @FXML
    private Label user_info;
    @FXML
    private ScrollPane pane;
    @FXML
    private GridPane scrollPaneContent;


    public OverviewVocabs(VocabularyAccess vocab) {
        this.vocab = vocab;
        this.Vocabularies = FXCollections.observableArrayList();

        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(c -> this);
        loader.setRoot(this);
        try {
            loader.load(getClass().getResource("/overview-vocabs.fxml").openStream());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void initialize() {
        getVocabs();

        scrollPaneContent.minWidthProperty().bind(Bindings.createDoubleBinding(() ->
                pane.getViewportBounds().getWidth(), pane.viewportBoundsProperty()));

        scrollPaneContent.setPadding(new Insets(20));
        scrollPaneContent.setHgap(10);
        scrollPaneContent.setVgap(10);
        pane.setContent(scrollPaneContent);

        return_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                clearOverviewVocabs();
                Controller vocab_menu = new Controller();
                getScene().setRoot(vocab_menu);
            }
        });

    }


    private Button createButton(int index) {
        Button button = new Button(Vocabularies.get(index).getName());
        button.setPrefHeight(40.0);
        button.setPrefWidth(350.0);
        int finalCurrent_size = index;
        button.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                OverviewWords overview = new OverviewWords(vocab, Vocabularies.get(finalCurrent_size).getID());
                getScene().setRoot(overview);
            }
        });
        return button;
    }

    private void setVocabs() {
        int size = Vocabularies.size();
        int height = size / 2;
        int width = 2;
        int r, c;

        int current_size = 0;
        for (r = 0; r < height; r++) {
            for (c = 0; c < width; c++) {
                Button button = createButton(current_size);
                int finalC = c;
                int finalR = r;
                Platform.runLater(() -> {
                    scrollPaneContent.add(button, finalC, finalR);
                });
                current_size++;
            }
        }
        if ((size % 2) != 0) {
            Button button = createButton(current_size++);
            int finalC = 0;
            int finalR = r + 1;
            Platform.runLater(() -> {
                scrollPaneContent.add(button, finalC, finalR);
            });
        }

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

    private void getVocabs() {
        //get Vocabularies
        getAllVocabsTask = new Task<>() {
            @Override
            protected List<Vocabulary> call() throws Exception {
                Vocabularies = vocab.getAllVocabularies();
                return Vocabularies;
            }
        };

        getAllVocabsTask.stateProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.CANCELLED || newValue == Worker.State.FAILED || getAllVocabsTask.getValue() == null) {
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
                    if (Vocabularies.size() == 1) {
                        Button button = createButton(0);
                        Platform.runLater(() -> {
                            scrollPaneContent.add(button, 0, 0);
                        });
                    } else {
                        setVocabs();
                    }
                }
            }
        }));

        Thread th = new Thread(getAllVocabsTask);
        th.setDaemon(true);
        th.start();
    }

    private void clearOverviewVocabs() {
        Vocabularies.clear();
    }
}