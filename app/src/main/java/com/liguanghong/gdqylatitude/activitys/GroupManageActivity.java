package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;

public class GroupManageActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backtrack;                            //返回群组信息界面
    private TextView menu;                                    //完成
    private EditText et_group_name;                         //群组名
    private EditText et_group_messges;                      //群公告
    private RelativeLayout rl_group_member_manage;                 //群成员管理
    private RelativeLayout rl_group_sign_manage;                 //签到管理
    private RelativeLayout rl_group_dissolve;               //解散群组

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_manage;
    }

    @Override
    protected void initView() {
        backtrack = (ImageView)findViewById(R.id.backtrack);
        menu = (TextView)findViewById(R.id.menu);
        et_group_name = (EditText)findViewById(R.id.et_group_name);
        et_group_messges = (EditText)findViewById(R.id.et_group_notice);
        rl_group_member_manage = (RelativeLayout)findViewById(R.id.rl_group_member_manage);
        rl_group_sign_manage = (RelativeLayout) findViewById(R.id.rl_group_sign_manage);
        rl_group_dissolve = (RelativeLayout) findViewById(R.id.rl_group_dissolve);


        backtrack.setOnClickListener(this);
        menu.setOnClickListener(this);
        rl_group_member_manage.setOnClickListener(this);
        rl_group_sign_manage.setOnClickListener(this);
        rl_group_dissolve.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.rl_group_dissolve:                   //解散群组

                break;

            case R.id.rl_group_sign_manage:
                //启动签到管理界面
                startActivity(new Intent(getApplicationContext(),GroupSignManageActivity.class));
                break;

            case R.id.rl_group_member_manage:
                //启动群成员界面
                Intent intent = new Intent(this, GroupMemberManageActivity.class);
                intent.putExtra("permission", true);
                startActivity(intent);
                break;

            case R.id.menu:                                 //完成

                break;

            case R.id.backtrack:
                //销毁当前界面，返回群信息界面
                finish();
                break;
        }
    }
}
