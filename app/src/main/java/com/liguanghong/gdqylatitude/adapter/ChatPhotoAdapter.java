package com.liguanghong.gdqylatitude.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.activitys.SelectPhotoActivity;
import com.liguanghong.gdqylatitude.util.SelectPhotoLoaderUtil;

import java.util.LinkedList;
import java.util.List;


public  class ChatPhotoAdapter extends BaseAdapter {
    private Context context;
    private String mDirPath;
    private List<String> mImgPaths;
    private LayoutInflater mInflater;
    public static List<String> mSelectImg=new LinkedList<>();
    private int lastPosition;//定义一个标记为最后选择的位置

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private boolean MultiSelect;

    public ChatPhotoAdapter(Context context, List<String> mDatas, String dirPath) {
        this.context = context;
        this.mDirPath=dirPath;
        this.mImgPaths=mDatas;
        mInflater=LayoutInflater.from(context);
        lastPosition = -1;
        sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE); //获取editor对象
        editor = sharedPreferences.edit();//获取编辑器
        MultiSelect = sharedPreferences.getBoolean("isMultiSelect",SelectPhotoActivity.isMultiSelect);
    }

    @Override
    public int getCount() {
        return mImgPaths.size();
    }

    @Override
    public Object getItem(int position) {
        return mImgPaths.get(position);
    }

    public void setSeclection(int position) {
        lastPosition = position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh=null;
        if (convertView==null){
            convertView=  mInflater.inflate(R.layout.item_chat_photo,parent,false);
            vh=new ViewHolder();
            vh.mImg=convertView.findViewById(R.id.iv_item);
            vh.mSelect=convertView.findViewById(R.id.ib_select);
            vh.mImg.setColorFilter(null);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }

        final String filePath=mDirPath+"/"+mImgPaths.get(position);
        vh.mSelect.setBackgroundResource(R.drawable.select_pic_circle);
        mSelectImg.remove(filePath);
        SelectPhotoLoaderUtil.getInStance(3, SelectPhotoLoaderUtil.Type.LIFO)
                .loadImage(mDirPath+"/"+mImgPaths.get(position),vh.mImg);

        if (MultiSelect == true){
            if (lastPosition == position) {//最后选择的位置
                mSelectImg.add(filePath);
                vh.mImg.setColorFilter(Color.parseColor("#77000000"));
                vh.mSelect.setBackgroundResource(R.drawable.select_pic_circle_check);
            } else {
                mSelectImg.remove(filePath);
                vh.mImg.setColorFilter(null);
                vh.mSelect.setBackgroundResource(R.drawable.select_pic_circle);
            }
        }else {
            final ViewHolder finalVh = vh;
            vh.mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //已经被选择
                    if (mSelectImg.contains(filePath)){
                        mSelectImg.remove(filePath);
                        finalVh.mImg.setColorFilter(null);
                        finalVh.mSelect.setBackgroundResource(R.drawable.select_pic_circle);
                    }else{
                        if (mSelectImg.size()<3){
                            //未被选中
                            mSelectImg.add(filePath);
                            finalVh.mImg.setColorFilter(Color.parseColor("#77000000"));
                            finalVh.mSelect.setBackgroundResource(R.drawable.select_pic_circle_check);
                        }else {
                            Toast.makeText(context,"只能选择3张图片",Toast.LENGTH_LONG).show();
                        }

                    }
                }
            });
        }

        return convertView;
    }

    public List<String> selectPhoto(){
        if (!mSelectImg.isEmpty()){
            return mSelectImg;
        }
        return null;
    }
    public class  ViewHolder{
        ImageView mImg;
        ImageView mSelect;
    }
}
