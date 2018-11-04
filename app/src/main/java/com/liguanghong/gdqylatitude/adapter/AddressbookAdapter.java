package com.liguanghong.gdqylatitude.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.manager.FriendsManager;

import java.util.List;

public class AddressbookAdapter extends BaseExpandableListAdapter {
    private List<String> parentList;
    private Context context;

    //构造函数
    public AddressbookAdapter(Context context, List<String> parentList) {
        this.context = context;
        this.parentList = parentList;
    }

    //获取分组数
    @Override
    public int getGroupCount() {
        return parentList.size();
    }
    //获取当前组的子项数
    @Override
    public int getChildrenCount(int groupPosition) {
        String groupName = parentList.get(groupPosition);
        int childCount = FriendsManager.getFriends().get(groupName).size();
        return childCount;
    }
    //获取当前组对象
    @Override
    public Object getGroup(int groupPosition) {
        String groupName = parentList.get(groupPosition);
        return groupName;
    }
    //获取当前子项对象
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String groupName = parentList.get(groupPosition);
        String chidlName = FriendsManager.getFriends().get(groupName).get(childPosition).getLogname();
        return chidlName;
    }
    //获取组ID
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    //获取子项ID
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
    //组视图初始化
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final int groupPos = groupPosition;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_friends_set_parent, null);
        }

        ImageView image = (ImageView) convertView.findViewById(R.id.image_parent);

        if(isExpanded){
            image.setImageResource(R.drawable.todown_gray);
        }else{
            image.setImageResource(R.drawable.toright_gray);
        }


        TextView parentText = (TextView) convertView.findViewById(R.id.text_parent);
        parentText.setText(parentList.get(groupPosition));
        return convertView;
    }
    //子项视图初始化
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final int groupPos = groupPosition;
        final int childPos = childPosition;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_friends_set_child, null);
        }
        TextView childText = (TextView) convertView.findViewById(R.id.text_child);
        TextView state = (TextView) convertView.findViewById(R.id.state);
        ImageView image_head = (ImageView) convertView.findViewById(R.id.image_head);
        ImageView img_state = (ImageView) convertView.findViewById(R.id.img_state);
        String parentName = parentList.get(groupPosition);
        String childName = FriendsManager.getFriends().get(parentName).get(childPosition).getLogname();
        childText.setText(childName);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

