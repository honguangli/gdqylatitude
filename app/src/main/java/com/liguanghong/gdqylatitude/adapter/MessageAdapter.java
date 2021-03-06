package com.liguanghong.gdqylatitude.adapter;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.activitys.ChatActivity;
import com.liguanghong.gdqylatitude.manager.GroupManager;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.unity.ChatMsg;
import com.liguanghong.gdqylatitude.manager.ConversationManager;
import com.liguanghong.gdqylatitude.manager.FriendsManager;
import com.liguanghong.gdqylatitude.unity.MessageType;
import com.liguanghong.gdqylatitude.util.ImageUtils;

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
        return ConversationManager.getInstance().getMsgMap().size();
    }

    @Override
    public Object getItem(int i) {
        return ConversationManager.getInstance().getMsgListByIndex(i);
    }

    @Override
    public long getItemId(int i) {
        return ConversationManager.getInstance().getMsgKeyByIndex(i);
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
                byte[] group = Base64.decode(GroupManager.getInstance().getGroupByID(chatMsg.getReceiverid()).getHeadportrait(), Base64.DEFAULT);
                img_headphoto.setImageBitmap(ImageUtils.getPicFromBytes(group,null));
                tv_nickname.setText(GroupManager.getInstance().getGroupByID(chatMsg.getReceiverid()).getGroupname());
            } else{
                //私聊
                if(chatMsg.getSenderid().equals(UserManager.getInstance().getAppUser().getUserid())){
                    //用户本人先发的消息
                    byte[] friend = Base64.decode(FriendsManager.getInstance().getFriendByID(chatMsg.getReceiverid()).getFriend().getHeadportrait(), Base64.DEFAULT);
                    img_headphoto.setImageBitmap(ImageUtils.getPicFromBytes(friend,null));
                    tv_nickname.setText(FriendsManager.getInstance().getFriendByID(chatMsg.getReceiverid()).getFriend().getLogname());
                } else{
                    //好友先发的消息
                    byte[] friend = Base64.decode(FriendsManager.getInstance().getFriendByID(chatMsg.getSenderid()).getFriend().getHeadportrait(), Base64.DEFAULT);
                    img_headphoto.setImageBitmap(ImageUtils.getPicFromBytes(friend,null));
                    tv_nickname.setText(FriendsManager.getInstance().getFriendByID(chatMsg.getSenderid()).getFriend().getLogname());
                }
            }
            if(chatMsg.getType().equals(MessageType.TEXT))
                tv_message.setText(new String(chatMsg.getData(), "utf-8"));
            else if(chatMsg.getType().equals(MessageType.IMAGE))
                tv_message.setText("[图片]");
            else if(chatMsg.getType().equals(MessageType.LOCATION))
                tv_message.setText("[位置]");
            SimpleDateFormat dateformat=new SimpleDateFormat("MM-dd HH:mm");
            tv_time.setText(dateformat.format(chatMsg.getSendtime()));
            //tv_message_number.setText(list.size() + "");
            tv_message_number.setVisibility(View.GONE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        return v;
    }

}
