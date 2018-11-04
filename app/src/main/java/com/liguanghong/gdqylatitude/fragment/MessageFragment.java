package com.liguanghong.gdqylatitude.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.activitys.ChatActivity;
import com.liguanghong.gdqylatitude.adapter.MessageAdapter;
import com.liguanghong.gdqylatitude.base.BaseFragment;
import com.liguanghong.gdqylatitude.unity.Chatmessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageFragment extends BaseFragment {

    private ListView lv_message;
    private MessageAdapter messageAdapter;
    private Map<Integer, List<Chatmessage>> chatmessageMap;
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
        chatmessageMap = new HashMap<Integer, List<Chatmessage>>();


        messageAdapter = new MessageAdapter(getContext(), chatmessageMap);
        lv_message.setAdapter(messageAdapter);
        lv_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("测试", "消息Fragment：" + "i:[" + i + "]，l:[" + l + "]");
                //activity跳转操作
                //Intent intent = new Intent();
                startActivity(new Intent(getActivity(), ChatActivity.class));
            }
        });
        messageAdapter.notifyDataSetChanged();

        messageHandler = new Handler(){
          public void handleMessage(Message message){
              switch (message.what){
                  case 200:
                      Chatmessage chatmessage = (Chatmessage)message.obj;
                      if(chatmessage.getIssingle()){
                          //私聊
                          List<Chatmessage> list = chatmessageMap.get(chatmessage.getSenderid());
                          if(list != null)
                              list.add(chatmessage);
                          else {
                              list = new ArrayList<Chatmessage>();
                              list.add(chatmessage);
                              chatmessageMap.put(chatmessage.getSenderid(), list);
                          }
                      } else{
                          //群聊
                          List<Chatmessage> list = chatmessageMap.get(chatmessage.getReceivergroupid());
                          if(list != null)
                              list.add(chatmessage);
                          else
                              chatmessageMap.put(chatmessage.getSenderid(), list);
                      }
                      messageAdapter.notifyDataSetChanged();
                      break;
              }
          }
        };

    }

    public static void addMessage(Chatmessage chatmessage){
        Message message = new Message();
        message.what = 200;
        message.obj = chatmessage;
        messageHandler.sendMessage(message);
    }

}
