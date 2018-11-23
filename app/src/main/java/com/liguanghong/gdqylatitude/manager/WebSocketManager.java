package com.liguanghong.gdqylatitude.manager;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.liguanghong.gdqylatitude.activitys.HomeActivity;
import com.liguanghong.gdqylatitude.unity.ChatMsg;
import com.liguanghong.gdqylatitude.unity.MessageType;
import com.liguanghong.gdqylatitude.util.HttpUtil;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketManager extends WebSocketClient{

    private static WebSocketManager instance;

    public WebSocketManager(URI serverUri) {
        super(serverUri);
        connect();
    }

    //单例模式
    public static WebSocketManager getInstance() {
        if (instance == null) {
            synchronized (WebSocketManager.class) {
                if (instance == null) {
                    try {
                        instance = new WebSocketManager(new URI(HttpUtil.getWebSocketAddress() + UserManager.getInstance().getAppUser().getUserid()));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return instance;
    }
    //释放资源
    public static void releaseResource(){
        Log.i("Socket管理器", "释放资源");
        instance.close();
        instance = null;
    }

    /**
     * 发送消息
     * @param chatMsg
     */
    public void sendMsg(ChatMsg chatMsg){
        try{
            send(JSONObject.toJSONString(chatMsg));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.i("Socket管理器", "open");
    }

    @Override
    public void onMessage(String message) {
        ChatMsg chatMsg = JSONObject.parseObject(message, ChatMsg.class);
        Log.i("Socket管理器", message);
        if(chatMsg.getType().equals(MessageType.LOGOUT)){
            //强制登出
            HomeActivity.getHomeHandler().sendEmptyMessage(222);
        } else if(chatMsg.getType().equals(MessageType.FRIENDONLINE)){
            //好友上线通知
            FriendsManager.getInstance().setFriendsStatus(chatMsg.getSenderid(), 2);
        } else if(chatMsg.getType().equals(MessageType.FRIENDOFFLINE)){
            //好友下线通知
            FriendsManager.getInstance().setFriendsStatus(chatMsg.getSenderid(), 1);
        } else if(chatMsg.getType() > 0 && chatMsg.getType() < 5){
            //聊天消息
            ConversationManager.getInstance().receiveMsg(chatMsg);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.i("Socket管理器", "close:code-" + code +"|reason-" + reason);
    }

    @Override
    public void onError(Exception ex) {
        Log.i("Socket管理器", "error");
        ex.printStackTrace();
    }

}
