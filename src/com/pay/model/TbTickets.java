package com.pay.model;

import java.util.Date;



/**
 * TbTickets entity. @author MyEclipse Persistence Tools
 */
public class TbTickets extends AbstractTbTickets implements java.io.Serializable {

    // Constructors

    /** default constructor */
    public TbTickets() {}

    /** minimal constructor */
    public TbTickets(String objId) {
        super(objId);
    }

    /** full constructor */
    public TbTickets(String objId,String ticketq, Date createTime, Date updateTime, Integer dataStatus,
            String tickets) {
        super(objId, ticketq, createTime, updateTime, dataStatus, tickets);
    }

}
