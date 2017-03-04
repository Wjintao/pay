package com.pay.dao;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.pay.model.TbOperate;

/**
 	* A data access object (DAO) providing persistence and search support for TbOperate entities.
 			* Transaction control of the save(), update() and delete() operations 
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see com.pay.model.TbOperate
  * @author MyEclipse Persistence Tools 
 */
    @Transactional   
public class TbOperateDAO  {
	     private static final Logger log = LoggerFactory.getLogger(TbOperateDAO.class);
		//property constants
	public static final String OPR_TITLE = "oprTitle";
	public static final String PIC_URL = "picUrl";
	public static final String DETAIL_URL = "detailUrl";
	public static final String SORT = "sort";
	public static final String OPR_TYPE = "oprType";
	public static final String OPR_STATUS = "oprStatus";
	public static final String CLIENT_TYPE = "clientType";
	public static final String REMARK = "remark";
	public static final String DATA_STATUS = "dataStatus";
	public static final String TCDETAIL_REL_ID = "tcdetailRelId";



    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory){
       this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession(){
     return sessionFactory.getCurrentSession(); 
    }
	protected void initDao() {
		//do nothing
	}
    
    public void save(TbOperate transientInstance) {
        log.debug("saving TbOperate instance");
        try {
            getCurrentSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TbOperate persistentInstance) {
        log.debug("deleting TbOperate instance");
        try {
            getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public TbOperate findById( java.lang.String id) {
        log.debug("getting TbOperate instance with id: " + id);
        try {
            TbOperate instance = (TbOperate) getCurrentSession()
                    .get("com.pay.model.TbOperate", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(TbOperate instance) {
        log.debug("finding TbOperate instance by example");
        try {
            List results = getCurrentSession().createCriteria("com.pay.model.TbOperate") .add(Example.create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public List findByProperty(String propertyName, Object value) {
      log.debug("finding TbOperate instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TbOperate as model where model." 
         						+ propertyName + "= ?";
         Query queryObject = getCurrentSession().createQuery(queryString);
		 queryObject.setParameter(0, value);
		 return queryObject.list();
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByOprTitle(Object oprTitle
	) {
		return findByProperty(OPR_TITLE, oprTitle
		);
	}
	
	public List findByPicUrl(Object picUrl
	) {
		return findByProperty(PIC_URL, picUrl
		);
	}
	
	public List findByDetailUrl(Object detailUrl
	) {
		return findByProperty(DETAIL_URL, detailUrl
		);
	}
	
	public List findBySort(Object sort
	) {
		return findByProperty(SORT, sort
		);
	}
	
	public List findByOprType(Object oprType
	) {
		return findByProperty(OPR_TYPE, oprType
		);
	}
	
	public List findByOprStatus(Object oprStatus
	) {
		return findByProperty(OPR_STATUS, oprStatus
		);
	}
	
	public List findByClientType(Object clientType
	) {
		return findByProperty(CLIENT_TYPE, clientType
		);
	}
	
	public List findByRemark(Object remark
	) {
		return findByProperty(REMARK, remark
		);
	}
	
	public List findByDataStatus(Object dataStatus
	) {
		return findByProperty(DATA_STATUS, dataStatus
		);
	}
	
	public List findByTcdetailRelId(Object tcdetailRelId
	) {
		return findByProperty(TCDETAIL_REL_ID, tcdetailRelId
		);
	}
	
	public List findAllByClientType(String cntType) {
		log.debug("finding all TbOperate instances");
		try {
			String queryString = "from TbOperate t where t.oprStatus=1 and t.clientType='"+cntType+"' and t.dataStatus=1 order by t.sort asc";
	         Query queryObject = getCurrentSession().createQuery(queryString);
			 return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public TbOperate merge(TbOperate detachedInstance) {
        log.debug("merging TbOperate instance");
        try {
            TbOperate result = (TbOperate) getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TbOperate instance) {
        log.debug("attaching dirty TbOperate instance");
        try {
            getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TbOperate instance) {
        log.debug("attaching clean TbOperate instance");
        try {
                      	getCurrentSession().buildLockRequest(LockOptions.NONE).lock(instance);
          	            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    
	public static TbOperateDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TbOperateDAO) ctx.getBean("TbOperateDAO");
	}
}