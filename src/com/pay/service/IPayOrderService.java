package com.pay.service;

import com.pay.model.ReturnMessage;

public interface IPayOrderService {
	
	
	/**
	 * 微信支付统一下单
	 * @param tcId
	 * @param openId
	 * @param phone
	 * @param wxno
	 * @param wxnick
	 * @param outTradeNo
	 * @param inputTime 当前时间戳
	 * @param sign 签名
	 * @return
	 */
    ReturnMessage addPayOrder(String lx,String tcId,String openId,String phone,String wxno,String wxnick,String outTradeNo,String ip,String inputTime, String sign);
   
    /**
     * 
     * @Description: TODO 微信支付统一下单
     * @param @param cntType
     * @param @param lx
     * @param @param tcId
     * @param @param openId
     * @param @param phone
     * @param @param wxno
     * @param @param wxnick
     * @param @param outTradeNo
     * @param @param ip
     * @param @param inputTime
     * @param @param sign
     * @param @param channel
     * @param @return   
     * @return ReturnMessage  
     * @throws
     * @author wujintao
     * @date 2016-10-18
     */
    ReturnMessage addWxPayOrder(String cntType,String lx,String tcId,String openId,String phone,String wxno,String wxnick,String outTradeNo,String ip,String inputTime, String sign,String channel);
    
    /**
     * 更新订单状态为成功状态
     * @param outTradeNo
     * @return
     */
    ReturnMessage updatePayOrder(String outTradeNo); 
    
    /**
     * 
     * @param outTradeNo
     * @return
     */
    ReturnMessage updateWxPayOrder(String outTradeNo); 
    
    /**
     * 更新订单信息为关闭状态
     * @param outTradeNo
     * @return
     */
    ReturnMessage closePayOrder(String outTradeNo);
    
    /**
     * 
     * @Description: TODO 查询套餐加载页面
     * @param @param phone
     * @param @param inputTime
     * @param @param sign
     * @param @return   
     * @return ReturnMessage  
     * @throws
     * @author wujintao
     * @date 2016-8-9
     */
    ReturnMessage getTcDetail(String phone,String inputTime, String sign,String objid);
    
    /**
     * 获取套餐时限
     * @param phone
     * @param inputTime
     * @param sign
     * @return
     */
    //ReturnMessage getTcTime(String phone,String inputTime, String sign);
    
    /**
     * 
     * @Description: TODO 获取运营信息
     * @param @param phone
     * @param @param cntType
     * @param @param inputTime
     * @param @param sign
     * @param @return   
     * @return ReturnMessage  
     * @throws
     * @author wujintao
     * @date 2016-8-9
     */
    ReturnMessage getOperate(String phone,String cntType,String inputTime, String sign);
    
    /**
     * 
     * @Description: TODO 生成微信支付二维码链接（入库）
     * @param @param lx
     * @param @param tcId
     * @param @param phone
     * @param @param productId
     * @param @param inputTime
     * @param @param sign
     * @param @param channel
     * @param @return   
     * @return ReturnMessage  
     * @throws
     * @author wujintao
     * @date 2016-10-18
     */
    ReturnMessage getCode(String lx, String tcId, String phone, String productId,String inputTime, String sign, String channel);
    
    /**
     * 
     * @Description: TODO 获取电影券
     * @param @param phone
     * @param @param inputTime
     * @param @param sign
     * @param @param objid
     * @param @return   
     * @return ReturnMessage  
     * @throws
     * @author wujintao
     * @date 2016-9-19
     */
    ReturnMessage getTicket(String phone,String openId, String tcId, String inputTime, String sign);

    
    /**
     * 
     * @Description: TODO 写入电影票与手机号
     * @param @param outTradeNo openId
     * @param @return   
     * @return ReturnMessage  
     * @throws
     * @author wujintao
     * @date 2016-9-19
     */
    ReturnMessage addtbPhoneTicket(String outTradeNo , String openId);
    
    /**
     * 
     * @Description: TODO 校验流量赠送活动首单
     * @param @param phone
     * @param @param inputTime
     * @param @param sign
     * @param @return   
     * @return ReturnMessage  
     * @throws
     * @author wujintao
     * @date 2016-10-12
     */
    ReturnMessage getIsUFO(String phone, String openId, String inputTime, String sign);
    
    /**
     * 
     * @Description: TODO 确认订单套餐ID
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
     * @Description: TODO 判断是否符合流量赠送
     * @param @param outTradeNo
     * @param @param openId
     * @param @return   
     * @return ReturnMessage  
     * @throws
     * @author wujintao
     * @date 2016-10-12
     */
    ReturnMessage isAccordFlow(String outTradeNo, String phone,String objIdTc);
    
    /**
     * 
     * @Description: TODO 流量赠送业务代码（请求响应）
     * @param @param phone
     * @param @param string
     * @param @return   
     * @return ReturnMessage  
     * @throws
     * @author wujintao
     * @date 2016-10-13
     */
    ReturnMessage flowGive(String outTradeNo,String openId, String phone, String flow);
    
    /**
     * 
     * @Description: TODO 乐视卡赠送数据生成
     * @param @param openId
     * @param @param phone
     * @param @param outTradeNo
     * @param @param objIdTc
     * @param @param cardfrom
     * @param @return   
     * @return ReturnMessage  
     * @throws
     * @author wujintao
     * @date 2016-10-24
     */
    ReturnMessage generateLeShiKaOrder(String openId, String phone, String outTradeNo, String objIdTc, String cardfrom);
}
