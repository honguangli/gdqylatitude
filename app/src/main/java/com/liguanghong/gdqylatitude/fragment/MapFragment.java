package com.liguanghong.gdqylatitude.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liguanghong.gdqylatitude.R;

public class MapFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if(view == null)
        {
            view = inflater.inflate(R.layout.fragment_map, container, false);

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

    }
    //初始化数据
    private void initData()
    {

    }
}
