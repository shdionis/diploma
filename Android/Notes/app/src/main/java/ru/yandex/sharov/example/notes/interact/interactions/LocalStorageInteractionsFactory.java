package ru.yandex.sharov.example.notes.interact.interactions;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import java.util.Collection;

import ru.yandex.sharov.example.notes.entities.Note;
import ru.yandex.sharov.example.notes.repository.local.LocalStorageRepository;

public class LocalStorageInteractionsFactory {

    @NonNull
    private final LocalStorageRepository localRepo;

    private LocalStorageInteractionsFactory(@NonNull LocalStorageRepository notesDB) {
        this.localRepo = notesDB;
    }

    @NonNull
    public static LocalStorageInteractionsFactory newInstance(@NonNull LocalStorageRepository localRepo) {
        return new LocalStorageInteractionsFactory(localRepo);
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
    public AsyncTask<Void, Void, Void> createMergeNotesTask(Collection<Note> notes) {
        return new MergeNotesTask(notes);
    }

    private class DeleteNoteAsyncTask extends AsyncTask<Long, Void, Void> {
        @Override
        protected Void doInBackground(Long... ids) {
            localRepo.deleteNotesByIds(ids);
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
            return localRepo.getNotesById(id);
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
            localRepo.insertOrUpdateNotes(notes);
            return null;
        }
    }

    private class MergeNotesTask extends AsyncTask<Void, Void, Void> {
        @NonNull
        private final Collection<Note> notes;

        public MergeNotesTask(@NonNull Collection<Note> notes) {
            this.notes = notes;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            localRepo.mergeNotesList(notes);
            return null;
        }
    }

}
