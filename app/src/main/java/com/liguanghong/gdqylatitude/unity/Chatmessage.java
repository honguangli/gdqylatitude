package com.liguanghong.gdqylatitude.unity;

import java.util.Date;

public class Chatmessage {
    private Integer messageid;

    private Boolean issingle;

    private Integer senderid;

    private Integer type;

    private Date sendtime;

    private Integer receiveruserid;

    private Integer receivergroupid;

    private Date receivertime;

    private byte[] data;

    public Integer getMessageid() {
        return messageid;
    }

    public void setMessageid(Integer messageid) {
        this.messageid = messageid;
    }

    public Boolean getIssingle() {
        return issingle;
    }

    public void setIssingle(Boolean issingle) {
        this.issingle = issingle;
    }

    public Integer getSenderid() {
        return senderid;
    }

    public void setSenderid(Integer senderid) {
        this.senderid = senderid;
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

    public Integer getReceiveruserid() {
        return receiveruserid;
    }

    public void setReceiveruserid(Integer receiveruserid) {
        this.receiveruserid = receiveruserid;
    }

    public Integer getReceivergroupid() {
        return receivergroupid;
    }

    public void setReceivergroupid(Integer receivergroupid) {
        this.receivergroupid = receivergroupid;
    }

    public Date getReceivertime() {
        return receivertime;
    }

    public void setReceivertime(Date receivertime) {
        this.receivertime = receivertime;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}