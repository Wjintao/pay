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
    private static final String WABP_IP = "120.197.23";
    @Inject
    private IWabpPayOrderService wabpPayOrderServiceImpl;
    @Inject
    private IPayOrderService payOrderServiceImpl;
    @Inject
    private IWabpCxlService wabpCxlServiceImpl;

    /**
     * 
     * @Description: TODO 订单同步(wabp、百世恒通、流量充值、会员卡、电影券)(这种代码不是我想要写的，是他们一直在催，所以我就不用设计模式了。)
     * @param @throws IOException
     * @return void
     * @申明 以下代码如有问题，请找产品经理，是他们的需求要这么写的，本人不负任何法律责任
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
                        returnMessage = wabpPayOrderServiceImpl.closePayOrder(aptrid);
                        String noticeStr =
                                setXML(aptrid, "000", "支付失败，不发货", RspTime,
                                        "ServiceWebTransfer2APRsp");
                        logger.info(noticeStr);
                        out.print(noticeStr);
                    } else if ("0".equals(ServiceAction)) {
                        logger.info("===============付款成功==============");
                        // ------------------------------
                        // 处理业务开始
                        // ------------------------------
                        // 此处处理订单状态，结合自己的订单数据完成订单状态的更新
                        if ("1".equals(OrderType)) {
                            returnMessage = wabpPayOrderServiceImpl.updatePayOrder(aptrid, 1);
                        } else {
                            returnMessage = wabpPayOrderServiceImpl.updatePayOrder(aptrid, 0);
                        }

                        if (returnMessage.getCode() == 0) {
                            logger.info("===============订单" + aptrid
                                    + "支付成功信息入库成功+付款成功==============");
                            returnMessage = wabpPayOrderServiceImpl.queryPayOrder(aptrid);
                            // -------流量逻辑参数-------
                            String objIdTc = ((TbTcdetail) returnMessage.getData()).getObjId();
                            Integer flow = ((TbTcdetail) returnMessage.getData()).getSortbyPrice();
                            String phone = returnMessage.getMessage();
                            // -------流量逻辑参数-------
                            if (returnMessage.getCode() == 0) {
                                //String objIdTc = ((TbTcdetail) returnMessage.getData()).getObjId();
                                if (SysConstants.STR_KJHY.equalsIgnoreCase(objIdTc)) {
                                 // ========洗车券赠送代码======
                                    returnMessage =
                                            wabpPayOrderServiceImpl.synchroXiChe(
                                                    ((TbTcdetail) returnMessage.getData())
                                                            .getTcpriceNow(), phone);
                                    // ========流量赠送代码========
                                    /*ReturnMessage returnMgs =
                                            payOrderServiceImpl.flowGive(aptrid + "hy1",
                                                    "会员套餐话费支付无openId", phone, "150");
                                    ReturnMessage returnMgb =
                                            payOrderServiceImpl.flowGive(aptrid + "hy2",
                                                    "会员套餐话费支付无openId", phone, "70");*/
                                    if (returnMessage.getCode() == 0 ) {
                                        String noticeStr =
                                                setXML(aptrid, "000", "支付成功，" + aptrid
                                                        + "会员套餐发货成功", RspTime,
                                                        "ServiceWebTransfer2APRsp");
                                        logger.info(noticeStr);
                                        logger.info(returnMessage.getData()
                                                + returnMessage.getMessage());
                                        out.print(noticeStr);
                                    } else {
                                        String noticeStr =
                                                setXML(aptrid, "000", "支付成功，" + aptrid
                                                        + "会员套餐发货失败", RspTime,
                                                        "ServiceWebTransfer2APRsp");
                                        logger.info(noticeStr);
                                        logger.info(returnMessage.getData()
                                                + returnMessage.getMessage());
                                        out.print(noticeStr);
                                    }
                                }
                                if (SysConstants.STR_TTHY.equalsIgnoreCase(objIdTc)) {
                                    if ("1".equals(OrderType)) {
                                        returnMessage =wabpPayOrderServiceImpl.synchroTongTong("2", phone);
                                    } else {
                                        returnMessage =wabpPayOrderServiceImpl.synchroTongTong("0", phone);
                                    }
                                    if (returnMessage.getCode() == 0 ) {
                                        String noticeStr =
                                                setXML(aptrid, "000", "支付成功，" + aptrid
                                                        + "通通券套餐发货成功", RspTime,
                                                        "ServiceWebTransfer2APRsp");
                                        logger.info(noticeStr);
                                        logger.info(returnMessage.getData()
                                                + returnMessage.getMessage());
                                        out.print(noticeStr);
                                    } else {
                                        String noticeStr =
                                                setXML(aptrid, "000", "支付成功，" + aptrid
                                                        + "通通券套餐发货失败", RspTime,
                                                        "ServiceWebTransfer2APRsp");
                                        logger.info(noticeStr);
                                        logger.info(returnMessage.getData()
                                                + returnMessage.getMessage());
                                        out.print(noticeStr);
                                    }
                                }
                            } else if (returnMessage.getCode() == 1) {
                                
                                    // ========流量赠送代码========
                                if (Arrays.asList(SysConstants.ARR_UFO_HF).contains(objIdTc)) {
                                    /*returnMessage = payOrderServiceImpl.isAccordFlow(aptrid,phone, objIdTc); 
                                    if (returnMessage.getCode() == 0) {*/
                                        returnMessage =
                                                payOrderServiceImpl.flowGive(aptrid, "话费支付无openId",
                                                        phone, flow.toString());
                                        if (returnMessage.getCode() == 0) {
                                            logger.info("===============流量赠送套餐购买的订单" + aptrid
                                                    + "支付成功信息入库成功+付款成功+流量赠送成功"
                                                    + returnMessage.getMessage() + "==============");
                                            String noticeStr =
                                                    setXML(aptrid, "000", "支付成功，容量或传输套餐发货", RspTime,
                                                            "ServiceWebTransfer2APRsp");
                                            logger.info(noticeStr);
                                            out.print(noticeStr);
                                        } else {
                                            logger.info("===============流量赠送套餐购买的订单" + aptrid
                                                + "支付成功信息入库成功+付款成功+流量赠送失败"
                                                + returnMessage.getMessage() + "==============");                                        String noticeStr =
                                                    setXML(aptrid, "000", "支付成功，容量或传输套餐发货", RspTime,
                                                            "ServiceWebTransfer2APRsp");
                                            logger.info(noticeStr);
                                            out.print(noticeStr);
                                        }
                                    /*} else {
                                        logger.info("===============流量赠送套餐购买的订单，但是不符合每天首单赠送" + aptrid
                                            + "支付成功信息入库成功+付款成功+流量不予赠送"
                                            + returnMessage.getMessage() + "==============");                                        String noticeStr =
                                                setXML(aptrid, "000", "支付成功，容量或传输套餐发货", RspTime,
                                                        "ServiceWebTransfer2APRsp");
                                        logger.info(noticeStr);
                                        out.print(noticeStr);
                                    } */
                                } else {
                                    logger.info("===============" + aptrid+ "支付成功信息入库成功+付款成功"+ returnMessage.getMessage() + "==============");
                                    String noticeStr =
                                            setXML(aptrid, "000", "支付成功，容量或传输套餐发货", RspTime,
                                                    "ServiceWebTransfer2APRsp");
                                    logger.info(noticeStr);
                                    out.print(noticeStr);
                                }
                                /*} else if (returnMessage.getCode() == 1
                                        && SysConstants.LS_TC.equalsIgnoreCase(objIdTc)) {
                                    String qudaoString =
                                            wabpPayOrderServiceImpl.queryPayOrderTbWebpPay(aptrid)
                                                    .getQuDao();
                                    if ("WAP".equalsIgnoreCase(qudaoString)) {
                                        // 抽奖或赠送乐视会员卡
                                        returnMessage =
                                                payOrderServiceImpl.generateLeShiKaOrder(
                                                        "话费支付无openid", phone, aptrid, objIdTc,
                                                        "话费支付渠道获取");
                                        if (returnMessage.getCode() == 0) {
                                            logger.info("=======获得乐视会员卡========" + aptrid
                                                    + returnMessage.getMessage()
                                                    + "支付成功信息入库成功+付款成功==============");
                                            String noticeStr =
                                                    setXML(aptrid, "000", "支付成功，容量或传输套餐发货",
                                                            RspTime, "ServiceWebTransfer2APRsp");
                                            logger.info(noticeStr);
                                            out.print(noticeStr);
                                        } else if (returnMessage.getCode() == 1) {
                                            logger.info("=======没有获得乐视会员卡,活动期间非首次购买========"
                                                    + aptrid + returnMessage.getMessage()
                                                    + "支付成功信息入库成功+付款成功==============");
                                            String noticeStr =
                                                    setXML(aptrid, "000", "支付成功，容量或传输套餐发货",
                                                            RspTime, "ServiceWebTransfer2APRsp");
                                            logger.info(noticeStr);
                                            out.print(noticeStr);
                                        } else {
                                            logger.info("=======直接购买但没中乐视会员卡可能没库存发完了或者数据异常========"
                                                    + aptrid + returnMessage.getMessage()
                                                    + "支付成功信息入库成功+付款成功==============");
                                            String noticeStr =
                                                    setXML(aptrid, "000", "支付成功，容量或传输套餐发货",
                                                            RspTime, "ServiceWebTransfer2APRsp");
                                            logger.info(noticeStr);
                                            out.print(noticeStr);
                                        }
                                    } else {
                                        logger.info("===============WEB端50G季度包流量赠送套餐订单" + aptrid
                                                + "支付成功信息入库成功+付款成功==============");
                                        String noticeStr =
                                                setXML(aptrid, "000", "支付成功，容量或传输套餐发货", RspTime,
                                                        "ServiceWebTransfer2APRsp");
                                        logger.info(noticeStr);
                                        out.print(noticeStr);
                                    }*/ 
                            }else {
                                logger.info("===============" + aptrid+ "支付成功信息入库成功但是订单查询有异常+付款成功==============");
                                String noticeStr =
                                setXML(aptrid, "000", "支付成功，容量或传输套餐发货，但是订单查询有异常", RspTime,
                                               "ServiceWebTransfer2APRsp");
                                logger.info(noticeStr);
                                out.print(noticeStr);
                            }
                        } else {
                            logger.info("===============code:" + returnMessage.getCode() + "订单"
                                    + aptrid + "支付成功信息入库失败+付款成功==============");
                            String noticeStr =
                                    setXML(aptrid, "000", "发货，但订单更新失败", RspTime,
                                            "ServiceWebTransfer2APRsp");
                            logger.info(noticeStr);
                            out.print(noticeStr);
                        }
                        // ------------------------------
                        // 处理业务完毕
                        // ------------------------------
                    } else if ("2".equals(ServiceAction) || "3".equals(ServiceAction)) {
                        logger.info("===============aptrid:" + aptrid
                                + "===============退订开始===============");
                        // ------------------------------
                        // 处理业务开始
                        // ------------------------------
                        // 此处处理订单状态，结合自己的订单数据完成订单状态的更新
                        returnMessage = wabpPayOrderServiceImpl.updatePayOrder(aptrid, -1);
                        if (returnMessage.getCode() == 0) {
                            logger.info("===============code:" + returnMessage.getCode() + "订单"
                                    + aptrid + "退订信息入库成功+退订成功==============");
                            returnMessage = wabpPayOrderServiceImpl.queryPayOrder(aptrid);
                            String objIdTc = ((TbTcdetail) returnMessage.getData()).getObjId();
                            String phone = returnMessage.getMessage();
                            if (SysConstants.STR_TTHY.equalsIgnoreCase(objIdTc)) {
                                returnMessage =wabpPayOrderServiceImpl.synchroTongTong("1", phone);
                            }
                            String noticeStr =
                                    setXML(aptrid, "000", "退订信息入库成功，退订成功", RspTime,
                                            "ServiceWebTransfer2APRsp");
                            logger.info(noticeStr);
                            out.print(noticeStr);
                        } else {
                            logger.info("===============code:" + returnMessage.getCode() + "订单"
                                    + aptrid + "退订信息入库失败+退订成功==============");
                            String noticeStr =
                                    setXML(aptrid, "000", "退订信息入库失败，退订成功", RspTime,
                                            "ServiceWebTransfer2APRsp");
                            logger.info(noticeStr);
                            out.print(noticeStr);
                        }
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
            returnMessage.setMessage("大兄弟，你想干嘛？？？");
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
            for (int i = 0; i < 3; i++) {
                returnMessage = wabpCxlServiceImpl.getResultQR(result);
                if (returnMessage.getCode() == 0) {
                    logger.info("彩心灵最终回复MM确认接口报文：" + (String) returnMessage.getMessage());
                    out.print(returnMessage.getMessage());
                    break;
                } else {
                    logger.info("彩心灵订单确认接口无返回" + returnMessage.getCode() + returnMessage.getData()
                            + returnMessage.getMessage());
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    continue;
                }
            }
        } else {
            String aptrid = map.get("APTransactionID");
            logger.info(aptrid);
            String ServiceId = map.get("ServiceId");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String RspTime = dateFormat.format(new Date());
            // ------------------------------
            // 和彩云处理业务开始
            // ------------------------------
            returnMessage = wabpPayOrderServiceImpl.queryPayOrder(aptrid);
            if (returnMessage.getCode() == 0 || returnMessage.getCode() == 1) {
                String noticeStr = setXML(aptrid, "000", "确认成功", RspTime, "VertifyUserState2APRsp");
                logger.info(noticeStr);
                // out.print(new ByteArrayInputStream(noticeStr.getBytes(Charset.forName("GBK"))));
                out.print(noticeStr);
            } else {
                String noticeStr =
                        setXML(aptrid, "001", "确认失败，不存在该订单", RspTime, "VertifyUserState2APRsp");
                logger.info(noticeStr);
                // out.print(new ByteArrayInputStream(noticeStr.getBytes(Charset.forName("GBK"))));
                out.print(noticeStr);
            }
        }

        // ------------------------------
        // 处理业务完毕
        // ------------------------------
        /*
         * }else { String noticeStr = setXML(aptrid, "001", "确认失败", RspTime,
         * "VertifyUserState2APRsp"); logger.info(noticeStr); out.print(new
         * ByteArrayInputStream(noticeStr.getBytes(Charset.forName("GBK")))); }
         */
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
