package com.zyx.pwdmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("START", Context.MODE_PRIVATE);
        final String password = sharedPreferences.getString("password", null);
        if (null == password) {
            Intent intent = new Intent();
            intent.setClass(this,SetPasswordActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent();
            intent.setClass(this,InputPasswordActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
