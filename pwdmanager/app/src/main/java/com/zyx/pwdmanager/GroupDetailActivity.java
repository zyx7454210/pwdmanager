package com.zyx.pwdmanager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.dialogs.TipDialog;
import com.kongzue.dialogx.dialogs.WaitDialog;
import com.kongzue.dialogx.interfaces.BaseDialog;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.zyx.pwdmanager.greendao.DbManager;
import com.zyx.pwdmanager.greendao.Group;
import com.zyx.pwdmanager.greendao.Item;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

public class GroupDetailActivity extends AppCompatActivity implements OnDialogButtonClickListener, ItemAdapter.OnItemClickListener {

    private long groupId;

    private String groupName;

    private List<Item> items;

    private RecyclerView itemRv;

    private ItemAdapter itemAdapter;

    private StatusLayoutManager statusLayoutManager;

    private DbManager dbManager;

    private int lastExpandedPos = -1;

    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        initData();
    }

    private void initView() {
        Intent intent  = getIntent();
        groupName = intent.getStringExtra("groupName");
        groupId= intent.getLongExtra("groupId",0);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(groupName);
        }
        itemRv = findViewById(R.id.rv_group);
        linearLayoutManager = new LinearLayoutManager(this);
        itemRv.setLayoutManager(linearLayoutManager);
        itemRv.addItemDecoration(new SpacesItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.C1Padding)));
        itemAdapter = new ItemAdapter(this);
        itemAdapter.setItemClickListener(this);
        itemRv.setAdapter(itemAdapter);
        statusLayoutManager = new StatusLayoutManager.Builder(itemRv)
                .setDefaultEmptyClickViewVisible(false)
                .setDefaultLoadingText("努力加载中...")
                .setDefaultEmptyText("无数据\n点击右下角加号添加一下试试")
                .build();
        statusLayoutManager.showLoadingLayout();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("addMode",2);
                intent.putExtra("groupId",groupId);
                intent.putExtra("groupName",groupName);
                intent.setClass(GroupDetailActivity.this,AddActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initData(){
        if (null == dbManager) {
            dbManager = new DbManager(this);
        }
        Group group = dbManager.findGroupById(groupId);
        items = group.getItemList();

        if (items.size() == 0) {
            statusLayoutManager.showEmptyLayout();
        }else {
            statusLayoutManager.showSuccessLayout();
        }

        itemAdapter.setDataList(items);
        itemAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete) {
            MessageDialog messageDialog = new MessageDialog("重要提示", "删除分组将删除该分组里的所有存储的账号信息\n您确定此操作吗?", "确定", "取消")
                    .setButtonOrientation(LinearLayout.VERTICAL);
            messageDialog.setOkButtonClickListener(this);
            messageDialog.show();
        }else {
            // 点击了返回
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (groupId == 1||groupId == 2||groupId == 3||groupId == 4||groupId == 5) {
            // 默认的5个分组不支持删除
            return false;
        }
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onClick(BaseDialog baseDialog, View v) {
        MessageDialog dialog = (MessageDialog) baseDialog;
        dialog.dismiss();
        WaitDialog.show("正在删除...");
        DbManager dbManager = new DbManager(this);
        dbManager.deleteItems(items);
        dbManager.deleteGroupById(groupId);
        WaitDialog.dismiss();
        Toast.makeText(this,"删除成功", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    @Override
    public void onItemClick(int position, View view, Item item) {
        ClipboardManager  cm;
        ClipData clipData;
        switch (view.getId()) {
            case R.id.titlelayout:
                if (lastExpandedPos != -1) {
                    // 将之前展开的收起
                    View viewGroup = linearLayoutManager.getChildAt(lastExpandedPos);
                    ExpandableLayout expandableLayout = viewGroup.findViewById(R.id.expandable_layout);
                    expandableLayout.collapse();

                    //将密码设置为不可见
                    TextView password = viewGroup.findViewById(R.id.password);
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ImageView show = viewGroup.findViewById(R.id.show);
                    show.setTag("hide");
                    show.setImageResource(R.drawable.hiddle);
                }

                if (position != lastExpandedPos) {
                    // 将当前展开
                    View viewGroup = linearLayoutManager.getChildAt(position);
                    ExpandableLayout expandableLayout = viewGroup.findViewById(R.id.expandable_layout);
                    expandableLayout.expand(true);

                    lastExpandedPos = position;
                }else {
                    // 将当前收起
                    View viewGroup = linearLayoutManager.getChildAt(position);
                    ExpandableLayout expandableLayout = viewGroup.findViewById(R.id.expandable_layout);
                    expandableLayout.collapse(true);

                    //将密码设置为不可见
                    TextView password = viewGroup.findViewById(R.id.password);
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ImageView show = viewGroup.findViewById(R.id.show);
                    show.setTag("hide");
                    show.setImageResource(R.drawable.hiddle);

                    lastExpandedPos = -1;
                }
                break;
            case R.id.accountcopy:
                //获取剪贴板管理器：
                cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipData =ClipData.newPlainText("Label",item.getAccount());
                cm.setPrimaryClip(clipData);
                Toast.makeText(this,"复制账号成功",Toast.LENGTH_SHORT).show();
                break;

            case R.id.passwordcopy:
                //获取剪贴板管理器：
                cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipData =ClipData.newPlainText("Label",item.getPassword());
                cm.setPrimaryClip(clipData);
                Toast.makeText(this,"复制密码成功",Toast.LENGTH_SHORT).show();
                break;

            case R.id.show:
                if (view.getTag() == null || view.getTag().equals("hide")) {
                    view.setTag("show");
                    View viewGroup = linearLayoutManager.getChildAt(position);
                    TextView password = viewGroup.findViewById(R.id.password);
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ImageView show = viewGroup.findViewById(R.id.show);
                    show.setImageResource(R.drawable.show);
                }else {
                    view.setTag("hide");
                    View viewGroup = linearLayoutManager.getChildAt(position);
                    TextView password = viewGroup.findViewById(R.id.password);
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ImageView show = viewGroup.findViewById(R.id.show);
                    show.setImageResource(R.drawable.hiddle);
                }
                break;

            case R.id.expandable_layout:
                Intent intent = new Intent();
                intent.setClass(GroupDetailActivity.this,EdItItemActivity.class);
                intent.putExtra("groupName",groupName);
                intent.putExtra("groupId",groupId);
                intent.putExtra("itemId",item.getId());
                startActivity(intent);
                finish();
                break;
        }



    }
}
