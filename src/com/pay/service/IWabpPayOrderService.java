package com.pay.service;

import java.util.List;

import com.pay.model.ReturnMessage;
import com.pay.model.TbPay;
import com.pay.model.TbWebpPay;
/**
 * 
 * ClassName: IWabpPayOrderService 
 * @Description: TODO 
 * @author wujintao
 * @date 2016-8-3
 */
public interface IWabpPayOrderService {
   /**
    * 
    * @Description: TODO
    * @param @param tcId
    * @param @param phone
    * @param @param mid
    * @param @param aptrid
    * @param @param inputTime
    * @param @param sign
    * @param @return   
    * @return ReturnMessage  
    * @throws
    * @author wujintao
    * @date 2016-8-3
    */
    ReturnMessage addPayOrder(String quDao,String flag,String lx,String tcId,String phone,String mid,String aptrid,String inputTime, String sign,String openId,String channel);
    
   /**
    * 
    * @Description: TODO 更新订单状态之下单支付成功
    * @param @param aptrid
    * @param @return   
    * @return ReturnMessage  
    * @throws
    * @author wujintao
    * @date 2016-8-3
    */
    ReturnMessage updatePayOrder(String aptrid,Integer OrderType); 
    
    /**
     * 
     * @Description: TODO 更新订单状态之关闭订单
     * @param @param aptrid
     * @param @return   
     * @return ReturnMessage  
     * @throws
     * @author wujintao
     * @date 2016-8-3
     */
    ReturnMessage closePayOrder(String aptrid);
    
    /**
     * 
     * @Description: TODO 确认订单
     * @param @param aptrid
     * @param @return   
     * @return ReturnMessage  
     * @throws
     * @author wujintao
     * @date 2016-8-3
     */
    ReturnMessage queryPayOrder(String aptrid);
    
    /**
     * 
     * @Description: TODO
     * @param @param aptrid
     * @param @return   
     * @return ReturnMessage  
     * @throws
     * @author wujintao
     * @date 2016-9-5
     */
    TbWebpPay queryPayOrderTbWebpPay(String aptrid);
    
    /**
     * 
     * @Description: TODO 同步洗车会员订单接口
     * @param @return   
     * @return ReturnMessage  
     * @throws
     * @author wujintao
     * @date 2016-9-5
     */
    ReturnMessage synchroXiChe(String tbPrice,String PhoneNo);
    
    
    /**
     * 
     * @Description: TODO
     * @param @param tbPrice
     * @param @param PhoneNo
     * @param @return   
     * @return ReturnMessage  
     * @throws
     * @author wujintao
     * @date 2017-2-10
     */
    ReturnMessage synchroTongTong(String Opration,String PhoneNo);
    
    /**
     * 
     * @Description: TODO 包时长业务退订
     * @param @param aptrid
     * @param @param PhoneNo
     * @param @param inputTime
     * @param @return   
     * @return ReturnMessage  
     * @throws
     * @author wujintao
     * @date 2016-9-21
     */
    ReturnMessage unPurchase(String aptrid,String openId,String PhoneNo, String inputTime, String sign);

    /**
     * 
     * @Description: TODO 查询是否有退订订单
     * @param @param penId 
     * @param @param PhoneNo
     * @param @param inputTime
     * @param @param sign
     * @param @return   
     * @return ReturnMessage  
     * @throws
     * @author wujintao
     * @date 2016-9-26
     */
    ReturnMessage getUnPurchase(String openId,String PhoneNo, String inputTime, String sign, String lx);

    /**
     * 
     * @Description: TODO 查询是否有退订订单
     * @param @param penId 
     * @param @param PhoneNo
     * @param @param inputTime
     * @param @param sign
     * @param @return   
     * @return ReturnMessage  
     * @throws
     * @author wujintao
     * @date 2016-9-26
     */
    ReturnMessage getUnPurchaseOrder(String openId,String PhoneNo, String inputTime, String sign, String lx);
}

