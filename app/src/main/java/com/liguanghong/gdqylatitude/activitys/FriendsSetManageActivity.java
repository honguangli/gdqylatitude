package com.liguanghong.gdqylatitude.activitys;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.util.DensityUtil;

public class FriendsSetManageActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backtrack_fenzu;
    private ImageView img_movegroup;

    private TextView done;

    private RelativeLayout rly_addgroup;

    private TextView tv_delete;
    private TextView tv_cancel;
    Dialog bottomDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_friends_set_manage;
    }

    @Override
    protected void initView() {
        backtrack_fenzu = (ImageView)findViewById(R.id.backtrack);
        img_movegroup = (ImageView)findViewById(R.id.img_movegroup);

        done = (TextView) findViewById(R.id.done);
        rly_addgroup = (RelativeLayout) findViewById(R.id.rly_addgroup);

        backtrack_fenzu.setOnClickListener(this);
        img_movegroup.setOnClickListener(this);
        done.setOnClickListener(this);
        rly_addgroup.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.backtrack:      //返回通讯录
                setResult(10);
                finish();
                break;

            case R.id.done:                  //完成分组管理

                break;

            case R.id.rly_addgroup:         //添加分组

                /*
                弹出dialog
                填入新建分组名
                点击确定
                新建分组成功
                 */

                break;

            case R.id.img_movegroup:        //删除已有的分组
                show();
                break;

            /*
            删除已有分组
             */
            case R.id.tv_delete:        //删除已有的分组
                Toast.makeText(getApplicationContext(),"删除",Toast.LENGTH_LONG).show();
                break;

            case R.id.tv_cancel:        //取消
                bottomDialog.dismiss();
                break;
        }
    }

    private void show() {
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_friends_delete, null);
        bottomDialog.setContentView(contentView);

        tv_delete = (TextView)contentView.findViewById(R.id.tv_delete);
        tv_cancel = (TextView)contentView.findViewById(R.id.tv_cancel);
        tv_delete.setText("删除分组");
        tv_cancel.setVisibility(View.VISIBLE);

        tv_delete.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(this, 16f);
        params.bottomMargin = DensityUtil.dp2px(this, 8f);
        contentView.setLayoutParams(params);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }
}
