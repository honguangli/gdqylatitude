package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;

public class GroupManageActivity extends AppCompatActivity implements View.OnClickListener {

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
        setContentView(R.layout.activity_group_manage);

        initGroup_manage();
    }


    private void initGroup_manage(){

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
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.rl_group_dissolve:                   //解散群组

                break;

            case R.id.rl_group_sign_manage:                       //签到管理
                Intent group_sign_manage = new Intent(getApplicationContext(),GroupSignManageActivity.class);
                startActivityForResult(group_sign_manage,1);
                break;

            case R.id.rl_group_member_manage:                 //群成员管理

                break;

            case R.id.menu:                                 //完成

                break;

            case R.id.backtrack:                        //返回群信息界面
                setResult(10);
                finish();
                break;
        }
    }
}
