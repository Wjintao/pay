package com.pay.service;

import java.util.Map;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pay.constants.SysConstants;
import com.pay.util.DSAUtil;

public class WabpUnPurchase {

    private static final Logger logger = LoggerFactory.getLogger(WabpUnPurchase.class);

    private String APTransactionID;
    private String FeeType;
    private String ChannelId;
    private String APContentId;
    private String APUserId;
    private String Actiontime;
    private String method;
    private String signMethod;
    private String sign;


    /**
     * 
     * @Description: TODO 退订请求
     * @param @return
     * @return String
     * @throws Exception
     * @throws
     * @author wujintao
     * @date 2016-9-21
     */
    public String unsubscribe() throws Exception {
        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // HttpClient
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost(SysConstants.UN_PURCHASE);
        String xml = getPackage4Unsubscribe();
        StringEntity entity;
        String ResultCode = null;
        try {
            entity = new StringEntity(xml, "GBK");
            httpPost.setEntity(entity);
            HttpResponse httpResponse;
            // post请求
            httpResponse = closeableHttpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                // 打印响应内容
                String result = EntityUtils.toString(httpEntity, "GBK");
                logger.info(result);
                // 过滤
                ResultCode = Jsoup.parse(result).select("ResultCode").html();

            }
            // 释放资源
            closeableHttpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultCode;
    }

    private String getPackage4Unsubscribe() throws Exception {
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        treeMap.put("APTransactionID", this.APTransactionID);
        treeMap.put("FeeType", this.FeeType);
        treeMap.put("ChannelId", this.ChannelId);
        treeMap.put("APContentId", this.APContentId);
        treeMap.put("APUserId", this.APUserId);
        treeMap.put("Actiontime", this.Actiontime);
        treeMap.put("method", this.method);
        //treeMap.put("signMethod", this.signMethod);
        // treeMap.put("sign", this.sign);
        StringBuilder sb = new StringBuilder();
        for (String key : treeMap.keySet()) {
            sb.append(key).append("=").append(treeMap.get(key)).append("&");
        }
        sign = DSAUtil.buildSign(SysConstants.AP_PRIVATEKEY, treeMap);
        treeMap.put("sign", sign);
        treeMap.put("signMethod", this.signMethod);
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"GBK\"?>\n" + "<UnSubscribeServiceReq>\n");
        for (Map.Entry<String, String> entry : treeMap.entrySet()) {
            xml.append("<" + entry.getKey() + ">").append(entry.getValue())
                    .append("</" + entry.getKey() + ">\n");

        }
        xml.append("</UnSubscribeServiceReq>");
        System.out.println(xml.toString().replace("null", ""));
        return xml.toString();
    }


    public String getAPTransactionID() {
        return APTransactionID;
    }

    public void setAPTransactionID(String aPTransactionID) {
        APTransactionID = aPTransactionID;
    }

    public String getFeeType() {
        return FeeType;
    }

    public void setFeeType(String feeType) {
        FeeType = feeType;
    }

    public String getChannelId() {
        return ChannelId;
    }

    public void setChannelId(String channelId) {
        ChannelId = channelId;
    }

    public String getAPContentId() {
        return APContentId;
    }

    public void setAPContentId(String aPContentId) {
        APContentId = aPContentId;
    }

    public String getAPUserId() {
        return APUserId;
    }

    public void setAPUserId(String aPUserId) {
        APUserId = aPUserId;
    }

    public String getActiontime() {
        return Actiontime;
    }

    public void setActiontime(String actiontime) {
        Actiontime = actiontime;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSignMethod() {
        return signMethod;
    }

    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
