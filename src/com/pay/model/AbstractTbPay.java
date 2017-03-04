package com.pay.model;

import java.sql.Timestamp;
import java.util.Date;


/**
 * AbstractTbPay entity provides the base persistence definition of the TbPay entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractTbPay  implements java.io.Serializable {


    // Fields    

     private String objId;
     private TbTcdetail tbTcdetail;
     private String phone;
     private String openid;
     private String wxno;
     private String wxnick;
     private String prepayid;
     private String outTradeNo;
     private Date createTime;
     private Date updateTime;
     private String remark;
     private Integer dataStatus;
     private Integer orderStatus;


    // Constructors

    /** default constructor */
    public AbstractTbPay() {
    }

	/** minimal constructor */
    public AbstractTbPay(String objId, Integer dataStatus, Integer orderStatus) {
        this.objId = objId;
        this.dataStatus = dataStatus;
        this.orderStatus = orderStatus;
    }
    
    /** full constructor */
    public AbstractTbPay(String objId, TbTcdetail tbTcdetail, String phone, String openid, String wxno, String wxnick, String prepayid, String outTradeNo, Timestamp createTime, Timestamp updateTime, String remark, Integer dataStatus, Integer orderStatus) {
        this.objId = objId;
        this.tbTcdetail = tbTcdetail;
        this.phone = phone;
        this.openid = openid;
        this.wxno = wxno;
        this.wxnick = wxnick;
        this.prepayid = prepayid;
        this.outTradeNo = outTradeNo;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.remark = remark;
        this.dataStatus = dataStatus;
        this.orderStatus = orderStatus;
    }

   
    // Property accessors

    public String getObjId() {
        return this.objId;
    }
    
    public void setObjId(String objId) {
        this.objId = objId;
    }

    public TbTcdetail getTbTcdetail() {
        return this.tbTcdetail;
    }
    
    public void setTbTcdetail(TbTcdetail tbTcdetail) {
        this.tbTcdetail = tbTcdetail;
    }

    public String getPhone() {
        return this.phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpenid() {
        return this.openid;
    }
    
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getWxno() {
        return this.wxno;
    }
    
    public void setWxno(String wxno) {
        this.wxno = wxno;
    }

    public String getWxnick() {
        return this.wxnick;
    }
    
    public void setWxnick(String wxnick) {
        this.wxnick = wxnick;
    }

    public String getPrepayid() {
        return this.prepayid;
    }
    
    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getOutTradeNo() {
        return this.outTradeNo;
    }
    
    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
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
   








}