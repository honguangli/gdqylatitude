package com.liguanghong.gdqylatitude.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.activitys.Fenzu_manageActivity;
import com.liguanghong.gdqylatitude.activitys.FriendApplyActivity;
import com.liguanghong.gdqylatitude.activitys.Friend_chatActivity;
import com.liguanghong.gdqylatitude.activitys.GroupActivity;
import com.liguanghong.gdqylatitude.activitys.SearchActivity;
import com.liguanghong.gdqylatitude.adapter.AddressbookAdapter;
import com.liguanghong.gdqylatitude.unity.QPopuWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressbookFragment extends Fragment implements View.OnClickListener, ExpandableListView.OnChildClickListener, AdapterView.OnItemLongClickListener {

    private View view;

    private EditText img_search;
    private ImageView add;
    private RelativeLayout rly_new_friends;
    private RelativeLayout rly_chat_group;

    private ExpandableListView expandableListView;
    public static AddressbookAdapter addressbookAdapter;

    int[] location = new  int[2] ;
    private int rawX;
    private int rawY;

    public static List<String> parentList;
    public static Map<String,List<String>> map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        if(view == null)
        {
            view = inflater.inflate(R.layout.fragment_addressbook, container, false);

            initView();
            initData();

        }
        else
        {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null){
                parent.removeView(view);
            }
        }

        return view;
    }

    //初始化组件
    private void initView()
    {

        add = (ImageView) view.findViewById(R.id.add);
        img_search = (EditText) view.findViewById(R.id.edittext_search);
        rly_new_friends = (RelativeLayout) view.findViewById(R.id.rly_new_friends);
        rly_chat_group = (RelativeLayout)view.findViewById(R.id.rly_chat_group);

        add.setOnClickListener(this);
        img_search.setOnClickListener(this);
        rly_new_friends.setOnClickListener(this);
        rly_chat_group.setOnClickListener(this);

        expandableListView = (ExpandableListView) view.findViewById(R.id.expandablelistview);
        //设置子项点击事件
        expandableListView.setOnChildClickListener(this);
        //设置长按点击事件
        expandableListView.setOnItemLongClickListener(this);

    }


    //初始化数据
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void initData(){

        //虚拟数据
        map = new HashMap<String, List<String>>();
        parentList = new ArrayList<String>();

        parentList = new ArrayList<String>();
        parentList.add("默认分组");
        parentList.add("我的好友");
        parentList.add("好友分组1");

        List<String> list1 = new ArrayList<String>();
        list1.add("好友1");
        list1.add("好友2");
        list1.add("好友3");
        map.put("默认分组", list1);

        List<String> list2 = new ArrayList<String>();
        list2.add("好友4");
        list2.add("好友5");
        list2.add("好友6");
        map.put("我的好友", list2);

        List<String> list3 = new ArrayList<String>();
        list3.add("好友7");
        list3.add("好友8");
        list3.add("好友9");
        map.put("好友分组1", list3);

        addressbookAdapter = new AddressbookAdapter(getActivity().getApplicationContext(), parentList, map);
        expandableListView.setAdapter(addressbookAdapter);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.add:

                break;

            case R.id.edittext_search:
                Toast.makeText(getActivity(),"搜索",Toast.LENGTH_LONG).show();
                Intent btn_search = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivityForResult(btn_search,1);
                break;

            case R.id.rly_new_friends:
                Toast.makeText(getActivity(),"新朋友",Toast.LENGTH_LONG).show();
                Intent friend_apply = new Intent(getActivity(), FriendApplyActivity.class);
                getActivity().startActivityForResult(friend_apply,1);
                break;

            case R.id.rly_chat_group:
                Toast.makeText(getActivity(),"群聊",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), GroupActivity.class);
                getActivity().startActivityForResult(intent,1);
                break;
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//        String str = "choose"+groupPosition+"-"+childPosition;
//        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
        Intent fenzu_manage = new Intent(getActivity(), Friend_chatActivity.class);
        getActivity().startActivityForResult(fenzu_manage,1);

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        view.getLocationInWindow(location);
        rawX = location[0] + view.getWidth()/2;
        rawY=  location[1];
        QPopuWindow.getInstance(getActivity()).builder
                .bindView(view, 0)
                .setPopupItemList(new String[]{"分组管理"})
                .setPointers(rawX,rawY)
                .setOnPopuListItemClickListener(new QPopuWindow.OnPopuListItemClickListener() {
                    @Override
                    public void onPopuListItemClick(View anchorView, int anchorViewPosition, int position) {
                        Toast.makeText(getContext(),anchorViewPosition+"---->"+position,Toast.LENGTH_SHORT).show();
                        Intent fenzu_manage = new Intent(getActivity(), Fenzu_manageActivity.class);
                        getActivity().startActivityForResult(fenzu_manage,1);
                    }
                }).show();;
        return true;           //默认为false，设为true时，长按/点击事件不会展开Group
    }

    //新增组
    public static void addGroup(String newGroupName){
        parentList.add(newGroupName);
        List<String> list = new ArrayList<String>();
        map.put(newGroupName, list);
        addressbookAdapter.notifyDataSetChanged();
    }

    //新增子项到指定组
    public static void addChild(int groupPosition, String newChildName){
        String groupName = parentList.get(groupPosition);
        List<String> list = map.get(groupName);
        list.add(newChildName);
        addressbookAdapter.notifyDataSetChanged();
    }

    //删除指定组
    public static void deleteGroup(int groupPos){
        String groupName = parentList.get(groupPos);
        map.remove(groupName);
        parentList.remove(groupPos);
        addressbookAdapter.notifyDataSetChanged();
    }
    //删除指定子项
    public static void deleteChild(int groupPos, int childPos){
        String groupName = parentList.get(groupPos);
        List<String> list = map.get(groupName);
        list.remove(childPos);
        addressbookAdapter.notifyDataSetChanged();
    }

    //修改该项名称
    public void modifyName(int groupPosition, int childPosition, String modifyName){
        Toast.makeText(getActivity(), String.valueOf(groupPosition)+'-'+String.valueOf(childPosition), Toast.LENGTH_SHORT).show();
        if(childPosition<0){
            //修改组名称
            String groupName = parentList.get(groupPosition);
            if(!groupName.equals(modifyName)){
                map.put(modifyName, map.get(groupName));
                map.remove(groupName);
                parentList.set(groupPosition, modifyName);
            }

        }else{
            //修改子项名称
            String group = parentList.get(groupPosition);
            List<String> list =map.get(group);
            list.set(childPosition, modifyName);
            map.put(group, list);
        }
        addressbookAdapter.notifyDataSetChanged();
    }

}
