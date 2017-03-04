package com.pay.service;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jdom2.JDOMException;
import org.jsoup.Jsoup;

import com.pay.constants.SysConstants;
import com.pay.model.TbTcdetail;
import com.pay.util.DSAUtil;
import com.pay.util.MD5Util;
import com.pay.util.OrderUtil;
import com.pay.util.XMLUtil;

/**
 * 
 * ClassName: WabpPay 
 * @Description: TODO WabpPay
 * @author wujintao
 * @date 2016-8-3
 */

public class WabpPay {

	private String apco;
	private String aptid;
	private String aptrid;
	private String ch;
	private String ex;
	private String sin;
	private String bu;
	private String xid;
	private String mid;
	private String sign;


	/**
	 * 请求wabp订购页接口
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public String getWabpUrl(String flag) throws Exception {
		/*HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		HttpGet httpGet = new HttpGet(SysConstants.WABPP_PAY);*/
	    
        String getStr = getPackage();
	    if ("WEB".equalsIgnoreCase(flag)) {

	        return SysConstants.WABPP_PAY_WEB +getStr;
        } else {

            return SysConstants.WABPP_PAY_WAP +getStr;
        }

	}


	public String getPackage() throws Exception {
		TreeMap<String, String> treeMap = new TreeMap<String, String>();
		treeMap.put("apco", this.apco);
		treeMap.put("aptid", this.aptid);
		treeMap.put("aptrid", this.aptrid);
		treeMap.put("ch", this.ch);
		treeMap.put("ex", this.ex);
		treeMap.put("sin", this.sin);
		treeMap.put("bu", this.bu);
		treeMap.put("xid", this.xid);
		treeMap.put("mid", this.mid);
		StringBuilder sb = new StringBuilder();
		for (String key : treeMap.keySet()) {
			sb.append(key).append("=").append(treeMap.get(key)).append("&");
		}
		/*String signstr=ChangeCharsetUtil.toGBK(sb.toString());
		byte[] bs = sb.toString().substring(0, sb.length()-1).getBytes("GBK");*/
		// 构建密钥  
		sign=DSAUtil.buildSign(SysConstants.AP_PRIVATEKEY, treeMap);
		sb.append("sign="+sign);
		return sb.toString();
	}


    public String getApco() {
        return apco;
    }


    public void setApco(String apco) {
        this.apco = apco;
    }


    public String getAptid() {
        return aptid;
    }


    public void setAptid(String aptid) {
        this.aptid = aptid;
    }


    public String getAptrid() {
        return aptrid;
    }


    public void setAptrid(String aptrid) {
        this.aptrid = aptrid;
    }


    public String getCh() {
        return ch;
    }


    public void setCh(String ch) {
        this.ch = ch;
    }


    public String getEx() {
        return ex;
    }


    public void setEx(String ex) {
        this.ex = ex;
    }


    public String getSin() {
        return sin;
    }


    public void setSin(String sin) {
        this.sin = sin;
    }


    public String getBu() {
        return bu;
    }


    public void setBu(String bu) {
        this.bu = bu;
    }


    public String getXid() {
        return xid;
    }


    public void setXid(String xid) {
        this.xid = xid;
    }


    public String getMid() {
        return mid;
    }


    public void setMid(String mid) {
        this.mid = mid;
    }


    public String getSign() {
        return sign;
    }


    public void setSign(String sign) {
        this.sign = sign;
    }

	
}
