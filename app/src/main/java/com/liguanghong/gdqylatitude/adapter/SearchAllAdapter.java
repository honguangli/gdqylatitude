package com.liguanghong.gdqylatitude.adapter;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.unity.Groupchat;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.util.ImageUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAllAdapter<T> extends BaseAdapter {

    private Context context;
    private List<T> list;
    private boolean selectUser;

    public SearchAllAdapter(Context context, List<T> list, boolean selectUser){
        this.context = context;
        this.list = list;
        this.selectUser = selectUser;
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

        if(selectUser) {
            tv_friend_name.setText(((User) list.get(i)).getLogname());
            byte[] friendHead = Base64.decode(((User)list.get(i)).getHeadportrait(), Base64.DEFAULT);
            search_friend_icon.setImageBitmap(ImageUtils.getPicFromBytes(friendHead,null));
        } else{
            tv_friend_name.setText(((Groupchat)list.get(i)).getGroupname());
            byte[] groupHead = Base64.decode(((Groupchat)list.get(i)).getHeadportrait(), Base64.DEFAULT);
            search_friend_icon.setImageBitmap(ImageUtils.getPicFromBytes(groupHead,null));
        }
        return v;
    }

    public void setSelectUser(boolean selectUser){
        this.selectUser = selectUser;
    }

}
