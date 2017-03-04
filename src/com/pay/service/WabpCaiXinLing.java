package com.pay.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pay.constants.SysConstants;
import com.pay.model.HeCaiYun4Flow;
import com.pay.model.Members;
import com.pay.util.MD5;
import com.pay.util.UtilFunc;

public class WabpCaiXinLing {

    private static final Logger logger = LoggerFactory.getLogger(WabpCaiXinLing.class);

    /**
     * 
     * @Description: TODO 请求CXL订单确认接口
     * @param @param result
     * @param @return   
     * @return String[]  
     * @throws
     * @author wujintao
     * @date 2016-11-18
     */
    public String submitXmlQR(String result){
        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // HttpClient
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost(SysConstants.CXL_QR);
        //String jsonStr = new ObjectMapper().writeValueAsString(strXiChe);
        logger.info("==CXL_QR=="+result+"==CXL_QR==");
        StringEntity entity;
        String resultQR = null;
        try {
            entity = new StringEntity(result,"UTF-8");
            httpPost.setEntity(entity);
            HttpResponse httpResponse;
            // post请求
            httpResponse = closeableHttpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                // 打印响应内容
                resultQR = EntityUtils.toString(httpEntity, "UTF-8");
                logger.info(resultQR);
            }
            // 释放资源
            closeableHttpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return prepay_id;
        return resultQR;
    }

    /**
     * 
     * @Description: TODO 请求CXL订单同步接口
     * @param @param result
     * @param @return   
     * @return String  
     * @throws
     * @author wujintao
     * @date 2016-11-19
     */
    public String submitXmlTB(String result){
        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // HttpClient
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost(SysConstants.CXL_TB);
        //String jsonStr = new ObjectMapper().writeValueAsString(strXiChe);
        logger.info("==CXL_TB=="+result+"==CXL_TB==");
        StringEntity entity;
        String resultQR = null;
        try {
            entity = new StringEntity(result,"UTF-8");
            httpPost.setEntity(entity);
            HttpResponse httpResponse;
            // post请求
            httpResponse = closeableHttpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                // 打印响应内容
                resultQR = EntityUtils.toString(httpEntity, "UTF-8");
                //logger.info(resultQR);
            }
            // 释放资源
            closeableHttpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // return prepay_id;
        return resultQR;
    }
    
    
    /**
     * 
     * @Description: TODO MD5加密
     * @param @param input
     * @param @return
     * @return String
     * @throws
     * @author wujintao
     * @date 2016-9-6
     */
    public static String getEncrypt(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] bb = md.digest();
            String hs = "", tmp = "";
            for (byte e : bb) {
                tmp = (Integer.toHexString(e & 0xFF));
                hs = tmp.length() == 1 ? hs + "0" + tmp : hs + tmp;
            }
            return hs;
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }


    public static Date nextMonthFirstDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }

    public static String printDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }
       
}
