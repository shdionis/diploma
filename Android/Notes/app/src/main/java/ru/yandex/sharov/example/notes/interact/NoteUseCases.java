package ru.yandex.sharov.example.notes.interact;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Collection;
import java.util.List;

import ru.yandex.sharov.example.notes.entities.Note;
import ru.yandex.sharov.example.notes.interact.interactions.StateRestInteraction;

public interface NoteUseCases {

    void addOrUpdateNotes(@NonNull Note note);

    void removeNote(@NonNull Long id);

    @NonNull
    LiveData<Note> getNote(@NonNull Long id, @NonNull MutableLiveData<Note> noteData);
}
