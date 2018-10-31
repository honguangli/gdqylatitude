package com.liguanghong.gdqylatitude.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.liguanghong.gdqylatitude.R;

public class FriendsNoticeActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tx_name,tx_notice;
    private Button bt_notice;

    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_friends_notice);
    initView();
    }
    private void initView()
    {
        tx_name = (TextView) findViewById(R.id.tv_friend_name);
        tx_notice = (TextView) findViewById(R.id.tv_friend_notice);
        bt_notice = (Button) findViewById(R.id.bt_friend_notice);
        bt_notice.setOnClickListener(this);
    }
    //按钮点击监听
    @Override
    public void onClick(View v) {

    }
}
