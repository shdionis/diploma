package com.sharov.examples.mynotepad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DBHelper extends SQLiteOpenHelper {
    public static final String DB_COLUMN_ID = "_id";
    public static final String DB_COLUMN_NAME = "name";
    public static final String DB_COLUMN_DESCRIPTION = "description";
    public static final String DB_COLUMN_DIRECTOR = "director";
    public static final String DB_COLUMN_PHONE = "phone";
    public static final String DB_COLUMN_ID_COMPANY = "id_company";
    public static final String DB_COLUMN_ID_DEPARTMENT = "id_department";

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "companiesDB";

    public DBHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.beginTransaction();
        try {
            sqLiteDatabase.execSQL("create table companies (" +
                    DB_COLUMN_ID + " integer primary key autoincrement," +
                    DB_COLUMN_NAME + " text" + ");");
            sqLiteDatabase.execSQL("create table departments (" +
                    DB_COLUMN_ID + " integer primary key autoincrement," +
                    DB_COLUMN_NAME + " text," +
                    DB_COLUMN_DESCRIPTION + " text," +
                    DB_COLUMN_DIRECTOR + " text," +
                    DB_COLUMN_PHONE + " text" + ");");
            sqLiteDatabase.execSQL("create table company_departments (" +
                    DB_COLUMN_ID + " integer primary key autoincrement," +
                    DB_COLUMN_ID_COMPANY + " integer," +
                    DB_COLUMN_ID_DEPARTMENT + " integer" + ");");
            sqLiteDatabase.setTransactionSuccessful();
        } catch (Exception ex) {
            Log.d("MyDB", "Database create failure " + ex.getMessage());
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
