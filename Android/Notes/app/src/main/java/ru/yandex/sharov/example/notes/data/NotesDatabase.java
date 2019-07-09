package ru.yandex.sharov.example.notes.data;

import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.RoomDatabase;

import java.util.List;

@Database(entities = {Note.class}, version = 1)
public abstract class NotesDatabase extends RoomDatabase {
    public abstract NoteDao getNoteDao();

    @Override
    public void init(@NonNull DatabaseConfiguration configuration) {
        super.init(configuration);
        DatabaseInteractionsFactory interactionsFactory = DatabaseInteractionsFactory.newInstance(this.getNoteDao());
        AsyncTask<Void, Void, List<Note>> genNotes = interactionsFactory.createNoteGenAsyncTask();
        genNotes.execute();
    }
}
