package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.FriendsManager;
import com.liguanghong.gdqylatitude.manager.WebSocketManager;
import com.liguanghong.gdqylatitude.unity.Friend;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.JsonResult;
import com.liguanghong.gdqylatitude.manager.UserManager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignInActivity extends BaseActivity implements View.OnClickListener {

    private boolean isHide = true;
    private EditText et_name;
    private EditText et_password;
    private Button btn_login;
    private TextView toRegister;
    private TextView forgotPassword;
    private ImageView ivPassword;
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
        et_name =findViewById(R.id.login_et_name);
        et_password = findViewById(R.id.login_et_password);
        btn_login = findViewById(R.id.login_btn_login);
        toRegister = findViewById(R.id.to_register);
        forgotPassword = findViewById(R.id.forgotpassword);
        ivPassword = findViewById(R.id.login_iv_password);

        btn_login.setOnClickListener(this);
        toRegister.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        ivPassword.setOnClickListener(this);
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
            case R.id.login_iv_password:
                checkPassword();
                break;
        }
    }
    /* *
     *修改密码隐藏
     */
    private void checkPassword(){
        if(isHide){
            ivPassword.setImageResource(R.drawable.pass);
            et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            isHide = false;
        }else{
            ivPassword.setImageResource(R.drawable.hintpass);
            et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            isHide = true;
        }
    }

    /**
     * 登录操作
     * @param logname
     * @param password
     */
    public static void signIn(final String logname, final String password){
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
                            JSONObject object = (JSONObject)result.getData();
                            User user = JSONObject.parseObject(object.getString("user"), User.class);
                            Map<String, List<Friend>> friends = JSONObject.parseObject(object.getString("friends"), new TypeReference<Map<String, List<Friend>>>() {});
                            FriendsManager.getInstance().setFriends(friends);
                            user.setPassword(password);
                            UserManager.getInstance().addAppUser(user);
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
