package com.liguanghong.gdqylatitude.unity;

import java.io.Serializable;

public class Groupchat implements Serializable {
    private Integer groupid;

    private Integer ownerid;

    private String groupname;

    private Integer status;

    private String announcement;

    private String introduction;

    private String groupmember;

    private Integer groupnum;

    private byte[] headportrait;

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public Integer getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(Integer ownerid) {
        this.ownerid = ownerid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname == null ? null : groupname.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement == null ? null : announcement.trim();
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction == null ? null : introduction.trim();
    }

    public String getGroupmember() {
        return groupmember;
    }

    public void setGroupmember(String groupmember) {
        this.groupmember = groupmember == null ? null : groupmember.trim();
    }

    public Integer getGroupnum() {
        return groupnum;
    }

    public void setGroupnum(Integer groupnum) {
        this.groupnum = groupnum;
    }

    public byte[] getHeadportrait() {
        return headportrait;
    }

    public void setHeadportrait(byte[] headportrait) {
        this.headportrait = headportrait;
    }
}