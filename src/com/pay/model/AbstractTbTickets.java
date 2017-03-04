package com.pay.model;

import java.util.Date;




/**
 * AbstractTbTickets entity provides the base persistence definition of the TbTickets entity. @author
 * MyEclipse Persistence Tools
 */

public abstract class AbstractTbTickets implements java.io.Serializable {


    // Fields

    private String objId;
    private String ticketq;
    private Date createTime;
    private Date updateTime;
    private Integer dataStatus;
    private String tickets;


    // Constructors

    /** default constructor */
    public AbstractTbTickets() {}

    /** minimal constructor */
    public AbstractTbTickets(String objId) {
        this.objId = objId;
    }

    /** full constructor */
    public AbstractTbTickets(String objId, String ticketq,Date createTime, Date updateTime,
            Integer dataStatus, String tickets) {
        this.objId = objId;
        this.ticketq=ticketq;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.dataStatus = dataStatus;
        this.tickets = tickets;
    }


    // Property accessors

    public String getObjId() {
        return this.objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDataStatus() {
        return this.dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getTickets() {
        return this.tickets;
    }

    public void setTickets(String tickets) {
        this.tickets = tickets;
    }

    public String getTicketq() {
        return ticketq;
    }

    public void setTicketq(String ticketq) {
        this.ticketq = ticketq;
    }



}
