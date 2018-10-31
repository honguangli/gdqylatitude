package com.liguanghong.gdqylatitude;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ZoomControls;

import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.liguanghong.gdqylatitude.activitys.HomeActivity;
import com.liguanghong.gdqylatitude.activitys.SignInActivity;
import com.liguanghong.gdqylatitude.fragment.AddressbookFragment;
import com.liguanghong.gdqylatitude.fragment.MapFragment;
import com.liguanghong.gdqylatitude.fragment.MessageFragment;
import com.liguanghong.gdqylatitude.fragment.MineFragment;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.JsonResult;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        checkIsLogin();
    }

    public static User getInstance() {
        if (user == null) {
            synchronized (User.class) {
                if (user == null) {
                    user = new User();
                }
            }
        }
        return user;
    }

    /**
     * 检查是否已经登录
     */
    private void checkIsLogin(){
        if(false){
            //未登录过，跳转登录界面
            signUp();
            startActivity(new Intent(this, SignInActivity.class));
        } else{
            //已登录过，自动登录，进入主界面
            signIn();
            startActivity(new Intent(this, HomeActivity.class));
        }
    }

    /**
     * 登录操作
     */
    private void signIn(){
        RequestBody requestBody = new FormBody.Builder()
                .add("logname","Tom")
                .add("password", "123456")
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
                            Log.i("登录操作", user.getUserid() + " : " + user.getLogname());
                        } else{
                            //登录失败
                            Log.i("登录操作",  result.getMessage());

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    /**
     * 注册操作
     */
    private void signUp(){
        RequestBody requestBody = new FormBody.Builder()
                .add("logname","ALJFLDJ")
                .add("password", "123456")
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
                            //登录成功
                            User user = ((JSONObject)result.getData()).toJavaObject(User.class);
                            Log.i("注册操作", result.getMessage() + " : " + user.getUserid() + " : " + user.getLogname());
                        } else{
                            //登录失败
                            Log.i("注册操作",  result.getMessage());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    public void onActivityResult(int requestCode,int resultCoded,Intent data){
        super.onActivityResult(requestCode,resultCoded,data);
        if (requestCode == 1 && resultCoded == 10){
            HomeActivity homeActivity = (HomeActivity)getApplicationContext();
            FragmentManager fragmentManager = homeActivity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contentPanel,new AddressbookFragment());
            fragmentTransaction.commit();

        }
    }
}
