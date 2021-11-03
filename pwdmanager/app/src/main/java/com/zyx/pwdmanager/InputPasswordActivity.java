package com.zyx.pwdmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class InputPasswordActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputpassword);
        SharedPreferences sharedPreferences = getSharedPreferences("START", Context.MODE_PRIVATE);
        final String password = sharedPreferences.getString("password", null);

        EditText editText = findViewById(R.id.input);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() != 0) {
                    if (password.equals(Md5Util.md5(s.toString().trim()))) {
                        Intent intent = new Intent();
                        intent.setClass(InputPasswordActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }
}
