package com.pay.model;

import java.sql.Timestamp;
import java.util.Date;


/**
 * AbstractTbTctype entity provides the base persistence definition of the TbTctype entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractTbTctype  implements java.io.Serializable {


    // Fields    

     private String objId;
     private String tctype;
     private Integer sort;
     private Date createTime;
     private Date updateTime;
     private String remark;
     private Integer dataStatus;


    // Constructors

    /** default constructor */
    public AbstractTbTctype() {
    }

	/** minimal constructor */
    public AbstractTbTctype(String objId, Integer dataStatus) {
        this.objId = objId;
        this.dataStatus = dataStatus;
    }
    
    /** full constructor */
    public AbstractTbTctype(String objId, String tctype, Integer sort, Timestamp createTime, Timestamp updateTime, String remark, Integer dataStatus) {
        this.objId = objId;
        this.tctype = tctype;
        this.sort = sort;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.remark = remark;
        this.dataStatus = dataStatus;
    }

   
    // Property accessors

    public String getObjId() {
        return this.objId;
    }
    
    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getTctype() {
        return this.tctype;
    }
    
    public void setTctype(String tctype) {
        this.tctype = tctype;
    }

    public Integer getSort() {
        return this.sort;
    }
    
    public void setSort(Integer sort) {
        this.sort = sort;
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
   








}