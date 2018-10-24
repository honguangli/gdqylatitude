package com.liguanghong.gdqylatitude;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ZoomControls;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.fragment_map);

        MapView mMapView;
        BaiduMap mBaiduMap;
        mMapView = (MapView) findViewById(R.id.map);
        mBaiduMap = mMapView.getMap();

        //隐藏百度地图logo
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)){
            child.setVisibility(View.INVISIBLE);
        }
        //地图上比例尺
        mMapView.showScaleControl(true);
        // 隐藏缩放控件
        mMapView.showZoomControls(true);

    }
}
