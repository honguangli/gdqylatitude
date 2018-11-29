package com.liguanghong.gdqylatitude.manager;

import android.util.Log;

import com.liguanghong.gdqylatitude.activitys.FriendsSetManageActivity;
import com.liguanghong.gdqylatitude.fragment.AddressbookFragment;
import com.liguanghong.gdqylatitude.unity.Friend;
import com.liguanghong.gdqylatitude.unity.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 好友管理类
 */
public class FriendsManager {

    private static FriendsManager instance = null;
    //好友列表
    public Map<String, List<Friend>> friends = new LinkedHashMap<>();
    private static final String DEFAULTSETNAME = "我的好友";

    //单例模式
    public static FriendsManager getInstance() {
        if (instance == null) {
            synchronized (FriendsManager.class) {
                if (instance == null) {
                    instance = new FriendsManager();
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
     * 设置通讯录
     * @param newfriends
     */
    public void setFriends(Map<String, List<Friend>> newfriends){
        List<Friend> defaultFriendsSet = newfriends.get(FriendsManager.DEFAULTSETNAME);
        friends.put(FriendsManager.DEFAULTSETNAME, defaultFriendsSet);
        newfriends.remove(defaultFriendsSet);
        friends.putAll(newfriends);
        notifyDataSetChanged();
    }

    /**
     * 获取好友分组名列表
     * @return
     */
    public List<String> getFriendsSetNameList(){
        List<String> friendsSetNameList = new ArrayList<>();
        for (Map.Entry<String, List<Friend>> entry : getFriends().entrySet()) {
           friendsSetNameList.add(entry.getKey());
        }
        return friendsSetNameList;
    }

    /**
     * 获取好友列表
     * @return
     */
    public Map<String, List<Friend>> getFriends(){
        return friends;
    }

    /**
     * 获取指定好友分组下好友列表
     * @param setName
     * @return
     */
    public List<Friend> getFriendsBySetName(String setName){
        return friends.get(setName);
    }

    /**
     * 获取指定好友分组下在线好友数
     * @param setName
     * @return
     */
    public int getOnlineCountBySetName(String setName){
        List<Friend> list = getFriendsBySetName(setName);
        int count = 0;
        for(Friend friend : list){
            if(friend.getFriend().getStatu() != null && friend.getFriend().getStatu().equals(2)){
                count++;
            }
        }
        return count;
    }

    /**
     * 获取指定好友信息
     * @param friendID
     * @return
     */
    public Friend getFriendByID(Integer friendID){
        for (Map.Entry<String, List<Friend>> entry : getFriends().entrySet()) {
            for(Friend friend : entry.getValue()){
                if(friend.getFriendid().equals(friendID)){
                    return friend;
                }
            }
        }
        return null;
    }

    /**
     * 获取指定好友所在分组名
     * @param friendID
     * @return
     */
    public String getFriendsSetNameByID(Integer friendID){
        String friendsSetName = null;
        for (Map.Entry<String, List<Friend>> entry : getFriends().entrySet()) {
            for(Friend friend  : entry.getValue()){
                if(friend.getFriendid().equals(friendID)){
                    friendsSetName = entry.getKey();
                    return friendsSetName;
                }
            }
        }
        return friendsSetName;
    }

    /**
     * 获取指定好友所在分组下标。ps：下标从0开始递增
     * @param friendID
     * @return
     */
    public int getFriendsSetIndexByID(Integer friendID){
        int index = 0;
        for (Map.Entry<String, List<Friend>> entry : getFriends().entrySet()) {
            for(Friend friend : entry.getValue()){
                if(friend.getFriendid().equals(friendID)){
                    return index;
                }
            }
            index++;
        }
        return index;
    }

    /**
     * 添加好友分组
     * @param setName
     * @param setList
     */
    public void addFriendsSet(String setName, List<Friend> setList){
        Log.i("好友管理器", "添加好友分组：分组名="+ setName);
        getFriends().put(setName, setList);
        notifyDataSetChanged();
    }

    /**
     * 删除好友分组
     * @param setName
     */
    public void deleteFriendsSet(String setName){
        Log.i("好友管理器", "删除好友分组：分组名="+ setName);
        List<Friend> list = getFriendsBySetName(setName);
        if(list != null && list.size() > 0){
            for(Friend friend : list){
                getFriendsBySetName(FriendsManager.DEFAULTSETNAME).add(friend);
            }
        }
        getFriends().remove(setName);
        notifyDataSetChanged();
    }

    /**
     * 移动好友所在分组
     * @param friendID
     * @param fromSetName
     * @param toSetName
     */
    public void changeFriendsSet(Integer friendID, String fromSetName, String toSetName){
        Log.i("好友管理器", "移动好友分组：好友ID="+ friendID + "，原分组名=" + fromSetName + "，目标分组名=" + toSetName);
        Friend friend = getFriendByID(friendID);
        getFriends().get(fromSetName).remove(friend);
        getFriends().get(toSetName).add(friend);
        notifyDataSetChanged();
    }

    /**
     * 添加好友
     * @param friend
     */
    public void addFriend(Friend friend){
        Log.i("好友管理器", "添加好友：好友ID="+ friend.getFriendid());
        getFriends().get(FriendsManager.DEFAULTSETNAME).add(friend);
        notifyDataSetChanged();
    }

    /**
     * 向指定分组添加好友
     * @param friend
     * @param setName
     */
    public void addFriend(Friend friend, String setName){
        Log.i("好友管理器", "添加好友：好友ID="+ friend.getFriendid() + ", 分组名="  + setName);
        getFriends().get(setName).add(friend);
        notifyDataSetChanged();
    }

    /**
     * 删除好友
     * @param friendID
     */
    public void deleteFriend(Integer friendID){
        Log.i("好友管理器", "删除好友：好友ID="+ friendID);
        getFriends().get(getFriendsSetNameByID(friendID)).remove(getFriendByID(friendID));
        notifyDataSetChanged();
    }

    /**
     * 设置好友状态
     * @param friendID
     * @param status
     */
    public void setFriendsStatus(Integer friendID, Integer status){
        Log.i("好友管理器", "好友状态改变：好友ID="+ friendID + ", 状态=" + (status == 2 ? "上线" : "下线"));
        for (Map.Entry<String, List<Friend>> entry : getFriends().entrySet()) {
            for(Friend friend : entry.getValue()){
                if(friend.getFriendid().equals(friendID)){
                    friend.getFriend().setStatu(status);
                }
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 设置好友备注
     * @param friendID
     * @param friendRemark
     */
    public void setFriendRemark(Integer friendID, String friendRemark){
        Log.i("好友管理器", "设置好友备注：好友ID="+ friendID + "，备注名=" + friendRemark);
        for (Map.Entry<String, List<Friend>> entry : getFriends().entrySet()) {
            for(Friend friend : entry.getValue()){
                if(friend.getFriendid().equals(friendID)){
                    friend.setRemark(friendRemark);
                }
            }
        }
        notifyDataSetChanged();
    }

    /**
     * 提醒UI做改变
     */
    private void notifyDataSetChanged(){
        if(AddressbookFragment.addressbookHandler != null)
            AddressbookFragment.addressbookHandler.sendEmptyMessage(200);
        if(FriendsSetManageActivity.getFriendsSetManageHandler() != null)
            FriendsSetManageActivity.getFriendsSetManageHandler().sendEmptyMessage(200);
    }

}
