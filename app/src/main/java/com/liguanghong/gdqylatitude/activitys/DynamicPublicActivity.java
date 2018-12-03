package com.liguanghong.gdqylatitude.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.DynamicPublicAdpter;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.util.ImageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicPublicActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView backtrack;
    private TextView menu;

    private GridView gridView;
    private List<Map<String, Object>> datas;
    private DynamicPublicAdpter dynamicPublicAdpter;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dynamic_public;
    }

    @Override
    protected void initView() {
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE); //获取editor对象
        editor = sharedPreferences.edit();//获取编辑器

        backtrack = findViewById(R.id.backtrack);
        menu = findViewById(R.id.menu);
        gridView = findViewById(R.id.gridView);

        backtrack.setOnClickListener(this);
        menu.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        datas = new ArrayList<>();
        dynamicPublicAdpter = new DynamicPublicAdpter(datas, this);
        gridView.setAdapter(dynamicPublicAdpter);
        gridView.setOnItemClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 20) {
            // 从相册返回的数据
            if (data != null) {
                List<String> photo = (List<String>) data.getSerializableExtra("photo");
                for (int i =0; i < photo.size();i++){
                    String path = photo.get(i);
                    photoPath(path);
                }

            }
        }
    }

    public void photoPath(String path) {
        Map<String,Object> map=new HashMap<>();
        map.put("path",path);
        datas.add(map);
        dynamicPublicAdpter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.menu:             //发表

                break;

            case R.id.backtrack:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (datas.size()==i){
            SelectPhotoActivity.isMultiSelect = false;
            editor.putBoolean("isMultiSelect",SelectPhotoActivity.isMultiSelect);
            editor.commit();

            Intent photo=new Intent(DynamicPublicActivity.this,SelectPhotoActivity.class);
            startActivityForResult(photo,10);
        }
    }
}
