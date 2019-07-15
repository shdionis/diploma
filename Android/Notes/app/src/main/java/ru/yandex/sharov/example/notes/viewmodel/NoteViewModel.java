package ru.yandex.sharov.example.notes.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import ru.yandex.sharov.example.notes.interact.LocalRepositoryNoteInteractor;
import ru.yandex.sharov.example.notes.model.Note;

public class NoteViewModel extends ViewModel {
    @NonNull
    private final MutableLiveData<Note> note = new MutableLiveData<>();
    @NonNull
    private LocalRepositoryNoteInteractor dbHelper;
    @NonNull
    private ObservableField<Note> noteBind = new ObservableField<>();
    @Nullable
    private Observer<Note> bindingObserver = new Observer<Note>() {
        @Override
        public void onChanged(Note n) {
            if(n != null) {
                noteBind.set(n);
                note.removeObserver(bindingObserver);
            }
        }
    };

    public NoteViewModel(@NonNull LocalRepositoryNoteInteractor dbHelper) {
        this.dbHelper = dbHelper;
        note.observeForever(bindingObserver);
    }

    @NonNull
    public LiveData<Note> getNote() {
        return note;
    }

    public void addOrUpdateNote() {
        Note n = noteBind.get();
        if(n!=null) {
            n.setDate(System.currentTimeMillis());
            dbHelper.addOrUpdateNote(n);
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

    @Nullable
    public ObservableField<Note> getNoteBind() {
        return noteBind;
    }
}
