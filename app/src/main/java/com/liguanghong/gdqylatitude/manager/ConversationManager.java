package com.liguanghong.gdqylatitude.manager;

import android.util.Log;

import com.liguanghong.gdqylatitude.activitys.ChatActivity;
import com.liguanghong.gdqylatitude.fragment.MessageFragment;
import com.liguanghong.gdqylatitude.unity.ChatMapKey;
import com.liguanghong.gdqylatitude.unity.ChatMsg;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 会话管理类
 */
public class ConversationManager {

    private static ConversationManager instance = null;
    private Map<ChatMapKey, List<ChatMsg>> msgMap = new LinkedHashMap<>();

    //单例模式
    public static ConversationManager getInstance() {
        if (instance == null) {
            synchronized (ConversationManager.class) {
                if (instance == null) {
                    instance = new ConversationManager();
                }
            }
        }
        return instance;
    }
    //释放资源
    public static void releaseResource(){
        Log.i("会话管理器", "释放资源");
        instance = null;
    }

    /**
     * 获取所有会话列表
     * @return
     */
    public Map<ChatMapKey, List<ChatMsg>> getMsgMap(){
        return msgMap;
    }

    /**
     * 获取指定会话消息列表，入参：好友id/群聊id， 是否群聊
     * @param targetID
     * @param isSingle
     * @return
     */
    public List<ChatMsg> getMsgList(Integer targetID, boolean isSingle){
        for (Map.Entry<ChatMapKey, List<ChatMsg>> entry : getMsgMap().entrySet()) {
            ChatMapKey entryKey = entry.getKey();
            if(entryKey.isSingle() == isSingle && entryKey.getTargetID().equals(targetID)){
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * 接收消息
     */
    public void receiveMsg(ChatMsg chatMsg){
        Log.i("会话管理器", "接收消息：私聊：" + chatMsg.getIssingle() +  ", 发送者ID：" + chatMsg.getSenderid() + "， 接收者ID：" + chatMsg.getReceiverid());
        ChatMapKey chatMapKey = new ChatMapKey();
        chatMapKey.setSingle(chatMsg.getIssingle());
        if(chatMsg.getIssingle()) {
            chatMapKey.setTargetID(chatMsg.getSenderid());
            //私聊
            if(getMsgList(chatMapKey.getTargetID(), chatMapKey.isSingle()) != null){
                getMsgList(chatMapKey.getTargetID(), chatMapKey.isSingle()).add(chatMsg);
            } else{
                List<ChatMsg> list = new ArrayList<>();
                list.add(chatMsg);
                getMsgMap().put(chatMapKey, list);
            }
        } else{
            chatMapKey.setTargetID(chatMsg.getReceiverid());
            //群聊
            if(getMsgList(chatMapKey.getTargetID(), chatMapKey.isSingle()) != null){
                getMsgList(chatMapKey.getTargetID(), chatMapKey.isSingle()).add(chatMsg);
            } else{
                List<ChatMsg> list = new ArrayList<>();
                list.add(chatMsg);
                getMsgMap().put(chatMapKey, list);
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 发送消息
     */
    public void sendMsg(ChatMsg chatMsg){
        Log.i("会话管理器", "发送消息：私聊：" + chatMsg.getIssingle() +  ", 发送者ID：" + chatMsg.getSenderid() + "， 接收者ID：" + chatMsg.getReceiverid());
        ChatMapKey chatMapKey = new ChatMapKey();
        chatMapKey.setTargetID(chatMsg.getReceiverid());
        chatMapKey.setSingle(chatMsg.getIssingle());
        if(getMsgList(chatMapKey.getTargetID(), chatMapKey.isSingle()) != null){
            //已经有会话记录
            getMsgList(chatMapKey.getTargetID(), chatMapKey.isSingle()).add(chatMsg);
        } else{
            //还没有会话记录
            List<ChatMsg> list = new ArrayList<>();
            list.add(chatMsg);
            getMsgMap().put(chatMapKey, list);
        }
        notifyDataSetChanged();
    }

    /**
     * 根据会话序号查找会话消息列表
     * @param index
     * @return
     */
    public List<ChatMsg> getMsgListByIndex(int index){
        int i = 0;
        for (Map.Entry<ChatMapKey, List<ChatMsg>> entry : getMsgMap().entrySet()) {
            if(i == index){
                return entry.getValue();
            }
            i++;
        }
        return new ArrayList<>();
    }

    public Integer getMsgKeyByIndex(int index){
        int i = 0;
        for (Map.Entry<ChatMapKey, List<ChatMsg>> entry : getMsgMap().entrySet()) {
            if(i == index){
                return entry.getKey().getTargetID();
            }
            i++;
        }
        return -1;
    }
    /**
     * 提醒UI做改变
     */
    private void notifyDataSetChanged(){
        if(MessageFragment.getMessageHandler() != null)
            MessageFragment.getMessageHandler().sendEmptyMessage(222);
        if(ChatActivity.getChatHandler() != null)
            ChatActivity.getChatHandler().sendEmptyMessage(222);
    }

}
