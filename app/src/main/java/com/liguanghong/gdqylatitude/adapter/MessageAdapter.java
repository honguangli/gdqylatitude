package com.liguanghong.gdqylatitude.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.unity.ChatMsg;
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
        return ConversationManager.getMsgMap().size();
    }

    @Override
    public Object getItem(int i) {
        return ConversationManager.getMsgListByIndex(i);
    }

    @Override
    public long getItemId(int i) {
        return ConversationManager.getMsgKeyByIndex(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_chat_message,null);

        ImageView img_headphoto = v.findViewById(R.id.img_headphoto);
        TextView tv_nickname = v.findViewById(R.id.tv_nickname);
        TextView tv_message =  v.findViewById(R.id.tv_message);
        TextView tv_time = v.findViewById(R.id.tv_time);
        TextView tv_message_number = v.findViewById(R.id.tv_message_number);

        try {
            img_headphoto.setImageResource(R.drawable.ic_launcher_background);
            List<ChatMsg> list = (List<ChatMsg>)getItem(i);

            ChatMsg chatMsg = list.get(list.size() - 1);
            if(!chatMsg.getIssingle()){
                //群聊

            } else{
                //私聊
                if(chatMsg.getSenderid().equals(UserManager.getAppUser().getUserid())){
                    //用户本人先发的消息
                    tv_nickname.setText(FriendsManager.getFriendByID(chatMsg.getReceiverid()).getLogname());
                } else{
                    //好友先发的消息
                    tv_nickname.setText(FriendsManager.getFriendByID(chatMsg.getSenderid()).getLogname());
                }
            }
            tv_message.setText(new String(chatMsg.getData(), "utf-8"));
            SimpleDateFormat dateformat=new SimpleDateFormat("MM-dd HH:mm");
            tv_time.setText(dateformat.format(chatMsg.getSendtime()));
            tv_message_number.setText(list.size() + "");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        return v;
    }

}
