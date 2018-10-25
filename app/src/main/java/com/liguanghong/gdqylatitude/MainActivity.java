package com.liguanghong.gdqylatitude;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ZoomControls;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.liguanghong.gdqylatitude.fragment.AddressbookFragment;
import com.liguanghong.gdqylatitude.fragment.MapFragment;
import com.liguanghong.gdqylatitude.fragment.MessageFragment;
import com.liguanghong.gdqylatitude.fragment.MineFragment;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        /*
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
        //mMapView.showScaleControl(true);
        // 隐藏缩放控件
        mMapView.showZoomControls(true);
        */
        initView();
        getData();

    }

    private void initView(){
        Fragment mapFragment = new MapFragment();
        Fragment messageFragment = new MessageFragment();
        Fragment addressbookFragment = new AddressbookFragment();
        Fragment mineFragment = new MineFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.contentPanel, mapFragment, mapFragment.getClass().getName());
        fragmentTransaction.add(R.id.contentPanel, messageFragment, messageFragment.getClass().getName());
        fragmentTransaction.add(R.id.contentPanel, addressbookFragment, addressbookFragment.getClass().getName());
        fragmentTransaction.add(R.id.contentPanel, mineFragment, mineFragment.getClass().getName());
        fragmentTransaction.show(mapFragment);
        fragmentTransaction.hide(messageFragment);
        fragmentTransaction.hide(addressbookFragment);
        fragmentTransaction.hide(mineFragment);
        fragmentTransaction.commit();
    }

    private void getData(){
        new Thread(){
            public void run(){
                Log.i("测试", "连接开始");
                //创建okHttpClient对象
                OkHttpClient mOkHttpClient = new OkHttpClient();
                //创建一个Request
                RequestBody requestBody = new FormBody.Builder()
                        .add("id","1")
                        .build();
                Request request = new Request.Builder()
                        .url("http://172.17.144.204:8080/ConfigureSSM/admin/queryById.do")
                        .post(requestBody)
                        .build();
                //new call
                Call call = mOkHttpClient.newCall(request);
                //请求加入调度
                call.enqueue(new Callback()
                {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("测试失败", "sss");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.i("测试", response.body().string().toString());
                    }

                });
            }
        }.start();
    }
}
