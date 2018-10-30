package com.liguanghong.gdqylatitude.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;

public class Group_infoActivity extends AppCompatActivity {

    private ImageView backtrack_group_info;
    private TextView group_name;
    private TextView group_id;
    private RelativeLayout rly_group_all;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
    }
}
