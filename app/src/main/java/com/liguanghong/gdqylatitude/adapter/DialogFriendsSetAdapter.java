package com.liguanghong.gdqylatitude.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.manager.FriendsManager;

import java.util.ArrayList;

public class DialogFriendsSetAdapter extends BaseAdapter {

    private Context context;
    private int userid;

    public DialogFriendsSetAdapter(Context context, int userid){
        this.context = context;
        this.userid = userid;

    }
    @Override
    public int getCount() {
        return FriendsManager.getFriendsSetNameList().size();
    }

    @Override
    public Object getItem(int i) {
        return FriendsManager.getFriendsSetNameList().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_dialod_friends_set_change,null);
        TextView item_friends_set_name = v.findViewById(R.id.item_friends_set_name);
        item_friends_set_name.setText(FriendsManager.getFriendsSetNameList().get(i));
        ImageView item_friends_set_check = v.findViewById(R.id.item_friends_set_check);
        Log.i("用户信息", "i="+i + " : index=" + FriendsManager.getFriendsSetIndex(userid));
        if(i == FriendsManager.getFriendsSetIndex(userid)){
            item_friends_set_check.setImageResource(R.drawable.check);
        }

        return v;
    }
}
