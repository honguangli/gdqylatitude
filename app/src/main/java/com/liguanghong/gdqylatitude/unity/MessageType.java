package com.liguanghong.gdqylatitude.unity;

/**
 * 消息类型
 * @author LGH
 *
 */
public class MessageType {
	//聊天类消息
	public static final Integer CHATTYPE = 0;
	public static final Integer TEXT = 1;			//文本消息
	public static final Integer IMAGE = 2;			//图片消息
	public static final Integer LOCATION = 3;		//定位消息
	public static final Integer VOICE = 4;			//语音消息

	//通知类消息 -- 系统通知类
	public static final Integer NOTICETYPE = 10;
	public static final Integer LOGOUT = 11;		//强制登出
	public static final Integer FRIENDONLINE = 12;	//好友上线
	public static final Integer FRIENDOFFLINE = 13;	//好友下线
	//通知类消息 -- 好友通知类
	public static final Integer NOTICEFRIENDTYPE = 20;
	public static final Integer FRIENDAPPLY = 21;	//好友申请
	public static final Integer FRIENDDENIED = 22;	//好友申请被拒绝
	public static final Integer FRIENDAGREED = 23;	//好友申请被同意
	public static final Integer FRIENDDELETE = 24; //删除好友
	//通知类消息 -- 群聊通知类
	public static final Integer NOTICEGROUPTYPE = 30;
	public static final Integer GROUPAPPLY = 31;			//群聊加入申请
	public static final Integer GROUPDENIED = 32;		//群聊加入申请被拒绝
	public static final Integer GROUPAGREED = 33;		//群聊加入申请被同意
	public static final Integer GROUPINVITE = 34;		//群聊邀请
	public static final Integer GROUPINVITEDENIED = 35;	//群聊邀请被拒绝
	public static final Integer GROUPINVITEAGREED = 36;	//群聊邀请被同意

}

