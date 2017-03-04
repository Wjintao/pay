package com.pay.model;

import java.sql.Timestamp;
import java.util.Date;


/**
 * AbstractTbOperate entity provides the base persistence definition of the TbOperate entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractTbOperate  implements java.io.Serializable {


    // Fields    

     private String objId;
     private String oprTitle;
     private String picUrl;
     private String detailUrl;
     private Integer sort;
     private String oprType;
     private Integer oprStatus;
     private String clientType;
     private Date startTime;
     private Date endTime;
     private Date createTime;
     private Date updateTime;
     private String remark;
     private Integer dataStatus;
     private String tcdetailRelId;


    // Constructors

    /** default constructor */
    public AbstractTbOperate() {
    }

	/** minimal constructor */
    public AbstractTbOperate(String objId, String oprType, Integer oprStatus, String clientType, Integer dataStatus) {
        this.objId = objId;
        this.oprType = oprType;
        this.oprStatus = oprStatus;
        this.clientType = clientType;
        this.dataStatus = dataStatus;
    }
    
    /** full constructor */
    public AbstractTbOperate(String objId, String oprTitle, String picUrl, String detailUrl, Integer sort, String oprType, Integer oprStatus, String clientType, Timestamp startTime, Timestamp endTime, Timestamp createTime, Timestamp updateTime, String remark, Integer dataStatus, String tcdetailRelId) {
        this.objId = objId;
        this.oprTitle = oprTitle;
        this.picUrl = picUrl;
        this.detailUrl = detailUrl;
        this.sort = sort;
        this.oprType = oprType;
        this.oprStatus = oprStatus;
        this.clientType = clientType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.remark = remark;
        this.dataStatus = dataStatus;
        this.tcdetailRelId = tcdetailRelId;
    }

   
    // Property accessors

    public String getObjId() {
        return this.objId;
    }
    
    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getOprTitle() {
        return this.oprTitle;
    }
    
    public void setOprTitle(String oprTitle) {
        this.oprTitle = oprTitle;
    }

    public String getPicUrl() {
        return this.picUrl;
    }
    
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getDetailUrl() {
        return this.detailUrl;
    }
    
    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public Integer getSort() {
        return this.sort;
    }
    
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getOprType() {
        return this.oprType;
    }
    
    public void setOprType(String oprType) {
        this.oprType = oprType;
    }

    public Integer getOprStatus() {
        return this.oprStatus;
    }
    
    public void setOprStatus(Integer oprStatus) {
        this.oprStatus = oprStatus;
    }

    public String getClientType() {
        return this.clientType;
    }
    
    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public Date getStartTime() {
        return this.startTime;
    }
    
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }
    
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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

    public String getTcdetailRelId() {
        return this.tcdetailRelId;
    }
    
    public void setTcdetailRelId(String tcdetailRelId) {
        this.tcdetailRelId = tcdetailRelId;
    }
   








}