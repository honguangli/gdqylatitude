package com.liguanghong.gdqylatitude.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.unity.SelectPicFolderBean;
import com.liguanghong.gdqylatitude.util.SelectPhotoLoaderUtil;

import java.util.List;

public class SelectPhotoDirAdapter extends ArrayAdapter<SelectPicFolderBean> {

    private LayoutInflater mInflater;
    private List<SelectPicFolderBean> mDatas;

    public SelectPhotoDirAdapter(@NonNull Context context, List<SelectPicFolderBean> datas) {
        super(context, 0, datas);
        mInflater= LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder vh=null;
        if (convertView==null){
            vh=new ViewHolder();
            convertView=  mInflater.inflate(R.layout.item_popup_photo_dir,parent,false);
            vh.mDirName=(TextView) convertView.findViewById(R.id.tv_dir_item_name);
            vh.mDirCount=(TextView) convertView.findViewById(R.id.tv_dir_item_count);
            vh.mImg= (ImageView) convertView.findViewById(R.id.iv_dir_image);
            convertView.setTag(vh);
        }else{
            vh= (ViewHolder) convertView.getTag();
        }
        SelectPicFolderBean bean = getItem(position);
        //重置
        vh.mImg.setImageResource(R.drawable.default_error);

        SelectPhotoLoaderUtil.getInStance(3, SelectPhotoLoaderUtil.Type.LIFO).loadImage(bean.getFirstImamgPath(),vh.mImg);
        vh.mDirName.setText(bean.getName());
        vh.mDirCount.setText("("+ bean.getCount()+")");
        return convertView;
    }

    private  class  ViewHolder{
        ImageView mImg;
        TextView mDirName;
        TextView mDirCount;
    }
}
