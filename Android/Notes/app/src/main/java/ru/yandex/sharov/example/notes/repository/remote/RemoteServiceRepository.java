package ru.yandex.sharov.example.notes.repository.remote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import retrofit2.Call;
import ru.yandex.sharov.example.notes.entities.RemoteNote;

public class RemoteServiceRepository {
    @NonNull
    private static final Object LOCK = new Object();

    @Nullable
    private static volatile RemoteServiceRepository instance;

    @NonNull
    private NotesService service;

    private RemoteServiceRepository() {
        this.service = NotesService.getInstance();
    }

    public static RemoteServiceRepository getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new RemoteServiceRepository();
                }
            }
        }
        return instance;
    }

    @NonNull
    public Call<List<RemoteNote>> getAllNotes(@NonNull int version, @NonNull String name) {
        return service.getAllNotesApi().getAllNotes(version, name);
    }

    @NonNull
    public Call<List<RemoteNote>> syncAllNotes(@NonNull int version, @NonNull String user, @NonNull List<RemoteNote> notes) {
        return service.getAllNotesApi().syncAllNotes(version, user, notes);
    }
}
