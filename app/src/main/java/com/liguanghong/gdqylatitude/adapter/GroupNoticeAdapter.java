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

public class GroupNoticeAdapter extends BaseAdapter {
    private ArrayList<String> list;
    Context context;

    public ImageView iv_friend_icon;
    public TextView tv_friend_name;
    public TextView tv_friend_notice;
    public TextView tv_friend_argee;

    public static String group_apply_name;

    public GroupNoticeAdapter(Context context, ArrayList<String> list){
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
        View v = LayoutInflater.from(context).inflate(R.layout.item_group_notice,null);
        iv_friend_icon = v.findViewById(R.id.iv_friend_icon);
        tv_friend_name = v.findViewById(R.id.tv_friend_name);
        tv_friend_notice = v.findViewById(R.id.tv_friend_notice);
        tv_friend_argee = v.findViewById(R.id.tv_friend_argee);


        group_apply_name = tv_friend_name.toString();

        return v;
    }
}
