package com.pay.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.pay.constants.SysConstants;
import com.pay.util.MD5Util;
import com.pay.util.StringUtil;

public class CxlTransmitAction extends BaseJsonAction {
    
    private static Logger logger = Logger.getLogger(CxlTransmitAction.class);  

    public String toCXL() throws IOException {
        try {
            this.getRequest().setCharacterEncoding("utf-8");
            String referer=this.getRequest().getHeader("referer");
            logger.info(referer);
            String requestID = this.getRequest().getParameter("requestID");
            String channelID = this.getRequest().getParameter("channelID");
            String toUrl = this.getRequest().getParameter("toUrl");
            logger.info(toUrl);
            String inputTime = this.getRequest().getParameter("inputTime");
            String sign = this.getRequest().getParameter("sign");
            //String addr = getIpAddress(this.getRequest());
            //logger.info(addr);
            if (StringUtil.isEmpty(requestID) || StringUtil.isEmpty(inputTime)
                    || StringUtil.isEmpty(channelID) || StringUtil.isEmpty(sign)) {
                returnMessage.setCode(-1);
                returnMessage.setData(-1);
                returnMessage.setMessage("请求参数不能为空！");
                outJson(returnMessage);
            } else {
                if (referer!=null && referer.contains(SysConstants.CXL_REFERER) && SysConstants.CXL_CHID.equalsIgnoreCase(channelID)) {
                    String preSignStr =
                            "requestID=" + requestID + "&channelID=" + channelID + "&inputTime="
                                    + inputTime + "&key=" + SysConstants.CXL_REQ_KEY;
                    String signStr = MD5Util.MD5Encode(preSignStr, null).toUpperCase();
                    if (!signStr.equals(sign)) {
                        returnMessage.setCode(1);
                        returnMessage.setData(1);
                        returnMessage.setMessage("sign签名校验失败！");
                        outJson(returnMessage);
                    } else {
                        this.getRequest().setAttribute("toUrl", toUrl);
                        //this.getResponse().setHeader("referer", SysConstants.CXL_RF);
                        //this.getResponse().sendRedirect(toUrl);
                        return SUCCESS;
                    }
                } else {
                    returnMessage.setCode(-2);
                    returnMessage.setData(-2);
                    returnMessage.setMessage("非法referer请求或者非法channelID！");
                    outJson(returnMessage);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
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
                    logger.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);  
                }  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("WL-Proxy-Client-IP");  
                if (logger.isInfoEnabled()) {  
                    logger.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);  
                }  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_CLIENT_IP");  
                if (logger.isInfoEnabled()) {  
                    logger.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);  
                }  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
                if (logger.isInfoEnabled()) {  
                    logger.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);  
                }  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getRemoteAddr();  
                if (logger.isInfoEnabled()) {  
                    logger.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);  
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
