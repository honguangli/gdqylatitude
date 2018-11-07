package com.liguanghong.gdqylatitude.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.manager.FriendsManager;
import com.liguanghong.gdqylatitude.unity.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchMineAdapter extends BaseAdapter {

    private Context context;
    private List<User> list;

    public SearchMineAdapter(Context context, List<User> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount(){
        return list.size() > 3 ? 3 : list.size();
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

        View v = LayoutInflater.from(context).inflate(R.layout.item_search_mine,null);
        CircleImageView search_friend_icon = v.findViewById(R.id.search_friend_icon);
        TextView tv_friend_name = v.findViewById(R.id.tv_friend_name);
        TextView tv_friend_from = v.findViewById(R.id.tv_friend_from);

        search_friend_icon.setImageResource(R.drawable.dynamic_background);
        tv_friend_name.setText(list.get(i).getLogname());
        tv_friend_from.setText("来自分组："+FriendsManager.getFriendsSetNameByID(list.get(i).getUserid()));
        return v;
    }
}
