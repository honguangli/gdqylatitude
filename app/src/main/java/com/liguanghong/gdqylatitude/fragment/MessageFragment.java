package com.liguanghong.gdqylatitude.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.activitys.ChatActivity;
import com.liguanghong.gdqylatitude.adapter.MessageAdapter;
import com.liguanghong.gdqylatitude.base.BaseFragment;
import com.liguanghong.gdqylatitude.manager.GroupManager;
import com.liguanghong.gdqylatitude.unity.ChatMsg;
import com.liguanghong.gdqylatitude.unity.Friend;
import com.liguanghong.gdqylatitude.manager.FriendsManager;
import com.liguanghong.gdqylatitude.unity.Groupchat;

import java.util.List;

public class MessageFragment extends BaseFragment {

    private ListView lv_message;
    private MessageAdapter messageAdapter;
    private static Handler messageHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutId(){
        return R.layout.fragment_message;
    }

    //初始化组件
    @Override
    protected void initView(View view)
    {
        lv_message = view.findViewById(R.id.message_list);
    }
    //初始化数据
    @Override
    protected void initData() {

        messageHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 222:
                        if(messageAdapter != null)
                            messageAdapter.notifyDataSetChanged();
                        break;
                }
            }
        };

        messageAdapter = new MessageAdapter(getContext());
        lv_message.setAdapter(messageAdapter);
        
        lv_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                List<ChatMsg> list = (List<ChatMsg>)messageAdapter.getItem(i);
                if(list.get(0).getIssingle()){
                    Friend friend = FriendsManager.getFriendByID(Long.valueOf(l).intValue());
                    //activity跳转操作，注意：启动ChatActivity时必须传User对象
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("userinfo", friend);
                    startActivity(intent);
                } else{
                    Groupchat groupchat = GroupManager.getGroupByID(list.get(0).getReceiverid());
                    //activity跳转操作，注意：启动ChatActivity时必须传User对象
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("groupinfo", groupchat);
                    startActivity(intent);
                }
            }
        });

    }

    public static Handler getMessageHandler(){
        return messageHandler;
    }

}
