package ru.yandex.sharov.example.notes.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.UUID;

import ru.yandex.sharov.example.notes.util.FormatUtil;

@Entity(tableName = "notes")
public class Note {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private Long id;
    @NonNull
    private String guid;
    @NonNull
    private String title;
    @Nullable
    private String content;
    @NonNull
    private Long date;
    @NonNull
    private Boolean deleted;

    public Note() {
        this.date = System.currentTimeMillis();
        this.title = "";
        this.guid = UUID.randomUUID().toString();
        this.deleted = Boolean.FALSE;
    }

    @Ignore
    public Note(@NonNull Long id, @NonNull String title, @NonNull Long date, @Nullable String content) {
        this();
        this.id = id;
        this.title = title;
        this.date = date;
        this.content = content;
    }

    @Ignore
    public Note(@NonNull String guid, @NonNull String title, @Nullable String content, @NonNull Long date, @NonNull Boolean deleted) {
        this.guid = guid;
        this.title = title;
        this.content = content;
        this.date = date;
        this.deleted = deleted;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    @NonNull
    public String getGuid() {
        return guid;
    }

    public void setGuid(@NonNull String guid) {
        this.guid = guid;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @Nullable
    public String getContent() {
        return content;
    }

    public void setContent(@Nullable String content) {
        this.content = content;
    }

    @NonNull
    public Long getDate() {
        return date;
    }

    public void setDate(@NonNull Long date) {
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

    @NonNull
    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(@NonNull Boolean deleted) {
        this.deleted = deleted;
    }
}