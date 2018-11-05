package com.liguanghong.gdqylatitude.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.unity.Chatmessage;
import com.liguanghong.gdqylatitude.util.DensityUtil;
import com.liguanghong.gdqylatitude.view.ChatTextView;

import java.io.UnsupportedEncodingException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends BaseAdapter {

    private Context context;
    private List<Chatmessage> chatmessageList;

    public ChatAdapter(Context context, List<Chatmessage> chatmessageList){
        this.context = context;
        this.chatmessageList = chatmessageList;
    }
    @Override
    public int getCount() {
        return chatmessageList.size();
    }

    @Override
    public Object getItem(int i) {
        return chatmessageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LinearLayout v = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_chat_text,null);
        ChatTextView tv = v.findViewById(R.id.tv_message);
        if( i%2 == 1){
            v.setGravity(Gravity.RIGHT);
            tv.setPadding(DensityUtil.dp2px(context, 8), DensityUtil.dp2px(context, 8),
                    DensityUtil.dp2px(context, 8), DensityUtil.dp2px(context, 20));
            tv.setmCornerPosition(0);
        }else {
            v.setGravity(Gravity.LEFT);
            tv.setPadding(DensityUtil.dp2px(context, 20), DensityUtil.dp2px(context, 8),
                    DensityUtil.dp2px(context, 8), DensityUtil.dp2px(context, 8));
            tv.setmCornerPosition(1);
        }

        try {
            tv.setText(new String(chatmessageList.get(i).getData(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return v;
    }

}
