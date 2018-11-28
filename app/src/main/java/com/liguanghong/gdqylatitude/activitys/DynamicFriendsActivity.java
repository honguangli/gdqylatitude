package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.DynamicFriendsAdapter;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.util.ImageUtils;
import com.liguanghong.gdqylatitude.view.FadingScrollView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DynamicFriendsActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backtrack,write_messge;
    private CircleImageView img_headphoto1;
    private RelativeLayout topPanel;
    private FadingScrollView fadingScrollView;
    private ListView listView;
    private ArrayList<String> list;
    DynamicFriendsAdapter dynamicFriendsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dynamic_friends;
    }

    @Override
    protected void initView() {
        backtrack = findViewById(R.id.backtrack);
        write_messge = findViewById(R.id.write_messge);
        img_headphoto1 = findViewById(R.id.img_headphoto1);
        topPanel = findViewById(R.id.topPanel);

        byte[] b = Base64.decode(UserManager.getInstance().getAppUser().getHeadportrait(), Base64.DEFAULT);
        img_headphoto1.setImageBitmap(ImageUtils.getPicFromBytes(b,null));
        //topPanel.setAlpha(0); //设置标题栏的透明度

        fadingScrollView = findViewById(R.id.nac_root);
        fadingScrollView.smoothScrollTo(0, 0);  //设置刚进入页面就在顶部
        fadingScrollView.setFadingView(topPanel);       //设置标题栏
        fadingScrollView.setFadingHeightView(findViewById(R.id.nac_image)); //图片及其高度

        backtrack.setOnClickListener(this);
        write_messge.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        list = new ArrayList<String>();
        for (int i = 0; i < 10;i++){
            list.add("Tom"+(i+1));
        }
        dynamicFriendsAdapter = new DynamicFriendsAdapter(this,list);
        listView = findViewById(R.id.lv);
        listView.setAdapter(dynamicFriendsAdapter);
        dynamicFriendsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.write_messge:
                Intent intent = new Intent(DynamicFriendsActivity.this,DynamicPublicActivity.class);
                startActivity(intent);
                break;
            case R.id.backtrack:
                finish();
                break;
        }
    }
}
