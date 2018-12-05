package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.GroupMemberManageAdapter;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.JsonResult;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GroupMemberManageActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;
    private ImageView backtrack;
    private TextView menu;

    private ListView listView;
    private GroupMemberManageAdapter groupLeaguerManageAdapter;
    private Integer groupid;
    private boolean permission;
    private Handler groupMemberHandler;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_member_manage;
    }

    @Override
    protected void initView() {
        title = findViewById(R.id.title);
        backtrack = findViewById(R.id.backtrack);

        backtrack.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        groupMemberHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 200:
                        List<User> list = (List<User>)message.obj;
                        groupLeaguerManageAdapter.setList(list);
                        break;
                }
            }
        };
        Intent intent = getIntent();
        groupid = intent.getIntExtra("groupid", -1);
        permission = intent.getBooleanExtra("permission", false);
        if(permission)
            title.setText("群成员管理");

        listView = findViewById(R.id.listView);
        groupLeaguerManageAdapter = new GroupMemberManageAdapter(this, permission);
        listView.setAdapter(groupLeaguerManageAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        getData();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.backtrack:
                finish();
                break;

            case R.id.menu:

                break;
        }
    }

    private void getData(){
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", UserManager.getInstance().getAppUser().getUserid() + "")
                .add("groupid", groupid + "")
                .add("size", "")
                .build();
        HttpUtil.postEnqueue("group/searchmember", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("查询群成员操作",  "连接失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        if(result.isSuccess()){
                            List<User> list = JSONArray.parseArray(result.getData().toString(), User.class);
                            Message message = new Message();
                            message.what = 200;
                            message.obj = list;
                            groupMemberHandler.sendMessage(message);
                        } else{

                        }
                        Log.i("查询群成员操作",  result.isSuccess() + "," + result.getMessage());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

}
