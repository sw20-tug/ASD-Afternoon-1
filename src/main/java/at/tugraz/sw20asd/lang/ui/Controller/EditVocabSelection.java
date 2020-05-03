package at.tugraz.sw20asd.lang.ui.Controller;

import at.tugraz.sw20asd.lang.model.Entry;
import at.tugraz.sw20asd.lang.model.Vocabulary;
import at.tugraz.sw20asd.lang.ui.VocabularyAccess;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class EditVocabSelection extends VBox {

    @FXML
    private Button return_btn;
    @FXML
    private VBox button_list;
    @FXML
    private Label title;
    @FXML
    private Button b;  // TODO Only for testing
    @FXML
    private Button add_btn;

    private VocabularyAccess vocab;

    public EditVocabSelection(VocabularyAccess vocab) {
        this.vocab = vocab;
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(c -> this);
        loader.setRoot(this);
        try {
            loader.load(getClass().getResource("/editvocab.fxml").openStream());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void initialize(){

        return_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Controller con = new Controller();
                getScene().setRoot(con);
            }
        });

        // vocab_list = // TODO Get list of vocabs
        int vocab_number = 6; //TODO vocab_list.size()
        for(int counter = 0; counter < vocab_number; counter++)
        {
            Button vocab_button = new Button("vocab_button" + String.valueOf(1));
            vocab_button.setId("vocab_button" + String.valueOf(1));
            vocab_button.setPrefHeight(40.0);
            vocab_button.setPrefWidth(376.0);
            vocab_button.setStyle("-fx-border-insets: 1;");
            vocab_button.setFont(new Font("Abyssinica SIL", 16));
            vocab_button.setLayoutX(318.0);
            vocab_button.setLayoutY(300.0);
            setMargin(vocab_button, new Insets(20,0,0,0));
            vocab_button.setText("Vocab Name here"); // TODO
            vocab_button.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    //EditVocab edit = new EditVocab(vocab, vocab_list.get(counter); // TODO add vocabulary to parameters
                    //getScene().setRoot(edit);
                }
            });
            button_list.getChildren().add(vocab_button);
        }
        //TODO button b is just the test button
        b.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                Locale src = Locale.GERMAN;
                Locale dest = Locale.ENGLISH;
                Vocabulary test1 = new Vocabulary(0,"MyVocab",src,dest);
                test1.addPhrase(new Entry("hund","dog"));
                test1.addPhrase(new Entry("katze","cat"));
                test1.addPhrase(new Entry("fisch","fish"));
                EditVocab edit = new EditVocab(vocab, test1);
                getScene().setRoot(edit);
            }
        });

        add_btn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                AddVocab add = new AddVocab(vocab);
                getScene().setRoot(add);
            }
        });
        }
    }