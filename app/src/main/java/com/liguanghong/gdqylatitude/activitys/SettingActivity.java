package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.AppManager;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_quit;
    private ImageView Backtrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        btn_quit=(Button)findViewById(R.id.btn_quit_login);
        Backtrack=(ImageView)findViewById(R.id.backtrack);

        Backtrack.setOnClickListener(this);
        btn_quit.setOnClickListener(this);

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_quit_login:
                Intent intent = new Intent(SettingActivity.this, SignInActivity.class);
                startActivity(intent);
                AppManager.getInstance().finishAllActivity();
                break;
            case R.id.backtrack:
                finish();
                break;
        }
    }
}
