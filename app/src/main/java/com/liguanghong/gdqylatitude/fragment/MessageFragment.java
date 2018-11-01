package com.liguanghong.gdqylatitude.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.MessageAdapter;

import java.util.ArrayList;

public class MessageFragment extends Fragment {

    private ListView lv_message;
    private MessageAdapter messageAdapter;
    private ArrayList<String> list;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if(view == null)
        {
            view = inflater.inflate(R.layout.fragment_message, container, false);

            initView();
            initData();

            messageAdapter = new MessageAdapter(getContext(),list);
            lv_message.setAdapter(messageAdapter);
            messageAdapter.notifyDataSetChanged();

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
        lv_message=(ListView)view.findViewById(R.id.lv_message);

    }
    //初始化数据
    private void initData()
    {
        list = new ArrayList<>();
        for (int i = 0;i < 10 ;i++){
            list.add(MessageAdapter.name);
        }
    }

}
