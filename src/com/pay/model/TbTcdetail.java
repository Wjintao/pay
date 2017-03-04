package com.pay.model;

import java.sql.Timestamp;
import java.util.Set;


/**
 * TbTcdetail entity. @author MyEclipse Persistence Tools
 */
public class TbTcdetail extends AbstractTbTcdetail implements java.io.Serializable {

    // Constructors

    /** default constructor */
    public TbTcdetail() {
    }

	/** minimal constructor */
    public TbTcdetail(String objId, String actype, Integer dataStatus) {
        super(objId, actype, dataStatus);        
    }
    
    /** full constructor */
    public TbTcdetail(String objId, String tcname, String tcprice, String tcpriceNow, String tcpace, String tctime, Integer sortbyPrice, Integer sortbySpace, Integer sortbyTime, String actype, Double discount, Timestamp createTime, Timestamp updateTime, String remark, Integer dataStatus, String tctypeRelId,String sin) {
        super(objId, tcname, tcprice, tcpriceNow, tcpace, tctime, sortbyPrice, sortbySpace, sortbyTime, actype, discount, createTime, updateTime, remark, dataStatus, tctypeRelId, sin);        
    }
   
}
