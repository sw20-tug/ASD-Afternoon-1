package at.tugraz.sw20asd.lang;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Locale;

@JsonPropertyOrder({"id", "name", "sourceLanguage", "targetLanguage"})
public class VocabularyBaseDto {
    private Long _id;

    private String _name;

    private Locale _sourceLanguage;

    private Locale _targetLanguage;

    public VocabularyBaseDto() {
    }

    public VocabularyBaseDto(Long id, String name, Locale src, Locale dst) {
        _id = id;
        _name = name;
        _sourceLanguage = src;
        _targetLanguage = dst;
    }

    public Long getId() {
        return _id;
    }

    public void setId(Long id) {
        _id = id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public Locale getSourceLanguage() {
        return _sourceLanguage;
    }

    public void setSourceLanguage(Locale lang) {
        _sourceLanguage = lang;
    }

    public Locale getTargetLanguage() {
        return _targetLanguage;
    }

    public void setTargetLanguage(Locale lang) {
        _targetLanguage = lang;
    }
}
