package com.pay.model;

import java.sql.Timestamp;


/**
 * TbPay entity. @author MyEclipse Persistence Tools
 */
public class TbPay extends AbstractTbPay implements java.io.Serializable {

    // Constructors

    /** default constructor */
    public TbPay() {
    }

	/** minimal constructor */
    public TbPay(String objId, Integer dataStatus, Integer orderStatus) {
        super(objId, dataStatus, orderStatus);        
    }
    
    /** full constructor */
    public TbPay(String objId, TbTcdetail tbTcdetail, String phone, String openid, String wxno, String wxnick, String prepayid, String outTradeNo, Timestamp createTime, Timestamp updateTime, String remark, Integer dataStatus, Integer orderStatus) {
        super(objId, tbTcdetail, phone, openid, wxno, wxnick, prepayid, outTradeNo, createTime, updateTime, remark, dataStatus, orderStatus);        
    }
   
}
