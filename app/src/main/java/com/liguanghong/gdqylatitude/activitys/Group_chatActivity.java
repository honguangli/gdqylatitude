package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;

public class Group_chatActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backtrack_group_chat;                //返回通讯录
    private TextView tv_data_group;                                 //好友详情
    private TextView tv_send_group;                                 //发送消息
    private RelativeLayout rly_add_group;                           //添加图片，位置
    private EditText ed_content_group;                              //要发送的文字内容


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        initGroupChat();
    }

    private void initGroupChat(){

        backtrack_group_chat = (ImageView)findViewById(R.id.backtrack);
        tv_data_group = (TextView) findViewById(R.id.tv_data_group);
        tv_send_group = (TextView) findViewById(R.id.tv_send_group);
        rly_add_group = (RelativeLayout)findViewById(R.id.rly_add_group);
        ed_content_group = (EditText) findViewById(R.id.ed_content);

        backtrack_group_chat.setOnClickListener(this);
        tv_data_group.setOnClickListener(this);
        tv_send_group.setOnClickListener(this);
        rly_add_group.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.tv_data_group:                          //跳转到好友详情界面
                Intent group = new Intent(getApplicationContext(), Group_infoActivity.class);
                startActivityForResult(group,1);
                break;

            case R.id.tv_send_group:                          //发送消息

                break;

            case R.id.rly_add_group:                          //添加图片，地理位置

                break;

            case R.id.backtrack:            //返回通讯录
                setResult(10);
                finish();
                break;
        }
    }
}
