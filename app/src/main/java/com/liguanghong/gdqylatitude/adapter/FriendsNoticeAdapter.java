package com.liguanghong.gdqylatitude.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.manager.FriendsManager;
import com.liguanghong.gdqylatitude.manager.NoticeManager;
import com.liguanghong.gdqylatitude.unity.Chatmessage;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.JsonResult;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FriendsNoticeAdapter extends BaseAdapter {
    Context context;

    public FriendsNoticeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return NoticeManager.getFriendsNotice().size();
    }

    @Override
    public Object getItem(int i) {
        return NoticeManager.getFriendNoticeByIndex(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_friends_notice, null);

        ImageView iv_friend_icon = v.findViewById(R.id.iv_friend_icon);
        TextView tv_friend_name = v.findViewById(R.id.tv_friend_name);
        TextView tv_friend_notice = v.findViewById(R.id.tv_friend_notice);
        TextView tv_friend_argee = v.findViewById(R.id.tv_friend_argee);

        Map.Entry<User, Chatmessage> entry = (Map.Entry<User, Chatmessage>)getItem(i);
        User user = entry.getKey();
        Chatmessage chatmessage = entry.getValue();

        iv_friend_icon.setImageResource(R.drawable.dynamic_background);
        tv_friend_name.setText(user.getLogname());
        try {
            tv_friend_notice.setText(new String(chatmessage.getData(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(chatmessage.getType().equals(15)){
            tv_friend_argee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NoticeManager.changeFriendsNotice(i, 17);
                }
            });
        } else if(chatmessage.getType().equals(16)){
            tv_friend_argee.setBackground(null);
            tv_friend_argee.setText("已拒绝");
        } else{
            tv_friend_argee.setBackground(null);
            tv_friend_argee.setText("已同意");
        }

        return v;
    }
}