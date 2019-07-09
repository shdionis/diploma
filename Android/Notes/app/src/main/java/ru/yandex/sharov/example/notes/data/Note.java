package ru.yandex.sharov.example.notes.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import ru.yandex.sharov.example.notes.util.FormatUtil;

@Entity
public class Note {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long date;
    @NonNull
    private String title = "";
    @Nullable
    private String text;

    public Note() {
        date = System.currentTimeMillis();
    }

    @Ignore
    public Note(@NonNull Long id, @NonNull String title, long date, @Nullable String text) {
        this();
        this.id = id;
        this.title = title;
        this.date = date;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @NonNull
    public String getShortFormatDate() {
        return FormatUtil.getShortDateFormated(date);
    }

    @NonNull
    public String getLongFormatDate() {
        return FormatUtil.getDateFormated(date);
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
                ", date=" + getShortFormatDate() +
                ", text='" + text + '\'' +
                '}';
    }
}
