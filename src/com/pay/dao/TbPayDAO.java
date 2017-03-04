package com.pay.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.pay.model.TbPay;

/**
 * A data access object (DAO) providing persistence and search support for TbPay entities.
 * Transaction control of the save(), update() and delete() operations can directly support Spring
 * container-managed transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how to configure it for
 * the desired type of transaction control.
 * 
 * @see com.pay.model.TbPay
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class TbPayDAO  {
    private static final Logger log = LoggerFactory.getLogger(TbPayDAO.class);
    // property constants
    public static final String PHONE = "phone";
    public static final String OPENID = "openid";
    public static final String WXNO = "wxno";
    public static final String WXNICK = "wxnick";
    public static final String PREPAYID = "prepayid";
    public static final String OUT_TRADE_NO = "outTradeNo";
    public static final String REMARK = "remark";
    public static final String DATA_STATUS = "dataStatus";
    public static final String ORDER_STATUS = "orderStatus";

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public Boolean save(TbPay transientInstance) {
        log.debug("saving TbPay instance");
        Boolean ret=false;
        try {
            getSession().save(transientInstance);
            ret = true;
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
        return ret;
    }

    public void delete(TbPay persistentInstance) {
        log.debug("deleting TbPay instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public TbPay findById(java.lang.String id) {
        log.debug("getting TbPay instance with id: " + id);
        try {
            TbPay instance = (TbPay) getSession().get("com.pay.model.TbPay", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }


    public List findByExample(TbPay instance) {
        log.debug("finding TbPay instance by example");
        try {
            List results =
                    getSession().createCriteria("com.pay.model.TbPay")
                            .add(Example.create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List findByProperty(String propertyName, Object value) {
        log.debug("finding TbPay instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from TbPay as model where model." + propertyName + "= ? and model.dataStatus=1";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter(0, value);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List findByPhone(Object phone) {
        return findByProperty(PHONE, phone);
    }

    public List findByOpenid(Object openid) {
        return findByProperty(OPENID, openid);
    }

    public List findByWxno(Object wxno) {
        return findByProperty(WXNO, wxno);
    }

    public List findByWxnick(Object wxnick) {
        return findByProperty(WXNICK, wxnick);
    }

    public List findByPrepayid(Object prepayid) {
        return findByProperty(PREPAYID, prepayid);
    }

    public List findByOutTradeNo(Object outTradeNo) {
        return findByProperty(OUT_TRADE_NO, outTradeNo);
    }

    public List findByRemark(Object remark) {
        return findByProperty(REMARK, remark);
    }

    public List findByDataStatus(Object dataStatus) {
        return findByProperty(DATA_STATUS, dataStatus);
    }

    public List findByOrderStatus(Object orderStatus) {
        return findByProperty(ORDER_STATUS, orderStatus);
    }


    public List findAll() {
        log.debug("finding all TbPay instances");
        try {
            String queryString = "from TbPay";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public TbPay merge(TbPay detachedInstance) {
        log.debug("merging TbPay instance");
        try {
            TbPay result = (TbPay) getSession().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TbPay instance) {
        log.debug("attaching dirty TbPay instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(TbPay instance) {
        log.debug("attaching clean TbPay instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public Boolean update(String out_trade_no) {
        Integer ret=0;
        try{
            Query query = getSession().createQuery("update TbPay t set t.orderStatus=2 where " +
                    "t.outTradeNo='"+out_trade_no+"' and t.dataStatus=1");
            ret=query.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
        return ret>0;
    }
    
    public Boolean updateClose(String out_trade_no,Integer status) {
        Integer ret=0;
        try{
            Query query = getSession().createQuery("update TbPay t set t.orderStatus="+status+" where " +
                    "t.outTradeNo='"+out_trade_no+"' and t.orderStatus=1 and t.dataStatus=1");
            ret=query.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
        return ret>0;
    }
}
