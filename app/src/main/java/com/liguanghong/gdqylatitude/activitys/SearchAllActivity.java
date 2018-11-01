package com.liguanghong.gdqylatitude.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;

public class SearchAllActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backtrack;
    private TextView tv_add_friend;
    private TextView tv_add_group;
    private Button bt_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_all;
    }

    @Override
    protected void initView() {
        backtrack = (ImageView)findViewById(R.id.backtrack);
        tv_add_friend = (TextView) findViewById(R.id.tv_add_friend);
        tv_add_group = (TextView)findViewById(R.id.tv_add_group);
        bt_search = (Button)findViewById(R.id.bt_search);

        backtrack.setOnClickListener(this);
        bt_search.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.bt_search:
                //执行网络查找
                search();
                break;

            case R.id.backtrack:
                //销毁当前界面，返回通讯录界面
                finish();
                break;
        }
    }

    private void search(){

    }

}
