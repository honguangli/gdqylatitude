package com.liguanghong.gdqylatitude.manager;


import android.content.SharedPreferences;
import android.util.Log;

import com.liguanghong.gdqylatitude.unity.User;

/**
 * 用户管理类
 */
public class UserManager {

    private static UserManager instance;
    //登录用户
    private User appUser;
    //存储
    private static SharedPreferences sharedPreferences;

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
        Log.i("用户管理器", "释放资源");
        instance = null;
    }

    public static void setSharedPreferences(SharedPreferences sp){
        sharedPreferences = sp;
    }

    public static SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }

    /**
     * 获取用户
     * @return
     */
    public User getAppUser() {
        if(appUser == null){
            String logname = sharedPreferences.getString("logname", null);
            String password = sharedPreferences.getString("password", null);
            if(null == logname || null == password){
                return null;
            } else {
                User user = new User();
                user.setLogname(logname);
                user.setPassword(password);
                return user;
            }
        }
        return appUser;
    }

    /**
     * 添加用户
     * @param newUser
     */
    public void addAppUser(User newUser){
        appUser = newUser;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("logname", appUser.getLogname());
        editor.putString("password", appUser.getPassword());
        editor.commit();
    }

    /**
     * 移除用户
     */
    public void removeAppUser(){
        appUser = null;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

}
