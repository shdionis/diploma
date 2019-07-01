package ru.yandex.sharov.example.notes;

import java.text.SimpleDateFormat;

public class Note {
    private String title;
    private long date;
    private String text;
    private SimpleDateFormat format;
    private SimpleDateFormat shortFormat;

    public Note(String tite, long date, String text) {
        this.title = tite;
        this.date = date;
        this.text = text;
        format = new SimpleDateFormat("dd MM yyyy, HH:mm");
        shortFormat = new SimpleDateFormat("dd.MM");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return format.format(date);
    }

    public String getShortDate() {
        return shortFormat.format(date);
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

    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                ", date=" + date +
                ", text='" + getShortText(10) + '\'' +
                ", format=" + format +
                ", shortFormat=" + shortFormat +
                '}';
    }
}
