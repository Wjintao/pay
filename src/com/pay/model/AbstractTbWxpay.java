package com.pay.model;

import java.sql.Timestamp;
import java.util.Date;


/**
 * AbstractTbWxpay entity provides the base persistence definition of the TbWxpay entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractTbWxpay  implements java.io.Serializable {


    // Fields    

     private String objId;
     private String phone;
     private String openid;
     private String wxno;
     private String wxnick;
     private String prepayid;
     private String outTradeNo;
     private String productId;
     private String tradeType;
     private Date createTime;
     private Date updateTime;
     private String remark;
     private Integer dataStatus;
     private Integer orderStatus;
     private String tcdetailRelId;


    // Constructors

    /** default constructor */
    public AbstractTbWxpay() {
    }

	/** minimal constructor */
    public AbstractTbWxpay(String objId, Integer dataStatus, Integer orderStatus) {
        this.objId = objId;
        this.dataStatus = dataStatus;
        this.orderStatus = orderStatus;
    }
    
    /** full constructor */
    public AbstractTbWxpay(String objId, String phone, String openid, String wxno, String wxnick, String prepayid, String outTradeNo, String productId, String tradeType, Timestamp createTime, Timestamp updateTime, String remark, Integer dataStatus, Integer orderStatus, String tcdetailRelId) {
        this.objId = objId;
        this.phone = phone;
        this.openid = openid;
        this.wxno = wxno;
        this.wxnick = wxnick;
        this.prepayid = prepayid;
        this.outTradeNo = outTradeNo;
        this.productId = productId;
        this.tradeType = tradeType;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.remark = remark;
        this.dataStatus = dataStatus;
        this.orderStatus = orderStatus;
        this.tcdetailRelId = tcdetailRelId;
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

    public String getProductId() {
        return this.productId;
    }
    
    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTradeType() {
        return this.tradeType;
    }
    
    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
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
   








}