package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.util.CheckCellphoneEmailUtil;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.ImageUtils;
import com.liguanghong.gdqylatitude.util.JsonResult;
import com.liguanghong.gdqylatitude.view.LoadingDialog;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserInfoMineActivity extends BaseActivity implements View.OnClickListener{

    private EditText etUsername,etSex,etRealname,etPhone,etEmail;
    private ImageView ivBacktrack,ivTouxiang;
    private TextView tvSure;
    private static Handler userinfoHandler;
    LoadingDialog dialog;
    String headPic;
    boolean checkup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info_mine;
    }

    @Override
    protected void initView() {
        etUsername = findViewById(R.id.personalinfo_et_username);
        etSex = findViewById(R.id.personalinfo_et_sex);
        etRealname = findViewById(R.id.personalinfo_et_realname);
        etPhone = findViewById(R.id.personalinfo_et_phone);
        etEmail = findViewById(R.id.personalinfo_et_email);
        ivBacktrack = findViewById(R.id.personalinfo_backtrack);
        tvSure = findViewById(R.id.personalinfo_sure);
        ivTouxiang = findViewById(R.id.personalinfo_iv_touxiang);

        //获取用户的个人信息
        byte[] b = android.util.Base64.decode(UserManager.getInstance().getAppUser().getHeadportrait(), android.util.Base64.DEFAULT);
        ivTouxiang.setImageBitmap(ImageUtils.getPicFromBytes(b,null));
        etUsername.setText(UserManager.getInstance().getAppUser().getLogname());
        etSex.setText(UserManager.getInstance().getAppUser().getSex());
        etRealname.setText(UserManager.getInstance().getAppUser().getUsername());
        etPhone.setText(UserManager.getInstance().getAppUser().getPhone());
        etEmail.setText(UserManager.getInstance().getAppUser().getEmail());


        ivBacktrack.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        ivTouxiang.setOnClickListener(this);
        dialog =new LoadingDialog(this,"玩命发送中...");

    }

    @Override
    protected void initData() {
        userinfoHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 200:
                        finish();
                        break;
                    default:
                        tip(String.valueOf(message.obj));
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.personalinfo_backtrack:
                finish();
                break;
            case R.id.personalinfo_sure:
                checkup = true;
                checkUpInfo();
                if (checkup){
                    //Toast.makeText(UserInfoMineActivity.this,"88",Toast.LENGTH_LONG).show();
                    dialog.show();
                    Log.i("dialog","已显示");
                    commitInfo(etUsername.getText().toString(),etSex.getText().toString(),etRealname.getText().toString(),etPhone.getText().toString(),etEmail.getText().toString(),headPic);
                }
                //dialog.close();
                break;
            case R.id.personalinfo_iv_touxiang:
                Intent photo=new Intent(UserInfoMineActivity.this,SelectPhotoActivity.class);
                startActivityForResult(photo,10);
                break;
        }
    }

    /**
     * 修改用户信息
     */
    private void commitInfo(final String logname, String sex, String username, String phone, String email,String headportrait){
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", UserManager.getInstance().getAppUser().getUserid() + "")
                .add("logname",logname)
                .add("sex", sex)
                .add("username",username)
                .add("phone",phone)
                .add("email",email)
                .add("headportrait",headportrait)
                .build();
        HttpUtil.postEnqueue("user/update", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("更新操作",  "更新操作失败");
                Message message = new Message();
                message.what = 404;
                message.obj = "更新操作失败";
                userinfoHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        if(result.isSuccess()){
                            //更新成功
                            User user = ((JSONObject)result.getData()).toJavaObject(User.class);
                            UserManager.getInstance().addAppUser(user);

                        } else{
                            //更新失败
                            Message message = new Message();
                            message.what = 0;
                            message.obj = result.getMessage();
                            userinfoHandler.sendMessage(message);
                        }
                        Log.i("修改个人信息",  result.isSuccess() + "," + result.getMessage());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dialog.close();
    }

    //检查更新的个人信息是否符合要求
    private void checkUpInfo(){
        if (etUsername.getText().toString().trim().equals("")){
            checkup = false;
            Toast.makeText(UserInfoMineActivity.this,"用户名不能为空",Toast.LENGTH_LONG).show();
        }else if (etSex.getText().toString().trim().equals("")){
            checkup = false;
            Toast.makeText(UserInfoMineActivity.this,"性别不能为空",Toast.LENGTH_LONG).show();
        }else if (!((etSex.getText().toString().trim().equals("男"))||(etSex.getText().toString().trim().equals("女")))){
            checkup = false;
            Toast.makeText(UserInfoMineActivity.this,"性别只能是男或女",Toast.LENGTH_LONG).show();
        }else if (etRealname.getText().toString().trim().equals("")){
            checkup = false;
            Toast.makeText(UserInfoMineActivity.this,"真是姓名不能为空",Toast.LENGTH_LONG).show();
        }else if (etPhone.getText().toString().trim().equals("")){
            checkup = false;
            Toast.makeText(UserInfoMineActivity.this,"手机号不能为空",Toast.LENGTH_LONG).show();
        }else if (!CheckCellphoneEmailUtil.checkCellphone(etPhone.getText().toString().trim())){
            checkup = false;
            Toast.makeText(UserInfoMineActivity.this,"该手机号不存在",Toast.LENGTH_LONG).show();
        }else if (etEmail.getText().toString().trim().equals("")){
            checkup = false;
            Toast.makeText(UserInfoMineActivity.this,"邮箱不能为空",Toast.LENGTH_LONG).show();
        }else if (!CheckCellphoneEmailUtil.checkEmail(etEmail.getText().toString().trim())){
            checkup = false;
            Toast.makeText(UserInfoMineActivity.this,"邮箱格式错误",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == 20) {
            String photo = (String) data.getExtras().get("photo");
            ivTouxiang.setImageURI(Uri.parse(photo));
            headPic = ImageUtils.filePathToString(photo);
        }
    }

    /**
     *修改成功返回主页面
     */
    private void navigateToHome(){
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    /**
     * 修改失败
     */
    private void tip(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 更新本地用户信息存储
     */

}
