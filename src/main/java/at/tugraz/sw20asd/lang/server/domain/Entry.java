package at.tugraz.sw20asd.lang.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "entries")
public class Entry extends EntityBase {

    @ManyToOne(
            targetEntity = Vocabulary.class,
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST)
    @JoinColumn(name = "fk_vocabulary")
    @JsonIgnore
    private Vocabulary vocabulary;

    private String phrase;
    private String translation;

    public Entry(String phrase, String translation) {
        super();
        this.phrase = phrase;
        this.translation = translation;
    }

    public Entry() {
        super();
    }

    public String getPhrase() {
        return this.phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getTranslation() {
        return this.translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public Vocabulary getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(Vocabulary vocabulary) {
        this.vocabulary = vocabulary;
    }
}
