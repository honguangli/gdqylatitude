package com.liguanghong.gdqylatitude.manager;


import com.liguanghong.gdqylatitude.unity.User;

/**
 * 用户管理类
 */
public class UserManager {

    private static UserManager instance;
    //登录用户
    public static User appUser;

    //单例模式
    public static UserManager getInstance() {
        if (instance == null) {
            synchronized (UserManager.class) {
                if (instance == null) {
                    instance = new UserManager();
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
    public void addAppUser(User newUser){
        appUser = newUser;
    }

    /**
     * 移除用户
     */
    public void removeAppUser(){
        appUser = null;
    }

}
