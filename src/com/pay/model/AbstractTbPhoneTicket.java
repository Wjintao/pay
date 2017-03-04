package com.pay.model;

import java.util.Date;


/**
 * AbstractTbPhoneTicket entity provides the base persistence definition of the TbPhoneTicket
 * entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractTbPhoneTicket implements java.io.Serializable {


    // Fields

    private String objId;
    private Date createTime;
    private Date updateTime;
    private Integer dataStatus;
    private String openid;
    private String phone;
    private Integer ticketgetCount;
    private String ticket1;
    private String ticket2;
    private String ticket3;
    private String ticket4;


    // Constructors

    /** default constructor */
    public AbstractTbPhoneTicket() {}

    /** minimal constructor */
    public AbstractTbPhoneTicket(String objId) {
        this.objId = objId;
    }

    /** full constructor */
    public AbstractTbPhoneTicket(String objId, Date createTime, Date updateTime,
            Integer dataStatus, String openid, String phone, Integer ticketgetCount,
            String ticket1, String ticket2, String ticket3, String ticket4) {
        this.objId = objId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.dataStatus = dataStatus;
        this.openid = openid;
        this.phone = phone;
        this.ticketgetCount = ticketgetCount;
        this.ticket1 = ticket1;
        this.ticket2 = ticket2;
        this.ticket3 = ticket3;
        this.ticket4 = ticket4;
    }


    // Property accessors

    public String getObjId() {
        return this.objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDataStatus() {
        return this.dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getOpenid() {
        return this.openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getTicketgetCount() {
        return this.ticketgetCount;
    }

    public void setTicketgetCount(Integer ticketgetCount) {
        this.ticketgetCount = ticketgetCount;
    }

    public String getTicket1() {
        return this.ticket1;
    }

    public void setTicket1(String ticket1) {
        this.ticket1 = ticket1;
    }

    public String getTicket2() {
        return this.ticket2;
    }

    public void setTicket2(String ticket2) {
        this.ticket2 = ticket2;
    }

    public String getTicket3() {
        return this.ticket3;
    }

    public void setTicket3(String ticket3) {
        this.ticket3 = ticket3;
    }

    public String getTicket4() {
        return this.ticket4;
    }

    public void setTicket4(String ticket4) {
        this.ticket4 = ticket4;
    }



}
