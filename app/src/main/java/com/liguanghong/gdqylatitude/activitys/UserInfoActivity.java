package com.liguanghong.gdqylatitude.activitys;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.DialogFriendsSetAdapter;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.util.DensityUtil;

import java.util.ArrayList;

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


    //分组
    private ListView dialog_lv;
    private TextView tv_delete;
    private ArrayList<String> list;
    private Dialog bottomDialog;
    private View groupchangeView;
    private View menuView;

    DialogFriendsSetAdapter dialogFriendsSetAdapter;


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
        menu = (ImageView)findViewById(R.id.menu);
        backtrack_friend_info = (ImageView)findViewById(R.id.backtrack);
        iv_information_icon = (ImageView)findViewById(R.id.iv_information_icon);

        tv_information_name = (TextView)findViewById(R.id.tv_information_name);
        tv_information_id = (TextView)findViewById(R.id.tv_information_id);
        tv_information_markname = (TextView)findViewById(R.id.tv_information_markname);
        tv_information_groupname = (TextView)findViewById(R.id.tv_information_groupname);
        tv_information_city = (TextView)findViewById(R.id.tv_information_city);
        tv_information_signature = (TextView)findViewById(R.id.tv_information_signature);

        bt_information_send = (Button) findViewById(R.id.bt_information_send);

        rly_beizhu = (RelativeLayout)findViewById(R.id.rly_beizhu);
        rly_fenzu = (RelativeLayout)findViewById(R.id.rly_fenzu);

        backtrack_friend_info.setOnClickListener(this);
        menu.setOnClickListener(this);
        rly_beizhu.setOnClickListener(this);
        rly_fenzu.setOnClickListener(this);
        bt_information_send.setOnClickListener(this);

    }

    @Override
    protected void initData() {

        list = new ArrayList<>();
        //虚拟数据
        for (int i = 0;i < 10 ;i++){
            list.add("好友分组" + (i+1));
        }

        dialogFriendsSetAdapter = new DialogFriendsSetAdapter(getApplicationContext(), list);
        dialogFriendsSetAdapter.notifyDataSetChanged();

        bottomDialog = new Dialog(this, R.style.BottomDialog);

        groupchangeView = LayoutInflater.from(this).inflate(R.layout.dialog_friends_set_change, null);
        dialog_lv = groupchangeView.findViewById(R.id.dialog_lv);
        dialog_lv.setAdapter(dialogFriendsSetAdapter);
        dialog_lv.setOnItemClickListener(this);

        menuView = LayoutInflater.from(this).inflate(R.layout.dialog_friends_delete, null);
        tv_delete = menuView.findViewById(R.id.tv_delete);
        tv_delete.setOnClickListener(this);

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
                Toast.makeText(getApplicationContext(),"删除好友",Toast.LENGTH_LONG).show();
                break;

        }
    }

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
            params.height = dialogFriendsSetAdapter.getCount() > 5 ?  mView.getMeasuredHeight() * 5: mView.getMeasuredHeight() * dialogFriendsSetAdapter.getCount();
        }
        params.bottomMargin = DensityUtil.dp2px(this, 8f);
        contentView.setLayoutParams(params);

        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();

    }

    public void showRemark() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_friends_set_remark,null,false);
        final AlertDialog alertdialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(true)
                .show();
        TextView surebutton = view.findViewById(R.id.sure);
        TextView cancelbutton = view.findViewById(R.id.cancel);

        surebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        Toast.makeText(getApplicationContext(),"ListView",Toast.LENGTH_LONG).show();
    }
}
