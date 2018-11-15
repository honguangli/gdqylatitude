package com.liguanghong.gdqylatitude.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.unity.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAllAdapter extends BaseAdapter {

    private Context context;
    private List<User> list;

    public SearchAllAdapter(Context context, List<User> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount(){
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

        View v = LayoutInflater.from(context).inflate(R.layout.item_search_all,null);
        CircleImageView search_friend_icon = v.findViewById(R.id.search_friend_icon);
        TextView tv_friend_name = v.findViewById(R.id.tv_friend_name);

        search_friend_icon.setImageResource(R.drawable.dynamic_background);
        tv_friend_name.setText(list.get(i).getLogname());

        return v;
    }
}
