package com.liguanghong.gdqylatitude.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liguanghong.gdqylatitude.adapter.ChatAdapter;
import com.liguanghong.gdqylatitude.manager.ConversationManager;
import com.liguanghong.gdqylatitude.manager.WebSocketManager;
import com.liguanghong.gdqylatitude.unity.ChatMsg;
import com.liguanghong.gdqylatitude.unity.Friend;
import com.liguanghong.gdqylatitude.unity.Groupchat;
import com.liguanghong.gdqylatitude.unity.MessageType;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.util.DensityUtil;
import com.liguanghong.gdqylatitude.util.ImageUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;


public class ChatActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener {

    private ImageView backtrack_friend_chat;                    //返回通讯录
    private TextView tv_friendName;                             //好友名称
    private TextView tv_friendState;                            //好友状态
    private TextView tv_data;                                   //好友详情
    private TextView tv_send;                                   //发送消息
    private TextView hint_tv_pic;                               //调用相册
    private TextView hint_tv_phone;                             //调用相机
    private TextView hint_tv_location;                          //获取定位信息
    private RelativeLayout add;                                 //添加图片，位置
    private RelativeLayout contentPanel_bottomPanel_hint;     //底部弹出栏
    private EditText ed_content;                                //要发送的文字内容
    private ListView message_listView;
    private ChatAdapter chatAdapter;
    private static Handler chatHandler;

    private Friend friend;                                   //好友
    private Groupchat groupchat;                            //群聊
    private boolean isSingle = true;                       //私聊

    private boolean isExpanded = true;                  //判断底部弹出栏的状态
    private ViewGroup.LayoutParams pp;                     //用于改变信息发送栏的位置

    private InputMethodManager inputMethodManager;      //用于隐藏软键盘
    private TranslateAnimation mShowAction;             //补间动画，用于底部弹出栏

    private final int CAMERA_REQUEST = 1;

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
        backtrack_friend_chat = findViewById(R.id.backtrack);
        tv_friendName = findViewById(R.id.tv_friendName);
        tv_friendState = findViewById(R.id.tv_friendState);
        tv_data = findViewById(R.id.tv_data);
        tv_send = findViewById(R.id.tv_send);
        hint_tv_pic = findViewById(R.id.hint_tv_pic);
        hint_tv_phone = findViewById(R.id.hint_tv_phone);
        hint_tv_location = findViewById(R.id.hint_tv_location);
        add = findViewById(R.id.add);
        contentPanel_bottomPanel_hint = findViewById(R.id.contentPanel_bottomPanel_hint);
        ed_content = findViewById(R.id.ed_content);
        message_listView = findViewById(R.id.message_listView);


        inputMethodManager = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);

        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(300);
        ed_content.setFocusable(true);
        ed_content.setFocusableInTouchMode(true);
        ed_content.requestFocus();

        backtrack_friend_chat.setOnClickListener(this);
        tv_data.setOnClickListener(this);
        tv_send.setOnClickListener(this);
        add.setOnClickListener(this);
        ed_content.setOnClickListener(this);
        hint_tv_pic.setOnClickListener(this);
        hint_tv_phone.setOnClickListener(this);
        hint_tv_location.setOnClickListener(this);
        message_listView.setOnTouchListener(this);

    }

    @Override
    protected void initData() {

        chatHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 222:
                        if(chatAdapter != null) {
                            chatAdapter.notifyDataSetChanged();
                        }
                        break;
                }
            }
        };

        friend = (Friend)this.getIntent().getSerializableExtra("userinfo");
        if(friend != null){
            //私聊
            String remark = friend.getRemark();
            if(remark == null)
                tv_friendName.setText(friend.getFriend().getLogname());
            else
                tv_friendName.setText(friend.getRemark() + " (" + friend.getFriend().getLogname() + ")");

            if(friend.getFriend().getStatu().equals(2)){
                tv_friendState.setText("在线");
            }else{
                tv_friendState.setText("离线");
            }
            chatAdapter = new ChatAdapter(this, friend.getFriend().getUserid(), isSingle);
        } else{
            //群聊
            isSingle = false;
            groupchat = (Groupchat)this.getIntent().getSerializableExtra("groupinfo");
            tv_friendName.setText(groupchat.getGroupname());
            tv_friendState.setText(groupchat.getGroupnum()+"人");
            chatAdapter = new ChatAdapter(this, groupchat.getGroupid(), isSingle);
        }


        message_listView.setAdapter(chatAdapter);
        message_listView.setSelection(chatAdapter.getCount() > 0 ? chatAdapter.getCount() - 1 : 0);
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
                if(!isSingle){
                    Intent intent = new Intent(this, GroupInfoActivity.class);
                    intent.putExtra("groupinfo", groupchat);
                    startActivity(intent);
                } else{
                    Intent intent = new Intent(this, UserInfoActivity.class);
                    intent.putExtra("userinfo", friend);
                    startActivity(intent);
                }
                break;

            case R.id.tv_send:                          //发送文本消息
                String text = ed_content.getText().toString();
                if(text != null && !text.trim().equals("")) {
                    sendText(text);
                    ed_content.setText(null);
                }
                break;

            case R.id.add:                          //添加图片，地理位置
                inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);//隐藏软键盘

                if (isExpanded) {
                    openRoot();
                } else {
                    closeRoot();
                }
                break;

            case R.id.ed_content:                   //编辑框
                closeRoot();
                break;

            case R.id.hint_tv_pic:                  //调用相册
                Intent photo=new Intent(ChatActivity.this,SelectPhotoActivity.class);
                startActivityForResult(photo,10);
                break;

            case R.id.hint_tv_phone:                //调用相机
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                break;

            case R.id.hint_tv_location:             //获取位置信息

                break;

            case R.id.backtrack:            //返回通讯录
                inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);//隐藏软键盘
                closeRoot();
                finish();
                break;
        }
    }
    //打开底部弹出框
    private void openRoot(){
        contentPanel_bottomPanel_hint.startAnimation(mShowAction);
        contentPanel_bottomPanel_hint.setVisibility(View.VISIBLE);
        isExpanded = false;
    }

    //关闭底部弹出框
    private void closeRoot(){

        contentPanel_bottomPanel_hint.setVisibility(View.GONE);
        isExpanded = true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);//隐藏软键盘
        closeRoot();
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            sendImg(ImageUtils.bitmapToString(photo));
        } else if (requestCode == 10 && resultCode == 20) {
            String photo = (String) data.getExtras().get("photo");
            sendImg(ImageUtils.filePathToString(photo));
        }
    }

    /**
     * 发送文本消息
     * @param text
     */
    private void sendText(String text){
        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setSenderid(UserManager.getAppUser().getUserid());
        if(!isSingle)
            chatMsg.setReceiverid(groupchat.getGroupid());
        else
            chatMsg.setReceiverid(friend.getFriend().getUserid());
        chatMsg.setIssingle(isSingle);
        chatMsg.setType(MessageType.TEXT);
        chatMsg.setData(text.getBytes(Charset.forName("UTF-8")));
        WebSocketManager.sendMsg(chatMsg);
        ConversationManager.sendMsg(chatMsg);
    }

    /**
     * 发送图片消息
     * @param photoString
     */
    private void sendImg(String photoString){
        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setSenderid(UserManager.getAppUser().getUserid());
        if(!isSingle)
            chatMsg.setReceiverid(groupchat.getGroupid());
        else
            chatMsg.setReceiverid(friend.getFriend().getUserid());
        chatMsg.setIssingle(isSingle);
        chatMsg.setType(MessageType.IMAGE);
        chatMsg.setData(photoString.getBytes(Charset.forName("UTF-8")));
        WebSocketManager.sendMsg(chatMsg);
        ConversationManager.sendMsg(chatMsg);
    }

}
