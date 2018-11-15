package com.liguanghong.gdqylatitude.adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.manager.FriendsManager;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.util.DensityUtil;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.JsonResult;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FriendsSetManageAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private int index;

    Dialog bottomDialog;

    public FriendsSetManageAdapter(Context context){
        this.context = context;

        createDialog();
    }

    @Override
    public int getCount() {
        return FriendsManager.getFriendsSetNameList().size();
    }

    @Override
    public Object getItem(int i) {
        return FriendsManager.getFriendsSetNameList().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_friends_set_manage,null);
        TextView group_name = v.findViewById(R.id.group_name);
        ImageView img_movegroup = v.findViewById(R.id.img_movegroup);

        group_name.setText(FriendsManager.getFriendsSetNameList().get(i));

        img_movegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = i;
                bottomDialog.show();
            }
        });

        return v;
    }

    public void createDialog() {
        bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this.context).inflate(R.layout.dialog_friends_delete, null);
        bottomDialog.setContentView(contentView);

        TextView tv_delete = (TextView)contentView.findViewById(R.id.tv_delete);
        tv_delete.setText("删除分组");

        tv_delete.setOnClickListener(this);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = this.context.getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(this.context, 16f);
        params.bottomMargin = DensityUtil.dp2px(this.context, 8f);
        contentView.setLayoutParams(params);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_delete:
                Log.i("ID", index + "");
                deleteFriendsSet(FriendsManager.getFriendsSetNameList().get(index));
                bottomDialog.cancel();
                break;
        }
    }


    /**
     * 删除好友分组操作
     * @param setName
     */
    private void deleteFriendsSet(final String setName){
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", UserManager.getAppUser().getUserid() + "")
                .add("setName", setName)
                .build();
        HttpUtil.postEnqueue("user/deletefriendsset", requestBody, new Callback() {
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
                            FriendsManager.deleteFriendsSet(setName);
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
