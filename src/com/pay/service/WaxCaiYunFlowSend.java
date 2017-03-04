package com.pay.service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.pay.activemq.MessageSender;
import com.pay.constants.SysConstants;
import com.pay.model.HeCaiYun4Flow;
import com.pay.model.Members;
import com.pay.model.Parms;
import com.pay.model.WabpPay4XiChe;
import com.pay.service.impl.WabpPayOrderServiceImpl;
import com.pay.util.DES;
import com.pay.util.MD5;
import com.pay.util.MD5Util;
import com.pay.util.OrderUtil;
import com.pay.util.StringUtil;
import com.pay.util.UtilFunc;
import com.sun.org.apache.bcel.internal.generic.NEW;


public class WaxCaiYunFlowSend {
    private static final Logger logger = LoggerFactory.getLogger(WaxCaiYunFlowSend.class);
    @Autowired
    MessageSender messageSender;
    /**
     * 
     * @Description: TODO 请求流量赠送接口
     * @param @param phoneNo
     * @param @param flow
     * @param @return
     * @param @throws JsonGenerationException
     * @param @throws JsonMappingException
     * @param @throws IOException   
     * @return String[]  
     * @throws
     * @author wujintao
     * @date 2016-10-13
     */
    public String[] submitJsonPost(String orderId,String phoneNo, String flow){
        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // HttpClient
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost(SysConstants.FLOW_URL);
        HeCaiYun4Flow strXiChe = getPackage(orderId,phoneNo, flow);
        String jsonStr=JSONObject.fromObject(strXiChe).toString();
        //String jsonStr = new ObjectMapper().writeValueAsString(strXiChe);
        logger.info(jsonStr);
        StringEntity entity;
        String[] ret = new String[3];
        try {
            entity = new StringEntity(jsonStr,"utf-8");
            httpPost.setEntity(entity);
            HttpResponse httpResponse;
            // post请求
            httpResponse = closeableHttpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                // 打印响应内容
                String resultSTR = EntityUtils.toString(httpEntity, "UTF-8");
                //String result = new String(Base64.decodeBase64(resultSTR), "UTF-8");
                logger.info(resultSTR);
                // String result = (new
                // BASE64Decoder()).decodeBuffer(EntityUtils.toString(httpEntity,
                // "UTF-8")).toString();
                // logger.info(result);
                // 过滤
                JSONObject jsonObject = JSONObject.fromObject(resultSTR);
                if ("-99".equalsIgnoreCase(jsonObject.getString("code"))) {
                    messageSender.send(jsonStr);
                    return ret;
                }else {
                    JSONArray members=jsonObject.getJSONArray("members");
                    JSONObject member = members.getJSONObject(0);
                    ret[0] = member.getString("resultCode");
                    System.out.println(ret[0]);
                    ret[1] = member.getString("resultMsg");
                    System.out.println(ret[1]);
                    ret[2] = jsonObject.getString("ordNum");
                    System.out.println(ret[2]);
                    return ret;
                }
            } else {
                /*for (int i = 0; i < 5; i++) {
                    httpResponse = closeableHttpClient.execute(httpPost);
                    HttpEntity httpEntityReply = httpResponse.getEntity();
                        if (httpEntityReply != null) {
                         // 打印响应内容
                            String resultSTRReply = EntityUtils.toString(httpEntityReply, "UTF-8");
                            logger.info(resultSTRReply);
                            JSONObject jsonObject = JSONObject.fromObject(resultSTRReply);
                            JSONArray members=jsonObject.getJSONArray("members");
                            JSONObject member = members.getJSONObject(0);
                            ret[0] = member.getString("resultCode");
                            System.out.println(ret[0]);
                            ret[1] = member.getString("resultMsg");
                            System.out.println(ret[1]);
                            ret[2] = jsonObject.getString("ordNum");
                            System.out.println(ret[2]);
                            logger.info(orderId+"+++++"+phoneNo+"请求失败重试后成功");
                            break;
                        } else {
                            logger.info(orderId+"+++++"+phoneNo+"请求失败重试失败继续重试");
                            try {
                                Thread.sleep(2000);
                            } catch (Exception e) {
                                // TODO: handle exception
                        }
                        continue;
                    }
                }*/
                messageSender.send(jsonStr);
            }
            // 释放资源
            closeableHttpClient.close();
            return ret;
        } catch (Exception e) {
            messageSender.send(jsonStr);
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 
     * @Description: TODO 封装请求参数
     * @param @param phoneNo
     * @param @param flow
     * @param @return   
     * @return HeCaiYun4Flow  
     * @throws
     * @author wujintao
     * @date 2016-10-13
     */
    public HeCaiYun4Flow getPackage(String orderId,String phoneNo, String flow) {
        String requestId=orderId;
        String keyMd5=MD5.GetMD5String(SysConstants.FLOW_KEY);
        String sigMd5=MD5.GetMD5String(requestId+keyMd5.substring(keyMd5.length()-32,keyMd5.length()));
        Members members = new Members();
        //members.setEffTime(printDate(nextMonthFirstDate()));
        if ((new Date()).getDate()>25) {
            members.setEffType("3");
            members.setEffTime(printDate(nextMonthFirstDate()));
        } else {
            members.setEffType("2");
            members.setEffTime("");
        }
        members.setMobile(phoneNo);
        members.setUserName(phoneNo);
        members.setPrdCode(UtilFunc.getProp(flow));
        List<Members> membersList = new ArrayList<Members>();
        membersList.add(members);
        HeCaiYun4Flow heCaiYun4Flow=new HeCaiYun4Flow();
        heCaiYun4Flow.setRequestID(requestId);
        heCaiYun4Flow.setChannelID(SysConstants.CHANEL_ID);
        heCaiYun4Flow.setSig(sigMd5.substring(sigMd5.length()-32,sigMd5.length()));
        heCaiYun4Flow.setMembers(membersList);
        return heCaiYun4Flow;
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
