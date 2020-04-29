package at.tugraz.sw20asd.lang.ui.Controller;

import at.tugraz.sw20asd.lang.model.Vocabulary;
import at.tugraz.sw20asd.lang.ui.VocabularyAccess;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import at.tugraz.sw20asd.lang.model.Entry;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class OverviewWords extends VBox {

    @FXML
    private Label title;
    @FXML
    private Button add_btn;
    @FXML
    private Button edit_btn;
    @FXML
    private Button return_btn;
    @FXML
    private TableView<String> table;

    private VocabularyAccess vocab;
    private ArrayList list;
    String jsonFilesFolder = System.getProperty("user.dir") + "/vocabs";


    public OverviewWords(VocabularyAccess vocab, ArrayList list){
        this.list = list;
        this.vocab = vocab;
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(c -> this);
        loader.setRoot(this);
        try {
            loader.load(getClass().getResource("/overviewwords.fxml").openStream());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void initialize(){
        String SourceLanguage = "";
        String TargetLanguage= "";
        JSONParser jsonParser = new JSONParser();
        for (int x = 0; x < list.size(); x++){
            try (FileReader reader = new FileReader(jsonFilesFolder + "/"+ list.get(x) + ".vocab"))
            {
                //Read JSON file
                Object obj = jsonParser.parse(reader);

                JSONObject json = (JSONObject) obj;


                SourceLanguage = parseSourceLanguageObject(json);
                TargetLanguage = parseTargetLanguageObject(json);


            } catch (Exception e) {
                e.printStackTrace();
            }
    }


        TableColumn sourceLanguage = new TableColumn(SourceLanguage);
        TableColumn targetLanguage = new TableColumn(TargetLanguage);
        table.getColumns().addAll(sourceLanguage, targetLanguage);




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

    private static String parseSourceLanguageObject(JSONObject vocab)
    {

        String SourceLanguage = (String) vocab.get("sourceLanguage");

        return SourceLanguage;
    }

    private static String parseTargetLanguageObject(JSONObject vocab)
    {

        String TargetLanguage = (String) vocab.get("targetLanguage");

        return TargetLanguage;
    }
}