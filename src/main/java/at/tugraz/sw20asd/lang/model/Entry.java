package at.tugraz.sw20asd.lang.model;

public class Entry{
    public Entry() {
    }

    public Entry(String phrase, String translation) {
        this._phrase = phrase;
        this._translation = translation;
    }

    private String _phrase;
    private String _translation;

    public String getPhrase() {
        return this._phrase;
    }

    public void setPhrase(String phrase) {
        this._phrase = phrase;
    }

    public String getTranslation() {
        return this._translation;
    }

    public void setTranslation(String translation) {
        this._translation = translation;
    }

}
