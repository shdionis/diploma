package ru.yandex.sharov.example.notes.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RemoteNote {

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

    public RemoteNote() {
    }

    public RemoteNote(@NonNull String guid, @NonNull String title, @Nullable String content, @NonNull Long date, @NonNull Boolean deleted) {
        this.guid = guid;
        this.title = title;
        this.content = content;
        this.date = date;
        this.deleted = deleted;
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
    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(@NonNull Boolean deleted) {
        this.deleted = deleted;
    }
}
