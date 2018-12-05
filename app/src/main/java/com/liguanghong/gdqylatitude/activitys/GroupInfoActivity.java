package com.liguanghong.gdqylatitude.activitys;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.unity.Groupchat;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.util.DensityUtil;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.ImageUtils;
import com.liguanghong.gdqylatitude.util.JsonResult;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GroupInfoActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backtrack;                                //返回群聊界面
    private ImageView menu;                                     //菜单
    private TextView group_name;                             //群名称
    private TextView group_id;                                  //群id
    private TextView group_memberNum;                           //群成员数
    private TextView group_messges;                         //群公告
    private RelativeLayout topPanel;
    private RelativeLayout rly_group_all;                       //查看所有成员
    private RelativeLayout rly_manage;                      //群管理
    private LinearLayout linear;                            //用户成员

    private TextView tv_share;
    private TextView tv_delete;
    private TextView tv_cancel;
    private Dialog bottomDialog;

    private Groupchat groupchat;
    private Handler groupInfoHandler;
    private Context context;

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
        backtrack = findViewById(R.id.backtrack);
        backtrack.setOnClickListener(this);
        menu = findViewById(R.id.menu);
        menu.setOnClickListener(this);
        group_name = findViewById(R.id.group_name);
        group_id = findViewById(R.id.group_id);
        group_memberNum = findViewById(R.id.tv_membernum);
        group_messges = findViewById(R.id.group_notice);
        linear = findViewById(R.id.linear);
        topPanel = findViewById(R.id.topPanel);
        rly_group_all = findViewById(R.id.RelativeLayout_number);
        rly_group_all.setOnClickListener(this);
        rly_manage = findViewById(R.id.rly_manage);
        rly_manage.setOnClickListener(this);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initData() {

        groupInfoHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 200:
                        List<User> list = (List<User>)message.obj;
                        for(User user : list)
                            showMemberView(linear, user);
                        showInviteView(linear);
                        break;
                }
            }
        };

        context = this;
        groupchat = (Groupchat)this.getIntent().getSerializableExtra("groupinfo");
        topPanel.setBackground(new BitmapDrawable(ImageUtils.getPicFromBytes(Base64.decode(groupchat.getHeadportrait(), Base64.DEFAULT),null)));
        group_name.setText("群聊名称：" + groupchat.getGroupname());
        group_id.setText("ID："+groupchat.getGroupid());
        group_memberNum.setText("共" + groupchat.getGroupnum() + "人");
        group_messges.setText(groupchat.getAnnouncement());

        if(groupchat.getOwnerid().equals(UserManager.getInstance().getAppUser().getUserid())){
            rly_manage.setVisibility(View.VISIBLE);
        } else{
            rly_manage.setVisibility(View.GONE);
        }
        setUserItem(groupchat.getGroupid(), 4);

    }

    private void setUserItem(Integer groupid, Integer size){
        RequestBody requestBody = new FormBody.Builder()
                .add("userid",UserManager.getInstance().getAppUser().getUserid() + "")
                .add("groupid", groupid + "")
                .add("size", size + "")
                .build();
        HttpUtil.postEnqueue("group/searchmember", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("查询群成员操作",  "连接失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        if(result.isSuccess()){
                            List<User> list = JSONArray.parseArray(result.getData().toString(), User.class);
                            Message message = new Message();
                            message.what = 200;
                            message.obj = list;
                            groupInfoHandler.sendMessage(message);
                        } else{

                        }
                        Log.i("查询群成员操作",  result.isSuccess() + "," + result.getMessage());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.rly_manage:
                //启动群管理界面
                Intent mintent = new Intent(this, GroupManageActivity.class);
                mintent.putExtra("groupchat", groupchat);
                startActivity(mintent);
                break;

            case R.id.RelativeLayout_number:
                //启动群成员界面
                Intent intent = new Intent(this, GroupMemberManageActivity.class);
                intent.putExtra("groupid", groupchat.getGroupid());
                intent.putExtra("permission", false);
                startActivity(intent);
                break;

            case R.id.menu:                         //菜单
                show();
                break;
            case R.id.backtrack:
                //销毁当前界面，返回群聊天界面
                finish();
                break;

            case R.id.tv_share:                  //分享群聊

                break;

            case R.id.tv_delete:                //加入或退出群聊

                break;

            case R.id.tv_cancel:                //取消
                bottomDialog.dismiss();
                break;
        }
    }

    private void showMemberView(ViewGroup parent, User user){
        View userItmeView = LayoutInflater.from(context).inflate(R.layout.item_group_member, null);
        CircleImageView itemHead = userItmeView.findViewById(R.id.item_user_head);
        itemHead.setImageBitmap(ImageUtils.getPicFromBytes(Base64.decode(user.getHeadportrait(), Base64.DEFAULT),null));
        TextView itemName = userItmeView.findViewById(R.id.item_user_logname);
        itemName.setText(user.getLogname());
        parent.addView(userItmeView);
    }

    private void showInviteView(ViewGroup parent){
        View inviteView = LayoutInflater.from(this).inflate(R.layout.item_group_user_invite, null);
        inviteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviteFriend();
            }
        });
        parent.addView(inviteView);
    }

    private void show() {
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_friends_delete, null);
        bottomDialog.setContentView(contentView);

        tv_share = contentView.findViewById(R.id.tv_share);
        tv_delete = contentView.findViewById(R.id.tv_delete);
        tv_cancel = contentView.findViewById(R.id.tv_cancel);
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

    /**
     * 邀请好友加入群聊
     */
    private void inviteFriend(){

    }

    /**
     * 加入群聊
     */
    private void joininGroup(){
        RequestBody requestBody = new FormBody.Builder()
                .add("userid",UserManager.getInstance().getAppUser().getUserid() + "")
                .add("groupid", groupchat.getGroupid() + "")
                .build();
        HttpUtil.postEnqueue("user/find", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("查询操作",  "登录连接失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        if(result.isSuccess()){
                            User user = JSONObject.parseObject(result.getData().toString(), User.class);
                            Message message = new Message();
                            message.what = 200;
                            message.obj = user;
                            groupInfoHandler.sendMessage(message);
                        } else{

                        }
                        Log.i("查询操作",  result.isSuccess() + "," + result.getMessage());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    /**
     * 退出群聊
     */
    private void quitGroup(){

    }

}
