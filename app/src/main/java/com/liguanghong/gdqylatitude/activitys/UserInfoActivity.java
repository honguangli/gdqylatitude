package com.liguanghong.gdqylatitude.activitys;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.DialogFriendsSetAdapter;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.FriendsManager;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.unity.Friend;
import com.liguanghong.gdqylatitude.unity.MessageType;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.util.DensityUtil;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.JsonResult;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView backtrack_friend_info;                    //返回聊天界面
    private ImageView menu;                                       //菜单
    private ImageView iv_information_icon;                      //好友头像

    private TextView tv_information_name;                       //好友名称
    private TextView tv_information_id;                         //好友id
    private TextView tv_information_markname;                   //好友备注
    private TextView tv_information_groupname;                  //好友所在分组
    private TextView tv_information_city;                       //地区
    private TextView tv_information_signature;                  //个性签名

    private Button bt_information_send;                         //发消息

    private RelativeLayout rly_beizhu,rly_fenzu;
    private LinearLayout lv_remark_and_set;

    private Friend friend;

    //分组
    private ListView dialog_lv;
    private TextView tv_delete;
    private Dialog bottomDialog;
    private View groupchangeView;
    private View menuView;

    private DialogFriendsSetAdapter dialogFriendsSetAdapter;
    private Handler userInfoHandler;
    private boolean hide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void initView() {
        menu = findViewById(R.id.menu);
        backtrack_friend_info = findViewById(R.id.backtrack);
        iv_information_icon = findViewById(R.id.iv_information_icon);

        tv_information_name = findViewById(R.id.tv_information_name);
        tv_information_markname = findViewById(R.id.tv_information_markname);
        tv_information_groupname = findViewById(R.id.tv_information_groupname);
        tv_information_city = findViewById(R.id.tv_information_city);
        tv_information_signature = findViewById(R.id.tv_information_signature);

        bt_information_send = findViewById(R.id.bt_information_send);

        rly_beizhu = findViewById(R.id.rly_beizhu);
        rly_fenzu = findViewById(R.id.rly_fenzu);
        lv_remark_and_set = findViewById(R.id.lv_remark_and_set);

        backtrack_friend_info.setOnClickListener(this);
        menu.setOnClickListener(this);
        rly_beizhu.setOnClickListener(this);
        rly_fenzu.setOnClickListener(this);
        bt_information_send.setOnClickListener(this);

    }

    @Override
    protected void initData() {

        userInfoHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 1://更改好友分组
                        tv_information_groupname.setText(FriendsManager.getFriendsSetNameByID(friend.getFriend().getUserid()));
                        dialogFriendsSetAdapter.notifyDataSetChanged();
                        break;
                    case 2://更改好友备注
                        tv_information_markname.setText(FriendsManager.getFriendByID(friend.getFriendid()).getRemark() == null ? "还没备注？点我设置" : FriendsManager.getFriendByID(friend.getFriendid()).getRemark());
                        break;
                    case 3://添加好友
                        bottomDialog.cancel();
                        tips("已发送申请");
                        break;
                    case 4://删除好友
                        bottomDialog.cancel();
                        finish();
                        break;
                }
            }
        };
        bottomDialog = new Dialog(this, R.style.BottomDialog);

        menuView = LayoutInflater.from(this).inflate(R.layout.dialog_friends_delete, null);
        tv_delete = menuView.findViewById(R.id.tv_delete);
        tv_delete.setOnClickListener(this);

        hide = this.getIntent().getBooleanExtra("hide", false);
        if(hide){
            //陌生人
            User user = (User)this.getIntent().getSerializableExtra("userinfo");
            friend = new Friend();
            friend.setFriendid(user.getUserid());
            friend.setFriend(user);
            lv_remark_and_set.setVisibility(View.GONE);
            bt_information_send.setVisibility(View.GONE);
            tv_delete.setText("添加为好友");
        } else{
            //好友
            friend = (Friend)this.getIntent().getSerializableExtra("userinfo");

            tv_information_markname.setText(friend.getRemark() == null ? "还没备注？点我设置" : friend.getRemark());
            tv_information_groupname.setText(FriendsManager.getFriendsSetNameByID(friend.getFriend().getUserid()));

            dialogFriendsSetAdapter = new DialogFriendsSetAdapter(getApplicationContext(), friend.getFriend().getUserid());
            dialogFriendsSetAdapter.notifyDataSetChanged();

            groupchangeView = LayoutInflater.from(this).inflate(R.layout.dialog_friends_set_change, null);
            dialog_lv = groupchangeView.findViewById(R.id.dialog_lv);
            dialog_lv.setAdapter(dialogFriendsSetAdapter);
            dialog_lv.setOnItemClickListener(this);
        }

        tv_information_name.setText(friend.getFriend().getLogname());



    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.rly_beizhu:              //修改备注
                showRemark();
                break;

            case R.id.menu:                     //菜单
                show(menuView, false);
                break;

            case R.id.rly_fenzu:                //修改分组名
                show(groupchangeView, true);
                break;

            case R.id.bt_information_send:
                //返回聊天界面
                finish();
                break;

            case R.id.backtrack:
                //销毁当前界面，返回上一界面
                finish();
                break;

            case R.id.tv_delete:
                //删除好友
                if(hide){
                    friendNotice();
                } else{
                    deleteFriend();
                }
                break;

        }
    }

    private void tips(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT);
    }

    //更改好友分组/删除好友/添加好友窗口
    private void show(View contentView, boolean setHeight) {
        //获取屏幕宽高
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        bottomDialog.setContentView(contentView);

        //设置View宽高和边距
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = size.x - DensityUtil.dp2px(this, 16f);
        if(setHeight) {
            View mView = dialogFriendsSetAdapter.getView(0, null, dialog_lv);    //得到每项的界面view
            mView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)); //得到一个view的大小
            params.height = dialogFriendsSetAdapter.getCount() > 5 ?
                    mView.getMeasuredHeight() * 5 + DensityUtil.dp2px(this, 16f)
                    : mView.getMeasuredHeight() * dialogFriendsSetAdapter.getCount() + DensityUtil.dp2px(this, 16f);
        }
        params.bottomMargin = DensityUtil.dp2px(this, 8f);
        contentView.setLayoutParams(params);

        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();

    }

    //更改好友备注窗口
    private void showRemark() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_friends_set_remark,null,false);
        final AlertDialog alertdialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(true)
                .show();
        TextView surebutton = view.findViewById(R.id.sure);
        TextView cancelbutton = view.findViewById(R.id.cancel);
        final EditText et_content = view.findViewById(R.id.et_content);

        surebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remark = et_content.getText().toString().trim();
                if(remark != null){
                    changeFriendRemark(remark);
                    alertdialog.cancel();
                }
            }
        });
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertdialog.cancel();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        changeFriendsSet(FriendsManager.getFriendsSetNameByID(friend.getFriend().getUserid()), FriendsManager.getFriendsSetNameList().get(i));
    }

    /**
     * 更改好友所在分组操作
     * @param fromSetName
     * @param toSetName
     */
    private void changeFriendsSet(final String fromSetName, final String toSetName){
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", UserManager.getAppUser().getUserid() + "")
                .add("targetid", friend.getFriendid()+"")
                .add("fromSetName", fromSetName)
                .add("toSetName", toSetName)
                .build();
        HttpUtil.postEnqueue("user/changefriendsset", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("好友分组管理", "移动好友所在分组失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        if(result.isSuccess()){
                            FriendsManager.changeFriendsSet(friend.getFriendid(), fromSetName, toSetName);
                            userInfoHandler.sendEmptyMessage(1);
                        } else{

                        }
                        Log.i("更改好友分组操作",  result.isSuccess() + "," + result.getMessage());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    /**
     * 更改好友备注操作
     */
    private void changeFriendRemark(final String remark){
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", UserManager.getAppUser().getUserid() + "")
                .add("targetid", friend.getFriendid() + "")
                .add("remark", remark)
                .build();
        HttpUtil.postEnqueue("user/changefriendremark", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("好友管理", "更改好友备注失败");
            }

            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        if(result.isSuccess()){
                            FriendsManager.setFriendRemark(friend.getFriendid(), remark);
                            userInfoHandler.sendEmptyMessage(2);
                        } else{

                        }
                        Log.i("好友管理",  result.isSuccess() + "," + result.getMessage());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    /**
     * 好友申请
     */
    private void friendNotice(){
        RequestBody requestBody = new FormBody.Builder()
                .add("noticetype", MessageType.FRIENDAPPLY + "")
                .add("senderid", UserManager.getAppUser().getUserid() + "")
                .add("receiverid", friend.getFriendid() + "")
                .add("status", 1+"")
                .build();
        HttpUtil.postEnqueue("notice/createnotice", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("通知管理", "发送好友申请失败");
            }

            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        if(result.isSuccess()){
                            userInfoHandler.sendEmptyMessage(3);
                        } else{

                        }
                        Log.i("通知管理",  result.isSuccess() + "," + result.getMessage());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    /**
     * 添加好友操作
     */
    private void addFriend(){
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", UserManager.getAppUser().getUserid() + "")
                .add("targetuserid", friend.getFriendid() + "")
                .build();
        HttpUtil.postEnqueue("user/addfriend", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("好友管理", "添加好友失败");
            }

            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        if(result.isSuccess()){
                            FriendsManager.addFriend(friend);
                            userInfoHandler.sendEmptyMessage(3);
                        } else{

                        }
                        Log.i("好友管理",  result.isSuccess() + "," + result.getMessage());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    /**
     * 删除好友操作
     */
    private void deleteFriend(){
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", UserManager.getAppUser().getUserid() + "")
                .add("targetuserid", friend.getFriendid() + "")
                .build();
        HttpUtil.postEnqueue("user/deletefriend", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("好友管理", "删除好友失败");
            }

            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        if(result.isSuccess()){
                            FriendsManager.deleteFriend(friend.getFriendid());
                            userInfoHandler.sendEmptyMessage(4);
                        } else{

                        }
                        Log.i("好友管理",  result.isSuccess() + "," + result.getMessage());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }
}
