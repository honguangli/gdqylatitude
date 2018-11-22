package com.liguanghong.gdqylatitude.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.manager.GroupManager;

public class GroupAdapter extends BaseAdapter {

    Context context;

    public GroupAdapter(Context context){
        this.context = context;
    }
    @Override
    public int getCount() {
        return GroupManager.getInstance().getGroupchatList().size();
    }

    @Override
    public Object getItem(int i) {
        return GroupManager.getInstance().getGroupchatList().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_group,null);

        ImageView img_chat_head = v.findViewById(R.id.group_head);
        TextView chat_name = v.findViewById(R.id.group_name);
        chat_name.setText(GroupManager.getInstance().getGroupchatList().get(i).getGroupname());

        return v;
    }
}

