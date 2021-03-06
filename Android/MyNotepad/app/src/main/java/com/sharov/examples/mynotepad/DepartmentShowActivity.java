package com.sharov.examples.mynotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import static com.sharov.examples.mynotepad.DBHelper.DB_COLUMN_NAME;

public class DepartmentShowActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private String id_dep;
    private TextView tvDepName;
    private TextView tvDepDirector;
    private TextView tvDepPhone;
    private TextView tvDepDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_show);
        dbHelper = new DBHelper(this);
        Intent intent = getIntent();
        id_dep = intent.getStringExtra(DBHelper.DB_COLUMN_ID);
        Cursor cursor = dbHelper.selectDepartmentById(id_dep);
        tvDepName = findViewById(R.id.tvDepNameShow1);
        tvDepDirector = findViewById(R.id.tvDepDirectorShow1);
        tvDepPhone = findViewById(R.id.tvDepPhoneShow1);
        tvDepDescription = findViewById(R.id.tvDepDescrShow1);
        if (cursor.moveToFirst()) {
            tvDepName.setText(cursor.getString(cursor.getColumnIndex(DB_COLUMN_NAME)));
            tvDepDirector.setText(cursor.getString(cursor.getColumnIndex(DBHelper.DB_COLUMN_DIRECTOR)));
            tvDepPhone.setText(cursor.getString(cursor.getColumnIndex(DBHelper.DB_COLUMN_PHONE)));
            tvDepDescription.setText(cursor.getString(cursor.getColumnIndex(DBHelper.DB_COLUMN_DESCRIPTION)));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();
    }
}
