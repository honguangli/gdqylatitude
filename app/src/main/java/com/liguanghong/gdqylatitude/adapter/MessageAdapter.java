package com.liguanghong.gdqylatitude.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.unity.Chatmessage;
import com.liguanghong.gdqylatitude.manager.ConversationManager;
import com.liguanghong.gdqylatitude.manager.FriendsManager;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;

public class MessageAdapter extends BaseAdapter {

    private Context context;

    public MessageAdapter(Context context){
        this.context = context;
    }
    @Override
    public int getCount() {
        return ConversationManager.getChatmessageMap().size();
    }

    @Override
    public Object getItem(int i) {
        return ConversationManager.getChatmessageListByIndex(i);
    }

    @Override
    public long getItemId(int i) {
        return ConversationManager.getChatmessageNameByIndex(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_chat_message,null);

        ImageView img_headphoto = (ImageView)v.findViewById(R.id.img_headphoto);
        TextView tv_nickname = (TextView) v.findViewById(R.id.tv_nickname);
        TextView tv_message = (TextView) v.findViewById(R.id.tv_message);
        TextView tv_time = (TextView) v.findViewById(R.id.tv_time);
        TextView tv_message_number = (TextView) v.findViewById(R.id.tv_message_number);
        img_headphoto.setImageResource(R.drawable.ic_launcher_background);

        List<Chatmessage> chatmessageList = (List<Chatmessage>)getItem(i);

        Chatmessage chatmessage = chatmessageList.get(chatmessageList.size() - 1);
        if(!chatmessage.getIssingle()){
            //群聊

        } else{
            //私聊
            if(chatmessage.getSenderid().equals(UserManager.getAppUser().getUserid())){
                //用户本人先发的消息
                tv_nickname.setText(FriendsManager.getFriendByID(chatmessage.getReceiveruserid()).getLogname());
            } else{
                //好友先发的消息
                tv_nickname.setText(FriendsManager.getFriendByID(chatmessage.getSenderid()).getLogname());
            }
        }


        try {
            tv_message.setText(new String(chatmessage.getData(), "utf-8"));
            SimpleDateFormat dateformat=new SimpleDateFormat("MM-dd HH:mm");
            String a2 = dateformat.format(chatmessage.getSendtime());
            tv_time.setText(a2);
            tv_message_number.setText(chatmessageList.size() + "");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return v;
    }

}
