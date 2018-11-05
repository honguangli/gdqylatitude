package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.adapter.ChatAdapter;
import com.liguanghong.gdqylatitude.fragment.MessageFragment;
import com.liguanghong.gdqylatitude.manager.ConversationManager;
import com.liguanghong.gdqylatitude.unity.Chatmessage;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.UserManager;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class ChatActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backtrack_friend_chat;                //返回通讯录
    private TextView tv_friendName;                          //好友名称
    private TextView tv_friendState;                         //好友状态
    private TextView tv_data;                                 //好友详情
    private TextView tv_send;                                 //发送消息
    private RelativeLayout rly_add;                           //添加图片，位置
    private EditText ed_content;                              //要发送的文字内容
    private ListView message_listView;
    private ChatAdapter chatAdapter;
    private static Handler chatHandler;

    private User friend;                                   //好友ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView() {
        backtrack_friend_chat = (ImageView)findViewById(R.id.backtrack);
        tv_friendName = (TextView) findViewById(R.id.tv_friendName);
        tv_friendState = (TextView) findViewById(R.id.tv_friendState);
        tv_data = (TextView) findViewById(R.id.tv_data);
        tv_send = (TextView) findViewById(R.id.tv_send);
        rly_add = (RelativeLayout)findViewById(R.id.add);
        ed_content = (EditText) findViewById(R.id.ed_content);
        message_listView = (ListView) findViewById(R.id.message_listView);

        backtrack_friend_chat.setOnClickListener(this);
        tv_data.setOnClickListener(this);
        tv_send.setOnClickListener(this);
        rly_add.setOnClickListener(this);

    }

    @Override
    protected void initData() {

        chatHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 222:
                        if(chatAdapter != null)
                            chatAdapter.notifyDataSetChanged();
                        break;
                }
            }
        };

        friend = (User)this.getIntent().getSerializableExtra("friendInfo");;
        tv_friendName.setText(friend.getLogname());
        if(friend.getStatu() == 2){
            tv_friendState.setText("在线");
        }else{
            tv_friendState.setText("离线");
        }
        List<Chatmessage> chatmessageList = ConversationManager.getChatmessageListByName(friend.getUserid());
        chatAdapter = new ChatAdapter(this, chatmessageList);
        message_listView.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
    }

    public static Handler getChatHandler(){
        return chatHandler;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.tv_data:                          //跳转到好友详情界面
                startActivity(new Intent(getApplicationContext(), UserInfoActivity.class));
                break;

            case R.id.tv_send:                          //发送消息
                UserManager.getSocketClientManager().sendMsg(true, 0, ed_content.getText().toString(), friend.getUserid());
                break;

            case R.id.add:                          //添加图片，地理位置

                break;

            case R.id.backtrack:            //返回通讯录
                finish();
                break;
        }
    }

    private void addFriendMessage(String text){

    }

    private void addMineMessage(){

    }

}
