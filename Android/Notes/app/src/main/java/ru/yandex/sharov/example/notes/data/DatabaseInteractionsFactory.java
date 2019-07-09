package ru.yandex.sharov.example.notes.data;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseInteractionsFactory {

    private final NoteDao notesDao;

    @NonNull
    public static DatabaseInteractionsFactory newInstance(@NonNull NoteDao notesDao) {
        return new DatabaseInteractionsFactory(notesDao);
    }

    private DatabaseInteractionsFactory(@NonNull NoteDao notesDB) {
        this.notesDao = notesDB;
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
    public AsyncTask<Void, Void, List<Note>> createNoteGenAsyncTask() {
        return new NoteGenAsyncTask(new InsertNotesAsyncTask());
    }

    private class DeleteNoteAsyncTask extends AsyncTask<Long, Void, Void> {
        @Override
        protected Void doInBackground(Long... ids) {
            notesDao.deleteNotes(ids);
            return null;
        }
    }

    private class SelectOneNoteAsyncTask extends AsyncTask<Void, Void, Note> {

        private Long id;
        private MutableLiveData liveDataConsumer;

        public SelectOneNoteAsyncTask(@NonNull Long id, @NonNull MutableLiveData liveDataConsumer) {
            this.id = id;
            this.liveDataConsumer = liveDataConsumer;
        }

        @Override
        protected Note doInBackground(Void... voids) {
            return notesDao.getNotesById(id);
        }

        @Override
        protected void onPostExecute(Note note) {
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
            notesDao.insertNotes(Arrays.asList(notes));
            return null;
        }
    }

    private class NoteGenAsyncTask extends AsyncTask<Void, Void, List<Note>> {

        private final AsyncTask<Note, Void, Void> insertNotesAsyncTask;

        public NoteGenAsyncTask(AsyncTask<Note, Void, Void> insertNotesAsyncTask) {
            this.insertNotesAsyncTask = insertNotesAsyncTask;
        }

        @Override
        protected List<Note> doInBackground(Void... args) {
            List<Note> generatedData = new ArrayList<>();
            final int TIME_STEP = 30 * 1000 * 60;
            final int ITERATIONS = 10000;
            final int TEXT_WORDS_COUNT = 30;
            final int SLEEP_TIME = 1500;

            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int i = 1; i <= ITERATIONS; i++) {
                StringBuilder textNote = new StringBuilder();
                for (int j = 0; j < TEXT_WORDS_COUNT; j++) {
                    textNote.append("Note").append(i).append(j);
                }
                Note note = new Note((long) i, "Note" + i, System.currentTimeMillis() - i * (long) TIME_STEP, textNote.toString());
                generatedData.add(note);
            }
            return generatedData;
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            if (notes == null || notes.isEmpty()) {
                return;
            }
            Note[] notesArr = new Note[notes.size()];
            notes.toArray(notesArr);
            insertNotesAsyncTask.execute(notesArr);
        }
    }
}
