package ru.yandex.sharov.example.notes.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.yandex.sharov.example.notes.util.UIUtil;

public class DBHelperStub {

    private static final Object lock = new Object();
    private static DBHelperStub instance;
    @NonNull
    private MutableLiveData<List<Note>> data = new MutableLiveData<>();

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
        List<Note> generatedData = new ArrayList<>();
        final int TIME_STEP = 30 * 1000 * 60;
        for (int i = 1; i <= 50; i++) {
            StringBuilder textNote = new StringBuilder();
            for (int j = 0; j < 30; j++) {
                textNote.append("Note").append(i).append(j);
            }
            Note note = new Note("Note" + i, System.currentTimeMillis() - i * TIME_STEP, textNote.toString());
            generatedData.add(note);
        }
        data.setValue(generatedData);
    }

    @NonNull
    public LiveData<List<Note>> getData() {
        return data;
    }

    public void addOrUpdateNote(@NonNull Note note) {
        List<Note> notes = data.getValue();
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId() == note.getId()) {
                notes.remove(i);
                notes.add(i, note);
                return;
            }
        }
        notes.add(note);
        data.setValue(notes);
    }

    public void removeNote(int noteId) {
        List<Note> notes = data.getValue();
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId() == noteId) {
                notes.remove(i);
                return;
            }
        }
        data.setValue(notes);
    }

    @Nullable
    public Note getNoteById(int noteId) {
        for (Note note : data.getValue()) {
            if (note.getId() == noteId) {
                return note;
            }
        }
        return null;
    }
}
