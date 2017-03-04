package com.pay.model;

import java.sql.Timestamp;
import java.util.Set;


/**
 * TbTctype entity. @author MyEclipse Persistence Tools
 */
public class TbTctype extends AbstractTbTctype implements java.io.Serializable {

    // Constructors

    /** default constructor */
    public TbTctype() {
    }

	/** minimal constructor */
    public TbTctype(String objId, Integer dataStatus) {
        super(objId, dataStatus);        
    }
    
    /** full constructor */
    public TbTctype(String objId, String tctype, Integer sort, Timestamp createTime, Timestamp updateTime, String remark, Integer dataStatus) {
        super(objId, tctype, sort, createTime, updateTime, remark, dataStatus);        
    }
}
