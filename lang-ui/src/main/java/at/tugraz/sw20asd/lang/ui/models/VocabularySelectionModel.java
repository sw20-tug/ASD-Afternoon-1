package at.tugraz.sw20asd.lang.ui.models;

import at.tugraz.sw20asd.lang.VocabularyBaseDto;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;


public class VocabularySelectionModel {
    private final SimpleLongProperty vocabularyId;
    private final SimpleStringProperty vocabularyName;
    private final SimpleStringProperty src;
    private final SimpleStringProperty target;
    private final SimpleStringProperty vocabularyTranslation;

    private final SimpleBooleanProperty selected;


    public static VocabularySelectionModel fromDto(VocabularyBaseDto v) {
        VocabularySelectionModel vsm = new VocabularySelectionModel();

        vsm.setVocabularyId(v.getId());
        vsm.setVocabularyName(v.getName());
        vsm.setVocabularySrc(v.getSourceLanguage().toString());
        vsm.setVocabularyTarget(v.getTargetLanguage().toString());
        vsm.setVocabularyTranslation(v.getSourceLanguage().toString().toUpperCase() + " - " +
                v.getTargetLanguage().toString().toUpperCase());

        vsm.setSelected(false);

        return vsm;
    }


    public VocabularySelectionModel() {
        this.vocabularyId = new SimpleLongProperty();
        this.vocabularyName = new SimpleStringProperty();
        this.selected = new SimpleBooleanProperty();
        this.src = new SimpleStringProperty();
        this.target = new SimpleStringProperty();
        this.vocabularyTranslation = new SimpleStringProperty();
    }

    private void setVocabularyTranslation(String translation) {
        this.vocabularyTranslation.set(translation);
    }

    public String getVocabularyTranslation() {
        return vocabularyTranslation.get();
    }

    private void setVocabularySrc(String translation) {
        this.src.set(translation);
    }

    public String getVocabularySrc() {
        return src.get();
    }

    private void setVocabularyTarget(String translation) {
        this.target.set(translation);
    }

    public String getVocabularyTarget() {
        return target.get();
    }


    public long getVocabularyId() {
        return vocabularyId.get();
    }


    public void setVocabularyId(long vocabularyId) {
        this.vocabularyId.set(vocabularyId);
    }

    public String getVocabularyName() {
        return vocabularyName.get();
    }

    public void setVocabularyName(String vocabularyName) {
        this.vocabularyName.set(vocabularyName);
    }

    public boolean isSelected() {
        return selected.get();
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean isSelected) {
        this.selected.set(isSelected);
    }
}