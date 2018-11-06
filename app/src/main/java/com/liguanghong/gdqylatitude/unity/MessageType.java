package com.liguanghong.gdqylatitude.unity;

/**
 * 消息类型
 * @author LGH
 *
 */
public class MessageType {
	//聊天类消息
	public static final Integer TEXT = 0;			//文本消息
	public static final Integer IMAGE = 1;			//图片消息
	public static final Integer LOCATION = 2;		//定位消息
	public static final Integer VOICE = 3;			//语音消息

	//通知类消息
	public static final Integer LOGOUT = 11;		//强制登出
	//通知类消息 -- 好友通知类
	public static final Integer FRIENDAPPLY = 15;	//好友申请
	public static final Integer FRIENDDENIED = 16;	//好友申请被拒绝
	public static final Integer FRIENDAGREED = 17;	//好友申请被同意
	//通知类消息 -- 群聊通知类
	public static final Integer GROUPINVITE = 15;	//群聊邀请
	public static final Integer GROUPDENIED = 16;	//群聊邀请被拒绝
	public static final Integer GROUPAGREED = 17;	//群聊邀请被同意

}
