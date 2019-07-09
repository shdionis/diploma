package ru.yandex.sharov.example.notes.data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import java.util.Collections;
import java.util.List;

public class NoteInteractor {

    private static final Object lock = new Object();
    private static NoteInteractor instance;
    @NonNull
    private final LiveData<List<Note>> data;
    private final DatabaseInteractionsFactory interactionsFactory;
    private final NotesDatabase notesDB;

    @NonNull
    public static NoteInteractor getInstance(Context context) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new NoteInteractor(context);
                }
                return instance;
            }
        } else return instance;
    }

    private NoteInteractor(Context context) {
        notesDB = Room.databaseBuilder(context, NotesDatabase.class, "notesDB").build();
        interactionsFactory = DatabaseInteractionsFactory.newInstance(notesDB.getNoteDao());
        data = notesDB.getNoteDao().getAllNotes();

        init();
    }

    private void init() {
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
    public LiveData<Note> getNoteById(long noteId, MutableLiveData<Note> noteData) {
        AsyncTask<Void, Void, Note> getNoteTask = interactionsFactory.createSelectOneNoteTask(noteId, noteData);
        getNoteTask.execute();
        return noteData;
    }
}
