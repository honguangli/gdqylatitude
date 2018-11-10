package com.liguanghong.gdqylatitude.manager;

import com.liguanghong.gdqylatitude.activitys.ChatActivity;
import com.liguanghong.gdqylatitude.fragment.MessageFragment;
import com.liguanghong.gdqylatitude.unity.ChatMsg;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 会话管理类
 */
public class ConversationManager {

    private static Map<Integer, List<ChatMsg>> msgMap = new LinkedHashMap<>();

    /**
     * 获取所有会话列表
     * @return
     */
    public static Map<Integer, List<ChatMsg>> getMsgMap(){
        return msgMap;
    }

    /**
     * 获取指定会话消息列表，入参：好友id/群聊id
     * @param userid
     * @return
     */
    public static List<ChatMsg> getMsgList(Integer userid){
        if(msgMap.get(userid) == null)
            return new ArrayList<>();
        return msgMap.get(userid);
    }

    /**
     * 接收消息
     */
    public static void receiveMsg(ChatMsg chatMsg){
        if(msgMap.get(chatMsg.getSenderid()) != null){
            //已经有会话记录
            msgMap.get(chatMsg.getSenderid()).add(chatMsg);
        } else{
            //还没有会话记录
            List<ChatMsg> list = new ArrayList<>();
            list.add(chatMsg);
            msgMap.put(chatMsg.getSenderid(), list);
        }
        notifyDataSetChanged();
    }

    /**
     * 发送消息
     */
    public static void sendMsg(ChatMsg chatMsg){
        if(msgMap.get(chatMsg.getReceiverid()) != null){
            //已经有会话记录
            msgMap.get(chatMsg.getReceiverid()).add(chatMsg);
        } else{
            //还没有会话记录
            List<ChatMsg> list = new ArrayList<>();
            list.add(chatMsg);
            msgMap.put(chatMsg.getReceiverid(), list);
        }
        notifyDataSetChanged();
    }

    /**
     * 根据会话序号查找会话消息列表
     * @param index
     * @return
     */
    public static List<ChatMsg> getMsgListByIndex(int index){
        int i = 0;
        for (Map.Entry<Integer, List<ChatMsg>> entry : ConversationManager.getMsgMap().entrySet()) {
            if(i == index){
                return entry.getValue();
            }
            i++;
        }
        return new ArrayList<>();
    }

    public static Integer getMsgKeyByIndex(int index){
        int i = 0;
        for (Map.Entry<Integer, List<ChatMsg>> entry : ConversationManager.getMsgMap().entrySet()) {
            if(i == index){
                return entry.getKey();
            }
            i++;
        }
        return -1;
    }

    /**
     * 提醒UI做改变
     */
    private static void notifyDataSetChanged(){
        if(MessageFragment.getMessageHandler() != null)
            MessageFragment.getMessageHandler().sendEmptyMessage(222);
        if(ChatActivity.getChatHandler() != null)
            ChatActivity.getChatHandler().sendEmptyMessage(222);
    }

}
