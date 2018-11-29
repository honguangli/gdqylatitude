package com.liguanghong.gdqylatitude.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.MainActivity;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.AppManager;

public class SettingActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout rlyLocation,rlyFriend,rlyExit;
    private TextView tvTimeinterval;
    private ImageView ivBacktrack;
    private AlertDialog refreshTimeDialog;

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

        rlyLocation = findViewById(R.id.setting_location);
        rlyFriend = findViewById(R.id.setting_friend);
        rlyExit = findViewById(R.id.setting_exit);
        ivBacktrack = findViewById(R.id.setting_backtrack);
        tvTimeinterval = findViewById(R.id.location_timeinterval);

        rlyExit.setOnClickListener(this);
        rlyFriend.setOnClickListener(this);
        rlyLocation.setOnClickListener(this);
        ivBacktrack.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.setting_location:
                final String [] timeValue=new String[4];
                timeValue[0]="1秒";
                timeValue[1]="3秒";
                timeValue[2]="5秒";
                timeValue[3]="10秒";
                AlertDialog.Builder refreshTimeBuilder = new AlertDialog.Builder(view.getContext());
                refreshTimeBuilder.setTitle("位置更新间隔");
                //refreshTimeBuilder.setIcon(R.drawable.icon_refreshtime);
                refreshTimeBuilder.setSingleChoiceItems(timeValue,0, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        tvTimeinterval.setText(timeValue[which].toString());
                        refreshTimeDialog.dismiss();
                    }
                });
                refreshTimeDialog = refreshTimeBuilder.show();
                break;
            case R.id.setting_friend:

                break;
            case R.id.setting_exit:
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                AppManager.getInstance().finishActivity(HomeActivity.class);
                break;
            case R.id.setting_backtrack:
                finish();
                break;
        }
    }
}
