package com.liguanghong.gdqylatitude.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;


public class GroupCreateActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backtrack_create;
    private RelativeLayout rly_add_newgroup;
    private EditText ed_chatname;
    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_create;
    }

    @Override
    protected void initView() {
        backtrack_create = (ImageView)findViewById(R.id.backtrack);
        rly_add_newgroup = (RelativeLayout)findViewById(R.id.add);
        btn_submit = (Button)findViewById(R.id.btn_submit);
        ed_chatname = (EditText)findViewById(R.id.ed_chatname);

        backtrack_create.setOnClickListener(this);
        rly_add_newgroup.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.add:         //选择头像

                break;

            case R.id.btn_submit:               //确认创建

                break;

            case R.id.backtrack:
                //销毁当前界面，返回通讯录界面
                finish();
                break;
        }
    }
}
