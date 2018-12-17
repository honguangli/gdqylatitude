package com.liguanghong.gdqylatitude.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 网络访问工具类
 */
public class HttpUtil {

    private static final String addr = "172.17.144.88";                  //服务器ip地址
    private static final Integer apiPort = 8080;                            //服务器api端口
    private static final Integer socketPort = 8888;                         //服务器聊天端口
    private static final String projectName = "GdqyLatitude";             //项目名称

    /**
     * 获取ApiURL
     * @return
     */
    public static String getApiUrl(){
        return "http://" + addr + ":" + apiPort + "/" + projectName + "/";
    }

    public static String getWebSocketAddress(){
        return "ws://" + addr + ":" + apiPort + "/" + projectName + "/websocket/";
    }

    public static String getAddr() {
        return addr;
    }

    public static Integer getApiPort() {
        return apiPort;
    }

    public static Integer getSocketPort() {
        return socketPort;
    }

    public static String getProjectName() {
        return projectName;
    }

    /**
     * GET请求
     * @param path
     * @param param
     * @param callback
     */
    public static void getEnqueue(String path, String param, Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(getApiUrl()+path+param)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * POST请求
     * @param path
     * @param requestBody
     * @param callback
     */
    public static void postEnqueue(String path, RequestBody requestBody, Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(getApiUrl()+path)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 检测网络状态是否联通
     *
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (null != info && info.isConnected() && info.isAvailable()) {
                return true;
            }
        } catch (Exception e) {
            Log.e("HttpUtil", "current network is not available");
            return false;
        }
        return false;
    }

}
