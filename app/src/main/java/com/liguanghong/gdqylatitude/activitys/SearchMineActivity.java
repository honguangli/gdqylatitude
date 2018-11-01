package com.liguanghong.gdqylatitude.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;

public class SearchMineActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backtrack_search,search_add;
    private TextView button_search_cancel;
    private EditText edittext_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_mine;
    }

    @Override
    protected void initView() {
        button_search_cancel = (TextView)findViewById(R.id.button_search_cancel);
        backtrack_search = (ImageView) findViewById(R.id.backtrack);
        search_add = (ImageView) findViewById(R.id.menu);
        edittext_search =(EditText)findViewById(R.id.edittext_search);

        button_search_cancel.setOnClickListener(this);
        backtrack_search.setOnClickListener(this);
        search_add.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.menu:

                break;

            case R.id.button_search_cancel:             //取消

                break;

            case R.id.backtrack:
                //销毁当前界面，返回通讯录界面
                finish();
                break;
        }
    }
}
