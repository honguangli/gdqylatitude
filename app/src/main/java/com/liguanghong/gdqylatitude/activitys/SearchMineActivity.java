package com.liguanghong.gdqylatitude.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.adapter.SearchMineAdapter;
import com.liguanghong.gdqylatitude.base.BaseActivity;
import com.liguanghong.gdqylatitude.manager.FriendsManager;
import com.liguanghong.gdqylatitude.unity.Friend;
import com.liguanghong.gdqylatitude.unity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchMineActivity extends BaseActivity implements View.OnClickListener {

    private ImageView backtrack_search,search_add;
    private TextView button_search_cancel;
    private TextView search_friend_title;
    private TextView search_friend_more;
    private EditText edittext_search;
    private ListView friendsListView;
    private ListView groupsListView;
    private SearchMineAdapter searchMineAdapter;
    private List<Friend> searchFriendList;
    private List<Friend> searchGroupList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_mine;
    }

    @Override
    protected void initView() {
        button_search_cancel = (TextView)findViewById(R.id.button_search_cancel);
        search_friend_title = findViewById(R.id.textview_search_friend);
        search_friend_more = findViewById(R.id.search_more);
        backtrack_search = (ImageView) findViewById(R.id.backtrack);
        search_add = (ImageView) findViewById(R.id.menu);
        edittext_search =(EditText)findViewById(R.id.edittext_search);
        friendsListView = findViewById(R.id.friendsListView);

        button_search_cancel.setOnClickListener(this);
        backtrack_search.setOnClickListener(this);
        search_add.setOnClickListener(this);
        edittext_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(edittext_search.getText().toString() == null || edittext_search.getText().toString().equals("")){
                    clearList();
                }else{
                    setSearchFriendList();
                    setSearchGroupList();
                }
            }
        });
    }

    @Override
    protected void initData() {
        searchFriendList = new ArrayList<>();
        searchMineAdapter = new SearchMineAdapter(this, searchFriendList);
        friendsListView.setAdapter(searchMineAdapter);

        searchGroupList = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.menu:
                startActivity(new Intent(this, SearchAllActivity.class));
                break;

            case R.id.button_search_cancel:             //取消
                edittext_search.setText(null);
                break;

            case R.id.backtrack:
                //销毁当前界面，返回通讯录界面
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edittext_search.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);//隐藏软键盘
                finish();
                break;
        }
    }

    private void setSearchFriendList(){
        List<Friend> newList = new ArrayList<>();
        Map<String, List<Friend>> map  = FriendsManager.getFriends();
        for (Map.Entry<String, List<Friend>> entry : map.entrySet()) {
            for(Friend friend : entry.getValue()){
                if(friend.getFriend().getLogname().contains(edittext_search.getText().toString().trim())
                        || (friend.getRemark() != null && friend.getRemark().contains(edittext_search.getText().toString().trim()))){
                    newList.add(friend);
                }
            }
        }
        searchFriendList.clear();
        searchFriendList.addAll(newList);
        searchMineAdapter.notifyDataSetChanged();
        if(searchFriendList.size() > 0){
            search_friend_title.setVisibility(View.VISIBLE);
            if(searchFriendList.size() > 3){
                search_friend_more.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setSearchGroupList(){

    }

    private void clearList(){
        searchFriendList.clear();
        searchMineAdapter.notifyDataSetChanged();
        search_friend_title.setVisibility(View.GONE);
        search_friend_more.setVisibility(View.GONE);
        searchGroupList.clear();
    }

}
