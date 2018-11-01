package com.liguanghong.gdqylatitude.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;

public class FriendsSetChangeActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backtrack_fenzu_move;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_friends_set_change;
    }

    @Override
    protected void initView() {
        backtrack_fenzu_move = (ImageView)findViewById(R.id.backtrack);

        backtrack_fenzu_move.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.backtrack:
                //销毁当前界面，返回好友详情界面
                finish();
                break;
        }
    }
}
