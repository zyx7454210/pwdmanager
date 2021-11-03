package com.zyx.pwdmanager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zyx.pwdmanager.greendao.DbManager;
import com.zyx.pwdmanager.greendao.Group;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GroupAdapter.OnItemClickListener{

    private RecyclerView mGroupRv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        DbManager dbManager = new DbManager(this);
        List<Group> allGroup = dbManager.findAllGroup();
        GroupAdapter groupAdapter = new GroupAdapter(this);
        groupAdapter.setDataList(allGroup);
        groupAdapter.setItemClickListener(this);
        mGroupRv.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mGroupRv = findViewById(R.id.rv_group);
        mGroupRv.setLayoutManager(new LinearLayoutManager(this));
        mGroupRv.addItemDecoration(new SpacesItemDecoration(
                getResources().getDimensionPixelSize(R.dimen.C1Padding)));
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("addMode",1);
                intent.setClass(MainActivity.this,AddActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onItemClick(String groupName,long groupId) {
        Intent intent = new Intent(MainActivity.this, GroupDetailActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("groupName",groupName);
        bundle.putLong("groupId",groupId);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}