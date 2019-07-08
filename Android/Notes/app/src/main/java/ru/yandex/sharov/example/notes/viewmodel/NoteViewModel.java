package ru.yandex.sharov.example.notes.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.yandex.sharov.example.notes.data.DBHelperStub;
import ru.yandex.sharov.example.notes.data.Note;

public class NoteViewModel extends ViewModel {
    MutableLiveData<Note> note = new MutableLiveData<>();
    @NonNull
    DBHelperStub dbHelper;

    public NoteViewModel(@NonNull DBHelperStub dbHelper) {
        this.dbHelper = dbHelper;
    }

    @NonNull
    public LiveData<Note> getNote() {
        return note;
    }

    public void addOrUpdateNote() {
        dbHelper.addOrUpdateNote(this.note.getValue());
    }

    public void removeNote() {
        dbHelper.removeNote(this.note.getValue().getId());
    }

    public void getNoteById(@Nullable Integer noteId) {
        if(noteId==null) {
            this.note.setValue(new Note());
            return;
        }
        Note note = dbHelper.getNoteById(noteId);
        if (note != null) {
            this.note.setValue(note);
        } else {
            this.note.setValue(new Note());
        }
    }
}
