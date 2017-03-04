package com.pay.model;

import java.sql.Timestamp;


/**
 * TbWxpay entity. @author MyEclipse Persistence Tools
 */
public class TbWxpay extends AbstractTbWxpay implements java.io.Serializable {

    // Constructors

    /** default constructor */
    public TbWxpay() {
    }

	/** minimal constructor */
    public TbWxpay(String objId, Integer dataStatus, Integer orderStatus) {
        super(objId, dataStatus, orderStatus);        
    }
    
    /** full constructor */
    public TbWxpay(String objId, String phone, String openid, String wxno, String wxnick, String prepayid, String outTradeNo, String productId, String tradeType, Timestamp createTime, Timestamp updateTime, String remark, Integer dataStatus, Integer orderStatus, String tcdetailRelId) {
        super(objId, phone, openid, wxno, wxnick, prepayid, outTradeNo, productId, tradeType, createTime, updateTime, remark, dataStatus, orderStatus, tcdetailRelId);        
    }
   
}
