package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backtrack_friend_info;                    //返回聊天界面
    private ImageView iv_information_icon;                      //好友头像

    private TextView tv_information_name;                       //好友名称
    private TextView tv_information_id;                         //好友id
    private TextView tv_information_markname;                   //好友备注
    private TextView tv_information_groupname;                  //好友所在分组
    private TextView tv_information_city;                       //地区
    private TextView tv_information_signature;                  //个性签名

    private Button bt_information_send;                         //发消息

    private RelativeLayout rly_beizhu,rly_fenzu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initFriendInfo();
    }

    private void initFriendInfo(){

        backtrack_friend_info = (ImageView)findViewById(R.id.backtrack);
        iv_information_icon = (ImageView)findViewById(R.id.iv_information_icon);

        tv_information_name = (TextView)findViewById(R.id.tv_information_name);
        tv_information_id = (TextView)findViewById(R.id.tv_information_id);
        tv_information_markname = (TextView)findViewById(R.id.tv_information_markname);
        tv_information_groupname = (TextView)findViewById(R.id.tv_information_groupname);
        tv_information_city = (TextView)findViewById(R.id.tv_information_city);
        tv_information_signature = (TextView)findViewById(R.id.tv_information_signature);

        bt_information_send = (Button) findViewById(R.id.bt_information_send);

        rly_beizhu = (RelativeLayout)findViewById(R.id.rly_beizhu);
        rly_fenzu = (RelativeLayout)findViewById(R.id.rly_fenzu);

        backtrack_friend_info.setOnClickListener(this);
        rly_beizhu.setOnClickListener(this);
        rly_fenzu.setOnClickListener(this);
        bt_information_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.rly_beizhu:              //修改备注
                break;

            case R.id.rly_fenzu:             //修改分组名
                Intent fenzu_move = new Intent(getApplicationContext(), FriendsSetChangeActivity.class);
                startActivityForResult(fenzu_move,1);
                break;

            case R.id.bt_information_send:                  //返回聊天界面
                setResult(10);
                finish();
                break;

            case R.id.backtrack:                //返回聊天界面
                setResult(10);
                finish();
                break;

        }
    }
}
