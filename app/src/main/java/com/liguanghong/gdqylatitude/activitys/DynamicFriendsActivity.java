package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.DynamicFriendsAdapter;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.FriendsManager;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.unity.Dynamic;
import com.liguanghong.gdqylatitude.unity.Friend;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.ImageUtils;
import com.liguanghong.gdqylatitude.util.JsonResult;
import com.liguanghong.gdqylatitude.view.FadingScrollView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DynamicFriendsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backtrack;
    private CircleImageView img_headphoto1;
    private RelativeLayout topPanel;
    private FadingScrollView fadingScrollView;
    private ListView listView;
    private static List<Dynamic> list;
    private static List<User> userlist;
    DynamicFriendsAdapter dynamicFriendsAdapter;
    private static Handler dynamicinfoHandler;
    private static Handler dynamicHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dynamic_friends;
    }

    @Override
    protected void initView() {
        backtrack = findViewById(R.id.backtrack);

        img_headphoto1 = findViewById(R.id.img_headphoto1);
        topPanel = findViewById(R.id.topPanel);

        byte[] b = Base64.decode(UserManager.getInstance().getAppUser().getHeadportrait(), Base64.DEFAULT);
        img_headphoto1.setImageBitmap(ImageUtils.getPicFromBytes(b,null));
        //topPanel.setAlpha(0); //设置标题栏的透明度

        fadingScrollView = findViewById(R.id.nac_root);
        fadingScrollView.smoothScrollTo(0, 0);  //设置刚进入页面就在顶部
        fadingScrollView.setFadingView(topPanel);       //设置标题栏
        fadingScrollView.setFadingHeightView(findViewById(R.id.nac_image)); //图片及其高度

        backtrack.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        list=new ArrayList<>();
        userlist = new ArrayList<>();
        dynamicHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 200:
                        for (int i=0; i<list.size();i++){
                            Log.i("ww",list.get(i).getUserid()+"");
                            dynamicfriendsinfo(list.get(i).getUserid());
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        dynamicinfoHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 200:
                        dynamicFriendsAdapter = new DynamicFriendsAdapter(DynamicFriendsActivity.this,list,userlist);
                        listView = findViewById(R.id.lv);
                        listView.setAdapter(dynamicFriendsAdapter);
                        dynamicFriendsAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        };
        dynamicfriends();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.backtrack:
                finish();
                break;
        }
    }
    public static void dynamicfriends(){
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", UserManager.getInstance().getAppUser().getUserid() + "")
                .build();
        HttpUtil.postEnqueue("dynamic/findfriends", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("获取动态",  "获取动态连接失败");
                Message message = new Message();
                message.what = 404;
                message.obj = "获取动态连接失败";
                dynamicHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        if(result.isSuccess()){
                            list= JSONArray.parseArray(result.getData().toString(),Dynamic.class);
                            dynamicHandler.sendEmptyMessage(200);
                        } else{
                            //登录失败
                            Message message = new Message();
                            message.what = 0;
                            message.obj = result.getMessage();
                            dynamicHandler.sendMessage(message);
                        }
                        Log.i("获取动态",  result.isSuccess() + "," + result.getMessage());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }
    public static void dynamicfriendsinfo(Integer dynamicid){
        RequestBody requestBody = new FormBody.Builder()
                .add("userid",dynamicid.toString())
                .build();
        HttpUtil.postEnqueue("user/find", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("获取动态",  "获取动态连接失败");
                Message message = new Message();
                message.what = 404;
                message.obj = "获取动态连接失败";
                dynamicinfoHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        if(result.isSuccess()){
                            User user = JSONObject.parseObject(result.getData().toString(), User.class);
                            userlist.add(user);
                            Log.i("qq",userlist.size()+"");
                            Log.i("cc",userlist+"");
                            if (userlist.size()==list.size()){
                                dynamicinfoHandler.sendEmptyMessage(200);
                            }
                        } else{
                            Message message = new Message();
                            message.what = 0;
                            message.obj = result.getMessage();
                            dynamicinfoHandler.sendMessage(message);
                        }
                        Log.i("获取用户",  result.isSuccess() + "," + result.getMessage());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }
}
