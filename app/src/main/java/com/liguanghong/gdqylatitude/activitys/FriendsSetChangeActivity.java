package com.liguanghong.gdqylatitude.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.liguanghong.gdqylatitude.R;

public class FriendsSetChangeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backtrack_fenzu_move;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_set_change);

        initFenzumove();
    }

    private void initFenzumove(){
        backtrack_fenzu_move = (ImageView)findViewById(R.id.backtrack);

        backtrack_fenzu_move.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.backtrack:
                setResult(10);
                finish();
                break;
        }
    }
}
