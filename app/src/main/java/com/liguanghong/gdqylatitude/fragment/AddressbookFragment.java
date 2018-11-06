package com.liguanghong.gdqylatitude.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.liguanghong.gdqylatitude.R;
import com.liguanghong.gdqylatitude.activitys.FriendsSetManageActivity;
import com.liguanghong.gdqylatitude.activitys.FriendsNoticeActivity;
import com.liguanghong.gdqylatitude.activitys.ChatActivity;
import com.liguanghong.gdqylatitude.activitys.GroupActivity;
import com.liguanghong.gdqylatitude.activitys.SearchAllActivity;
import com.liguanghong.gdqylatitude.activitys.SearchMineActivity;
import com.liguanghong.gdqylatitude.adapter.AddressbookAdapter;
import com.liguanghong.gdqylatitude.base.BaseFragment;
import com.liguanghong.gdqylatitude.unity.FriendsSet;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.manager.FriendsManager;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.JsonResult;
import com.liguanghong.gdqylatitude.manager.UserManager;
import com.liguanghong.gdqylatitude.view.QPopuWindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddressbookFragment extends BaseFragment implements View.OnClickListener, ExpandableListView.OnChildClickListener, AdapterView.OnItemLongClickListener {

    private EditText img_search;
    private ImageView add;
    private RelativeLayout rly_new_friends;
    private RelativeLayout rly_chat_group;

    private ExpandableListView expandableListView;
    private AddressbookAdapter addressbookAdapter;

    private int[] location = new  int[2] ;
    private int rawX;
    private int rawY;

    private Handler addressbookHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutId(){
        return R.layout.fragment_addressbook;
    }

    //初始化组件
    @Override
    protected void initView(View view)
    {

        add = (ImageView) view.findViewById(R.id.add);
        img_search = (EditText) view.findViewById(R.id.edittext_search);
        rly_new_friends = (RelativeLayout) view.findViewById(R.id.rly_new_friends);
        rly_chat_group = (RelativeLayout)view.findViewById(R.id.rly_chat_group);

        add.setOnClickListener(this);
        img_search.setOnClickListener(this);
        rly_new_friends.setOnClickListener(this);
        rly_chat_group.setOnClickListener(this);

        expandableListView = (ExpandableListView) view.findViewById(R.id.expandablelistview);
        //设置子项点击事件
        expandableListView.setOnChildClickListener(this);
        //设置长按点击事件
        expandableListView.setOnItemLongClickListener(this);

    }


    //初始化数据
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void initData(){

        addressbookHandler = new Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 200:
                        addressbookAdapter = new AddressbookAdapter(getActivity().getApplicationContext());
                        expandableListView.setAdapter(addressbookAdapter);
                        break;
                }
            }
        };

        //网络访问获取用户信息
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", UserManager.getAppUser().getUserid()+"")
                .build();
        HttpUtil.postEnqueue("user/findfriends", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("通讯录", "失败了");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                        Map<String, List<User>> friends = JSONObject.parseObject( result.getData().toString(), new TypeReference<Map<String, List<User>>>() {});
                        FriendsManager.setFriends(friends);
                        addressbookHandler.sendEmptyMessage(200);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.add:
                startActivity(new Intent(getActivity(), SearchAllActivity.class));
                break;

            case R.id.edittext_search:
                startActivity(new Intent(getActivity(), SearchMineActivity.class));
                break;

            case R.id.rly_new_friends:
                startActivity(new Intent(getActivity(), FriendsNoticeActivity.class));
                break;

            case R.id.rly_chat_group:
                startActivity(new Intent(getActivity(), GroupActivity.class));
                break;
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        String setName = FriendsManager.getFriendsSetNameList().get(groupPosition);
        User user = FriendsManager.getFriendsBySetName(setName).get(childPosition);
        //启动聊天界面
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("friendInfo", user);
        startActivity(intent);
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //长按分组，显示分组管理窗
        if(ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP){
            view.getLocationInWindow(location);
            rawX = location[0] + view.getWidth()/2;
            rawY=  location[1];
            QPopuWindow.getInstance(getActivity()).builder
                    .bindView(view, 0)
                    .setPopupItemList(new String[]{"分组管理"})
                    .setPointers(rawX,rawY)
                    .setOnPopuListItemClickListener(new QPopuWindow.OnPopuListItemClickListener() {
                        @Override
                        public void onPopuListItemClick(View anchorView, int anchorViewPosition, int position) {
                            //启动分组管理界面
                            startActivity(new Intent(getActivity(), FriendsSetManageActivity.class));
                        }
                    }).show();;
        }
        return true;           //默认为false，设为true时，长按/点击事件不会展开Group
    }

    /*
    //新增组
    public void addGroup(String newGroupName){
        friendsSetName.add(newGroupName);
        List<User> list = new ArrayList<User>();
        friendsSetsMap.put(newGroupName, list);
        addressbookAdapter.notifyDataSetChanged();
    }

    //新增子项到指定组
    public void addChild(int groupPosition, User newChildName){
        String groupName = friendsSetName.get(groupPosition);
        List<User> list = friendsSetsMap.get(groupName);
        list.add(newChildName);
        addressbookAdapter.notifyDataSetChanged();
    }

    //删除指定组
    public void deleteGroup(int groupPos){
        String groupName = friendsSetName.get(groupPos);
        friendsSetsMap.remove(groupName);
        friendsSetName.remove(groupPos);
        addressbookAdapter.notifyDataSetChanged();
    }
    //删除指定子项
    public void deleteChild(int groupPos, int childPos){
        String groupName = friendsSetName.get(groupPos);
        List<User> list = friendsSetsMap.get(groupName);
        list.remove(childPos);
        addressbookAdapter.notifyDataSetChanged();
    }

    //修改该项名称
    public void modifyName(int groupPosition, int childPosition, String modifyName){
        Toast.makeText(getActivity(), String.valueOf(groupPosition)+'-'+String.valueOf(childPosition), Toast.LENGTH_SHORT).show();
        if(childPosition<0){
            //修改组名称
            String groupName = friendsSetName.get(groupPosition);
            if(!groupName.equals(modifyName)){
                friendsSetsMap.put(modifyName, friendsSetsMap.get(groupName));
                friendsSetsMap.remove(groupName);
                friendsSetName.set(groupPosition, modifyName);
            }

        }else{
            //修改子项名称
            String group = friendsSetName.get(groupPosition);
            List<User> list =friendsSetsMap.get(group);
            User user = list.get(childPosition);
            user.setLogname(modifyName);
            list.set(childPosition, user);
            friendsSetsMap.put(group, list);
        }
        addressbookAdapter.notifyDataSetChanged();
    }
    */

    /*
        private void test(){
        try{
            User user = UserManager.getAppUser();
            List<FriendsSet> friendsSets = JSONArray.parseArray(user.getFriendsid(), FriendsSet.class);
            friendsSetName = new ArrayList<String>();
            for(final FriendsSet friendsSet : friendsSets){
                friendsSetName.add(friendsSet.getName());

                final List<User> temp = new ArrayList<User>();
                if(!friendsSet.getFriendsid().equals("")){
                    //该分组下有好友
                    String [] friendsID = friendsSet.getFriendsid().split(",");
                    for(String friendID : friendsID){
                        //网络访问获取用户信息
                        RequestBody requestBody = new FormBody.Builder()
                                .add("userid", friendID)
                                .build();
                        HttpUtil.postEnqueue("user/find", requestBody, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if(response.isSuccessful()){
                                    try {
                                        JsonResult<Object> result = JSONObject.parseObject(response.body().string(), JsonResult.class);
                                        User user = JSONObject.parseObject(result.getData().toString(), User.class);
                                        temp.add(user);
                                        FriendsManager.addFriendsSet(friendsSet.getName(), temp);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                }
                            }
                        });
                    }
                }else {
                    FriendsManager.addFriendsSet(friendsSet.getName(), temp);
                }
            }
            addressbookAdapter = new AddressbookAdapter(getActivity().getApplicationContext());
            expandableListView.setAdapter(addressbookAdapter);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
     */


}
