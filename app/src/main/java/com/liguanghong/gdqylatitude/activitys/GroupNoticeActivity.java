package com.liguanghong.gdqylatitude.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.liguanghong.gdqylatitude.R;


public class GroupNoticeActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView backtrack2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_notice);

        backtrack2 = (ImageView)findViewById(R.id.backtrack);
        backtrack2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.backtrack:           //返回群聊页面
//                Intent intent = new Intent(getApplicationContext(),GroupActivity.class);
//                startActivity(intent);
                setResult(10);
                finish();
                break;
        }
    }
}