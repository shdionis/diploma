package ru.yandex.sharov.example.notes.repository.local.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ru.yandex.sharov.example.notes.entities.Note;

@Database(entities = {Note.class}, version = 2, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DB_NAME = "notes_DB";

    @Nullable
    private static NotesDatabase instance;

    @NonNull
    public static NotesDatabase getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, NotesDatabase.class, DB_NAME)
                            .addMigrations(Migrations.MIGRATION_1_2)
                            .build();
                }
            }
        }
        return instance;
    }

    @NonNull
    public abstract NoteDao getNoteDao();
}
