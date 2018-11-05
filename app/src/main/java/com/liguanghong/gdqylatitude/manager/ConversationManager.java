package com.liguanghong.gdqylatitude.manager;

import com.liguanghong.gdqylatitude.activitys.ChatActivity;
import com.liguanghong.gdqylatitude.fragment.MessageFragment;
import com.liguanghong.gdqylatitude.unity.Chatmessage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 会话管理类
 */
public class ConversationManager {

    //会话列表
    private static Map<Integer, List<Chatmessage>> chatmessageMap = new LinkedHashMap<Integer, List<Chatmessage>>();

    /**
     * 接收消息
     * @param chatmessage
     */
    public static void addChatmessage(Chatmessage chatmessage){
        List<Chatmessage> chatmessageList = chatmessageMap.get(chatmessage.getSenderid());
        if(chatmessageList == null){
            chatmessageList = new ArrayList<Chatmessage>();
            chatmessageList.add(chatmessage);
            chatmessageMap.put(chatmessage.getSenderid(), chatmessageList);
        } else{
            chatmessageList.add(chatmessage);
        }

        if(MessageFragment.getMessageHandler() != null)
            MessageFragment.getMessageHandler().sendEmptyMessage(222);
        if(ChatActivity.getChatHandler() != null)
            ChatActivity.getChatHandler().sendEmptyMessage(222);

    }

    /**
     * 获取会话列表
     * @return
     */
    public static Map<Integer, List<Chatmessage>> getChatmessageMap(){
        return chatmessageMap;
    }

    /**
     * 获取指定会话所有消息
     * @param chatmessageName
     * @return
     */
    public static List<Chatmessage> getChatmessageListByName(Integer chatmessageName){
        if(chatmessageMap.get(chatmessageName) == null)
            return new ArrayList<Chatmessage>();
        return chatmessageMap.get(chatmessageName);
    }

    public static List<Chatmessage> getChatmessageListByIndex(int index){
        int i = 0;
        for (Map.Entry<Integer, List<Chatmessage>> entry : ConversationManager.getChatmessageMap().entrySet()) {
            if(i == index){
                return entry.getValue();
            }
            i++;
        }
        return null;
    }

    public static long getChatmessageNameByIndex(int index){
        int i = 0;
        for (Map.Entry<Integer, List<Chatmessage>> entry : ConversationManager.getChatmessageMap().entrySet()) {
            if(i == index){
                return entry.getKey();
            }
            i++;
        }
        return -1;
    }
}
