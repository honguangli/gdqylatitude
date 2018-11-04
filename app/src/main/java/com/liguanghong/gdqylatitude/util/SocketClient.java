package com.liguanghong.gdqylatitude.util;

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
import com.liguanghong.gdqylatitude.fragment.MessageFragment;
import com.liguanghong.gdqylatitude.unity.Chatmessage;


public class SocketClient implements Runnable{
	
	private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private static boolean runnable;

	public static boolean isRunnable() {
		return runnable;
	}

	public static void setRunnable(boolean runnable) {
		SocketClient.runnable = runnable;
	}

	/**
	 * 向服务器注册一个socket
	 * @throws IOException
	 */
	private void registerSocket() throws IOException{
		try {
			clientSocket = new Socket(HttpUtil.getAddr(), HttpUtil.getSocketPort());
			SocketClient.setRunnable(true);
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
				Log.i("聊天", chatmessage.getSenderid() + "】发给【" + chatmessage.getReceiveruserid() + "】内容：" + chatmessage.getData() );
				MessageFragment.addMessage(chatmessage);
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
                Chatmessage msgJson = new Chatmessage();
				msgJson.setIssingle(isSingle);
				msgJson.setSenderid(UserManager.getAppUser().getUserid());
				msgJson.setType(type);
				msgJson.setData(msg.getBytes(Charset.forName("utf-8")));
				msgJson.setSendtime(new Date());
				msgJson.setReceiveruserid(toId);
				Log.i("聊天", "发送消息："+JSONObject.toJSON(msgJson));
                out.println(JSONObject.toJSON(msgJson));
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
		SocketClient.setRunnable(false);
    }

}
