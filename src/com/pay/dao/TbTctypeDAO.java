package com.pay.dao;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.pay.model.TbTctype;

/**
 * A data access object (DAO) providing persistence and search support for TbTctype entities.
 * Transaction control of the save(), update() and delete() operations can directly support Spring
 * container-managed transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how to configure it for
 * the desired type of transaction control.
 * 
 * @see com.pay.model.TbTctype
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class TbTctypeDAO {
    private static final Logger log = LoggerFactory.getLogger(TbTctypeDAO.class);
    // property constants
    public static final String TCTYPE = "tctype";
    public static final String REMARK = "remark";
    public static final String DATA_STATUS = "dataStatus";
	public static final String SORT = "sort";

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void save(TbTctype transientInstance) {
        log.debug("saving TbTctype instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(TbTctype persistentInstance) {
        log.debug("deleting TbTctype instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public TbTctype findById(java.lang.String id) {
        log.debug("getting TbTctype instance with id: " + id);
        try {
            TbTctype instance = (TbTctype) getSession().get("com.pay.model.TbTctype", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }


    public List findByExample(TbTctype instance) {
        log.debug("finding TbTctype instance by example");
        try {
            List results =
                    getSession().createCriteria("com.pay.model.TbTctype")
                            .add(Example.create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List findByProperty(String propertyName, Object value) {
        log.debug("finding TbTctype instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from TbTctype as model where model." + propertyName + "= ?";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter(0, value);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }

    public List findByTctype(Object tctype) {
        return findByProperty(TCTYPE, tctype);
    }

    public List findByRemark(Object remark) {
        return findByProperty(REMARK, remark);
    }

    public List findByDataStatus(Object dataStatus) {
        return findByProperty(DATA_STATUS, dataStatus);
    }


    public List findAll() {
        log.debug("finding all TbTctype instances");
        try {
            String queryString = "from TbTctype";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public TbTctype merge(TbTctype detachedInstance) {
        log.debug("merging TbTctype instance");
        try {
            TbTctype result = (TbTctype) getSession().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TbTctype instance) {
        log.debug("attaching dirty TbTctype instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(TbTctype instance) {
        log.debug("attaching clean TbTctype instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
}
