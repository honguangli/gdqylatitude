package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ResetPwdActivity extends BaseActivity implements View.OnClickListener{

    private EditText etEmail,etSecuritycode,etNewpassword,etSureNewpassword;
    private Button btnAgree;
    private TextView tvSecuritycode;
    private ImageView ivBacktrack;
    private static Handler resetpwdHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_resetpassword;
    }

    @Override
    protected void initView() {

        etEmail = findViewById(R.id.resetpassword_et_email);
        etSecuritycode = findViewById(R.id.resetpassword_et_securitycode);
        etNewpassword = findViewById(R.id.resetpassword_et_password);
        etSureNewpassword = findViewById(R.id.register_et_ensurepassword);
        ivBacktrack = findViewById(R.id.resetpassword_backtrack);
        tvSecuritycode = findViewById(R.id.resetpassword_tv_securitycode);
        btnAgree = findViewById(R.id.resetpassword_btn_argee);

        ivBacktrack.setOnClickListener(this);
        tvSecuritycode.setOnClickListener(this);
        btnAgree.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        resetpwdHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 200:
                        navigateToSignin();
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
            case R.id.resetpassword_backtrack:
                finish();
                break;
            case R.id.resetpassword_tv_securitycode:

                if((etEmail.getText().toString()).equals(null)){
                    Toast.makeText(this,"邮箱不可为空，请输入！",Toast.LENGTH_SHORT).show();
                }else{
                    if(isEmail(etEmail.getText().toString())){
                        Toast.makeText(this,"邮箱输入正确",Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(this,"邮箱格式错误，请重新输入！",Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.resetpassword_btn_argee:
                if ((etNewpassword.getText().toString()).equals(etSureNewpassword.getText().toString())){
                    resetPwd(etEmail.getText().toString(),etNewpassword.getText().toString());

                }else{
                    Toast.makeText(this,"密码不一致，请重新输入！",Toast.LENGTH_SHORT).show();
                    etSureNewpassword.setText(null);
                }

                break;
        }
    }

    /**
     * 邮箱验证
     */
    public static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        if (TextUtils.isEmpty(strPattern)) {
            return false;
        } else {
            return strEmail.matches(strPattern);
        }
    }


    /**
     * 重置密码操作
     * @param email
     * @param password
     */
    public static void resetPwd(final String email, String password){
        RequestBody requestBody = new FormBody.Builder()
                .add("email",email)
                .add("password", password)
                .build();
        HttpUtil.postEnqueue("user/signin", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("重置密码操作",  "重置密码失败");
                Message message = new Message();
                message.what = 404;
                message.obj = "重置密码失败";
                resetpwdHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        if(result.isSuccess()){
                            //重置成功
                            User user = ((JSONObject)result.getData()).toJavaObject(User.class);
                            //添加到用户管理
                            UserManager.getInstance().addAppUser(user);
                            //UserManager.addSocketClient();
                            resetpwdHandler.sendEmptyMessage(200);
                        } else{
                            //重置失败
                            Message message = new Message();
                            message.what = 0;
                            message.obj = result.getMessage();
                            resetpwdHandler.sendMessage(message);
                        }
                        Log.i("重置密码操作",  result.isSuccess() + "," + result.getMessage());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    /**
     * 重置密码成功，返回登录页面
     */
    private void navigateToSignin(){
        finish();
    }

    /**
     * 重置密码失败，提示
     */
    private void tip(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}