package com.liguanghong.gdqylatitude.manager;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.liguanghong.gdqylatitude.activitys.HomeActivity;
import com.liguanghong.gdqylatitude.unity.ChatMsg;
import com.liguanghong.gdqylatitude.unity.Friend;
import com.liguanghong.gdqylatitude.unity.Groupchat;
import com.liguanghong.gdqylatitude.unity.MessageType;
import com.liguanghong.gdqylatitude.unity.Msg;
import com.liguanghong.gdqylatitude.unity.NoticeMsg;
import com.liguanghong.gdqylatitude.unity.User;
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
     * @param msg
     */
    public void sendMsg(Msg msg){
        try{
            send(JSONObject.toJSONString(msg));
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
        Log.i("Socket管理器", message);
        JSONObject object = JSONObject.parseObject(message);
        Integer msgType = object.getInteger("msgType");
        if(MessageType.CHATTYPE.equals(msgType)){
            ChatMsg chatMsg = object.getObject("msg", ChatMsg.class);
            ConversationManager.getInstance().receiveMsg(chatMsg);
        } else if(MessageType.NOTICETYPE.equals(msgType)){
            NoticeMsg noticeMsg = object.getObject("msg", NoticeMsg.class);
            if(noticeMsg.getNoticetype().equals(MessageType.LOGOUT)){
                //强制登出
                HomeActivity.getHomeHandler().sendEmptyMessage(222);
            } else if(noticeMsg.getNoticetype().equals(MessageType.FRIENDONLINE)){
                //好友上线通知
                FriendsManager.getInstance().setFriendsStatus(noticeMsg.getSenderid(), 2);
            } else if(noticeMsg.getNoticetype().equals(MessageType.FRIENDOFFLINE)){
                //好友下线通知
                FriendsManager.getInstance().setFriendsStatus(noticeMsg.getSenderid(), 1);
            }
        } else if(MessageType.NOTICEFRIENDTYPE.equals(msgType)){
            NoticeMsg noticeMsg = object.getObject("msg", NoticeMsg.class);
            if(noticeMsg.getNoticetype().equals(MessageType.FRIENDAPPLY)){
                //好友申请
                NoticesManager.getInstance().addFriendNotice(noticeMsg);
            } else if(noticeMsg.getNoticetype().equals(MessageType.FRIENDDENIED)){
                //好友申请被拒绝
            } else if(noticeMsg.getNoticetype().equals(MessageType.FRIENDAGREED)){
                //好友申请被接受
                Friend friend = new Friend();
                User user = (User)noticeMsg.getData();
                friend.setFriendid(user.getUserid());
                friend.setFriend(user);
                FriendsManager.getInstance().addFriend(friend);
            }
        } else if(MessageType.NOTICEGROUPTYPE.equals(msgType)){
            NoticeMsg noticeMsg = object.getObject("msg", NoticeMsg.class);
            if(noticeMsg.getNoticetype().equals(MessageType.GROUPAPPLY)){
                //群组申请
            } else if(noticeMsg.getNoticetype().equals(MessageType.GROUPDENIED)){
                //群组申请被拒绝
            } else if(noticeMsg.getNoticetype().equals(MessageType.GROUPAGREED)){
                //群组申请被接受
                Groupchat groupchat = (Groupchat)noticeMsg.getData();
                GroupManager.getInstance().addGroup(groupchat);
            } else if(noticeMsg.getNoticetype().equals(MessageType.GROUPINVITE)){
                //群组邀请
                NoticesManager.getInstance().addGroupNotice(noticeMsg);
            } else if(noticeMsg.getNoticetype().equals(MessageType.GROUPINVITEDENIED)){
                //群组邀请被拒绝
            } else if(noticeMsg.getNoticetype().equals(MessageType.GROUPINVITEAGREED)){
                //群组邀请被接受

            }
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
