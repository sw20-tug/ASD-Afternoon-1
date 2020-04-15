package at.tugraz.sw20asd.lang.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Vocabulary {
    public Vocabulary() {
    }

    public Vocabulary(Integer id, String name, Locale sourceLanguage, Locale targetLanguage) {
        super();

        this._id = id;
        this._name = name;
        this._sourceLanguage = sourceLanguage;
        this._targetLanguage = targetLanguage;

        this._entries = new ArrayList<>();
    }

    private Integer _id;
    private String _name;
    private Locale _sourceLanguage;
    private Locale _targetLanguage;
    private List<Entry> _entries;

    public Integer getID() {
        return this._id;
    }

    public void setId(Integer id) {
        this._id = id;
    }

    public String getName() {
        return this._name;
    }

    public void setName (String name) {
        this._name = name;
    }

    public Locale getSourceLanguage() {
        return this._sourceLanguage;
    }

    public void setSourceLanguage(Locale src) {
        this._sourceLanguage = src;
    }

    public Locale getTargetLanguage () {
        return this._targetLanguage;
    }

    public void setTargetLanguage(Locale dst) {
        this._targetLanguage = dst;
    }

    public List<Entry> getEntries() {
        return this._entries;
    }

    public void setEntries(List<Entry> entries) {
        this._entries = entries;
    }

    public boolean addPhrase(Entry phrase) {
        return _entries.add(phrase);
    }

    @Override
    public String toString() {
        return "Vocabulary [id=" + _id
                + ", name=" + _name
                + ", SrcLang=" + _sourceLanguage
                + ", DstLang=" + _targetLanguage
                + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;
        if(!(obj instanceof Vocabulary))
            return false;
        Vocabulary v = (Vocabulary)obj;
        return _id.equals(v._id)
                && _name.equals(v._name)
                && _sourceLanguage.equals(v._sourceLanguage)
                && _targetLanguage.equals(v._targetLanguage);
    }
}
