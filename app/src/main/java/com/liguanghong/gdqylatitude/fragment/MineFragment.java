package com.liguanghong.gdqylatitude.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.activitys.DynamicFriendsActivity;
import com.liguanghong.gdqylatitude.base.BaseFragment;

public class MineFragment extends BaseFragment implements View.OnClickListener {
    private RelativeLayout personInfo;
    private RelativeLayout dyanamicFriends, dynamicMine;
    private RelativeLayout setting;
    private ImageView personal_head;
    private TextView personal_name;
    private TextView personal_id;

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
        personInfo = view.findViewById(R.id.personal_info);
        dyanamicFriends = view.findViewById(R.id.dynamic_friends);
        dynamicMine = view.findViewById(R.id.dynamic_mine);
        setting = view.findViewById(R.id.setting);
        personal_head = view.findViewById(R.id.personal_head);
        personal_name = view.findViewById(R.id.personal_name);
        personal_id = view.findViewById(R.id.personal_id);

        personInfo.setOnClickListener(this);
        dyanamicFriends.setOnClickListener(this);
        dynamicMine.setOnClickListener(this);
        setting.setOnClickListener(this);
    }
    //初始化数据
    @Override
    protected void initData()
    {
        personal_head.setImageResource(R.drawable.head);
        personal_name.setText("Tom");
        personal_id.setText("5464654564");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.personal_info:
                Toast.makeText(getActivity(), "您点击了用户信息", Toast.LENGTH_SHORT);
                //startActivity(new Intent(getActivity(), UserInfoActivity.class));
                break;
            case R.id.dynamic_friends:
                Toast.makeText(getActivity(), "您点击了动态", Toast.LENGTH_SHORT);
                startActivity(new Intent(getActivity(), DynamicFriendsActivity.class));
                break;
            case R.id.dynamic_mine:
                Toast.makeText(getActivity(), "您点击了相册", Toast.LENGTH_SHORT);
                //startActivity(new Intent(getActivity(), DynamicMineActivity.class));
                break;
            case R.id.setting:
                Toast.makeText(getActivity(), "您点击了设置", Toast.LENGTH_SHORT);
                //startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
        }
    }

}
