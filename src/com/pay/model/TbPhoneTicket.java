package com.pay.model;

import java.util.Date;


/**
 * TbPhoneTicket entity. @author MyEclipse Persistence Tools
 */
public class TbPhoneTicket extends AbstractTbPhoneTicket implements java.io.Serializable {

    // Constructors

    /** default constructor */
    public TbPhoneTicket() {}

    /** minimal constructor */
    public TbPhoneTicket(String objId) {
        super(objId);
    }

    /** full constructor */
    public TbPhoneTicket(String objId, Date createTime, Date updateTime,
            Integer dataStatus, String openid, String phone, Integer ticketgetCount,
            String ticket1, String ticket2, String ticket3, String ticket4) {
        super(objId, createTime, updateTime, dataStatus, openid, phone, ticketgetCount, ticket1,
                ticket2, ticket3, ticket4);
    }

}
