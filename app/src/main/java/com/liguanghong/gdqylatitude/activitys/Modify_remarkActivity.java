package com.liguanghong.gdqylatitude.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;

public class Modify_remarkActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView backtrack_modify_remark;
    private TextView complete;
    private EditText ed_notesName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_remark);

        initModify_remark();
    }

    private void initModify_remark(){

        backtrack_modify_remark = (ImageView)findViewById(R.id.backtrack);
        complete = (TextView)findViewById(R.id.complete);
        ed_notesName = (EditText)findViewById(R.id.ed_chatname);

        backtrack_modify_remark.setOnClickListener(this);
        complete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.backtrack:
                setResult(10);
                finish();
                break;

            case R.id.complete:
                setResult(10);
                finish();
                break;
        }
    }
}
