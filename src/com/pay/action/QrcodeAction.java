package com.pay.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom2.JDOMException;
import org.springframework.web.context.ContextLoader;

import com.pay.constants.SysConstants;
import com.pay.dao.TbPayDAO;
import com.pay.dao.TbTcdetailDAO;
import com.pay.dao.TbWxpayDAO;
import com.pay.model.MdlPay;
import com.pay.model.TbPay;
import com.pay.model.TbTcdetail;
import com.pay.model.TbWxpay;
import com.pay.service.IPayOrderService;
import com.pay.service.WXNative;
import com.pay.service.WXOrderQuery;
import com.pay.service.WXPay;
import com.pay.service.WXPrepay;
import com.pay.util.MD5Util;
import com.pay.util.OrderUtil;
import com.pay.util.StringUtil;
import com.pay.util.XMLUtil;

public class QrcodeAction extends BaseJsonAction{
    
    @Inject
    private IPayOrderService payOrderServiceImpl;
    
    /**
     * 该接口用于：扫码回调接口
     * @param request
     * @param response
     * @throws IOException
     */
    public void qrcodeCallback() throws IOException {
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
        
        TbWxpayDAO tbWxpayDAO =(TbWxpayDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TbWxpayDAO");
        List<TbWxpay> list = tbWxpayDAO.findByProductId(map.get("product_id"));
        TbWxpay tbWxpay = list.get(0);
        
        TbTcdetailDAO tbTcdetailDAO =(TbTcdetailDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TbTcdetailDAO");
        List<TbTcdetail> alist = tbTcdetailDAO.findByObjId(tbWxpay.getTcdetailRelId());
        TbTcdetail tbTcdetail = alist.get(0);
        
        MdlPay pay = new MdlPay();
        pay.setAppId(map.get("appid"));
        pay.setPartnerId(SysConstants.PARTNER_ID);
        pay.setPartnerKey(SysConstants.PARTNER_KEY);
        
        System.out.println("pay对象初始化：" + pay);
        String spbill_create_ip = "117.136.240.23";
        WXNative wxNative = new WXNative();
        wxNative.setAppid(map.get("appid"));
        wxNative.setOpenid(map.get("openid"));
        wxNative.setMch_id(map.get("mch_id"));
        wxNative.setProduct_id(map.get("product_id"));
        wxNative.setBody(SysConstants.DESC_BODY);
        wxNative.setPartnerKey(pay.getPartnerKey());
        wxNative.setNotify_url(SysConstants.NOTIFY_WXURL);
        wxNative.setOut_trade_no(OrderUtil.GetOrderNumber(""));
        wxNative.setSpbill_create_ip(spbill_create_ip);
        wxNative.setTotal_fee(tbTcdetail.getTcpriceNow());
        wxNative.setTrade_type("NATIVE");
        wxNative.setDetail(getGoodsDetail(tbTcdetail));
        // 获取预支付订单号
        String[] rs = wxNative.submitXmlGetPrepayId();
        String prepayid = rs[0];
        String nstr = rs[1];
        System.out.println("二维码响应请求获取的预支付订单是：" + prepayid);
        if (prepayid != null && prepayid.length() > 10) {
            // 生成微信支付参数，此处拼接为完整的JSON格式，符合支付调起传入格式
            //String jsParam = WXPay.createPackageValue(pay.getAppId(), pay.getPartnerKey(), prepayid);
            //System.out.println("生成的微信调起JS参数为：" + jsParam);
            try {
                if (tbWxpayDAO.update(wxNative.getOut_trade_no(),map.get("openid"),prepayid,map.get("product_id"), tbWxpay.getPhone(),"NATIVE")) {
                	String ret = setXML("SUCCESS", "OK",map.get("appid"),map.get("mch_id"),nstr,prepayid,"SUCCESS","");
                    //out.print(new ByteArrayInputStream(ret.getBytes(Charset.forName("UTF-8"))));
                    out.print(ret);
                } else {
                	String ret = setXML("SUCCESS", "OK",map.get("appid"),map.get("mch_id"),nstr,prepayid,"FAIL","订单记录失败");
                    //out.print(new ByteArrayInputStream(ret.getBytes(Charset.forName("UTF-8"))));
                	out.print(ret);
                }
            } catch (Exception e) {
                e.printStackTrace();
                String ret = setXML("SUCCESS", "OK",map.get("appid"),map.get("mch_id"),nstr,prepayid,"FAIL","系统错误");
                //out.print(new ByteArrayInputStream(ret.getBytes(Charset.forName("UTF-8"))));
                out.print(ret);
            }
        }
    }

    private String setXML(String return_code, String return_msg,String appid,String mch_id,String nonceStr,String prepay_id,String result_code,String err_code_des) {
    	TreeMap<String, String> treeMap = new TreeMap<String, String>();
		treeMap.put("return_code", return_code);
		treeMap.put("return_msg", return_msg);
		treeMap.put("appid", appid);
		treeMap.put("mch_id", mch_id);
		treeMap.put("nonce_str", nonceStr);
		treeMap.put("prepay_id", prepay_id);
		treeMap.put("result_code", result_code);
		if(result_code!=null && !result_code.equalsIgnoreCase("SUCCESS") && err_code_des!=null && !err_code_des.equalsIgnoreCase("")){
			treeMap.put("err_code_des", err_code_des);
		}
		StringBuilder sb = new StringBuilder();
		for (String key : treeMap.keySet()) {
			sb.append(key).append("=").append(treeMap.get(key)).append("&");
		}
		sb.append("key=" + SysConstants.PARTNER_KEY);
		String sign = MD5Util.MD5Encode(sb.toString(), "utf-8").toUpperCase();
		treeMap.put("sign", sign);
		StringBuilder xml = new StringBuilder();
		xml.append("<xml>");
		for (Map.Entry<String, String> entry : treeMap.entrySet()) {
			//if ("sign".equals(entry.getKey())) {
				xml.append("<" + entry.getKey() + "><![CDATA[").append(entry.getValue()).append("]]></" + entry.getKey() + ">");
			//} else {
			//	xml.append("<" + entry.getKey() + ">").append(entry.getValue()).append("</" + entry.getKey() + ">\n");
			//}
		}
		xml.append("</xml>");
		System.out.println(xml.toString());
		return xml.toString();
    }

    public String getGoodsDetail(TbTcdetail tbTcdetail){
    	String ret="{\"goods_detail\": [{\"goods_id\": \""+tbTcdetail.getObjId()+"\",\"goods_name\": \""+tbTcdetail.getTcname()+"\"," +
    			"\"goods_num\": 1,\"price\": "+tbTcdetail.getTcpriceNow()+"}]}";
    	return ret;
    }
}
