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

import java.util.UUID;

@Database(entities = {Note.class}, version = 1)
public abstract class NotesDatabase extends RoomDatabase {

    private static final String LOG_TAG = "[LOG_TAG:NotesDB]";
    private static final Object LOCK = new Object();
    private static final String DB_NAME = "notes_DB";
    private static final String TABLE_NAME = "notes";

    @NonNull
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {

        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //final String noteTmpName = "noteV2_tmp";
            final String noteV2TableCreateTemplate = "CREATE TABLE %s (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "guid TEXT UNIQUE NOT NULL," +
                    "version INTEGER," +
                    "user TEXT," +
                    "date INTEGER," +
                    "title TEXT," +
                    "content TEXT," +
                    "deleted INTEGER" +
                    "); ";
            //final String noteV2TableCreate = String.format(noteV2TableCreateTemplate, noteTmpName);
            final String noteV2TableCreate = String.format(noteV2TableCreateTemplate, TABLE_NAME);
            database.beginTransaction();
            database.execSQL("ALTER TABLE " + TABLE_NAME +
                    " RENAME TO note_old;");
            database.execSQL(noteV2TableCreate);
            database.execSQL(noteV2TableCreate);
            final String[] columnsV1 = new String[]{"id", "date", "title", "text"};
            final String[] columnsV2 = new String[]{"id", "guid", "date", "title", "content", "deleted"};
            Cursor oldData = database.query("note_old");
            if (oldData.moveToFirst()) {
                StringBuilder valuesToInsert = new StringBuilder();
                do {
                    valuesToInsert.append("(")
                            .append("\"").append(oldData.getLong(oldData.getColumnIndex(columnsV1[0]))).append("\",")
                            .append("\"").append(UUID.randomUUID().toString()).append("\",")
                            .append("\"").append(oldData.getLong(oldData.getColumnIndex(columnsV1[1]))).append("\",")
                            .append("\"").append(oldData.getLong(oldData.getColumnIndex(columnsV1[2]))).append("\",")
                            .append("\"").append(oldData.getLong(oldData.getColumnIndex(columnsV1[3]))).append("\",")
                            .append("\"" + 0 + "\"")
                            .append(")");
                    if (oldData.isLast()) {
                        valuesToInsert.append(",");
                    }
                } while (oldData.moveToNext());

                StringBuilder insertQuery = new StringBuilder();
                insertQuery.append("INSERT INTO ")
                        .append(TABLE_NAME)
                        .append(" (")
                        .append(columnsV2[0]).append(",")
                        .append(columnsV2[1]).append(",")
                        .append(columnsV2[2]).append(",")
                        .append(columnsV2[3]).append(",")
                        .append(columnsV2[4]).append(",")
                        .append(columnsV2[5]).append(",")
                        .append(" VALUES ")
                        .append(valuesToInsert.toString()).append(";");
                try {
                    database.execSQL(insertQuery.toString());
                } catch (Exception e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                }
            }
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
                        .append("\"").append((long) i).append("\",")
                        .append("\"Note").append(i).append("\",")
                        .append("\"").append((System.currentTimeMillis() - i * (long) timeStep)).append("\",")
                        .append("\"").append(textNote.toString()).append("\"")
                        .append(")");
                if (i <= iterations - 1) {
                    valuesToInsert.append(",");
                }
            }
            StringBuilder insertQuery = new StringBuilder();
            insertQuery.append("INSERT INTO ")
                    .append(TABLE_NAME)
                    .append(" (")
                    .append(columns[0]).append(",")
                    .append(columns[1]).append(",")
                    .append(columns[2]).append(",")
                    .append(columns[3]).append(")")
                    .append(" VALUES ")
                    .append(valuesToInsert.toString()).append(";");
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
