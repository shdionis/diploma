package ru.yandex.sharov.example.notes.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelperStub {

    private static final String LOG_TAG = "LOG_TAG";
    private List<Note> data;
    private static DBHelperStub instance;

    public static DBHelperStub getInstance() {
        if (instance == null) {
            instance = new DBHelperStub();
        }
        return instance;

    }

    private DBHelperStub() {
        initData();
    }

    private void initData() {
        Log.d(LOG_TAG, getClass().getSimpleName() + " onCreate");
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

    private void addOrUpdateNote(Note note) {
        data.add(note);
    }
}
