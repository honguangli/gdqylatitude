package com.liguanghong.gdqylatitude.unity;

import java.io.Serializable;

public class Friend implements Serializable {

	private Integer friendid;		//好友ID
	private User friend;			//好友信息
	private String remark;			//好友备注名

	public Integer getFriendid() {
		return friendid;
	}

	public void setFriendid(Integer friendid) {
		this.friendid = friendid;
	}

	public User getFriend() {
		return friend;
	}

	public void setFriend(User friend) {
		this.friend = friend;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
