package com.liguanghong.gdqylatitude.util;

import com.liguanghong.gdqylatitude.unity.User;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 好友管理类
 */
public class FriendsManager {
    //好友列表
    private static Map<String, List<User>> friends = new LinkedHashMap<String, List<User>>();;

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
                if(friendID == user.getUserid()){
                    return user;
                }
            }
        }
        return null;
    }

    public static void addFriendsSet(String setName, List<User> setList){
        getFriends().put(setName, setList);
    }
}
