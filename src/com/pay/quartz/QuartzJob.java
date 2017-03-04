package com.pay.quartz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.mail.Flags.Flag;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;

import com.pay.constants.SysConstants;
import com.pay.dao.TbPayDAO;
import com.pay.dao.TbWebpPayDAO;
import com.pay.dao.TbWxpayDAO;
import com.pay.model.TbPay;
import com.pay.model.TbTcdetail;
import com.pay.model.TbWebpPay;
import com.pay.model.TbWxpay;
import com.pay.service.IReportService;
import com.pay.service.MailService;
import com.pay.service.WXOrderClose;
import com.pay.service.WXOrderQuery;
import com.pay.service.impl.WabpPayOrderServiceImpl;
import com.pay.util.DES;
import com.pay.util.ExcelToHtml;
import com.pay.util.UtilFunc;

// @RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration({ "../../../applicationContext.xml"})
@Transactional
public class QuartzJob {
    private static final Logger logger = LoggerFactory.getLogger(QuartzJob.class);

    @Inject
    IReportService reportServiceImpl;

    // @Test
    public void work() {
        reportServiceImpl.productReort();
        MailService sendmail = new MailService();
        ExcelToHtml excelToHtml = new ExcelToHtml();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTimeString = dateFormat.format(cal.getTime());
        String nowTimeString1 = dateFormat2.format(cal.getTime());
        String nowTimeString2 = dateFormat2.format(new Date());
        sendmail.setUserName(UtilFunc.getProp("n"));
        try {
            sendmail.setPassWord(DES.decrypt((UtilFunc.getProp("p")), "htxl~1ok"));
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        sendmail.setHost(UtilFunc.getProp("h"));
        sendmail.setRecipientStr(UtilFunc.getProp("t"));
        sendmail.setCc(UtilFunc.getProp("c"));
        sendmail.setFrom(UtilFunc.getProp("f"));
        sendmail.setSubject(nowTimeString + " 和彩云空间扩容传输量与容量购买下单支付情况（QuartzJob自动发送请勿回复）");

        try {
            sendmail.setContent(nowTimeString1 + "~" + nowTimeString2 + UtilFunc.getProp("s")
                    + excelToHtml.read());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            logger.info("sendmail exception:", e);

        }
        boolean flags = true;
        try {
            while (flags) {
                sendmail.attachfile("C:\\ReportYunPay\\" + nowTimeString + ".xls");
                if (sendmail.sendMail()) {
                    flags = false;
                } else {
                    flags = true;
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    logger.info("sendmail exception:", e);
                }
            }
        } catch (AddressException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
            logger.info("sendmail exception:", e2);
        } catch (UnsupportedEncodingException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
            logger.info("sendmail exception:", e2);
        } catch (MessagingException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
            logger.info("sendmail exception:", e2);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        System.out.println("-----定时任务开始-----");
        TbWxpayDAO tbPayDAO =
                (TbWxpayDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TbWxpayDAO");
        /*TbWebpPayDAO tbWebpPayDAO =
                (TbWebpPayDAO) ContextLoader.getCurrentWebApplicationContext().getBean(
                        "TbWebpPayDAO");*/
        List<TbWxpay> list = tbPayDAO.findByOrderStatus(1);
        //List<TbWebpPay> listwabp = tbWebpPayDAO.findByOrderStatus(1);
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String outTradeNo = list.get(i).getOutTradeNo();
                Date ctime = list.get(i).getCreateTime();
                if (outTradeNo != null && !outTradeNo.equals("") && ctime != null) {
                    Date nowTime = new Date();
                    long temp = nowTime.getTime() - ctime.getTime();
                    long mins = temp / 60000;
                    if (mins >= 120) {
                        if (reqOrderquery(outTradeNo)) {
                            if (!reqOrderClose(outTradeNo)) {
                                System.out.println("订单关闭失败");
                            } else {
                                System.out.println("===============订单关闭成功==============");
                                if (tbPayDAO.updateClose(outTradeNo, 0)) {
                                    System.out.println("===============订单" + outTradeNo
                                            + "订单关闭信息入库成功==============");
                                }
                            }
                        } else {
                            if (tbPayDAO.updateClose(outTradeNo, -1)) {
                                System.out.println("===============清除不存在的订单" + outTradeNo
                                        + "入库成功==============");
                            }
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

       /* if (listwabp.size() > 0) {
            for (int i = 0; i < listwabp.size(); i++) {
                String outTradeNo = listwabp.get(i).getAptrid();
                Date ctime = listwabp.get(i).getCreateTime();
                if (outTradeNo != null && !outTradeNo.equals("") && ctime != null) {
                    Date nowTime = new Date();
                    long temp = nowTime.getTime() - ctime.getTime();
                    long mins = temp / 60000;
                    if (mins >= 60) {
                        System.out.println("===============只生成订单没支付的话费支付订单关闭成功==============");
                        if (tbWebpPayDAO.updateClose(outTradeNo, 0)) {
                            System.out.println("===============订单" + outTradeNo
                                    + "话费支付订单关闭信息入库成功==============");
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }*/
    }

    private String setXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code
                + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }

    /**
     * 请求订单查询接口，判断是否成功
     * 
     * @param map
     * @param accessToken
     * @return
     */
    private boolean reqOrderquery(String out_trade_no) {
        WXOrderQuery orderQuery = new WXOrderQuery();
        orderQuery.setAppid(SysConstants.APP_ID);
        orderQuery.setMch_id(SysConstants.PARTNER_ID);
        orderQuery.setTransaction_id(null);
        orderQuery.setOut_trade_no(out_trade_no);
        orderQuery.setNonce_str(out_trade_no);

        // 此处需要密钥PartnerKey，此处直接写死，自己的业务需要从持久化中获取此密钥，否则会报签名错误
        orderQuery.setPartnerKey(SysConstants.PARTNER_KEY);

        Map<String, String> orderMap = orderQuery.reqOrderquery();
        // 此处添加支付成功后，支付金额和实际订单金额是否等价，防止钓鱼
        if (orderMap.get("return_code") != null
                && orderMap.get("return_code").equalsIgnoreCase("SUCCESS")) {
            if (orderMap.get("result_code") != null
                    && orderMap.get("result_code").equalsIgnoreCase("SUCCESS")) {
                // String total_fee = map.get("total_fee");
                // order_total_fee应该取订单表Tbpay中的价格纪录吧。。
                // String order_total_fee = map.get("total_fee");
                // if (Integer.parseInt(order_total_fee) >= Integer.parseInt(total_fee)) {
                return true;
                // }
            }
        }
        return false;
    }


    /**
     * 请求订单关闭接口，判断是否成功
     * 
     * @param map
     * @param accessToken
     * @return
     */
    private boolean reqOrderClose(String out_trade_no) {
        WXOrderClose orderClose = new WXOrderClose();
        orderClose.setAppid(SysConstants.APP_ID);
        orderClose.setMch_id(SysConstants.PARTNER_ID);
        orderClose.setOut_trade_no(out_trade_no);
        orderClose.setNonce_str(out_trade_no);

        // 此处需要密钥PartnerKey，此处直接写死，自己的业务需要从持久化中获取此密钥，否则会报签名错误
        orderClose.setPartnerKey(SysConstants.PARTNER_KEY);

        Map<String, String> orderMap = orderClose.reqOrderClose();
        // 此处添加支付成功后，支付金额和实际订单金额是否等价，防止钓鱼
        if (orderMap.get("return_code") != null
                && orderMap.get("return_code").equalsIgnoreCase("SUCCESS")) {
            System.out.println(orderMap.get("orderMap.get"));
            System.out.println(orderMap.get("err_code_des"));
            return true;
        }
        return false;
    }
}
