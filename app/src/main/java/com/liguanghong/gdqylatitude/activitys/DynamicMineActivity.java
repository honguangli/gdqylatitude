package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.DynamicFriendsAdapter;
import com.liguanghong.gdqylatitude.adapter.DynamicMineAdapter;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.unity.Dynamic;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.ImageUtils;
import com.liguanghong.gdqylatitude.util.JsonResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DynamicMineActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backtrack,write_messge;
    private ListView listView;
    public static List<Dynamic> list;
    DynamicMineAdapter dynamicMineAdapter;
    private static Handler dynamicHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dynamic_mine;
    }

    @Override
    protected void initView() {
        backtrack = findViewById(R.id.backtrack);
        write_messge = findViewById(R.id.write_messge);

        backtrack.setOnClickListener(this);
        write_messge.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        list = new ArrayList<Dynamic>();
        dynamicHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 200:
                        Collections.reverse(list);
                        dynamicMineAdapter = new DynamicMineAdapter(DynamicMineActivity.this,list);
                        listView = findViewById(R.id.lv_dynamic_mine);
                        listView.setAdapter(dynamicMineAdapter);
                        dynamicMineAdapter.notifyDataSetChanged();
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
            case R.id.write_messge:
                Intent intent = new Intent(DynamicMineActivity.this,DynamicPublicActivity.class);
                startActivityForResult(intent,10);
                //startActivity(intent);
                break;

            case R.id.backtrack:
                finish();
                break;
        }
    }

    public static void dynamicfriends(){
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", UserManager.getInstance().getAppUser().getUserid() + "")
                .build();
        HttpUtil.postEnqueue("dynamic/findmy", requestBody, new Callback() {
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
                            Log.i("qq",list.size()+"");
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == 20) {
            dynamicfriends();
        }
    }
}
