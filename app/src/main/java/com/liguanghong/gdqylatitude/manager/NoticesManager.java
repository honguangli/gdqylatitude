package com.liguanghong.gdqylatitude.manager;

import com.liguanghong.gdqylatitude.unity.ChatMsg;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NoticesManager {

    private static Map<Integer, List<ChatMsg>> systemNotices = new LinkedHashMap<>();        //系统通知列表
    private static Map<Integer, List<ChatMsg>> friendNotices = new LinkedHashMap<>();        //好友通知列表
    private static Map<Integer, List<ChatMsg>> groupNotices = new LinkedHashMap<>();        //群聊通知列表

    /**
     * 获取系统通知列表
     * @return
     */
    public static Map<Integer, List<ChatMsg>> getSystemNotices(){
        return systemNotices;
    }

    /**
     * 获取好友通知列表
     * @return
     */
    public static Map<Integer, List<ChatMsg>> getFriendNotices(){
        return friendNotices;
    }

    /**
     * 获取群聊通知列表
     * @return
     */
    public static Map<Integer, List<ChatMsg>> getGroupNotices(){
        return groupNotices;
    }

    public static List<ChatMsg> getNoticesByIndex(int index){
        int i = 0;
        for (Map.Entry<Integer, List<ChatMsg>> entry : ConversationManager.getMsgMap().entrySet()) {
            if(i == index){
                return entry.getValue();
            }
            i++;
        }
        return new ArrayList<>();
    }

    /**
     * 收到系统通知
     * @param chatMsg
     */
    public static void addSystemNotice(ChatMsg chatMsg){
        if(getSystemNotices().get(chatMsg.getSenderid()) != null){
            List<ChatMsg> list = new ArrayList<>();
            list.add(chatMsg);
            getSystemNotices().put(chatMsg.getSenderid(), list);
        } else{
            getSystemNotices().get(chatMsg.getSenderid()).add(chatMsg);
        }
    }

    /**
     * 收到好友通知
     * @param chatMsg
     */
    public static void addFriendNotice(ChatMsg chatMsg){
        if(getFriendNotices().get(chatMsg.getSenderid()) != null){
            List<ChatMsg> list = new ArrayList<>();
            list.add(chatMsg);
            getFriendNotices().put(chatMsg.getSenderid(), list);
        } else{
            getFriendNotices().get(chatMsg.getSenderid()).add(chatMsg);
        }
    }

    /**
     * 收到群聊通知
     * @param chatMsg
     */
    public static void addGroupNotice(ChatMsg chatMsg){
        if(getGroupNotices().get(chatMsg.getSenderid()) != null){
            List<ChatMsg> list = new ArrayList<>();
            list.add(chatMsg);
            getGroupNotices().put(chatMsg.getSenderid(), list);
        } else{
            getGroupNotices().get(chatMsg.getSenderid()).add(chatMsg);
        }
    }

}
