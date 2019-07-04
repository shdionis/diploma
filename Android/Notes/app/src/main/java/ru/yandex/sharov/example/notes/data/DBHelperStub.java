package ru.yandex.sharov.example.notes.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelperStub {

    private static final Object lock = new Object();
    private static DBHelperStub instance;

    private List<Note> data;

    @NonNull
    public static DBHelperStub getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new DBHelperStub();
                }
                return instance;
            }
        } else return instance;

    }

    private DBHelperStub() {
        initData();
    }

    private void initData() {
        data = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            StringBuilder textNote = new StringBuilder();
            for (int j = 0; j < 30; j++) {
                textNote.append("Note").append(i).append(j);
            }
            Note note = new Note("Note" + i, System.currentTimeMillis() - i, textNote.toString());
            data.add(note);
        }
    }

    public List<Note> getData() {
        return data;
    }

    public void addOrUpdateNote(@NonNull Note note) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId() == note.getId()) {
                data.remove(i);
                data.add(i, note);
                return;
            }
        }
        data.add(note);

    }

    public void removeNote(Integer noteId) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId() == noteId) {
                data.remove(i);
                return;
            }
        }
    }

    @Nullable
    public Note getNoteById(int noteId) {
        for (Note note : data) {
            if (note.getId() == noteId) {
                return note;
            }
        }
        return null;
    }
}
