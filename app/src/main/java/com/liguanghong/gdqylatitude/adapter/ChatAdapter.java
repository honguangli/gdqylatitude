package com.liguanghong.gdqylatitude.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.activitys.SendLocationActivity;
import com.liguanghong.gdqylatitude.manager.ConversationManager;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.unity.ChatMsg;
import com.liguanghong.gdqylatitude.unity.MessageType;
import com.liguanghong.gdqylatitude.util.DensityUtil;
import com.liguanghong.gdqylatitude.view.ChatImageView;
import com.liguanghong.gdqylatitude.view.ChatTextView;

import java.io.UnsupportedEncodingException;

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
        return ConversationManager.getMsgList(friendID, isSingle).size();
    }

    @Override
    public Object getItem(int i) {
        return ConversationManager.getMsgList(friendID, isSingle).get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_chat_text,null);

        ChatMsg chatMsg = (ChatMsg)getItem(i);
        if(chatMsg.getSenderid().equals(UserManager.getAppUser().getUserid())){
            //自己发的消息
            v.findViewById(R.id.right_chat).setVisibility(View.VISIBLE);
            CircleImageView iv = v.findViewById(R.id.right_headphoto);
            iv.setImageResource(R.drawable.dynamic_background);
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
            iv.setImageResource(R.drawable.dynamic_background);
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
            }
        }





        return v;
    }

//    private void initView(int i, View view, ViewGroup viewGroup{
//        RelativeLayout v = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.item_chat_text,null);
//
//        CircleImageView img_headphoto = v.findViewById(R.id.img_headphoto);
//        ChatImageView tv = v.findViewById(R.id.tv_message);
//        img_headphoto.setImageResource(R.drawable.dynamic_background);
//
//        RelativeLayout.LayoutParams imglayoutParams = new RelativeLayout.LayoutParams(DensityUtil.dp2px(context, 36f), DensityUtil.dp2px(context, 36f));
//        RelativeLayout.LayoutParams tvlayoutParms = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        if(((ChatMsg)getItem(i)).getSenderid().equals(UserManager.getAppUser().getUserid())){
//            //用户本人发的消息
//            imglayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            imglayoutParams.setMargins(0, 0, DensityUtil.dp2px(context, 10f), 0);
//            img_headphoto.setLayoutParams(imglayoutParams);
//
//            tvlayoutParms.addRule(RelativeLayout.LEFT_OF, R.id.img_headphoto);
//            tvlayoutParms.setMargins(DensityUtil.dp2px(context, 40f), 0, 0, 0);
//            tv.setLayoutParams(tvlayoutParms);
//
//            tv.setPadding(DensityUtil.dp2px(context, 8), DensityUtil.dp2px(context, 8),
//                    DensityUtil.dp2px(context, 20), DensityUtil.dp2px(context, 8));
//
//            tv.setmCornerPosition(ChatTextView.CORNER_RIGHT);
//
//
//        }else {
//            //好友的消息
//            imglayoutParams.setMargins(DensityUtil.dp2px(context, 10f), 0, 0, 0);
//            imglayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//            img_headphoto.setLayoutParams(imglayoutParams);
//
//            tvlayoutParms.addRule(RelativeLayout.RIGHT_OF, R.id.img_headphoto);
//            tvlayoutParms.setMargins(0, 0, DensityUtil.dp2px(context, 40f), 0);
//            tv.setLayoutParams(tvlayoutParms);
//
//            tv.setPadding(DensityUtil.dp2px(context, 20), DensityUtil.dp2px(context, 8),
//                    DensityUtil.dp2px(context, 8), DensityUtil.dp2px(context, 8));
//
//            tv.setmCornerPosition(ChatTextView.CORNER_LEFT);
//        }
//
//        try {
//            String  str = new String(((ChatMsg)getItem(i)).getData(), "utf-8");
//            byte[] bytes = Base64.decode(str, Base64.DEFAULT);
//            tv.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }

}
