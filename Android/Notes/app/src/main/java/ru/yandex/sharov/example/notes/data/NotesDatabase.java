package ru.yandex.sharov.example.notes.data;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NotesDatabase extends RoomDatabase {

    private static final String LOG_TAG = "[LOG_TAG:NotesDB]";
    private static final Object lock = new Object();
    private static final String DB_NAME = "notes_DB";

    public abstract NoteDao getNoteDao();

    private static NotesDatabase instance;

    private static Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            Log.d(LOG_TAG, "onCreateDB");
            final int TIME_STEP = 30 * 1000 * 60;
            final int ITERATIONS = 10000;
            final int TEXT_WORDS_COUNT = 30;
            for (int i = 1; i <= ITERATIONS; i++) {
                StringBuilder textNote = new StringBuilder();
                for (int j = 0; j < TEXT_WORDS_COUNT; j++) {
                    textNote.append("Note").append(i).append(j);
                }
                ContentValues cv = new ContentValues();
                cv.put("id", (long) i);
                cv.put("title", "Note" + i);
                cv.put("date", System.currentTimeMillis() - i * (long) TIME_STEP);
                cv.put("text", textNote.toString());
                db.insert("note", 0, cv);
            }
        }
    };

    @NonNull
    public static NotesDatabase getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, NotesDatabase.class, DB_NAME)
                            .addCallback(callback)
                            .build();
                }
            }
        }
        return instance;
    }
}
