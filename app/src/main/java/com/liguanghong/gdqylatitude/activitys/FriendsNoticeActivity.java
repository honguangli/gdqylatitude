package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
    private FriendsNoticeAdapter friendsNoticeAdapter;
    private static Handler friendsNoticeHandler;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_friends_notice;
    }

    @Override
    protected void initView() {
        lv_friend_apply = (ListView)findViewById(R.id.lv_friend_notice);
        backtrack = (ImageView)findViewById(R.id.backtrack);

        backtrack.setOnClickListener(this);
        lv_friend_apply.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        friendsNoticeHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 222:
                        if(friendsNoticeAdapter != null)
                            friendsNoticeAdapter.notifyDataSetChanged();
                        break;
                }
            }
        };
        friendsNoticeAdapter = new FriendsNoticeAdapter(getApplicationContext());
        lv_friend_apply.setAdapter(friendsNoticeAdapter);
        friendsNoticeAdapter.notifyDataSetChanged();
    }

    //按钮点击监听
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backtrack:
                //销毁当前界面，返回通讯录界面
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //启动好友申请详情界面
        startActivity(new Intent(getApplicationContext(),FriendsNoticeInfoActivity.class));
    }

    public static Handler getFriendsNoticeHandler(){
        return friendsNoticeHandler;
    }
}
