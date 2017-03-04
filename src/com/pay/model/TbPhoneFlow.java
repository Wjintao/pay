package com.pay.model;

import java.util.Date;


/**
 * TbPhoneFlow entity. @author MyEclipse Persistence Tools
 */
public class TbPhoneFlow extends AbstractTbPhoneFlow implements java.io.Serializable {

    // Constructors

    /** default constructor */
    public TbPhoneFlow() {}

    /** minimal constructor */
    public TbPhoneFlow(String objId) {
        super(objId);
    }

    /** full constructor */
    public TbPhoneFlow(String objId, Date createTime, Date updateTime,
            Integer dataStatus, String openid, String phone, String flow, String orderid,
            String remark) {
        super(objId, createTime, updateTime, dataStatus, openid, phone, flow, orderid, remark);
    }

}
