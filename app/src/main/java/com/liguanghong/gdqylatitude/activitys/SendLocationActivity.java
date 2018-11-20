package com.liguanghong.gdqylatitude.activitys;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.fragment.MapFragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SendLocationActivity extends BaseActivity implements View.OnClickListener {
    private ImageView backtrack;
    private TextView menu;
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private Handler handler;
    private List<OverlayOptions> options;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static boolean locationtype = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
    }
    @Override
    protected int getLayoutId() { return R.layout.activity_send_location; }

    protected void initView() {
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE); //获取editor对象
        editor = sharedPreferences.edit();//获取编辑器

        mMapView = findViewById(R.id.map);
        backtrack = findViewById(R.id.backtrack);
        menu = findViewById(R.id.menu);

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);

        backtrack.setOnClickListener(this);
        menu.setOnClickListener(this);

        setOptions();

    }

    protected void initData() {
        handler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 200:
                        Intent intent = new Intent();
                        intent.putExtra("bitmap", (byte[])message.obj);
                        setResult(25,intent);
                        finish();
                        break;
                }
            }
        };
        //隐藏百度地图logo
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)){
            child.setVisibility(View.INVISIBLE);
        }

        //通过判断locationtype的true/false展现我方位置
        boolean type = sharedPreferences.getBoolean("locationtype",locationtype);
        System.out.println("3333333333333"+type);
        if (type = locationtype){
            showMyLocation();
        }
    }


    //对方位置
    private void setOptions(){
        options = new ArrayList<OverlayOptions>();
        try {
            OverlayOptions option = new MarkerOptions()
                    .position(new LatLng(MapFragment.latitude, MapFragment.longitude))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding));
            Marker marker = (Marker) (mBaiduMap.addOverlay(option));
            options.add(option);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        //设定中心点坐标
        LatLng cenpt =  new LatLng(MapFragment.latitude, MapFragment.longitude);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                //要移动的点
                .target(cenpt)
                .build();

        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu:
                onSendAddress();
                break;

            case R.id.backtrack:
                finish();
                break;
        }
    }

    //点击则发送定位
    private void onSendAddress(){
        final BitmapDescriptor mapCenterPoint = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
        OverlayOptions option = new MarkerOptions()
                .position(new LatLng(MapFragment.latitude, MapFragment.longitude))
                .icon(mapCenterPoint);

        mBaiduMap.addOverlay(option);
        mBaiduMap.setMyLocationEnabled(false);
        //mIvMapCenter.setVisibility(View.GONE); //由于图片添加marker需要一定时间，才可以显示，故这里延时500ms //如果不延时，截图上没有marker

        mMapView.postDelayed(new Runnable() {
            @Override
            public void run() {
                //截图动作
                mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap bitmap){
                        //上传截图
                        ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);//把bitmap100%高质量压缩 到 output对象里
                        byte[] result = output.toByteArray();//转换成功了  result就是一个bit的资源数组
                        Message message = new Message();
                        message.what = 200;
                        message.obj = result;
                        handler.sendMessage(message);
                    }
                });
            }
        },500);
    }

    //我方位置
   private void showMyLocation(){
        menu.setVisibility(View.INVISIBLE);
       LatLng ll = new LatLng(40, 116);
       mBaiduMap.setMyLocationEnabled(true);
       // 构造定位数据
       MyLocationData locData = new MyLocationData.Builder()
               .accuracy(0)
               // 此处设置开发者获取到的方向信息，顺时针0-360
               .direction(100).latitude(40)
               .longitude(110).build();

       // 设置定位数据
       mBaiduMap.setMyLocationData(locData);
       MapStatus mMapStatus = new MapStatus.Builder()
               .target(new LatLng(40, 110))    //要移动的点
               .zoom(18)   //设置缩放级别
               .build();
       //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
       MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
       //更新地图状态
       mBaiduMap.setMapStatus(mMapStatusUpdate);

       locationtype = false;
       editor.putBoolean("locationtype",locationtype);
       editor.commit();

   }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause(); //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}