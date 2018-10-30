package com.liguanghong.gdqylatitude.activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView backtrack_search,search_add;
    private TextView button_search_cancel;
    private EditText edittext_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_mine);

        initSearch();
    }

    private void initSearch(){
        button_search_cancel = (TextView)findViewById(R.id.button_search_cancel);
        backtrack_search = (ImageView) findViewById(R.id.backtrack);
        search_add = (ImageView) findViewById(R.id.menu);
        edittext_search =(EditText)findViewById(R.id.edittext_search);

        button_search_cancel.setOnClickListener(this);
        backtrack_search.setOnClickListener(this);
        search_add.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.menu:

                break;

            case R.id.button_search_cancel:             //取消

                break;

            case R.id.backtrack:                 //返回通讯录
                setResult(10);
                finish();
                break;
        }
    }
}
