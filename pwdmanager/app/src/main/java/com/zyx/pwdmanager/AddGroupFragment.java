package com.zyx.pwdmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.zyx.pwdmanager.greendao.DbManager;
import com.zyx.pwdmanager.greendao.Group;

import net.sqlcipher.database.SQLiteConstraintException;

public class AddGroupFragment extends Fragment implements View.OnClickListener {
    private EditText title;

    private Button save;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frgament_addgroup,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        title = view.findViewById(R.id.title);
        save = view.findViewById(R.id.save);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (this.title.getText().toString().trim().length() == 0) {
            TipDialog.show("标题不能为空", WaitDialog.TYPE.WARNING);
        }else {
            DbManager dbManager = new DbManager(getContext());
            Group group = new Group();
            group.setName(title.getText().toString().trim());
            try {
                dbManager.insertGroup(group);
                Intent intent = new Intent();
                intent.setClass(getContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(getContext(), "创建成功", Toast.LENGTH_LONG).show();
                getActivity().finish();
            }catch (SQLiteConstraintException e){
                TipDialog.show("已有同名分组\n请更换标题", WaitDialog.TYPE.ERROR);
            }
        }
    }
}
