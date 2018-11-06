package com.liguanghong.gdqylatitude.manager;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.liguanghong.gdqylatitude.activitys.HomeActivity;
import com.liguanghong.gdqylatitude.fragment.MessageFragment;
import com.liguanghong.gdqylatitude.unity.Chatmessage;
import com.liguanghong.gdqylatitude.unity.MessageType;
import com.liguanghong.gdqylatitude.util.HttpUtil;


public class SocketClientManager implements Runnable{
	
	private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private static boolean runnable;

	public static boolean isRunnable() {
		return runnable;
	}

	public static void setRunnable(boolean runnable) {
		SocketClientManager.runnable = runnable;
	}

	/**
	 * 向服务器注册一个socket
	 * @throws IOException
	 */
	private void registerSocket() throws IOException{
		try {
			clientSocket = new Socket(HttpUtil.getAddr(), HttpUtil.getSocketPort());
			SocketClientManager.setRunnable(true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()),true);

			Chatmessage registerJson = new Chatmessage();
			registerJson.setSenderid(UserManager.getAppUser().getUserid());
			out.println(JSONObject.toJSON(registerJson));
			out.flush();
		} catch (IOException e){
			Log.i("聊天", "socket注册失败");
			throw e;
		}
	}

	/**
	 * 消息接收器
	 * @throws IOException
	 */
	private void receiveMsg() throws IOException{
		try{
			String msg = in.readLine();
			while(runnable) {
				Chatmessage chatmessage = JSONObject.parseObject(msg, Chatmessage.class);
				if(chatmessage.getType().equals(MessageType.LOGOUT)){
					//强制登出
					HomeActivity.getHomeHandler().sendEmptyMessage(222);
				} else if(chatmessage.getType().equals(MessageType.FRIENDONLINE)){
					//好友上线通知
					FriendsManager.setFriendsStatus(chatmessage.getSenderid(), 2);
				} else if(chatmessage.getType().equals(MessageType.FRIENDOFFLINE)){
					//好友下线通知
					FriendsManager.setFriendsStatus(chatmessage.getSenderid(), 1);
				} else if(chatmessage.getType() > 0 && chatmessage.getType() < 5){
					//聊天消息
					Log.i("聊天", chatmessage.getSenderid() + "】发给【" + chatmessage.getReceiveruserid() + "】内容：" + chatmessage.getData() );
					ConversationManager.addChatmessage(chatmessage, chatmessage.getIssingle(), false);
				} else if(chatmessage.getType() > 14 && chatmessage.getType() < 18){
					//好友申请通知
					NoticeManager.addFriendsNotice(chatmessage);
				} else if(chatmessage.getType() > 17 && chatmessage.getType() < 21){
					//群聊通知
					NoticeManager.addGroupNotice(chatmessage);
				}
				msg = in.readLine();
			}
		} catch (IOException e){
			Log.i("聊天", "socket接收器异常");
			throw e;
		}
	}

	/**
	 * 发送消息
	 * @param isSingle
	 * @param type
	 * @param msg
	 * @param toId
	 */
	public void sendMsg(final boolean isSingle, final Integer type, final String msg, final Integer toId){
	    new Thread(){
	        public void run(){
                Chatmessage chatmessage = new Chatmessage();
				chatmessage.setIssingle(isSingle);
				chatmessage.setSenderid(UserManager.getAppUser().getUserid());
				chatmessage.setType(type);
				chatmessage.setData(msg.getBytes(Charset.forName("utf-8")));
				chatmessage.setSendtime(new Date());
				chatmessage.setReceiveruserid(toId);
				Log.i("聊天", "发送消息："+JSONObject.toJSON(chatmessage));
				ConversationManager.addChatmessage(chatmessage, chatmessage.getIssingle(), true);
                out.println(JSONObject.toJSON(chatmessage));
                out.flush();
            }
        }.start();
	}


    @Override
    public void run() {
        try {
			registerSocket();
			receiveMsg();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
        	e.printStackTrace();
		}

        try {
        	in.close();
        	out.close();
		} catch (IOException e){
        	e.printStackTrace();
		} catch (Exception e){
        	e.printStackTrace();
		}

		//socket正常退出或者异常退出后，将socketclient状态设置为false，便于重启socket服务
		SocketClientManager.setRunnable(false);
    }

}
