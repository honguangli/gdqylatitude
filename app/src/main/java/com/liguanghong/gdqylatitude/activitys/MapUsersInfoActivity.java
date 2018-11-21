package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.MapUsersInfoAdapter;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.fragment.MapFragment;
import com.liguanghong.gdqylatitude.manager.FriendsManager;
import com.liguanghong.gdqylatitude.unity.Friend;
import com.liguanghong.gdqylatitude.unity.User;

import java.util.ArrayList;
import java.util.List;

public class MapUsersInfoActivity extends BaseActivity implements View.OnClickListener{
    private ImageView backtrack_info;
    private ListView listView;
    MapUsersInfoAdapter mapUsersInfoAdapter;
    List<User> list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_map_users_info;
    }
    @Override
    protected void initView() {
        backtrack_info = findViewById(R.id.backtrack);
        String ss = getIntent().getStringExtra("user");
        list= JSONArray.parseArray(ss, User.class);
        mapUsersInfoAdapter = new MapUsersInfoAdapter(this,list);
        listView = findViewById(R.id.map_user_lv);
        listView.setAdapter(mapUsersInfoAdapter);
        backtrack_info.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Intent intent = new Intent(,UserInfoActivity.class);
                Intent intent = new Intent(MapUsersInfoActivity.this,UserInfoActivity.class);
                Friend friend = FriendsManager.getFriendByID(list.get(i).getUserid());
                if(friend != null){
                    //好友
                    intent.putExtra("userinfo", friend);
                    intent.putExtra("hide", false);
                } else {
                    //非好友
                    intent.putExtra("userinfo", list.get(i));
                    intent.putExtra("hide", true);
                }

                startActivity(intent);
            }
        });
    }
    @Override
    protected void initData() {

    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.backtrack:
                //销毁当前界面，返回上一界面
                finish();
                break;
        }
    }
}
