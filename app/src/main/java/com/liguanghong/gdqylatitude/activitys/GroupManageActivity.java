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
    private Button menu;                                    //完成
    private EditText et_group_name;                         //群组名
    private EditText et_group_messges;                      //群公告
    private RelativeLayout rly_gorup_member;                //群成员管理
    private TextView group_sign;                            //签到管理
    private TextView group_dissolve;                        //解散群组


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_manage);

        initGroup_manage();
    }


    private void initGroup_manage(){

        backtrack = (ImageView)findViewById(R.id.backtrack);
        menu = (Button)findViewById(R.id.menu);
        et_group_name = (EditText)findViewById(R.id.et_group_name);
        et_group_messges = (EditText)findViewById(R.id.et_group_messges);
        rly_gorup_member = (RelativeLayout)findViewById(R.id.rly_gorup_member);
        group_sign = (TextView)findViewById(R.id.group_sign);
        group_dissolve = (TextView)findViewById(R.id.group_dissolve);


        backtrack.setOnClickListener(this);
        menu.setOnClickListener(this);
        rly_gorup_member.setOnClickListener(this);
        group_sign.setOnClickListener(this);
        group_dissolve.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.group_dissolve:                   //解散群组

                break;

            case R.id.group_sign:                       //签到管理
                Intent group_sign_manage = new Intent(getApplicationContext(),GroupSignManageActivity.class);
                startActivityForResult(group_sign_manage,1);
                break;

            case R.id.rly_gorup_member:                 //群成员管理

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
