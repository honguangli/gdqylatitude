package com.liguanghong.gdqylatitude.unity;

/**
 * 消息类型
 * @author LGH
 *
 */
public class MessageType {
	//聊天类消息
	public static final Integer TEXT = 1;				//文本消息
	public static final Integer IMAGE = 2;			//图片消息
	public static final Integer LOCATION = 3;		//定位消息
	public static final Integer VOICE = 4;			//语音消息

	//通知类消息
	public static final Integer LOGOUT = 11;			//强制登出
	//通知类消息 -- 好友通知类
	public static final Integer FRIENDONLINE = 13;	//好友上线
	public static final Integer FRIENDOFFLINE = 14;	//好友下线
	public static final Integer FRIENDAPPLY = 15;	//好友申请
	public static final Integer FRIENDDENIED = 16;	//好友申请被拒绝
	public static final Integer FRIENDAGREED = 17;	//好友申请被同意
	//通知类消息 -- 群聊通知类
	public static final Integer GROUPINVITE = 18;	//群聊邀请
	public static final Integer GROUPDENIED = 19;	//群聊邀请被拒绝
	public static final Integer GROUPAGREED = 20;	//群聊邀请被同意

}
