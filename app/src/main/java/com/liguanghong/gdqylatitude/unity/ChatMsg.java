package com.liguanghong.gdqylatitude.unity;

import java.util.Date;

/**
 * 消息类
 */
public class ChatMsg {
    private Integer msgid;

    private Integer senderid;

    private Integer receiverid;

    private Boolean issingle;

    private Integer type;

    private Date sendtime;

    private Integer status;

    private byte[] data;

    public Integer getMsgid() {
        return msgid;
    }

    public void setMsgid(Integer msgid) {
        this.msgid = msgid;
    }

    public Integer getSenderid() {
        return senderid;
    }

    public void setSenderid(Integer senderid) {
        this.senderid = senderid;
    }

    public Integer getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(Integer receiverid) {
        this.receiverid = receiverid;
    }

    public Boolean getIssingle() {
        return issingle;
    }

    public void setIssingle(Boolean issingle) {
        this.issingle = issingle;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getSendtime() {
        return sendtime;
    }

    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}