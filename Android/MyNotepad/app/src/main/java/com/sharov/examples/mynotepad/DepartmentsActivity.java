package com.sharov.examples.mynotepad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import static android.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER;
import static com.sharov.examples.mynotepad.DBHelper.DB_COLUMN_ID;
import static com.sharov.examples.mynotepad.DBHelper.DB_COLUMN_ID_COMPANY;
import static com.sharov.examples.mynotepad.DBHelper.DB_COLUMN_ID_DEPARTMENT;
import static com.sharov.examples.mynotepad.DBHelper.DB_COLUMN_NAME;

public class DepartmentsActivity extends AppCompatActivity implements View.OnClickListener {
    private String[] from = new String[]{DB_COLUMN_NAME};
    private int[] to = new int[]{android.R.id.text1};
    private final int CONTEXT_MENU_DELETE = 1;
    private final int CONTEXT_MENU_EDIT = 2;
    private final int REQUEST_CODE_ADD_DEARTMENT = 0;
    private final int REQUEST_CODE_EDIT_DEPARTMENT = 1;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private SimpleCursorAdapter cursorAdapter;
    private String id_company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departments);
        dbHelper = new DBHelper(this, DBHelper.DB_NAME, DBHelper.DB_VERSION);
        db = dbHelper.getWritableDatabase();
        Intent intent = getIntent();
        id_company = intent.getStringExtra(DBHelper.DB_COLUMN_ID);
        Cursor cursor = db.rawQuery("select comDeps._id, comDeps.id_department, deps.name from company_departments as comDeps " +
                "inner join departments as deps on comDeps.id_department = deps._id " +
                "where comDeps.id_company = ?", new String[]{id_company}
        );

        cursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, from, to, FLAG_REGISTER_CONTENT_OBSERVER);
        ListView lvDepartments = findViewById(R.id.lvDepartments);
        lvDepartments.setAdapter(cursorAdapter);


        lvDepartments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                SQLiteCursor cursor = (SQLiteCursor) adapterView.getItemAtPosition(position);
                String id_dep = cursor.getString(cursor.getColumnIndex(DB_COLUMN_ID_DEPARTMENT));
                Intent intent = new Intent(view.getContext(), DepartmentShowActivity.class);
                intent.putExtra(DB_COLUMN_ID, id_dep);
                startActivity(intent);
                Toast.makeText(view.getContext(), id_dep, Toast.LENGTH_SHORT).show();
            }
        });
        registerForContextMenu(lvDepartments);
        findViewById(R.id.add_menuItem).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, AddDepartmentActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_DEARTMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.DB_COLUMN_NAME, data.getStringExtra(DBHelper.DB_COLUMN_NAME));
            cv.put(DBHelper.DB_COLUMN_DESCRIPTION, data.getStringExtra(DBHelper.DB_COLUMN_DESCRIPTION));
            cv.put(DBHelper.DB_COLUMN_DIRECTOR, data.getStringExtra(DBHelper.DB_COLUMN_DIRECTOR));
            cv.put(DBHelper.DB_COLUMN_PHONE, data.getStringExtra(DBHelper.DB_COLUMN_PHONE));
            switch (requestCode) {
                case REQUEST_CODE_ADD_DEARTMENT:
                    long depId = db.insert("departments", null, cv);
                    cv.clear();
                    cv.put(DB_COLUMN_ID_COMPANY, id_company);
                    cv.put(DB_COLUMN_ID_DEPARTMENT, depId);
                    db.insert("company_departments", null, cv);
                    refreshCursor();
                    break;
                case REQUEST_CODE_EDIT_DEPARTMENT:
                    String[] updatedDepId = new String[]{data.getStringExtra(DB_COLUMN_ID)};
                    db.update("departments", cv, DB_COLUMN_ID+" = ?", updatedDepId);
                    break;
            }
        }
    }

    private void refreshCursor() {
        Cursor cursor = db.rawQuery("select comDeps._id, comDeps.id_department, deps.name from company_departments as comDeps " +
                "inner join departments as deps on comDeps.id_department = deps._id " +
                "where comDeps.id_company = ?", new String[]{id_company}
        );
        cursorAdapter.changeCursor(cursor);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CONTEXT_MENU_DELETE, 0, "Удалить запись");
        menu.add(0, CONTEXT_MENU_EDIT, 0, "Редактировать запись");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Cursor cursor = (Cursor) cursorAdapter.getItem(adapterContextMenuInfo.position);
        String id = cursor.getString(cursor.getColumnIndex(DBHelper.DB_COLUMN_ID_DEPARTMENT));
        switch (item.getItemId()) {
            case CONTEXT_MENU_DELETE:
                db.beginTransaction();
                try {
                    db.delete("company_departments", DB_COLUMN_ID_DEPARTMENT + " = ?",
                            new String[]{id});
                    db.delete("departments", DB_COLUMN_ID + " = ?",
                            new String[]{id});
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                    refreshCursor();
                }
                break;
            case CONTEXT_MENU_EDIT:
                Intent intent = new Intent(this, AddDepartmentActivity.class);
                intent.putExtra(DB_COLUMN_ID, id);
                startActivityForResult(intent, REQUEST_CODE_EDIT_DEPARTMENT);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
