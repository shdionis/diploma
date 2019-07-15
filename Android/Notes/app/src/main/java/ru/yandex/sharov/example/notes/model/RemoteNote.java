package ru.yandex.sharov.example.notes.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RemoteNote {

    @SerializedName("guid")
    @Expose
    private String guid;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("date")
    @Expose
    private Long date;

    @SerializedName("deleted")
    @Expose
    private Boolean deleted;

    public RemoteNote() {
    }

    public RemoteNote(Note note) {
        this.guid = note.getGuid();
        this.title = note.getTitle();
        this.content = note.getContent();
        this.date = note.getDate();
        this.deleted = note.getDeleted();
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
