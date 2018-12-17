package com.liguanghong.gdqylatitude.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.activitys.ChatActivity;
import com.liguanghong.gdqylatitude.activitys.DynamicFriendsActivity;
import com.liguanghong.gdqylatitude.activitys.DynamicMineActivity;
import com.liguanghong.gdqylatitude.activitys.GuardActivity;
import com.liguanghong.gdqylatitude.activitys.SettingActivity;
import com.liguanghong.gdqylatitude.activitys.UserInfoActivity;
import com.liguanghong.gdqylatitude.activitys.UserInfoMineActivity;
import com.liguanghong.gdqylatitude.base.BaseFragment;
import com.liguanghong.gdqylatitude.manager.AppManager;
import com.liguanghong.gdqylatitude.manager.FriendsManager;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.unity.Friend;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.ImageUtils;
import com.liguanghong.gdqylatitude.util.JsonResult;
import com.liguanghong.gdqylatitude.view.LoadingDialog;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MineFragment extends BaseFragment implements View.OnClickListener {

    private RelativeLayout personInfo;
    private RelativeLayout dyanamicFriends;
    private RelativeLayout dynamicMine;
    private RelativeLayout guard;
    private RelativeLayout setting;
    private AlertDialog.Builder customizeDialog;
    private AlertDialog dialog;
    private EditText et_name;
    private EditText et_password;
    private Button sure;
    private Button cancle;
    private LoadingDialog loadingDialog;

    private ImageView personal_head;
    private TextView personal_name;

    private Handler mineHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutId(){
        return R.layout.fragment_mine;
    }

    //初始化组件
    @Override
    protected void initView(View view)
    {
        personInfo = view.findViewById(R.id.personal_info);
        dyanamicFriends = view.findViewById(R.id.dynamic_friends);
        dynamicMine = view.findViewById(R.id.dynamic_mine);
        guard = view.findViewById(R.id.guard);
        setting = view.findViewById(R.id.setting);
        personal_head = view.findViewById(R.id.personal_head);
        personal_name = view.findViewById(R.id.personal_name);

        personInfo.setOnClickListener(this);
        dyanamicFriends.setOnClickListener(this);
        dynamicMine.setOnClickListener(this);
        guard.setOnClickListener(this);
        setting.setOnClickListener(this);

        customizeDialog =
                new AlertDialog.Builder(MineFragment.this.getContext());
        View dialogView = LayoutInflater.from(MineFragment.this.getContext())
                .inflate(R.layout.dialog_guard,null);
        customizeDialog.setView(dialogView);
        dialog = customizeDialog.create();

        et_name = dialogView.findViewById(R.id.et_name);
        et_password = dialogView.findViewById(R.id.et_password);
        sure = dialogView.findViewById(R.id.btn_sure);
        cancle = dialogView.findViewById(R.id.btn_cancel);
        sure.setOnClickListener(this);
        cancle.setOnClickListener(this);
    }

    //初始化数据
    @Override
    protected void initData()
    {

        mineHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 200:
                        loadingDialog.setText("更新成功");
                        loadingDialog.close();
                        Intent intent = new Intent(MineFragment.this.getContext(), GuardActivity.class);
                        intent.putExtra("entityName", "gps_id_"+message.obj);
                        startActivity(intent);
                        break;
                    case 400:
                        loadingDialog.setText("验证失败");
                        loadingDialog.close();
                        break;
                }
            }
        };

        refreshUserInfo();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.personal_info:
                startActivityForResult(new Intent(getActivity(), UserInfoMineActivity.class), AppManager.UPDATEUSER);
                break;
            case R.id.dynamic_friends:
                startActivity(new Intent(getActivity(), DynamicFriendsActivity.class));
                break;
            case R.id.dynamic_mine:
                startActivity(new Intent(getActivity(), DynamicMineActivity.class));
                break;
            case R.id.guard:
                dialog.show();
                break;
            case R.id.setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.btn_sure:
                if(et_name.getText().toString().trim() != null && et_password.getText().toString().trim() != null) {
                    check(et_name.getText().toString(), et_password.getText().toString());
                    dialog.dismiss();
                    loadingDialog = new LoadingDialog(MineFragment.this.getContext(),"玩命更新中...");
                    loadingDialog.show();
                    et_name.setText("");
                    et_password.setText("");
                }
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppManager.UPDATEUSER && resultCode == AppManager.SUCCESS) {
            //更新了用户信息
            refreshUserInfo();
        }
    }

    //更新用户信息
    private void refreshUserInfo(){
        personal_name.setText(UserManager.getInstance().getAppUser().getLogname());

        byte[] b = android.util.Base64.decode(UserManager.getInstance().getAppUser().getHeadportrait(), android.util.Base64.DEFAULT);
        personal_head.setImageBitmap(ImageUtils.getPicFromBytes(b,null));
    }

    private void check(String logname, String password){
        RequestBody requestBody = new FormBody.Builder()
                .add("logname",logname)
                .add("password", password)
                .build();
        HttpUtil.postEnqueue("user/signin", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("验证操作",  "验证连接失败");
                Message message = new Message();
                message.what = 400;
                message.obj = "验证连接失败";
                mineHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        if(result.isSuccess()){
                            //验证成功
                            Message message = new Message();
                            JSONObject object = (JSONObject)result.getData();
                            User user = JSONObject.parseObject(object.getString("user"), User.class);
                            message.what = 200;
                            message.obj = user.getUserid();
                            mineHandler.sendMessage(message);
                        } else{
                            //验证失败
                            mineHandler.sendEmptyMessage(400);
                        }
                        Log.i("验证操作",  result.isSuccess() + "," + result.getMessage());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

}
