package ru.yandex.sharov.example.notes.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class NoteInteractor {

    private static final Object LOCK = new Object();
    @Nullable
    private static NoteInteractor instance;
    @NonNull
    private final MutableLiveData<List<Note>> data;
    @NonNull
    private LiveData<List<Note>> dbData;
    @NonNull
    private DatabaseInteractionsFactory interactionsFactory;

    private NoteInteractor(@NonNull Context context) {
        data = new MutableLiveData<>();
        initDataLayer(context);
    }

    private void initDataLayer(@NonNull Context context) {
        Consumer<NotesDatabase> consumer = notesDatabase -> {
            interactionsFactory = DatabaseInteractionsFactory.newInstance(notesDatabase.getNoteDao());
            dbData = notesDatabase.getNoteDao().getAllNotes();
            dbData.observeForever(notes -> data.setValue(notes));

        };

        InitDastabase initDastabase = new InitDastabase(context, consumer);
        initDastabase.execute();
    }

    @NonNull
    public static NoteInteractor getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new NoteInteractor(context);
                }
                return instance;
            }
        }
        return instance;
    }


    @NonNull
    public LiveData<List<Note>> getData() {
        return data;
    }

    public void addOrUpdateNote(@NonNull Note note) {
        interactionsFactory.createInsertNotesTask().execute(note);
    }

    public void removeNote(long noteId) {
        AsyncTask<Long, Void, Void> removeNoteTask = interactionsFactory.createDeleteNotesTask();
        removeNoteTask.execute(noteId);
    }

    @Nullable
    public LiveData<Note> getNoteById(long noteId, @NonNull MutableLiveData<Note> noteData) {
        AsyncTask<Void, Void, Note> getNoteTask = interactionsFactory.createSelectOneNoteTask(noteId, noteData);
        getNoteTask.execute();
        return noteData;
    }

    private static class InitDastabase extends AsyncTask<Void, Void, NotesDatabase> {

        @NonNull
        private final Context context;
        private final Consumer<NotesDatabase> dbConsumer;

        private InitDastabase(@NonNull Context context, Consumer<NotesDatabase> dbConsumer) {
            this.context = context;
            this.dbConsumer = dbConsumer;
        }

        @Override
        protected NotesDatabase doInBackground(Void... voids) {
            return NotesDatabase.getInstance(context);
        }

        @Override
        protected void onPostExecute(NotesDatabase notesDB) {
            dbConsumer.accept(notesDB);
        }
    }
}
