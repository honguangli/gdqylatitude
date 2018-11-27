package com.liguanghong.gdqylatitude.unity;

public class Msg<T> {
	
	private Integer msgType;
	private T msg;
	
	public Integer getMsgType() {
		return msgType;
	}
	
	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}
	
	public T getMsg() {
		return msg;
	}

	public void setMsg(T msg) {
		this.msg = msg;
	}
	
}
