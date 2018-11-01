package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.GroupNoticeAdapter;

import java.util.ArrayList;


public class GroupNoticeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private ImageView backtrack2;
    private ListView lv_group_apply;

    private ArrayList<String> list;
    GroupNoticeAdapter group_noticeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_notice);

        initGroup_apply();

        list = new ArrayList<>();
        for (int i = 0;i < 3 ;i++){
            list.add("小明");
        }


        group_noticeAdapter = new GroupNoticeAdapter(getApplicationContext(),list);
        lv_group_apply.setAdapter(group_noticeAdapter);
        group_noticeAdapter.notifyDataSetChanged();

    }

    private void initGroup_apply(){
        backtrack2 = (ImageView)findViewById(R.id.backtrack);
        lv_group_apply = (ListView)findViewById(R.id.lv_group_notice);

        backtrack2.setOnClickListener(this);

        lv_group_apply.setOnItemClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.backtrack:           //返回群聊页面
                setResult(10);
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getApplicationContext(),"pppppp",Toast.LENGTH_LONG).show();
        Intent group_apply_info = new Intent(getApplicationContext(),GroupNoticeInfoActivity.class);
        startActivityForResult(group_apply_info,1);
    }
}