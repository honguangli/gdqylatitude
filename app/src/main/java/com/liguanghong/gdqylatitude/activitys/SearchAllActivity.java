package com.liguanghong.gdqylatitude.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;

public class SearchAllActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backtrack;
    private TextView tv_add_friend;
    private TextView tv_add_group;
    private Button bt_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_all);

        initSearchAll();
    }

    private void initSearchAll(){
        backtrack = (ImageView)findViewById(R.id.backtrack);
        tv_add_friend = (TextView) findViewById(R.id.tv_add_friend);
        tv_add_group = (TextView)findViewById(R.id.tv_add_group);
        bt_search = (Button)findViewById(R.id.bt_search);


        backtrack.setOnClickListener(this);
        bt_search.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.bt_search:

                break;

            case R.id.backtrack:
                setResult(10);
                finish();
                break;
        }
    }
}
