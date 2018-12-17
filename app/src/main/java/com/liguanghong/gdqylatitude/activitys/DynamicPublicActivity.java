package com.liguanghong.gdqylatitude.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.DynamicPublicAdpter;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.AppManager;
import com.liguanghong.gdqylatitude.manager.FriendsManager;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.ImageUtils;
import com.liguanghong.gdqylatitude.util.JsonResult;
import com.liguanghong.gdqylatitude.view.LoadingDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DynamicPublicActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView backtrack;
    private TextView menu;
    private EditText et_send_messges;

    private GridView gridView;
    private ArrayList<String> photo;
    private DynamicPublicAdpter dynamicPublicAdpter;

    private static Handler userinfoHandler;
    private LoadingDialog dialog;
    private int resultCode = AppManager.NOTCHANGE;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dynamic_public;
    }

    @Override
    protected void initView() {
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE); //获取editor对象
        editor = sharedPreferences.edit();//获取编辑器

        backtrack = findViewById(R.id.backtrack);
        menu = findViewById(R.id.menu);
        et_send_messges = findViewById(R.id.et_send_messges);
        gridView = findViewById(R.id.gridView);

        backtrack.setOnClickListener(this);
        menu.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        userinfoHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 200:
                        dialog.setText("更新成功");
                        dialog.close();
                        resultCode = AppManager.SUCCESS;
                        break;
                    default:
                        dialog.setText(String.valueOf(message.obj));
                        dialog.close();
                        resultCode = AppManager.ERROR;
                        break;
                }
            }
        };
        photo = new ArrayList<String>();
        dynamicPublicAdpter = new DynamicPublicAdpter(photo, this);
        gridView.setAdapter(dynamicPublicAdpter);
        gridView.setOnItemClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 20) {
            List<String> str = (List<String>)data.getSerializableExtra("photo");
            photo.addAll(str);
            dynamicPublicAdpter.notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.menu:             //发表
                if (photo.size() != 0 || et_send_messges.getText().toString().length() != 0){
                    dialog = new LoadingDialog(this,"玩命发送中...");
                    dialog.show();
                    if (photo.size()==0){
                        SendDynamic(et_send_messges.getText().toString().trim(),
                                "",
                                "",
                                "");
                    }else if (photo.size()==1){
                        SendDynamic(et_send_messges.getText().toString().trim(),
                                ImageUtils.filePathToString(photo.get(0)),
                                "",
                                "");
                    }else if (photo.size()==2){
                        SendDynamic(et_send_messges.getText().toString().trim(),
                                ImageUtils.filePathToString(photo.get(0)),
                                ImageUtils.filePathToString(photo.get(1)),
                                "");
                    }else if (photo.size()==3){
                        SendDynamic(et_send_messges.getText().toString().trim(),
                                ImageUtils.filePathToString(photo.get(0)),
                                ImageUtils.filePathToString(photo.get(1)),
                                ImageUtils.filePathToString(photo.get(2)));
                    }
                    finish();
                }

                break;

            case R.id.backtrack:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (photo.size() == i){
            SelectPhotoActivity.isMultiSelect = false;
            editor.putBoolean("isMultiSelect",SelectPhotoActivity.isMultiSelect);
            editor.commit();

            Intent intent = new Intent(DynamicPublicActivity.this,SelectPhotoActivity.class);
            intent.putExtra("size", photo.size());
            startActivityForResult(intent,10);
        }
    }

    /**
     * 发送动态
     */
    private void SendDynamic(String messges,String pic,String pic2,String pic3){
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", UserManager.getInstance().getAppUser().getUserid() + "")
                .add("text",messges)
                .add("pic",pic)
                .add("pic2",pic2)
                .add("pic3",pic3)
                .build();

        HttpUtil.postEnqueue("dynamic/public", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("发送操作",  "发送操作失败");
                Message message = new Message();
                message.what = 404;
                message.obj = "发送失败！无法连接到服务器";
                userinfoHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        if(result.isSuccess()){
                            //发送成功
                            userinfoHandler.sendEmptyMessage(200);
                        } else{
                            //发送失败
                            Message message = new Message();
                            message.what = 500;
                            message.obj = result.getMessage();
                            userinfoHandler.sendMessage(message);
                        }
                        Log.i("发送动态",  result.isSuccess() + "," + result.getMessage());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }
}
