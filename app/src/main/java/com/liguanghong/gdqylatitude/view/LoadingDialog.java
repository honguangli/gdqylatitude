package com.liguanghong.gdqylatitude.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;

public class LoadingDialog {

    private Context context;
    private String msg;

    private LinearLayout linearLayout;
    private LoadingDialogView mLoadingView;
    private TextView loadingText;
    private Dialog mLoadingDialog;

    public LoadingDialog(Context context, String msg) {
        this.context = context;
        this.msg = msg;

        create();
    }

    public void create(){
        createView();
        createDialog();
    }

    public void createView(){
        // 首先得到整个View
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        // 获取整个布局
        linearLayout = view.findViewById(R.id.dialog_view);
        // 页面中的LoadingView
        mLoadingView = view.findViewById(R.id.lv_circularring);
        // 页面中显示文本
        loadingText = view.findViewById(R.id.loading_text);
        // 显示文本
        loadingText.setText(msg);
    }

    public void createDialog(){
        // 创建自定义样式的Dialog
        mLoadingDialog = new Dialog(context, R.style.loading_dialog);
        // 设置返回键无效
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setContentView(linearLayout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
    }

    public void setText(String msg){
        loadingText.setText(msg);
    }

    public void show(){
        mLoadingDialog.show();
        mLoadingView.startAnim();
    }

    public void close(){
        if (mLoadingDialog!=null) {
            mLoadingView.stopAnim();
            mLoadingDialog.dismiss();
            mLoadingDialog=null;
        }
    }

}
