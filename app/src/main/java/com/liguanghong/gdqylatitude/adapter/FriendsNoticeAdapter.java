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

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.manager.NoticesManager;
import com.liguanghong.gdqylatitude.unity.ChatMsg;
import com.liguanghong.gdqylatitude.unity.NoticeMsg;
import com.liguanghong.gdqylatitude.unity.User;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class FriendsNoticeAdapter extends BaseAdapter {
    Context context;

    public FriendsNoticeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return NoticesManager.getInstance().getFriendNotices().size();
    }

    @Override
    public Object getItem(int i) {
        return NoticesManager.getInstance().getFriendNotices().get(i);
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

        NoticeMsg noticeMsg = (NoticeMsg)getItem(i);

        iv_friend_icon.setImageResource(R.drawable.dynamic_background);
        tv_friend_name.setText(noticeMsg.getSenderid() + "");
        if(noticeMsg.getNoticetype().equals(15)){
            tv_friend_argee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //NoticesManager.changeFriendsNotice(i, 17);
                }
            });
        } else if(noticeMsg.getNoticetype().equals(16)){
            tv_friend_argee.setBackground(null);
            tv_friend_argee.setText("已拒绝");
        } else{
            tv_friend_argee.setBackground(null);
            tv_friend_argee.setText("已同意");
        }

        return v;
    }
}