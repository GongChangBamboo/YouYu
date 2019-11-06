package com.fish.yuyou.bean;

public class LocationBean {
    //用户ID
    private String userId;
    //姓名
    private String userName;
    //纬度
    private double latitude;
    //经度
    private double longtitude;
    //记录时间
    private long recodeTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getRecodeTime() {
        return recodeTime;
    }

    public void setRecodeTime(long recodeTime) {
        this.recodeTime = recodeTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }
}
