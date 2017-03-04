package com.pay.model;


import java.util.Date;


/**
 * AbstractTbPhoneFlow entity provides the base persistence definition of the TbPhoneFlow entity. @author
 * MyEclipse Persistence Tools
 */

public abstract class AbstractTbPhoneFlow implements java.io.Serializable {


    // Fields

    private String objId;
    private Date createTime;
    private Date updateTime;
    private Integer dataStatus;
    private String openid;
    private String phone;
    private String flow;
    private String orderid;
    private String remark;


    // Constructors

    /** default constructor */
    public AbstractTbPhoneFlow() {}

    /** minimal constructor */
    public AbstractTbPhoneFlow(String objId) {
        this.objId = objId;
    }

    /** full constructor */
    public AbstractTbPhoneFlow(String objId, Date createTime, Date updateTime,
            Integer dataStatus, String openid, String phone, String flow, String orderid,
            String remark) {
        this.objId = objId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.dataStatus = dataStatus;
        this.openid = openid;
        this.phone = phone;
        this.flow = flow;
        this.orderid = orderid;
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

    public String getFlow() {
        return this.flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public String getOrderid() {
        return this.orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }



}
