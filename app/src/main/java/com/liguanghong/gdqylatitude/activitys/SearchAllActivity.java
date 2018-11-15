package com.liguanghong.gdqylatitude.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.SearchAllAdapter;
import com.liguanghong.gdqylatitude.base.BaseActivity;
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
    private LinearLayout linearLayout_add_friend;
    private LinearLayout linearLayout_add_Group;
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
        linearLayout_add_friend = findViewById(R.id.LinearLayout_add_friend);
        linearLayout_add_Group = findViewById(R.id.LinearLayout_add_group);
        bt_search = (Button)findViewById(R.id.bt_search);
        et_keyword = findViewById(R.id.keyword);

        search_selcet_user = findViewById(R.id.search_selcet_user);
        search_selcet_group = findViewById(R.id.search_selcet_group);
        search_all_listview = findViewById(R.id.search_all_listview);

        backtrack.setOnClickListener(this);
        bt_search.setOnClickListener(this);
        linearLayout_add_friend.setOnClickListener(this);
        linearLayout_add_Group.setOnClickListener(this);
        search_all_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = (User)searchAllAdapter.getItem(i);
                Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
                intent.putExtra("hide", true);
                intent.putExtra("userinfo", user);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        searchAllHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 200:
                        searchAllList.clear();
                        searchAllList.addAll((List<User>)message.obj);
                        searchAllAdapter.notifyDataSetChanged();
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
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(et_keyword.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);//隐藏软键盘
                finish();
                break;

            case R.id.LinearLayout_add_friend:
                change(true);
                break;

            case R.id.LinearLayout_add_group:
                change(false);
                break;

        }

    }

    /**
     * 更改搜索引擎-搜用户/搜群聊
     * @param selectuser
     */
    private void change(boolean selectuser){

        if(selectuser){
            search_selcet_user.setVisibility(View.VISIBLE);
            search_selcet_group.setVisibility(View.GONE);
        } else{
            search_selcet_user.setVisibility(View.GONE);
            search_selcet_group.setVisibility(View.VISIBLE);
        }

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
