package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;

public class ResetPwdActivity extends BaseActivity implements View.OnClickListener{

    private EditText etEmail,etSecuritycode,etNewpassword,etSureNewpassword;
    private Button btnAgree;
    private TextView tvSecuritycode;
    private ImageView ivBacktrack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_resetpassword;
    }

    @Override
    protected void initView() {

        etEmail = findViewById(R.id.resetpassword_et_email);
        etSecuritycode = findViewById(R.id.resetpassword_et_securitycode);
        etNewpassword = findViewById(R.id.resetpassword_et_password);
        etSureNewpassword = findViewById(R.id.register_et_ensurepassword);
        ivBacktrack = findViewById(R.id.resetpassword_backtrack);
        tvSecuritycode = findViewById(R.id.resetpassword_tv_securitycode);
        btnAgree = findViewById(R.id.resetpassword_btn_argee);

        ivBacktrack.setOnClickListener(this);
        tvSecuritycode.setOnClickListener(this);
        btnAgree.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.resetpassword_backtrack:
                startActivity(new Intent(this,SignInActivity.class));
                break;
            case R.id.resetpassword_tv_securitycode:

                break;
            case R.id.resetpassword_btn_argee:

                break;
        }
    }
}