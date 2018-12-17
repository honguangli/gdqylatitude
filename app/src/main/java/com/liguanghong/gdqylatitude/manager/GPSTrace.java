package com.liguanghong.gdqylatitude.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.entity.LocRequest;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.track.LatestPoint;
import com.baidu.trace.api.track.LatestPointRequest;
import com.baidu.trace.api.track.LatestPointResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TraceLocation;
import com.liguanghong.gdqylatitude.unity.CurrentLocation;
import com.liguanghong.gdqylatitude.util.CommonUtil;
import com.liguanghong.gdqylatitude.util.HttpUtil;

import java.util.concurrent.atomic.AtomicInteger;

public class GPSTrace {

    private Context context;

    // 轨迹服务ID
    private static final long serviceId = 206877;
    // 设备标识
    private String entityName = "gps_test";
    private AtomicInteger mSequenceGenerator = new AtomicInteger();
    private LocRequest locRequest = null;

    // 是否需要对象存储服务，默认为：false，关闭对象存储服务。注：鹰眼 Android SDK v3.0以上版本支持随轨迹上传图像等对象数据，若需使用此功能，该参数需设为 true，且需导入bos-android-sdk-1.0.2.jar。
    boolean isNeedObjectStorage = false;
    Trace mTrace;
    LBSTraceClient mTraceClient;
    // 定位周期(单位:秒)
    int gatherInterval = 5;
    // 打包回传周期(单位:秒)
    int packInterval = 5;
    /**
     * 轨迹服务监听器
     */
    private OnTraceListener traceListener = null;

    /**
     * 轨迹监听器(用于接收纠偏后实时位置回调)
     */
    private OnTrackListener trackListener = null;

    /**
     * Entity监听器(用于接收实时定位回调)
     */
    private OnEntityListener entityListener = null;

    /**
     * 实时定位任务
     */
    private RealTimeHandler realTimeHandler = new RealTimeHandler();
    private RealTimeLocRunnable realTimeLocRunnable = null;

    private boolean isRealTimeRunning = true;

    public GPSTrace(Context context){
        this.context = context;
        init();
    }

    public GPSTrace(Context context, String entityName){
        this.context = context;
        this.entityName = entityName;
        init();
    }

    private void init(){
        // 初始化轨迹服务
        mTrace = new Trace(serviceId, entityName, isNeedObjectStorage);
        locRequest = new LocRequest(serviceId);
        // 初始化轨迹服务客户端
        mTraceClient = new LBSTraceClient(context);
        mTraceClient.setLocTimeOffset(2000);
        // 设置定位和打包周期
        mTraceClient.setInterval(gatherInterval, packInterval);
        trackListener = new OnTrackListener() {

            @Override
            public void onLatestPointCallback(LatestPointResponse response) {
                Log.i("GPSTrace", "onLatestPointCallback"+response.getStatus() + "-" +response.getLatestPoint());
                if (StatusCodes.SUCCESS != response.getStatus()) {
                    return;
                }

                LatestPoint point = response.getLatestPoint();
                if (null == point || CommonUtil.isZeroPoint(point.getLocation().getLatitude(), point.getLocation()
                        .getLongitude())) {
                    return;
                }

                LatLng currentLatLng = new LatLng(point.getLocation().latitude, point.getLocation().longitude);
                if (null == currentLatLng) {
                    return;
                }
                CurrentLocation.locTime = point.getLocTime();
                CurrentLocation.latitude = currentLatLng.latitude;
                CurrentLocation.longitude = currentLatLng.longitude;
            }


        };

        entityListener = new OnEntityListener() {

            @Override
            public void onReceiveLocation(TraceLocation location) {
                Log.i("GPSTrace", "onReceiveLocation" + location.getStatus() + "-" + location.getLongitude());
                if (StatusCodes.SUCCESS != location.getStatus() || CommonUtil.isZeroPoint(location.getLatitude(),
                        location.getLongitude())) {
                    return;
                }
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng currentLatLng = new LatLng(latitude, longitude);
                if (CoordType.wgs84 == location.getCoordType()) {
                    LatLng sourceLatLng = currentLatLng;
                    CoordinateConverter converter = new CoordinateConverter();
                    converter.from(CoordinateConverter.CoordType.GPS);
                    converter.coord(sourceLatLng);
                    currentLatLng = converter.convert();
                }
                if (null == currentLatLng) {
                    return;
                }
                CurrentLocation.locTime = CommonUtil.toTimeStamp(location.getTime());
                CurrentLocation.latitude = currentLatLng.latitude;
                CurrentLocation.longitude = currentLatLng.longitude;

            }

        };
        // 初始化轨迹服务监听器
        traceListener = new OnTraceListener() {
            @Override
            public void onBindServiceCallback(int i, String s) {
                Log.i("GPSTrace", "onBindServiceCallback:"+i+"-"+s);
            }
            // 开启服务回调
            @Override
            public void onStartTraceCallback(int status, String message) {
                Log.i("GPSTrace", "onStartTraceCallback:"+status+"-"+message);
            }
            // 停止服务回调
            @Override
            public void onStopTraceCallback(int status, String message) {
                Log.i("GPSTrace", "onStopTraceCallback:"+status+"-"+message);
            }
            // 开启采集回调
            @Override
            public void onStartGatherCallback(int status, String message) {
                Log.i("GPSTrace", "onStartGatherCallback:"+status+"-"+message);
            }
            // 停止采集回调
            @Override
            public void onStopGatherCallback(int status, String message) {
                Log.i("GPSTrace", "onStopGatherCallback:"+status+"-"+message);
            }
            // 推送回调
            @Override
            public void onPushCallback(byte messageNo, PushMessage message) {
                Log.i("GPSTrace", "onPushCallback:"+messageNo+"-"+message);
            }
            @Override
            public void onInitBOSCallback(int i, String s) {
                Log.i("GPSTrace", "onInitBOSCallback:"+i+"-"+s);
            }
        };

    }

    public void startService(){
        // 开启服务
        mTraceClient.startTrace(mTrace, traceListener);
        startRealTimeLoc(packInterval);
        // 开启采集
        mTraceClient.startGather(traceListener);
    }

    public void stopService(){
        // 关闭采集
        mTraceClient.stopGather(traceListener);
        // 关闭服务
        mTraceClient.stopTrace(mTrace, traceListener);
        stopRealTimeLoc();
    }

    /**
     * 获取请求标识
     *
     * @return
     */
    public int getTag() {
        return mSequenceGenerator.incrementAndGet();
    }

    /**
     * 实时定位任务
     *
     * @author baidu
     */
    class RealTimeLocRunnable implements Runnable {

        private int interval = 0;

        public RealTimeLocRunnable(int interval) {
            this.interval = interval;
        }

        @Override
        public void run() {
            if (isRealTimeRunning) {
                Log.i("GPSTrace", "query");
                // 网络连接正常，开启服务及采集，则查询纠偏后实时位置；否则进行实时定位
                if (HttpUtil.isNetworkAvailable(context)) {
                    LatestPointRequest request = new LatestPointRequest(getTag(), serviceId, entityName);
                    ProcessOption processOption = new ProcessOption();
                    processOption.setNeedDenoise(true);
                    processOption.setRadiusThreshold(100);
                    request.setProcessOption(processOption);
                    mTraceClient.queryLatestPoint(request, trackListener);
                } else {
                    mTraceClient.queryRealTimeLoc(locRequest, entityListener);
                }
                realTimeHandler.postDelayed(this, interval * 1000);
            }
        }
    }

    public void startRealTimeLoc(int interval) {
        isRealTimeRunning = true;
        realTimeLocRunnable = new RealTimeLocRunnable(interval);
        realTimeHandler.post(realTimeLocRunnable);
    }

    public void stopRealTimeLoc() {
        isRealTimeRunning = false;
        if (null != realTimeHandler && null != realTimeLocRunnable) {
            realTimeHandler.removeCallbacks(realTimeLocRunnable);
        }
        mTraceClient.stopRealTimeLoc();
    }

    static class RealTimeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

}
