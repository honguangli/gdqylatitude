package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.FriendsNoticeAdapter;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.NoticesManager;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.unity.MessageType;
import com.liguanghong.gdqylatitude.unity.NoticeMsg;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.JsonResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

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
        getData();
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

    private void getData(){
        final RequestBody requestBody = new FormBody.Builder()
                .add("userid", UserManager.getInstance().getAppUser().getUserid() + "")
                .add("status", "")
                .build();
        HttpUtil.postEnqueue("notice/findnotices", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("通知管理", "查询通知失败");
            }

            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        if(result.isSuccess()){
                            List<NoticeMsg> list = JSONArray.parseArray(result.getData().toString(), NoticeMsg.class);
                            for(NoticeMsg noticeMsg : list){
                                if(noticeMsg.getNoticetype() > 14 && noticeMsg.getNoticetype() < 18)
                                    NoticesManager.getInstance().addFriendNotice(noticeMsg);
                                else
                                    NoticesManager.getInstance().addGroupNotice(noticeMsg);
                            }
                            friendsNoticeHandler.sendEmptyMessage(222);
                        } else{

                        }
                        Log.i("通知管理",  result.isSuccess() + "," + result.getMessage());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

}
