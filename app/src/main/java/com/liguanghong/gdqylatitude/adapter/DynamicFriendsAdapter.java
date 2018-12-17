package com.liguanghong.gdqylatitude.adapter;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.unity.Dynamic;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.util.ImageUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DynamicFriendsAdapter extends BaseAdapter {

    private List<Dynamic> list;
    private List<User> userList;
    private Context context;

    private ImageView img_headphoto3;               //头像
    private ImageView img_photo1;                   //已发送的图片1
    private ImageView img_photo2;                   //已发送的图片2
    private ImageView img_photo3;                   //已发送的图片3
    private ImageView img_praise2;                  //点赞
    private ImageView img_comment;                  //评论
    private ImageView img_share2;                   //转发
    private TextView item_name;                     //用户名
    private TextView tv_content2;                   //发送的内容
    private TextView tv_time;

    public DynamicFriendsAdapter(Context context, List<Dynamic> list,List<User> userList){
        this.context = context;
        this.list = list;
        this.userList=userList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_dynamic_friends,null);

        img_headphoto3 = v.findViewById(R.id.img_headphoto);
        img_photo1 = v.findViewById(R.id.img_photo1);
        img_photo2 = v.findViewById(R.id.img_photo2);
        img_photo3 = v.findViewById(R.id.img_photo3);
        img_praise2 = v.findViewById(R.id.img_praise);
        img_comment = v.findViewById(R.id.img_comment);
        img_share2 = v.findViewById(R.id.img_share);
        item_name = v.findViewById(R.id.tv_friendName);
        tv_content2 = v.findViewById(R.id.tv_content);
        tv_time = v.findViewById(R.id.tv_time);

        try{
            byte[] pic = Base64.decode(list.get(i).getPic(), Base64.DEFAULT);
            if (!pic.equals("")){
                img_photo1.setImageBitmap(ImageUtils.getPicFromBytes(pic,null));
            }
        }catch (NullPointerException e){
        }
        try{
            byte[] pic2 = Base64.decode(list.get(i).getPic2(), Base64.DEFAULT);
            if (!pic2.equals("")){
                img_photo2.setImageBitmap(ImageUtils.getPicFromBytes(pic2,null));
            }
        }catch (NullPointerException e){
        }
        try{
            byte[] pic3 = Base64.decode(list.get(i).getPic3(), Base64.DEFAULT);
            if (!pic3.equals("")){
                img_photo3.setImageBitmap(ImageUtils.getPicFromBytes(pic3,null));
            }
        }catch (NullPointerException e){
        }
        byte[]  head= Base64.decode(userList.get(i).getHeadportrait(), Base64.DEFAULT);
        img_headphoto3.setImageBitmap(ImageUtils.getPicFromBytes(head,null));
        item_name.setText(userList.get(i).getLogname());
        tv_content2.setText(list.get(i).getText());
        SimpleDateFormat dateformat=new SimpleDateFormat("MM-dd HH:mm");
        tv_time.setText(dateformat.format(list.get(i).getPostedtime()));

        return v;
    }
}

