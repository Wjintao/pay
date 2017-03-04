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

import com.pay.model.TbPhoneFlow;
import com.pay.model.TbWxpay;

/**
 * A data access object (DAO) providing persistence and search support for TbPhoneFlow entities.
 * Transaction control of the save(), update() and delete() operations can directly support Spring
 * container-managed transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how to configure it for
 * the desired type of transaction control.
 * 
 * @see com.pay.model.TbPhoneFlow
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class TbPhoneFlowDAO  {
    private static final Logger log = LoggerFactory.getLogger(TbPhoneFlowDAO.class);
    // property constants
    public static final String DATA_STATUS = "dataStatus";
    public static final String OPENID = "openid";
    public static final String PHONE = "phone";
    public static final String FLOW = "flow";
    public static final String ORDERID = "orderid";
    public static final String REMARK = "remark";

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public Boolean save(TbPhoneFlow transientInstance) {
        log.debug("saving TbPhoneFlow instance");
        Boolean ret = false;
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
            ret = true;
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
        return ret;
    }

    public void delete(TbPhoneFlow persistentInstance) {
        log.debug("deleting TbPhoneFlow instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public TbPhoneFlow findById(java.lang.String id) {
        log.debug("getting TbPhoneFlow instance with id: " + id);
        try {
            TbPhoneFlow instance = (TbPhoneFlow) getSession().get("com.pay.model.TbPhoneFlow", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }


    public List findByExample(TbPhoneFlow instance) {
        log.debug("finding TbPhoneFlow instance by example");
        try {
            List results =
                    getSession().createCriteria("com.pay.model.TbPhoneFlow")
                            .add(Example.create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List findByProperty(String propertyName, Object value) {
        log.debug("finding TbPhoneFlow instance with property: " + propertyName + ", value: "
                + value);
        try {
            String queryString = "from TbPhoneFlow as model where model." + propertyName + "= ?";
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

    public List findByPhone(Object phone) {
        return findByProperty(PHONE, phone);
    }

    public List findByFlow(Object flow) {
        return findByProperty(FLOW, flow);
    }

    public List findByOrderid(Object orderid) {
        return findByProperty(ORDERID, orderid);
    }

    public List findByRemark(Object remark) {
        return findByProperty(REMARK, remark);
    }


    public List findAll() {
        log.debug("finding all TbPhoneFlow instances");
        try {
            String queryString = "from TbPhoneFlow";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public TbPhoneFlow merge(TbPhoneFlow detachedInstance) {
        log.debug("merging TbPhoneFlow instance");
        try {
            TbPhoneFlow result = (TbPhoneFlow) getSession().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TbPhoneFlow instance) {
        log.debug("attaching dirty TbPhoneFlow instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(TbPhoneFlow instance) {
        log.debug("attaching clean TbPhoneFlow instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public List<TbPhoneFlow> findByPhoneUFO(String phone,String openId) {
        log.debug("finding all TbPhoneFlow instances");
        try {
            String queryString = " Select t.* from tb_phone_flow t where  t.data_status=1 and t.phone='"+phone+"'  and create_time > (select date_sub(now() ,interval 5 MINUTE))";
            Query query = getSession().createSQLQuery(queryString);
            List list=query.list();
            return list;
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }
}
