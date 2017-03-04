package com.pay.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.inject.Inject;

import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pay.constants.SysConstants;
import com.pay.service.IPayOrderService;
import com.pay.service.IWabpPayOrderService;
import com.pay.service.WXOrderClose;
import com.pay.util.OrderUtil;
import com.pay.util.StringUtil;
import com.pay.util.XMLUtil;

/**
 * 
 * ClassName: CloseOrderAction 
 * @Description: TODO
 * @author wujintao
 * @date 2016-9-22
 */
public class CloseOrderAction extends BaseJsonAction {
    private static final Logger logger = LoggerFactory.getLogger(CloseOrderAction.class);
    @Inject
    private IPayOrderService payOrderServiceImpl;
    @Inject
    private IWabpPayOrderService wabpPayOrderServiceImpl;
    /**
     * 该接口用于：我们调用微信关闭接口
     * @param request
     * @param response
     * @throws IOException
     */
    public void closeOrder() throws IOException {
        /*PrintWriter out = this.getResponse().getWriter();
        InputStream inStream = this.getRequest().getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        String result = new String(outSteam.toByteArray(), "utf-8");
        Map<String, String> map = null;
        try {
            map = XMLUtil.doXMLParse(result);
        } catch (JDOMException e) {
            e.printStackTrace();
        }*/
        getRequest().setCharacterEncoding("utf-8");
        String out_trade_no = getRequest().getParameter("out_trade_no");

        // 此处调用订单关闭接口验证是否交易成功

        boolean isSucc = reqOrderClose(out_trade_no);
        // 支付成功，商户处理后同步返回给微信参数
        if (!isSucc) {
            // 支付失败
            logger.info("订单关闭失败");
        } else {
            logger.info("===============订单关闭成功==============");
            // ------------------------------
            // 处理业务开始
            // ------------------------------
            // 此处处理订单状态，结合自己的订单数据完成订单状态的更新
            returnMessage=payOrderServiceImpl.closePayOrder(out_trade_no);
            if (returnMessage.getCode()==0) {
                logger.info("===============订单"+out_trade_no+"订单关闭信息入库成功==============");
            }
            // ------------------------------
            // 处理业务完毕
            // ------------------------------
        }
    }

    public static String setXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }
    /**
     * 请求订单关闭接口，判断是否成功
     * @param map
     * @param accessToken
     * @return
     */
    public static boolean reqOrderClose(String out_trade_no) {
        WXOrderClose orderClose = new WXOrderClose();
        orderClose.setAppid(SysConstants.APP_ID);
        orderClose.setMch_id(SysConstants.PARTNER_ID);
        orderClose.setOut_trade_no(out_trade_no);
        orderClose.setNonce_str(out_trade_no);
        
        //此处需要密钥PartnerKey，此处直接写死，自己的业务需要从持久化中获取此密钥，否则会报签名错误
        orderClose.setPartnerKey(SysConstants.PARTNER_KEY);
        
        Map<String, String> orderMap = orderClose.reqOrderClose();
        //此处添加支付成功后，支付金额和实际订单金额是否等价，防止钓鱼
        if (orderMap.get("return_code") != null && orderMap.get("return_code").equalsIgnoreCase("SUCCESS")) {
            logger.info(orderMap.get("err_code"));
            logger.info(orderMap.get("err_code_des"));
            logger.info(orderMap.get("result_code"));
            return true;
                }
        return false;
    }
    
    /**
     * 
     * @Description: TODO 包时长订单退订接口
     * @param    
     * @return void  
     * @throws
     * @author wujintao
     * @date 2016-9-21
     */
    public void unPurchase() {
        try {
            this.getRequest().setCharacterEncoding("utf-8");
            String aptrid = this.getRequest().getParameter("aptrid");
            String phone = this.getRequest().getParameter("phone");
            String inputTime=this.getRequest().getParameter("inputTime");
            String sign=this.getRequest().getParameter("sign");
            String openId = this.getRequest().getParameter("openId");
            if (StringUtil.isEmpty(openId) ||StringUtil.isEmpty(aptrid) || StringUtil.isEmpty(phone) || StringUtil.isEmpty(phone) || StringUtil.isEmpty(inputTime)
                    || StringUtil.isEmpty(sign) ) {
                returnMessage.setCode(-1);
                returnMessage.setData(-1);
                returnMessage.setMessage("退订请求参数不能为空！");
            } else {
                returnMessage =wabpPayOrderServiceImpl.unPurchase(aptrid,openId, phone, inputTime, sign);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        outJson(returnMessage);
    }

   /**
    * 
    * @Description: TODO 获取该用户退订订单
    * @param    
    * @return void  
    * @throws
    * @author wujintao
    * @date 2016-9-26
    */
    public void getUnPurchase() {
        try {
            this.getRequest().setCharacterEncoding("utf-8");
            String openId = this.getRequest().getParameter("openId");
            String phone = this.getRequest().getParameter("phone");
            String lx = this.getRequest().getParameter("lx");
            String inputTime=this.getRequest().getParameter("inputTime");
            String sign=this.getRequest().getParameter("sign");
            if (StringUtil.isEmpty(openId) || StringUtil.isEmpty(phone) || StringUtil.isEmpty(phone) || StringUtil.isEmpty(inputTime)
                    || StringUtil.isEmpty(sign) || StringUtil.isEmpty(lx) ) {
                returnMessage.setCode(-1);
                returnMessage.setData(-1);
                returnMessage.setMessage("退订请求参数不能为空！");
            } else {
                returnMessage =wabpPayOrderServiceImpl.getUnPurchaseOrder(openId, phone, inputTime, sign, lx);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        outJson(returnMessage);
    }
}
