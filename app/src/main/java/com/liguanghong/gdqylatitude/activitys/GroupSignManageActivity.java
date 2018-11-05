package com.liguanghong.gdqylatitude.activitys;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.GroupSignManageAdapter;
import com.liguanghong.gdqylatitude.base.BaseActivity;

import java.util.ArrayList;

public class GroupSignManageActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backtrack;                                //返回群管理
    private TextView menu;                                       //完成
    private RelativeLayout rly_group_newsign;                   //新建签到

    private ListView lv;
    private ArrayList<String> list;
    GroupSignManageAdapter groupSignManageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_sign_manage;
    }

    @Override
    protected void initView() {
        backtrack = (ImageView)findViewById(R.id.backtrack);
        menu = (TextView)findViewById(R.id.menu);
        rly_group_newsign = (RelativeLayout)findViewById(R.id.rly_group_newsign);

        backtrack.setOnClickListener(this);
        menu.setOnClickListener(this);
        rly_group_newsign.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        list = new ArrayList<>();
        for (int i = 0;i < 3;i++){
            list.add("新建签到"+(i+1));
        }
        lv = findViewById(R.id.lv);
        groupSignManageAdapter = new GroupSignManageAdapter(this,list);
        lv.setAdapter(groupSignManageAdapter);
        groupSignManageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.rly_group_newsign:                    //新建签到
                showaddgroup();
                break;

            case R.id.menu:                                  //完成

                break;

            case R.id.backtrack:
                //销毁当前界面，返回群管理界面
                finish();
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

        title.setText("新建签到");
        et_content.setHint("请输入新增签到名称");

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
