package com.liguanghong.gdqylatitude.manager;

import android.util.Log;

import com.liguanghong.gdqylatitude.activitys.GroupActivity;
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
        Log.i("群组管理器", "释放资源");
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
        Log.i("群组管理器", "添加群聊：群ID=" + newGroup.getGroupid() + "，群名=" + newGroup.getGroupname());
        groupchatList.add(newGroup);
    }

    /**
     * 移除群聊
     * @param newGroup
     */
    public void removeGroup(Groupchat newGroup){
        Log.i("群组管理器", "删除群聊：群ID=" + newGroup.getGroupid() + "，群名=" + newGroup.getGroupname());
        groupchatList.remove(newGroup);
    }

    public void updateGroup(Groupchat groupchat){
        for(Groupchat group : groupchatList){
            if(group.getGroupid().equals(groupchat.getGroupid())){
                groupchatList.remove(group);
                break;
            }
        }
        groupchatList.add(groupchat);
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged(){
        if(GroupActivity.groupHandler != null)
            GroupActivity.groupHandler.sendEmptyMessage(200);
    }

}
