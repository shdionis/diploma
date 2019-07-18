package ru.yandex.sharov.example.notes.interact;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Collection;
import java.util.List;

import ru.yandex.sharov.example.notes.entities.Note;
import ru.yandex.sharov.example.notes.interact.interactions.LocalStorageInteractionsFactory;
import ru.yandex.sharov.example.notes.interact.interactions.NetworkInteractionsFactory;
import ru.yandex.sharov.example.notes.interact.interactions.StateRestInteraction;
import ru.yandex.sharov.example.notes.repository.local.LocalStorageRepository;
import ru.yandex.sharov.example.notes.repository.local.PreferencesRepository;
import ru.yandex.sharov.example.notes.repository.remote.RemoteServiceRepository;

public class NotesInteractor implements NotesListUseCases, NoteUseCases {

    private static final Object LOCK = new Object();
    @Nullable
    private static volatile NotesInteractor instance;
    @NonNull
    private LocalStorageRepository localRepo;
    @NonNull
    private PreferencesRepository prefsRepos;
    @NonNull
    private RemoteServiceRepository remoteRepo;
    @NonNull
    private LocalStorageInteractionsFactory interactionsFactory;

    @NonNull
    public static NotesInteractor getInstance(@NonNull LocalStorageRepository localRepo,
                                              @NonNull RemoteServiceRepository remoteRepo,
                                              @NonNull PreferencesRepository prefsRepos) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new NotesInteractor(localRepo, remoteRepo, prefsRepos);
                }
                return instance;
            }
        }
        return instance;
    }

    private NotesInteractor(@NonNull LocalStorageRepository localRepo,
                            @NonNull RemoteServiceRepository remoteRepo,
                            @NonNull PreferencesRepository prefsRepos) {
        this.localRepo = localRepo;
        this.remoteRepo = remoteRepo;
        this.prefsRepos = prefsRepos;
        this.interactionsFactory = LocalStorageInteractionsFactory.newInstance(localRepo);
    }

    @Override
    public LiveData<List<Note>> getAllNotes() {
        return localRepo.getAllNotes();
    }

    @Override
    public void addOrUpdateNotes(@NonNull Note note) {
        interactionsFactory.createInsertNotesTask().execute(note);
    }

    @Override
    public void addOrUpdateNotes(@NonNull Collection<Note> notes) {
        interactionsFactory.createMergeNotesTask(notes).execute();
    }

    @Override
    public void removeNote(@NonNull Long id) {
        interactionsFactory.createDeleteNotesTask().execute(id);
    }

    @Override
    public LiveData<Note> getNote(@NonNull Long id, @NonNull MutableLiveData<Note> noteData) {
        interactionsFactory.createSelectOneNoteTask(id, noteData).execute();
        return noteData;
    }

    @Override
    public void pullDataToLocalStorage(@NonNull Consumer<StateRestInteraction> consumer) {
        NetworkInteractionsFactory.executeGetRemoteNotesTask(
                remoteRepo.getAllNotes(prefsRepos.getVersion(), prefsRepos.getUser()),
                this,
                consumer
        );
    }
}
