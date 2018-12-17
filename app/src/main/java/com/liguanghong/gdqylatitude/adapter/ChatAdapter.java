package com.liguanghong.gdqylatitude.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.activitys.ChatActivity;
import com.liguanghong.gdqylatitude.activitys.SendLocationActivity;
import com.liguanghong.gdqylatitude.manager.ConversationManager;
import com.liguanghong.gdqylatitude.manager.FriendsManager;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.unity.ChatMsg;
import com.liguanghong.gdqylatitude.unity.MessageType;
import com.liguanghong.gdqylatitude.util.ImageUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends BaseAdapter {

    private Context context;
    private Integer friendID;
    private boolean isSingle;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public ChatAdapter(Context context, Integer friendID, boolean isSingle){
        this.context = context;
        this.friendID = friendID;
        this.isSingle = isSingle;

        sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE); //获取editor对象
        editor = sharedPreferences.edit();//获取编辑器
    }
    @Override
    public int getCount() {
        List<ChatMsg> list = ConversationManager.getInstance().getMsgList(friendID, isSingle);
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return ConversationManager.getInstance().getMsgList(friendID, isSingle).get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_chat_text,null);

        ChatMsg chatMsg = (ChatMsg)getItem(i);
        if(chatMsg.getSenderid().equals(UserManager.getInstance().getAppUser().getUserid())){
            //自己发的消息
            v.findViewById(R.id.right_chat).setVisibility(View.VISIBLE);
            CircleImageView iv = v.findViewById(R.id.right_headphoto);
            byte[] my = android.util.Base64.decode(UserManager.getInstance().getAppUser().getHeadportrait(), android.util.Base64.DEFAULT);
            iv.setImageBitmap(ImageUtils.getPicFromBytes(my,null));
            if(chatMsg.getType().equals(MessageType.TEXT)){
                //文本消息
                v.findViewById(R.id.right_layout).setVisibility(View.VISIBLE);
                TextView tv = v.findViewById(R.id.right_msg);
                tv.setText(new String(chatMsg.getData()));
            } else if(chatMsg.getType().equals(MessageType.IMAGE)){
                //图片消息
                ImageView imgV = v.findViewById(R.id.right_img);
                imgV.setVisibility(View.VISIBLE);
                String str = new String(chatMsg.getData());
                byte[] bytes = Base64.decode(str, Base64.DEFAULT);
                imgV.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }else if(chatMsg.getType().equals(MessageType.LOCATION)){
                //位置消息
                ImageView imgV = v.findViewById(R.id.right_img);
                imgV.setVisibility(View.VISIBLE);
                String str = new String(chatMsg.getData());
                byte[] bytes = Base64.decode(str, Base64.DEFAULT);
                imgV.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));

                //点击位置截图进入地图，查看对方和自己的位置
                imgV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SendLocationActivity.locationtype = true;
                        editor.putBoolean("locationtype",SendLocationActivity.locationtype);
                        System.out.println("222222222222"+SendLocationActivity.locationtype);
                        editor.commit();
                        Intent location = new Intent(context,SendLocationActivity.class);
                        context.startActivity(location);
                    }
                });

            }


        } else{
            //对方发的消息
            v.findViewById(R.id.left_chat).setVisibility(View.VISIBLE);
            CircleImageView iv = v.findViewById(R.id.left_headphoto);
            byte[] friend = android.util.Base64.decode(FriendsManager.getInstance().getFriendByID(chatMsg.getSenderid()).getFriend().getHeadportrait(), android.util.Base64.DEFAULT);
            iv.setImageBitmap(ImageUtils.getPicFromBytes(friend,null));
            if(chatMsg.getType().equals(MessageType.TEXT)){
                //文本消息
                v.findViewById(R.id.left_layout).setVisibility(View.VISIBLE);
                TextView tv = v.findViewById(R.id.left_msg);
                tv.setText(new String(chatMsg.getData()));
            } else if(chatMsg.getType().equals(MessageType.IMAGE)){
                //图片消息
                ImageView imgV = v.findViewById(R.id.left_img);
                imgV.setVisibility(View.VISIBLE);
                String str = new String(chatMsg.getData());
                byte[] bytes = Base64.decode(str, Base64.DEFAULT);
                imgV.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }else if(chatMsg.getType().equals(MessageType.LOCATION)){
                //位置消息
                ImageView imgV = v.findViewById(R.id.left_img);
                imgV.setVisibility(View.VISIBLE);
                String str = new String(chatMsg.getData());
                byte[] bytes = Base64.decode(str, Base64.DEFAULT);
                imgV.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));

                //点击位置截图进入地图，查看对方和自己的位置
                imgV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SendLocationActivity.locationtype = true;
                        editor.putBoolean("locationtype",SendLocationActivity.locationtype);
                        editor.commit();
                        Intent location = new Intent(context,SendLocationActivity.class);
                        context.startActivity(location);
                    }
                });
            }
        }





        return v;
    }

}
