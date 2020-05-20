package at.tugraz.sw20asd.lang.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"id", "phrase", "translation"})
public class EntryDto {
    private Long id;
    private String phrase;
    private String translation;

    public EntryDto() {
    }

    public EntryDto(String phrase, String translation) {
        this.phrase = phrase;
        this.translation = translation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
