package com.pay.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;

import javax.inject.Inject;

import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pay.constants.SysConstants;
import com.pay.model.TbTcdetail;
import com.pay.service.IPayOrderService;
import com.pay.service.WXOrderQuery;
import com.pay.util.XMLUtil;

public class NotifyWxAction extends BaseJsonAction{
    
    @Inject
    private IPayOrderService payOrderServiceImpl;
    private static final Logger logger = LoggerFactory.getLogger(NotifyWxAction.class);

    /**
     * 该接口用于：微信回调我们接口
     * @param request
     * @param response
     * @throws IOException 以下代码如有问题，请找产品经理，是他们的需求要这么写的，本人不负任何法律责任
     * @author wujt
     */
    public void notifyWxCallback() throws IOException {
        PrintWriter out = this.getResponse().getWriter();
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
        }

        // 此处调用订单查询接口验证是否交易成功
        boolean isSucc = reqOrderquery(map);
        // 支付成功，商户处理后同步返回给微信参数
        if (!isSucc) {
            logger.info("支付失败"); // 支付失败
        } else {
            logger.info("===============付款成功==============");
            // ------------------------------
            // 处理业务开始
            // ------------------------------
            // 此处处理订单状态，结合自己的订单数据完成订单状态的更新

            String outTradeNo = map.get("out_trade_no");
            String openId = map.get("openid");
            String trade_type = map.get("trade_type");
            returnMessage = payOrderServiceImpl.updateWxPayOrder(outTradeNo);
            if (returnMessage.getCode() == 0) {
                returnMessage = payOrderServiceImpl.queryPayOrder(outTradeNo);
                String objIdTc = ((TbTcdetail) returnMessage.getData()).getObjId();
                Integer flow = ((TbTcdetail) returnMessage.getData()).getSortbyPrice();
                String phone = returnMessage.getMessage();
                if (returnMessage.getCode() == 0 
                        && Arrays.asList(SysConstants.ARR_UFO_WX).contains(objIdTc)) {
                    /*returnMessage =
                            payOrderServiceImpl.isAccordFlow(outTradeNo, phone, objIdTc);
                    if (returnMessage.getCode() == 0) {*/
                        //========流量赠送代码========
                        returnMessage = payOrderServiceImpl.flowGive(outTradeNo,openId,phone,flow.toString());
                        if (returnMessage.getCode() == 0) {
                            logger.info("===============流量赠送套餐购买的订单" + outTradeNo + "支付成功信息入库成功+付款成功+流量赠送成功"+returnMessage.getMessage()+"==============");
                            String noticeStr = setXML("SUCCESS", "");
                            logger.info(noticeStr);
                            out.print(noticeStr);
                        } else {
                            logger.info("===============流量赠送套餐购买的订单" + outTradeNo + "支付成功信息入库成功+付款成功+流量赠送失败"+returnMessage.getMessage()+"==============");
                            String noticeStr = setXML("SUCCESS", "");
                            logger.info(noticeStr);
                            out.print(noticeStr);
                        }
                    /*} else {
                        logger.info("=======流量赠送套餐购买的订单，但是不符合每天首单赠送========" + outTradeNo + "支付成功信息入库成功+付款成功+流量不予赠送"+returnMessage.getMessage()+"==============");
                        String noticeStr = setXML("SUCCESS", "");
                        logger.info(noticeStr);
                        out.print(noticeStr);
                    }*/
                } else {
                    logger.info("===============" + outTradeNo + "支付成功信息入库成功+付款成功==============");
                    String noticeStr = setXML("SUCCESS", "");
                    logger.info(noticeStr);
                    out.print(noticeStr);
                }
            } else {
                logger.info("===============code:" + returnMessage.getCode() + "订单"
                        + outTradeNo + "支付成功信息入库失败+付款成功==============");
                String noticeStr = setXML("FAIL", "");
                logger.info(noticeStr);
                out.print(noticeStr);
            }
            // ------------------------------
            // 处理业务完毕
            // ------------------------------
        }
       // return "success";
    }

    public static String setXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }

    /**
     * 请求订单查询接口，判断是否成功
     * @param map
     * @param accessToken
     * @return
     */
    public static boolean reqOrderquery(Map<String, String> map) {
        WXOrderQuery orderQuery = new WXOrderQuery();
        orderQuery.setAppid(map.get("appid"));
        orderQuery.setMch_id(map.get("mch_id"));
        orderQuery.setTransaction_id(map.get("transaction_id"));
        orderQuery.setOut_trade_no(map.get("out_trade_no"));
        orderQuery.setNonce_str(map.get("nonce_str"));
        
        //此处需要密钥PartnerKey，此处直接写死，自己的业务需要从持久化中获取此密钥，否则会报签名错误
        orderQuery.setPartnerKey(SysConstants.PARTNER_KEY);
        
        Map<String, String> orderMap = orderQuery.reqOrderquery();
        //此处添加支付成功后，支付金额和实际订单金额是否等价，防止钓鱼
        if (orderMap.get("return_code") != null && orderMap.get("return_code").equalsIgnoreCase("SUCCESS")) {
            if (orderMap.get("result_code") != null && orderMap.get("result_code").equalsIgnoreCase("SUCCESS")) {
                String total_fee = map.get("total_fee");
                //order_total_fee应该取订单表Tbpay中的价格纪录吧。。
                String order_total_fee = map.get("total_fee");
                if (Integer.parseInt(order_total_fee) >= Integer.parseInt(total_fee)) {
                    return true;
                }
            }
        }
        return false;
    }
}