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

public class MessageAdapter extends BaseAdapter {

    private ArrayList<String> list;
    Context context;

    public ImageView img_headphoto;
    public TextView tv_nickname,tv_message,tv_time,tv_message_number;


    public static String name;

    public MessageAdapter(Context context,ArrayList<String> list){
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

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_item_message,null);
            img_headphoto = (ImageView)view.findViewById(R.id.img_headphoto);
            tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
            tv_message = (TextView) view.findViewById(R.id.tv_message);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_message_number = (TextView) view.findViewById(R.id.tv_message_number);


        }
        name = tv_nickname.toString();



        return view;
    }
}
