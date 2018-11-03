package com.liguanghong.gdqylatitude.activitys;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.util.DensityUtil;

public class GroupInfoActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backtrack;                                //返回群聊界面
    private ImageView menu;                                     //菜单
    private TextView group_name;                             //群名称
    private TextView group_id;                                  //群id
    private TextView group_messges;                         //群公告
    private TextView invite;                                    //邀请
    private RelativeLayout rly_group_all;                       //查看所有成员
    private RelativeLayout rly_manage;                      //群管理


    private TextView tv_share;
    private TextView tv_delete;
    private TextView tv_cancel;
    Dialog bottomDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_info;
    }

    @Override
    protected void initView() {
        backtrack = (ImageView)findViewById(R.id.backtrack);
        menu = (ImageView)findViewById(R.id.menu);
        group_name = (TextView)findViewById(R.id.group_name);
        group_id = (TextView)findViewById(R.id.group_name);
        group_messges = (TextView)findViewById(R.id.group_name);
        //invite = (TextView)findViewById(R.id.group_name);
        rly_group_all = (RelativeLayout)findViewById(R.id.rly_group_all);
        rly_manage = (RelativeLayout)findViewById(R.id.rly_manage);

        backtrack.setOnClickListener(this);
        menu.setOnClickListener(this);
        //invite.setOnClickListener(this);
        rly_group_all.setOnClickListener(this);
        rly_manage.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.rly_manage:
                //启动群管理界面
                startActivity(new Intent(getApplicationContext(), GroupManageActivity.class));
                break;

            case R.id.rly_group_all:            //查看群成员

                break;

            case R.id.menu:                         //菜单
                show();
                break;
        /*
            case R.id.invite:                   //邀请

                break;
*/
            case R.id.backtrack:
                //销毁当前界面，返回群聊天界面
                finish();
                break;


            /*
            分享，加入/退出群聊
             */
            case R.id.tv_share:                  //分享群聊

                break;

            case R.id.tv_delete:                //加入或退出群聊

                break;

            case R.id.tv_cancel:                //取消
                bottomDialog.dismiss();
                break;
        }
    }

    private void show() {
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_friends_delete, null);
        bottomDialog.setContentView(contentView);

        tv_share = (TextView)contentView.findViewById(R.id.tv_share);
        tv_delete = (TextView)contentView.findViewById(R.id.tv_delete);
        tv_cancel = (TextView)contentView.findViewById(R.id.tv_cancel);
        tv_delete.setText("加入群聊");
        tv_delete.setTextColor(0xFF25C6FC);
        tv_share.setVisibility(View.VISIBLE);
        tv_cancel.setVisibility(View.VISIBLE);

        tv_share.setOnClickListener(this);
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
