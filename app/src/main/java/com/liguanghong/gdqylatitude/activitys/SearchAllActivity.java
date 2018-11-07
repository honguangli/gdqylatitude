package com.liguanghong.gdqylatitude.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.SearchAllAdapter;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.UserManager;
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

public class SearchAllActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backtrack;
    private TextView tv_add_friend;
    private TextView tv_add_group;
    private View search_selcet_user;
    private View search_selcet_group;
    private Button bt_search;
    private EditText et_keyword;
    private ListView search_all_listview;
    private int myPage = 1;
    private List<User> searchAllList;
    private SearchAllAdapter searchAllAdapter;
    private Handler searchAllHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_all;
    }

    @Override
    protected void initView() {
        backtrack = (ImageView)findViewById(R.id.backtrack);
        tv_add_friend = (TextView) findViewById(R.id.tv_add_friend);
        tv_add_group = (TextView)findViewById(R.id.tv_add_group);
        bt_search = (Button)findViewById(R.id.bt_search);
        et_keyword = findViewById(R.id.keyword);

        search_selcet_user = findViewById(R.id.search_selcet_user);
        search_selcet_group = findViewById(R.id.search_selcet_group);
        search_all_listview = findViewById(R.id.search_all_listview);

        tv_add_friend.setOnClickListener(this);
        tv_add_group.setOnClickListener(this);
        backtrack.setOnClickListener(this);
        bt_search.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        searchAllHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 200:
                        add((List<User>)message.obj);
                        break;
                }
            }
        };
        searchAllList = new ArrayList<>();
        searchAllAdapter = new SearchAllAdapter(this, searchAllList);
        search_all_listview.setAdapter(searchAllAdapter);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.bt_search:
                //执行网络查找
                if(et_keyword.getText().toString().trim() != null && !et_keyword.getText().toString().trim().equals("")){
                    search(et_keyword.getText().toString().trim(), 1);
                }
                break;

            case R.id.backtrack:
                //销毁当前界面，返回通讯录界面
                finish();
                break;
            case R.id.tv_add_friend:
                change(true);
                break;
            case R.id.tv_add_group:
                change(false);
                break;
        }
    }

    private void change(boolean selectuser){
        if(selectuser){
            search_selcet_user.setVisibility(View.VISIBLE);
            search_selcet_group.setVisibility(View.GONE);
        } else{
            search_selcet_user.setVisibility(View.GONE);
            search_selcet_group.setVisibility(View.VISIBLE);
        }
    }

    private void add(List<User> newList){
        searchAllList.addAll(newList);
        searchAllAdapter.notifyDataSetChanged();
    }

    private void search(String keyword, int page){
        final RequestBody requestBody = new FormBody.Builder()
                .add("keyword", keyword)
                .add("page", page+"")
                .build();
        HttpUtil.postEnqueue("user/search", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("查询操作",  "查询失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        if(result.isSuccess()){
                            //查询成功
                            JSONObject list = JSONObject.parseObject(result.getData().toString());
                            List<User> userList = JSONArray.parseArray(((JSONArray)list.get("list")).toJSONString(), User.class);
                            Message message = new Message();
                            message.what = 200;
                            message.obj = userList;
                            searchAllHandler.sendMessage(message);
                        } else{
                            //查询失败

                        }
                        Log.i("查询操作",  result.isSuccess() + "," + result.getMessage());
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

}
