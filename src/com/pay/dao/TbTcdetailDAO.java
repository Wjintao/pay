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

import com.pay.model.TbTcdetail;

/**
 * A data access object (DAO) providing persistence and search support for TbTcdetail entities.
 * Transaction control of the save(), update() and delete() operations can directly support Spring
 * container-managed transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how to configure it for
 * the desired type of transaction control.
 * 
 * @see com.pay.model.TbTcdetail
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class TbTcdetailDAO {
    private static final Logger log = LoggerFactory.getLogger(TbTcdetailDAO.class);
    // property constants
    public static final String ID = "objId";
    public static final String TCNAME = "tcname";
	public static final String TCPRICE = "tcprice";
	public static final String TCPRICE_NOW = "tcpriceNow";
	public static final String TCPACE = "tcpace";
	public static final String TCTIME = "tctime";
	public static final String SORTBY_PRICE = "sortbyPrice";
	public static final String SORTBY_SPACE = "sortbySpace";
	public static final String SORTBY_TIME = "sortbyTime";
	public static final String ACTYPE = "actype";
	public static final String DISCOUNT = "discount";
	public static final String REMARK = "remark";
	public static final String DATA_STATUS = "dataStatus";
	public static final String TCTYPE_REL_ID = "tctypeRelId";

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public void save(TbTcdetail transientInstance) {
        log.debug("saving TbTcdetail instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }

    public void delete(TbTcdetail persistentInstance) {
        log.debug("deleting TbTcdetail instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public TbTcdetail findById(java.lang.String id) {
        log.debug("getting TbTcdetail instance with id: " + id);
        try {
            TbTcdetail instance = (TbTcdetail) getSession().get("com.pay.model.TbTcdetail", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }


    public List findByExample(TbTcdetail instance) {
        log.debug("finding TbTcdetail instance by example");
        try {
            List results =
                    getSession().createCriteria("com.pay.model.TbTcdetail")
                            .add(Example.create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List findByProperty(String propertyName, Object value) {
        log.debug("finding TbTcdetail instance with property: " + propertyName + ", value: "
                + value);
        try {
            String queryString = "from TbTcdetail as model where model." + propertyName + "= ?";
            Query queryObject = getSession().createQuery(queryString);
            queryObject.setParameter(0, value);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find by property name failed", re);
            throw re;
        }
    }
    
    public List findAllByTcpace(String objid) {
        log.debug("finding TbTcdetail findAllByTcpace");
        try {
            String queryString = "from TbTcdetail as model where model.dataStatus=1  and model.tctypeRelId='"+ objid +"' order by model.sortbySpace asc";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("findAllByTcpace failed", re);
            throw re;
        }
    }
    
    public List findTcTime() {
    	log.debug("finding TbTcdetail findAllByTcpace");
    	try {
    		String queryString = "from TbTcdetail as model where model.dataStatus=1 order by model.sortbySpace asc";
    		Query queryObject = getSession().createQuery(queryString);
    		return queryObject.list();
    	} catch (RuntimeException re) {
    		log.error("findAllByTcpace failed", re);
    		throw re;
    	}
    }
    
    public List findByObjId(Object objId) {
        return findByProperty(ID, objId);
    }
    
    public List findByTcname(Object tcname) {
    	return findByProperty(TCNAME, tcname);
    }

    public List findByTcprice(Object tcprice) {
        return findByProperty(TCPRICE, tcprice);
    }

    public List findByTcpace(Object tcpace) {
        return findByProperty(TCPACE, tcpace);
    }

    public List findByRemark(Object remark) {
        return findByProperty(REMARK, remark);
    }

    public List findByDataStatus(Object dataStatus) {
        return findByProperty(DATA_STATUS, dataStatus);
    }

    public List findByTctypeRelId(String tctypeRelId) {
        try{
            Query query = getSession().createSQLQuery("select t.obj_id from tb_tcdetail t where t.tctype_rel_id='"+tctypeRelId+"'");
            List list=query.list();
            return list;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public List findAll() {
        log.debug("finding all TbTcdetail instances");
        try {
            String queryString = "from TbTcdetail";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public TbTcdetail merge(TbTcdetail detachedInstance) {
        log.debug("merging TbTcdetail instance");
        try {
            TbTcdetail result = (TbTcdetail) getSession().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TbTcdetail instance) {
        log.debug("attaching dirty TbTcdetail instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(TbTcdetail instance) {
        log.debug("attaching clean TbTcdetail instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
}
