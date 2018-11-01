package com.liguanghong.gdqylatitude.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;

public class FriendsSetManageActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backtrack_fenzu;
    private ImageView img_movegroup;

    private TextView done;

    private RelativeLayout rly_addgroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_set_manage);

        initFenzuManage();
    }

    private void initFenzuManage(){

        backtrack_fenzu = (ImageView)findViewById(R.id.backtrack);
        img_movegroup = (ImageView)findViewById(R.id.img_movegroup);

        done = (TextView) findViewById(R.id.done);
        rly_addgroup = (RelativeLayout) findViewById(R.id.rly_addgroup);

        backtrack_fenzu.setOnClickListener(this);
        img_movegroup.setOnClickListener(this);
        done.setOnClickListener(this);
        rly_addgroup.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.backtrack:      //返回通讯录
                setResult(10);
                finish();
                break;

            case R.id.done:                  //完成分组管理

                break;

            case R.id.rly_addgroup:         //添加分组

                /*
                弹出dialog
                填入新建分组名
                点击确定
                新建分组成功
                 */

                break;

            case R.id.img_movegroup:        //删除已有的分组

                break;
        }
    }
}
