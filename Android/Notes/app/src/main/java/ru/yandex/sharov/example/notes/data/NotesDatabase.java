package ru.yandex.sharov.example.notes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.UUID;

@Database(entities = {Note.class}, version = 2)
public abstract class NotesDatabase extends RoomDatabase {

    private static final String LOG_TAG = "[LOG_TAG:NotesDB]";
    private static final Object LOCK = new Object();
    private static final String DB_NAME = "notes_DB";
    private static final String TABLE_NAME = "notes";

    @NonNull
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            Log.d(LOG_TAG, "Migration v1 to v2");
            final String[] columnsV1 = new String[]{"id", "date", "title", "content"};
            final String[] columnsV2 = new String[]{"id", "guid", "date", "title", "content", "deleted"};
            final String backupTable = "backup";
            final String notesV1BackupTableCreate = "CREATE TABLE  " + backupTable + " (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "date INTEGER NOT NULL," +
                    "title TEXT NOT NULL," +
                    "content TEXT" +
                    "); ";
            final String notesV2TableCreate = "CREATE TABLE " + TABLE_NAME + " (" +
                    "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "guid TEXT UNIQUE NOT NULL," +
                    "date INTEGER NOT NULL," +
                    "title TEXT NOT NULL," +
                    "content TEXT," +
                    "deleted INTEGER NOT NULL" +
                    "); ";
            final String selectQueryTemplate = "SELECT * FROM %s";
            database.beginTransaction();
            try {
                database.execSQL(notesV1BackupTableCreate);
                Cursor v1Data = database.query(String.format(selectQueryTemplate, TABLE_NAME));
                if (v1Data.moveToFirst()) {
                    do {
                        ContentValues cv = new ContentValues();
                        cv.put(columnsV1[0], v1Data.getLong(v1Data.getColumnIndex(columnsV1[0])));
                        cv.put(columnsV1[1], v1Data.getLong(v1Data.getColumnIndex(columnsV1[1])));
                        cv.put(columnsV1[2], v1Data.getString(v1Data.getColumnIndex(columnsV1[2])));
                        cv.put(columnsV1[3], v1Data.getString(v1Data.getColumnIndex(columnsV1[3])));
                        database.insert(backupTable, SQLiteDatabase.CONFLICT_NONE, cv);
                    } while (v1Data.moveToNext());
                }
                database.execSQL("DROP TABLE " + TABLE_NAME);
                database.execSQL(notesV2TableCreate);
                Cursor oldData = database.query(String.format(selectQueryTemplate, backupTable));
                if (oldData.moveToFirst()) {
                    do {
                        ContentValues cv = new ContentValues();
                        cv.put(columnsV2[0], oldData.getLong(oldData.getColumnIndex(columnsV1[0])));
                        cv.put(columnsV2[1], UUID.randomUUID().toString());
                        cv.put(columnsV2[2], oldData.getLong(oldData.getColumnIndex(columnsV1[1])));
                        cv.put(columnsV2[3], oldData.getString(oldData.getColumnIndex(columnsV1[2])));
                        cv.put(columnsV2[4], oldData.getString(oldData.getColumnIndex(columnsV1[3])));
                        cv.put(columnsV2[5], 0);
                        database.insert(TABLE_NAME, SQLiteDatabase.CONFLICT_NONE, cv);
                    } while (oldData.moveToNext());
                }
                database.execSQL("DROP TABLE " + backupTable);
                database.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            } finally {
                database.endTransaction();
            }
        }
    };

    @NonNull
    private static final Callback callback_v2 = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            Log.d(LOG_TAG, "onCreateDB");
            final String[] columns = new String[]{"id", "guid", "date", "title", "content", "deleted"};
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
                        .append("\"").append(UUID.randomUUID().toString()).append("\",")
                        .append("\"").append((System.currentTimeMillis() - i * (long) timeStep)).append("\",")
                        .append("\"Note").append(i).append("\",")
                        .append("\"").append(textNote.toString()).append("\",")
                        .append("\"").append(0).append("\"")
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
                    .append(columns[3]).append(",")
                    .append(columns[4]).append(",")
                    .append(columns[5]).append(")")
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
                            .addCallback(callback_v2)
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return instance;
    }

    @NonNull
    public abstract NoteDao getNoteDao();
}
