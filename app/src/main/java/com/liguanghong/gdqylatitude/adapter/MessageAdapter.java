package com.liguanghong.gdqylatitude.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.unity.Chatmessage;
import com.liguanghong.gdqylatitude.util.ConversationManager;
import com.liguanghong.gdqylatitude.util.FriendsManager;
import com.liguanghong.gdqylatitude.util.UserManager;

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
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_message,null);

        ImageView img_headphoto = (ImageView)v.findViewById(R.id.img_headphoto);
        TextView tv_nickname = (TextView) v.findViewById(R.id.tv_nickname);
        TextView tv_message = (TextView) v.findViewById(R.id.tv_message);
        TextView tv_time = (TextView) v.findViewById(R.id.tv_time);
        TextView tv_message_number = (TextView) v.findViewById(R.id.tv_message_number);
        img_headphoto.setImageResource(R.drawable.ic_launcher_background);

        List<Chatmessage> chatmessageList = (List<Chatmessage>)getItem(i);

        try {
            tv_nickname.setText(FriendsManager.getFriendByID(chatmessageList.get(chatmessageList.size() - 1).getSenderid()).getLogname());
            tv_message.setText(new String(chatmessageList.get(chatmessageList.size() - 1).getData(), "utf-8"));
            SimpleDateFormat dateformat=new SimpleDateFormat("MM-dd HH:mm");
            String a2 =dateformat.format(chatmessageList.get(chatmessageList.size() - 1).getSendtime());
            tv_time.setText(a2);
            tv_message_number.setText(chatmessageList.size() + "");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return v;
    }

}
