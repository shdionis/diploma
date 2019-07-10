package ru.yandex.sharov.example.notes.data;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NotesDatabase extends RoomDatabase {

    private static final String LOG_TAG = "[LOG_TAG:NotesDB]";
    private static final Object LOCK = new Object();
    private static final String DB_NAME = "notes_DB";

    @NonNull
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            final String noteV2TableCreate = "CREATE TABLE note (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "guid TEXT UNIQUE NOT NULL," +
                    "version INTEGER," +
                    "user TEXT," +
                    "date INTEGER," +
                    "title TEXT," +
                    "content TEXT," +
                    "deleted INTEGER" +
                    "); ";
            database.beginTransaction();
            Cursor oldData = database.query("note");
            database.execSQL("ALTER TABLE note " +
                    "RENAME TO note_old;");
            database.execSQL(noteV2TableCreate);
            database.execSQL("DROP TABLE note_old");

            database.setTransactionSuccessful();
            database.endTransaction();
        }
    };

    @NonNull
    private static final Callback callback_v1 = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            Log.d(LOG_TAG, "onCreateDB");
            final String[] columns = new String[]{"id", "title", "date", "text"};
            final int timeStep = 30 * 1000 * 60;
            final int iterations = 10000;
            final int textWordsCount = 30;
            StringBuilder valuesToInsert = new StringBuilder();
            for (int i = 1; i <= iterations; i++) {
                StringBuilder textNote = new StringBuilder();
                for (int j = 0; j < textWordsCount; j++) {
                    textNote.append("Note").append(i).append(j);
                }
                valuesToInsert.append("(")
                        .append("\"" + (long) i + "\",")
                        .append("\"Note" + i + "\",")
                        .append("\"" + (System.currentTimeMillis() - i * (long) timeStep) + "\",")
                        .append("\"" + textNote.toString() + "\"")
                        .append(")");
                if (i <= iterations - 1) {
                    valuesToInsert.append(",");
                }
            }
            StringBuilder insertQuery = new StringBuilder();
            insertQuery.append("INSERT INTO ")
                    .append("note")
                    .append(" (")
                    .append(columns[0] + ",")
                    .append(columns[1] + ",")
                    .append(columns[2] + ",")
                    .append(columns[3] + ")")
                    .append(" VALUES ")
                    .append(valuesToInsert.toString() + ";");
            try {
                db.execSQL(insertQuery.toString());
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
            Log.d(LOG_TAG, "endGenerate");
        }
    };

    @Nullable
    private static NotesDatabase instance;

    @NonNull
    public static NotesDatabase getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context, NotesDatabase.class, DB_NAME)
                            .addCallback(callback_v1)
                            .build();
                }
            }
        }
        return instance;
    }

    @NonNull
    public abstract NoteDao getNoteDao();
}
