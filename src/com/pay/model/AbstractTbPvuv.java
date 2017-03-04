package com.pay.model;

import java.util.Date;




/**
 * AbstractTbPvuv entity provides the base persistence definition of the TbPvuv entity. @author
 * MyEclipse Persistence Tools
 */

public abstract class AbstractTbPvuv implements java.io.Serializable {


    // Fields

    private String objId;
    private Date createTime;
    private Date updateTime;
    private Integer dataStatus;
    private String realIp;
    private String phone;
    private String form;
    private String channel;
    private String remark;


    // Constructors

    /** default constructor */
    public AbstractTbPvuv() {}

    /** minimal constructor */
    public AbstractTbPvuv(String objId) {
        this.objId = objId;
    }

    /** full constructor */
    public AbstractTbPvuv(String objId, Date createTime, Date updateTime,
            Integer dataStatus, String realIp, String phone, String form, String channel,
            String remark) {
        this.objId = objId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.dataStatus = dataStatus;
        this.realIp = realIp;
        this.phone = phone;
        this.form = form;
        this.channel = channel;
        this.remark = remark;
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

    public String getRealIp() {
        return this.realIp;
    }

    public void setRealIp(String realIp) {
        this.realIp = realIp;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getForm() {
        return this.form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getChannel() {
        return this.channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }



}
