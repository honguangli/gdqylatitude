package com.liguanghong.gdqylatitude.manager;

import com.liguanghong.gdqylatitude.unity.Groupchat;

import java.util.ArrayList;
import java.util.List;

public class GroupManager {

    private static GroupManager instance = null;
    private List<Groupchat> groupchatList = new ArrayList<>();
    //单例模式
    public static GroupManager getInstance() {
        if (instance == null) {
            synchronized (GroupManager.class) {
                if (instance == null) {
                    instance = new GroupManager();
                }
            }
        }
        return instance;
    }
    //释放资源
    public static void releaseResource(){
        instance = null;
    }
    /**
     * 设置群组列表
     * @param list
     */
    public void setGroupchatList(List<Groupchat> list){
        if(list != null){
            groupchatList = list;
        }
    }

    /**
     * 获取群组列表
     * @return
     */
    public List<Groupchat> getGroupchatList(){
        return groupchatList;
    }

    /**
     * 获取创建的群组列表
     * @return
     */
    public List<Groupchat> getMyGroupchatList(){
        List<Groupchat> list = new ArrayList<>();
        for(Groupchat tempGroup : groupchatList){
            if(tempGroup.getOwnerid().equals(UserManager.getInstance().getAppUser().getUserid())){
                list.add(tempGroup);
            }
        }
        return list;
    }

    /**
     * 获取我加入的群组列表
     * @return
     */
    public List<Groupchat> getJoininGroupchatList(){
        List<Groupchat> list = new ArrayList<>();
        for(Groupchat tempGroup : groupchatList){
            if(!tempGroup.getOwnerid().equals(UserManager.getInstance().getAppUser().getUserid())){
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
    public Groupchat getGroupByID(Integer groupID){
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
    public void addGroup(Groupchat newGroup){
        groupchatList.add(newGroup);
    }

    /**
     * 移除群聊
     * @param newGroup
     */
    public void removeGroup(Groupchat newGroup){
        groupchatList.remove(newGroup);
    }

}
