package com.sharov.examples.mynotepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddCompanyActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etCompanyName;
    private Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company);
        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);
        etCompanyName = findViewById(R.id.etCompanyName);
        etCompanyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnOk.setEnabled(charSequence.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Intent intent = getIntent();
        etCompanyName.setText(intent.getStringExtra(DBHelper.DB_COLUMN_NAME));
    }

    @Override
    public void onClick(View view) {
        Intent intent = getIntent();
        intent.putExtra(DBHelper.DB_COLUMN_NAME, etCompanyName.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
