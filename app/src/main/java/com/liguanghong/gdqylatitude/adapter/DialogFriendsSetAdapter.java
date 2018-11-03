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

public class DialogFriendsSetAdapter extends BaseAdapter {
    private ArrayList<String> list;
    private Context context;

    private TextView item_friends_set_name;
    private ImageView item_friends_set_check;


    public DialogFriendsSetAdapter(Context context, ArrayList<String> list){
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
        View v = LayoutInflater.from(context).inflate(R.layout.item_dialod_friends_set_change,null);

        item_friends_set_name = v.findViewById(R.id.item_friends_set_name);
        item_friends_set_name.setText(list.get(i));
        item_friends_set_check = v.findViewById(R.id.item_friends_set_check);
        if(i == 3){
            item_friends_set_check.setImageResource(R.drawable.check);
        }

        return v;
    }
}
