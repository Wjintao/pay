package com.pay.dao;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

import com.pay.model.TbTickets;

/**
 * A data access object (DAO) providing persistence and search support for TbTickets entities.
 * Transaction control of the save(), update() and delete() operations can directly support Spring
 * container-managed transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how to configure it for
 * the desired type of transaction control.
 * 
 * @see com.pay.model.TbTickets
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class TbTicketsDAO {
    private static final Logger log = LoggerFactory.getLogger(TbTicketsDAO.class);
    // property constants
    public static final String DATA_STATUS = "dataStatus";
    public static final String TICKETS = "tickets";

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void save(TbTickets transientInstance) {
        log.debug("saving TbTickets instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(TbTickets persistentInstance) {
        log.debug("deleting TbTickets instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public TbTickets findById(java.lang.String id) {
        log.debug("getting TbTickets instance with id: " + id);
        try {
            TbTickets instance = (TbTickets) getSession().get("com.pay.model.TbTickets", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }


    public List findByExample(TbTickets instance) {
        log.debug("finding TbTickets instance by example");
        try {
            List results =
                    getSession().createCriteria("com.pay.model.TbTickets")
                            .add(Example.create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List findByProperty(String propertyName, Object value) {
        log.debug("finding TbTickets instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from TbTickets as model where model." + propertyName + "= ?";
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

    public List findByTickets(Object tickets) {
        return findByProperty(TICKETS, tickets);
    }


    public List findAll() {
        log.debug("finding all TbTickets instances");
        try {
            String queryString = "from TbTickets";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public TbTickets merge(TbTickets detachedInstance) {
        log.debug("merging TbTickets instance");
        try {
            TbTickets result = (TbTickets) getSession().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TbTickets instance) {
        log.debug("attaching dirty TbTickets instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(TbTickets instance) {
        log.debug("attaching clean TbTickets instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public Boolean isExtract() {
        log.debug("finding all TbTickets instances");
        try {
            //String hql =
            //        "select count(*) from TbTickets as model where model.dataStatus=0 and to_days(model.updateTime) = to_days(now())";
            String hql =
                    "select count(*) from TbTickets as model where model.dataStatus=0 ";
            Query query = getSession().createQuery(hql);
            if (((Long) query.uniqueResult()).longValue()> 999) {
                return false;
            } else {
                return true;
            }
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }
    
    public Boolean update(String ticketString) {
        Integer ret = 0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTimeString = dateFormat.format(new Date());
        try {
                Query query =
                        getSession().createQuery(
                                "update TbTickets t set t.dataStatus=0, t.updateTime='"
                                        + nowTimeString + "' where " + "t.tickets='" + ticketString
                                        + "' and t.dataStatus=1");
                ret = query.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret > 0;
    }
}
