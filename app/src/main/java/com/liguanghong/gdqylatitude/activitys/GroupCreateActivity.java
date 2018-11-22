package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.GroupManager;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.unity.Groupchat;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.JsonResult;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;


public class GroupCreateActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backtrack_create;
    private CircleImageView bt_add;
    private RelativeLayout rly_add_newgroup;
    private EditText ed_chatname;
    private Button btn_submit;
    private Handler groupCreateHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_create;
    }

    @Override
    protected void initView() {
        backtrack_create = (ImageView)findViewById(R.id.backtrack);
        bt_add = findViewById(R.id.bt_add);
        rly_add_newgroup = (RelativeLayout)findViewById(R.id.add);
        btn_submit = (Button)findViewById(R.id.btn_submit);
        ed_chatname = (EditText)findViewById(R.id.ed_chatname);

        backtrack_create.setOnClickListener(this);
        rly_add_newgroup.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        groupCreateHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 200:
                        finish();
                        break;
                    case 400:
                        tip(message.obj.toString());
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.add:         //选择头像
                Intent selectHead = new Intent(GroupCreateActivity.this,SelectPhotoActivity.class);
                startActivityForResult(selectHead,10);
                break;

            case R.id.btn_submit:               //确认创建
                String groupName = ed_chatname.getText().toString();
                if(groupName != null && !groupName.trim().equals("")){
                    createGroup(groupName.trim());
                }
                break;

            case R.id.backtrack:
                //销毁当前界面，返回通讯录界面
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == 20) {
            String photo = (String) data.getExtras().get("photo");
            System.out.println("666666666666666666"+photo);
            bt_add.setImageURI(Uri.parse(photo));
        }
    }

    private void createGroup(String groupName){
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", UserManager.getInstance().getAppUser().getUserid()+"")
                .add("groupname", groupName)
                .build();
        HttpUtil.postEnqueue("group/creategroup", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = 400;
                message.obj = "连接不上服务器，群聊创建失败";
                groupCreateHandler.sendMessage(message);
                Log.i("群聊管理", "群聊创建失败");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        if(result.isSuccess()){
                            groupCreateHandler.sendEmptyMessage(200);
                        } else{
                            Message message = new Message();
                            message.what = 400;
                            message.obj = result.getMessage();
                            groupCreateHandler.sendMessage(message);
                        }
                        Log.i("群聊管理",  result.isSuccess() + "," + result.getMessage());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void tip(String tips){
        Toast.makeText(this, tips, Toast.LENGTH_SHORT);
    }

}
