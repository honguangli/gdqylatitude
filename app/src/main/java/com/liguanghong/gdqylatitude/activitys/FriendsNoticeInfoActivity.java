package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;

public class FriendsNoticeInfoActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backtrack;
    private RelativeLayout relativelayout_id;
    private Button bt_refuse_notice;
    private Button bt_agree_notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_notice_info);

        initFriend_apply_info();
    }

    private void initFriend_apply_info(){
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
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.bt_agree_notice:                      //同意

                break;

            case R.id.bt_refuse_notice:                     //拒绝

                break;

            case R.id.relativelayout_id:                    //查看该申请详情
                Intent friend_apply_info = new Intent(getApplicationContext(),UserInfoActivity.class);
                startActivityForResult(friend_apply_info,1);
                break;

            case R.id.backtrack:                            //返回群通知
                setResult(10);
                finish();
                break;
        }
    }
}
