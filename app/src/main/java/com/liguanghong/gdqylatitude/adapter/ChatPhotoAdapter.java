package com.liguanghong.gdqylatitude.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.activitys.SelectPhotoActivity;
import com.liguanghong.gdqylatitude.util.SelectPhotoLoaderUtil;

import java.util.LinkedList;
import java.util.List;


public  class ChatPhotoAdapter extends BaseAdapter {
    private String mDirPath;
    private List<String> mImgPaths;
    private LayoutInflater mInflater;
    private static List<String> mSelectImg=new LinkedList<>();
    public ChatPhotoAdapter(Context context, List<String> mDatas, String dirPath) {
        this.mDirPath=dirPath;
        this.mImgPaths=mDatas;
        mInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mImgPaths.size();
    }

    @Override
    public Object getItem(int position) {
        return mImgPaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh=null;
        if (convertView==null){
            convertView=  mInflater.inflate(R.layout.item_chat_photo,parent,false);
            vh=new ViewHolder();
            vh.mImg=convertView.findViewById(R.id.iv_item);
            vh.mSelect=convertView.findViewById(R.id.ib_select);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.mImg.setImageResource(R.drawable.default_error);
        vh.mSelect.setChecked(SelectPhotoActivity.isChecked);
        vh.mImg.setColorFilter(null);
        final String filePath=mDirPath+"/"+mImgPaths.get(position);
        //   new  ImageLoader(3, ImageLoader.Type.LIFO).loadImage(mDirPath + "/" + mImgPaths.get(position),vh.mImg);
        SelectPhotoLoaderUtil.getInStance(3, SelectPhotoLoaderUtil.Type.LIFO).loadImage(mDirPath+"/"+mImgPaths.get(position),vh.mImg);
        final ViewHolder finalVh = vh;
        vh.mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //已经被选择
                if (mSelectImg.contains(filePath)){
                    mSelectImg.remove(filePath);
                    finalVh.mImg.setColorFilter(null);
                    finalVh.mSelect.setChecked(false);
                }else{
                    //未被选中
                    mSelectImg.add(filePath);
                    finalVh.mImg.setColorFilter(Color.parseColor("#77000000"));
                    finalVh.mSelect.setChecked(true);
                }
            }
        });
        if (mSelectImg.contains(filePath)){
            vh.mImg.setColorFilter(Color.parseColor("#77000000"));
            vh.mSelect.setChecked(true);
        }
        return convertView;
    }

    public List<String> selectPhoto(){
        if (!mSelectImg.isEmpty()){
            return mSelectImg;
        }
        return null;
    }
    private class  ViewHolder{
        ImageView mImg;
        RadioButton mSelect;
    }
}
