package com.liguanghong.gdqylatitude.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.liguanghong.gdqylatitude.R;


public class GroupCreateActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backtrack_create;
    private RelativeLayout rly_add_newgroup;
    private EditText ed_chatname;
    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);

        initGroupCreate();
    }

    private void initGroupCreate(){
        backtrack_create = (ImageView)findViewById(R.id.backtrack);
        rly_add_newgroup = (RelativeLayout)findViewById(R.id.add);
        btn_submit = (Button)findViewById(R.id.btn_submit);
        ed_chatname = (EditText)findViewById(R.id.ed_chatname);

        backtrack_create.setOnClickListener(this);
        rly_add_newgroup.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.add:         //选择头像

                break;

            case R.id.btn_submit:               //确认创建

                break;

            case R.id.backtrack:         //返回通讯录
                setResult(10);
                finish();
                break;
        }
    }
}
