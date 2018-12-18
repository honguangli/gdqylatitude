package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.GroupNoticeAdapter;
import com.liguanghong.gdqylatitude.base.BaseActivity;

import java.util.ArrayList;


public class GroupNoticeActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ImageView backtrack2;
    private ListView lv_group_apply;

    private ArrayList<String> list;
    GroupNoticeAdapter group_noticeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_notice;
    }

    @Override
    protected void initView() {
        backtrack2 = (ImageView)findViewById(R.id.backtrack);
        lv_group_apply = (ListView)findViewById(R.id.lv_group_notice);

        backtrack2.setOnClickListener(this);

        lv_group_apply.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        list = new ArrayList<>();
//        for (int i = 0;i < 3 ;i++){
//            list.add("小明");
//        }

        group_noticeAdapter = new GroupNoticeAdapter(getApplicationContext(),list);
        lv_group_apply.setAdapter(group_noticeAdapter);
        group_noticeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.backtrack:
                //销毁当前界面，返回群聊界面
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //启动群通知详情界面
        startActivity(new Intent(getApplicationContext(),GroupNoticeInfoActivity.class));
    }
}