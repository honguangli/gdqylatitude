package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.GroupAdapter;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.GroupManager;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.unity.Groupchat;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.JsonResult;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GroupActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView lv_chat;
    private ImageView backtrack1;
    private RelativeLayout rly_group_note,rly_group_create;
    private GroupAdapter groupAdapter;
    public static Handler groupHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group;
    }

    @Override
    protected void initView() {
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
    protected void initData() {
        groupHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 200:
                        groupAdapter.notifyDataSetChanged();
                        break;
                }
            }
        };
//      getData();
        groupAdapter = new GroupAdapter(getApplicationContext());
        lv_chat.setAdapter(groupAdapter);
    }

//    private void getData(){
//        //网络访问获取用户信息
//        RequestBody requestBody = new FormBody.Builder()
//                .add("userid", UserManager.getAppUser().getUserid()+"")
//                .build();
//        HttpUtil.postEnqueue("group/fingmygroup", requestBody, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.i("群聊管理", "失败了");
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if(response.isSuccessful()){
//                    try {
//                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
//                        if(result.isSuccess()){
//                            GroupManager.setGroupchatList(JSONArray.parseArray(result.getData().toString(), Groupchat.class));
//                            groupHandler.sendEmptyMessage(200);
//                        } else{
//
//                        }
//                        Log.i("群聊管理",  result.isSuccess() + "," + result.getMessage());
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        });
//    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.rly_group_create:
                //启动新建群聊界面
                startActivity(new Intent(getApplicationContext(),GroupCreateActivity.class));
                break;

            case R.id.rly_group_note:
                //启动群通知界面
                startActivity(new Intent(getApplicationContext(),GroupNoticeActivity.class));
                break;

            case R.id.backtrack:
                //销毁当前界面，返回通讯录界面
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //启动群聊界面
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("groupinfo", (Groupchat)groupAdapter.getItem(i));
        startActivity(intent);
    }
}

