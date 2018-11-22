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
import com.liguanghong.gdqylatitude.activitys.ChatActivity;
import com.liguanghong.gdqylatitude.activitys.DynamicFriendsActivity;
import com.liguanghong.gdqylatitude.activitys.DynamicMineActivity;
import com.liguanghong.gdqylatitude.activitys.SettingActivity;
import com.liguanghong.gdqylatitude.activitys.UserInfoActivity;
import com.liguanghong.gdqylatitude.activitys.UserInfoMineActivity;
import com.liguanghong.gdqylatitude.base.BaseFragment;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.unity.User;

public class MineFragment extends BaseFragment implements View.OnClickListener {

    private RelativeLayout personInfo;
    private RelativeLayout dyanamicFriends;
    private RelativeLayout dynamicMine;
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

    @Override
    protected int getLayoutId(){
        return R.layout.fragment_mine;
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
        //personal_id = view.findViewById(R.id.personal_id);

        personInfo.setOnClickListener(this);
        dyanamicFriends.setOnClickListener(this);
        dynamicMine.setOnClickListener(this);
        setting.setOnClickListener(this);
    }

    //初始化数据
    @Override
    protected void initData()
    {
        User user = UserManager.getInstance().getAppUser();
        personal_name.setText(user.getLogname());
        //personal_id.setText(user.getUserid() + "");

        byte[] b = android.util.Base64.decode(UserManager.getAppUser().getHeadportrait(), android.util.Base64.DEFAULT);
        personal_head.setImageBitmap(ChatActivity.getPicFromBytes(b,null));
        System.out.println("666666666666666666"+b);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.personal_info:
                startActivity(new Intent(getActivity(), UserInfoMineActivity.class));
                break;
            case R.id.dynamic_friends:
                startActivity(new Intent(getActivity(), DynamicFriendsActivity.class));
                break;
            case R.id.dynamic_mine:
                startActivity(new Intent(getActivity(), DynamicMineActivity.class));
                break;
            case R.id.setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
        }
    }

}
