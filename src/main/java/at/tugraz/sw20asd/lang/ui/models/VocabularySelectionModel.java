package at.tugraz.sw20asd.lang.ui.models;

import at.tugraz.sw20asd.lang.dto.VocabularyBaseDto;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class VocabularySelectionModel {
    private final SimpleLongProperty vocabularyId;
    private final SimpleStringProperty vocabularyName;
    private final SimpleBooleanProperty selected;

    public static VocabularySelectionModel fromDto(VocabularyBaseDto v) {
        VocabularySelectionModel vsm = new VocabularySelectionModel();

        vsm.setVocabularyId(v.getId());
        vsm.setVocabularyName(v.getName());
        vsm.setSelected(false);

        return vsm;
    }

    public VocabularySelectionModel() {
        this.vocabularyId = new SimpleLongProperty();
        this.vocabularyName = new SimpleStringProperty();
        this.selected = new SimpleBooleanProperty();
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
