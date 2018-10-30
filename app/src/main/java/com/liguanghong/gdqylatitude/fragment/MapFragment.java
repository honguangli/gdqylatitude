package com.liguanghong.gdqylatitude.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ZoomControls;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.JsonResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MapFragment extends Fragment {

    private View view;

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private List<User> userList;
    private List<User> friendsList;
    private List<User> strangerList;
    private List<OverlayOptions> options;

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
        return view;
    }

    //初始化组件
    private void initView() {
        mMapView = (MapView)view.findViewById(R.id.map);
        mBaiduMap = mMapView.getMap();
    }

    //初始化数据
    private void initData(){
        //隐藏百度地图logo
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)){
            child.setVisibility(View.INVISIBLE);
        }
        //地图上比例尺
        mMapView.showScaleControl(true);
        // 隐藏缩放控件
        mMapView.showZoomControls(true);
        getData();
    }

    /**
     * 获取数据
     */
    private void getData(){
        RequestBody requestBody = new FormBody.Builder()
                .add("statu","")
                .add("required", "")
                .build();
        HttpUtil.postEnqueue("user/findall", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        userList = JSONArray.parseArray(((JSONArray)result.getData()).toJSONString(), User.class);
                        for(User user : userList){
                            Log.i("输出", user.getLogname());
                        }
                        setOptions();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    /**
     * 设置地图标记
     */
    private void setOptions() {
        //创建OverlayOptions属性
        options = new ArrayList<OverlayOptions>();
        for (int i = 0; i < userList.size(); i++) {
            try {
                OverlayOptions option = new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(userList.get(i).getLatitude()), Double.parseDouble(userList.get(i).getLongitude())))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka));
                options.add(option);
            } catch (NullPointerException e) {
                Log.i("地图标记操作", "解析标记错误，用户userid：" + userList.get(i).getUserid());
                e.printStackTrace();
                continue;
            }
        }
        //在地图上批量添加
        mBaiduMap.addOverlays(options);
    }

}
