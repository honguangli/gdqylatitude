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

public class WebSocketManager {

    private static WebSocketClient client;

    /**
     * 连接websocket
     * @param userid
     */
    public static void connect(Integer userid){
        try{
            client = new WebSocketClient(new URI(HttpUtil.getWebSocketAddress() + userid)) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.i("websocket", "open");
                }

                @Override
                public void onMessage(String message) {
                    ChatMsg chatMsg = JSONObject.parseObject(message, ChatMsg.class);
                    Log.i("消息", message);
                    if(chatMsg.getType().equals(MessageType.LOGOUT)){
                        //强制登出
                        HomeActivity.getHomeHandler().sendEmptyMessage(222);
                    } else if(chatMsg.getType().equals(MessageType.FRIENDONLINE)){
                        //好友上线通知
                        FriendsManager.setFriendsStatus(chatMsg.getSenderid(), 2);
                    } else if(chatMsg.getType().equals(MessageType.FRIENDOFFLINE)){
                        //好友下线通知
                        FriendsManager.setFriendsStatus(chatMsg.getSenderid(), 1);
                    } else if(chatMsg.getType() > 0 && chatMsg.getType() < 5){
                        //聊天消息
                        ConversationManager.receiveMsg(chatMsg);
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.i("websocket", "close:code-" + code +"|reason-" + reason);
                    client = null;
                }

                @Override
                public void onError(Exception ex) {
                    Log.i("websocket", "error");
                    client = null;
                    ex.printStackTrace();
                }
            };
            client.connect();
        } catch (URISyntaxException e){
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     * @param chatMsg
     */
    public static void sendMsg(ChatMsg chatMsg){
        client.send(JSONObject.toJSONString(chatMsg));
    }

    /**
     * 关闭websocket
     */
    public static void close(){
        if(client != null && !client.isClosed()){
            client.close();
        }
    }

}
