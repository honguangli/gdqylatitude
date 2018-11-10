package com.liguanghong.gdqylatitude.adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.manager.ConversationManager;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.unity.ChatMsg;
import com.liguanghong.gdqylatitude.util.DensityUtil;
import com.liguanghong.gdqylatitude.view.ChatTextView;

import java.io.UnsupportedEncodingException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends BaseAdapter {

    private Context context;
    private Integer friendID;

    public ChatAdapter(Context context, Integer friendID){
        this.context = context;
        this.friendID = friendID;
    }
    @Override
    public int getCount() {
        Log.i("聊天count", ConversationManager.getMsgMap().size()+":"+ConversationManager.getMsgList(friendID).size()+"" );
        return ConversationManager.getMsgList(friendID).size();
    }

    @Override
    public Object getItem(int i) {
        Log.i("聊天item", ConversationManager.getMsgList(friendID).get(i).getSenderid()+"" );
        return ConversationManager.getMsgList(friendID).get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        RelativeLayout v = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.item_chat_text,null);

        CircleImageView img_headphoto = v.findViewById(R.id.img_headphoto);
        ChatTextView tv = v.findViewById(R.id.tv_message);
        img_headphoto.setImageResource(R.drawable.dynamic_background);

        RelativeLayout.LayoutParams imglayoutParams = new RelativeLayout.LayoutParams(DensityUtil.dp2px(context, 36f), DensityUtil.dp2px(context, 36f));
        RelativeLayout.LayoutParams tvlayoutParms = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if(((ChatMsg)getItem(i)).getSenderid().equals(UserManager.getAppUser().getUserid())){
            //用户本人发的消息
            imglayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            imglayoutParams.setMargins(0, 0, DensityUtil.dp2px(context, 10f), 0);
            img_headphoto.setLayoutParams(imglayoutParams);

            tvlayoutParms.addRule(RelativeLayout.LEFT_OF, R.id.img_headphoto);
            tvlayoutParms.setMargins(DensityUtil.dp2px(context, 40f), 0, 0, 0);
            tv.setLayoutParams(tvlayoutParms);

            tv.setPadding(DensityUtil.dp2px(context, 8), DensityUtil.dp2px(context, 8),
                    DensityUtil.dp2px(context, 20), DensityUtil.dp2px(context, 8));
            tv.setGravity(Gravity.RIGHT);
            tv.setmCornerPosition(ChatTextView.CORNER_RIGHT);

        }else {
            //好友的消息
            imglayoutParams.setMargins(DensityUtil.dp2px(context, 10f), 0, 0, 0);
            imglayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            img_headphoto.setLayoutParams(imglayoutParams);

            tvlayoutParms.addRule(RelativeLayout.RIGHT_OF, R.id.img_headphoto);
            tvlayoutParms.setMargins(0, 0, DensityUtil.dp2px(context, 40f), 0);
            tv.setLayoutParams(tvlayoutParms);

            tv.setPadding(DensityUtil.dp2px(context, 20), DensityUtil.dp2px(context, 8),
                    DensityUtil.dp2px(context, 8), DensityUtil.dp2px(context, 8));
            tv.setGravity(Gravity.LEFT);
            tv.setmCornerPosition(ChatTextView.CORNER_LEFT);
        }

        try {
            tv.setText(new String(((ChatMsg)getItem(i)).getData(), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return v;
    }

}
