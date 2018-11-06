package com.liguanghong.gdqylatitude.manager;

import com.liguanghong.gdqylatitude.fragment.AddressbookFragment;
import com.liguanghong.gdqylatitude.fragment.MessageFragment;
import com.liguanghong.gdqylatitude.unity.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 好友管理类
 */
public class FriendsManager {
    //好友列表
    private static Map<String, List<User>> friends = new LinkedHashMap<String, List<User>>();

    /**
     * 设置通讯录
     * @param friends
     */
    public static void setFriends(Map<String, List<User>> friends){
        FriendsManager.friends.putAll(friends);
    }

    /**
     * 设置好友状态
     * @param friendID
     * @param status
     */
    public static void setFriendsStatus(Integer friendID, Integer status){
        for (Map.Entry<String, List<User>> entry : getFriends().entrySet()) {
            for(User user : entry.getValue()){
                if(user.getUserid().equals(friendID)){
                    user.setStatu(status);
                }
            }
        }
        if(AddressbookFragment.getAddressbookHandler() != null)
            AddressbookFragment.getAddressbookHandler().sendEmptyMessage(200);
    }

    /**
     * 获取好友分组名列表
     * @return
     */
    public static List<String> getFriendsSetNameList(){
        List<String> friendsSetNameList = new ArrayList<String>();
        for (Map.Entry<String, List<User>> entry : getFriends().entrySet()) {
           friendsSetNameList.add(entry.getKey());
        }
        return friendsSetNameList;
    }

    /**
     * 获取好友列表
     * @return
     */
    public static Map<String, List<User>> getFriends(){
        return friends;
    }

    /**
     * 获取指定好友分组下好友列表
     * @param setName
     * @return
     */
    public static List<User> getFriendsBySetName(String setName){
        return friends.get(setName);
    }

    /**
     * 获取指定好友信息
     * @param friendID
     * @return
     */
    public static User getFriendByID(Integer friendID){
        User friend = new User();
        friend.setUserid(friendID);
        for (Map.Entry<String, List<User>> entry : getFriends().entrySet()) {
            for(User user : entry.getValue()){
                if(user.getUserid().equals(friendID)){
                    return user;
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
    public static String getFriendsSetNameByID(Integer friendID){
        String friendsSetName = null;
        for (Map.Entry<String, List<User>> entry : getFriends().entrySet()) {
            for(User user : entry.getValue()){
                if(user.getUserid().equals(friendID)){
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
    public static int getFriendsSetIndex(Integer friendID){
        int index = 0;
        for (Map.Entry<String, List<User>> entry : getFriends().entrySet()) {
            for(User user : entry.getValue()){
                if(user.getUserid().equals(friendID)){
                    return index;
                }
            }
            index++;
        }
        return index;
    }

    public static void addFriendsSet(String setName, List<User> setList){
        getFriends().put(setName, setList);
    }
}
