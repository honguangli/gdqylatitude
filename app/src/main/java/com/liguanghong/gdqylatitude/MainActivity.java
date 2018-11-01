package com.liguanghong.gdqylatitude;

import android.content.Intent;
import android.os.Bundle;
import com.liguanghong.gdqylatitude.activitys.HomeActivity;
import com.liguanghong.gdqylatitude.activitys.SignInActivity;
import com.liguanghong.gdqylatitude.base.BaseActivity;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        new Thread(){
            public void run(){
                try{
                    Thread.sleep(500);
                    checkIsLogin();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 检查是否已经登录
     */
    private void checkIsLogin(){
        if(false){
            //未登录过，跳转登录界面
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        } else{
            //已登录过，自动登录，进入主界面
            SignInActivity.signIn("Tom", "123456");
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

}
