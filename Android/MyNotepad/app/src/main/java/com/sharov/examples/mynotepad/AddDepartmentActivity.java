package com.sharov.examples.mynotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddDepartmentActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etDepName;
    private EditText etDepDirector;
    private EditText etDepPhone;
    private EditText etDepDescription;
    private Button btnOkAddDep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_department);
        btnOkAddDep = findViewById(R.id.btnOkAddDep);
        btnOkAddDep.setOnClickListener(this);
        etDepName = findViewById(R.id.etDepName);
        etDepName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnOkAddDep.setEnabled(charSequence.length()>0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etDepDirector = findViewById(R.id.etDepDirector);
        etDepPhone = findViewById(R.id.etDepPhone);
        etDepDescription = findViewById(R.id.etDepDescr);
        Intent intent = getIntent();
        String id = intent.getStringExtra(DBHelper.DB_COLUMN_ID);
        if(id!=null && !id.isEmpty()) {
            DBHelper dbHelper = new DBHelper(this, DBHelper.DB_NAME, DBHelper.DB_VERSION);
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query("departments", null, DBHelper.DB_COLUMN_ID+" = ?", new String[]{id}, null, null, null);
            if(cursor.moveToFirst()){
                etDepName.setText(cursor.getString(cursor.getColumnIndex(DBHelper.DB_COLUMN_NAME)));
                etDepDirector.setText(cursor.getString(cursor.getColumnIndex(DBHelper.DB_COLUMN_DIRECTOR)));
                etDepPhone.setText(cursor.getString(cursor.getColumnIndex(DBHelper.DB_COLUMN_PHONE)));
                etDepDescription.setText(cursor.getString(cursor.getColumnIndex(DBHelper.DB_COLUMN_DESCRIPTION)));
            }
            db.close();
        }

    }

    @Override
    public void onClick(View view) {
        Intent intent = getIntent();

        intent.putExtra(DBHelper.DB_COLUMN_NAME, etDepName.getText().toString());
        intent.putExtra(DBHelper.DB_COLUMN_DIRECTOR, etDepDirector.getText().toString());
        intent.putExtra(DBHelper.DB_COLUMN_PHONE, etDepPhone.getText().toString());
        intent.putExtra(DBHelper.DB_COLUMN_DESCRIPTION, etDepDescription.getText().toString());
        setResult(RESULT_OK,intent);
        finish();
    }
}
