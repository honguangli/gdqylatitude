package com.liguanghong.gdqylatitude.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.liguanghong.gdqylatitude.util.AppManager;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this); //添加到栈中
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().finishActivity(this); //从栈中移除
    }
}
