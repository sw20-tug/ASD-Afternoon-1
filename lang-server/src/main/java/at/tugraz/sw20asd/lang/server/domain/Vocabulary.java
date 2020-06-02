package at.tugraz.sw20asd.lang.server.domain;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Entity
@Table(name = "vocabularies")
public class Vocabulary extends EntityBase {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotNull(message = "Source language is mandatory")
    private Locale sourceLanguage;

    @NotNull(message = "Target language is mandatory")
    private Locale targetLanguage;

    @OneToMany(mappedBy = "vocabulary",
            cascade = CascadeType.PERSIST,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    @OrderBy("id ASC")
    private Set<Entry> entries = new HashSet<>();

    public Vocabulary(Long id, String name, Locale sourceLanguage, Locale targetLanguage) {
        super();

        this.id = id;
        this.name = name;
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
    }

    public Vocabulary(Long id) {
        super();
        this.id = id;
    }

    public Vocabulary() {
        super();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Locale getSourceLanguage() {
        return this.sourceLanguage;
    }

    public void setSourceLanguage(Locale src) {
        this.sourceLanguage = src;
    }

    public Locale getTargetLanguage() {
        return this.targetLanguage;
    }

    public void setTargetLanguage(Locale dst) {
        this.targetLanguage = dst;
    }

    public Set<Entry> getEntries() {
        return this.entries;
    }

    public void setEntries(Set<Entry> entries) {
        for (Entry e : entries) {
            e.setVocabulary(this);
        }
        this.entries = entries;
    }

    public void addEntry(Entry entry) {
        this.entries.add(entry);
        entry.setVocabulary(this);
    }

    @Override
    public String toString() {
        return "Vocabulary [id=" + id
                + ", name=" + name
                + ", SrcLang=" + sourceLanguage
                + ", DstLang=" + targetLanguage
                + "]";
    }
}
