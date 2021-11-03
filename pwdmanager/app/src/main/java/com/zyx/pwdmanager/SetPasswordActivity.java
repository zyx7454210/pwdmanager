package com.zyx.pwdmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;

public class SetPasswordActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpassword);

        EditText input = findViewById(R.id.input);
        EditText input2 = findViewById(R.id.input2);
        Button button = findViewById(R.id.save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input.getText().toString().trim().length() == 0) {
                    TipDialog.show("密码不能为空", WaitDialog.TYPE.ERROR);
                    return;
                }
                if (!input.getText().toString().trim().equals(input2.getText().toString().trim())) {
                    TipDialog.show("两次密码不相同", WaitDialog.TYPE.ERROR);
                    return;
                }

                SharedPreferences sharedPreferences = getSharedPreferences("START", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("password", Md5Util.md5(input.getText().toString().trim()));
                editor.apply();

                Intent intent = new Intent();
                intent.setClass(SetPasswordActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
