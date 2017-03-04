package com.pay.model;

import java.util.Date;




/**
 * AbstractTbWebpPay entity provides the base persistence definition of the TbWebpPay entity. @author
 * MyEclipse Persistence Tools
 */

public abstract class AbstractTbWebpPay implements java.io.Serializable {


    // Fields

    private String objId;
    private String phone;
    private String mid;
    private String aptrid;
    private Date createTime;
    private Date updateTime;
    private String remark;
    private Integer dataStatus;
    private Integer orderStatus;
    private String tcdetailRelId;
    private String quDao;
    private String sin;

    // Constructors

    /** default constructor */
    public AbstractTbWebpPay() {}

    /** minimal constructor */
    public AbstractTbWebpPay(String objId, Integer dataStatus, Integer orderStatus) {
        this.objId = objId;
        this.dataStatus = dataStatus;
        this.orderStatus = orderStatus;
    }

    /** full constructor */
    public AbstractTbWebpPay(String objId, String phone, String mid, String aptrid,
            Date createTime, Date updateTime, String remark, Integer dataStatus,
            Integer orderStatus, String tcdetailRelId,String quDao,String sin) {
        this.objId = objId;
        this.phone = phone;
        this.mid = mid;
        this.aptrid = aptrid;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.remark = remark;
        this.dataStatus = dataStatus;
        this.orderStatus = orderStatus;
        this.tcdetailRelId = tcdetailRelId;
        this.quDao=quDao;
        this.sin=sin;
    }


    // Property accessors

    public String getObjId() {
        return this.objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMid() {
        return this.mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getAptrid() {
        return this.aptrid;
    }

    public void setAptrid(String aptrid) {
        this.aptrid = aptrid;
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

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getDataStatus() {
        return this.dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public Integer getOrderStatus() {
        return this.orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getTcdetailRelId() {
        return this.tcdetailRelId;
    }

    public void setTcdetailRelId(String tcdetailRelId) {
        this.tcdetailRelId = tcdetailRelId;
    }

    public String getQuDao() {
        return quDao;
    }

    public void setQuDao(String quDao) {
        this.quDao = quDao;
    }

    public String getSin() {
        return sin;
    }

    public void setSin(String sin) {
        this.sin = sin;
    }
}
