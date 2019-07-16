package ru.yandex.sharov.example.notes.interact.interactions;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.http.PATCH;
import ru.yandex.sharov.example.notes.data.NotesDatabase;
import ru.yandex.sharov.example.notes.model.Note;
import ru.yandex.sharov.example.notes.model.util.DataConvertUtil;

public class DatabaseInteractionsFactory {

    @NonNull
    private final NotesDatabase notesDB;

    private DatabaseInteractionsFactory(@NonNull NotesDatabase notesDB) {
        this.notesDB = notesDB;
    }

    @NonNull
    public static DatabaseInteractionsFactory newInstance(@NonNull NotesDatabase notesDao) {
        return new DatabaseInteractionsFactory(notesDao);
    }

    @NonNull
    public AsyncTask<Void, Void, Note> createSelectOneNoteTask(@NonNull Long id, @NonNull MutableLiveData liveDataConsumer) {
        return new SelectOneNoteAsyncTask(id, liveDataConsumer);
    }

    @NonNull
    public AsyncTask<Note, Void, Void> createInsertNotesTask() {
        return new InsertNotesAsyncTask();
    }

    @NonNull
    public AsyncTask<Long, Void, Void> createDeleteNotesTask() {
        return new DeleteNoteAsyncTask();
    }

    @NonNull
    public AsyncTask<Note, Void, Void> createMergeNotesTask() {
        return new MergeNotesTask();
    }

    private class DeleteNoteAsyncTask extends AsyncTask<Long, Void, Void> {
        @Override
        protected Void doInBackground(Long... ids) {
            notesDB.getNoteDao().deleteNotesByIds(ids);
            return null;
        }
    }

    private class SelectOneNoteAsyncTask extends AsyncTask<Void, Void, Note> {

        @NonNull
        private final Long id;
        @NonNull
        private final MutableLiveData liveDataConsumer;

        public SelectOneNoteAsyncTask(@NonNull Long id, @NonNull MutableLiveData liveDataConsumer) {
            this.id = id;
            this.liveDataConsumer = liveDataConsumer;
        }

        @Nullable
        @Override
        protected Note doInBackground(Void... voids) {
            return notesDB.getNoteDao().getNotesById(id);
        }

        @Override
        protected void onPostExecute(@Nullable Note note) {
            if (note == null) {
                liveDataConsumer.setValue(new Note());
            } else {
                liveDataConsumer.setValue(note);
            }
        }
    }

    private class InsertNotesAsyncTask extends AsyncTask<Note, Void, Void> {

        @Override
        protected Void doInBackground(Note... notes) {
            notesDB.getNoteDao().insertOrUpdateNotes(Arrays.asList(notes));
            return null;
        }
    }

    private class MergeNotesTask extends AsyncTask<Note, Void, Void> {

        @Override
        protected Void doInBackground(Note... notes) {
            notesDB.beginTransaction();
            try {
                Map<String, Note> toUpdate = new HashMap<>();
                Deque<String> toDelete = new LinkedList<>();
                for(Note n : notes) {
                    if(Boolean.TRUE.equals(n.getDeleted())) {
                        toDelete.add(n.getGuid());
                    } else {
                        toUpdate.put(n.getGuid(), n);
                    }
                }
                Set<String> partToDelete  = new HashSet<>();
                int i = 0;
                while (!toDelete.isEmpty()) {
                    while(i<=100 && !toDelete.isEmpty()) {
                        partToDelete.add(toDelete.poll());
                        i++;
                    }
                    notesDB.getNoteDao().deleteNotesByUIDs(partToDelete);
                    partToDelete.clear();
                    i=0;
                }
                List<Note> existingNotes = notesDB.getNoteDao().getAllNotesList();
                for(Note n : existingNotes) {
                    Note updating = toUpdate.get(n.getGuid());
                    if(updating!=null) {
                        updating.setId(n.getId());
                    }
                }
                notesDB.getNoteDao().insertOrUpdateNotes(toUpdate.values());
                notesDB.setTransactionSuccessful();
            } finally {
                notesDB.endTransaction();
            }
            return null;
        }
    }

}
