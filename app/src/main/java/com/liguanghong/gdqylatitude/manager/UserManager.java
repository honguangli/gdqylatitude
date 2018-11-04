package com.liguanghong.gdqylatitude.manager;


import com.liguanghong.gdqylatitude.unity.User;

/**
 * 用户管理类
 */
public class UserManager {

    //登录用户
    private static User appUser;
    //登录用户socket端
    private static SocketClientManager socketClientManager;

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

    /**
     * 获取用户socket
     * @return
     */
    public static SocketClientManager getSocketClientManager(){
        return socketClientManager;
    }

    /**
     * 添加用户socket
     */
    public static void addSocketClient(){

        if(socketClientManager == null || !SocketClientManager.isRunnable()){
            socketClientManager = new SocketClientManager();
            Thread socketThread = new Thread(socketClientManager);
            socketThread.start();
        }

    }

    /**
     * 移除用户socket
     */
    public static void removeSocketClient(){
        socketClientManager = null;
    }

}
