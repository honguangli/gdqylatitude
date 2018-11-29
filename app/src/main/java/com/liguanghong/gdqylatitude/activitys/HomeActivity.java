package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baidu.mapapi.SDKInitializer;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.FragmentAdapter;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.base.BaseViewPager;
import com.liguanghong.gdqylatitude.fragment.AddressbookFragment;
import com.liguanghong.gdqylatitude.fragment.MapFragment;
import com.liguanghong.gdqylatitude.fragment.MessageFragment;
import com.liguanghong.gdqylatitude.fragment.MineFragment;
import com.liguanghong.gdqylatitude.manager.ConversationManager;
import com.liguanghong.gdqylatitude.manager.FriendsManager;
import com.liguanghong.gdqylatitude.manager.GroupManager;
import com.liguanghong.gdqylatitude.manager.NoticesManager;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.manager.WebSocketManager;
import com.liguanghong.gdqylatitude.unity.Friend;
import com.liguanghong.gdqylatitude.unity.Groupchat;
import com.liguanghong.gdqylatitude.unity.MessageType;
import com.liguanghong.gdqylatitude.unity.NoticeMsg;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.JsonResult;

import org.java_websocket.client.WebSocketClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private BaseViewPager viewPager;

    private LinearLayout map;
    private LinearLayout message;
    private LinearLayout addressbook;
    private LinearLayout mine;

    private ImageView iv_map;
    private ImageView iv_message;
    private ImageView iv_addressbook;
    private ImageView iv_mine;

    private TextView tv_map;
    private TextView tv_message;
    private TextView tv_addressbook;
    private TextView tv_mine;

    private ImageView iv_current;
    private TextView tv_current;
    private int id_current_gray;

    private static Handler homeHandler;
    private FragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView(){

        viewPager = findViewById(R.id.viewpager);

        map = findViewById(R.id.map);
        message = findViewById(R.id.message);
        addressbook = findViewById(R.id.addressbook);
        mine = findViewById(R.id.mine);
        map.setOnClickListener(this);
        message.setOnClickListener(this);
        addressbook.setOnClickListener(this);
        mine.setOnClickListener(this);

        iv_map = findViewById(R.id.map_iv);
        iv_message = findViewById(R.id.message_iv);
        iv_addressbook = findViewById(R.id.addressbook_iv);
        iv_mine = findViewById(R.id.mine_iv);
        tv_map = findViewById(R.id.map_tv);
        tv_message = findViewById(R.id.message_tv);
        tv_addressbook = findViewById(R.id.addressbook_tv);
        tv_mine = findViewById(R.id.mine_tv);

    }

    @Override
    protected void initData(){

        homeHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 200:
                        //显示
                        viewPager.setAdapter(fragmentAdapter);
                        break;
                    case 222:
                        //强制登出
                        logout();
                        break;
                }
            }
        };

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MapFragment());
        fragments.add(new MessageFragment());
        fragments.add(new AddressbookFragment());
        fragments.add(new MineFragment());
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOffscreenPageLimit(4);
        iv_current = iv_map;
        tv_current = tv_map;
        id_current_gray = R.drawable.map;

        getMyGroups();
        getNotices();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.map:
                viewPager.setCurrentItem(0, false);
                changeNavigationBar(iv_map, tv_map, R.drawable.map,  R.drawable.map_light);
                break;
            case R.id.message:
                viewPager.setCurrentItem(1, false);
                changeNavigationBar(iv_message, tv_message, R.drawable.message, R.drawable.message_light);
                break;
            case R.id.addressbook:
                viewPager.setCurrentItem(2, false);
                changeNavigationBar(iv_addressbook, tv_addressbook, R.drawable.addressbook, R.drawable.addressbook_light);
                break;
            case R.id.mine:
                viewPager.setCurrentItem(3, false);
                changeNavigationBar(iv_mine, tv_mine, R.drawable.mine, R.drawable.mine_light);
                break;

        }

    }

    @Override
    protected void onDestroy() {
        //释放资源
        ConversationManager.releaseResource();
        FriendsManager.releaseResource();
        GroupManager.releaseResource();
        NoticesManager.releaseResource();
        UserManager.releaseResource();
        WebSocketManager.releaseResource();
        super.onDestroy();
    }

    /**
     * 改变底部导航栏显示
     * @param iv
     * @param tv
     * @param id_next_gray
     * @param id_next_light
     */
    private void changeNavigationBar(ImageView iv, TextView tv, int id_next_gray, int id_next_light){

        iv_current.setImageDrawable(getResources().getDrawable(id_current_gray));
        iv_current = iv;
        iv_current.setImageDrawable(getResources().getDrawable(id_next_light));
        id_current_gray = id_next_gray;

        tv_current.setTextColor(getResources().getColor(R.color.colorBF));
        tv_current = tv;
        tv_current.setTextColor(getResources().getColor(R.color.colorTitle));

    }

    /**
     * 强制登出
     */
    private void logout(){
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    public static Handler getHomeHandler(){
        return homeHandler;
    }

    /**
     * 获取群组列表
     */
    private void getMyGroups(){
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", UserManager.getInstance().getAppUser().getUserid()+"")
                .build();
        HttpUtil.postEnqueue("group/fingmygroup", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("群聊管理", "失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        if(result.isSuccess()){
                            GroupManager.getInstance().setGroupchatList(JSONArray.parseArray(result.getData().toString(), Groupchat.class));
                            //连接socket
                            WebSocketManager.getInstance();
                            GroupActivity.groupHandler.sendEmptyMessage(200);
                        } else{

                        }
                        Log.i("群聊管理",  result.isSuccess() + "," + result.getMessage());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    /**
     * 获取通知
     */
    private void getNotices(){
        final RequestBody requestBody = new FormBody.Builder()
                .add("userid", UserManager.getInstance().getAppUser().getUserid() + "")
                .add("status", 1+"")
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
                                if(noticeMsg.getNoticetype() >= MessageType.LOGOUT && noticeMsg.getNoticetype() <= MessageType.FRIENDOFFLINE)
                                    NoticesManager.getInstance().addSystemNotice(noticeMsg);
                                else if(noticeMsg.getNoticetype() >= MessageType.FRIENDAPPLY && noticeMsg.getNoticetype() <= MessageType.FRIENDAGREED)
                                    NoticesManager.getInstance().addFriendNotice(noticeMsg);
                                else if(noticeMsg.getNoticetype() >= MessageType.GROUPAPPLY && noticeMsg.getNoticetype() <= MessageType.GROUPAGREED)
                                    NoticesManager.getInstance().addGroupNotice(noticeMsg);
                            }
                        } else{

                        }
                        Log.i("通知管理",  result.isSuccess() + "," + result.getMessage() + result.getData().toString());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }


}
