package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.JsonResult;
import com.liguanghong.gdqylatitude.util.UserManager;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignInActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_name;
    private EditText et_password;
    private Button btn_login;
    private TextView toRegister;
    private TextView forgotPassword;
    private static Handler loginHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_signin;
    }

    @Override
    protected void initView() {
        et_name = findViewById(R.id.login_et_name);
        et_password = findViewById(R.id.login_et_password);
        btn_login = findViewById(R.id.login_btn_login);
        toRegister = findViewById(R.id.to_register);
        forgotPassword = findViewById(R.id.forgotpassword);

        btn_login.setOnClickListener(this);
        toRegister.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        loginHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 200:
                        navigateToHome();
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
            case R.id.login_btn_login:
                signIn(et_name.getText().toString(), et_password.getText().toString());
                break;
            case R.id.to_register:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.forgotpassword:
                startActivity(new Intent(this, ResetPwdActivity.class));
                break;
        }
    }

    /**
     * 登录操作
     * @param logname
     * @param password
     */
    public static void signIn(final String logname, String password){
        RequestBody requestBody = new FormBody.Builder()
                .add("logname",logname)
                .add("password", password)
                .build();
        HttpUtil.postEnqueue("user/signin", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("登录操作",  "登录连接失败");
                Message message = new Message();
                message.what = 404;
                message.obj = "登录连接失败";
                loginHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        if(result.isSuccess()){
                            //登录成功
                            User user = ((JSONObject)result.getData()).toJavaObject(User.class);
                            //添加到用户管理
                            UserManager.addAppUser(user);
                            UserManager.addSocketClient();
                            loginHandler.sendEmptyMessage(200);
                        } else{
                            //登录失败
                            Message message = new Message();
                            message.what = 0;
                            message.obj = result.getMessage();
                            loginHandler.sendMessage(message);
                        }
                        Log.i("登录操作",  result.isSuccess() + "," + result.getMessage());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    /**
     * 登录成功，进入主界面
     */
    private void navigateToHome(){
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    /**
     * 登录失败，提示
     */
    private void tip(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
