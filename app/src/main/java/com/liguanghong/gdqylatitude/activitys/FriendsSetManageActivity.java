package com.liguanghong.gdqylatitude.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.FriendsSetManageAdapter;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.FriendsManager;
import com.liguanghong.gdqylatitude.unity.Friend;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.JsonResult;
import com.liguanghong.gdqylatitude.manager.UserManager;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FriendsSetManageActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backtrack_fenzu;
    private TextView done;
    private RelativeLayout rly_addgroup;
    private ListView lv;

    private FriendsSetManageAdapter friendsSetManageAdapter;
    private static Handler friendsSetManageHandler;

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

        done = (TextView) findViewById(R.id.done);
        rly_addgroup = (RelativeLayout) findViewById(R.id.rly_addgroup);


        backtrack_fenzu.setOnClickListener(this);
        done.setOnClickListener(this);
        rly_addgroup.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        friendsSetManageHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 200:
                        if(friendsSetManageAdapter != null)
                            friendsSetManageAdapter.notifyDataSetChanged();
                        break;
                }
            }
        };
        lv = findViewById(R.id.lv);
        friendsSetManageAdapter = new FriendsSetManageAdapter(this);
        lv.setAdapter(friendsSetManageAdapter);
        friendsSetManageAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.backtrack:      //返回通讯录
                finish();
                break;

            case R.id.done:                  //完成分组管理
                finish();
                break;

            case R.id.rly_addgroup:         //添加分组
                showDialog();
                break;

        }
    }

    /**
     * 获取handle
     * @return
     */
    public static Handler getFriendsSetManageHandler(){
        return friendsSetManageHandler;
    }

    /**
     * 打开添加分组弹窗
     */
    public void showDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_friends_set_remark,null,false);
        final AlertDialog alertdialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(true)
                .show();
        TextView title = view.findViewById(R.id.title);
        TextView surebutton = view.findViewById(R.id.sure);
        TextView cancelbutton = view.findViewById(R.id.cancel);

        final EditText et_content = view.findViewById(R.id.et_content);

        title.setText("新建分组");
        et_content.setHint("请输入新增分组名称");

        surebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setName = et_content.getText().toString();
                if(setName.trim() != null){
                    addFriendsSet(setName.trim());
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

    /**
     * 创建好友分组操作
     * @param setName
     */
    private void addFriendsSet(final String setName){
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", UserManager.getInstance().getAppUser().getUserid() + "")
                .add("setName", setName)
                .build();
        HttpUtil.postEnqueue("user/createfriendsset", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("好友分组管理", "创建好友分组失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        if(result.isSuccess()){
                            FriendsManager.getInstance().addFriendsSet(setName, new ArrayList<Friend>());
                        } else{

                        }
                        Log.i("好友分组管理",  result.isSuccess() + "," + result.getMessage());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

}
