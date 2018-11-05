package com.liguanghong.gdqylatitude.activitys;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.GroupLeaguerManageAdapter;
import com.liguanghong.gdqylatitude.base.BaseActivity;

import java.util.ArrayList;

public class GroupLeaguerManageActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView backtrack;
    private TextView menu;

    private ListView lv;
    private ArrayList<String> list;
    GroupLeaguerManageAdapter groupLeaguerManageAdapter;

    @Override
    protected int getLayoutId() { return R.layout.activity_group_leaguer_manage; }

    @Override
    protected void initView() {
        backtrack = findViewById(R.id.backtrack);
        menu = findViewById(R.id.menu);

        backtrack.setOnClickListener(this);
        menu.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        list = new ArrayList<>();
        for (int i = 0;i < 3;i++){
            list.add("新建签到"+(i+1));
        }
        lv = findViewById(R.id.lv);
        groupLeaguerManageAdapter = new GroupLeaguerManageAdapter(this,list);
        lv.setAdapter(groupLeaguerManageAdapter);
        groupLeaguerManageAdapter.notifyDataSetChanged();
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.backtrack:
                finish();
                break;

            case R.id.menu:

                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getApplicationContext(),"群成员",Toast.LENGTH_LONG).show();
    }
}
