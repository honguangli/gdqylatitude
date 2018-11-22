package com.liguanghong.gdqylatitude.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.util.SelectPhotoLoaderUtil;

import java.util.LinkedList;
import java.util.List;


public  class ChatPhotoAdapter extends BaseAdapter {
    private Context context;
    private String mDirPath;
    private List<String> mImgPaths;
    private LayoutInflater mInflater;
    private static List<String> mSelectImg=new LinkedList<>();
    private int lastPosition;//定义一个标记为最后选择的位置

    public ChatPhotoAdapter(Context context, List<String> mDatas, String dirPath) {
        this.context = context;
        this.mDirPath=dirPath;
        this.mImgPaths=mDatas;
        mInflater=LayoutInflater.from(context);
        lastPosition = -1;

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
        mSelectImg.remove(filePath);
        SelectPhotoLoaderUtil.getInStance(3, SelectPhotoLoaderUtil.Type.LIFO)
                .loadImage(mDirPath+"/"+mImgPaths.get(position),vh.mImg);

        if (lastPosition == position) {//最后选择的位置
            mSelectImg.add(filePath);
            vh.mImg.setColorFilter(Color.parseColor("#77000000"));
            vh.mSelect.setBackgroundResource(R.drawable.select_pic_circle_check);
        } else {
            mSelectImg.remove(filePath);
            vh.mImg.setColorFilter(null);
            vh.mSelect.setBackgroundResource(R.drawable.select_pic_circle);
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
