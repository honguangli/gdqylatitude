package com.liguanghong.gdqylatitude.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liguanghong.gdqylatitude.MainActivity;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.AppManager;
import com.liguanghong.gdqylatitude.manager.GPSTrace;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.view.SwitchView;

public class SettingActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout rlyLocation,rlyFriend,rlyExit;
    private TextView tvTimeinterval;
    private ImageView ivBacktrack;
    private SwitchView swFriend;

    private AlertDialog refreshTimeDialog;
    private static int time;//更新时间间隔
    private boolean isOpened;//是否提醒好友上线
    private static String [] timeValue = new String[4];
    private AlertDialog.Builder refreshTimeBuilder;
    private GPSTrace gps;

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
        swFriend = findViewById(R.id.switchview_friend);

        rlyExit.setOnClickListener(this);
        rlyFriend.setOnClickListener(this);
        rlyLocation.setOnClickListener(this);
        ivBacktrack.setOnClickListener(this);
        swFriend.setOnClickListener(this);
        gps = new GPSTrace(getApplication(), "gps_id_"+ UserManager.getInstance().getAppUser().getUserid());
    }

    @Override
    protected void initData() {
        timeValue[0]="1秒";
        timeValue[1]="3秒";
        timeValue[2]="5秒";
        timeValue[3]="10秒";
        tvTimeinterval.setText(timeValue[time].toString());
        refreshTimeBuilder = new AlertDialog.Builder(this);
        refreshTimeBuilder.setTitle("位置更新间隔");

        refreshTimeBuilder.setSingleChoiceItems(timeValue,time, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvTimeinterval.setText(timeValue[which].toString());
                time = which;
                refreshTimeDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.setting_location:
                refreshTimeDialog = refreshTimeBuilder.show();
                break;
            case R.id.switchview_friend:
                isOpened = swFriend.isOpened();
                if(isOpened){
                    gps.startService();
                } else{
                    gps.stopService();
                }
                Toast.makeText(this,"开关状态："+isOpened,Toast.LENGTH_SHORT).show();
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
