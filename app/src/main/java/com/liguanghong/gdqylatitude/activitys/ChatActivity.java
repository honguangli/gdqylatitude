package com.liguanghong.gdqylatitude.activitys;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liguanghong.gdqylatitude.adapter.ChatAdapter;
import com.liguanghong.gdqylatitude.fragment.MessageFragment;
import com.liguanghong.gdqylatitude.manager.ConversationManager;
import com.liguanghong.gdqylatitude.unity.Chatmessage;
import com.liguanghong.gdqylatitude.unity.MessageType;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.UserManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;

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
    private RelativeLayout contentPanel_bottomPanel;          //信息发送栏
    private RelativeLayout contentPanel_bottomPanel_hint;     //底部弹出栏
    private EditText ed_content;                                //要发送的文字内容
    private ListView message_listView;
    private ChatAdapter chatAdapter;
    private static Handler chatHandler;

    private User friend;                                   //好友ID

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
        backtrack_friend_chat = (ImageView)findViewById(R.id.backtrack);
        tv_friendName = (TextView) findViewById(R.id.tv_friendName);
        tv_friendState = (TextView) findViewById(R.id.tv_friendState);
        tv_data = (TextView) findViewById(R.id.tv_data);
        tv_send = (TextView) findViewById(R.id.tv_send);
        hint_tv_pic = findViewById(R.id.hint_tv_pic);
        hint_tv_phone = findViewById(R.id.hint_tv_phone);
        hint_tv_location = findViewById(R.id.hint_tv_location);
        add = (RelativeLayout)findViewById(R.id.add);
        contentPanel_bottomPanel = findViewById(R.id.contentPanel_bottomPanel);
        contentPanel_bottomPanel_hint = findViewById(R.id.contentPanel_bottomPanel_hint);
        ed_content = (EditText) findViewById(R.id.ed_content);
        message_listView = (ListView) findViewById(R.id.message_listView);

        pp = contentPanel_bottomPanel.getLayoutParams();

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

//        aboutIntent();
    }

    @Override
    protected void initData() {

        chatHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 222:
                        if(chatAdapter != null) {
                            chatAdapter.notifyDataSetChanged();
                            message_listView.setSelection(chatAdapter.getCount()-1);
                        }
                        break;
                }
            }
        };

        friend = (User)this.getIntent().getSerializableExtra("userinfo");
        tv_friendName.setText(friend.getLogname());
        if(friend.getStatu().equals(2)){
            tv_friendState.setText("在线");
        }else{
            tv_friendState.setText("离线");
        }
        chatAdapter = new ChatAdapter(this, friend.getUserid());
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
                Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
                intent.putExtra("userinfo", friend);
                startActivity(intent);
                break;

            case R.id.tv_send:                          //发送消息
                UserManager.getSocketClientManager().sendMsg(true, MessageType.TEXT, ed_content.getText().toString(), friend.getUserid());
                ed_content.setText(null);
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
        pp.height = 800;
        contentPanel_bottomPanel.startAnimation(mShowAction);
        contentPanel_bottomPanel.setLayoutParams(pp);
        contentPanel_bottomPanel_hint.startAnimation(mShowAction);
        contentPanel_bottomPanel_hint.setVisibility(View.VISIBLE);
        isExpanded = false;
    }

    //关闭底部弹出框
    private void closeRoot(){
        pp.height = 120;
        contentPanel_bottomPanel.setLayoutParams(pp);
        contentPanel_bottomPanel_hint.setVisibility(View.INVISIBLE);
        isExpanded = true;
    }

    @Override
    //点击屏幕软键盘隐藏，底部栏隐藏
    public boolean onTouch(View view, MotionEvent motionEvent) {
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);//隐藏软键盘
        closeRoot();
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Toast.makeText(this,"图片已获取",Toast.LENGTH_LONG).show();
            //获取到的图片
            backtrack_friend_chat.setImageBitmap(photo);

        } else if (requestCode == 10 && resultCode == 20) {
           aboutIntent(data);

        }
    }

    //获取照片返回该界面
    private void aboutIntent(Intent intent) {
        String photo = (String) intent.getExtras().get("photo");
        //List<String> photoSelect = (List<String>) intent.getSerializableExtra("photo");
        if (photo!=null) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(photo);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bitmap  = BitmapFactory.decodeStream(fis);
            Log.e("mDatas",photo);
            backtrack_friend_chat.setImageBitmap(bitmap);
//            final SimpleAdapter mAdapter = new SimpleAdapter(this, photoSelect);
//            mListView.setAdapter(mAdapter);
//            mListView.setLayoutManager(new GridLayoutManager(this,3));
        }
    }
}
