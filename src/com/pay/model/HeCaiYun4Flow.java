package com.pay.model;

import java.io.Serializable;
import java.util.List;

public class HeCaiYun4Flow implements Serializable{

    private String requestID;
    private String channelID;
    private String sig;
    private List<Members> members;
    public String getChannelID() {
        return channelID;
    }
    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }
    public List<Members> getMembers() {
        return members;
    }
    public void setMembers(List<Members> members) {
        this.members = members;
    }
    public String getRequestID() {
        return requestID;
    }
    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }
    public String getSig() {
        return sig;
    }
    public void setSig(String sig) {
        this.sig = sig;
    }
}
