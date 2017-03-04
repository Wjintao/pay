package com.pay.service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.pay.constants.SysConstants;
import com.pay.model.Parms;
import com.pay.model.WabpPay4XiChe;
import com.pay.service.impl.WabpPayOrderServiceImpl;
import com.pay.util.DES;
import com.pay.util.MD5;


public class WabpPayXiCheHuiYuan {
    private static final Logger logger = LoggerFactory.getLogger(WabpPayXiCheHuiYuan.class);

    /**
     * 
     * @Description: TODO
     * @param @return   
     * @return String[]  
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonGenerationException 
     * @throws
     * @author wujintao
     * @date 2016-9-5
     */
    public String[] submitJsonPost(String PhoneNo,String AuthorizeAccountNo,String Opration) throws JsonGenerationException, JsonMappingException, IOException {
        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // HttpClient
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost(SysConstants.XICHE_SYCHRO);
        WabpPay4XiChe strXiChe = getPackage(PhoneNo,AuthorizeAccountNo,Opration);
        //String jsonStr=JSONObject.fromObject(strXiChe).toString();
        String jsonStr= new ObjectMapper().writeValueAsString(strXiChe);
        logger.info(jsonStr);
        StringEntity entity;
        String[] ret=new String[3];
        try {
            entity = new StringEntity((new BASE64Encoder()).encodeBuffer(jsonStr.getBytes()), "utf-8");
            httpPost.setEntity(entity);
            HttpResponse httpResponse;
            // post请求
            httpResponse = closeableHttpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                // 打印响应内容
                String resultSTR=EntityUtils.toString(httpEntity, "UTF-8");
                String result= new String(Base64.decodeBase64(resultSTR),"UTF-8");
                logger.info(result);
                //String result =  (new BASE64Decoder()).decodeBuffer(EntityUtils.toString(httpEntity, "UTF-8")).toString();
                //logger.info(result);
                // 过滤
                JSONObject jsonObject = JSONObject.fromObject(result);
                ret[0]=jsonObject.getString("Success");
                System.out.println(ret[0]);
                ret[1]=jsonObject.getString("Error");
                System.out.println(ret[1]);
                ret[2]=jsonObject.getString("Result");
                System.out.println(ret[2]);
            } else {
                for (int i = 0; i < 5; i++) {
                    httpResponse = closeableHttpClient.execute(httpPost);
                    HttpEntity httpEntityReply = httpResponse.getEntity();
                    if (httpEntityReply != null) {
                     // 打印响应内容
                        String resultSTRReply=EntityUtils.toString(httpEntityReply, "UTF-8");
                        String resultReply= new String(Base64.decodeBase64(resultSTRReply),"UTF-8");
                        logger.info(resultReply);
                        //String result =  (new BASE64Decoder()).decodeBuffer(EntityUtils.toString(httpEntity, "UTF-8")).toString();
                        //logger.info(result);
                        // 过滤
                        JSONObject jsonObjectReply = JSONObject.fromObject(resultReply);
                        ret[0]=jsonObjectReply.getString("Success");
                        System.out.println(ret[0]);
                        ret[1]=jsonObjectReply.getString("Error");
                        System.out.println(ret[1]);
                        ret[2]=jsonObjectReply.getString("Result");
                        System.out.println(ret[2]);
                        logger.info("+++++"+PhoneNo+"请求失败重试后成功");
                        break;
                    } else {
                        logger.info("+++++"+PhoneNo+"请求失败重试失败继续重试");
                        try {
                            Thread.sleep(2000);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        continue;
                    }
                }
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
     * 
     * @Description: TODO 
     * @param @param PhoneNo
     * @param @param AuthorizeAccountNo
     * @param @param Opration
     * @param @return   
     * @return WabpPay4XiChe  
     * @throws
     * @author wujintao
     * @date 2016-9-6
     */
    public WabpPay4XiChe getPackage(String PhoneNo,String AuthorizeAccountNo,String Opration) {
        String timeSpan=DES.formatDate(new Date(), "yyyyMMddHHmmss");
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        treeMap.put("AuthorizeAccountNo", AuthorizeAccountNo);
        treeMap.put("Opration", Opration);
        treeMap.put("PhoneNo", PhoneNo);
        treeMap.put("key", SysConstants.SIGN_KEY);
        //treeMap.put("TimeSpan",timeSpan);
        StringBuilder sb = new StringBuilder();
        for (String key : treeMap.keySet()) {
            sb.append(key).append("=").append(treeMap.get(key)).append("&");
        }
        sb.append("TimeSpan=" + timeSpan);
        logger.info(sb.toString());
        //String value=MD5.GetMD5String(sb.toString());
        String value=getEncrypt(sb.toString()).toUpperCase();
        logger.info(value);
        Parms parms=new Parms();
        parms.setAuthorizeAccountNo(AuthorizeAccountNo);
        parms.setOpration(Opration);
        parms.setPhoneNo(PhoneNo);
        parms.setSign(value);
        WabpPay4XiChe wabpPay4XiChe=new WabpPay4XiChe();
        wabpPay4XiChe.setFormat("json");
        wabpPay4XiChe.setMethod(SysConstants.METHOD);
        wabpPay4XiChe.setParms(parms);
        wabpPay4XiChe.setTimeSpan(timeSpan);
        wabpPay4XiChe.setToken(SysConstants.REQ_TOKEN);
        wabpPay4XiChe.setTypeName(SysConstants.TYPE_NAME);
        return wabpPay4XiChe;
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
    
      public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
          WabpPayXiCheHuiYuan wabpPayXiCheHuiYuan=new WabpPayXiCheHuiYuan();
          wabpPayXiCheHuiYuan.submitJsonPost("13825192407", "hecaiyun10", "0");
    }
}
