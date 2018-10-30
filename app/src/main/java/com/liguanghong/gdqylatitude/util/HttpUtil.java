package com.liguanghong.gdqylatitude.util;

import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 网络访问工具类
 */
public class HttpUtil {

    private static final String url = "http://172.17.146.220:8080/GdqyLatitude/";

    /**
     * 获取基本URL
     * @return
     */
    public static String getUrl(){
        return url;
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
                .url(url+path+param)
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
                .url(url+path)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
