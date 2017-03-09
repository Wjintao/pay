package com.pay.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pay.constants.SysConstants;
import com.pay.model.ReturnMessage;
import com.pay.model.TbTcdetail;
import com.pay.service.IPayOrderService;
import com.pay.service.IWabpCxlService;
import com.pay.service.IWabpPayOrderService;
import com.pay.util.DSAUtil;
import com.pay.util.XMLUtil;

public class Notify4WabpAction extends BaseJsonAction {

    private static final Logger logger = LoggerFactory.getLogger(Notify4WabpAction.class);
    private static final String WABP_IP = "";
    @Inject
    private IWabpPayOrderService wabpPayOrderServiceImpl;
    @Inject
    private IPayOrderService payOrderServiceImpl;
    @Inject
    private IWabpCxlService wabpCxlServiceImpl;

    /**
     * 
     * @Description: TODO 订单同步(wabp、百世恒通、流量充值、会员卡、电影券)
     * @param @throws IOException
     * @return void
     * @author wujt
     * @throws Exception
     * @date 2016-8-4
     */
    public void synchro() throws Exception {
        String addr = this.getRequest().getRemoteAddr();
        logger.info(addr);
        if (addr.contains(WABP_IP)) {
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
            String result = new String(outSteam.toByteArray(), "GBK");
            logger.info(result);
            Map<String, String> map = null;
            try {
                map = XMLUtil.doXMLParse(result);
            } catch (JDOMException e) {
                e.printStackTrace();
            }

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String RspTime = dateFormat.format(new Date());
            String ServiceAction = map.get("ServiceAction");
            String apco = map.get("APContentId");
            String aptrid = map.get("APTransactionID");
            String OrderType = map.get("OrderType");
            if (SysConstants.CXL_APCO.equalsIgnoreCase(apco)) {
                // ------------------------------
                // 彩心灵处理业务开始
                // ------------------------------
                for (int i = 0; i < 3; i++) {
                    returnMessage = wabpCxlServiceImpl.getResultTB(result);
                    if (returnMessage.getCode() == 0) {
                        logger.info("彩心灵最终回复MM订单同步接口报文：" + (String) returnMessage.getMessage());
                        out.print(returnMessage.getMessage());
                        break;
                    } else {
                        logger.info("彩心灵订单确认接口无返回" + returnMessage.getCode()
                                + returnMessage.getData() + returnMessage.getMessage());
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                        continue;
                    }
                }
            } else {
                if (DSAUtil.verifySignHcy(map)) {
                    if ("1".equals(ServiceAction)) {
                        logger.info("支付失败，不发货");
                        // ------------------------------
                        // 和彩云处理业务开始
                        // ------------------------------
                        // 此处处理订单状态，结合自己的订单数据完成订单状态的更新
                        
                    } else if ("0".equals(ServiceAction)) {
                        logger.info("===============付款成功==============");
                        // ------------------------------
                        // 处理业务开始
                        // ------------------------------
                        // 此处处理订单状态，结合自己的订单数据完成订单状态的更新
					} else if ("2".equals(ServiceAction) || "3".equals(ServiceAction)) {
                        logger.info("===============aptrid:" + aptrid
                                + "===============退订开始===============");
                        // ------------------------------
                        // 处理业务开始
                        // ------------------------------
                        // 此处处理订单状态，结合自己的订单数据完成订单状态的更新
                    } else {
                        String noticeStr =
                                setXML(aptrid, "000", "无相关信息订单", RspTime,
                                        "ServiceWebTransfer2APRsp");
                        logger.info(noticeStr);
                        out.print(noticeStr);
                    }
                } else {
                    returnMessage.setCode(2);
                    returnMessage.setData(2);
                    returnMessage.setMessage("签名失败，非法请求不发货!!!");
                    outJson(returnMessage);
                }
            }
        } else {
            returnMessage.setCode(2);
            returnMessage.setData(2);
            returnMessage.setMessage("非法请求？？？");
            outJson(returnMessage);
        }
    }

    /**
     * 
     * @Description: TODO 订单通知确认
     * @param @throws IOException
     * @return void
     * @throws
     * @author wujintao
     * @date 2016-8-4
     */
    public void confirm() throws IOException {
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
        String result = new String(outSteam.toByteArray(), "GBK");
        logger.info(result);
        Map<String, String> map = null;
        try {
            map = XMLUtil.doXMLParse(result);
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        String apco = map.get("APContentId");
        if (SysConstants.CXL_APCO.equalsIgnoreCase(apco)) {
            // --------------彩心灵业务逻辑------------
            
        } else {
            String aptrid = map.get("APTransactionID");
            logger.info(aptrid);
            String ServiceId = map.get("ServiceId");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String RspTime = dateFormat.format(new Date());
            // ------------------------------
            // 和彩云处理业务开始
            // ------------------------------
            
        }

        // ------------------------------
        // 处理业务完毕
        // ------------------------------
        
        // return "success";
    }

    public static String setXML(String APTransactionID, String ResultCode, String ResultMSG,
            String RspTime, String type) {
        return "<?xml version='1.0' encoding='GBK'?><" + type + "><APTransactionID>"
                + APTransactionID + "</APTransactionID><ResultCode>" + ResultCode
                + "</ResultCode><ResultMSG>" + ResultMSG + "</ResultMSG><RspTime>" + RspTime
                + "</RspTime></" + type + ">";
    }

    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     * 
     * @param request
     * @return
     * @throws IOException
     */
    public final static String getIpAddress(HttpServletRequest request) throws IOException {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

        String ip = request.getHeader("X-Forwarded-For");
        if (logger.isInfoEnabled()) {
            logger.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip="
                            + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip="
                            + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip="
                            + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip="
                            + ip);
                }
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                if (logger.isInfoEnabled()) {
                    logger.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip="
                            + ip);
                }
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }
}
