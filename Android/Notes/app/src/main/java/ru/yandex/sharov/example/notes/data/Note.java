package ru.yandex.sharov.example.notes.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.atomic.AtomicInteger;

import ru.yandex.sharov.example.notes.util.FormatUtil;

public class Note {
    private static AtomicInteger count = new AtomicInteger();

    private int id;
    private long date;
    @NonNull
    private String title;
    @Nullable
    private String text;

    public Note() {
        id = count.decrementAndGet();
        date = System.currentTimeMillis();
    }

    public Note(@NonNull String title, long date, @Nullable String text) {
        this();
        this.title = title;
        this.date = date;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public long getDateLong() {
        return date;
    }

    @NonNull
    public String getDate() {
        return FormatUtil.getDateFormated(date);
    }

    @NonNull
    public String getShortDate() {
        return FormatUtil.getShortDateFormated(date);
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Nullable
    public String getText() {
        return text;
    }

    public void setText(@Nullable String text) {
        this.text = text;
    }

    @NonNull
    @Override
    public String toString() {
        return "Note{" +
                "id='" + id + '\'' +
                "title='" + title + '\'' +
                ", date=" + getShortDate() +
                ", text='" + text + '\'' +
                '}';
    }
}
