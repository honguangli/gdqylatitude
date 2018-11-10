package com.liguanghong.gdqylatitude.manager;


import com.liguanghong.gdqylatitude.unity.User;

/**
 * 用户管理类
 */
public class UserManager {

    //登录用户
    private static User appUser;

    /**
     * 获取用户
     * @return
     */
    public static User getAppUser() {
        if(appUser == null){
            //SQLite数据库操作
            appUser = new User();
        }
        return appUser;
    }

    /**
     * 添加用户
     * @param newUser
     */
    public static void addAppUser(User newUser){
        appUser = newUser;
    }

    /**
     * 移除用户
     */
    public static void removeAppUser(){
        appUser = null;
    }

}
