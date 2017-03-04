package com.pay.dao;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.pay.model.TbPhoneFlow;
import com.pay.model.TbPhoneTicket;
import com.pay.model.TbWebpPay;

/**
 * A data access object (DAO) providing persistence and search support for TbPhoneTicket entities.
 * Transaction control of the save(), update() and delete() operations can directly support Spring
 * container-managed transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how to configure it for
 * the desired type of transaction control.
 * 
 * @see com.pay.model.TbPhoneTicket
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class TbPhoneTicketDAO {
    private static final Logger log = LoggerFactory.getLogger(TbPhoneTicketDAO.class);
    // property constants
    public static final String DATA_STATUS = "dataStatus";
    public static final String OPENID = "openid";
    public static final String PHONE = "phone";
    public static final String TICKETGET_COUNT = "ticketgetCount";
    public static final String TICKET1 = "ticket1";
    public static final String TICKET2 = "ticket2";
    public static final String TICKET3 = "ticket3";
    public static final String TICKET4 = "ticket4";

    @Autowired
    TbTicketsDAO tbTicketsDAO;

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    /*
     * public void save(TbPhoneTicket transientInstance) {
     * log.debug("saving TbPhoneTicket instance"); try { getSession().save(transientInstance);
     * log.debug("save successful"); } catch (RuntimeException re) { log.error("save failed", re);
     * throw re; } }
     */
    public Boolean save(TbPhoneTicket transientInstance) {
        log.debug("saving TbPhoneTicket instance");
        Boolean ret = false;
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

    public void delete(TbPhoneTicket persistentInstance) {
        log.debug("deleting TbPhoneTicket instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public TbPhoneTicket findById(java.lang.String id) {
        log.debug("getting TbPhoneTicket instance with id: " + id);
        try {
            TbPhoneTicket instance =
                    (TbPhoneTicket) getSession().get("com.pay.model.TbPhoneTicket", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }


    public List findByExample(TbPhoneTicket instance) {
        log.debug("finding TbPhoneTicket instance by example");
        try {
            List results =
                    getSession().createCriteria("com.pay.model.TbPhoneTicket")
                            .add(Example.create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List findByProperty(String propertyName, Object value) {
        log.debug("finding TbPhoneTicket instance with property: " + propertyName + ", value: "
                + value);
        try {
            String queryString = "from TbPhoneTicket as model where model." + propertyName + "= ?";
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

    public List findByOpenid(Object openid) {
        return findByProperty(OPENID, openid);
    }

    public List findByPhone(Object phone, String objIdTc) {
        log.debug("finding all TbPhoneTicket instances");
        try {
            String queryString = " from TbPhoneTicket t where  t.dataStatus=1 and t.phone='"+phone+"'  and  t.ticket3='"+ objIdTc +"'";
            Query query = getSession().createQuery(queryString);
            List list=query.list();
            return list;
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }

    }

    public List findByTicketgetCount(Object ticketgetCount) {
        return findByProperty(TICKETGET_COUNT, ticketgetCount);
    }

    public List findByTicket1(Object ticket1) {
        return findByProperty(TICKET1, ticket1);
    }

    public List findByTicket2(Object ticket2) {
        return findByProperty(TICKET2, ticket2);
    }

    public List findByTicket3(Object ticket3) {
        return findByProperty(TICKET3, ticket3);
    }

    public List findByTicket4(Object ticket4) {
        return findByProperty(TICKET4, ticket4);
    }


    public List findAll() {
        log.debug("finding all TbPhoneTicket instances");
        try {
            String queryString = "from TbPhoneTicket";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public TbPhoneTicket merge(TbPhoneTicket detachedInstance) {
        log.debug("merging TbPhoneTicket instance");
        try {
            TbPhoneTicket result = (TbPhoneTicket) getSession().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TbPhoneTicket instance) {
        log.debug("attaching dirty TbPhoneTicket instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(TbPhoneTicket instance) {
        log.debug("attaching clean TbPhoneTicket instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public Boolean update(String outTradeNo, String phoneString, Integer count, String ticketString) {
        Integer ret = 0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTimeString = dateFormat.format(new Date());
        try {
            if (count == 2) {
                Query query =
                        getSession().createQuery(
                                "update TbPhoneTicket t set t.ticket2='" + ticketString
                                        + "',  t.ticket4='" + outTradeNo + "', t.ticketgetCount="
                                        + count + ", t.updateTime='" + nowTimeString + "' where "
                                        + "t.phone='" + phoneString + "' and t.dataStatus=1");
                tbTicketsDAO.update(ticketString);
                ret = query.executeUpdate();
            } else {
                Query query =
                        getSession().createQuery(
                                "update TbPhoneTicket t set  t.ticketgetCount=3, t.updateTime='"
                                        + nowTimeString + "' where " + "t.phone='" + phoneString
                                        + "' and t.dataStatus=1");
                ret = query.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret > 0;
    }
    
    public List<TbPhoneTicket> findByPhoneUFO(String phone,String tcId) {
        try {
            
            String queryString = " from TbPhoneTicket t where  t.dataStatus=1 and t.phone='"+phone+"'  and  t.ticket3='"+ tcId +"'  and  createTime > '" + getTimeByMinute(-5)+"'";
            Query query = getSession().createQuery(queryString);
            List list=query.list();
            return list;
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }
    
    public static String getTimeByMinute(int minute) {

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MINUTE, minute);

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

    }
}
