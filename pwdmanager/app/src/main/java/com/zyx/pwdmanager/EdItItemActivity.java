package com.zyx.pwdmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.kongzue.dialogx.dialogs.BottomMenu;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.OnMenuItemClickListener;
import com.zyx.pwdmanager.greendao.DbManager;
import com.zyx.pwdmanager.greendao.Group;
import com.zyx.pwdmanager.greendao.Item;

import java.util.List;

public class EdItItemActivity extends AppCompatActivity implements View.OnClickListener {
    private Button group;
    private Button delete;
    private Button save;

    private EditText title;
    private EditText account;
    private EditText password;
    private EditText remark;

    private long groupId;

    private String groupName;

    private long itemId;

    private Item item;

    private List<Group> groupList;
    private long selectedGroupId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edititem);
        BottomMenu.init(this);
        initView();
        initData();
    }

    private void initData(){
        Intent intent = getIntent();
        itemId = intent.getLongExtra("itemId",0);
        groupId = intent.getLongExtra("groupId",0);
        selectedGroupId = groupId;
        groupName = intent.getStringExtra("groupName");
        DbManager dbManager = new DbManager(this);
        item =  dbManager.findItemById(itemId);
        groupList = dbManager.findAllGroup();
        group.setText(item.getGroup().getName());
        title.setText(item.getTitle());
        account.setText(item.getAccount());
        password.setText(item.getPassword());
        remark.setText(item.getRemark());
    }

    private void initView() {
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("编辑账号");
        }
        group = findViewById(R.id.group);
        delete = findViewById(R.id.delete);
        save = findViewById(R.id.save);
        title = findViewById(R.id.title);
        account = findViewById(R.id.account);
        password = findViewById(R.id.password);
        remark = findViewById(R.id.remark);
        group.setOnClickListener(this);
        delete.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.setClass(EdItItemActivity.this,GroupDetailActivity.class);
        intent.putExtra("groupName",groupName);
        intent.putExtra("groupId",groupId);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.group) {
            String[] bottomMenus = new String[groupList.size()];
            for (int i = 0; i < groupList.size(); i++) {
                bottomMenus[i] = groupList.get(i).getName();
            }

            BottomMenu.show(bottomMenus).setOnMenuItemClickListener(new OnMenuItemClickListener<BottomMenu>() {
                @Override
                public boolean onClick(BottomMenu dialog, CharSequence text, int index) {
                    Group groupItem = groupList.get(index);
                    selectedGroupId = groupItem.getId();
                    group.setText(groupItem.getName());
                    //return false弹窗消失
                    return false;
                }
            });
        }else if (v.getId() == R.id.delete) {
            DbManager dbManager = new DbManager(this);
            dbManager.delete(item);
            Toast.makeText(this,"删除成功",Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.setClass(EdItItemActivity.this,GroupDetailActivity.class);
            intent.putExtra("groupName",groupName);
            intent.putExtra("groupId",groupId);
            startActivity(intent);
            finish();
        }else if (v.getId() == R.id.save) {
            if (title.getText().toString().trim().length()==0) {
                TipDialog.show("标题不能为空", WaitDialog.TYPE.ERROR);
            }else if (account.getText().toString().trim().length()==0) {
                TipDialog.show("账号不能为空", WaitDialog.TYPE.ERROR);
            }else if (password.getText().toString().trim().length()==0) {
                TipDialog.show("密码不能为空", WaitDialog.TYPE.ERROR);
            }else {
                DbManager dbManager = new DbManager(this);
                item.setGroupId(selectedGroupId);
                item.setTitle(title.getText().toString().trim());
                item.setAccount(account.getText().toString().trim());
                item.setPassword(password.getText().toString().trim());
                item.setRemark(remark.getText().toString().trim());
                dbManager.updateItem(item);
                Toast.makeText(this, "修改成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(EdItItemActivity.this,GroupDetailActivity.class);
                intent.putExtra("groupName",groupName);
                intent.putExtra("groupId",groupId);
                startActivity(intent);
                finish();
            }
        }
    }
}
