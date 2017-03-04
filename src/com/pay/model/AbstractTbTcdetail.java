package com.pay.model;

import java.sql.Timestamp;
import java.util.Date;


/**
 * AbstractTbTcdetail entity provides the base persistence definition of the TbTcdetail entity. @author MyEclipse Persistence Tools
 */

public abstract class AbstractTbTcdetail  implements java.io.Serializable {


    // Fields    

     private String objId;
     private String tcname;
     private String tcprice;
     private String tcpriceNow;
     private String tcpace;
     private String tctime;
     private Integer sortbyPrice;
     private Integer sortbySpace;
     private Integer sortbyTime;
     private String actype;
     private Double discount;
     private Date createTime;
     private Date updateTime;
     private String remark;
     private Integer dataStatus;
     private String tctypeRelId;
     private String sin;

    // Constructors

    /** default constructor */
    public AbstractTbTcdetail() {
    }

	/** minimal constructor */
    public AbstractTbTcdetail(String objId, String actype, Integer dataStatus) {
        this.objId = objId;
        this.actype = actype;
        this.dataStatus = dataStatus;
    }
    
    /** full constructor */
    public AbstractTbTcdetail(String objId, String tcname, String tcprice, String tcpriceNow, String tcpace, String tctime, Integer sortbyPrice, Integer sortbySpace, Integer sortbyTime, String actype, Double discount, Timestamp createTime, Timestamp updateTime, String remark, Integer dataStatus, String tctypeRelId,String sin) {
        this.objId = objId;
        this.tcname = tcname;
        this.tcprice = tcprice;
        this.tcpriceNow = tcpriceNow;
        this.tcpace = tcpace;
        this.tctime = tctime;
        this.sortbyPrice = sortbyPrice;
        this.sortbySpace = sortbySpace;
        this.sortbyTime = sortbyTime;
        this.actype = actype;
        this.discount = discount;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.remark = remark;
        this.dataStatus = dataStatus;
        this.tctypeRelId = tctypeRelId;
        this.sin=sin;
    }

   
    // Property accessors

    public String getObjId() {
        return this.objId;
    }
    
    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getTcname() {
        return this.tcname;
    }
    
    public void setTcname(String tcname) {
        this.tcname = tcname;
    }

    public String getTcprice() {
        return this.tcprice;
    }
    
    public void setTcprice(String tcprice) {
        this.tcprice = tcprice;
    }

    public String getTcpriceNow() {
        return this.tcpriceNow;
    }
    
    public void setTcpriceNow(String tcpriceNow) {
        this.tcpriceNow = tcpriceNow;
    }

    public String getTcpace() {
        return this.tcpace;
    }
    
    public void setTcpace(String tcpace) {
        this.tcpace = tcpace;
    }

    public String getTctime() {
        return this.tctime;
    }
    
    public void setTctime(String tctime) {
        this.tctime = tctime;
    }

    public Integer getSortbyPrice() {
        return this.sortbyPrice;
    }
    
    public void setSortbyPrice(Integer sortbyPrice) {
        this.sortbyPrice = sortbyPrice;
    }

    public Integer getSortbySpace() {
        return this.sortbySpace;
    }
    
    public void setSortbySpace(Integer sortbySpace) {
        this.sortbySpace = sortbySpace;
    }

    public Integer getSortbyTime() {
        return this.sortbyTime;
    }
    
    public void setSortbyTime(Integer sortbyTime) {
        this.sortbyTime = sortbyTime;
    }

    public String getActype() {
        return this.actype;
    }
    
    public void setActype(String actype) {
        this.actype = actype;
    }

    public Double getDiscount() {
        return this.discount;
    }
    
    public void setDiscount(Double discount) {
        this.discount = discount;
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

    public String getTctypeRelId() {
        return this.tctypeRelId;
    }
    
    public void setTctypeRelId(String tctypeRelId) {
        this.tctypeRelId = tctypeRelId;
    }

    public String getSin() {
        return sin;
    }

    public void setSin(String sin) {
        this.sin = sin;
    }
   
}