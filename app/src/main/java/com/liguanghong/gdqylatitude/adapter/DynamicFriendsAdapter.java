package com.liguanghong.gdqylatitude.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;

import java.util.ArrayList;

public class DynamicFriendsAdapter extends BaseAdapter {

    private ArrayList<String> list;
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

    public DynamicFriendsAdapter(Context context, ArrayList<String> list){
        this.context = context;
        this.list = list;
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
        item_name.setText(list.get(i));

        return v;
    }
}

