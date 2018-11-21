package com.liguanghong.gdqylatitude.manager;

import com.liguanghong.gdqylatitude.unity.NoticeMsg;

import java.util.ArrayList;
import java.util.List;

public class NoticesManager {

    private static List<NoticeMsg> systemNotices = new ArrayList<>();        //系统通知列表
    private static List<NoticeMsg> friendNotices = new ArrayList<>();        //好友通知列表
    private static List<NoticeMsg> groupNotices = new ArrayList<>();        //群聊通知列表

    /**
     * 获取系统通知列表
     * @return
     */
    public static List<NoticeMsg> getSystemNotices(){
        return systemNotices;
    }

    public static void setSystemNotices(List<NoticeMsg> list){
        if(list != null)
            systemNotices = list;
    }

    /**
     * 获取好友通知列表
     * @return
     */
    public static List<NoticeMsg> getFriendNotices(){
        return friendNotices;
    }

    /**
     * 设置好友通知列表
     * @param list
     */
    public static void setFriendNotices(List<NoticeMsg> list){
        if(list != null)
            friendNotices = list;
    }

    /**
     * 获取群聊通知列表
     * @return
     */
    public static List<NoticeMsg> getGroupNotices(){
        return groupNotices;
    }

    /**
     * 设置群聊通知列表
     * @param list
     */
    public static void setGroupNotices(List<NoticeMsg> list){
        if(list != null)
            groupNotices = list;
    }

    /**
     * 收到系统通知
     * @param noticeMsg
     */
    public static void addSystemNotice(NoticeMsg noticeMsg){
        systemNotices.add(noticeMsg);
    }

    /**
     * 收到好友通知
     * @param noticeMsg
     */
    public static void addFriendNotice(NoticeMsg noticeMsg){
       friendNotices.add(noticeMsg);
    }

    /**
     * 收到群聊通知
     * @param noticeMsg
     */
    public static void addGroupNotice(NoticeMsg noticeMsg){
        groupNotices.add(noticeMsg);
    }

    /**
     * 清除系统通知
     */
    public static void clearSystemNotices(){
        systemNotices.clear();
    }

    /**
     * 清除好友通知
     */
    public static void clearFriendNotices(){
        friendNotices.clear();
    }

    /**
     * 清除群聊通知
     */
    public static void clearGroupNotices(){
        groupNotices.clear();
    }

}
