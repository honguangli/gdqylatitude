package com.liguanghong.gdqylatitude.activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.AppManager;
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

import static com.liguanghong.gdqylatitude.util.ImageUtils.getPicFromBytes;

public class UserInfoMineActivity extends BaseActivity implements View.OnClickListener{

    private EditText etUsername,etPassword,etSex,etRealname,etPhone,etEmail;
    private ImageView ivBacktrack,ivTouxiang;
    private TextView tvEdit,tvSure;
    private static Handler userinfoHandler;
    private LoadingDialog dialog;
    private String headPic;
    private int resultCode = AppManager.NOTCHANGE;

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
        etPassword = findViewById(R.id.personalinfo_et_password);
        etSex = findViewById(R.id.personalinfo_et_sex);
        etRealname = findViewById(R.id.personalinfo_et_realname);
        etPhone = findViewById(R.id.personalinfo_et_phone);
        etEmail = findViewById(R.id.personalinfo_et_email);
        ivBacktrack = findViewById(R.id.personalinfo_backtrack);
        tvEdit = findViewById(R.id.personalinfo_edit);
        tvSure = findViewById(R.id.personalinfo_sure);
        ivTouxiang = findViewById(R.id.personalinfo_iv_touxiang);

        ivBacktrack.setOnClickListener(this);
        tvEdit.setOnClickListener(this);
        tvSure.setOnClickListener(this);

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

        //获取用户的个人信息
        byte[] b = Base64.decode(UserManager.getInstance().getAppUser().getHeadportrait(), Base64.DEFAULT);
        ivTouxiang.setImageBitmap(ImageUtils.getPicFromBytes(b,null));
        etUsername.setText(UserManager.getInstance().getAppUser().getLogname());
        etPassword.setText(UserManager.getInstance().getAppUser().getPassword());
        etSex.setText(UserManager.getInstance().getAppUser().getSex());
        etRealname.setText(UserManager.getInstance().getAppUser().getUsername());
        etPhone.setText(UserManager.getInstance().getAppUser().getPhone());
        etEmail.setText(UserManager.getInstance().getAppUser().getEmail());
        headPic = ImageUtils.bitmapToString(getPicFromBytes(b,null));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.personalinfo_backtrack:
                setResult(resultCode);
                finish();
                break;
            case R.id.personalinfo_edit:
                ivTouxiang.setOnClickListener(this);
                tvEdit.setVisibility(View.INVISIBLE);
                tvSure.setVisibility(View.VISIBLE);
                editTextEnabled(true, Color.BLACK);
                break;
            case R.id.personalinfo_sure:
                if (checkUpInfo()){
                    dialog = new LoadingDialog(this,"玩命更新中...");
                    dialog.show();
                    ivTouxiang.setOnClickListener(null);
                    tvEdit.setVisibility(View.VISIBLE);
                    tvSure.setVisibility(View.INVISIBLE);
                    update(etUsername.getText().toString().trim(),
                            etPassword.getText().toString().trim(),
                            etSex.getText().toString().trim(),
                            etRealname.getText().toString().trim(),
                            etPhone.getText().toString().trim(),
                            etEmail.getText().toString().trim(),
                            headPic);
                    editTextEnabled(false, Color.parseColor("#BFBFBF"));
                }
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
    private void update(String logname, String password, String sex, String username, String phone, String email,String headportrait){
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", UserManager.getInstance().getAppUser().getUserid() + "")
                .add("logname",logname)
                .add("password", password)
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
                message.obj = "无法连接服务器";
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
                            String password = UserManager.getInstance().getAppUser().getPassword();
                            UserManager.getInstance().addAppUser(user);
                            userinfoHandler.sendEmptyMessage(200);
                        } else{
                            //更新失败
                            Message message = new Message();
                            message.what = 500;
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
    }

    //检查更新的个人信息是否符合要求
    private boolean checkUpInfo(){
        if (etUsername.getText().toString().trim().equals("")){
            Toast.makeText(this,"用户名不能为空",Toast.LENGTH_LONG).show();
            return false;
        }else if (etSex.getText().toString().trim().equals("")){
            Toast.makeText(this,"性别不能为空",Toast.LENGTH_LONG).show();
            return false;
        }else if (!((etSex.getText().toString().trim().equals("男"))||(etSex.getText().toString().trim().equals("女")))){
            Toast.makeText(this,"性别只能是男或女",Toast.LENGTH_LONG).show();
            return false;
        }else if (etRealname.getText().toString().trim().equals("")){
            Toast.makeText(this,"真是姓名不能为空",Toast.LENGTH_LONG).show();
            return false;
        }else if (etPhone.getText().toString().trim().equals("")){
            Toast.makeText(this,"手机号不能为空",Toast.LENGTH_LONG).show();
            return false;
        }else if (!CheckCellphoneEmailUtil.checkCellphone(etPhone.getText().toString().trim())){
            Toast.makeText(this,"该手机号不存在",Toast.LENGTH_LONG).show();
            return false;
        }else if (etEmail.getText().toString().trim().equals("")){
            Toast.makeText(this,"邮箱不能为空",Toast.LENGTH_LONG).show();
            return false;
        }else if (!CheckCellphoneEmailUtil.checkEmail(etEmail.getText().toString().trim())){
            Toast.makeText(this,"邮箱格式错误",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;

    }

    /**
     * 设置editTest是否可编辑和字体颜色
     *
     * @param enabled   是否可编辑
     * @param textcolor 字体颜色
     */
    private void editTextEnabled(boolean enabled,int textcolor){
        etUsername.setEnabled(enabled);
        etSex.setEnabled(enabled);
        etRealname.setEnabled(enabled);
        etPhone.setEnabled(enabled);
        etEmail.setEnabled(enabled);

        etUsername.setTextColor(textcolor);
        etSex.setTextColor(textcolor);
        etRealname.setTextColor(textcolor);
        etPhone.setTextColor(textcolor);
        etEmail.setTextColor(textcolor);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10 && resultCode == 20) {
            String photo = (String) data.getExtras().get("photo");
            ivTouxiang.setImageURI(Uri.parse(photo));
            headPic = ImageUtils.filePathToString(photo);
        }
    }

}
