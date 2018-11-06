package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.FragmentAdapter;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.base.BaseViewPager;
import com.liguanghong.gdqylatitude.fragment.AddressbookFragment;
import com.liguanghong.gdqylatitude.fragment.MapFragment;
import com.liguanghong.gdqylatitude.fragment.MessageFragment;
import com.liguanghong.gdqylatitude.fragment.MineFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private BaseViewPager viewPager;

    private LinearLayout map;
    private LinearLayout message;
    private LinearLayout addressbook;
    private LinearLayout mine;

    private ImageView iv_map;
    private ImageView iv_message;
    private ImageView iv_addressbook;
    private ImageView iv_mine;

    private TextView tv_map;
    private TextView tv_message;
    private TextView tv_addressbook;
    private TextView tv_mine;

    private ImageView iv_current;
    private TextView tv_current;
    private int id_current_gray;

    private static Handler homeHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView(){

        viewPager = (BaseViewPager) findViewById(R.id.viewpager);

        map = (LinearLayout)findViewById(R.id.map);
        message = (LinearLayout)findViewById(R.id.message);
        addressbook = (LinearLayout)findViewById(R.id.addressbook);
        mine = (LinearLayout)findViewById(R.id.mine);
        map.setOnClickListener(this);
        message.setOnClickListener(this);
        addressbook.setOnClickListener(this);
        mine.setOnClickListener(this);

        iv_map = (ImageView)findViewById(R.id.map_iv);
        iv_message = (ImageView)findViewById(R.id.message_iv);
        iv_addressbook = (ImageView)findViewById(R.id.addressbook_iv);
        iv_mine = (ImageView)findViewById(R.id.mine_iv);
        tv_map = (TextView)findViewById(R.id.map_tv);
        tv_message = (TextView)findViewById(R.id.message_tv);
        tv_addressbook = (TextView)findViewById(R.id.addressbook_tv);
        tv_mine = (TextView)findViewById(R.id.mine_tv);

    }

    @Override
    protected void initData(){

        homeHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 222:
                        //强制登出
                        logout();
                        break;
                }
            }
        };

        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new MapFragment());
        fragments.add(new MessageFragment());
        fragments.add(new AddressbookFragment());
        fragments.add(new MineFragment());
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOffscreenPageLimit(4);
        iv_current = iv_map;
        tv_current = tv_map;
        id_current_gray = R.drawable.map;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.map:
                viewPager.setCurrentItem(0, false);
                changeNavigationBar(iv_map, tv_map, R.drawable.map,  R.drawable.map_light);
                break;
            case R.id.message:
                viewPager.setCurrentItem(1, false);
                changeNavigationBar(iv_message, tv_message, R.drawable.message, R.drawable.message_light);
                break;
            case R.id.addressbook:
                viewPager.setCurrentItem(2, false);
                changeNavigationBar(iv_addressbook, tv_addressbook, R.drawable.addressbook, R.drawable.addressbook_light);
                break;
            case R.id.mine:
                viewPager.setCurrentItem(3, false);
                changeNavigationBar(iv_mine, tv_mine, R.drawable.mine, R.drawable.mine_light);
                break;

        }

    }

    /**
     * 改变底部导航栏显示
     * @param iv
     * @param tv
     * @param id_next_gray
     * @param id_next_light
     */
    private void changeNavigationBar(ImageView iv, TextView tv, int id_next_gray, int id_next_light){

        iv_current.setImageDrawable(getResources().getDrawable(id_current_gray));
        iv_current = iv;
        iv_current.setImageDrawable(getResources().getDrawable(id_next_light));
        id_current_gray = id_next_gray;

        tv_current.setTextColor(getResources().getColor(R.color.colorBF));
        tv_current = tv;
        tv_current.setTextColor(getResources().getColor(R.color.colorTitle));

    }

    /**
     * 强制登出
     */
    private void logout(){
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    public static Handler getHomeHandler(){
        return homeHandler;
    }

}
