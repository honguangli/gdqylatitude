package com.liguanghong.gdqylatitude.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.SelectPhotoDirAdapter;
import com.liguanghong.gdqylatitude.unity.SelectPicFolderBean;

import java.util.List;


public class SelectPhotoDirPopupWindow extends PopupWindow {
    private  int mWidth;
    private int mHeight;
    private View mConvertView;
    private List<SelectPicFolderBean> mDatas;
    private ListView mListView;

    public  interface  OnDirSelectedListener{
        void onSelected(SelectPicFolderBean folderBean);
    }
    public  OnDirSelectedListener mListener;

    public void setOnDirSelectedListener(OnDirSelectedListener mListener) {
        this.mListener = mListener;
    }

    public SelectPhotoDirPopupWindow(Context context, List<SelectPicFolderBean> datas) {
       calWidthAndHeight(context);
        mConvertView= LayoutInflater.from(context).inflate(R.layout.popup_select_photo_dir,null);
        mDatas=datas;
        setContentView(mConvertView);
        setWidth(mWidth);
        setHeight(mHeight);
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction()==MotionEvent.ACTION_OUTSIDE){
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        initViews(context);
        initEvent();
    }

    private void initViews(Context context) {
        mListView=  mConvertView.findViewById(R.id.lv_dir);
        mListView.setAdapter(new SelectPhotoDirAdapter(context,mDatas));
    }

    private void initEvent() {
       mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               if (mListener!=null){
                   mListener.onSelected(mDatas.get(position));
               }
           }
       });
    }

    /**
     * 计算popupWindow的宽度和高度
     * @param context
     */
    private void calWidthAndHeight(Context context) {

        WindowManager wm= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mWidth= outMetrics.widthPixels;
        mHeight= (int) (outMetrics.heightPixels*0.7);

    }
}
