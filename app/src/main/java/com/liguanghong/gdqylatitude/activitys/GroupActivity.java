package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;


import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.GroupAdapter;
import com.liguanghong.gdqylatitude.base.BaseActivity;

import java.util.ArrayList;

public class GroupActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView lv_chat;
    private ImageView backtrack1;
    private RelativeLayout rly_group_note,rly_group_create;

    private ArrayList<String> list;

    ViewPager viewpager;
    GroupAdapter groupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        initChat();

        list = new ArrayList<>();
        for (int i = 0;i < 3 ;i++){
            list.add(GroupAdapter.name);
        }

        groupAdapter = new GroupAdapter(getApplicationContext(),list);
        lv_chat.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();



    }

    private void initChat(){
        lv_chat = (ListView)findViewById(R.id.lv_chat);
        backtrack1 = (ImageView)findViewById(R.id.backtrack);
        rly_group_note = (RelativeLayout)findViewById(R.id.rly_group_note);
        rly_group_create = (RelativeLayout)findViewById(R.id.rly_group_create);

        backtrack1.setOnClickListener(this);
        rly_group_note.setOnClickListener(this);
        rly_group_create.setOnClickListener(this);
        lv_chat.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.rly_group_create:         //跳转到新建群聊页面
                Intent rly_group_create = new Intent(getApplicationContext(),GroupCreateActivity.class);
                startActivityForResult(rly_group_create,1);
                break;

            case R.id.rly_group_note:         //跳转到群通知页面
                Intent re_chat_nota = new Intent(getApplicationContext(),GroupNoticeActivity.class);
                startActivityForResult(re_chat_nota,1);
                break;

            case R.id.backtrack:           //返回通讯录
                setResult(10);
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent group_chat = new Intent(getApplicationContext(),ChatActivity.class);
        startActivityForResult(group_chat,1);
    }
}

