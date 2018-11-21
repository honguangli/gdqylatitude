package com.liguanghong.gdqylatitude.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.activitys.FriendsSetManageActivity;
import com.liguanghong.gdqylatitude.activitys.FriendsNoticeActivity;
import com.liguanghong.gdqylatitude.activitys.ChatActivity;
import com.liguanghong.gdqylatitude.activitys.GroupActivity;
import com.liguanghong.gdqylatitude.activitys.SearchAllActivity;
import com.liguanghong.gdqylatitude.activitys.SearchMineActivity;
import com.liguanghong.gdqylatitude.adapter.AddressbookAdapter;
import com.liguanghong.gdqylatitude.base.BaseFragment;
import com.liguanghong.gdqylatitude.unity.Friend;
import com.liguanghong.gdqylatitude.manager.FriendsManager;
import com.liguanghong.gdqylatitude.view.QPopuWindow;

public class AddressbookFragment extends BaseFragment implements View.OnClickListener, ExpandableListView.OnChildClickListener, AdapterView.OnItemLongClickListener {

    private TextView img_search;
    private ImageView add;
    private RelativeLayout rly_new_friends;
    private RelativeLayout rly_chat_group;

    private ExpandableListView expandableListView;
    private AddressbookAdapter addressbookAdapter;

    private int[] location = new  int[2] ;
    private int rawX;
    private int rawY;

    private static Handler addressbookHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutId(){
        return R.layout.fragment_addressbook;
    }

    //初始化组件
    @Override
    protected void initView(View view)
    {

        add = view.findViewById(R.id.add);
        img_search = view.findViewById(R.id.edittext_search);
        rly_new_friends = view.findViewById(R.id.rly_new_friends);
        rly_chat_group = view.findViewById(R.id.rly_chat_group);

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
    @Override
    protected void initData(){

        addressbookHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 200:
                        addressbookAdapter = new AddressbookAdapter(getActivity().getApplicationContext());
                        expandableListView.setAdapter(addressbookAdapter);
                        break;
                }
            }
        };

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.add:
                startActivity(new Intent(getActivity(), SearchAllActivity.class));
                break;

            case R.id.edittext_search:
                startActivity(new Intent(getActivity(), SearchMineActivity.class));
                break;

            case R.id.rly_new_friends:
                startActivity(new Intent(getActivity(), FriendsNoticeActivity.class));
                break;

            case R.id.rly_chat_group:
                startActivity(new Intent(getActivity(), GroupActivity.class));
                break;
        }
    }

    public static Handler getAddressbookHandler(){
        return addressbookHandler;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        String setName = FriendsManager.getFriendsSetNameList().get(groupPosition);
        Friend friend = FriendsManager.getFriendsBySetName(setName).get(childPosition);
        //启动聊天界面
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("userinfo", friend);
        startActivity(intent);
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //长按分组，显示分组管理窗
        if(ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP){
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
                            //启动分组管理界面
                            startActivity(new Intent(getActivity(), FriendsSetManageActivity.class));
                        }
                    }).show();;
        }
        return true;           //默认为false，设为true时，长按/点击事件不会展开Group
    }

}
