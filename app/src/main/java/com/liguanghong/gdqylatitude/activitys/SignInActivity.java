package com.liguanghong.gdqylatitude.activitys;

import android.os.Bundle;
import android.util.Log;

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

public class SignInActivity extends BaseActivity {

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

    }

    @Override
    protected void initData() {

    }

    /**
     * 登录操作
     * @param logname
     * @param password
     */
    public static void signIn(String logname, String password){
        RequestBody requestBody = new FormBody.Builder()
                .add("logname",logname)
                .add("password", password)
                .build();
        HttpUtil.postEnqueue("user/signin", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("登录操作",  "操作失败，未知原因");
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
                        } else{
                            //登录失败
                        }
                        Log.i("登录操作",  result.isSuccess() + "," + result.getMessage());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

}
