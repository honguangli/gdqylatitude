package com.liguanghong.gdqylatitude.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter{
    private List<Fragment> fragmentList;
    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments){
        super(fm);
        fragmentList=fragments;
    }
    @Override
    public Fragment getItem(int arg0) {
        return fragmentList.get(arg0);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
