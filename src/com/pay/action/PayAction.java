package com.pay.action;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pay.constants.SysConstants;
import com.pay.model.MdlPay;
import com.pay.service.IPayOrderService;
import com.pay.service.IWabpPayOrderService;
import com.pay.service.WXPay;
import com.pay.service.WXPrepay;
import com.pay.util.OrderUtil;
import com.pay.util.StringUtil;
import com.zte.weixin.utils.AES;


public class PayAction extends BaseJsonAction {

    private static final Logger logger = LoggerFactory.getLogger(PayAction.class);
    @Inject
    private IPayOrderService payOrderServiceImpl;
    @Inject
    private IWabpPayOrderService wabpPayOrderServiceImpl;

    /**
     * 微信支付-统一下单
     * @return
     * @throws Exception 
     */
    public void prepay() throws Exception {
        try {
            this.getRequest().setCharacterEncoding("utf-8");
            String tcId = this.getRequest().getParameter("tcId");
            String lx = this.getRequest().getParameter("lx");
            String phone = this.getRequest().getParameter("phone");
            String openId = this.getRequest().getParameter("openId");
            String inputTime=this.getRequest().getParameter("inputTime");
			String sign=this.getRequest().getParameter("sign");
            if (StringUtil.isEmpty(tcId) || StringUtil.isEmpty(lx) || StringUtil.isEmpty(phone) || StringUtil.isEmpty(openId)
            		|| StringUtil.isEmpty(inputTime) || StringUtil.isEmpty(sign)) {
                returnMessage.setCode(-1);
                returnMessage.setData(-1);
                returnMessage.setMessage("微信支付统一下单请求参数不能为空！");
            } else {
                returnMessage =payOrderServiceImpl.addPayOrder(lx,tcId, openId, phone, null, null, OrderUtil.GetOrderNumber(""),this.getRequest().getRemoteAddr(),inputTime, sign);

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        outJson(returnMessage);
    }
    
    /**
     * 
     * @Description: TODO 页面初始化套餐
     * @param    
     * @return void
     * @throws
     * @author wujintao
     * @date 2016-8-9
     */
    public void getTcDetail() {
        
        try {
            this.getRequest().setCharacterEncoding("utf-8");
            String phone = this.getRequest().getParameter("phone");
            String inputTime=this.getRequest().getParameter("inputTime");
            String sign=this.getRequest().getParameter("sign");
            String objid=this.getRequest().getParameter("objid");
            if ( StringUtil.isEmpty(phone)||StringUtil.isEmpty(inputTime) || StringUtil.isEmpty(sign)|| StringUtil.isEmpty(objid)) {
                returnMessage.setCode(-1);
                returnMessage.setData(-1);
                returnMessage.setMessage("获取套餐列表请求参数不能为空！");
            } else {
                returnMessage =payOrderServiceImpl.getTcDetail(phone, inputTime, sign, objid);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        outJson(returnMessage);
    }
    
    /**
     * 
     * @Description: TODO 页面初始化运营信息
     * @param    
     * @return void  
     * @throws
     * @author wujintao
     * @date 2016-8-9
     */
    public void getOperate() {
        try {
            this.getRequest().setCharacterEncoding("utf-8");
            String phone = this.getRequest().getParameter("phone");
            String cntType = this.getRequest().getParameter("cntType");
            String inputTime=this.getRequest().getParameter("inputTime");
            String sign=this.getRequest().getParameter("sign");
            if ( StringUtil.isEmpty(phone)||StringUtil.isEmpty(inputTime) || StringUtil.isEmpty(sign)) {
                returnMessage.setCode(-1);
                returnMessage.setData(-1);
                returnMessage.setMessage("获取运营列表请求参数不能为空！");
            } else {
                returnMessage =payOrderServiceImpl.getOperate(phone,cntType, inputTime, sign);

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        outJson(returnMessage);
    }
    
    /**
     * 接收确认支付请求
     * @throws Exception
     */
    public void surePay() throws Exception {
        try {
            this.getRequest().setCharacterEncoding("utf-8");
            String tcId = this.getRequest().getParameter("tcId");
            String lx = this.getRequest().getParameter("lx");
            String phone = this.getRequest().getParameter("phone");
            String cntType = this.getRequest().getParameter("cntType");
            String zfType = this.getRequest().getParameter("zfType");
            String openId = this.getRequest().getParameter("openId");
            String inputTime=this.getRequest().getParameter("inputTime");
			String sign=this.getRequest().getParameter("sign");
			String channel = this.getRequest().getParameter("channel");
            if (StringUtil.isEmpty(tcId) || StringUtil.isEmpty(lx) || StringUtil.isEmpty(phone) || StringUtil.isEmpty(cntType)
            		|| StringUtil.isEmpty(zfType) || StringUtil.isEmpty(inputTime) || StringUtil.isEmpty(sign)) {
                returnMessage.setCode(-1);
                returnMessage.setData(-1);
                returnMessage.setMessage("确认支付请求参数不能为空！");
            } else {
            	if(cntType.equalsIgnoreCase("WEB")){
            		if(zfType.equalsIgnoreCase("1")){
            	        String pString =phone.replace("0", "R").replace("1", "I").replace("2", "Z").replace("3", "B")
            	                            .replace("4", "H").replace("5", "G").replace("6", "E")
            	                            .replace("7", "C").replace("8", "F").replace("9", "O");
            	        String mid = pString.substring(0, 5) + "KAF" + pString.substring(5, pString.length());
            		    returnMessage =wabpPayOrderServiceImpl.addPayOrder("WEB",cntType, tcId, lx, phone, mid,
                                        OrderUtil.GetOrderNumber(""), inputTime, sign, openId, channel);
            		}else if(zfType.equalsIgnoreCase("2")){
                    	returnMessage =payOrderServiceImpl.addWxPayOrder(cntType,lx,tcId, openId, phone, null, null, OrderUtil.GetOrderNumber(""),this.getRequest().getRemoteAddr(),inputTime, sign, channel);
            		}
            	}else if(cntType.equalsIgnoreCase("WAP")){
            		if(zfType.equalsIgnoreCase("1")){
            	        String pString =phone.replace("0", "R").replace("1", "I").replace("2", "Z").replace("3", "B")
            	                            .replace("4", "H").replace("5", "G").replace("6", "E")
            	                            .replace("7", "C").replace("8", "F").replace("9", "O");
            	        String mid = pString.substring(0, 5) + "KAF" + pString.substring(5, pString.length());
            		    returnMessage =wabpPayOrderServiceImpl.addPayOrder("WAP",cntType, tcId, lx, phone, mid,
                            OrderUtil.GetOrderNumber(""), inputTime, sign, openId, channel );
            		}else if(zfType.equalsIgnoreCase("2")){
            			if(StringUtil.isEmpty(openId)){
                    		returnMessage.setCode(-1);
                            returnMessage.setData(-1);
                            returnMessage.setMessage("确认支付请求参数不能为空！");
                    	}else{
                    	    returnMessage =payOrderServiceImpl.addWxPayOrder(cntType,lx,tcId, openId, phone, null, null, OrderUtil.GetOrderNumber(""),this.getRequest().getRemoteAddr(),inputTime, sign, channel);
                    	}
            		}
            	}
                
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        outJson(returnMessage);
    }
}
