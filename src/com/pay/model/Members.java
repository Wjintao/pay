package com.pay.model;

import java.io.Serializable;

public class Members implements Serializable {

    private String userName;
    private String effType;
    private String effTime;
    private String prdCode;
    
    private String mobile;
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getEffType() {
        return effType;
    }
    public void setEffType(String effType) {
        this.effType = effType;
    }
    public String getEffTime() {
        return effTime;
    }
    public void setEffTime(String effTime) {
        this.effTime = effTime;
    }
    public String getPrdCode() {
        return prdCode;
    }
    public void setPrdCode(String prdCode) {
        this.prdCode = prdCode;
    }

}
