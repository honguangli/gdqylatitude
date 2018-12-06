package com.liguanghong.gdqylatitude.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.AppManager;
import com.liguanghong.gdqylatitude.manager.GroupManager;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.unity.Groupchat;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.util.CheckCellphoneEmailUtil;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.ImageUtils;
import com.liguanghong.gdqylatitude.util.JsonResult;
import com.liguanghong.gdqylatitude.view.LoadingDialog;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.liguanghong.gdqylatitude.util.ImageUtils.getPicFromBytes;

public class GroupManageActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backtrack;                            //返回群组信息界面
    private TextView menu;                                    //完成
    private CircleImageView ivTouxiang;
    private EditText et_group_name;                         //群组名
    private EditText et_group_messges;                      //群公告
    private RelativeLayout rl_group_member_manage;                 //群成员管理
    private RelativeLayout rl_group_sign_manage;                 //签到管理
    private RelativeLayout rl_group_dissolve;               //解散群组
    private Groupchat groupchat;
    private static Handler GroupManageHandler;
    private LoadingDialog dialog;
    private int resultCode = AppManager.NOTCHANGE;
    private String headPic;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_manage;
    }

    @Override
    protected void initView() {
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE); //获取editor对象
        editor = sharedPreferences.edit();//获取编辑器

        backtrack = (ImageView)findViewById(R.id.backtrack);
        menu = (TextView)findViewById(R.id.menu);
        et_group_name = (EditText)findViewById(R.id.et_group_name);
        et_group_messges = (EditText)findViewById(R.id.et_group_notice);
        rl_group_member_manage = (RelativeLayout)findViewById(R.id.rl_group_member_manage);
        rl_group_sign_manage = (RelativeLayout) findViewById(R.id.rl_group_sign_manage);
        rl_group_dissolve = (RelativeLayout) findViewById(R.id.rl_group_dissolve);
        ivTouxiang = findViewById(R.id.personalinfo_iv_touxiang);


        backtrack.setOnClickListener(this);
        menu.setOnClickListener(this);
        rl_group_member_manage.setOnClickListener(this);
        rl_group_sign_manage.setOnClickListener(this);
        rl_group_dissolve.setOnClickListener(this);
        ivTouxiang.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        GroupManageHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 200:
                        dialog.setText("更新成功");
                        dialog.close();
                        resultCode = AppManager.SUCCESS;
                        GroupManager.getInstance().updateGroup(groupchat);
                        break;
                    default:
                        dialog.setText(String.valueOf(message.obj));
                        dialog.close();
                        resultCode = AppManager.ERROR;
                        break;
                }
            }
        };

        groupchat = (Groupchat)getIntent().getSerializableExtra("groupchat");
        //获取用户的个人信息
        byte[] b = Base64.decode(groupchat.getHeadportrait(), Base64.DEFAULT);
        ivTouxiang.setImageBitmap(ImageUtils.getPicFromBytes(b,null));
        et_group_name.setText(groupchat.getGroupname());
        et_group_messges.setText(groupchat.getAnnouncement());
        headPic = ImageUtils.bitmapToString(getPicFromBytes(b,null));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.rl_group_dissolve:                   //解散群组

                break;

            case R.id.rl_group_sign_manage:
                //启动签到管理界面
                startActivity(new Intent(getApplicationContext(),GroupSignManageActivity.class));
                break;

            case R.id.rl_group_member_manage:
                //启动群成员界面
                Intent intent = new Intent(this, GroupMemberManageActivity.class);
                intent.putExtra("groupid", groupchat.getGroupid());
                intent.putExtra("permission", true);
                startActivity(intent);
                break;

            case R.id.personalinfo_iv_touxiang:                     //头像
                SelectPhotoActivity.isMultiSelect = true;
                editor.putBoolean("isMultiSelect",SelectPhotoActivity.isMultiSelect);
                editor.commit();

                Intent photo=new Intent(GroupManageActivity.this,SelectPhotoActivity.class);
                startActivityForResult(photo,10);
                break;

            case R.id.menu:                                 //完成
                if (checkUpInfo()){
                    dialog = new LoadingDialog(this,"玩命更新中...");
                    dialog.show();
                    ivTouxiang.setOnClickListener(null);
                    update(et_group_name.getText().toString().trim(),
                            et_group_messges.getText().toString().trim(),
                            headPic);
                    groupchat.setGroupname(et_group_name.getText().toString().trim());
                    groupchat.setAnnouncement(et_group_messges.getText().toString().trim());
                    groupchat.setHeadportrait(headPic.getBytes());
                    editTextEnabled(false, Color.parseColor("#BFBFBF"));
                }

                break;

            case R.id.backtrack:
                //销毁当前界面，返回群信息界面
                Intent mintent = new Intent(this, GroupManageActivity.class);
                mintent.putExtra("groupinfo", groupchat);
                setResult(AppManager.SUCCESS, mintent);
                finish();
                break;
        }
    }
    private void update(String groupname, String groupmessges,String headportrait){
        RequestBody requestBody = new FormBody.Builder()
                .add("groupid",groupchat.getGroupid()+"")
                .add("ownerid", UserManager.getInstance().getAppUser().getUserid()+"")
                .add("groupname",groupname)
                .add("announcement", groupmessges)
                .add("headportrait",headportrait)
                .build();
        HttpUtil.postEnqueue("group/updategroup", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("更新操作",  "更新操作失败");
                Message message = new Message();
                message.what = 404;
                message.obj = "无法连接服务器";
                GroupManageHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        if(result.isSuccess()){
                            GroupManageHandler.sendEmptyMessage(200);
                        } else{
                            //更新失败
                            Message message = new Message();
                            message.what = 500;
                            message.obj = result.getMessage();
                            GroupManageHandler.sendMessage(message);
                        }
                        Log.i("修改群组信息",  result.isSuccess() + "," + result.getMessage());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }
    private void editTextEnabled(boolean enabled,int textcolor){
        et_group_name.setEnabled(enabled);
        et_group_messges.setEnabled(enabled);

        et_group_name.setTextColor(textcolor);
        et_group_messges.setTextColor(textcolor);
    }
    private boolean checkUpInfo(){
        if (et_group_name.getText().toString().trim().equals("")){
            Toast.makeText(this,"群名不能为空",Toast.LENGTH_LONG).show();
            return false;
        }else if (et_group_messges.getText().toString().trim().equals("")){
            Toast.makeText(this,"群公告不能为空",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == 20) {
            String photo = (String) data.getExtras().get("photo");
            ivTouxiang.setImageURI(Uri.parse(photo));
            headPic = ImageUtils.filePathToString(photo);
        }
    }
}
