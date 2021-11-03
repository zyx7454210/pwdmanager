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

import com.kongzue.dialogx.DialogX;
import com.kongzue.dialogx.dialogs.BottomMenu;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.OnMenuItemClickListener;
import com.zyx.pwdmanager.greendao.DbManager;
import com.zyx.pwdmanager.greendao.Group;
import com.zyx.pwdmanager.greendao.Item;

import net.sqlcipher.database.SQLiteConstraintException;

import java.util.List;

public class AddItemFragment extends Fragment implements View.OnClickListener {
    private int addMode;

    private long groupId;

    private String groupName;

    private long selectedGroupId;

    private Button group;

    private EditText title;
    private EditText account;
    private EditText password;
    private EditText remark;

    private Button save;

    private List<Group> groups;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frgament_additem, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        DialogX.init(getContext());
        group = view.findViewById(R.id.group);
        title = view.findViewById(R.id.title);
        account = view.findViewById(R.id.account);
        password = view.findViewById(R.id.password);
        remark = view.findViewById(R.id.remark);
        save = view.findViewById(R.id.save);
        save.setOnClickListener(this);
        Bundle bundle = getArguments();
        addMode = bundle.getInt("addMode");
        if (addMode == 1) {
            queryAllGroup();
            group.setOnClickListener(this);
        } else {
            groupId = bundle.getLong("groupId");
            groupName = bundle.getString("groupName");
            group.setClickable(false);
            if (null != group) {
                selectedGroupId = groupId;
                this.group.setText(groupName);
            }
        }
    }

    private void queryAllGroup() {
        DbManager dbManager = new DbManager(getContext());
        groups = dbManager.findAllGroup();
        if (null != group && groups.size() > 0) {
            Group group = groups.get(0);
            this.group.setText(group.getName());
            selectedGroupId = group.getId();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.group) {
            if (null == groups || groups.size() == 0) {
                Toast.makeText(getContext(), "没有分组,请去新建个分组再来吧", Toast.LENGTH_LONG).show();
                return;
            }
            String[] bottomMenus = new String[groups.size()];
            for (int i = 0; i < groups.size(); i++) {
                bottomMenus[i] = groups.get(i).getName();
            }

            BottomMenu.show(bottomMenus).setOnMenuItemClickListener(new OnMenuItemClickListener<BottomMenu>() {
                @Override
                public boolean onClick(BottomMenu dialog, CharSequence text, int index) {
                    Group groupItem = groups.get(index);
                    selectedGroupId = groupItem.getId();
                    group.setText(groupItem.getName());
                    //return false弹窗消失
                    return false;
                }
            });
        } else if (v.getId() == R.id.save) {
            if (selectedGroupId == 0) {
                TipDialog.show("未选中分组", WaitDialog.TYPE.ERROR);
            } else if (this.title.getText().toString().trim().length() == 0) {
                TipDialog.show("标题不能为空", WaitDialog.TYPE.ERROR);
            } else if (this.account.getText().toString().trim().length() == 0) {
                TipDialog.show("账号不能为空", WaitDialog.TYPE.ERROR);
            } else if (this.password.getText().toString().trim().length() == 0) {
                TipDialog.show("密码不能为空", WaitDialog.TYPE.ERROR);
            } else {
                DbManager dbManager = new DbManager(getContext());
                String title = this.title.getText().toString().trim();
                String account = this.account.getText().toString().trim();
                String password = this.password.getText().toString().trim();
                String remark = this.remark.getText().toString().trim();
                try {
                    Item item = new Item();
                    item.setGroupId(selectedGroupId);
                    item.setTitle(title);
                    item.setAccount(account);
                    item.setPassword(password);
                    item.setRemark(remark);
                    dbManager.insertItem(item);
                    Toast.makeText(getContext(), "创建成功", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.setClass(getContext(), addMode == 1 ? MainActivity.class : GroupDetailActivity.class);
                    if (addMode == 2){
                        intent.putExtra("groupName",groupName);
                        intent.putExtra("groupId",groupId);
                    }
                    startActivity(intent);
                    getActivity().finish();
                } catch (SQLiteConstraintException e) {
                    TipDialog.show("分组里已有同名标题\n请更换标题", WaitDialog.TYPE.ERROR);
                }
            }
        }
    }
}
