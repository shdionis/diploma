package ru.yandex.sharov.example.notes.data.migrations;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.UUID;

public class Migrations {
    private static final String LOG_TAG = "[LOG_TAG:Migrations]";
    private static final String TABLE_NAME = "notes";

    @NonNull
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
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
}
