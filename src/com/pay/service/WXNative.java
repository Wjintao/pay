package com.pay.service;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jdom2.JDOMException;
import org.jsoup.Jsoup;

import com.pay.constants.SysConstants;
import com.pay.model.TbTcdetail;
import com.pay.util.MD5Util;
import com.pay.util.OrderUtil;
import com.pay.util.XMLUtil;

/**
 * 扫码支付
 * @author
 *
 */
public class WXNative {
	private String appid;
	private String mch_id;
	private String nonce_str = OrderUtil.CreateNoncestr();
	private String product_id;
	private String body;
	private String out_trade_no;
	private String total_fee;
	private String spbill_create_ip;
	private String trade_type;
	private String openid;
	private String notify_url;
	private String sign;
	private String partnerKey;
	private TbTcdetail tbTcdetail;
	private String prepay_id; //预支付订单号
	private String detail; //预支付订单号
	
	/**
	 * 生成二维码支付链接
	 * @return
	 */
	public String getCodeUrl(){
		String nonceStr=Long.toString(System.currentTimeMillis());
		String timeStamp=Long.toString(System.currentTimeMillis() / 1000);
	    String url="weixin://wxpay/bizpayurl?appid="+this.appid+"&mch_id="+
	    		this.mch_id+"&nonce_str="+nonceStr+"&product_id="+this.product_id+"&time_stamp="+timeStamp+"&sign=";
	    TreeMap<String, String> treeMap = new TreeMap<String, String>();
		treeMap.put("appid",this.appid );
		treeMap.put("mch_id",this.mch_id);
		treeMap.put("nonce_str", nonceStr);
		treeMap.put("product_id",this.product_id);
		treeMap.put("time_stamp", timeStamp);
		StringBuilder sb = new StringBuilder();
		for (String key : treeMap.keySet()) {
			sb.append(key).append("=").append(treeMap.get(key)).append("&");
		}
		sb.append("key=" + this.partnerKey);
		this.sign = MD5Util.MD5Encode(sb.toString(), "utf-8").toUpperCase();
		url=url+this.sign;
		return url;
    }

	/**
	 * 生成预支付订单
	 * 
	 * @return
	 */
	public String[] submitXmlGetPrepayId() {
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		HttpPost httpPost = new HttpPost(SysConstants.WX_UNIFIED_OREDER);
		String xml = getPackage();
		StringEntity entity;
		String[] ret=new String[2];
		try {
			entity = new StringEntity(xml, "utf-8");
			httpPost.setEntity(entity);
			HttpResponse httpResponse;
			// post请求
			httpResponse = closeableHttpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				// 打印响应内容
				String result = EntityUtils.toString(httpEntity, "UTF-8");
				System.out.println(result);
				// 过滤
				result = result.replaceAll("<![CDATA[|]]>", "");
				String prepay_id = Jsoup.parse(result).select("prepay_id").html();
				this.prepay_id = prepay_id;
				if (prepay_id != null){
					//return prepay_id;
				}
				String nonceStr=Jsoup.parse(result).select("nonce_str").html();
				ret[0]=prepay_id;
				ret[1]=nonceStr;
			}
			// 释放资源
			closeableHttpClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//return prepay_id;
		return ret;
	}

	/**
	 * 请求订单查询接口
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> reqOrderquery() {
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		HttpPost httpPost = new HttpPost(SysConstants.WX_ORDER_QUERY);
		String xml = getPackage();
		StringEntity entity;
		Map<String, String> map = null;
		try {
			entity = new StringEntity(xml, "utf-8");
			httpPost.setEntity(entity);
			HttpResponse httpResponse;
			// post请求
			httpResponse = closeableHttpClient.execute(httpPost);
			// getEntity()
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				// 打印响应内容
				String result = EntityUtils.toString(httpEntity, "UTF-8");
				// 过滤
				result = result.replaceAll("<![CDATA[|]]>", "");
				try {
					map = XMLUtil.doXMLParse(result);
				} catch (JDOMException e) {
					e.printStackTrace();
				}
			}
			// 释放资源
			closeableHttpClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public String getPackage() {
		TreeMap<String, String> treeMap = new TreeMap<String, String>();
		treeMap.put("appid", this.appid);
		treeMap.put("mch_id", this.mch_id);
		treeMap.put("nonce_str", this.nonce_str);
		treeMap.put("body", this.body);
		treeMap.put("out_trade_no", this.out_trade_no);
		treeMap.put("total_fee", this.total_fee);
		treeMap.put("spbill_create_ip", this.spbill_create_ip);
		treeMap.put("trade_type", this.trade_type);
		treeMap.put("notify_url", this.notify_url);
		treeMap.put("openid", this.openid);
		treeMap.put("product_id", this.product_id);
		treeMap.put("detail", this.getDetail());
		StringBuilder sb = new StringBuilder();
		for (String key : treeMap.keySet()) {
			sb.append(key).append("=").append(treeMap.get(key)).append("&");
		}
		sb.append("key=" + partnerKey);
		sign = MD5Util.MD5Encode(sb.toString(), "utf-8").toUpperCase();
		treeMap.put("sign", sign);
		StringBuilder xml = new StringBuilder();
		xml.append("<xml>\n");
		for (Map.Entry<String, String> entry : treeMap.entrySet()) {
			if ("body".equals(entry.getKey()) || "sign".equals(entry.getKey()) || "detail".equals(entry.getKey())) {
				xml.append("<" + entry.getKey() + "><![CDATA[").append(entry.getValue()).append("]]></" + entry.getKey() + ">\n");
			} else {
				xml.append("<" + entry.getKey() + ">").append(entry.getValue()).append("</" + entry.getKey() + ">\n");
			}
		}
		xml.append("</xml>");
		System.out.println(xml.toString());
		return xml.toString().trim();
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getPartnerKey() {
		return partnerKey;
	}

	public void setPartnerKey(String partnerKey) {
		this.partnerKey = partnerKey;
	}

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public TbTcdetail getTbTcdetail() {
        return tbTcdetail;
    }

    public void setTbTcdetail(TbTcdetail tbTcdetail) {
        this.tbTcdetail = tbTcdetail;
    }
	
	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
}
