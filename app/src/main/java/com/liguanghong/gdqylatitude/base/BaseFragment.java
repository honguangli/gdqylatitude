package com.liguanghong.gdqylatitude.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(getLayoutId(), container, false);
        initView(view);
        initData();

        return view;
    }

    /**
     * 获取布局文件
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化组件
     */
    protected abstract void initView(View view);

    /**
     * 初始化数据
     */
    protected abstract void initData();

}
