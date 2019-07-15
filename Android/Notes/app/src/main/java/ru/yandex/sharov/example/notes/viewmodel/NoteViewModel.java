package ru.yandex.sharov.example.notes.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.yandex.sharov.example.notes.interact.LocalRepositoryNoteInteractor;
import ru.yandex.sharov.example.notes.model.Note;

public class NoteViewModel extends ViewModel {
    @NonNull
    private final MutableLiveData<Note> note = new MutableLiveData<>();
    @NonNull
    private LocalRepositoryNoteInteractor dbHelper;

    public NoteViewModel(@NonNull LocalRepositoryNoteInteractor dbHelper) {
        this.dbHelper = dbHelper;
    }

    @NonNull
    public LiveData<Note> getNote() {
        return note;
    }

    public void addOrUpdateNote() {
        if(note.getValue()!=null) {
            dbHelper.addOrUpdateNote(note.getValue());
        }
    }

    public void removeNote() {
        if(note.getValue() == null) {
            return;
        }
        dbHelper.removeNote(note.getValue().getId());
    }

    public void getNoteById(@Nullable Long noteId) {
        if (noteId == null) {
            this.note.setValue(new Note());
            return;
        }
        dbHelper.getNoteById(noteId, this.note);
    }

    public void saveState(String text, String title) {
        if(note.getValue() == null) {
            return;
        }
        Note n = note.getValue();
        n.setDate(System.currentTimeMillis());
        n.setContent(text);
        n.setTitle(title);
        note.setValue(n);
    }
}
