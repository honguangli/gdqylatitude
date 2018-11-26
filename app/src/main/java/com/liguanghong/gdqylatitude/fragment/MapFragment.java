package com.liguanghong.gdqylatitude.fragment;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.activitys.UserInfoActivity;
import com.liguanghong.gdqylatitude.activitys.MapUsersInfoActivity;
import com.liguanghong.gdqylatitude.base.BaseFragment;
import com.liguanghong.gdqylatitude.clusterutil.clustering.Cluster;
import com.liguanghong.gdqylatitude.clusterutil.clustering.ClusterItem;
import com.liguanghong.gdqylatitude.clusterutil.clustering.ClusterManager;
import com.liguanghong.gdqylatitude.manager.FriendsManager;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.unity.Friend;
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

public class MapFragment extends BaseFragment {

    private MapView mMapView;
    private Button btnCenter;
    private BaiduMap mBaiduMap;

    private ClusterManager<MyItem> mClusterManager;

    private LocationClient mLocationClient;
    private BDAbstractLocationListener myListener = new MyLocationListener();
    private static double latitude;
    private static double longitude;
    private boolean isZoomTo = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutId(){
        return R.layout.fragment_map;
    }

    @Override
    protected void initView(View view) {
        btnCenter = view.findViewById(R.id.btn_center);
        btnCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newLatLngZoom(mBaiduMap, new LatLng(latitude, longitude), 18);
            }
        });
        mMapView = view.findViewById(R.id.map);

    }

    //初始化数据
    @Override
    protected void initData(){
        //隐藏百度地图logo
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)){
            child.setVisibility(View.INVISIBLE);
        }
        //地图上比例尺
        mMapView.showScaleControl(false);
        // 隐藏缩放控件
        mMapView.showZoomControls(true);
        //获取地图
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //声明LocationClient类
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        //初始化定位属性
        initLocation();
        //启动定位
        mLocationClient.start();
        //声明集群管理器
        mClusterManager = new ClusterManager<MyItem>(getContext(), mBaiduMap);
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        mBaiduMap.setOnMarkerClickListener(mClusterManager);
        mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){

                }
            }
        });
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                mBaiduMap.hideInfoWindow();
            }
        });
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                List<MyItem> items = (List<MyItem>) cluster.getItems();
                try{
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.item_map_user_info,null);
                    TextView logname = view.findViewById(R.id.user_logname);
                    final List<User> list = new ArrayList<>();
                    for(MyItem myItem : items){
                        final User user = (User)myItem.getExtraInfo().get("info");
                        list.add(user);
                    }
                    logname.setText(list.get(0).getLogname()+" 等"+cluster.getSize()+"位用户");
                    LinearLayout getUserInfoPanel = view.findViewById(R.id.getUserInfoPanel);
                    getUserInfoPanel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), MapUsersInfoActivity.class);
                            String ss = JSONObject.toJSONString(list);
                            intent.putExtra("user", ss);
                            startActivity(intent);
                        }
                    });

                    final LatLng ll = cluster.getPosition();//得到坐标
                    Point p = mBaiduMap.getProjection().toScreenLocation(ll);
                    LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
                    InfoWindow mInfoWindow = new InfoWindow(view, llInfo, -47*3);
                    //显示InfoWindow
                    mBaiduMap.showInfoWindow(mInfoWindow);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem item) {
                final User user = (User)item.getExtraInfo().get("info");
                try {
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.item_map_user_info,null);
                    TextView logname = view.findViewById(R.id.user_logname);
                    logname.setText(user.getLogname());
                    LinearLayout getUserInfoPanel = view.findViewById(R.id.getUserInfoPanel);
                    getUserInfoPanel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), UserInfoActivity.class);
                            Friend friend = FriendsManager.getInstance().getFriendByID(user.getUserid());
                            if(friend != null){
                                //好友
                                intent.putExtra("userinfo", friend);
                                intent.putExtra("hide", false);
                            } else {
                                //非好友
                                intent.putExtra("userinfo", user);
                                intent.putExtra("hide", true);
                            }

                            startActivity(intent);
                        }
                    });
                    //定义用于显示该InfoWindow的坐标点
                    final LatLng ll = item.getPosition();//得到坐标
                    Point p = mBaiduMap.getProjection().toScreenLocation(ll);
                    LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
                    InfoWindow mInfoWindow = new InfoWindow(view, llInfo, -47*3);
                    //显示InfoWindow
                    mBaiduMap.showInfoWindow(mInfoWindow);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getData();
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static LatLng getLocation(){
        return new LatLng(latitude, longitude);
    }

    public static double getLatitude(){
        return latitude;
    }

    public static void setLatitude(double lat){
        latitude = lat;
    }

    public static double getLongitude(){
        return longitude;
    }

    public static void setLongitude(double lng){
        longitude = lng;
    }

    /**
     * 获取网站用户数据，用于地图标记点
     */
    private void getData(){
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", UserManager.getInstance().getAppUser().getUserid() + "")
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
                        List<User> userList = JSONArray.parseArray(((JSONArray)result.getData()).toJSONString(), User.class);
                        setOptions(userList);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void updateLocation(double latitude, double longitude){
        if(getLatitude() == latitude && getLongitude() == longitude){
            return;
        } else{
            setLatitude(latitude);
            setLongitude(longitude);
            RequestBody requestBody = new FormBody.Builder()
                    .add("userid", UserManager.getInstance().getAppUser().getUserid() + "")
                    .add("latitude",latitude + "")
                    .add("longitude", longitude + "")
                    .build();
            HttpUtil.postEnqueue("user/updatelocation", requestBody, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.i("定位管理", "更新定位：连接失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()){
                        try {
                            JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                            if(result.isSuccess()){

                            } else{

                            }
                            Log.i("定位管理", "更新定位：" + result.isSuccess() + "-" + result.getMessage());
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }
            });
        }
    }

    /**
     * 设置地图标记点
     */
    private void setOptions(List<User> userList){
        //清空标记点
        mClusterManager.clearItems();
        for (int i = 0; i < userList.size(); i++) {
            try {
                Log.i("测试", userList.get(i).getUserid() + " :" + userList.get(i).getLogname());
                LatLng latLng = new LatLng(userList.get(i).getLatitude(), userList.get(i).getLongitude());//得到每个用户的定位
                List<MyItem> items = new ArrayList<MyItem>();  //集合
                Bundle bundle = new Bundle();
                bundle.putSerializable("info", (User)userList.get(i));//用户
                items.add(new MyItem(latLng,bundle));
                mClusterManager.addItems(items);
            } catch (NullPointerException e) {
                Log.i("地图标记操作", "解析标记错误，用户userid：" + userList.get(i).getUserid());
                e.printStackTrace();
            }
        }
    }

    /**
     * 移动到指定点
     * @param baiduMap
     * @param latLng
     * @param zoom
     */
    private void newLatLngZoom(BaiduMap baiduMap, LatLng latLng, float zoom){
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(latLng)    //要移动的点
                .zoom(zoom)   //设置缩放级别
                .build();
        //更新地图状态
        baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mMapStatus));
    }

    /**
     * 初始化定位属性
     */
    private void initLocation(){

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span= 1500;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        // option.setIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        //  option.setWifiValidTime(5*60*1000);
        //可选，7.2版本新增能力，如果您设置了这个接口，首次启动定位时，会先判断当前WiFi是否超出有效期，超出有效期的话，会先重新扫描WiFi，然后再定位

        mLocationClient.setLocOption(option);
    }

    /**
     * 用户定位监听
     */
    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            //获取定位结果
            StringBuffer sb = new StringBuffer(256);

            sb.append("time : ");
            sb.append(location.getTime());    //获取定位时间

            sb.append("\nerror code : ");
            sb.append(location.getLocType());    //获取类型类型

            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());    //获取纬度信息

            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());    //获取经度信息

            sb.append("\nradius : ");
            sb.append(location.getRadius());    //获取定位精准度


            if (location.getLocType() == BDLocation.TypeGpsLocation) {

                // GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());    // 单位：公里每小时

                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());    //获取卫星数

                sb.append("\nheight : ");
                sb.append(location.getAltitude());    //获取海拔高度信息，单位米

                sb.append("\ndirection : ");
                sb.append(location.getDirection());    //获取方向信息，单位度

                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\ndescribe : ");
                sb.append("gps定位成功");


            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

                // 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                sb.append("\noperationers : ");
                sb.append(location.getOperators());    //获取运营商信息

                sb.append("\ndescribe : ");
                sb.append("网络定位成功");

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                // 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");

            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");


            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());    //位置语义化信息
            List<Poi> list = location.getPoiList();    // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            //更新当前位置
            updateLocation(location.getLatitude(), location.getLongitude());

            // 设置定位数据
            mBaiduMap.setMyLocationData(new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(location.getDirection())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build());
            //定位成功，移动到中心点
            if(isZoomTo){
                isZoomTo = false;
                newLatLngZoom(mBaiduMap, new LatLng(location.getLatitude(), location.getLongitude()), 18);
            }

            Log.i("定位管理", sb.toString());
        }

        /**
         * 回调定位诊断信息，开发者可以根据相关信息解决定位遇到的一些问题
         * 自动回调，相同的diagnosticType只会回调一次
         *
         * @param locType           当前定位类型
         * @param diagnosticType    诊断类型（1~9）
         * @param diagnosticMessage 具体的诊断信息释义
         */
        public void onLocDiagnosticMessage(int locType, int diagnosticType, String diagnosticMessage) {

            if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_GPS) {

                //建议打开GPS

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_WIFI) {

                //建议打开wifi，不必连接，这样有助于提高网络定位精度！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_LOC_PERMISSION) {

                //定位权限受限，建议提示用户授予APP定位权限！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_NET) {

                //网络异常造成定位失败，建议用户确认网络状态是否异常！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CLOSE_FLYMODE) {

                //手机飞行模式造成定位失败，建议用户关闭飞行模式后再重试定位！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_INSERT_SIMCARD_OR_OPEN_WIFI) {

                //无法获取任何定位依据，建议用户打开wifi或者插入sim卡重试！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_OPEN_PHONE_LOC_SWITCH) {

                //无法获取有效定位依据，建议用户打开手机设置里的定位开关后重试！

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_SERVER_FAIL) {

                //百度定位服务端定位失败
                //建议反馈location.getLocationID()和大体定位时间到loc-bugs@baidu.com

            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_FAIL_UNKNOWN) {

                //无法获取有效定位依据，但无法确定具体原因
                //建议检查是否有安全软件屏蔽相关定位权限
                //或调用重新启动后重试！

            }
        }
    }

    class MyItem implements ClusterItem {
        private final LatLng mPosition;
        private Bundle buns;
        public MyItem(LatLng latLng) {
            mPosition = latLng;
        }
        public MyItem(LatLng latLng,Bundle bun) {
            mPosition = latLng;
            buns=bun;
        }
        public LatLng getPosition() {
            return mPosition;
        }
        public Bundle getExtraInfo() {
            return buns;
        }
        public BitmapDescriptor getBitmapDescriptor() {
            return BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_gcoding);
        }
    }


}
