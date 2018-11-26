package com.liguanghong.gdqylatitude;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.liguanghong.gdqylatitude.activitys.HomeActivity;
import com.liguanghong.gdqylatitude.activitys.PermissionsActivity;
import com.liguanghong.gdqylatitude.activitys.SignInActivity;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.util.PermissionsUtil;


public class MainActivity extends BaseActivity {

    private static final int REQUEST_CODE = 0; // 请求码
    private PermissionsUtil permissionsUtil; // 权限检测器

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
    };

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
        permissionsUtil = new PermissionsUtil(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (permissionsUtil.lacksPermissions(PERMISSIONS)) {
            // 缺少权限时, 进入权限配置页面
            startPermissionsActivity();
        } else{
            // 含有全部权限时，检查是否登录过
            new Thread(){
                public void run(){
                    try{
                        Thread.sleep(300);
                        checkIsLogin();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
            finish();
        } else{
            // 含有全部权限时，检查是否登录过
            new Thread(){
                public void run(){
                    try{
                        Thread.sleep(300);
                        checkIsLogin();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    /**
     * 检查是否已经登录
     */
    private void checkIsLogin(){
        UserManager.setSharedPreferences(getSharedPreferences("data", MODE_PRIVATE));
        if(true){
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
