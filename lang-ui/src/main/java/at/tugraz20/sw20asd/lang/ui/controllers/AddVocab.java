package at.tugraz20.sw20asd.lang.ui.controllers;

import at.tugraz.sw20asd.lang.EntryDto;
import at.tugraz.sw20asd.lang.VocabularyDetailDto;
import at.tugraz20.sw20asd.lang.ui.dataaccess.VocabularyAccess;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;

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
    FXMLLoader loader = new FXMLLoader();

    public AddVocab(VocabularyAccess vocab) {

        this.vocab = vocab;
        URL location = getClass().getResource("/add.fxml");
        loader.setControllerFactory(c -> this);
        loader.setRoot(this);
        try {
            loader.load(location.openStream());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }


    public void initialize() {
        user_info.setVisible(false);
        ObservableList<String> languages =
                FXCollections.observableArrayList("German", "English");

        from_choice.setItems(languages);
        to_choice.setItems(languages);

        language_map.put("German", Locale.GERMAN);
        language_map.put("English", Locale.ENGLISH);


        from_choice.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            from_label.setText(newValue + ":");

            if (!to_choice.getSelectionModel().isEmpty() &&
                    (to_choice.getSelectionModel().getSelectedItem().equals(newValue))) {

                updateUserInformation("equal_lang");
            } else {
                user_info.setVisible(false);
            }
        });

        to_choice.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            to_label.setText(newValue + ":");

            if (!from_choice.getSelectionModel().isEmpty() &&
                    from_choice.getSelectionModel().getSelectedItem().equals(newValue)) {

                updateUserInformation("equal_lang");
            } else {
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
                if (from_choice.getSelectionModel().isEmpty()
                        || to_choice.getSelectionModel().isEmpty()) {
                    updateUserInformation("missing_selection");
                } else if (from_choice.getSelectionModel().getSelectedItem().equals(to_choice.getSelectionModel().getSelectedItem())) {
                    updateUserInformation("equal_lang");
                } else if (category.getText().isEmpty()) {
                    updateUserInformation("category");
                } else if (!checkEntries()) {
                    updateUserInformation("entry_missing");
                }
                else {
                    sendAddCommand();
                }
            }
        });

        return_btn.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                clearAddVocab();
                Controller con = new Controller();
                getScene().setRoot(con);
            }
        });
    }

    private void updateUserInformation(String code) {
        user_info.setVisible(true);
        user_info.setTextFill(Color.RED);
        switch (code) {
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

    private void sendAddCommand() {

        user_info.setVisible(false);
        int amount = getAmountOfEntries();
        id = -1;

        String from_lang = from_choice.getSelectionModel().getSelectedItem();
        String to_lang = to_choice.getSelectionModel().getSelectedItem();

        Locale source_lang = language_map.get(from_lang);
        Locale target_lang = language_map.get(to_lang);

        VocabularyDetailDto vocabulary = new VocabularyDetailDto(null, category_string, source_lang, target_lang);

        for (int i = 0; (i + 1) < amount; i++) {
            String from = vocabulary_list.get(i);
            String to = vocabulary_list.get(++i);
            EntryDto entry = new EntryDto(from, to);
            vocabulary.addEntry(entry);
        }

        addtask = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                int id = 0;
                id = vocab.addVocabulary(vocabulary);
                return id;
            }
        };

        addtask.stateProperty().addListener(((observableValue, oldState, newState) -> {
            if (newState == Worker.State.CANCELLED) {
                addtask.cancel();
            }
            if (newState == Worker.State.SUCCEEDED) {
                id = addtask.getValue();
                if(id != -1){
                    Platform.runLater(() -> {
                        updateUserInformation("added_vocab");
                    });
                    clearAddVocab();
                }
                else{
                    Platform.runLater(() -> {
                        updateUserInformation("");
                    });
                }
            }
        }));

        Thread th = new Thread(addtask);
        th.setDaemon(true);
        th.start();


    }

    private int getAmountOfEntries() {
        int amount = 0;
        if (!from_field.getText().isEmpty() && !to_field.getText().isEmpty()) {
            amount += 2;
            vocabulary_list.addAll(from_string, to_string);
        }

        if (!from_field1.getText().isEmpty() && !to_field1.getText().isEmpty()) {
            amount += 2;
            vocabulary_list.addAll(from_string1, to_string1);
        }

        if (!from_field2.getText().isEmpty() && !to_field2.getText().isEmpty()) {
            amount += 2;
            vocabulary_list.addAll(from_string2, to_string2);
        }

        if (!from_field3.getText().isEmpty() && !to_field3.getText().isEmpty()) {
            amount += 2;
            vocabulary_list.addAll(from_string3, to_string3);
        }

        if (!from_field4.getText().isEmpty() && !to_field4.getText().isEmpty()) {
            amount += 2;
            vocabulary_list.addAll(from_string4, to_string4);
        }

        if (!from_field5.getText().isEmpty() && !to_field5.getText().isEmpty()) {
            amount += 2;
            vocabulary_list.addAll(from_string5, to_string5);
        }
        return amount;

    }

    private boolean checkText_ExclusiveOr(TextField tf1, TextField tf2) {
        return ((!tf1.getText().isEmpty() && tf2.getText().isEmpty())
                || (tf1.getText().isEmpty() && !tf2.getText().isEmpty()));
    }

    private boolean checkEntries(){
        if(checkText_ExclusiveOr(from_field, to_field))
            return false;
        if(checkText_ExclusiveOr(from_field1, to_field1))
            return false;
        if(checkText_ExclusiveOr(from_field2, to_field2))
            return false;
        if(checkText_ExclusiveOr(from_field3, to_field3))
            return false;
        if(checkText_ExclusiveOr(from_field4, to_field4))
            return false;
        if(checkText_ExclusiveOr(from_field5, to_field5))
            return false;

        return true;
    }

    private void clearAddVocab(){
        from_choice.getSelectionModel().clearSelection();
        to_choice.getSelectionModel().clearSelection();
        from_field.clear();
        to_field1.clear();
        from_field1.clear();
        to_field2.clear();
        from_field2.clear();
        to_field3.clear();
        from_field3.clear();
        to_field4.clear();
        from_field4.clear();
        to_field5.clear();
        from_field5.clear();
        to_field.clear();
        from_label.setText("From:");
        to_label.setText("To:");

        vocabulary_list.clear();
        category.clear();
    }
}