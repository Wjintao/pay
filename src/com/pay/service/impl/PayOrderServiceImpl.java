package com.pay.service.impl;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;

import com.danga.MemCached.MemCachedClient;
import com.pay.action.NotifyWxAction;
import com.pay.constants.SysConstants;
import com.pay.dao.TbOperateDAO;
import com.pay.dao.TbPayDAO;
import com.pay.dao.TbPhoneFlowDAO;
import com.pay.dao.TbPhoneTicketDAO;
import com.pay.dao.TbTcdetailDAO;
import com.pay.dao.TbTicketsDAO;
import com.pay.dao.TbWebpPayDAO;
import com.pay.dao.TbWxpayDAO;
import com.pay.model.MdlPay;
import com.pay.model.ReturnMessage;
import com.pay.model.TbPay;
import com.pay.model.TbPhoneFlow;
import com.pay.model.TbPhoneTicket;
import com.pay.model.TbTcdetail;
import com.pay.model.TbTctype;
import com.pay.model.TbTickets;
import com.pay.model.TbWebpPay;
import com.pay.model.TbWxpay;
import com.pay.service.IPayOrderService;
import com.pay.service.WXNative;
import com.pay.service.WXPay;
import com.pay.service.WXPrepay;
import com.pay.service.WaxCaiYunFlowSend;
import com.pay.util.MD5Util;
import com.pay.util.OrderUtil;
import com.pay.util.StringUtil;

@Transactional
@Repository("tbPayServiceImpl")
public class PayOrderServiceImpl implements IPayOrderService {

    private static final Logger logger = LoggerFactory.getLogger(PayOrderServiceImpl.class);
    private ReturnMessage returnMessage = null;
    @Autowired
    TbPhoneTicketDAO tbPhoneTicketDAO;
    @Autowired
    TbTicketsDAO tbTicketsDAO;
    @Autowired
    WaxCaiYunFlowSend waxCaiYunFlowSend;
    

    @Override
    public ReturnMessage addPayOrder(String lx, String tcId, String openId, String phone,
            String wxno, String wxnick, String outTradeNo, String ip, String inputTime, String sign) {
        returnMessage = new ReturnMessage();
        String preSignStr =
                "phone=" + phone + "&inputTime=" + inputTime + "&key=" + SysConstants.REQ_KEY;
        String signStr = MD5Util.MD5Encode(preSignStr, null).toUpperCase();
        if (!signStr.equals(sign)) {
            returnMessage.setCode(1);
            returnMessage.setData(1);
            returnMessage.setMessage("微信支付统一下单请求校验失败！");
        } else {
            TbTcdetailDAO tbTcdetailDAO =
                    (TbTcdetailDAO) ContextLoader.getCurrentWebApplicationContext().getBean(
                            "TbTcdetailDAO");
            List<TbTcdetail> alist = tbTcdetailDAO.findByObjId(tcId);
            if (alist.size() > 0) {
                TbTcdetail tbTcdetail = alist.get(0);
                MdlPay pay = new MdlPay();
                pay.setAppId(SysConstants.APP_ID);
                pay.setPartnerId(SysConstants.PARTNER_ID);
                pay.setPartnerKey(SysConstants.PARTNER_KEY);
                System.out.println("pay对象初始化：" + pay);
                String spbill_create_ip = ip;
                WXPrepay prePay = new WXPrepay();
                prePay.setAppid(pay.getAppId());
                if (lx != null) {
                    if (lx.equals("1")) {
                        prePay.setBody("和彩云云盘容量");
                    } else if (lx.equals("2")) {
                        prePay.setBody("和彩云云盘传输量");
                    } else {
                        prePay.setBody(SysConstants.DESC_BODY);
                    }
                } else {
                    prePay.setBody(SysConstants.DESC_BODY);
                }
                // prePay.setBody(SysConstants.DESC_BODY);
                prePay.setPartnerKey(pay.getPartnerKey());
                prePay.setMch_id(pay.getPartnerId());
                prePay.setNotify_url(SysConstants.NOTIFY_URL);
                prePay.setOut_trade_no(outTradeNo);
                prePay.setSpbill_create_ip(spbill_create_ip);
                prePay.setTotal_fee(tbTcdetail.getTcprice());
                prePay.setTrade_type("JSAPI");
                prePay.setOpenid(openId);
                // 获取预支付订单号
                String prepayid = prePay.submitXmlGetPrepayId();
                System.out.println("获取的预支付订单是：" + prepayid);
                if (prepayid != null && prepayid.length() > 10) {
                    // 生成微信支付参数，此处拼接为完整的JSON格式，符合支付调起传入格式
                    String jsParam =
                            WXPay.createPackageValue(pay.getAppId(), pay.getPartnerKey(), prepayid);
                    System.out.println("生成的微信调起JS参数为：" + jsParam);
                    try {
                        TbPayDAO tbPayDAO =
                                (TbPayDAO) ContextLoader.getCurrentWebApplicationContext().getBean(
                                        "TbPayDAO");
                        TbPay tbPay = new TbPay();
                        tbPay.setObjId(StringUtil.getUUID());
                        tbPay.setOpenid(openId);
                        tbPay.setPhone(phone);
                        tbPay.setWxno(wxno);
                        tbPay.setWxnick(wxnick);
                        tbPay.setOutTradeNo(outTradeNo);
                        tbPay.setPrepayid(prepayid);
                        tbPay.setTbTcdetail(tbTcdetail);
                        tbPay.setOrderStatus(1);
                        tbPay.setDataStatus(1);
                        tbPay.setCreateTime(new Date());
                        tbPay.setUpdateTime(new Date());
                        tbPay.setRemark("");
                        if (tbPayDAO.save(tbPay)) {
                            returnMessage.setCode(0);
                            returnMessage.setData(jsParam);
                            returnMessage.setMessage("微信支付统一下单入库成功");
                        } else {
                            returnMessage.setCode(2);
                            returnMessage.setData(2);
                            returnMessage.setMessage("微信支付统一下单入库失败！");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        returnMessage.setCode(3);
                        returnMessage.setData(3);
                        returnMessage.setMessage("微信支付统一下单入库失败！");
                    }
                } else {
                    returnMessage.setCode(4);
                    returnMessage.setData(4);
                    returnMessage.setMessage("微信支付统一下单失败");
                }
            } else {
                returnMessage.setCode(5);
                returnMessage.setData(5);
                returnMessage.setMessage("库中查不到相应套餐");
            }
        }
        return returnMessage;
    }

    @Override
    public ReturnMessage updatePayOrder(String outTradeNo) {
        returnMessage = new ReturnMessage();
        try {
            TbPayDAO tbPayDAO =
                    (TbPayDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TbPayDAO");
            List<TbPay> list = tbPayDAO.findByOutTradeNo(outTradeNo);
            if (list.size() > 0) {
                if (tbPayDAO.update(outTradeNo)) {
                    returnMessage.setCode(0);
                    returnMessage.setData(0);
                    returnMessage.setMessage("订单支付成功信息入库成功！");
                } else {
                    returnMessage.setCode(1);
                    returnMessage.setData(1);
                    returnMessage.setMessage("订单支付成功信息入库失败！");
                }
            } else {
                returnMessage.setCode(2);
                returnMessage.setData(2);
                returnMessage.setMessage("未查询到订单！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMessage.setCode(3);
            returnMessage.setData(3);
            returnMessage.setMessage("数据库错误！");
        }
        return returnMessage;
    }


    @Override
    public ReturnMessage closePayOrder(String outTradeNo) {
        returnMessage = new ReturnMessage();
        try {
            TbPayDAO tbPayDAO =
                    (TbPayDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TbPayDAO");
            List<TbPay> list = tbPayDAO.findByOutTradeNo(outTradeNo);
            if (list.size() > 0) {
                if (tbPayDAO.updateClose(outTradeNo, 0)) {
                    returnMessage.setCode(0);
                    returnMessage.setData(0);
                    returnMessage.setMessage("订单关闭成功信息入库成功！");
                } else {
                    returnMessage.setCode(1);
                    returnMessage.setData(1);
                    returnMessage.setMessage("订单关闭成功信息入库失败！");
                }
            } else {
                returnMessage.setCode(2);
                returnMessage.setData(2);
                returnMessage.setMessage("未查询到订单！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMessage.setCode(3);
            returnMessage.setData(3);
            returnMessage.setMessage("数据库错误！");
        }
        return returnMessage;
    }

    @Override
    public ReturnMessage getTcDetail(String phone, String inputTime, String sign, String objid) {
        returnMessage = new ReturnMessage();
        String preSignStr =
                "phone=" + phone + "&inputTime=" + inputTime + "&key=" + SysConstants.REQ_KEY;
        String signStr = MD5Util.MD5Encode(preSignStr, null).toUpperCase();
        if (!signStr.equals(sign)) {
            returnMessage.setCode(1);
            returnMessage.setData(1);
            returnMessage.setMessage("获取套餐列表请求校验失败！");
        } else {
            TbTcdetailDAO tbTcdetailDAO =
                    (TbTcdetailDAO) ContextLoader.getCurrentWebApplicationContext().getBean(
                            "TbTcdetailDAO");
            MemCachedClient memcachedClient =
                    (MemCachedClient) ContextLoader.getCurrentWebApplicationContext().getBean(
                            "memcachedClient");
            if (memcachedClient.get("tbTcdetail_"+objid+"key")!=null) {
                returnMessage.setCode(0);
                returnMessage.setData(memcachedClient.get("tbTcdetail_"+objid+"key"));
                returnMessage.setMessage("获取套餐列表成功");
            } else {
                List<TbTcdetail> alist = tbTcdetailDAO.findAllByTcpace(objid);
                if (alist.size() > 0) {
                    memcachedClient.set("tbTcdetail_"+objid+"key", alist, new Date(1000*60*60));
                    returnMessage.setCode(0);
                    returnMessage.setData(alist);
                    returnMessage.setMessage("获取套餐列表成功");
                } else {
                    returnMessage.setCode(2);
                    returnMessage.setData(2);
                    returnMessage.setMessage("库中查不到套餐列表");
                }
            }
            
        }
        return returnMessage;
    }

    @Override
    public ReturnMessage getOperate(String phone, String cntType, String inputTime, String sign) {
        returnMessage = new ReturnMessage();
        String preSignStr =
                "phone=" + phone + "&inputTime=" + inputTime + "&key=" + SysConstants.REQ_KEY;
        String signStr = MD5Util.MD5Encode(preSignStr, null).toUpperCase();
        if (!signStr.equals(sign)) {
            returnMessage.setCode(1);
            returnMessage.setData(1);
            returnMessage.setMessage("获取运营列表请求校验失败！");
        } else {
            TbOperateDAO tbOperateDAO =
                    (TbOperateDAO) ContextLoader.getCurrentWebApplicationContext().getBean(
                            "TbOperateDAO");
            if (cntType != null && cntType.equalsIgnoreCase("WEB")) {
                cntType = "01";
            } else if (cntType != null && cntType.equalsIgnoreCase("WAP")) {
                cntType = "02";
            } else {
                cntType = "";
            }
            MemCachedClient memcachedClient =
                    (MemCachedClient) ContextLoader.getCurrentWebApplicationContext().getBean(
                            "memcachedClient");
            if (memcachedClient.get("tbOperate"+cntType+"_key")!=null) {
                returnMessage.setCode(0);
                returnMessage.setData(memcachedClient.get("tbOperate"+cntType+"_key"));
                returnMessage.setMessage("获取运营列表成功");
            }else {
                List<TbTcdetail> operatelist = tbOperateDAO.findAllByClientType(cntType);
                if (operatelist.size() > 0) {
                    memcachedClient.set("tbOperate"+cntType+"_key", operatelist,new Date(1000*60*60));
                    returnMessage.setCode(0);
                    returnMessage.setData(operatelist);
                    returnMessage.setMessage("获取运营列表成功");
                } else {
                    returnMessage.setCode(5);
                    returnMessage.setData(5);
                    returnMessage.setMessage("库中查不到运营列表");
                }
            }
        }
        return returnMessage;
    }

    @Override
    public ReturnMessage addWxPayOrder(String cntType, String lx, String tcId, String openId,
            String phone, String wxno, String wxnick, String outTradeNo, String ip,
            String inputTime, String sign ,String channel) {
        returnMessage = new ReturnMessage();
        String preSignStr =
                "phone=" + phone + "&inputTime=" + inputTime + "&key=" + SysConstants.REQ_KEY;
        String signStr = MD5Util.MD5Encode(preSignStr, null).toUpperCase();
        if (!signStr.equals(sign)) {
            returnMessage.setCode(1);
            returnMessage.setData(1);
            returnMessage.setMessage("微信支付统一下单请求校验失败！");
        } else {
            TbTcdetailDAO tbTcdetailDAO =
                    (TbTcdetailDAO) ContextLoader.getCurrentWebApplicationContext().getBean(
                            "TbTcdetailDAO");
            List<TbTcdetail> alist = tbTcdetailDAO.findByObjId(tcId);
            if (alist.size() > 0) {
                if (cntType.equalsIgnoreCase("WEB")) {
                    returnMessage =
                            getCode(lx, tcId, phone, OrderUtil.GetOrderNumber(""), inputTime, sign, channel);
                } else if (cntType.equalsIgnoreCase("WAP")) {
                    TbTcdetail tbTcdetail = alist.get(0);
                    MdlPay pay = new MdlPay();
                    pay.setAppId(SysConstants.APP_ID);
                    pay.setPartnerId(SysConstants.PARTNER_ID);
                    pay.setPartnerKey(SysConstants.PARTNER_KEY);
                    System.out.println("pay对象初始化：" + pay);
                    String spbill_create_ip = ip;
                    WXPrepay prePay = new WXPrepay();
                    prePay.setAppid(pay.getAppId());
                    if (lx != null) {
                        if (lx.equals("1")) {
                            prePay.setBody("和彩云云盘容量");
                        } else if (lx.equals("2")) {
                            prePay.setBody("和彩云云盘传输量");
                        } else {
                            prePay.setBody(SysConstants.DESC_BODY);
                        }
                    } else {
                        prePay.setBody(SysConstants.DESC_BODY);
                    }
                    prePay.setPartnerKey(pay.getPartnerKey());
                    prePay.setMch_id(pay.getPartnerId());
                    prePay.setNotify_url(SysConstants.NOTIFY_WXURL);
                    prePay.setOut_trade_no(outTradeNo);
                    prePay.setSpbill_create_ip(spbill_create_ip);
                    prePay.setTotal_fee(tbTcdetail.getTcpriceNow());
                    prePay.setTrade_type("JSAPI");
                    prePay.setOpenid(openId);
                    // 获取预支付订单号
                    String prepayid = prePay.submitXmlGetPrepayId();
                    System.out.println("获取的预支付订单是：" + prepayid);
                    if (prepayid != null && prepayid.length() > 10) {
                        // 生成微信支付参数，此处拼接为完整的JSON格式，符合支付调起传入格式
                        String jsParam =
                                WXPay.createPackageValue(pay.getAppId(), pay.getPartnerKey(),
                                        prepayid);
                        System.out.println("生成的微信调起JS参数为：" + jsParam);

                        try {
                            /*
                             * TbPayDAO tbPayDAO = (TbPayDAO)
                             * ContextLoader.getCurrentWebApplicationContext().getBean("TbPayDAO");
                             * TbPay tbPay = new TbPay(); tbPay.setObjId(StringUtil.getUUID());
                             * tbPay.setOpenid(openId); tbPay.setPhone(phone); tbPay.setWxno(wxno);
                             * tbPay.setWxnick(wxnick); tbPay.setOutTradeNo(outTradeNo);
                             * tbPay.setPrepayid(prepayid); tbPay.setTbTcdetail(tbTcdetail);
                             * tbPay.setOrderStatus(1); tbPay.setDataStatus(1);
                             * tbPay.setCreateTime(new Date()); tbPay.setUpdateTime(new Date());
                             * tbPay.setRemark("");
                             */
                            TbWxpayDAO tbWxpayDAO =
                                    (TbWxpayDAO) ContextLoader.getCurrentWebApplicationContext()
                                            .getBean("TbWxpayDAO");
                            TbWxpay tbWxpay = new TbWxpay();
                            tbWxpay.setObjId(StringUtil.getUUID());
                            tbWxpay.setOpenid(openId);
                            tbWxpay.setPhone(phone);
                            tbWxpay.setWxno(wxno);
                            tbWxpay.setWxnick(wxnick);
                            tbWxpay.setPrepayid(prepayid);
                            tbWxpay.setOutTradeNo(outTradeNo);
                            tbWxpay.setTradeType("JSAPI");
                            tbWxpay.setTcdetailRelId(tbTcdetail.getObjId());
                            tbWxpay.setOrderStatus(1);
                            tbWxpay.setDataStatus(1);
                            tbWxpay.setCreateTime(new Date());
                            tbWxpay.setUpdateTime(new Date());
                            tbWxpay.setRemark(channel);
                            if (tbWxpayDAO.save(tbWxpay)) {
                                returnMessage.setCode(0);
                                returnMessage.setData(jsParam);
                                returnMessage.setMessage("微信支付统一下单入库成功");
                            } else {
                                returnMessage.setCode(2);
                                returnMessage.setData(2);
                                returnMessage.setMessage("微信支付统一下单入库失败！");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            returnMessage.setCode(3);
                            returnMessage.setData(3);
                            returnMessage.setMessage("微信支付统一下单入库失败！");
                        }
                    } else {
                        returnMessage.setCode(4);
                        returnMessage.setData(4);
                        returnMessage.setMessage("微信支付统一下单失败");
                    }
                }
            } else {
                returnMessage.setCode(5);
                returnMessage.setData(5);
                returnMessage.setMessage("库中查不到相应套餐");
            }
        }
        return returnMessage;
    }

    @Override
    public ReturnMessage getCode(String lx, String tcId, String phone, String productId,
            String inputTime, String sign, String channel) {
        returnMessage = new ReturnMessage();
        String preSignStr =
                "phone=" + phone + "&inputTime=" + inputTime + "&key=" + SysConstants.REQ_KEY;
        String signStr = MD5Util.MD5Encode(preSignStr, null).toUpperCase();
        if (!signStr.equals(sign)) {
            returnMessage.setCode(1);
            returnMessage.setData(1);
            returnMessage.setMessage("生成微信支付二维码请求校验失败！");
        } else {
            TbTcdetailDAO tbTcdetailDAO =
                    (TbTcdetailDAO) ContextLoader.getCurrentWebApplicationContext().getBean(
                            "TbTcdetailDAO");
            List<TbTcdetail> alist = tbTcdetailDAO.findByObjId(tcId);
            if (alist.size() > 0) {
                TbTcdetail tbTcdetail = alist.get(0);
                WXNative wxNative = new WXNative();
                wxNative.setAppid(SysConstants.APP_ID);
                wxNative.setMch_id(SysConstants.PARTNER_ID);
                wxNative.setProduct_id(productId);
                wxNative.setPartnerKey(SysConstants.PARTNER_KEY);
                wxNative.setTrade_type("NATIVE");
                String codeUrl = wxNative.getCodeUrl();
                System.out.println("获取的微信支付二维码地址是：" + codeUrl);
                if (codeUrl != null && codeUrl.length() > 0) {
                    try {
                        TbWxpayDAO tbWxpayDAO =
                                (TbWxpayDAO) ContextLoader.getCurrentWebApplicationContext()
                                        .getBean("TbWxpayDAO");
                        TbWxpay tbWxpay = new TbWxpay();
                        tbWxpay.setObjId(StringUtil.getUUID());
                        tbWxpay.setPhone(phone);
                        tbWxpay.setProductId(productId);
                        tbWxpay.setTradeType("NATIVE");
                        tbWxpay.setTcdetailRelId(tbTcdetail.getObjId());
                        tbWxpay.setOrderStatus(11);
                        tbWxpay.setDataStatus(1);
                        tbWxpay.setCreateTime(new Date());
                        tbWxpay.setUpdateTime(new Date());
                        tbWxpay.setRemark(channel);
                        if (tbWxpayDAO.save(tbWxpay)) {
                            returnMessage.setCode(0);
                            returnMessage.setData(codeUrl);
                            returnMessage.setMessage("生成微信支付二维码入库成功");
                        } else {
                            returnMessage.setCode(2);
                            returnMessage.setData(2);
                            returnMessage.setMessage("生成微信支付二维码入库失败！");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        returnMessage.setCode(3);
                        returnMessage.setData(3);
                        returnMessage.setMessage("生成微信支付二维码入库失败！");
                    }
                } else {
                    returnMessage.setCode(4);
                    returnMessage.setData(4);
                    returnMessage.setMessage("生成微信支付二维码失败");
                }
            } else {
                returnMessage.setCode(5);
                returnMessage.setData(5);
                returnMessage.setMessage("库中查不到相应套餐");
            }
        }
        return returnMessage;
    }

    @Override
    public ReturnMessage updateWxPayOrder(String outTradeNo) {
        returnMessage = new ReturnMessage();
        try {
            TbWxpayDAO tbWxpayDAO =
                    (TbWxpayDAO) ContextLoader.getCurrentWebApplicationContext().getBean(
                            "TbWxpayDAO");
            List<TbWxpay> list = tbWxpayDAO.findByOutTradeNo(outTradeNo);
            if (list.size() > 0) {
                if (tbWxpayDAO.update(outTradeNo)) {
                    returnMessage.setCode(0);
                    returnMessage.setData(0);
                    returnMessage.setMessage("订单支付成功信息入库成功！");
                } else {
                    returnMessage.setCode(1);
                    returnMessage.setData(1);
                    returnMessage.setMessage("订单支付成功信息入库失败！");
                }
            } else {
                returnMessage.setCode(2);
                returnMessage.setData(2);
                returnMessage.setMessage("未查询到订单！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMessage.setCode(3);
            returnMessage.setData(3);
            returnMessage.setMessage("数据库错误！");
        }
        return returnMessage;
    }

    @Override
    public ReturnMessage getTicket(String phone, String openId, String tcId, String inputTime, String sign) {
        returnMessage = new ReturnMessage();
        String preSignStr =
                "phone=" + phone + "&inputTime=" + inputTime + "&key=" + SysConstants.REQ_KEY;
        String signStr = MD5Util.MD5Encode(preSignStr, null).toUpperCase();
        if (!signStr.equals(sign)) {
            returnMessage.setCode(1);
            returnMessage.setData(1);
            returnMessage.setMessage("获取乐视卡请求校验失败！");
        } else {
                if (SysConstants.LS_TC.equalsIgnoreCase(tcId)) {
                    List<TbPhoneTicket> alist = tbPhoneTicketDAO.findByPhoneUFO(phone,tcId);
                    if (alist.size() > 0) {
                        returnMessage.setCode(0);
                        returnMessage.setData(alist.get(0).getTicket1().substring(2));
                        returnMessage.setMessage("获取该号码乐视卡成功");
                    } else {
                        List<TbTickets> tbTList = tbTicketsDAO.findByDataStatus(1);
                        if (tbTList.size()==0) {
                            returnMessage.setCode(3);
                            returnMessage.setData(3);
                            returnMessage.setMessage("乐视卡没有了，送完了");
                        } else {
                            returnMessage.setCode(1);
                            returnMessage.setData(1);
                            returnMessage.setMessage("活动期间非首次购买，没有乐视卡送");
                        }
                    }
                } else if(SysConstants.TX_TC.equalsIgnoreCase(tcId)){
                    List<TbPhoneTicket> alist = tbPhoneTicketDAO.findByPhoneUFO(phone,tcId);
                    if (alist.size() > 0) {
                        returnMessage.setCode(0);
                        returnMessage.setData(alist.get(0).getTicket1().substring(2));
                        returnMessage.setMessage("获取该号码腾讯游戏码成功");
                    } else {
                        List<TbTickets> tbTList = tbTicketsDAO.findByDataStatus(11);
                        if (tbTList.size()==0) {
                            returnMessage.setCode(3);
                            returnMessage.setData(3);
                            returnMessage.setMessage("腾讯游戏码没有了，送完了");
                        } else {
                            returnMessage.setCode(1);
                            returnMessage.setData(1);
                            returnMessage.setMessage("活动期间非每天首次购买，没有腾讯游戏码送");
                        }
                    }
                }else {
                    returnMessage.setCode(2);
                    returnMessage.setData(2);
                    returnMessage.setMessage("无卡券赠送活动。。");
                }
            }
        return returnMessage;
    }

    @Override
    public ReturnMessage addtbPhoneTicket(String outTradeNo, String openId) {
        returnMessage = new ReturnMessage();
        TbWxpayDAO tbWxpayDAO =
                (TbWxpayDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TbWxpayDAO");
        TbWxpay tbWxpay = (TbWxpay) tbWxpayDAO.findByOutTradeNo(outTradeNo).get(0);
        // TbPhoneTicket tbPhoneTicket=new TbPhoneTicket();
        String phoneString = tbWxpay.getPhone();
        if (tbTicketsDAO.isExtract()) {
            List<TbPhoneTicket> tbPTList = tbPhoneTicketDAO.findByPhone(phoneString,"%%");
            List<TbTickets> tbTList = tbTicketsDAO.findByDataStatus(1);
            String ticketString = tbTList.get(0).getTickets();
            if (tbPTList.size() > 0) {
                Integer count = tbPTList.get(0).getTicketgetCount();
                if (count < 3) {
                    if (tbPhoneTicketDAO.update(outTradeNo,phoneString, count + 1, ticketString)) {
                        returnMessage.setCode(0);
                        returnMessage.setData(0);
                        returnMessage.setMessage("个人电影票" + count + 1 + "发送入库成功");
                    } else {
                        returnMessage.setCode(-1);
                        returnMessage.setData(-1);
                        returnMessage.setMessage("个人电影票" + count + 1 + "发送入库失败");
                    }
                } else {
                    returnMessage.setCode(1);
                    returnMessage.setData(1);
                    returnMessage.setMessage("（每人最多获得两张电影票~欢迎把活动分享给你的朋友！）");
                }
            } else {
                TbPhoneTicket tbPhoneTicket = new TbPhoneTicket();
                tbPhoneTicket.setObjId(StringUtil.getUUID());
                tbPhoneTicket.setDataStatus(1);
                tbPhoneTicket.setOpenid(openId);
                tbPhoneTicket.setPhone(phoneString);
                tbPhoneTicket.setTicketgetCount(1);
                tbPhoneTicket.setCreateTime(new Date());
                tbPhoneTicket.setTicket1(ticketString);
                tbPhoneTicket.setTicket2(null);
                tbPhoneTicket.setTicket3(outTradeNo);
                tbPhoneTicket.setTicket4(null);
                if (tbPhoneTicketDAO.save(tbPhoneTicket)) {
                    returnMessage.setCode(0);
                    returnMessage.setData(0);
                    returnMessage.setMessage("个人电影票1发送入库成功");
                } else {
                    returnMessage.setCode(-1);
                    returnMessage.setData(-1);
                    returnMessage.setMessage("个人电影票1发送入库失败");
                }
            }
        } else {
            returnMessage.setCode(2);
            returnMessage.setData(2);
            returnMessage.setMessage("（《爵迹》电影兑换码被抢光了！）");
        }
        return returnMessage;
    }
    
    @Override
    public ReturnMessage queryPayOrder(String aptrid) {
        returnMessage = new ReturnMessage();
        try {
            TbWxpayDAO tbWxpayDAO =
                    (TbWxpayDAO) ContextLoader.getCurrentWebApplicationContext().getBean(
                            "TbWxpayDAO");
            TbTcdetailDAO tbTcdetailDAO =
                    (TbTcdetailDAO) ContextLoader.getCurrentWebApplicationContext().getBean(
                            "TbTcdetailDAO");
            List<TbWxpay> listwx = tbWxpayDAO.findByOutTradeNo(aptrid);
            if (listwx.size() > 0) {
                TbTcdetail tbTcdetail = tbTcdetailDAO.findById(listwx.get(0).getTcdetailRelId());
                returnMessage.setCode(0);
                returnMessage.setData(tbTcdetail);
                returnMessage.setMessage(listwx.get(0).getPhone());
            } else {
                returnMessage.setCode(2);
                returnMessage.setData(2);
                returnMessage.setMessage("未查询到订单！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnMessage.setCode(3);
            returnMessage.setData(3);
            returnMessage.setMessage("数据库错误！");
        }
        return returnMessage;
    }

    @Override
    public ReturnMessage isAccordFlow(String outTradeNo, String phone,String objIdTc) {
        TbWebpPayDAO tbWebpPayDAO =
                (TbWebpPayDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TbWebpPayDAO");
        TbWxpayDAO tbWxpayDAO =
                (TbWxpayDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TbWxpayDAO");
        List<TbWebpPay> listhf = tbWebpPayDAO.findByPhoneUFO(phone,objIdTc);
        List<TbWxpay> listwx = tbWxpayDAO.findByPhoneUFO(phone,objIdTc);
        if ((listhf.size() + listwx.size())<=1) {
            returnMessage.setCode(0);
            returnMessage.setData(0);
            returnMessage.setMessage("该用户是符合首单流量赠送");
        } else {
            returnMessage.setCode(1);
            returnMessage.setData(1);
            returnMessage.setMessage("该用户是不符合首单流量赠送");
        }
        return returnMessage;
    }


    @Override
    public ReturnMessage getIsUFO(String phone, String openId,String inputTime, String sign) {
        returnMessage = new ReturnMessage();
        String preSignStr =
                "phone=" + phone + "&inputTime=" + inputTime + "&key=" + SysConstants.REQ_KEY;
        String signStr = MD5Util.MD5Encode(preSignStr, null).toUpperCase();
        if (!signStr.equals(sign)) {
            returnMessage.setCode(1);
            returnMessage.setData(1);
            returnMessage.setMessage("请求参数校验失败！");
        } else {
            TbPhoneFlowDAO tbPhoneFlowDAO =
                    (TbPhoneFlowDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TbPhoneFlowDAO");
            List<TbPhoneFlow> list = tbPhoneFlowDAO.findByPhoneUFO(phone,openId);
            if (list.size()>0) {
                returnMessage.setCode(0);
                returnMessage.setData(SysConstants.WAP_BU + phone + "&openId=" + openId);
                returnMessage.setMessage(phone+"该用户是符合流量赠送");
            } else {
                returnMessage.setCode(2);
                returnMessage.setData(SysConstants.WAP_BU + phone + "&openId=" + openId);
                returnMessage.setMessage(phone+"该用户是不符合流量赠送");
            }
               
        }
        return returnMessage;
    }

    @Override
    public ReturnMessage flowGive(String outTradeNo,String openId,String phone, String flow) {
        returnMessage = new ReturnMessage();
        //WaxCaiYunFlowSend waxCaiYunFlowSend=new WaxCaiYunFlowSend();
        TbPhoneFlowDAO tbPhoneFlowDAO =
                (TbPhoneFlowDAO) ContextLoader.getCurrentWebApplicationContext().getBean("TbPhoneFlowDAO");
        String[] flowReturn=waxCaiYunFlowSend.submitJsonPost(outTradeNo,phone, flow);
        if (flowReturn.length>0 && ("0").equalsIgnoreCase(flowReturn[0])) {
            TbPhoneFlow tbPhoneFlow=new TbPhoneFlow();
            tbPhoneFlow.setCreateTime(new Date());
            tbPhoneFlow.setUpdateTime(new Date());
            tbPhoneFlow.setDataStatus(1);
            tbPhoneFlow.setFlow(flow);
            tbPhoneFlow.setObjId(StringUtil.getUUID());
            tbPhoneFlow.setOpenid(openId);
            tbPhoneFlow.setOrderid(flowReturn[2]);
            tbPhoneFlow.setRemark(flowReturn[1]);
            tbPhoneFlow.setPhone(phone);
            if (tbPhoneFlowDAO.save(tbPhoneFlow)) {
                returnMessage.setCode(0);
                returnMessage.setData(0);
                returnMessage.setMessage(phone+"赠送"+flow+"流量成功并信息入库成功");
            } else {
                returnMessage.setCode(0);
                returnMessage.setData(1);
                returnMessage.setMessage(phone+"赠送"+flow+"流量成功并信息入库失败");
            };
        } else {
            TbPhoneFlow tbPhoneFlowsb=new TbPhoneFlow();
            tbPhoneFlowsb.setCreateTime(new Date());
            tbPhoneFlowsb.setUpdateTime(new Date());
            tbPhoneFlowsb.setDataStatus(0);
            tbPhoneFlowsb.setFlow(flow);
            tbPhoneFlowsb.setObjId(StringUtil.getUUID());
            tbPhoneFlowsb.setOpenid(openId);
            tbPhoneFlowsb.setOrderid(flowReturn[2]);
            tbPhoneFlowsb.setRemark(flowReturn[1]);
            tbPhoneFlowsb.setPhone(phone);
            if (tbPhoneFlowDAO.save(tbPhoneFlowsb)) {
                returnMessage.setCode(1);
                returnMessage.setData(1);
                returnMessage.setMessage(phone+"赠送"+flow+"流量失败并信息入库成功");
            } else {
                returnMessage.setCode(1);
                returnMessage.setData(1);
                returnMessage.setMessage(phone+"赠送"+flow+"流量失败并信息入库失败");
            };
        }
        return returnMessage;
    }

    @Override
    public ReturnMessage generateLeShiKaOrder(String openId, String phone, String outTradeNo,
            String objIdTc, String cardfrom) {
        returnMessage = new ReturnMessage();
        List<TbTickets> tbTList = tbTicketsDAO.findByDataStatus(1);
        if (tbTList.size()>0) {
            String ticketString = tbTList.get(0).getTickets();
            //String retickets = ticketString.substring(2);
            if ("1".equalsIgnoreCase(objIdTc)) {
                if (tbPhoneTicketDAO.findByTicket4(cardfrom).size()<200) {
                    java.util.Random ran = new java.util.Random();
                    double base = ran.nextDouble();
                    logger.info(phone+"抽奖概率是"+String.valueOf(base));
                    if (base <= SysConstants.GL_NUM) {
                        TbPhoneTicket tbPhoneTicket = new TbPhoneTicket();
                        tbPhoneTicket.setObjId(StringUtil.getUUID());
                        tbPhoneTicket.setDataStatus(1);
                        tbPhoneTicket.setOpenid(openId);
                        tbPhoneTicket.setPhone(phone);
                        tbPhoneTicket.setTicketgetCount(1);
                        tbPhoneTicket.setCreateTime(new Date());
                        tbPhoneTicket.setUpdateTime(new Date());
                        tbPhoneTicket.setTicket1(ticketString);
                        tbPhoneTicket.setTicket2(outTradeNo);
                        tbPhoneTicket.setTicket3(objIdTc);
                        tbPhoneTicket.setTicket4(cardfrom);
                        if (tbPhoneTicketDAO.save(tbPhoneTicket)) {
                            if (tbTicketsDAO.update(ticketString)) {
                                returnMessage.setCode(0);
                                returnMessage.setData(0);
                                returnMessage.setMessage(phone+"抽奖中乐视会员卡发送入库成功，更新库存成功");
                            } else {
                                returnMessage.setCode(-1);
                                returnMessage.setData(-1);
                                returnMessage.setMessage(phone+"抽奖中乐视会员卡发送异常入库失败，更新库存失败");
                            }
                        } else {
                            returnMessage.setCode(-1);
                            returnMessage.setData(-1);
                            returnMessage.setMessage(phone+"抽奖中乐视会员卡发送异常入库失败");
                        }
                    } else {
                        returnMessage.setCode(1);
                        returnMessage.setData(1);
                        returnMessage.setMessage(phone+"很抱歉，没中奖。。");
                    }
                } else {
                    returnMessage.setCode(2);
                    returnMessage.setData(2);
                    returnMessage.setMessage("抽奖的乐视会员卡200张木有了。。");
                }
            } else if(SysConstants.LS_TC.equalsIgnoreCase(objIdTc)){
                if (tbPhoneTicketDAO.findByPhone(phone,objIdTc).size()>0) {
                    returnMessage.setCode(1);
                    returnMessage.setData(1);
                    returnMessage.setMessage(phone+"活动期间非首次购买");
                } else {
                TbPhoneTicket tbPhoneTicket = new TbPhoneTicket();
                tbPhoneTicket.setObjId(StringUtil.getUUID());
                tbPhoneTicket.setDataStatus(1);
                tbPhoneTicket.setOpenid(openId);
                tbPhoneTicket.setPhone(phone);
                tbPhoneTicket.setTicketgetCount(1);
                tbPhoneTicket.setCreateTime(new Date());
                tbPhoneTicket.setUpdateTime(new Date());
                tbPhoneTicket.setTicket1(ticketString);
                tbPhoneTicket.setTicket2(outTradeNo);
                tbPhoneTicket.setTicket3(objIdTc);
                tbPhoneTicket.setTicket4(cardfrom);
                if (tbPhoneTicketDAO.save(tbPhoneTicket)) {
                    if (tbTicketsDAO.update(ticketString)) {
                        returnMessage.setCode(0);
                        returnMessage.setData(0);
                        returnMessage.setMessage(phone+"乐视会员卡发送入库成功，更新库存成功");
                    } else {
                        returnMessage.setCode(-1);
                        returnMessage.setData(-1);
                        returnMessage.setMessage(phone+"乐视会员卡发送入库成功，更新库存失败");
                    }
                } else {
                    returnMessage.setCode(-1);
                    returnMessage.setData(-1);
                    returnMessage.setMessage(phone+"乐视会员卡发送异常入库失败，入库失败");
                }
            }
          }
        } else {
            returnMessage.setCode(3);
            returnMessage.setData(3);
            returnMessage.setMessage("乐视会员卡没了，发完了，驾崩了");
        }
        return returnMessage;
    }
}
