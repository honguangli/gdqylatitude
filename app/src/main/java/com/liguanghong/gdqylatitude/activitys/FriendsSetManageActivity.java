package com.liguanghong.gdqylatitude.activitys;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.FriendsSetManageAdapter;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.util.DensityUtil;

import java.util.ArrayList;

public class FriendsSetManageActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backtrack_fenzu;
    private TextView done;
    private RelativeLayout rly_addgroup;
    private ListView lv;

    private ArrayList<String> list;
    FriendsSetManageAdapter friendsSetManageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_friends_set_manage;
    }

    @Override
    protected void initView() {
        backtrack_fenzu = (ImageView)findViewById(R.id.backtrack);

        done = (TextView) findViewById(R.id.done);
        rly_addgroup = (RelativeLayout) findViewById(R.id.rly_addgroup);


        backtrack_fenzu.setOnClickListener(this);
        done.setOnClickListener(this);
        rly_addgroup.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        list = new ArrayList<>();
        for (int i = 0;i < 3;i++){
            list.add("新建分组"+(i+1));
        }
        lv = findViewById(R.id.lv);
        friendsSetManageAdapter = new FriendsSetManageAdapter(this,list);
        lv.setAdapter(friendsSetManageAdapter);
        friendsSetManageAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.backtrack:      //返回通讯录
                setResult(10);
                finish();
                break;

            case R.id.done:                  //完成分组管理

                break;

            case R.id.rly_addgroup:         //添加分组
                showaddgroup();
                break;

        }
    }

    public void showaddgroup() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_friends_set_remark,null,false);
        final AlertDialog alertdialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(true)
                .show();
        TextView title = view.findViewById(R.id.title);
        TextView surebutton = view.findViewById(R.id.sure);
        TextView cancelbutton = view.findViewById(R.id.cancel);

        EditText et_content = view.findViewById(R.id.et_content);

        title.setText("新建分组");
        et_content.setHint("请输入新增分组名称");

        surebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertdialog.cancel();
            }
        });
    }
}
