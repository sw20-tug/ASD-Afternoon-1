package at.tugraz20.sw20asd.lang.ui.models;

import at.tugraz.sw20asd.lang.EntryDto;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class EntryModel {
    private SimpleLongProperty entryId;
    private SimpleStringProperty phrase;
    private SimpleStringProperty translation;

    public static EntryModel fromEntry(EntryDto e) {
        EntryModel em = new EntryModel();
        em.setPhrase(e.getPhrase());
        em.setTranslation(e.getTranslation());

        return em;
    }

    public EntryModel() {
    }

    public EntryModel(Long entryId, String phrase, String translation) {
        this.entryId = new SimpleLongProperty(entryId);
        this.phrase = new SimpleStringProperty(phrase);
        this.translation = new SimpleStringProperty(translation);
    }

    public long getEntryId() {
        return entryId.get();
    }

    public void setEntryId(long entryId) {
        this.entryId = new SimpleLongProperty(entryId);
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
}
