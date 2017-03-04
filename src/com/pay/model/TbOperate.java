package com.pay.model;

import java.sql.Timestamp;


/**
 * TbOperate entity. @author MyEclipse Persistence Tools
 */
public class TbOperate extends AbstractTbOperate implements java.io.Serializable {

    // Constructors

    /** default constructor */
    public TbOperate() {
    }

	/** minimal constructor */
    public TbOperate(String objId, String oprType, Integer oprStatus, String clientType, Integer dataStatus) {
        super(objId, oprType, oprStatus, clientType, dataStatus);        
    }
    
    /** full constructor */
    public TbOperate(String objId, String oprTitle, String picUrl, String detailUrl, Integer sort, String oprType, Integer oprStatus, String clientType, Timestamp startTime, Timestamp endTime, Timestamp createTime, Timestamp updateTime, String remark, Integer dataStatus, String tcdetailRelId) {
        super(objId, oprTitle, picUrl, detailUrl, sort, oprType, oprStatus, clientType, startTime, endTime, createTime, updateTime, remark, dataStatus, tcdetailRelId);        
    }
   
}
