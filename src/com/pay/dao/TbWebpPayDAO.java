package com.pay.dao;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.pay.constants.SysConstants;
import com.pay.model.TbTcdetail;
import com.pay.model.TbWebpPay;
import com.pay.model.TbWxpay;
import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * A data access object (DAO) providing persistence and search support for TbWebpPay entities.
 * Transaction control of the save(), update() and delete() operations can directly support Spring
 * container-managed transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how to configure it for
 * the desired type of transaction control.
 * 
 * @see com.pay.model.TbWebpPay
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class TbWebpPayDAO {
    private static final Logger log = LoggerFactory.getLogger(TbWebpPayDAO.class);
    // property constants
    public static final String PHONE = "phone";
    public static final String MID = "mid";
    public static final String APTRID = "aptrid";
    public static final String REMARK = "remark";
    public static final String DATA_STATUS = "dataStatus";
    public static final String ORDER_STATUS = "orderStatus";
    public static final String CREATE_TIME = "createTime";
    public static final String UPDATE_TIME = "updateTime";
    public static final String TCDETAIL_REL_ID = "tcdetailRelId";
    
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public Boolean save(TbWebpPay transientInstance) {
        log.debug("saving TbWebpPay instance");
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

    public void delete(TbWebpPay persistentInstance) {
        log.debug("deleting TbWebpPay instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }

    public TbWebpPay findById(java.lang.String id) {
        log.debug("getting TbWebpPay instance with id: " + id);
        try {
            TbWebpPay instance = (TbWebpPay) getSession().get("com.pay.model.TbWebpPay", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }


    public List findByExample(TbWebpPay instance) {
        log.debug("finding TbWebpPay instance by example");
        try {
            List results =
                    getSession().createCriteria("com.pay.model.TbWebpPay")
                            .add(Example.create(instance)).list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }

    public List findByProperty(String propertyName, Object value) {
        log.debug("finding TbWebpPay instance with property: " + propertyName + ", value: " + value);
        try {
            String queryString = "from TbWebpPay as model where model." + propertyName + "= ?";
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

    public List findByMid(Object mid) {
        return findByProperty(MID, mid);
    }

    public List findByAptrid(Object aptrid) {
        return findByProperty(APTRID, aptrid);
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
        log.debug("finding all TbWebpPay instances");
        try {
            String queryString = "from TbWebpPay";
            Query queryObject = getSession().createQuery(queryString);
            return queryObject.list();
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }

    public TbWebpPay merge(TbWebpPay detachedInstance) {
        log.debug("merging TbWebpPay instance");
        try {
            TbWebpPay result = (TbWebpPay) getSession().merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TbWebpPay instance) {
        log.debug("attaching dirty TbWebpPay instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

    public void attachClean(TbWebpPay instance) {
        log.debug("attaching clean TbWebpPay instance");
        try {
            getSession().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public Boolean update(String aptrid,Integer OrderType) {
        Integer ret=0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTimeString=dateFormat.format(new Date());
        try{
            if (OrderType==1) {
                Query query = getSession().createQuery("update TbWebpPay t set t.orderStatus=2, t.remark='老用户续订', t.createTime='"+ nowTimeString +"', t.updateTime='"+ nowTimeString +"' where " +
                        "t.aptrid='"+aptrid+"' and t.dataStatus=1");
                ret=query.executeUpdate();
            } else if(OrderType==-1){
                Query query = getSession().createQuery("update TbWebpPay t set t.orderStatus=-1, t.remark='老用户退订', t.updateTime='"+ nowTimeString +"' where " +
                        "t.aptrid='"+aptrid+"' and t.dataStatus=1");
                ret=query.executeUpdate();
            }else {
                Query query = getSession().createQuery("update TbWebpPay t set t.orderStatus=2, t.remark='新用户订购', t.updateTime='"+ nowTimeString +"' where " +
                        "t.aptrid='"+aptrid+"' and t.dataStatus=1 and  t.orderStatus in (0,1)");
                ret=query.executeUpdate();
            }        
        }catch(Exception e){
            e.printStackTrace();
        }
        return ret>0;
    }
    
    public Boolean updateClose(String aptrid,Integer status) {
        Integer ret=0;
        try{
            Query query = getSession().createQuery("update TbWebpPay t set t.orderStatus="+status+" where " +
                    "t.aptrid='"+aptrid+"' and t.orderStatus=1 and t.dataStatus=1");
            ret=query.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }
        return ret>0;
    }
    

    public List checkXiCheHy(String phone, String tcId) {

        try {
            Query query =
                    getSession()
                            .createSQLQuery(
                                    "select * from tb_webp_pay t where t.update_time>DATE_SUB(CURDATE(), INTERVAL 1 MONTH) and phone='"
                                            + phone
                                            + "'and  t.order_status in (2,-1) and t.data_status='1' and tcdetail_rel_id='"
                                            + tcId + "'");
            List list = query.list();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    
    public List<TbWebpPay> findPayedKJ(String newTimeString,String tcdetail_rel_id) {
        log.debug("finding all TbWebpPay instances");
        try {
            String queryString = " from TbWebpPay t where  t.orderStatus in (2,-1) and t.dataStatus=1 and t.updateTime>='"+newTimeString+"' and  t.tcdetailRelId  in ("+ tcdetail_rel_id +")  order by t.createTime";
            Query query = getSession().createQuery(queryString);
            List list=query.list();
            return list;
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }
    
    public List<TbWebpPay> findPayedKJHY(String newTimeString,String tcdetail_rel_id) {
        log.debug("finding all TbWebpPay instances");
        try {
            String queryString = " Select t.* from tb_webp_pay t where  t.data_status=1 and t.order_status in (2,-1) and t.update_time>='"+newTimeString+"' and  t.tcdetail_rel_id  in ("+ tcdetail_rel_id +") order by t.create_time ";
            Query query = getSession().createSQLQuery(queryString).addEntity("t",TbWebpPay.class);
            List list=query.list();
            return list;
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }
    
    public List<TbWebpPay> findUnPurchaseHY(String PhoneNo) {
        log.debug("finding all TbWebpPay instances");
        try {
            String queryString = " Select t.* from tb_webp_pay t where  t.data_status=1 and t.order_status=2 and t.phone='"+PhoneNo+"' and  t.tcdetail_rel_id  in ("+ SysConstants.ARR_BSC +") ";
            Query query = getSession().createSQLQuery(queryString).addEntity("t",TbWebpPay.class);
            List<TbWebpPay> list=query.list();
            return list;
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }
    
    public List<TbWebpPay> findUnPurchaseKJ(String PhoneNo) {
        log.debug("finding all TbWebpPay instances");
        try {
            String queryString = " Select t.* from tb_webp_pay t where  t.data_status=1 and t.order_status=2 and t.phone='"+PhoneNo+"' and  t.create_time>='2016-10-27 10:10:00' and   t.create_time<'2016-11-17 11:10:00' and t.tcdetail_rel_id  in ("+ SysConstants.ARR_ASC +") ";
            Query query = getSession().createSQLQuery(queryString).addEntity("t",TbWebpPay.class);
            List<TbWebpPay> list=query.list();
            return list;
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }
    
    public List findUnPurchaseOrderDetailKJ(String PhoneNo) {
        log.debug("finding all TbWebpPay instances");
        try {
            String queryString = " Select {d.*},{t.*} from tb_webp_pay d, tb_tcdetail t where  d.data_status=1 and d.order_status=2 and d.phone='"+PhoneNo+"' and  d.tcdetail_rel_id=t.obj_id and  d.create_time>='2016-10-27 10:10:00' and  d.create_time<'2016-11-17 11:10:00' and  d.tcdetail_rel_id in ("+ SysConstants.ARR_ASC +") ";
            Query query = getSession().createSQLQuery(queryString).addEntity("d",TbWebpPay.class).addEntity("t",TbTcdetail.class);
            List list=query.list();
            return list;
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }
    
    public List findUnPurchaseOrderDetailHY(String PhoneNo) {
        log.debug("finding all TbWebpPay instances");
        try {
            String queryString = " Select {d.*},{t.*} from tb_webp_pay d, tb_tcdetail t where  d.data_status=1 and d.order_status=2 and d.phone='"+PhoneNo+"' and  d.tcdetail_rel_id=t.obj_id and  d.tcdetail_rel_id in ("+ SysConstants.ARR_BSC +") ";
            Query query = getSession().createSQLQuery(queryString).addEntity("d",TbWebpPay.class).addEntity("t",TbTcdetail.class);
            List list=query.list();
            return list;
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }
    
    public List<TbWebpPay> findByPhoneUFO(String phone,String objIdTc) {
        log.debug("finding all TbWebpPay instances");
        try {
            String queryString = " from TbWebpPay t where  t.orderStatus=2 and t.dataStatus=1 and t.phone='"+phone+"' and to_days(createTime)=to_days(curdate()) and t.tcdetailRelId ='"+ objIdTc +"'";
            Query query = getSession().createQuery(queryString);
            List list=query.list();
            return list;
        } catch (RuntimeException re) {
            log.error("find all failed", re);
            throw re;
        }
    }
}

