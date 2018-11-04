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
import java.util.Random;

public class MessageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> list;

    public MessageAdapter(Context context, ArrayList<String> list){
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
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_message,null);

        ImageView img_headphoto = (ImageView)v.findViewById(R.id.img_headphoto);
        TextView tv_nickname = (TextView) v.findViewById(R.id.tv_nickname);
        TextView tv_message = (TextView) v.findViewById(R.id.tv_message);
        TextView tv_time = (TextView) v.findViewById(R.id.tv_time);
        TextView tv_message_number = (TextView) v.findViewById(R.id.tv_message_number);
        img_headphoto.setImageResource(R.drawable.ic_launcher_background);
        tv_nickname.setText(list.get(i));
        tv_message.setText(list.get(i) + list.get(i));
        tv_time.setText(new Random().nextInt(25) + ":" + new Random().nextInt(60));
        tv_message_number.setText(""+new Random().nextInt(1000));

        return v;
    }

}
