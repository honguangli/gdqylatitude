package com.liguanghong.gdqylatitude.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.liguanghong.gdqylatitude.R;
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

    //初始化组件
    @Override
    protected void initView(View view)
    {
        lv_message=(ListView)view.findViewById(R.id.lv_message);

    }
    //初始化数据
    @Override
    protected void initData()
    {
        list = new ArrayList<>();
        for (int i = 0;i < 10 ;i++){
            list.add(MessageAdapter.name);
        }
    }

}
