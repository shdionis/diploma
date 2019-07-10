package ru.yandex.sharov.example.notes.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class NoteInteractor {

    private static final Object lock = new Object();
    private static NoteInteractor instance;
    @NonNull
    private final LiveData<List<Note>> data;
    @NonNull
    private final DatabaseInteractionsFactory interactionsFactory;
    @NonNull
    private final NotesDatabase notesDB;

    @NonNull
    public static NoteInteractor getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new NoteInteractor(context);
                }
                return instance;
            }
        } else return instance;
    }

    private NoteInteractor(@NonNull Context context) {
        notesDB = NotesDatabase.getInstance(context);
        interactionsFactory = DatabaseInteractionsFactory.newInstance(notesDB.getNoteDao());
        data = notesDB.getNoteDao().getAllNotes();
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
}
