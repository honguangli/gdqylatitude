package com.liguanghong.gdqylatitude.adapter;

import android.app.Dialog;
import android.content.Context;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.util.DensityUtil;
import com.liguanghong.gdqylatitude.util.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupMemberManageAdapter extends BaseAdapter {

    private List<User> list = new ArrayList<>();
    private Context context;
    private boolean permission;

    private Dialog bottomDialog;

    public GroupMemberManageAdapter(Context context, boolean permission){
        this.context = context;
        this.permission = permission;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_group_member_manage,null);

        TextView memberName = v.findViewById(R.id.item_name);
        memberName.setText(list.get(i).getLogname());
        CircleImageView memberHead = v.findViewById(R.id.item_head);
        memberHead.setImageBitmap(ImageUtils.getPicFromBytes(Base64.decode(list.get(i).getHeadportrait(), Base64.DEFAULT),null));

        ImageView item_delete = v.findViewById(R.id.item_delete);
        if(!permission){
            item_delete.setVisibility(View.GONE);
        } else{
            item_delete.setVisibility(View.VISIBLE);
            item_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bottomDialog.show();
                }
            });
        }

        return v;
    }

    public void createDialog() {
        bottomDialog = new Dialog(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this.context).inflate(R.layout.dialog_friends_delete, null);
        bottomDialog.setContentView(contentView);

        TextView tv_delete = (TextView)contentView.findViewById(R.id.tv_delete);
        tv_delete.setText("删除该成员");

        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = this.context.getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(this.context, 16f);
        params.bottomMargin = DensityUtil.dp2px(this.context, 8f);
        contentView.setLayoutParams(params);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }

    public void setList(List<User> list){
        this.list = list;
        notifyDataSetChanged();
    }

}
