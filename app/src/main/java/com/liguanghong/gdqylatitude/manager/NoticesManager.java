package com.liguanghong.gdqylatitude.manager;

import android.util.Log;

import com.liguanghong.gdqylatitude.activitys.FriendsNoticeActivity;
import com.liguanghong.gdqylatitude.unity.NoticeMsg;

import java.util.ArrayList;
import java.util.List;

public class NoticesManager {

    private static NoticesManager instance = null;
    private List<NoticeMsg> systemNotices = new ArrayList<>();        //系统通知列表
    private List<NoticeMsg> friendNotices = new ArrayList<>();        //好友通知列表
    private List<NoticeMsg> groupNotices = new ArrayList<>();        //群聊通知列表
    //单例模式
    public static NoticesManager getInstance() {
        if (instance == null) {
            synchronized (NoticesManager.class) {
                if (instance == null) {
                    instance = new NoticesManager();
                }
            }
        }
        return instance;
    }
    //释放资源
    public static void releaseResource(){
        Log.i("通知管理器", "释放资源");
        instance = null;
    }
    /**
     * 获取系统通知列表
     * @return
     */
    public List<NoticeMsg> getSystemNotices(){
        return systemNotices;
    }

    public void setSystemNotices(List<NoticeMsg> list){
        if(list != null)
            systemNotices = list;
    }

    /**
     * 获取好友通知列表
     * @return
     */
    public List<NoticeMsg> getFriendNotices(){
        return friendNotices;
    }

    /**
     * 设置好友通知列表
     * @param list
     */
    public void setFriendNotices(List<NoticeMsg> list){
        if(list != null)
            friendNotices = list;
        notifyDataSetChanged();
    }

    /**
     * 获取群聊通知列表
     * @return
     */
    public List<NoticeMsg> getGroupNotices(){
        return groupNotices;
    }

    /**
     * 设置群聊通知列表
     * @param list
     */
    public void setGroupNotices(List<NoticeMsg> list){
        if(list != null)
            groupNotices = list;
    }

    /**
     * 收到系统通知
     * @param noticeMsg
     */
    public void addSystemNotice(NoticeMsg noticeMsg){
        systemNotices.add(noticeMsg);
    }

    /**
     * 收到好友通知
     * @param noticeMsg
     */
    public void addFriendNotice(NoticeMsg noticeMsg){
        friendNotices.add(noticeMsg);
        notifyDataSetChanged();
    }

    /**
     * 收到群聊通知
     * @param noticeMsg
     */
    public void addGroupNotice(NoticeMsg noticeMsg){
        groupNotices.add(noticeMsg);
    }

    /**
     * 改变好友通知
     * @param friendid
     * @param status
     */
    public void changeFriendNoticeStatus(Integer friendid, Integer status){
        for(NoticeMsg msg : friendNotices){
            if(msg.getSenderid().equals(friendid)){
                msg.setNoticetype(status);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 清除系统通知
     */
    public void clearSystemNotices(){
        systemNotices.clear();
    }

    /**
     * 清除好友通知
     */
    public void clearFriendNotices(){
        friendNotices.clear();
        notifyDataSetChanged();
    }

    /**
     * 清除群聊通知
     */
    public void clearGroupNotices(){
        groupNotices.clear();
    }

    /**
     * 提醒UI改变
     */
    public void notifyDataSetChanged(){
        FriendsNoticeActivity.getFriendsNoticeHandler().sendEmptyMessage(200);
    }
}
