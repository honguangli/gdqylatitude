package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.JsonResult;
import com.liguanghong.gdqylatitude.view.LoadingDialog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserInfoMineActivity extends BaseActivity implements View.OnClickListener{

    private EditText etUsername,etSex,etRealname,etPhone,etEmail;
    private ImageView ivBacktrack,ivTouxiang;
    private TextView tvSure;
    private static Handler userinfoHandler;
    LoadingDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info_mine;
    }

    @Override
    protected void initView() {
        etUsername = findViewById(R.id.personalinfo_et_username);
        etSex = findViewById(R.id.personalinfo_et_sex);
        etRealname = findViewById(R.id.personalinfo_et_realname);
        etPhone = findViewById(R.id.personalinfo_et_phone);
        etEmail = findViewById(R.id.personalinfo_et_email);
        ivBacktrack = findViewById(R.id.personalinfo_backtrack);
        tvSure = findViewById(R.id.personalinfo_sure);
        ivTouxiang = findViewById(R.id.personalinfo_iv_touxiang);

        ivBacktrack.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        ivTouxiang.setOnClickListener(this);
        dialog =new LoadingDialog(this,"玩命发送中...");

    }

    @Override
    protected void initData() {
        userinfoHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 200:
                        finish();
                        break;
                    default:
                        tip(String.valueOf(message.obj));
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.personalinfo_backtrack:
                startActivity(new Intent(this,HomeActivity.class));
                break;
            case R.id.personalinfo_sure:

                dialog.show();
                Log.i("dialog","已显示");
                commitInfo(etUsername.getText().toString(),etSex.getText().toString(),etRealname.getText().toString(),etPhone.getText().toString(),etEmail.getText().toString(),"11");

                //dialog.close();
                break;
            case R.id.personalinfo_iv_touxiang:

                break;
        }
    }

    /**
     * 修改用户信息
     */
    private void commitInfo(final String logname, String sex, String username, String phone, String email,String headportrait){

        RequestBody requestBody = new FormBody.Builder()
                .add("logname",logname)
                .add("sex", sex)
                .add("username",username)
                .add("phone",phone)
                .add("email",email)
                .add("headportrait",headportrait)
                .build();
        HttpUtil.postEnqueue("user/update", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("更新操作",  "更新操作失败");
                Message message = new Message();
                message.what = 404;
                message.obj = "更新操作失败";
                userinfoHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        if(result.isSuccess()){
                            //更新成功
                            User user = ((JSONObject)result.getData()).toJavaObject(User.class);
                            UserManager.addAppUser(user);

                        } else{
                            //更新失败
                            Message message = new Message();
                            message.what = 0;
                            message.obj = result.getMessage();
                            userinfoHandler.sendMessage(message);
                        }
                        Log.i("修改个人信息",  result.isSuccess() + "," + result.getMessage());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dialog.close();
    }

    /**
     *修改成功返回主页面
     */
    private void navigateToHome(){
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    /**
     * 修改失败
     */
    private void tip(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 更新本地用户信息存储
     */

}
