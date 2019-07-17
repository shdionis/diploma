package ru.yandex.sharov.example.notes.repository.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.yandex.sharov.example.notes.entities.Note;
import ru.yandex.sharov.example.notes.repository.local.room.NoteDao;
import ru.yandex.sharov.example.notes.repository.local.room.NotesDatabase;


public class LocalStorageRepository {

    @NonNull
    private static final Object LOCK = new Object();

    @Nullable
    private static volatile LocalStorageRepository instance;

    @NonNull
    private NotesDatabase database;

    @NonNull
    private NoteDao dao;

    private LocalStorageRepository(@NonNull Context context) {
        this.database = NotesDatabase.getInstance(context);
        ;
        this.dao = database.getNoteDao();
    }

    @NonNull
    public static LocalStorageRepository getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new LocalStorageRepository(context);
                }
                return instance;
            }
        }
        return instance;
    }

    @NonNull
    public LiveData<List<Note>> getAllNotes() {
        return dao.getAllNotes();
    }

    @NonNull
    public List<Note> getAllNotesList() {
        return database.getNoteDao().getAllNotesList();
    }

    @Nullable
    public Note getNotesById(long id) {
        return database.getNoteDao().getNotesById(id);
    }

    public void insertOrUpdateNotes(@NonNull Note... notes) {
        database.getNoteDao().insertOrUpdateNotes(notes);
    }


    public void deleteNotesByIds(@NonNull Long[] ids) {
        database.getNoteDao().deleteNotesByIds(ids);
    }

    public void deleteNotesByUIDs(@NonNull Set<String> ids) {
        database.getNoteDao().deleteNotesByUIDs(ids);
    }

    public void mergeNotesList(Collection<Note> notes) {
        database.beginTransaction();
        try {
            Map<String, Note> toUpdate = new HashMap<>();
            Deque<String> toDelete = new LinkedList<>();
            for (Note n : notes) {
                if (Boolean.TRUE.equals(n.getDeleted())) {
                    toDelete.add(n.getGuid());
                } else {
                    toUpdate.put(n.getGuid(), n);
                }
            }
            Set<String> partToDelete = new HashSet<>();
            int i = 0;
            while (!toDelete.isEmpty()) {
                while (i <= 100 && !toDelete.isEmpty()) {
                    partToDelete.add(toDelete.poll());
                    i++;
                }
                deleteNotesByUIDs(partToDelete);
                partToDelete.clear();
                i = 0;
            }
            List<Note> existingNotes = getAllNotesList();
            for (Note n : existingNotes) {
                Note updating = toUpdate.get(n.getGuid());
                if (updating != null) {
                    updating.setId(n.getId());
                }
            }
            database.getNoteDao().insertOrUpdateNotes(toUpdate.values());
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }
}
