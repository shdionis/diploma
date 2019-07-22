package ru.yandex.sharov.example.notes.repository.local;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import androidx.collection.ArraySet;
import androidx.lifecycle.LiveData;

import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.yandex.sharov.example.notes.entities.Note;
import ru.yandex.sharov.example.notes.repository.local.room.NoteDao;
import ru.yandex.sharov.example.notes.repository.local.room.NotesDatabase;


public class LocalStorageRepository {

    private static final Object LOCK = new Object();
    private static final String LOG_TAG = "[LOG_TAG:LocStorage]";

    @Nullable
    private static volatile LocalStorageRepository instance;

    @NonNull
    private NotesDatabase database;

    @NonNull
    private NoteDao dao;

    private LocalStorageRepository(@NonNull Context context) {
        this.database = NotesDatabase.getInstance(context);
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
        return dao.getAllNotesList();
    }

    @Nullable
    public Note getNotesById(long id) {
        return dao.getNotesById(id);
    }

    public void insertOrUpdateNotes(@NonNull Note... notes) {
        dao.insertOrUpdateNotes(notes);
    }


    public void deleteNotesByIds(@NonNull Long[] ids) {
        dao.deleteNotesByIds(ids);
    }

    public void deleteNotesByUIDs(@NonNull Set<String> ids) {
        dao.deleteNotesByUIDs(ids);
    }

    public void mergeNotesList(@NonNull Collection<Note> notes) {
        database.beginTransaction();
        try {
            Map<String, Note> toUpdate = new ArrayMap<>();
            Deque<String> toDelete = new LinkedList<>();
            for (Note n : notes) {
                if (Boolean.TRUE.equals(n.getDeleted())) {
                    toDelete.add(n.getGuid());
                } else {
                    toUpdate.put(n.getGuid(), n);
                }
            }
            Set<String> partToDelete = new ArraySet<>();
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
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Merge notes list into Database failure!", ex);
        } finally {
            database.endTransaction();
        }
    }
}
