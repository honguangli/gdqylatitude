package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.FriendsNoticeAdapter;
import com.liguanghong.gdqylatitude.base.BaseActivity;

import java.util.ArrayList;

public class FriendsNoticeActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView lv_friend_apply;
    private ImageView backtrack;

    private ArrayList<String> list;
    FriendsNoticeAdapter friendsNoticeAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_notice);

        initView();

        list = new ArrayList<>();
        for (int i = 0;i < 3 ;i++){
            list.add("小明");
        }


        friendsNoticeAdapter = new FriendsNoticeAdapter(getApplicationContext(),list);
        lv_friend_apply.setAdapter(friendsNoticeAdapter);
        friendsNoticeAdapter.notifyDataSetChanged();

    }
    private void initView() {
        lv_friend_apply = (ListView)findViewById(R.id.lv_friend_notice);
        backtrack = (ImageView)findViewById(R.id.backtrack);

        backtrack.setOnClickListener(this);
        lv_friend_apply.setOnItemClickListener(this);
    }

    //按钮点击监听
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.backtrack:        //返回通讯录
                setResult(10);
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent friend_apply_info = new Intent(getApplicationContext(),FriendsNoticeInfoActivity.class);
        startActivityForResult(friend_apply_info,1);
    }
}
