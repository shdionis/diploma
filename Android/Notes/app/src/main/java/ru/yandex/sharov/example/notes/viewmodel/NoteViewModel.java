package ru.yandex.sharov.example.notes.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.yandex.sharov.example.notes.data.DBHelperStub;
import ru.yandex.sharov.example.notes.data.Note;

public class NoteViewModel extends ViewModel {
    @NonNull
    private final MutableLiveData<Note> NOTE = new MutableLiveData<>();
    @NonNull
    private DBHelperStub dbHelper;

    public NoteViewModel(@NonNull DBHelperStub dbHelper) {
        this.dbHelper = dbHelper;
    }

    @NonNull
    public LiveData<Note> getNote() {
        return NOTE;
    }

    public void addOrUpdateNote() {
        if(NOTE.getValue()!=null) {
            dbHelper.addOrUpdateNote(NOTE.getValue());
        }
    }

    public void removeNote() {
        if(NOTE.getValue() == null) {
            return;
        }
        dbHelper.removeNote(NOTE.getValue().getId());
    }

    public void getNoteById(@Nullable Integer noteId) {
        if (noteId == null) {
            this.NOTE.setValue(new Note());
            return;
        }
        Note note = dbHelper.getNoteById(noteId);
        if (note != null) {
            this.NOTE.setValue(note);
        } else {
            this.NOTE.setValue(new Note());
        }
    }

    public void saveState(String text, String title) {
        if(NOTE.getValue() == null) {
            return;
        }
        Note n = NOTE.getValue();
        n.setDate(System.currentTimeMillis());
        n.setText(text);
        n.setTitle(title);
        NOTE.setValue(n);
    }
}
