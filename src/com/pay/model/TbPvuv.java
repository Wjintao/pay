package com.pay.model;

import java.util.Date;


/**
 * TbPvuv entity. @author MyEclipse Persistence Tools
 */
public class TbPvuv extends AbstractTbPvuv implements java.io.Serializable {

    // Constructors

    /** default constructor */
    public TbPvuv() {}

    /** minimal constructor */
    public TbPvuv(String objId) {
        super(objId);
    }

    /** full constructor */
    public TbPvuv(String objId, Date createTime, Date updateTime, Integer dataStatus,
            String realIp, String phone, String form, String channel, String remark) {
        super(objId, createTime, updateTime, dataStatus, realIp, phone, form, channel, remark);
    }

}
