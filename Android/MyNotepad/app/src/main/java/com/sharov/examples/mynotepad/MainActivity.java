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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String[] from = new String[]{DBHelper.DB_COLUMN_ID, DBHelper.DB_COLUMN_NAME};
    private int[] to = new int[]{android.R.id.text2, android.R.id.text1};
    private final int CONTEXT_MENU_DELETE = 1;
    private final int CONTEXT_MENU_EDIT = 2;
    private final int REQUEST_CODE_ADD_COMPANY = 0;
    private final int REQUEST_CODE_EDIT_COMPANY = 1;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private SimpleCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this, DBHelper.DB_NAME, DBHelper.DB_VERSION);
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("companies", null, null, null, null, null, "name");
        cursorAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, from, to, FLAG_REGISTER_CONTENT_OBSERVER);
        ListView lvCompanies = findViewById(R.id.lvCompanies);
        lvCompanies.setAdapter(cursorAdapter);
        lvCompanies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                SQLiteCursor cursor = (SQLiteCursor) adapterView.getItemAtPosition(position);
                String id_company = cursor.getString(cursor.getColumnIndex(DBHelper.DB_COLUMN_ID));
                Intent intent = new Intent(view.getContext(), DepartmentsActivity.class);
                intent.putExtra(DBHelper.DB_COLUMN_ID, id_company);
                startActivity(intent);
                Toast.makeText(view.getContext(), id_company, Toast.LENGTH_SHORT).show();
            }
        });
        registerForContextMenu(lvCompanies);
        findViewById(R.id.add_menuItem).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, AddCompanyActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_COMPANY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.DB_COLUMN_NAME, data.getStringExtra(DBHelper.DB_COLUMN_NAME));
            switch (requestCode) {
                case REQUEST_CODE_ADD_COMPANY:
                    db.insert("companies", null, cv);
                    break;
                case REQUEST_CODE_EDIT_COMPANY:
                    String id = data.getStringExtra(DB_COLUMN_ID);
                    db.update("companies", cv, DB_COLUMN_ID + " = ?", new String[]{id});
                    break;
            }
            refreshCursor(); //TODO: костыль. сделать через cursorAdapter.notifyDataSetChanged();
        }
    }

    private void refreshCursor() {
        Cursor cursor = db.query("companies", null, null, null, null, null, "name");
        cursorAdapter.changeCursor(cursor);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CONTEXT_MENU_DELETE, 0, "Удалить Запись");
        menu.add(0, CONTEXT_MENU_EDIT, 0, "Редактировать Запись");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Cursor cursor = (Cursor) cursorAdapter.getItem(acmi.position);
        String companyId = cursor.getString(cursor.getColumnIndex(DB_COLUMN_ID));
        switch (item.getItemId()) {
            case CONTEXT_MENU_DELETE:
                db.beginTransaction();
                try {
                    Cursor departmentsIds = db.query("company_departments",
                            new String[]{DB_COLUMN_ID_DEPARTMENT},
                            DB_COLUMN_ID_COMPANY + " = ?",
                            new String[]{companyId},
                            null, null, null);

                    if (departmentsIds.moveToFirst()) {
                        do {
                            String[] idDep = new String[]{departmentsIds.getString(departmentsIds.getColumnIndex(DBHelper.DB_COLUMN_ID_DEPARTMENT))};
                            db.delete("departments", DB_COLUMN_ID + " = ?", idDep); //TODO: Оптимизировать кол-во запросов
                        } while (departmentsIds.moveToNext());
                    }
                    db.delete("companies", DB_COLUMN_ID + "=?", new String[]{companyId});
                    db.delete("company_departments", DB_COLUMN_ID_COMPANY + "= ?", new String[]{companyId});
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                    refreshCursor();
                }
                break;
            case CONTEXT_MENU_EDIT:
                Intent intent = new Intent(this, AddCompanyActivity.class);
                intent.putExtra(DB_COLUMN_ID, companyId);
                String companyName = cursor.getString(cursor.getColumnIndex(DB_COLUMN_NAME));
                intent.putExtra(DB_COLUMN_NAME, companyName);
                startActivityForResult(intent, REQUEST_CODE_EDIT_COMPANY);
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
