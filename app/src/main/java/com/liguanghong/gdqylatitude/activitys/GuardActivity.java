package com.liguanghong.gdqylatitude.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.api.track.HistoryTrackRequest;
import com.baidu.trace.api.track.HistoryTrackResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.api.track.SupplementMode;
import com.baidu.trace.api.track.TrackPoint;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.SortType;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TransportMode;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.util.CommonUtil;
import com.liguanghong.gdqylatitude.util.MapUtil;
import com.liguanghong.gdqylatitude.view.DateDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GuardActivity extends BaseActivity implements View.OnClickListener {

    // 轨迹服务ID
    private long serviceId = 206877;
    // 设备标识
    private String entityName = "gps_test";
    private HistoryTrackRequest historyTrackRequest;
    private BaiduMap mBaiduMap;
    private MapView mMapView;
    private ImageView backtrack;
    private DateDialog dateDialog = null;
    private Button startTimeBtn = null;
    private Button endTimeBtn = null;
    private Button search;
    private LBSTraceClient mTraceClient;

    private AtomicInteger mSequenceGenerator = new AtomicInteger();
    private DateDialog.Callback startTimeCallback = null;
    private DateDialog.Callback endTimeCallback = null;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    private long startTime = CommonUtil.getCurrentTime();
    private long endTime = CommonUtil.getCurrentTime();

    private void Init(){
// 设置开始时间
        historyTrackRequest.setStartTime(startTime);
// 设置结束时间
        historyTrackRequest.setEndTime(endTime);
        // 查询历史轨迹
        historyTrackRequest.setPageIndex(pageIndex);
        historyTrackRequest.setPageSize(PAGE_SIZE);
        mTraceClient.queryHistoryTrack(historyTrackRequest, mTrackListener);
    }

    /**
     * 轨迹点集合
     */
    private List<LatLng> trackPoints = new ArrayList<>();
    public static final int PAGE_SIZE = 5000;
    /**
     * 轨迹排序规则
     */
    private SortType sortType = SortType.asc;

    private int pageIndex = 1;
    // 初始化轨迹监听器
    OnTrackListener mTrackListener = new OnTrackListener() {
        // 历史轨迹回调
        @Override
        public void onHistoryTrackCallback(HistoryTrackResponse response) {
            int toal = response.getTotal();
            Log.i("轨迹记录", response.getStatus()+"-"+response.getMessage());
            if (StatusCodes.SUCCESS != response.getStatus()) {

            }else if (0 == toal){
                Log.i("轨迹记录", "数量：空");
                Toast.makeText(GuardActivity.this,"所选时间段未有出行轨迹",Toast.LENGTH_SHORT).show();
                mBaiduMap.clear();
            }else {
                List<TrackPoint> points = response.getTrackPoints();

                Log.i("轨迹记录", "数量："+points.size());
                if (null != points) {
                    for (TrackPoint trackPoint : points) {
                        if (!MapUtil.isZeroPoint(trackPoint.getLocation().getLatitude(),
                                trackPoint.getLocation().getLongitude())) {
                            trackPoints.add(new LatLng(trackPoint.getLocation().latitude, trackPoint.getLocation().longitude));
                        }
                    }
                }
            }
            if (toal > PAGE_SIZE*pageIndex){
                historyTrackRequest.setPageIndex(++pageIndex);
                Init();
            }
            else {
                drawHistoryTrack(trackPoints, sortType);
            }


        }
    };

    public  BitmapDescriptor bmStart = null;
    public BitmapDescriptor bmEnd = null;
    public  BitmapDescriptor bmArrowPoint = null;
    /**
     * 路线覆盖物
     */
    public Overlay polylineOverlay = null;
    private Marker mMoveMarker = null;
    private MapStatus mapStatus = null;
    /**
     * 绘制历史轨迹
     */
    public void drawHistoryTrack( final List<LatLng> points, SortType sortType) {
        // 绘制新覆盖物前，清空之前的覆盖物
        mBaiduMap.clear();
        if (null != mMoveMarker) {
            mMoveMarker.remove();
            mMoveMarker = null;
        }
        if (points == null || points.size() == 0) {
            if (null != polylineOverlay) {
                polylineOverlay.remove();
                polylineOverlay = null;
            }
            return;
        }
        //资源文件
        bmStart = BitmapDescriptorFactory.fromResource(R.mipmap.icon_start);
        bmEnd = BitmapDescriptorFactory.fromResource(R.mipmap.icon_end);
        bmArrowPoint = BitmapDescriptorFactory.fromResource(R.mipmap.icon_point);
        if (points.size() == 1) {
            OverlayOptions startOptions = new MarkerOptions().position(points.get(0)).icon(bmStart)
                    .zIndex(9).draggable(true);
            mBaiduMap.addOverlay(startOptions);
            animateMapStatus(points.get(0), 18.0f);
            return;
        }

        LatLng startPoint;
        LatLng endPoint;
        if (sortType == SortType.asc) {
            startPoint = points.get(0);
            endPoint = points.get(points.size() - 1);
        } else {
            startPoint = points.get(points.size() - 1);
            endPoint = points.get(0);
        }

        // 添加起点图标
        OverlayOptions startOptions = new MarkerOptions()
                .position(startPoint).icon(bmStart)
                .zIndex(9).draggable(true);
        // 添加终点图标
        OverlayOptions endOptions = new MarkerOptions().position(endPoint)
                .icon(bmEnd).zIndex(9).draggable(true);

        // 添加路线（轨迹）
        OverlayOptions polylineOptions = new PolylineOptions().width(10)
                .color(Color.BLUE).points(points);

        mBaiduMap.addOverlay(startOptions);
        mBaiduMap.addOverlay(endOptions);
        polylineOverlay = mBaiduMap.addOverlay(polylineOptions);

        OverlayOptions markerOptions =
                new MarkerOptions().flat(true).anchor(0.5f, 0.5f).icon(bmArrowPoint)
                        .position(points.get(points.size() - 1))
                        .rotate((float) MapUtil.getAngle(points.get(0), points.get(1)));
        mMoveMarker = (Marker) mBaiduMap.addOverlay(markerOptions);
        animateMapStatus(points);

    }
    public void animateMapStatus(LatLng point, float zoom) {
        MapStatus.Builder builder = new MapStatus.Builder();
        mapStatus = builder.target(point).zoom(zoom).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
    }

    public void animateMapStatus(List<LatLng> points) {
        if (null == points || points.isEmpty()) {
            return;
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : points) {
            builder.include(point);
        }
        MapStatusUpdate msUpdate = MapStatusUpdateFactory.newLatLngBounds(builder.build());
        mBaiduMap.animateMapStatus(msUpdate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_guard;
    }

    @Override
    protected void initView() {
        mMapView = findViewById(R.id.guard_map);
        //隐藏百度地图logo
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)){
            child.setVisibility(View.INVISIBLE);
        }
        //地图上比例尺
        mMapView.showScaleControl(false);
        // 隐藏缩放控件
        mMapView.showZoomControls(true);
        backtrack = findViewById(R.id.backtrack);
        backtrack.setOnClickListener(this);
        startTimeBtn = findViewById(R.id.start_time);
        endTimeBtn = findViewById(R.id.end_time);
        startTimeBtn.setOnClickListener(this);
        search = findViewById(R.id.search);
        search.setOnClickListener(this);
        long nowTime = System.currentTimeMillis();
        startTimeBtn.setText("开始："+simpleDateFormat.format(nowTime));
        endTimeBtn.setOnClickListener(this);
        endTimeBtn.setText("结束："+simpleDateFormat.format(nowTime));
    }

    @Override
    protected void initData() {

        Intent intent = getIntent();
        entityName = intent.getStringExtra("entityName");
        //获取地图
        mBaiduMap = mMapView.getMap();
        mTraceClient = new LBSTraceClient(getApplicationContext());
        historyTrackRequest = new HistoryTrackRequest(mSequenceGenerator.incrementAndGet(), serviceId, entityName);
        ProcessOption processOption = new ProcessOption();//纠偏选项
        processOption.setRadiusThreshold(0);
        processOption.setTransportMode(TransportMode.riding);
        processOption.setNeedDenoise(true);//去噪处理
        processOption.setNeedMapMatch(true);//绑路处理
        processOption.setNeedVacuate(true);//抽稀处理
        historyTrackRequest.setProcessOption(processOption);//设置参数
        historyTrackRequest.setProcessed(true);
        Init();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backtrack:
                finish();
                break;
            case R.id.start_time:
                onStartTime();
                break;
            case R.id.end_time:
                onEndTime();
                break;
            case R.id.search:
                requery();
                break;
        }
    }

    public void onStartTime() {
        if (null == startTimeCallback) {
            startTimeCallback = new DateDialog.Callback() {
                @Override
                public void onDateCallback(long timeStamp) {
                    GuardActivity.this.startTime = timeStamp;
                    StringBuilder startTimeBuilder = new StringBuilder();
                    startTimeBuilder.append("开始：");
                    startTimeBuilder.append(simpleDateFormat.format(timeStamp * 1000));
                    startTimeBtn.setText(startTimeBuilder.toString());
                }
            };
        }
        if (null == dateDialog) {
            dateDialog = new DateDialog(this, startTimeCallback);
        } else {
            dateDialog.setCallback(startTimeCallback);
        }
        dateDialog.show();
    }

    public void onEndTime() {
        if (null == endTimeCallback) {
            endTimeCallback = new DateDialog.Callback() {
                @Override
                public void onDateCallback(long timeStamp) {
                    GuardActivity.this.endTime = timeStamp;
                    StringBuilder endTimeBuilder = new StringBuilder();
                    endTimeBuilder.append("结束：");
                    endTimeBuilder.append(simpleDateFormat.format(timeStamp * 1000));
                    endTimeBtn.setText(endTimeBuilder.toString());
                }
            };
        }
        if (null == dateDialog) {
            dateDialog = new DateDialog(this, endTimeCallback);
        } else {
            dateDialog.setCallback(endTimeCallback);
        }
        dateDialog.show();
    }

    private void requery(){
        Log.i("GPS", "starttime："+startTime+" , endtime："+endTime);
        Init();
    }

}
