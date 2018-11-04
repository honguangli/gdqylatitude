package com.liguanghong.gdqylatitude.util;


import com.liguanghong.gdqylatitude.unity.Chatmessage;
import com.liguanghong.gdqylatitude.unity.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理类
 */
public class UserManager {

    //登录用户
    private static User appUser;
    //登录用户socket端
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
    public static SocketClient getSocketClient(){
        return socketClient;
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
