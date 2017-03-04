package com.pay.activemq;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

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
import org.springframework.beans.factory.annotation.Autowired;

import com.pay.constants.SysConstants;
import com.pay.dao.TbPvuvDAO;
import com.pay.model.TbPvuv;
import com.pay.service.WaxCaiYunFlowSend;
/**
 * 
 * ClassName: MessageReceiver 
 * @Description: TODO
 * @author wujintao
 * @date 2016-12-8
 */
public class MessageReceiver implements MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(MessageReceiver.class);
    @Autowired
    MessageSender messageSender;
    @Autowired
    TbPvuvDAO tbPvuvDAO;
    
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            // 创建HttpClientBuilder
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            // HttpClient
            CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
            HttpPost httpPost = new HttpPost(SysConstants.FLOW_URL);
            try {
                String jsonStr = textMessage.getText();
                System.out.println("接收到消息: " + jsonStr);
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
                        }else {
                            JSONArray members=jsonObject.getJSONArray("members");
                            JSONObject member = members.getJSONObject(0);
                            ret[0] = member.getString("resultCode");
                            System.out.println(ret[0]);
                            ret[1] = member.getString("resultMsg");
                            System.out.println(ret[1]);
                            ret[2] = jsonObject.getString("ordNum");
                            System.out.println(ret[2]);
                        }
                    } else {
                        messageSender.send(jsonStr);
                    }
                    // 释放资源
                    closeableHttpClient.close();
                } catch (Exception e) {
                    messageSender.send(jsonStr);
                    e.printStackTrace();
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        
        if (message instanceof ObjectMessage) {
            //pvuv统计逻辑
            ObjectMessage objectMessage = (ObjectMessage) message;
            TbPvuv tbPvuv;
            try {
                tbPvuv = (TbPvuv) objectMessage.getObject();
                tbPvuvDAO.save(tbPvuv);
            } catch (JMSException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
