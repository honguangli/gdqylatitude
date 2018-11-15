package com.liguanghong.gdqylatitude.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.DynamicMineAdapter;
import com.liguanghong.gdqylatitude.base.BaseActivity;

import java.util.ArrayList;

public class DynamicMineActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backtrack;
    private ListView listView;
    private ArrayList<String> list;
    DynamicMineAdapter dynamicMineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dynamic_mine;
    }

    @Override
    protected void initView() {
        backtrack = findViewById(R.id.backtrack);

        backtrack.setOnClickListener(this);

    }

    @Override
    protected void initData() {

        list = new ArrayList<String>();
        for (int i = 0; i < 5;i++){
            list.add("Tom");
        }
        dynamicMineAdapter = new DynamicMineAdapter(this,list);
        listView = findViewById(R.id.lv_dynamic_mine);
        listView.setAdapter(dynamicMineAdapter);
        dynamicMineAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.backtrack:
                finish();
                break;
        }
    }
}
