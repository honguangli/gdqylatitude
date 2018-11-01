package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;

public class GroupNoticeInfoActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backtrack;
    private RelativeLayout relativelayout_id;
    private Button bt_refuse_notice;
    private Button bt_agree_notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_notice_info;
    }

    @Override
    protected void initView() {
        backtrack = (ImageView)findViewById(R.id.backtrack);
        relativelayout_id = (RelativeLayout)findViewById(R.id.relativelayout_id);
        bt_refuse_notice = (Button)findViewById(R.id.bt_refuse_notice);
        bt_agree_notice = (Button)findViewById(R.id.bt_agree_notice);

        backtrack.setOnClickListener(this);
        relativelayout_id.setOnClickListener(this);
        bt_refuse_notice.setOnClickListener(this);
        bt_agree_notice.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.bt_agree_notice:                      //同意

                break;

            case R.id.bt_refuse_notice:                     //拒绝

                break;

            case R.id.relativelayout_id:
                //启动群通知来源界面
                startActivity(new Intent(getApplicationContext(),GroupInfoActivity.class));
                break;

            case R.id.backtrack:
                //销毁当前界面，返回群通知界面
                finish();
                break;
        }
    }
}
