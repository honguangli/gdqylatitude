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

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.util.DensityUtil;

import java.util.ArrayList;

public class GroupSignManageAdapter extends BaseAdapter implements View.OnClickListener {

    private ArrayList<String> list;
    private Context context;

    private int index;

    Dialog bottomDialog;

    public GroupSignManageAdapter(Context context,ArrayList<String> list){
        this.context = context;
        this.list = list;

        createDialog();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_group_sign_manage,null);
        TextView group_name = v.findViewById(R.id.group_name);
        ImageView img_movegroup = v.findViewById(R.id.img_movegroup);

        group_name.setText(list.get(i));

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
        tv_delete.setText("删除签到");

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
                break;
        }
    }
}
