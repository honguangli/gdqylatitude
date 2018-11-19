package com.liguanghong.gdqylatitude.manager;

import com.liguanghong.gdqylatitude.unity.Groupchat;

import java.util.ArrayList;
import java.util.List;

public class GroupManager {

    private static List<Groupchat> groupchatList = new ArrayList<>();

    /**
     * 设置群组列表
     * @param list
     */
    public static void setGroupchatList(List<Groupchat> list){
        if(list != null){
            groupchatList = list;
        }
    }

    /**
     * 获取群组列表
     * @return
     */
    public static List<Groupchat> getGroupchatList(){
        return groupchatList;
    }

    /**
     * 获取创建的群组列表
     * @return
     */
    public static List<Groupchat> getMyGroupchatList(){
        List<Groupchat> list = new ArrayList<>();
        for(Groupchat tempGroup : groupchatList){
            if(tempGroup.getOwnerid().equals(UserManager.getAppUser().getUserid())){
                list.add(tempGroup);
            }
        }
        return list;
    }

    /**
     * 获取我加入的群组列表
     * @return
     */
    public static List<Groupchat> getJoininGroupchatList(){
        List<Groupchat> list = new ArrayList<>();
        for(Groupchat tempGroup : groupchatList){
            if(!tempGroup.getOwnerid().equals(UserManager.getAppUser().getUserid())){
                list.add(tempGroup);
            }
        }
        return list;
    }

    /**
     * 获取指定群聊
     * @param groupID
     * @return
     */
    public static Groupchat getGroupByID(Integer groupID){
        for(Groupchat groupchat : groupchatList){
            if(groupchat.getGroupid().equals(groupID)){
                return groupchat;
            }
        }
        return null;
    }

    /**
     * 添加群聊
     * @param newGroup
     */
    public static void addGroup(Groupchat newGroup){
        groupchatList.add(newGroup);
    }

    /**
     * 移除群聊
     * @param newGroup
     */
    public static void removeGroup(Groupchat newGroup){
        groupchatList.remove(newGroup);
    }

}
