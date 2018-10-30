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

public class GroupAdapter extends BaseAdapter {
    private ArrayList<String> list;
    Context context;

    public ImageView img_chat_head;
    public TextView chat_name;

    public static String name;

    public GroupAdapter(Context context,ArrayList<String> list){
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
            view = LayoutInflater.from(context).inflate(R.layout.group_adapter,null);
            img_chat_head = (ImageView)view.findViewById(R.id.img_chat_head);
            chat_name = (TextView) view.findViewById(R.id.chat_name);

        }
        name = chat_name.toString();



        return view;
    }
}

