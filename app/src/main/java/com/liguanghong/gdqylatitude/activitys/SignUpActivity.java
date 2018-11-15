package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.JsonResult;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends BaseActivity implements View.OnClickListener{

    private TextView signupTextview;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etSurepassword;
    private Button btnAgree;
    private ImageView ivBack;
    private static Handler signupHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_signup;
    }

    @Override
    protected void initView() {

        signupTextview = findViewById(R.id.signup_textview);
        etUsername = findViewById(R.id.register_et_username);
        etPassword = findViewById(R.id.register_et_password);
        etSurepassword = findViewById(R.id.register_et_ensurepassword);
        btnAgree = findViewById(R.id.register_btn_argee);
        ivBack = findViewById(R.id.register_backtrack);

        btnAgree.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        setText();

    }

    @Override
    protected void initData() {
        signupHandler = new Handler(){
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

    /**
     * 设置TextView文字
     */
    private void setText(){
        SpannableString spannableString = new SpannableString("已阅读并同意以下协议   《纵横天下服务协议》、《隐私权政策》");
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 10, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signupTextview.setText(spannableString);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.register_btn_argee:
                if ((etPassword.getText().toString()).equals(etSurepassword.getText().toString())){
                    signUp(etUsername.getText().toString(),etPassword.getText().toString());

                }else{
                    Toast.makeText(this,"密码不一致，请重新输入！",Toast.LENGTH_SHORT).show();
                    etSurepassword.setText(null);
                }
                break;
            case R.id.register_backtrack:
                startActivity(new Intent(this,SignInActivity.class));
                break;
        }
    }

    /**
     * 注册操作
     * @param logname
     * @param password
     *
     */
    private void signUp(String logname, String password){
        RequestBody requestBody = new FormBody.Builder()
                .add("logname", logname)
                .add("password", password)
                .build();
        HttpUtil.postEnqueue("user/signup", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("注册操作",  "操作失败，未知原因");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        if(result.isSuccess()){
                            //注册成功
                            User user = ((JSONObject)result.getData()).toJavaObject(User.class);
                            signupHandler.sendEmptyMessage(200);
                        } else{
                            //注册失败
                            Log.i("注册操作",  result.getMessage());
                        }
                        Log.i("注册操作",  result.isSuccess() + "," + result.getMessage());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }


    /**
     * 注册成功，返回登录页面
     */
    private void navigateToSignin(){
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    /**
     * 登录失败，提示
     */
    private void tip(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}