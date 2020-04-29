package at.tugraz.sw20asd.lang.ui.Controller;

import at.tugraz.sw20asd.lang.ui.VocabularyAccess;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class OverviewVocabs extends VBox {


    public Button vocabtrainer_btn;
    @FXML
    private Label title;
    @FXML
    private Label name;


    private VocabularyAccess vocab;
    private int i;
    String jsonFilesFolder = System.getProperty("user.dir") + "/vocabs";



    public OverviewVocabs(VocabularyAccess vocab) {
        this.vocab = vocab;
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(c -> this);
        loader.setRoot(this);
        try {
            loader.load(getClass().getResource("/overviewvocabs.fxml").openStream());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }



    public void initialize() throws FileNotFoundException {

        int num_but = new File(jsonFilesFolder).list().length;
        ArrayList list = new ArrayList();




        for (i = 0; i < num_but; i++) {
            String NameVocab = "";
            JSONParser jsonParser = new JSONParser();
            list.add(i);
            try (FileReader reader = new FileReader(jsonFilesFolder + "/"+ i + ".vocab"))
            {
                //Read JSON file
                Object obj = jsonParser.parse(reader);

                JSONObject json = (JSONObject) obj;


                NameVocab = parseNameObject(json);


            } catch (Exception e) {
                e.printStackTrace();
            }

            Button b = new Button("b" + String.valueOf(i));
            b.setId("b" + String.valueOf(i));
            b.setPrefHeight(40.0);
            b.setPrefWidth(376.0);
            b.setStyle("-fx-border-insets: 1;");
            b.setFont(new Font("Abyssinica SIL", 16));
            b.setLayoutX(318.0);
            b.setLayoutY(300.0);
            b.setText(NameVocab);

            b.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent event) {
                    OverviewWords overview = new OverviewWords(vocab, list);
                    getScene().setRoot(overview);
                }
            });
            getChildren().add(b);
        }




    }

    private static String parseNameObject(JSONObject vocab)
    {

        String NameVocab = (String) vocab.get("name");

        return NameVocab;
    }
}