package com.liguanghong.gdqylatitude.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.activitys.ChatActivity;
import com.liguanghong.gdqylatitude.adapter.MessageAdapter;
import com.liguanghong.gdqylatitude.base.BaseFragment;
import com.liguanghong.gdqylatitude.unity.Chatmessage;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.manager.ConversationManager;
import com.liguanghong.gdqylatitude.manager.FriendsManager;

public class MessageFragment extends BaseFragment {

    private ListView lv_message;
    private MessageAdapter messageAdapter;
    private static Handler messageHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
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
        lv_message = (ListView)view.findViewById(R.id.message_list);
    }
    //初始化数据
    @Override
    protected void initData()
    {
        messageAdapter = new MessageAdapter(getContext());
        lv_message.setAdapter(messageAdapter);
        lv_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = FriendsManager.getFriendByID(Long.valueOf(l).intValue());
                //activity跳转操作，注意：启动ChatActivity时必须传User对象
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("friendInfo", user);
                startActivity(intent);
            }
        });
        messageAdapter.notifyDataSetChanged();

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

    }

    public static Handler getMessageHandler(){
        return messageHandler;
    }

}
