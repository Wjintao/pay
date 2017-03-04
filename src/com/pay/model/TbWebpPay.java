package com.pay.model;

import java.sql.Date;


/**
 * TbWebpPay entity. @author MyEclipse Persistence Tools
 */
public class TbWebpPay extends AbstractTbWebpPay implements java.io.Serializable {

    // Constructors

    /** default constructor */
    public TbWebpPay() {}

    /** minimal constructor */
    public TbWebpPay(String objId, Integer dataStatus, Integer orderStatus) {
        super(objId, dataStatus, orderStatus);
    }

    /** full constructor */
    public TbWebpPay(String objId, String phone, String mid, String aptrid, Date createTime,
            Date updateTime, String remark, Integer dataStatus, Integer orderStatus,
            String tcdetailRelId,String quDao,String sin) {
        super(objId, phone, mid, aptrid, createTime, updateTime, remark, dataStatus, orderStatus,
                tcdetailRelId,quDao,sin);
    }

}
