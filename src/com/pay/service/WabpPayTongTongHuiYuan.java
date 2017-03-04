package com.pay.service;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import TestDes.NetDes;

import com.pay.constants.SysConstants;
import com.pay.util.DES;

/**
 * 
 * ClassName: WabpPayTongTongHuiYuan
 * 
 * @Description: TODO
 * @author wujintao
 * @date 2017-2-9
 */
public class WabpPayTongTongHuiYuan {
    private static final Logger logger = LoggerFactory.getLogger(WabpPayTongTongHuiYuan.class);

    /**
     * 
     * @Description: TODO
     * @param @param PhoneNo
     * @param @param Opration
     * @param @return
     * @param @throws JsonGenerationException
     * @param @throws JsonMappingException
     * @param @throws IOException
     * @return String[]
     * @throws
     * @author wujintao
     * @date 2017-2-9
     */
    public String[] submitJsonPost(String PhoneNo, String Opration) throws JsonGenerationException,
            JsonMappingException, IOException {
        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // HttpClient
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        // HttpPost httpPost = new HttpPost(SysConstants.TONG_SYCHRO);
        String strXiChe = getPackage(PhoneNo, Opration);
        HttpGet httpGet = new HttpGet(SysConstants.TONG_SYCHRO + strXiChe);
        // String jsonStr=JSONObject.fromObject(strXiChe).toString();
        logger.info(strXiChe);
        String[] ret = new String[3];
        try {
            HttpResponse httpResponse;
            // post请求
            httpResponse = closeableHttpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                // 打印响应内容
                String resultSTR = EntityUtils.toString(httpEntity, "UTF-8");
                // String result= new String(Base64.decodeBase64(resultSTR),"UTF-8");
                logger.info(resultSTR);
                // String result = (new
                // BASE64Decoder()).decodeBuffer(EntityUtils.toString(httpEntity,
                // "UTF-8")).toString();
                // logger.info(result);
                // 过滤
                JSONObject jsonObject = JSONObject.fromObject(resultSTR);
                ret[0] = jsonObject.getString("Code");
                System.out.println(ret[0]);
                ret[1] = jsonObject.getString("Message");
                System.out.println(ret[1]);
            } else {
                for (int i = 0; i < 5; i++) {
                    httpResponse = closeableHttpClient.execute(httpGet);
                    HttpEntity httpEntityReply = httpResponse.getEntity();
                    if (httpEntityReply != null) {
                        // 打印响应内容
                        String resultSTRReply = EntityUtils.toString(httpEntityReply, "UTF-8");
                        // String resultReply= new
                        // String(Base64.decodeBase64(resultSTRReply),"UTF-8");
                        logger.info(resultSTRReply);
                        // String result = (new
                        // BASE64Decoder()).decodeBuffer(EntityUtils.toString(httpEntity,
                        // "UTF-8")).toString();
                        // logger.info(result);
                        // 过滤
                        JSONObject jsonObjectReply = JSONObject.fromObject(resultSTRReply);
                        ret[0] = jsonObjectReply.getString("Code");
                        System.out.println(ret[0]);
                        ret[1] = jsonObjectReply.getString("Message");
                        System.out.println(ret[1]);
                        logger.info("+++++" + PhoneNo + "请求失败重试后成功");
                        break;
                    } else {
                        logger.info("+++++" + PhoneNo + "请求失败重试失败继续重试");
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
        // return prepay_id;
        return ret;
    }

    /**
     * 
     * @Description: TODO
     * @param @param PhoneNo
     * @param @param Opration
     * @param @return
     * @return String
     * @throws
     * @author wujintao
     * @date 2017-2-9
     */
    public String getPackage(String PhoneNo, String Opration) {
        String timeSpan = DES.formatDate(new Date(), "yyyyMMddHHmmss");
        StringBuilder signStringBuilder = new StringBuilder();
        String value =
                NetDes.MD5Sign(
                        signStringBuilder.append(PhoneNo).append("SyncUserInfoMD5")
                                .append(timeSpan).append(Opration).append("hecaiyun").toString(),
                        SysConstants.KEY_TONG);
        logger.info(signStringBuilder.toString());
        logger.info(value);
        StringBuilder sb = new StringBuilder();
        sb.append("PhoneNo=").append(PhoneNo).append("&Method=SyncUserInfo&Format=MD5&TimeSpan=")
                .append(timeSpan).append("&Opration=").append(Opration)
                .append("&AuthorizeAccount=hecaiyun&Sign=").append(value);
        return sb.toString();
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

    public static void main(String[] args) throws JsonGenerationException, JsonMappingException,
            IOException {
        WabpPayTongTongHuiYuan wabpPayXiCheHuiYuan = new WabpPayTongTongHuiYuan();
        wabpPayXiCheHuiYuan.submitJsonPost("13825192407", "0");
    }
}
