package ru.yandex.sharov.example.notes.data;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBHelperStub {

    private static final Object lock = new Object();
    private static DBHelperStub instance;
    @NonNull
    private final MutableLiveData<List<Note>> data = new MutableLiveData<>();

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
    }

    private void lazyInitData() {
        NoteGenAsyncTask noteGenAsyncTask = new NoteGenAsyncTask(data);
        noteGenAsyncTask.execute();
    }

    @NonNull
    public LiveData<List<Note>> getData() {
        if (data.getValue() == null) {
            lazyInitData();
        }
        return data;
    }

    public void addOrUpdateNote(@NonNull Note note) {
        List<Note> notes = getDataValue();
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId() == note.getId()) {
                notes.remove(i);
                notes.add(i, note);
                data.setValue(notes);
                return;
            }
        }
        notes.add(note);
        data.setValue(notes);
    }

    public void removeNote(int noteId) {
        List<Note> notes = getDataValue();
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId() == noteId) {
                notes.remove(i);
                break;
            }
        }
        data.setValue(notes);
    }

    @Nullable
    public Note getNoteById(int noteId) {
        for (Note note : getDataValue()) {
            if (note.getId() == noteId) {
                return note;
            }
        }
        return null;
    }

    @NonNull
    private List<Note> getDataValue() {
        return data.getValue() == null ? Collections.emptyList() : data.getValue();
    }

    private static class NoteGenAsyncTask extends AsyncTask<Integer, Void, List<Note>> {

        private final MutableLiveData<List<Note>> data;

        NoteGenAsyncTask(MutableLiveData<List<Note>> data) {
            this.data = data;
        }

        @Override
        protected List<Note> doInBackground(Integer... args) {
            List<Note> generatedData = new ArrayList<>();
            final int TIME_STEP = 30 * 1000 * 60;
            final int ITERATIONS = 10000;
            final int TEXT_WORDS_COUNT = 30;
            final int SLEEP_TIME = 1500;

            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 1; i <= ITERATIONS; i++) {
                StringBuilder textNote = new StringBuilder();
                for (int j = 0; j < TEXT_WORDS_COUNT; j++) {
                    textNote.append("Note").append(i).append(j);
                }
                Note note = new Note("Note" + i, System.currentTimeMillis() - i*(long)TIME_STEP, textNote.toString());
                generatedData.add(note);
            }
            return generatedData;
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            this.data.postValue(notes);
        }
    }
}
