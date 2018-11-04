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
import com.liguanghong.gdqylatitude.fragment.AddressbookFragment;
import com.liguanghong.gdqylatitude.unity.Chatmessage;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MessageAdapter extends BaseAdapter {

    private Context context;
    private Map<Integer, List<Chatmessage>> chatmessageMap;

    public MessageAdapter(Context context, Map<Integer, List<Chatmessage>> chatmessageMap){
        this.context = context;
        this.chatmessageMap = chatmessageMap;
    }
    @Override
    public int getCount() {
        return chatmessageMap.size();
    }

    @Override
    public Object getItem(int i) {
        int index = 0;
        for (Map.Entry<Integer, List<Chatmessage>> entry : chatmessageMap.entrySet()) {
            //System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
            if(index == i){
                return entry.getValue();
            }
            i++;
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
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
        if(chatmessageList != null){
            try {
                tv_nickname.setText(AddressbookFragment.getFriendInfoByID(chatmessageList.get(chatmessageList.size() - 1).getSenderid()).getLogname());
                tv_message.setText(new String(chatmessageList.get(chatmessageList.size() - 1).getData(), "utf-8"));
                SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
                String a2 =dateformat.format(chatmessageList.get(chatmessageList.size() - 1).getSendtime());
                tv_time.setText(a2);
                tv_message_number.setText(chatmessageList.size() + "");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else{
            Log.i("聊天", "消息列为空");
        }

        return v;
    }

}
