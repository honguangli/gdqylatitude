package com.liguanghong.gdqylatitude.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
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
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.JsonResult;
import com.liguanghong.gdqylatitude.util.UserManager;
import com.liguanghong.gdqylatitude.view.QPopuWindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
    public static AddressbookAdapter addressbookAdapter;

    int[] location = new  int[2] ;
    private int rawX;
    private int rawY;

    private List<FriendsSet> friendsSets;
    private List<String> friendsSetName;
    private Map<String, List<String>> friendsSetMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return super.onCreateView(inflater, container, savedInstanceState);
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

        User user = UserManager.getAppUser();
        Log.i("通讯录", user.getFriendsid());
        friendsSets = JSONArray.parseArray(user.getFriendsid(), FriendsSet.class);
        friendsSetName = new ArrayList<String>();
        friendsSetMap = new HashMap<String, List<String>>();
        for(final FriendsSet friendsSet : friendsSets){
            friendsSetName.add(friendsSet.getName());

            final List<String> temp = new ArrayList<String>();
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
                                    JsonResult<Object> result = com.alibaba.fastjson.JSONObject.parseObject(response.body().string(), JsonResult.class);
                                    Log.i("测试输出", "用户："+result.getData().toString());
                                    User user = com.alibaba.fastjson.JSONObject.parseObject(result.getData().toString(), User.class);
                                    Log.i("测试输出", "用户名："+user.getLogname());
                                    temp.add(user.getLogname());
                                    friendsSetMap.put(friendsSet.getName(), temp);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
                }
            }else {
                friendsSetMap.put(friendsSet.getName(), temp);
            }
        }
        addressbookAdapter = new AddressbookAdapter(getActivity().getApplicationContext(), friendsSetName, friendsSetMap);
        expandableListView.setAdapter(addressbookAdapter);

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.add:
                Toast.makeText(getActivity(),"搜索+",Toast.LENGTH_LONG).show();
                Intent img_search = new Intent(getActivity(), SearchAllActivity.class);
                getActivity().startActivityForResult(img_search,1);
                break;

            case R.id.edittext_search:
                Toast.makeText(getActivity(),"搜索",Toast.LENGTH_LONG).show();
                Intent btn_search = new Intent(getActivity(), SearchMineActivity.class);
                getActivity().startActivityForResult(btn_search,1);
                break;

            case R.id.rly_new_friends:
                Toast.makeText(getActivity(),"新朋友",Toast.LENGTH_LONG).show();
                Intent friend_apply = new Intent(getActivity(), FriendsNoticeActivity.class);
                getActivity().startActivityForResult(friend_apply,1);
                break;

            case R.id.rly_chat_group:
                Toast.makeText(getActivity(),"群聊",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), GroupActivity.class);
                getActivity().startActivityForResult(intent,1);
                break;
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//        String str = "choose"+groupPosition+"-"+childPosition;
//        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
        Intent fenzu_manage = new Intent(getActivity(), ChatActivity.class);
        getActivity().startActivityForResult(fenzu_manage,1);

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //长按组
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
                            Toast.makeText(getContext(),anchorViewPosition+"---->"+position,Toast.LENGTH_SHORT).show();
                            Intent fenzu_manage = new Intent(getActivity(), FriendsSetManageActivity.class);
                            getActivity().startActivityForResult(fenzu_manage,1);
                        }
                    }).show();;
        }
        return true;           //默认为false，设为true时，长按/点击事件不会展开Group
    }

    //新增组
    public void addGroup(String newGroupName){
        friendsSetName.add(newGroupName);
        List<String> list = new ArrayList<String>();
        friendsSetMap.put(newGroupName, list);
        addressbookAdapter.notifyDataSetChanged();
    }

    //新增子项到指定组
    public void addChild(int groupPosition, String newChildName){
        String groupName = friendsSetName.get(groupPosition);
        List<String> list = friendsSetMap.get(groupName);
        list.add(newChildName);
        addressbookAdapter.notifyDataSetChanged();
    }

    //删除指定组
    public void deleteGroup(int groupPos){
        String groupName = friendsSetName.get(groupPos);
        friendsSetMap.remove(groupName);
        friendsSetName.remove(groupPos);
        addressbookAdapter.notifyDataSetChanged();
    }
    //删除指定子项
    public void deleteChild(int groupPos, int childPos){
        String groupName = friendsSetName.get(groupPos);
        List<String> list = friendsSetMap.get(groupName);
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
                friendsSetMap.put(modifyName, friendsSetMap.get(groupName));
                friendsSetMap.remove(groupName);
                friendsSetName.set(groupPosition, modifyName);
            }

        }else{
            //修改子项名称
            String group = friendsSetName.get(groupPosition);
            List<String> list =friendsSetMap.get(group);
            list.set(childPosition, modifyName);
            friendsSetMap.put(group, list);
        }
        addressbookAdapter.notifyDataSetChanged();
    }


}
