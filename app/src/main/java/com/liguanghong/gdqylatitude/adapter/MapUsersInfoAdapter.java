package com.liguanghong.gdqylatitude.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.util.ImageUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapUsersInfoAdapter extends BaseAdapter {
    private Context context;
    private static List<User> list;

    private TextView textView_name,textView_id;

    public MapUsersInfoAdapter(Context context,List<User> list){
        this.context = context;
        this.list=list;
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
        View v = LayoutInflater.from(context).inflate(R.layout.item_map_users_info,null);
        textView_name=v.findViewById(R.id.tv_map_user_name);
        textView_id=v.findViewById(R.id.tv_map_user_id);
        textView_name.setText(list.get(i).getLogname());
        CircleImageView map_user_icon = v.findViewById(R.id.map_user_icon);
        byte[] my = android.util.Base64.decode(list.get(i).getHeadportrait(), android.util.Base64.DEFAULT);
        map_user_icon.setImageBitmap(ImageUtils.getPicFromBytes(my,null));
        String ID=Integer.toString(list.get(i).getUserid());
        textView_id.setText("(ID:"+ID+")");
        return v;
    }
}
