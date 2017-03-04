package com.pay.service.impl;

import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;

import com.pay.constants.SysConstants;
import com.pay.dao.TbPayDAO;
import com.pay.dao.TbTcdetailDAO;
import com.pay.dao.TbWebpPayDAO;
import com.pay.model.MdlPay;
import com.pay.model.ReturnMessage;
import com.pay.model.TbPay;
import com.pay.model.TbTcdetail;
import com.pay.model.TbWebpPay;
import com.pay.service.IWabpPayOrderService;
import com.pay.service.WXPay;
import com.pay.service.WXPrepay;
import com.pay.service.WabpCaiXinLing;
import com.pay.service.WabpPay;
import com.pay.service.WabpPayTongTongHuiYuan;
import com.pay.service.WabpPayXiCheHuiYuan;
import com.pay.service.WabpUnPurchase;
import com.pay.util.MD5Util;
import com.pay.util.StringUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;

@Transactional
@Repository("wabpPayOrderServiceImpl")
public class WabpPayOrderServiceImpl implements IWabpPayOrderService {
    private static final Logger logger = LoggerFactory.getLogger(WabpPayOrderServiceImpl.class);

    private ReturnMessage returnMessage = null;

    @Override
    public ReturnMessage addPayOrder(String quDao, String flag, String tcId, String lx,
            String phone, String mid, String aptrid, String inputTime, String sign, String openId ,String channel) {
        returnMessage = new ReturnMessage();
        String preSignStr =
                "phone=" + phone + "&inputTime=" + inputTime + "&key=" + SysConstants.REQ_KEY;
        String signStr = MD5Util.MD5Encode(preSignStr, null).toUpperCase();
        logger.info(signStr);
        if (!signStr.equals(sign)) {
            returnMessage.setCode(1);
            returnMessage.setData(1);
            returnMessage.setMessage("支付请求校验失败！");
        } else {
            TbTcdetailDAO tbTcdetailDAO =
                    (TbTcdetailDAO) ContextLoader.getCurrentWebApplicationContext().getBean(
                            "TbTcdetailDAO");
            List<TbTcdetail> alist = tbTcdetailDAO.findByObjId(tcId);
            if (alist.size() > 0) {
                TbTcdetail tbTcdetail = alist.get(0);
                WabpPay wabpPay = new WabpPay();
                wabpPay.setApco(SysConstants.APCO);
                wabpPay.setAptid(Base64.encodeBase64String(phone.getBytes()));
                wabpPay.setEx(SysConstants.EX);
                wabpPay.setCh(SysConstants.CH);
                if (("4").equals(lx)) {
                    if (("4").equals(tcId)) {
                        wabpPay.setSin("kdyzz");
                    } else if(("5").equals(tcId)){
                        wabpPay.setSin("kdyzo");
                    }else if(("7").equals(tcId)){
                        wabpPay.setSin("kdyzy");
                    }else {
                        wabpPay.setSin(tbTcdetail.getSin());
                    }
                }else {
                    wabpPay.setSin(tbTcdetail.getSin());
                }
                
                // wabpPay.setBu((new
                // sun.misc.BASE64Encoder()).encode((SysConstants.BU).getBytes()));
                if (flag.equalsIgnoreCase("WEB")) {
                    if (("4").equals(lx)) {
                        wabpPay.setBu(Base64.encodeBase64String((SysConstants.WAP_BU_JSWEB + channel
                                + "&phone=" + phone + "&tcId=" + tcId).getBytes()));
                    }else{
                        wabpPay.setBu(Base64.encodeBase64String((SysConstants.WEB_BU + phone)
                            .getBytes()));
                    }
                } else if (flag.equalsIgnoreCase("WAP")) {
                    if (("3").equals(lx)) {
                        wabpPay.setBu(Base64.encodeBase64String((SysConstants.WAP_BU_HY + phone
                                + "&openId=" + openId + "&tcId=" + tcId).getBytes()));
                    } else if (("4").equals(lx)) {
                        wabpPay.setBu(Base64.encodeBase64String((SysConstants.WAP_BU_JSWAP + channel
                                + "&phone=" + phone + "&tcId=" + tcId).getBytes()));
                    }else{
                        wabpPay.setBu(Base64.encodeBase64String((SysConstants.WAP_BU + phone
                                + "&openId=" + openId + "&tcId=" + tcId).getBytes()));
                    }
                }
                wabpPay.setAptrid(aptrid);
                wabpPay.setXid("");
                wabpPay.setMid(mid);
                TbWebpPayDAO tbWebpPayDAO =
                        (TbWebpPayDAO) ContextLoader.getCurrentWebApplicationContext().getBean(
                                "TbWebpPayDAO");
                List lxist = null;
                if (Arrays.asList(SysConstants.ARR_KJHY).contains(tcId) && !("4").equals(lx)) {
                    lxist = tbWebpPayDAO.checkXiCheHy(phone,tcId);
                } 

                if (lxist != null && lxist.size()>0) {
                    returnMessage.setCode(1);
                    returnMessage.setData(1);
                    returnMessage.setMessage("该套餐一个月只能购买一次");
                } else {
                    try {
                        TbWebpPay tbWebpPay = new TbWebpPay();
                        tbWebpPay.setObjId(StringUtil.getUUID());
                        tbWebpPay.setMid(mid);
                        tbWebpPay.setPhone(phone);
                        tbWebpPay.setAptrid(aptrid);
                        tbWebpPay.setTcdetailRelId(tbTcdetail.getObjId());
                        tbWebpPay.setOrderStatus(1);
                        tbWebpPay.setDataStatus(1);
                        tbWebpPay.setCreateTime(new Date());
                        tbWebpPay.setUpdateTime(new Date());
                        tbWebpPay.setRemark("");
                        tbWebpPay.setQuDao(quDao);
                        tbWebpPay.setSin(channel);
                        if (tbWebpPayDAO.save(tbWebpPay)) {
                            String UrlWabp = wabpPay.getWabpUrl(flag);
                            logger.info(UrlWabp);
                            returnMessage.setCode(0);
                            returnMessage.setData(UrlWabp);
                            returnMessage.setMessage("支付下单入库成功");
                        } else {
                            returnMessage.setCode(2);
                            returnMessage.setData(2);
                            returnMessage.setMessage("支付下单入库失败！");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        returnMessage.setCode(3);
                        returnMessage.setData(3);
                        returnMessage.setMessage("支付下单入库失败！");
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
    public ReturnMessage updatePayOrder(String aptrid, Integer OrderType) {
        returnMessage = new ReturnMessage();
        try {
            TbWebpPayDAO tbWebpPayDAO =
                    (TbWebpPayDAO) ContextLoader.getCurrentWebApplicationContext().getBean(
                            "TbWebpPayDAO");
            List<TbWebpPay> list = tbWebpPayDAO.findByAptrid(aptrid);
            if (list.size() > 0) {
                if (tbWebpPayDAO.update(aptrid, OrderType)) {
                    returnMessage.setCode(0);
                    returnMessage.setData(0);
                    returnMessage.setMessage("订单信息入库成功！");
                } else {
                    returnMessage.setCode(1);
                    returnMessage.setData(1);
                    returnMessage.setMessage("订单信息入库失败！");
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
    public ReturnMessage closePayOrder(String aptrid) {
        returnMessage = new ReturnMessage();
        try {
            TbWebpPayDAO tbWebpPayDAO =
                    (TbWebpPayDAO) ContextLoader.getCurrentWebApplicationContext().getBean(
                            "TbWebpPayDAO");
            List<TbWebpPay> list = tbWebpPayDAO.findByAptrid(aptrid);
            if (list.size() > 0) {
                if (tbWebpPayDAO.updateClose(aptrid, 0)) {
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
    public ReturnMessage queryPayOrder(String aptrid) {
        returnMessage = new ReturnMessage();
        try {
            TbWebpPayDAO tbWebpPayDAO =
                    (TbWebpPayDAO) ContextLoader.getCurrentWebApplicationContext().getBean(
                            "TbWebpPayDAO");
            TbTcdetailDAO tbTcdetailDAO =
                    (TbTcdetailDAO) ContextLoader.getCurrentWebApplicationContext().getBean(
                            "TbTcdetailDAO");
            List<TbWebpPay> list = tbWebpPayDAO.findByAptrid(aptrid);
            if (list.size() > 0) {
                TbTcdetail tbTcdetail = tbTcdetailDAO.findById(list.get(0).getTcdetailRelId());
                if (tbTcdetail.getTctypeRelId().equals("123zxcv")) {
                    returnMessage.setCode(0);
                    returnMessage.setData(tbTcdetail);
                    returnMessage.setMessage(list.get(0).getPhone());
                } else {
                    returnMessage.setCode(1);
                    returnMessage.setData(tbTcdetail);
                    returnMessage.setMessage(list.get(0).getPhone());
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
    public TbWebpPay queryPayOrderTbWebpPay(String aptrid) {
        TbWebpPayDAO tbWebpPayDAO =
                (TbWebpPayDAO) ContextLoader.getCurrentWebApplicationContext().getBean(
                        "TbWebpPayDAO");
        List<TbWebpPay> list = tbWebpPayDAO.findByAptrid(aptrid);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            logger.info("很大问题呀");
            return null;
        }
    }

    @Override
    public ReturnMessage synchroXiChe(String tbPrice, String PhoneNo) {
        returnMessage = new ReturnMessage();
        WabpPayXiCheHuiYuan wabpPayXiCheHuiYuan = new WabpPayXiCheHuiYuan();
        String AuthorizeAccountNo;
        if (tbPrice.equals("1000")) {
            AuthorizeAccountNo = "hecaiyun10";
        } else if (tbPrice.equals("2000")) {
            AuthorizeAccountNo = "hecaiyun20";
        } else {
            AuthorizeAccountNo = "hecaiyun30";
        }
        String[] reStrings;
        try {
            reStrings = wabpPayXiCheHuiYuan.submitJsonPost(PhoneNo, AuthorizeAccountNo, "0");
            if (reStrings[0].equalsIgnoreCase("true")) {
                returnMessage.setCode(0);
                returnMessage.setData(reStrings[1]);
                returnMessage.setMessage(reStrings[2]);
            } else {
                returnMessage.setCode(1);
                returnMessage.setData(reStrings[1]);
                returnMessage.setMessage(reStrings[2]);
            }
        } catch (JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return returnMessage;

    }

    @Override
    public ReturnMessage synchroTongTong(String Opration, String PhoneNo) {
        returnMessage = new ReturnMessage();
        WabpPayTongTongHuiYuan wabpTongTongHuiYuan = new WabpPayTongTongHuiYuan();
        String[] reStrings;
        try {
            reStrings = wabpTongTongHuiYuan.submitJsonPost(PhoneNo, Opration);
            if ("00000".equalsIgnoreCase(reStrings[0])) {
                returnMessage.setCode(0);
                returnMessage.setData(reStrings[1]);
                returnMessage.setMessage(reStrings[1]);
            } else {
                returnMessage.setCode(1);
                returnMessage.setData(reStrings[1]);
                returnMessage.setMessage(reStrings[1]);
            }
        } catch (JsonGenerationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return returnMessage;

    }
    
    @Override
    public ReturnMessage unPurchase(String aptrid,String openId, String PhoneNo, String inputTime, String sign) {
        returnMessage = new ReturnMessage();
        String preSignStr =
                "phone=" + PhoneNo + "&inputTime=" + inputTime + "&key=" + SysConstants.REQ_KEY;
        String signStr = MD5Util.MD5Encode(preSignStr, null).toUpperCase();
        if (!signStr.equals(sign)) {
            returnMessage.setCode(1);
            returnMessage.setData(1);
            returnMessage.setMessage("订单退订请求校验失败！");
        } else {
            TbWebpPayDAO tbWebpPayDAO =
                    (TbWebpPayDAO) ContextLoader.getCurrentWebApplicationContext().getBean(
                            "TbWebpPayDAO");
            List<TbWebpPay> alist = tbWebpPayDAO.findByAptrid(aptrid);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (alist.size() > 0) {
                TbWebpPay tbWebpPay = alist.get(0);
                WabpUnPurchase upc = new WabpUnPurchase();
                upc.setActiontime(dateFormat.format(new Date()));
                upc.setAPContentId(SysConstants.APCO);
                upc.setAPTransactionID(aptrid);
                upc.setAPUserId(Base64.encodeBase64String(PhoneNo.getBytes()));
                upc.setChannelId(SysConstants.CH);
                upc.setFeeType("15");
                upc.setMethod(null);
                upc.setSignMethod("DSA");
                // 获取预支付订单号
                String resultCode = null;
                try {
                    resultCode = upc.unsubscribe();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                logger.info(aptrid + "订单退订返回结果是：" + resultCode);
                if ("000".equalsIgnoreCase(resultCode)) {
                    if (tbWebpPayDAO.update(aptrid, -1)) {
                        returnMessage.setCode(0);
                        returnMessage.setData(SysConstants.WAP_BU_HY + PhoneNo + "&openId=" + openId);
                        returnMessage.setMessage(aptrid + "订单退订成功，数据更新成功" + resultCode);
                    } else {
                        returnMessage.setCode(1);
                        returnMessage.setData(1);
                        returnMessage.setMessage(aptrid + "订单退订成功，数据更新失败" + resultCode);
                    };
                } else {
                    returnMessage.setCode(2);
                    returnMessage.setData(2);
                    returnMessage.setMessage(aptrid + "订单退订失败" + resultCode);
                }
            } else {
                returnMessage.setCode(5);
                returnMessage.setData(5);
                returnMessage.setMessage("库中查不到相应订单");
            }
        }
        logger.info(returnMessage.getCode() + returnMessage.getMessage());
        return returnMessage;
    }


    @Override
    public ReturnMessage getUnPurchase(String openId, String PhoneNo, String inputTime, String sign, String lx) {
        returnMessage = new ReturnMessage();
        String preSignStr =
                "phone=" + PhoneNo + "&inputTime=" + inputTime + "&key=" + SysConstants.REQ_KEY;
        String signStr = MD5Util.MD5Encode(preSignStr, null).toUpperCase();
        if (!signStr.equals(sign)) {
            returnMessage.setCode(1);
            returnMessage.setData(1);
            returnMessage.setMessage("订单退订请求校验失败！");
        } else {
            TbWebpPayDAO tbWebpPayDAO =
                    (TbWebpPayDAO) ContextLoader.getCurrentWebApplicationContext().getBean(
                            "TbWebpPayDAO");
            if ("1".equalsIgnoreCase(lx)) {
                List<TbWebpPay> kjlist = tbWebpPayDAO.findUnPurchaseKJ(PhoneNo);
                if (kjlist.size() > 0) {
                    returnMessage.setCode(0);
                    returnMessage.setData(SysConstants.WAP_BU_HY + PhoneNo + "&openId=" + openId
                        + "&lx="+ lx  + "&wabp_result=666");
                    returnMessage.setMessage("您有可退订的容量套餐服务");
                } else {
                    returnMessage.setCode(2);
                    returnMessage.setData(2);
                    returnMessage.setMessage("您暂无可退订的容量套餐服务");
                }
            } else if ("3".equalsIgnoreCase(lx)){
                List<TbWebpPay> hylist = tbWebpPayDAO.findUnPurchaseHY(PhoneNo);
                if (hylist.size() > 0) {
                    returnMessage.setCode(0);
                    returnMessage.setData(SysConstants.WAP_BU_HY + PhoneNo + "&openId=" + openId 
                        + "&lx="+ lx + "&wabp_result=666");
                    returnMessage.setMessage("您有可退订的会员套餐服务");
                } else {
                    returnMessage.setCode(2);
                    returnMessage.setData(2);
                    returnMessage.setMessage("您暂无可退订的会员套餐服务");
                }
            }
        }
        return returnMessage;
    }

    @Override
    public ReturnMessage getUnPurchaseOrder(String openId, String PhoneNo, String inputTime,
            String sign, String lx) {
        returnMessage = new ReturnMessage();
        String preSignStr =
                "phone=" + PhoneNo + "&inputTime=" + inputTime + "&key=" + SysConstants.REQ_KEY;
        String signStr = MD5Util.MD5Encode(preSignStr, null).toUpperCase();
        if (!signStr.equals(sign)) {
            returnMessage.setCode(1);
            returnMessage.setData(1);
            returnMessage.setMessage("订单退订请求校验失败！");
        } else {
            if ("1".equalsIgnoreCase(lx)) {
                TbWebpPayDAO tbWebpPayDAO =
                        (TbWebpPayDAO) ContextLoader.getCurrentWebApplicationContext().getBean(
                                "TbWebpPayDAO");
                List<TbWebpPay> dlist = tbWebpPayDAO.findUnPurchaseOrderDetailKJ(PhoneNo);
                if (dlist.size() > 0) {
                    returnMessage.setCode(0);
                    returnMessage.setData(dlist);
                    returnMessage.setMessage("获取退订数据成功");
                } else {
                    returnMessage.setCode(2);
                    returnMessage.setData(2);
                    returnMessage.setMessage("获取退订数据失败，请检查网络");
                }
            } else if("3".equalsIgnoreCase(lx)){
                TbWebpPayDAO tbWebpPayDAO =
                        (TbWebpPayDAO) ContextLoader.getCurrentWebApplicationContext().getBean(
                                "TbWebpPayDAO");
                List<TbWebpPay> dlist = tbWebpPayDAO.findUnPurchaseOrderDetailHY(PhoneNo);
                if (dlist.size() > 0) {
                    returnMessage.setCode(0);
                    returnMessage.setData(dlist);
                    returnMessage.setMessage("获取退订数据成功");
                } else {
                    returnMessage.setCode(2);
                    returnMessage.setData(2);
                    returnMessage.setMessage("获取退订数据失败，请检查网络");
                }
            }
        }
        return returnMessage;
    }
    
}
