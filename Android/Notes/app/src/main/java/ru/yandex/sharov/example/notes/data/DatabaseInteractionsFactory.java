package ru.yandex.sharov.example.notes.data;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

public class DatabaseInteractionsFactory {

    @NonNull
    private final NoteDao notesDao;

    private DatabaseInteractionsFactory(@NonNull NoteDao notesDB) {
        this.notesDao = notesDB;
    }

    @NonNull
    public static DatabaseInteractionsFactory newInstance(@NonNull NoteDao notesDao) {
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

    private class DeleteNoteAsyncTask extends AsyncTask<Long, Void, Void> {
        @Override
        protected Void doInBackground(Long... ids) {
            notesDao.deleteNotes(ids);
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
            return notesDao.getNotesById(id);
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
            notesDao.insertOrUpdateNotes(notes);
            return null;
        }
    }

}
