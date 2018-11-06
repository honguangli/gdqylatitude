package com.liguanghong.gdqylatitude.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;

import java.util.ArrayList;

public class DynamicFriendsAdapter extends BaseAdapter {

    private ArrayList<String> list;
    private Context context;

    private TextView item_name;

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
        item_name = v.findViewById(R.id.tv_friendName);
        item_name.setText(list.get(i));

        return v;
    }
}

