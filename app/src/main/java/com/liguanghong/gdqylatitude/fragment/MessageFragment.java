package com.liguanghong.gdqylatitude.fragment;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;

public class MessageFragment extends BaseFragment {

    private ListView lv_message;
    private MessageAdapter messageAdapter;
    private ArrayList<String> list;

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
        list = new ArrayList<>();
        for (int i = 0;i < 10 ;i++){
            list.add("用户"+i);
            Log.i("测试", "消息Fragment：" + "用户名：[" + list.get(i) + "]");
        }
        messageAdapter = new MessageAdapter(getContext(),list);
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

    }

}
