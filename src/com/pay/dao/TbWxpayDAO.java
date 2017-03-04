package com.pay.dao;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.pay.constants.SysConstants;
import com.pay.model.TbWebpPay;
import com.pay.model.TbWxpay;

/**
 	* A data access object (DAO) providing persistence and search support for TbWxpay entities.
 			* Transaction control of the save(), update() and delete() operations 
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see com.pay.model.TbWxpay
  * @author MyEclipse Persistence Tools 
 */
    @Transactional   
public class TbWxpayDAO  {
	     private static final Logger log = LoggerFactory.getLogger(TbWxpayDAO.class);
		//property constants
	public static final String PHONE = "phone";
	public static final String OPENID = "openid";
	public static final String WXNO = "wxno";
	public static final String WXNICK = "wxnick";
	public static final String PREPAYID = "prepayid";
	public static final String OUT_TRADE_NO = "outTradeNo";
	public static final String PRODUCT_ID = "productId";
	public static final String TRADE_TYPE = "tradeType";
	public static final String REMARK = "remark";
	public static final String DATA_STATUS = "dataStatus";
	public static final String ORDER_STATUS = "orderStatus";
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
    
    public Boolean save(TbWxpay transientInstance) {
    	Boolean ret=false;
        log.debug("saving TbWxpay instance");
        try {
            getCurrentSession().save(transientInstance);
            ret=true;
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
        return ret;
    }
    
	public void delete(TbWxpay persistentInstance) {
        log.debug("deleting TbWxpay instance");
        try {
            getCurrentSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public TbWxpay findById( java.lang.String id) {
        log.debug("getting TbWxpay instance with id: " + id);
        try {
            TbWxpay instance = (TbWxpay) getCurrentSession()
                    .get("com.pay.model.TbWxpay", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(TbWxpay instance) {
        log.debug("finding TbWxpay instance by example");
        try {
            List results = getCurrentSession().createCriteria("com.pay.model.TbWxpay") .add(Example.create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public List findByProperty(String propertyName, Object value) {
      log.debug("finding TbWxpay instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TbWxpay as model where model." 
         						+ propertyName + "= ?";
         Query queryObject = getCurrentSession().createQuery(queryString);
		 queryObject.setParameter(0, value);
		 return queryObject.list();
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByPhone(Object phone
	) {
		return findByProperty(PHONE, phone
		);
	}
	
	public List findByOpenid(Object openid
	) {
		return findByProperty(OPENID, openid
		);
	}
	
	public List findByWxno(Object wxno
	) {
		return findByProperty(WXNO, wxno
		);
	}
	
	public List findByWxnick(Object wxnick
	) {
		return findByProperty(WXNICK, wxnick
		);
	}
	
	public List findByPrepayid(Object prepayid
	) {
		return findByProperty(PREPAYID, prepayid
		);
	}
	
	public List findByOutTradeNo(Object outTradeNo
	) {
		return findByProperty(OUT_TRADE_NO, outTradeNo
		);
	}
	
	public List findByProductId(Object productId
	) {
		return findByProperty(PRODUCT_ID, productId
		);
	}
	
	public List findByTradeType(Object tradeType
	) {
		return findByProperty(TRADE_TYPE, tradeType
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
	
	public List findByOrderStatus(Object orderStatus
	) {
		return findByProperty(ORDER_STATUS, orderStatus
		);
	}
	
	public List findByTcdetailRelId(Object tcdetailRelId
	) {
		return findByProperty(TCDETAIL_REL_ID, tcdetailRelId
		);
	}
	

	public List findAll() {
		log.debug("finding all TbWxpay instances");
		try {
			String queryString = "from TbWxpay";
	         Query queryObject = getCurrentSession().createQuery(queryString);
			 return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public TbWxpay merge(TbWxpay detachedInstance) {
        log.debug("merging TbWxpay instance");
        try {
            TbWxpay result = (TbWxpay) getCurrentSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TbWxpay instance) {
        log.debug("attaching dirty TbWxpay instance");
        try {
            getCurrentSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TbWxpay instance) {
        log.debug("attaching clean TbWxpay instance");
        try {
                      	getCurrentSession().buildLockRequest(LockOptions.NONE).lock(instance);
          	            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TbWxpayDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TbWxpayDAO) ctx.getBean("TbWxpayDAO");
	}
	
	public Boolean update(String out_trade_no) {
        Integer ret=0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTimeString=dateFormat.format(new Date());
        try{
            Query query = getCurrentSession().createQuery("update TbWxpay t set t.orderStatus=2 , t.updateTime='"+ nowTimeString +"' where " +
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
            Query query = getCurrentSession().createQuery("update TbWxpay t set t.orderStatus="+status+" where " +
                    "t.outTradeNo='"+out_trade_no+"' and t.orderStatus=1 and t.dataStatus=1");
            ret=query.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
        return ret>0;
    }
    
    public Boolean update(String out_trade_no,String openid,String prepayid,String productId,String phone,String tradeType) {
        Integer ret=0;
        try{
            Query query = getCurrentSession().createQuery("update TbWxpay t set t.orderStatus=1,t.outTradeNo='"+out_trade_no +
            		"',t.openid='"+openid+"',t.prepayid='"+prepayid+"' where t.productId='"+productId+"' and t.phone='"+phone+"'" +
                    " and t.tradeType='"+tradeType+"' and t.orderStatus=11 and t.dataStatus=1");
            ret=query.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
        return ret>0;
    }
    
    public List<TbWxpay> findPayedKJ(String newTimeString,String tcdetail_rel_id) {
        log.debug("finding all TbWxpay instances");
        try {
            String queryString = " from TbWxpay t where  t.orderStatus=2 and t.dataStatus=1 and t.updateTime>='"+newTimeString+"' and t.tcdetailRelId  in ("+ tcdetail_rel_id +")  order by t.createTime";
            Query query = getCurrentSession().createQuery(queryString);
            List list=query.list();
            return list;
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }
    
    public List<TbWxpay> findPayedKJsb(String newTimeString,String tcdetail_rel_id) {
        log.debug("finding all TbWxpay instances");
        try {
            String queryString = " Select t.* from tb_wxpay t where  t.data_status=1 and t.order_status=2 and t.create_time>='"+newTimeString+"' and  t.tcdetail_rel_id  in ("+ tcdetail_rel_id +") ";
            Query query = getCurrentSession().createSQLQuery(queryString).addEntity("t",TbWxpay.class);
            List list=query.list();
            return list;
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }
    
    public List<TbWxpay> findByPhoneUFO(String phone,String objIdTc) {
        log.debug("finding all TbWxpay instances");
        try {
            String queryString = " from TbWxpay t where  t.orderStatus=2 and t.dataStatus=1 and t.phone='"+phone+"' and to_days(createTime)=to_days(curdate()) and t.tcdetailRelId='"+ objIdTc +"'";
            Query query = getCurrentSession().createQuery(queryString);
            List list=query.list();
            return list;
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }
    
}