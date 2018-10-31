package com.liguanghong.gdqylatitude.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;

public class GroupSignManageActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backtrack;                                //返回群管理
    private TextView menu;                                       //完成
    private RelativeLayout rly_group_newsign;                   //新建签到


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_sign_manage);

        initGroup_sign_manage();
    }

    private void initGroup_sign_manage(){

        backtrack = (ImageView)findViewById(R.id.backtrack);
        menu = (TextView)findViewById(R.id.menu);
        rly_group_newsign = (RelativeLayout)findViewById(R.id.rly_group_newsign);

        backtrack.setOnClickListener(this);
        menu.setOnClickListener(this);
        rly_group_newsign.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.rly_group_newsign:                    //新建签到

                break;

            case R.id.menu:                                  //完成

                break;

            case R.id.backtrack:                            //返回群管理
                setResult(10);
                finish();
                break;
        }
    }
}
