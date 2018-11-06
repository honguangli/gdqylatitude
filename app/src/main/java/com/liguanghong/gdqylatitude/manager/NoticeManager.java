package com.liguanghong.gdqylatitude.manager;

import android.icu.util.Freezable;

import com.alibaba.fastjson.JSONObject;
import com.liguanghong.gdqylatitude.activitys.FriendsNoticeActivity;
import com.liguanghong.gdqylatitude.unity.Chatmessage;
import com.liguanghong.gdqylatitude.unity.User;
import com.liguanghong.gdqylatitude.util.HttpUtil;
import com.liguanghong.gdqylatitude.util.JsonResult;

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

/**
 * 通知管理类
 */
public class NoticeManager {

    private static Map<User, Chatmessage> friendsNotice = new LinkedHashMap<>();        //好友通知列表
    private static Map<User, Chatmessage> groupNotice = new LinkedHashMap<>();          //群聊通知列表

    /**
     * 添加好友通知
     * @param chatmessage
     */
    public static void addFriendsNotice(final Chatmessage chatmessage){
        //网络访问获取用户信息
        RequestBody requestBody = new FormBody.Builder()
                .add("userid", chatmessage.getSenderid() + "")
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
                        friendsNotice.put(user, chatmessage);
                        if(FriendsNoticeActivity.getFriendsNoticeHandler() != null)
                            FriendsNoticeActivity.getFriendsNoticeHandler().sendEmptyMessage(222);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    /**
     * 获取好友通知列表
     * @return
     */
    public static Map<User, Chatmessage> getFriendsNotice(){
        return friendsNotice;
    }

    public static Map.Entry<User, Chatmessage> getFriendNoticeByIndex(int index){
        int i = 0;
        for (Map.Entry<User, Chatmessage> entry : getFriendsNotice().entrySet()) {
            if(i == index){
                return entry;
            }
            i++;
        }
        return null;
    }

    /**
     * 改变好友通知状态
     * @param index
     * @param type
     */
    public static void changeFriendsNotice(int index, Integer type){
        friendsNotice.get(index).setType(type);
        if(FriendsNoticeActivity.getFriendsNoticeHandler() != null)
            FriendsNoticeActivity.getFriendsNoticeHandler().sendEmptyMessage(222);
    }

    /**
     * 添加群聊通知
     * @param chatmessage
     */
    public static void addGroupNotice(Chatmessage chatmessage){

    }

    /**
     * 获取群聊通知列表
     * @return
     */
    public static Map<User, Chatmessage> getGroupNotice(){
        return groupNotice;
    }

    /**
     * 改变群聊通知状态
     * @param index
     * @param type
     */
    public static void changeGroupsNotice(int index, Integer type){

    }

}
