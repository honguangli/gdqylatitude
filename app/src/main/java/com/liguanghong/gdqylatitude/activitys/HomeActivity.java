package com.liguanghong.gdqylatitude.activitys;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.FragmentAdapter;
import com.liguanghong.gdqylatitude.base.BaseViewPager;
import com.liguanghong.gdqylatitude.fragment.AddressbookFragment;
import com.liguanghong.gdqylatitude.fragment.MapFragment;
import com.liguanghong.gdqylatitude.fragment.MessageFragment;
import com.liguanghong.gdqylatitude.fragment.MineFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout map,message,addressbook,mine;
    private BaseViewPager vp;
    private ImageView iv_map,iv_message,iv_addressbook,iv_mine;
    private TextView tv_map,tv_message,tv_addressbook,tv_mine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_home);

        initView();
        getData();
    }

    private void initView(){
        List<Fragment> fragments = new ArrayList<Fragment>();
        Fragment mapFragment = new MapFragment();
        Fragment messageFragment = new MessageFragment();
        Fragment addressbookFragment = new AddressbookFragment();
        Fragment mineFragment = new MineFragment();
        fragments.add(mapFragment);
        fragments.add(messageFragment);
        fragments.add(addressbookFragment);
        fragments.add(mineFragment);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(),fragments);
        vp = (BaseViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(adapter);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.map:
                vp.setCurrentItem(0);
                iv_map.setImageDrawable(getResources().getDrawable(R.drawable.map_light));
                iv_message.setImageDrawable(getResources().getDrawable(R.drawable.message));
                iv_addressbook.setImageDrawable(getResources().getDrawable(R.drawable.addressbook));
                iv_mine.setImageDrawable(getResources().getDrawable(R.drawable.mine));
                tv_map.setTextColor(getResources().getColor(R.color.colorTitle));
                tv_message.setTextColor(getResources().getColor(R.color.colorBF));
                tv_addressbook.setTextColor(getResources().getColor(R.color.colorBF));
                tv_mine.setTextColor(getResources().getColor(R.color.colorBF));
                break;
            case R.id.message:
                vp.setCurrentItem(1);
                iv_map.setImageDrawable(getResources().getDrawable(R.drawable.map));
                iv_message.setImageDrawable(getResources().getDrawable(R.drawable.message_light));
                iv_addressbook.setImageDrawable(getResources().getDrawable(R.drawable.addressbook));
                iv_mine.setImageDrawable(getResources().getDrawable(R.drawable.mine));
                tv_map.setTextColor(getResources().getColor(R.color.colorBF));
                tv_message.setTextColor(getResources().getColor(R.color.colorTitle));
                tv_addressbook.setTextColor(getResources().getColor(R.color.colorBF));
                tv_mine.setTextColor(getResources().getColor(R.color.colorBF));
                break;
            case R.id.addressbook:
                vp.setCurrentItem(2);
                iv_map.setImageDrawable(getResources().getDrawable(R.drawable.map));
                iv_message.setImageDrawable(getResources().getDrawable(R.drawable.message));
                iv_addressbook.setImageDrawable(getResources().getDrawable(R.drawable.addressbook_light));
                iv_mine.setImageDrawable(getResources().getDrawable(R.drawable.mine));
                tv_map.setTextColor(getResources().getColor(R.color.colorBF));
                tv_message.setTextColor(getResources().getColor(R.color.colorBF));
                tv_addressbook.setTextColor(getResources().getColor(R.color.colorTitle));
                tv_mine.setTextColor(getResources().getColor(R.color.colorBF));
                break;
            case R.id.mine:
                vp.setCurrentItem(3);
                iv_map.setImageDrawable(getResources().getDrawable(R.drawable.map));
                iv_message.setImageDrawable(getResources().getDrawable(R.drawable.message));
                iv_addressbook.setImageDrawable(getResources().getDrawable(R.drawable.addressbook));
                iv_mine.setImageDrawable(getResources().getDrawable(R.drawable.mine_light));
                tv_map.setTextColor(getResources().getColor(R.color.colorBF));
                tv_message.setTextColor(getResources().getColor(R.color.colorBF));
                tv_addressbook.setTextColor(getResources().getColor(R.color.colorBF));
                tv_mine.setTextColor(getResources().getColor(R.color.colorTitle));
                break;
        }
    }

    private void getData(){

    }
}
