package ru.yandex.sharov.example.notes.interact;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.lifecycle.LiveData;

import java.util.Collection;
import java.util.List;

import ru.yandex.sharov.example.notes.entities.Note;
import ru.yandex.sharov.example.notes.interact.interactions.StateRestInteraction;

public interface NotesListUseCases {
    @NonNull
    LiveData<List<Note>> getAllNotes();

    void addOrUpdateNotes(@NonNull Collection<Note> note);

    void pullDataToLocalStorage(@NonNull Consumer<StateRestInteraction> consumer);
}
