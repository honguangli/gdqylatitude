package com.liguanghong.gdqylatitude.unity;

/**
 * 好友分组
 * @author LGH
 *
 */
public class FriendsSet {
	
	private String name;
	private String friendsid;
	
	public FriendsSet() {
		
	}
	
	public FriendsSet(String name, String friendsid) {
		super();
		this.name = name;
		this.friendsid = friendsid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getFriendsid() {
		return friendsid;
	}

	public void setFriendsid(String friendsid) {
		this.friendsid = friendsid;
	}

}
