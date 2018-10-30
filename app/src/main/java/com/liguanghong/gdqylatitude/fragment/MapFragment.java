package com.liguanghong.gdqylatitude.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.liguanghong.gdqylatitude.R;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private List<LatLng> latlngs;
    private List<OverlayOptions> options;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if(view == null)
        {
            view = inflater.inflate(R.layout.fragment_map, container, false);
            initView();
            initData();

        }
        else
        {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null){
                parent.removeView(view);
            }
        }

        SDKInitializer.initialize(getActivity().getApplicationContext());
        return view;
    }

    //初始化组件
    private void initView()
    {
        mMapView = (MapView)view.findViewById(R.id.map);
        mBaiduMap = mMapView.getMap();

        latlngs = new ArrayList<LatLng>();
        options = new ArrayList<OverlayOptions>();

        LatLng point1 = new LatLng(39.92235, 116.380338);
        LatLng point2 = new LatLng(39.947246, 116.414977);
        LatLng point3 = new LatLng(39.96235, 116.360338);
        LatLng point4 = new LatLng(39.987246, 116.444977);

        latlngs.add(point1);
        latlngs.add(point2);
        latlngs.add(point3);
        latlngs.add(point4);

        ini(latlngs);
    }
    //初始化数据
    private void initData()
    {

    }
    private void ini(List<LatLng> latlngs){
        //创建OverlayOptions属性

        for(int i = 0; i < latlngs.size(); i++){
            OverlayOptions option =  new MarkerOptions().position(latlngs.get(i)).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka));
            options.add(option);
        }

        //在地图上批量添加
        mBaiduMap.addOverlays(options);
    }

}
