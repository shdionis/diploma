package com.sharov.examples.mynotepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DBHelper extends SQLiteOpenHelper {
    public static final String DB_TABLE_COMPANIES = "companies";
    public static final String DB_TABLE_COMPANY_DEPS = "company_departments";
    public static final String DB_TABLE_DEPARTMENTS = "departments";
    public static final String DB_COLUMN_ID = "_id";
    public static final String DB_COLUMN_NAME = "name";
    public static final String DB_COLUMN_DESCRIPTION = "description";
    public static final String DB_COLUMN_DIRECTOR = "director";
    public static final String DB_COLUMN_PHONE = "phone";
    public static final String DB_COLUMN_ID_COMPANY = "id_company";
    public static final String DB_COLUMN_ID_DEPARTMENT = "id_department";

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "companiesDB";

    private SQLiteDatabase database = null;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.beginTransaction();
        try {
            sqLiteDatabase.execSQL("create table " + DB_TABLE_COMPANIES + " (" +
                    DB_COLUMN_ID + " integer primary key autoincrement," +
                    DB_COLUMN_NAME + " text" + ");");
            sqLiteDatabase.execSQL("create table " + DB_TABLE_DEPARTMENTS + " (" +
                    DB_COLUMN_ID + " integer primary key autoincrement," +
                    DB_COLUMN_NAME + " text," +
                    DB_COLUMN_DESCRIPTION + " text," +
                    DB_COLUMN_DIRECTOR + " text," +
                    DB_COLUMN_PHONE + " text" + ");");
            sqLiteDatabase.execSQL("create table " + DB_TABLE_COMPANY_DEPS + " (" +
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

    @Override
    public synchronized void close() {
        super.close();
        database.close();
    }

    public Cursor selectAllCompanies() {
        return database.query(DB_TABLE_COMPANIES, null, null, null, null, null, DB_COLUMN_NAME);
    }

    public void deleteDepartmentById(String id) {
        database.beginTransaction();
        try {
            database.delete("company_departments", DB_COLUMN_ID_DEPARTMENT + " = ?",
                    new String[]{id});
            database.delete("departments", DB_COLUMN_ID + " = ?",
                    new String[]{id});
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public void deleteCompanyById(String companyId) {
        database.beginTransaction();
        try {
            Cursor departmentsIds = database.query("company_departments",
                    new String[]{DB_COLUMN_ID_DEPARTMENT},
                    DB_COLUMN_ID_COMPANY + " = ?",
                    new String[]{companyId},
                    null, null, null);

            if (departmentsIds.moveToFirst()) {
                do {
                    String[] idDep = new String[]{departmentsIds.getString(departmentsIds.getColumnIndex(DBHelper.DB_COLUMN_ID_DEPARTMENT))};
                    database.delete("departments", DB_COLUMN_ID + " = ?", idDep); //TODO: Оптимизировать кол-во запросов
                } while (departmentsIds.moveToNext());
            }
            database.delete("companies", DB_COLUMN_ID + "=?", new String[]{companyId});
            database.delete("company_departments", DB_COLUMN_ID_COMPANY + "= ?", new String[]{companyId});
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return database.update(table,values,whereClause,whereArgs);
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        return database.insert(table, nullColumnHack, values);
    }

    public Cursor selectDepartmentById(String idDep) {
        return database.query(DB_TABLE_DEPARTMENTS, null, "_id = ?", new String[]{idDep}, null, null, null);
    }

    public Cursor selectCompanyById(String id) {
        return database.query(DB_TABLE_COMPANIES, null, "_id = ?", new String[]{id}, null, null, null);
    }

    public Cursor selectCompanyDepsByCompanyId(String id) {
        return database.rawQuery("select comDeps._id, comDeps.id_department, deps.name from company_departments as comDeps " +
                "inner join departments as deps on comDeps.id_department = deps._id " +
                "where comDeps.id_company = ?", new String[]{id});
    }
}
