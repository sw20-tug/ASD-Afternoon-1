package at.tugraz.sw20asd.lang.ui.Controller;

import at.tugraz.sw20asd.lang.model.Vocabulary;
import at.tugraz.sw20asd.lang.ui.VocabularyAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import at.tugraz.sw20asd.lang.model.Entry;
import javafx.scene.paint.Color;

public class AddVocab extends VBox {
    @FXML
    private TextField from_field, from_field1, from_field2, from_field3, from_field4, from_field5;
    @FXML
    private TextField to_field, to_field1, to_field2, to_field3, to_field4, to_field5;
    @FXML
    private Label from_label;
    @FXML
    private Label to_label;
    @FXML
    private Label category_label;
    @FXML
    private Button submit_btn;
    @FXML
    private Button return_btn;
    @FXML
    private Button ok_btn;
    @FXML
    private ComboBox<String> from_choice;
    @FXML
    private ComboBox<String> to_choice;
    @FXML
    private Label user_info;
    @FXML
    private TextField category;

    private String from_string, from_string1, from_string2, from_string3, from_string4, from_string5;
    private String to_string, to_string1, to_string2, to_string3, to_string4, to_string5;

    private Task<Integer> addtask;
    private VocabularyAccess vocab;

    private String category_string;
    private ObservableList<String> vocabulary_list = FXCollections.observableArrayList();
    private HashMap<String, Locale> language_map = new HashMap<String, Locale>();
    private int id;


    public AddVocab(VocabularyAccess vocab){

        this.vocab = vocab;

        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(c -> this);
        loader.setRoot(this);
        try {
            loader.load(getClass().getResource("/add.fxml").openStream());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    public void initialize(){


        user_info.setVisible(false);
        ObservableList<String> languages =
                FXCollections.observableArrayList("German", "English");

        from_choice.setItems(languages);
        to_choice.setItems(languages);


        language_map.put("German", Locale.GERMAN);
        language_map.put("English", Locale.ENGLISH);



        from_choice.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            from_label.setText(newValue + ":");

            if(!to_choice.getSelectionModel().isEmpty()  &&
                    (to_choice.getSelectionModel().getSelectedItem().equals(newValue))){

                updateUserInformation("equal_lang");
            }
            else{
                user_info.setVisible(false);
            }
        });

        to_choice.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            to_label.setText(newValue + ":");

            if(!from_choice.getSelectionModel().isEmpty() &&
                    from_choice.getSelectionModel().getSelectedItem().equals(newValue)){

                updateUserInformation("equal_lang");
            }
            else{
                user_info.setVisible(false);
            }
        });

        category.textProperty().addListener((observable, oldValue, newValue) -> {
            category_string = newValue;
        });

        from_field.textProperty().addListener((observable, oldValue, newValue) -> {
            from_string = newValue;
        });
        to_field.textProperty().addListener((observable, oldValue, newValue) -> {
            to_string = newValue;
        });
        from_field1.textProperty().addListener((observable, oldValue, newValue) -> {
            from_string1 = newValue;
        });
        to_field1.textProperty().addListener((observable, oldValue, newValue) -> {
            to_string1 = newValue;
        });
        from_field2.textProperty().addListener((observable, oldValue, newValue) -> {
            from_string2 = newValue;
        });
        to_field2.textProperty().addListener((observable, oldValue, newValue) -> {
            to_string2 = newValue;
        });
        from_field3.textProperty().addListener((observable, oldValue, newValue) -> {
            from_string3 = newValue;
        });
        to_field3.textProperty().addListener((observable, oldValue, newValue) -> {
            to_string3 = newValue;
        });
        from_field4.textProperty().addListener((observable, oldValue, newValue) -> {
            from_string4 = newValue;
        });
        to_field4.textProperty().addListener((observable, oldValue, newValue) -> {
            to_string4 = newValue;
        });
        from_field5.textProperty().addListener((observable, oldValue, newValue) -> {
            from_string5 = newValue;
        });
        to_field5.textProperty().addListener((observable, oldValue, newValue) -> {
            to_string5 = newValue;
        });

        submit_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                if(from_choice.getSelectionModel().isEmpty()
                        || to_choice.getSelectionModel().isEmpty()){
                    updateUserInformation("missing_selection");
                }
                else if(from_choice.getSelectionModel().getSelectedItem().equals(to_choice.getSelectionModel().getSelectedItem())){
                    updateUserInformation("equal_lang");
                }
                else if(category.getText().isEmpty()){
                    updateUserInformation("category");
                }
                else if(!checkEntries())
                {
                    updateUserInformation("entry_missing");
                }
                else{
                    sendAddCommand();
                }
            }
        });

        return_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                from_choice.getSelectionModel().clearSelection();
                to_choice.getSelectionModel().clearSelection();
                from_field.clear();
                to_field.clear();
                from_label.setText("From:");
                to_label.setText("To:");
                Controller con = new Controller();
                getScene().setRoot(con);
            }
        });


    }

    private void updateUserInformation(String code){
        user_info.setVisible(true);
        user_info.setTextFill(Color.RED);
        switch (code){
            case "added_vocab":
                user_info.setText("Vocabulary added!");
                break;
            case "equal_lang":
                user_info.setText("Please select another language");
                break;
            case "missing_selection":
                user_info.setText("Please select from and to language");
                break;
            case "amount_missing":
                user_info.setText("Please fill out the amount");
                break;
            case "entry_missing":
                user_info.setText("Please check if you have entered" + "\n" + "a matching To for each From in the same line");
                break;
            case "category":
                user_info.setText("Please fill out the Name of your Vocabulary group");
                break;
            default:
                user_info.setText("Sorry, something went wrong");
        }
    }

    private void sendAddCommand(){

        user_info.setVisible(false);
        int amount = getAmountOfEntries();
        id = -1;

        String from_lang = from_choice.getSelectionModel().getSelectedItem();
        String to_lang = to_choice.getSelectionModel().getSelectedItem();

        Locale source_lang = language_map.get(from_lang);
        Locale target_lang = language_map.get(to_lang);

        Vocabulary vocabulary = new Vocabulary(null, category_string, source_lang, target_lang);

        for(int i = 0; i < amount; i++)
        {
            String from = vocabulary_list.get(i);
            String to = vocabulary_list.get(i);
            Entry entry = new Entry(from, to);
            vocabulary.addPhrase(entry);
        }

        addtask = new Task<Integer> () {
            @Override
            protected Integer call() throws Exception {
                int id = 0;
                id = vocab.addVocabulary(vocabulary);
                return id;
            }
        };

        addtask.stateProperty().addListener(((observableValue, oldState, newState)->{
            if(newState == Worker.State.CANCELLED){
                addtask.cancel();
            }
            if(newState == Worker.State.SUCCEEDED){
                id = addtask.getValue();
            }
        }));

        Thread th = new Thread(addtask);
        th.setDaemon(true);
        th.start();


        if(id != -1)
            updateUserInformation("added_vocab");
        else
            updateUserInformation("");
    }

    private int getAmountOfEntries(){
        int amount = 0;
        if(!from_field.getText().isEmpty() && !to_field.getText().isEmpty()){
            amount++;
            System.out.println(from_string + to_string);
            vocabulary_list.add(from_string);
            vocabulary_list.add(to_string);
        }

        if(!from_field1.getText().isEmpty() && !to_field1.getText().isEmpty()){
            amount++;
            vocabulary_list.addAll(from_string1, to_string1);
        }

        if(!from_field2.getText().isEmpty() && !to_field2.getText().isEmpty()){
            amount++;
            vocabulary_list.addAll(from_string2, to_string2);
        }

        if(!from_field3.getText().isEmpty() && !to_field3.getText().isEmpty()){
            amount++;
            vocabulary_list.addAll(from_string3, to_string3);
        }

        if(!from_field4.getText().isEmpty() && !to_field4.getText().isEmpty()){
            amount++;
            vocabulary_list.addAll(from_string4, to_string4);
        }

        if(!from_field5.getText().isEmpty() && !to_field5.getText().isEmpty()){
            amount++;
            vocabulary_list.addAll(from_string5, to_string5);
        }
        return amount;

    }

    private boolean checkEntries(){
        if((!from_field.getText().isEmpty() && to_field.getText().isEmpty()) ||
                (from_field.getText().isEmpty() && !to_field.getText().isEmpty())){
            return false;
        }
        else if((!from_field1.getText().isEmpty() && to_field1.getText().isEmpty()) ||
                (from_field1.getText().isEmpty() && !to_field1.getText().isEmpty())){
            return false;
        }
        else if((!from_field2.getText().isEmpty() && to_field2.getText().isEmpty()) ||
                (from_field2.getText().isEmpty() && !to_field2.getText().isEmpty())){
            return false;
        }
        else if((!from_field3.getText().isEmpty() && to_field3.getText().isEmpty()) ||
                (from_field3.getText().isEmpty() && !to_field3.getText().isEmpty())){
            return false;
        }
        else if((!from_field4.getText().isEmpty() && to_field4.getText().isEmpty()) ||
                (from_field4.getText().isEmpty() && !to_field4.getText().isEmpty())){
            return false;
        }else if((!from_field5.getText().isEmpty() && to_field5.getText().isEmpty()) ||
                (from_field5.getText().isEmpty() && !to_field5.getText().isEmpty())){
            return false;
        }
        return  true;
    }


}