package com.zyx.pwdmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity{

    private TabLayout tabLayout;

    private ViewPager viewPager;

    private int addMode;

    private long groupId;

    private String groupName;

    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        parseIntent();
        initView();
        initViewPager();
    }

    private void parseIntent() {
        Intent intent = getIntent();
        addMode = intent.getIntExtra("addMode",0);
        if (addMode == 1) { // 首页进来的，有新建分组、新建账号
            AddItemFragment addItemFragment = new AddItemFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("addMode",1);
            addItemFragment.setArguments(bundle);
            fragments.add(addItemFragment);
            fragments.add(new AddGroupFragment());
        }else{ // 分组详情进来的，只有新建账号
            AddItemFragment addItemFragment = new AddItemFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("addMode",2);
            groupId = intent.getLongExtra("groupId",0);
            groupName = intent.getStringExtra("groupName");
            bundle.putLong("groupId",groupId);
            bundle.putString("groupName",groupName);
            addItemFragment.setArguments(bundle);
            fragments.add(addItemFragment);
        }
    }

    private void initView() {
        viewPager = findViewById(R.id.viewpager);
        tabLayout =findViewById(R.id.tablayout);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("新增");
        }
    }

    private void initViewPager() {
        List<String> titles =new ArrayList<>();
        if (addMode == 1) {
            titles.add("新增密码");
            titles.add("新增分组");
        }else {
            titles.add("新增密码");
        }

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),fragments,titles);
        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.setClass(this,addMode == 1 ? MainActivity.class : GroupDetailActivity.class);
        if (addMode == 2){
            intent.putExtra("groupName",groupName);
            intent.putExtra("groupId",groupId);
        }
        startActivity(intent);
    }
}
