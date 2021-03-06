package com.liguanghong.gdqylatitude.activitys;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.ChatPhotoAdapter;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.unity.SelectPicFolderBean;
import com.liguanghong.gdqylatitude.view.SelectPhotoDirPopupWindow;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectPhotoActivity extends BaseActivity implements View.OnClickListener {
    private GridView mGridView;
    private List<String> mImgs;
    private RelativeLayout mbottomLayout;
    private TextView mTvDirName,mTvDirCount;
    private ImageView backtrack;
    private TextView tv_send;
    private File mCurrentDir;
    private  int mMaxCount;
    private SelectPhotoDirPopupWindow mImageDirPopupWindow;
    private List<SelectPicFolderBean> mFolderBeans=new ArrayList<>();

    public List<String> photoPaths;

    private ProgressDialog mProgressDialog;
    private ChatPhotoAdapter adapter;
    private Handler handler;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static boolean isMultiSelect;    //用于判断是否多选图片


    @Override
    protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    protected int getLayoutId() { return R.layout.activity_select_photo; }

    protected void initView() {
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE); //获取editor对象
        editor = sharedPreferences.edit();//获取编辑器

        mGridView= (GridView) findViewById(R.id.id_gridView);
        mbottomLayout= (RelativeLayout) findViewById(R.id.rl_bottom_layout);
        mTvDirName= (TextView) findViewById(R.id.tv_dir_name);
        mTvDirCount= (TextView) findViewById(R.id.tv_dir_count);
        backtrack= (ImageView) findViewById(R.id.backtrack);
        tv_send= findViewById(R.id.tv_send);

        mbottomLayout.setOnClickListener(this);
        backtrack.setOnClickListener(this);
        tv_send.setOnClickListener(this);


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                adapter.setSeclection(position);//传值更新
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 利用Contentprovider扫描手机中的所有图片
     */
    protected void initData() {
        handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==0x110){
                    mProgressDialog.dismiss();
                    dataToView();
                    initPopupWindow();
                }
            }
        };

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(SelectPhotoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SelectPhotoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }else{
                aboutScanPhoto();
            }
        }else{
            aboutScanPhoto();
        }
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.rl_bottom_layout:     //底部弹出框
                mImageDirPopupWindow.setAnimationStyle(R.style.BottomDialog_Animation);
                mImageDirPopupWindow.showAsDropDown(mbottomLayout,0,0);
                lightOff();
                break;

            case R.id.tv_send:          //发送
                selectPic();
                break;

            case R.id.backtrack:            //返回
                if (mCurrentDir!=null){
                    List<String> photoPaths  = adapter.selectPhoto();
                    if (photoPaths!=null){
                        photoPaths.clear();
                        finish();
                    }else{
                        finish();
                    }
                }else{
                    finish();
                }
                break;
        }
    }

    private void selectPic(){
        if (ChatPhotoAdapter.mSelectImg.size()>=1){
            boolean  Multiselect = sharedPreferences.getBoolean("isMultiSelect",isMultiSelect);
            Intent intent =new Intent();
            if (mCurrentDir!=null){ photoPaths  = adapter.selectPhoto(); }
            if (Multiselect == true){
                intent.putExtra("photo", photoPaths.get(0));
                setResult(20, intent);
                finish();
            }else {
                Intent data = getIntent();
                int size = (int)data.getExtras().get("size");
                System.out.println("size"+size);
                if (((size + ChatPhotoAdapter.mSelectImg.size())>=0) && ((size + ChatPhotoAdapter.mSelectImg.size())<4)){
                    intent.putExtra("photo", (Serializable) photoPaths);
                    setResult(20, intent);
                    finish();
                }
            }
        }
    }

    //检测相册是否有图片，若有图片则显示该相册的详情
    private void dataToView() {
        if (mCurrentDir==null){
            Toast.makeText(this,"未扫描到任何图片",Toast.LENGTH_LONG).show();
            return;
        }
        mImgs= Arrays.asList(mCurrentDir.list());
        adapter = new ChatPhotoAdapter(this,mImgs,mCurrentDir.getAbsolutePath());
        mGridView.setAdapter(adapter);
        mTvDirName.setText(mCurrentDir.getName());
        mTvDirCount.setText("共"+ mMaxCount+"张");
    }


    //底部弹出框，显示所有相册的详情。点击切换相册
    private void initPopupWindow() {
        mImageDirPopupWindow=new SelectPhotoDirPopupWindow(this,mFolderBeans);
        mImageDirPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lightOn();
            }
        });
        mImageDirPopupWindow.setOnDirSelectedListener(new SelectPhotoDirPopupWindow.OnDirSelectedListener() {
            @Override
            public void onSelected(SelectPicFolderBean folderBean) {
                mCurrentDir=new File(folderBean.getDir());

               mImgs= Arrays.asList(mCurrentDir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        if (name.endsWith(".jpg")||name.endsWith("jpeg")||name.endsWith("png")){
                            return  true;
                        }
                        return false;
                    }
                }));
                adapter=new ChatPhotoAdapter(SelectPhotoActivity.this,mImgs,mCurrentDir.getAbsolutePath());
                mGridView.setAdapter(adapter);
                mTvDirCount.setText(mImgs.size()+"");
                mTvDirName.setText(folderBean.getName());
                mImageDirPopupWindow.dismiss();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0 &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    aboutScanPhoto();
                }else {
                    Toast.makeText(this, "请打开权限！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    /*
    检测sd卡中所有文件夹，
    挑选出文件夹（有图片）显示到页面上
    检测图片格式包括jpg,jpeg,png
    */
    private void aboutScanPhoto() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(this,"当前存储卡不可用！",Toast.LENGTH_LONG);
            return;
        }
        mProgressDialog= ProgressDialog.show(this,null,"正在加载。。。");
        new Thread(){
            @Override
            public void run() {
                Uri mImgUri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr=SelectPhotoActivity.this.getContentResolver();
                Cursor mCursor = cr.query(mImgUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[] { "image/jpeg", "image/png" },
                        MediaStore.Images.Media.DATE_MODIFIED);
                Set<String> mDirPaths=new HashSet<String>();
                while (mCursor.moveToNext()){
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File parentFile=new File(path).getParentFile();
                    if (parentFile==null) { continue; }
                    String dirPath = parentFile.getAbsolutePath();
                    SelectPicFolderBean folderBean=null;
                    if (mDirPaths.contains(dirPath)){
                        continue;
                    }else{
                        mDirPaths.add(dirPath);
                        folderBean=new SelectPicFolderBean();
                        folderBean.setDir(dirPath);
                        folderBean.setFirstImamgPath(path);
                    }

                    if (parentFile.list()==null){ continue; }
                    int picSize=parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            if (name.endsWith(".jpg")||name.endsWith("jpeg")||name.endsWith("png")){
                                return  true;
                            }
                            return false;
                        }
                    }).length;
                    folderBean.setCount(picSize);
                    mFolderBeans.add(folderBean);

                    if (picSize>mMaxCount){
                        mMaxCount=picSize;
                        mCurrentDir=parentFile;
                    }
                }
                mCursor.close();
                handler.sendEmptyMessage(0x110);
            }
        }.start();
    }


    /**
     * 内容区域变亮
     */
    private void lightOn() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha=1.0f;
        getWindow().setAttributes(lp);
    }

    /**
     * 内容区域变暗
     */
    private void lightOff() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha=0.3f;
        getWindow().setAttributes(lp);
    }
}
