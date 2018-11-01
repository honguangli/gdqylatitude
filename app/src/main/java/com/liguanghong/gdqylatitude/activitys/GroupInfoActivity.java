package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;

public class GroupInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backtrack;                                //返回群聊界面
    private ImageView menu;                                     //菜单
    private TextView group_name;                             //群名称
    private TextView group_id;                                  //群id
    private TextView group_messges;                         //群公告
    private TextView invite;                                    //邀请
    private RelativeLayout rly_group_all;                       //查看所有成员
    private RelativeLayout rly_manage;                      //群管理


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        initGroup_info();
    }

    private void initGroup_info(){

        backtrack = (ImageView)findViewById(R.id.backtrack);
        menu = (ImageView)findViewById(R.id.menu);
        group_name = (TextView)findViewById(R.id.group_name);
        group_id = (TextView)findViewById(R.id.group_name);
        group_messges = (TextView)findViewById(R.id.group_name);
        //invite = (TextView)findViewById(R.id.group_name);
        rly_group_all = (RelativeLayout)findViewById(R.id.rly_group_all);
        rly_manage = (RelativeLayout)findViewById(R.id.rly_manage);

        backtrack.setOnClickListener(this);
        menu.setOnClickListener(this);
        //invite.setOnClickListener(this);
        rly_group_all.setOnClickListener(this);
        rly_manage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.rly_manage:           //群管理
                Intent group_manage = new Intent(getApplicationContext(), GroupManageActivity.class);
                startActivityForResult(group_manage,1);
                break;

            case R.id.rly_group_all:            //查看群成员

                break;

            case R.id.menu:                         //菜单

                break;
        /*
            case R.id.invite:                   //邀请

                break;
*/
            case R.id.backtrack:         //返回群聊
                setResult(10);
                finish();
                break;
        }
    }
}
