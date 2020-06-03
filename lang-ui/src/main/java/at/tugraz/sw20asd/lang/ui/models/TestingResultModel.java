package at.tugraz.sw20asd.lang.ui.models;

import at.tugraz.sw20asd.lang.EntryDto;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class TestingResultModel {
    private SimpleStringProperty phrase;
    private SimpleStringProperty translation;
    private SimpleIntegerProperty repetitions;


    public TestingResultModel(){
    }

    public TestingResultModel(String phrase, String translation, Integer repetitions){
        this.phrase = new SimpleStringProperty(phrase);
        this.translation = new SimpleStringProperty(translation);
        this.repetitions = new SimpleIntegerProperty(repetitions);
    }

    public String getPhrase() {
        return phrase.get();
    }

    public void setPhrase(String phrase) {
        this.phrase = new SimpleStringProperty(phrase);
    }

    public String getTranslation() {
        return translation.get();
    }

    public void setTranslation(String translation) {
        this.translation = new SimpleStringProperty(translation);
    }

    public Integer getRepetitions() {
        return repetitions.get();
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = new SimpleIntegerProperty(repetitions);
    }

}
