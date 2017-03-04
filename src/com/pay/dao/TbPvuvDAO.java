package com.pay.dao;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.pay.model.TbPvuv;

/**
 * A data access object (DAO) providing persistence and search support for TbPvuv entities.
 * Transaction control of the save(), update() and delete() operations can directly support Spring
 * container-managed transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how to configure it for
 * the desired type of transaction control.
 * 
 * @see com.pay.model.TbPvuv
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class TbPvuvDAO  {
    private static final Logger log = LoggerFactory.getLogger(TbPvuvDAO.class);
    // property constants
    public static final String DATA_STATUS = "dataStatus";
    public static final String REAL_IP = "realIp";
    public static final String PHONE = "phone";
    public static final String FORM = "form";
    public static final String CHANNEL = "channel";
    public static final String REMARK = "remark";

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void save(TbPvuv transientInstance) {
        log.debug("saving TbPvuv instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(TbPvuv persistentInstance) {
        log.debug("deleting TbPvuv instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public TbPvuv findById(java.lang.String id) {
        log.debug("getting TbPvuv instance with id: " + id);
        try {
            TbPvuv instance = (TbPvuv) getSession().get("com.pay.model.TbPvuv", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }


    public List findByExample(TbPvuv instance) {
        log.debug("finding TbPvuv instance by example");
        try {
            List results =
                    getSession().createCriteria("com.pay.model.TbPvuv")
                            .add(Example.create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List findByProperty(String propertyName, Object value) {
        log.debug("finding TbPvuv instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from TbPvuv as model where model." + propertyName + "= ?";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter(0, value);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List findByDataStatus(Object dataStatus) {
        return findByProperty(DATA_STATUS, dataStatus);
    }

    public List findByRealIp(Object realIp) {
        return findByProperty(REAL_IP, realIp);
    }

    public List findByPhone(Object phone) {
        return findByProperty(PHONE, phone);
    }

    public List findByForm(Object form) {
        return findByProperty(FORM, form);
    }

    public List findByChannel(Object channel) {
        return findByProperty(CHANNEL, channel);
    }

    public List findByRemark(Object remark) {
        return findByProperty(REMARK, remark);
    }


    public List findAll() {
        log.debug("finding all TbPvuv instances");
        try {
            String queryString = "from TbPvuv";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public TbPvuv merge(TbPvuv detachedInstance) {
        log.debug("merging TbPvuv instance");
        try {
            TbPvuv result = (TbPvuv) getSession().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TbPvuv instance) {
        log.debug("attaching dirty TbPvuv instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(TbPvuv instance) {
        log.debug("attaching clean TbPvuv instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
}
