package ru.yandex.sharov.example.notes;

public class Note {
    private String tite;
    private String date;
    private String text;

    public Note(String tite, String date, String text) {
        this.tite = tite;
        this.date = date;
        this.text = text;
    }

    public String getTite() {
        return tite;
    }

    public void setTite(String tite) {
        this.tite = tite;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public String getShortText(int length) {
        if(length > text.length()) {
            return text;
        }
        return text.substring(0, length).concat("...");
    }

    public void setText(String text) {
        this.text = text;
    }
}
