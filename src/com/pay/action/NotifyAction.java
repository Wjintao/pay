package com.pay.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Map;

import javax.inject.Inject;

import org.jdom2.JDOMException;

import com.pay.constants.SysConstants;
import com.pay.service.IPayOrderService;
import com.pay.service.WXOrderQuery;
import com.pay.util.XMLUtil;

public class NotifyAction extends BaseJsonAction{
    
    @Inject
    private IPayOrderService payOrderServiceImpl;
    /**
     * 注意：：：该接口用于第一版空间扩容微信支付微信回调接口，现已用notifyWxCallback
     * @param request
     * @param response
     * @throws IOException
     */
    public void notifyCallback() throws IOException {
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
            System.out.println("支付失败"); // 支付失败
        } else {
            System.out.println("===============付款成功==============");
            // ------------------------------
            // 处理业务开始
            // ------------------------------
            // 此处处理订单状态，结合自己的订单数据完成订单状态的更新 
            
            String outTradeNo=map.get("out_trade_no");
            returnMessage=payOrderServiceImpl.updatePayOrder(outTradeNo);
            if (returnMessage.getCode()==0) {
                System.out.println("===============订单"+outTradeNo+"支付成功信息入库成功+付款成功==============");
                String noticeStr = setXML("SUCCESS", "");
                out.print(new ByteArrayInputStream(noticeStr.getBytes(Charset.forName("UTF-8"))));
            }else{
            	System.out.println("===============code:"+returnMessage.getCode()+"订单"+outTradeNo+"支付成功信息入库失败+付款成功==============");
            	String noticeStr = setXML("FAIL", "");
            	out.print(new ByteArrayInputStream(noticeStr.getBytes(Charset.forName("UTF-8"))));
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
     *  注意：：：该接口用于第一版空间扩容微信支付请求订单查询接口，判断是否成功，现已用NotifyWxAction的接口
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
