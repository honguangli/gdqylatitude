package com.liguanghong.gdqylatitude.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.manager.FriendsManager;

public class DialogFriendsSetAdapter extends BaseAdapter {

    private Context context;
    private int userid;

    public DialogFriendsSetAdapter(Context context, int userid){
        this.context = context;
        this.userid = userid;

    }
    @Override
    public int getCount() {
        return FriendsManager.getInstance().getFriendsSetNameList().size();
    }

    @Override
    public Object getItem(int i) {
        return FriendsManager.getInstance().getFriendsSetNameList().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_dialod_friends_set_change,null);
        TextView item_friends_set_name = v.findViewById(R.id.item_friends_set_name);
        item_friends_set_name.setText(FriendsManager.getInstance().getFriendsSetNameList().get(i));
        ImageView item_friends_set_check = v.findViewById(R.id.item_friends_set_check);
        if(i == FriendsManager.getInstance().getFriendsSetIndexByID(userid)){
            item_friends_set_check.setImageResource(R.drawable.check);
        }

        return v;
    }
}
