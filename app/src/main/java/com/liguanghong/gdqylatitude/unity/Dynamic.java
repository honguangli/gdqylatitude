package com.liguanghong.gdqylatitude.unity;

import java.util.Date;

public class Dynamic {
    private Integer dynamicid;

    private Integer userid;

    private String text;

    private Double latitude;

    private Double longitude;

    private String likeid;

    private Integer likenumber;

    private String transmitid;

    private Integer transmitnumber;

    private String commentid;

    private byte[] pic;

    private byte[] pic2;

    private byte[] pic3;

    private Date postedtime;

    public Date getPostedtime() {
        return postedtime;
    }

    public void setPostedtime(Date postedtime) {
        this.postedtime = postedtime;
    }

    public Integer getDynamicid() {
        return dynamicid;
    }

    public void setDynamicid(Integer dynamicid) {
        this.dynamicid = dynamicid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text == null ? null : text.trim();
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLikeid() {
        return likeid;
    }

    public void setLikeid(String likeid) {
        this.likeid = likeid == null ? null : likeid.trim();
    }

    public Integer getLikenumber() {
        return likenumber;
    }

    public void setLikenumber(Integer likenumber) {
        this.likenumber = likenumber;
    }

    public String getTransmitid() {
        return transmitid;
    }

    public void setTransmitid(String transmitid) {
        this.transmitid = transmitid == null ? null : transmitid.trim();
    }

    public Integer getTransmitnumber() {
        return transmitnumber;
    }

    public void setTransmitnumber(Integer transmitnumber) {
        this.transmitnumber = transmitnumber;
    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid == null ? null : commentid.trim();
    }

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    public byte[] getPic2() {
        return pic2;
    }

    public void setPic2(byte[] pic2) {
        this.pic2 = pic2;
    }

    public byte[] getPic3() {
        return pic3;
    }

    public void setPic3(byte[] pic3) {
        this.pic3 = pic3;
    }

}