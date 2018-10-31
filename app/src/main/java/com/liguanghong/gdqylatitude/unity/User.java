package com.liguanghong.gdqylatitude.unity;

import java.io.Serializable;

public class User implements Serializable {
    private Integer userid;

    private String logname;

    private String password;

    private String username;

    private String sex;

    private Integer statu;

    private String email;

    private String phone;

    private String signature;

    private String province;

    private String city;

    private String latitude;

    private String longitude;

    private String friendsid;

    private String creategroupid;

    private String joingroupid;

    private byte[] headportrait;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getLogname() {
        return logname;
    }

    public void setLogname(String logname) {
        this.logname = logname == null ? null : logname.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public Integer getStatu() {
        return statu;
    }

    public void setStatu(Integer statu) {
        this.statu = statu;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature == null ? null : signature.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    public String getFriendsid() {
        return friendsid;
    }

    public void setFriendsid(String friendsid) {
        this.friendsid = friendsid == null ? null : friendsid.trim();
    }

    public String getCreategroupid() {
        return creategroupid;
    }

    public void setCreategroupid(String creategroupid) {
        this.creategroupid = creategroupid == null ? null : creategroupid.trim();
    }

    public String getJoingroupid() {
        return joingroupid;
    }

    public void setJoingroupid(String joingroupid) {
        this.joingroupid = joingroupid == null ? null : joingroupid.trim();
    }

    public byte[] getHeadportrait() {
        return headportrait;
    }

    public void setHeadportrait(byte[] headportrait) {
        this.headportrait = headportrait;
    }
}