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
    private final MutableLiveData<List<Note>> DATA = new MutableLiveData<>();

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
        NoteGenAsyncTask noteGenAsyncTask = new NoteGenAsyncTask(DATA);
        noteGenAsyncTask.execute();
    }

    @NonNull
    public LiveData<List<Note>> getData() {
        if (DATA.getValue() == null) {
            lazyInitData();
        }
        return DATA;
    }

    public void addOrUpdateNote(@NonNull Note note) {
        List<Note> notes = getDataValue();
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId() == note.getId()) {
                notes.remove(i);
                notes.add(i, note);
                DATA.setValue(notes);
                return;
            }
        }
        notes.add(note);
        DATA.setValue(notes);
    }

    public void removeNote(int noteId) {
        List<Note> notes = getDataValue();
        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId() == noteId) {
                notes.remove(i);
                break;
            }
        }
        DATA.setValue(notes);
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
        return DATA.getValue() == null ? Collections.emptyList() : DATA.getValue();
    }

    private static class NoteGenAsyncTask extends AsyncTask<Integer, Void, List<Note>> {

        private final MutableLiveData<List<Note>> DATA;

        NoteGenAsyncTask(MutableLiveData<List<Note>> data) {
            this.DATA = data;
        }

        @Override
        protected List<Note> doInBackground(Integer... args) {
            List<Note> generatedData = new ArrayList<>();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final int TIME_STEP = 30 * 1000 * 60;
            for (int i = 1; i <= 10000; i++) {
                StringBuilder textNote = new StringBuilder();
                for (int j = 0; j < 30; j++) {
                    textNote.append("Note").append(i).append(j);
                }
                Note note = new Note("Note" + i, System.currentTimeMillis() - i*(long)TIME_STEP, textNote.toString());
                generatedData.add(note);
            }
            return generatedData;
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            this.DATA.postValue(notes);
        }
    }
}
