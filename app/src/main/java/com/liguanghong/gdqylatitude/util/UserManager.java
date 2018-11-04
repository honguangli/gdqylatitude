package com.liguanghong.gdqylatitude.util;

import com.liguanghong.gdqylatitude.unity.User;

public class UserManager {

    private static User appUser;

    private static SocketClient socketClient;

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
     * 获取用户socket
     * @return
     */
    public static SocketClient getSocketClient(){
        return socketClient;
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
     * 添加用户socket
     */
    public static void addSocketClient(){

        if(socketClient == null || !SocketClient.isRunnable()){
            socketClient = new SocketClient();
            Thread socketThread = new Thread(socketClient);
            socketThread.start();
        }

    }

    /**
     * 移除用户socket
     */
    public static void removeSocketClient(){
        socketClient = null;
    }

}
