package ru.yandex.sharov.example.notes.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import ru.yandex.sharov.example.notes.entities.Note;
import ru.yandex.sharov.example.notes.interact.NoteUseCases;

public class NoteViewModel extends ViewModel {
    private final MutableLiveData<Note> note = new MutableLiveData<>();
    @NonNull
    private NoteUseCases interactor;
    @NonNull
    private ObservableField<Note> noteBind = new ObservableField<>();
    @Nullable
    private Observer<Note> bindingObserver = new Observer<Note>() {
        @Override
        public void onChanged(Note n) {
            if (n != null) {
                noteBind.set(n);
                note.removeObserver(bindingObserver);
            }
        }
    };

    public NoteViewModel(@NonNull NoteUseCases interactor) {
        this.interactor = interactor;
        note.observeForever(bindingObserver);
    }

    @NonNull
    public LiveData<Note> getNote() {
        return note;
    }

    public void addOrUpdateNote() {
        Note n = noteBind.get();
        if (n != null) {
            n.setDate(System.currentTimeMillis());
            interactor.addOrUpdateNotes(n);
        }
    }

    public void removeNote() {
        if (note.getValue() == null) {
            return;
        }
        interactor.removeNote(note.getValue().getId());
    }

    public void getNoteById(@Nullable Long noteId) {
        if (noteId == null) {
            this.note.setValue(new Note());
            return;
        }
        interactor.getNote(noteId, this.note);
    }

    @Nullable
    public ObservableField<Note> getNoteBind() {
        return noteBind;
    }
}
