package com.pay.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import TestDes.NetDes;

import com.pay.constants.SysConstants;
import com.pay.service.IWabpPayOrderService;
import com.pay.util.DES;
import com.pay.util.DesHuiYuan;
import com.pay.util.MD5Util;
import com.pay.util.SendRequest;
import com.pay.util.StringUtil;
import com.zte.weixin.utils.AES;

public class PageAction extends BaseJsonAction {
    private static final Logger logger = LoggerFactory.getLogger(PageAction.class);

    @Inject
    private IWabpPayOrderService wabpPayOrderServiceImpl;

    /**
     * 微信公众号空间扩容菜单入口
     * 
     * @return
     */
    public String order() {
        try {
            this.getRequest().setCharacterEncoding("utf-8");
            String authInfo = this.getRequest().getParameter("authinfo");
            AES aesed = new AES();
            String decryped = aesed.aesDecode(authInfo);
            String[] paramsString = decryped.split("\\$\\$");
            System.out.println("decryped====decryped=" + decryped);
            for (int i = 0; i < paramsString.length; i++) {
                System.out.println(i + "==== paramsString   =" + paramsString[i]);
            }
            this.getRequest().setAttribute("phone", paramsString[0]);
            this.getRequest().setAttribute("openId", paramsString[1]);
            this.getRequest().setAttribute("token", paramsString[3]);
            // // this.getRequest().setAttribute("phone", "15820712910");
            // this.getRequest().setAttribute("phone", "13802880716");
            // // this.getRequest().setAttribute("openId", "oMPl0s8GOB_fJr2tnhkycNUiMKf8");
            // this.getRequest().setAttribute("openId", "oMPl0s4F0ToWoWDJPWG2dYNt2DQo");
            // this.getRequest().setAttribute("token", "aaaa");
            // this.getRequest().setAttribute("xsm", this.getRequest().getParameter("xsm"));
        } catch (UnsupportedEncodingException ue) {
            ue.printStackTrace();
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    /**
     * 套餐详情页
     * 
     * @return
     */
    public String introduce() {
        return "success";
    }

    /**
     * 套餐详情页
     * 
     * @return
     */
    public String introcsl() {
        return "success";
    }

    /**
     * WEB主页面
     * 
     * @return
     */
    public String toWeb() {
        String phone = this.getRequest().getParameter("account");
        if (phone != null && !phone.equalsIgnoreCase("")) {
            if (isMobileNO(phone)) {
                this.getRequest().setAttribute("phone", phone);
                return "success";
            } else {
                return "error";
            }
        } else {
            return "error";
        }
    }

    /**
     * WAP主页面
     * 
     * @return
     */
    public String toWap() {
        String phone = this.getRequest().getParameter("account");
        String openId = this.getRequest().getParameter("openId");
        String tcId = this.getRequest().getParameter("tcId");
        String wabp_result = this.getRequest().getParameter("wabp_result");
        if ( phone != null && !phone.equalsIgnoreCase("")) {
            if (("000").equals(wabp_result) && isMobileNO(phone)) {
                this.getRequest().setAttribute("phone", phone);
                this.getRequest().setAttribute("openId", openId);
                this.getRequest().setAttribute("tcId", tcId);
                this.getRequest().setAttribute("from", "WAP");
                this.getRequest().setAttribute("liuliang", "OK");
                return "success";
            }else if (isMobileNO(phone)) {
                this.getRequest().setAttribute("phone", phone);
                this.getRequest().setAttribute("openId", openId);
                this.getRequest().setAttribute("from", "WAP");
                return "success";
            } else {
                return "error";
            }
        } else {
            return "error";
        }
    }

    /**
     * 
     * @Description: TODO WAP洗车跳转接口
     * @param @return
     * @param @throws Exception
     * @return String
     * @throws
     * @author wujintao
     * @date 2016-9-6
     */
    public String toWapXiChe() throws Exception {
        String phone = this.getRequest().getParameter("account");
        String openId = this.getRequest().getParameter("openId");
        String tcId = this.getRequest().getParameter("tcId");
        String lx = this.getRequest().getParameter("lx");
        String wabp_result = this.getRequest().getParameter("wabp_result");
        if (("000").equals(wabp_result) && phone != null && !phone.equalsIgnoreCase("")) {
            this.getRequest().setAttribute("phone", phone);
            this.getRequest().setAttribute("openId", openId);
            this.getRequest().setAttribute("tcId", tcId);
            this.getRequest().setAttribute("from", "WAP");
            this.getRequest().setAttribute("liuliang", "OK");
            this.getRequest().setAttribute("tctype", "HY");
            String desPhone =
                    DES.toHexString(
                            DES.encrypt(
                                    (phone + "#" + DES.formatDate(new Date(), "yyyyMMddHHmmss")),
                                    "htxl~1ok")).toUpperCase();
            String returnUrl =
                    URLEncoder.encode(SysConstants.WAP_BU + phone + "&openId=" + openId, "utf-8");
            //this.getResponse().sendRedirect(SysConstants.XICHE_BU + desPhone + "&returnUrl=" + returnUrl);
            //return null;
            String toXiCheUrl=SysConstants.XICHE_BU + desPhone + "&returnUrl=" + returnUrl;
            this.getRequest().setAttribute("toXiCheUrl", toXiCheUrl);
            return "wapmain";
        } else if (("111").equals(wabp_result) && phone != null && !phone.equalsIgnoreCase("")) {
            this.getRequest().setAttribute("phone", phone);
            this.getRequest().setAttribute("openId", openId);
            this.getRequest().setAttribute("from", "WAP");
            //this.getRequest().setAttribute("liuliang", "OK");
            //this.getRequest().setAttribute("tctype", "HY");
            String desPhone =
                    DES.toHexString(
                            DES.encrypt(
                                    (phone + "#" + DES.formatDate(new Date(), "yyyyMMddHHmmss")),
                                    "htxl~1ok")).toUpperCase();
            String returnUrl =
                    URLEncoder.encode(SysConstants.WAP_BU + phone + "&openId=" + openId, "utf-8");
            System.out.println(SysConstants.XICHE_BU + desPhone + "&returnUrl=" + returnUrl);
            this.getResponse().sendRedirect(SysConstants.XICHE_BU + desPhone + "&returnUrl=" + returnUrl);
            return null;
        }else if (("666").equals(wabp_result) && phone != null && !phone.equalsIgnoreCase("")) {
            this.getRequest().setAttribute("phone", phone);
            this.getRequest().setAttribute("openId", openId);
            this.getRequest().setAttribute("lx", lx);
            this.getRequest().setAttribute("mysign", 1);
            this.getRequest().setAttribute("from", "WAP");
            return "waptuiding";
        } else if (("222").equals(wabp_result) && phone != null && !phone.equalsIgnoreCase("")) {
            this.getRequest().setAttribute("phone", phone);
            this.getRequest().setAttribute("openId", openId);
            this.getRequest().setAttribute("lx", lx);
            this.getRequest().setAttribute("mysign", 1);
            this.getRequest().setAttribute("from", "WAP");
            return "waphuiyuan";
        } else if (("333").equals(wabp_result) && phone != null && !phone.equalsIgnoreCase("")) {
            this.getRequest().setAttribute("phone", phone);
            this.getRequest().setAttribute("openId", openId);
            this.getRequest().setAttribute("from", "WAP");
            String HuiYuanP =DesHuiYuan.jiaMi(phone+"#hecaiyun#"+SysConstants.WAP_BU + phone + "&openId=" + openId);
            System.out.println("HuiYuanP=" + HuiYuanP.toString());
            this.getResponse().sendRedirect(SysConstants.HUIYUAN_BU + HuiYuanP.toString());
            return null;
        } else {
            if (phone != null && !phone.equalsIgnoreCase("")) {
                if (isMobileNO(phone)) {
                    this.getRequest().setAttribute("phone", phone);
                    this.getRequest().setAttribute("openId", openId);
                    this.getRequest().setAttribute("from", "WAP");
                    return "wapmain";
                } else {
                    return "error";
                }
            } else {
                return "error";
            }
        }

    }

    /**
     * 
     * @Description: TODO营销渠道跳转营销WAP页面
     * @param @return
     * @param @throws Exception   
     * @return String  
     * @throws
     * @author wujintao
     * @date 2016-10-18
     */
    public String toMarketPageWap() throws Exception {
        String channel = this.getRequest().getParameter("channel");
        Integer channelInteger = Integer.valueOf(channel);
        String phone = this.getRequest().getParameter("phone");
        String openId = this.getRequest().getParameter("openId");
        String wabp_result = this.getRequest().getParameter("wabp_result");
        if (("000").equals(wabp_result) && channel != null) {
            this.getRequest().setAttribute("channel", channel);
            this.getRequest().setAttribute("openId", openId);
            this.getRequest().setAttribute("from", "WAP");
            this.getRequest().setAttribute("phone", phone);
            this.getRequest().setAttribute("liuliang", "OK");
            return "jiangsu";
        }else if (channel != null && !channel.equalsIgnoreCase("")&&(channelInteger<100)) {
            this.getRequest().setAttribute("channel", channel);
            this.getRequest().setAttribute("openId", openId);
            this.getRequest().setAttribute("from", "WAP");
            this.getRequest().setAttribute("phone", phone);
            return "success";
        } else if(channel != null && !channel.equalsIgnoreCase("")&&(channelInteger>=100)&&(channelInteger<200)) {
            this.getRequest().setAttribute("channel", channel);
            this.getRequest().setAttribute("openId", openId);
            this.getRequest().setAttribute("from", "WAP");
            this.getRequest().setAttribute("phone", phone);
            return "jiangsu";
        } else{
                return "error";
        }
    }
    
    /**
     * 
     * @Description: TODO营销渠道跳转营销WEB页面
     * @param @return
     * @param @throws Exception   
     * @return String  
     * @throws
     * @author wujintao
     * @date 2016-10-18
     */
    public String toMarketPageWeb() throws Exception {
        String channel = this.getRequest().getParameter("channel");
        Integer channelInteger = Integer.valueOf(channel);
        String phone = this.getRequest().getParameter("phone");
        if ( channel != null && !channel.equalsIgnoreCase("")&&(channelInteger<100)) {
            this.getRequest().setAttribute("channel", channel);
            this.getRequest().setAttribute("from", "WEB");
            this.getRequest().setAttribute("phone", phone);
            return "success";
        } else if ( channel != null && !channel.equalsIgnoreCase("")&&(channelInteger>=100)&&(channelInteger<200)) {
            this.getRequest().setAttribute("channel", channel);
            this.getRequest().setAttribute("from", "WEB");
            this.getRequest().setAttribute("phone", phone);
            return "jiangsu";
        } else {
                return "error";
        }
    }
    
    /**
     * 
     * @Description: TODO营销渠道跳转营销洗车页面
     * @param @return
     * @param @throws Exception   
     * @return String  
     * @throws
     * @author wujintao
     * @date 2016-10-18
     */
    public String toMarketPageWxc() throws Exception {
        String channel = this.getRequest().getParameter("channel");
        Integer channelInteger = Integer.valueOf(channel);
        String phone = this.getRequest().getParameter("phone");
        String openId = this.getRequest().getParameter("openId");
        String wabp_result = this.getRequest().getParameter("wabp_result");
        if (wabp_result == null && channel != null && !channel.equalsIgnoreCase("")&&(channelInteger<1000)){
            this.getRequest().setAttribute("channel", channel);
            this.getRequest().setAttribute("from", "WAP");
            this.getRequest().setAttribute("phone", phone);
            return "success";
        } else if( ("000").equals(wabp_result) && phone != null && !phone.equalsIgnoreCase("")&&channel != null && !channel.equalsIgnoreCase("")&&(channelInteger<100)) {
            this.getRequest().setAttribute("phone", phone);
            this.getRequest().setAttribute("openId", openId);
            this.getRequest().setAttribute("from", "WAP");
            this.getRequest().setAttribute("channel", channel);
            String desPhone =
                    DES.toHexString(
                            DES.encrypt(
                                    (phone + "#" + DES.formatDate(new Date(), "yyyyMMddHHmmss")),
                                    "htxl~1ok")).toUpperCase();
            String returnUrl =
                    URLEncoder.encode(SysConstants.WAP_TG_HY +channel+ "&phone=" +  phone + "&openId=" + openId, "utf-8");
            this.getResponse().sendRedirect(
                    SysConstants.XICHE_BU + desPhone + "&returnUrl=" + returnUrl);
            return null;
        }else {
            return "error";
        }
    }
    
    
    
    /**
     * 跳转到订单详情页
     * 
     * @return
     */
    public String toSure() {
        try {
            this.getRequest().setCharacterEncoding("utf-8");
            String tcId = this.getRequest().getParameter("tcId");
            String lx = this.getRequest().getParameter("lx");
            String phone = this.getRequest().getParameter("phone");
            String name = this.getRequest().getParameter("name");
            String space = this.getRequest().getParameter("space");
            String price = this.getRequest().getParameter("price");
            String flag = this.getRequest().getParameter("flag");
            String sin = this.getRequest().getParameter("sin");
            String openId = this.getRequest().getParameter("openId");
            String channel = this.getRequest().getParameter("channel");
            if (!StringUtil.isEmpty(tcId) && !StringUtil.isEmpty(lx) && !StringUtil.isEmpty(phone)
                    && !StringUtil.isEmpty(name) && !StringUtil.isEmpty(space)
                    && !StringUtil.isEmpty(price) && !StringUtil.isEmpty(flag)) {
                this.getRequest().setAttribute("tcId", tcId);
                this.getRequest().setAttribute("lx", lx);
                this.getRequest().setAttribute("phone", phone);
                this.getRequest().setAttribute("name", name);
                this.getRequest().setAttribute("space", space);
                this.getRequest().setAttribute("price", price);
                this.getRequest().setAttribute("openId", openId);
                if (!StringUtil.isEmpty(channel)) {
                    this.getRequest().setAttribute("channel", channel);
                } else {
                    this.getRequest().setAttribute("channel", "内部渠道");
                }
                if ("".equals(sin)) {
                    this.getRequest().setAttribute("sin", 5);
                } else if (!"2".equals(getMobileType(phone))) {
                    this.getRequest().setAttribute("sin", 6);
                } else {
                    this.getRequest().setAttribute("sin", sin);
                }
                this.getRequest().setAttribute("mysign", 1);
                if ("web".equalsIgnoreCase(flag)) {
                    return "webpay";
                } else {
                    return "wappay";
                }
            } else {
                return "error";
            }
        } catch (UnsupportedEncodingException ue) {
            ue.printStackTrace();
            return "error";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        // return "success";
    }

    private String getMobileType(String mobile) {
        if (mobile.startsWith("0") || mobile.startsWith("+860")) {
            mobile = mobile.substring(mobile.indexOf("0") + 1, mobile.length());
        }
        List chinaUnicom =
                Arrays.asList(new String[] {"130", "131", "132", "145", "155", "156", "186", "185"});
        List chinaMobile1 =
                Arrays.asList(new String[] {"135", "136", "137", "138", "139", "147", "150", "151",
                        "152", "157", "158", "159", "178", "182", "183", "184", "187", "188"});
        List chinaMobile2 =
                Arrays.asList(new String[] {"1340", "1341", "1342", "1343", "1344", "1345", "1346",
                        "1347", "1348"});

        boolean bolChinaUnicom = (chinaUnicom.contains(mobile.substring(0, 3)));
        boolean bolChinaMobile1 = (chinaMobile1.contains(mobile.substring(0, 3)));
        boolean bolChinaMobile2 = (chinaMobile2.contains(mobile.substring(0, 4)));
        if (bolChinaUnicom) {
            return "1";// 联通
        }
        if (bolChinaMobile1 || bolChinaMobile2) {
            return "2"; // 移动
        }
        return "3"; // 其他为电信
    }

    private boolean isMobileNO(String mobile) {
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobile);
        return m.matches();
    }


    /**
     * 
     * @Description: TODO 退订页面跳转
     * @param @return
     * @param @throws Exception
     * @return String
     * @throws
     * @author wujintao
     * @date 2016-9-26
     */
    public void toUnpurchase() throws Exception {
        String phone = this.getRequest().getParameter("phone");
        String openId = this.getRequest().getParameter("openId");
        String lx = this.getRequest().getParameter("lx");
        String inputTime = this.getRequest().getParameter("inputTime");
        String sign = this.getRequest().getParameter("sign");
        if (StringUtil.isEmpty(openId) || StringUtil.isEmpty(phone) || StringUtil.isEmpty(lx)
                || StringUtil.isEmpty(inputTime) || StringUtil.isEmpty(sign)) {
            returnMessage.setCode(-1);
            returnMessage.setData(-1);
            returnMessage.setMessage("退订请求参数不能为空！");
        } else {
            returnMessage = wabpPayOrderServiceImpl.getUnPurchase(openId, phone, inputTime, sign, lx);
        }
        outJson(returnMessage);
    }
    
    public void getHfllcx() throws IOException  {
        String phone = this.getRequest().getParameter("phone");
        String inputTime = this.getRequest().getParameter("inputTime");
        String sign = this.getRequest().getParameter("sign");
        if (StringUtil.isEmpty(phone)
                || StringUtil.isEmpty(inputTime) || StringUtil.isEmpty(sign)) {
            returnMessage.setCode(-1);
            returnMessage.setData(-1);
            returnMessage.setMessage("退订请求参数不能为空！");
        } else {
            String userkey=MD5Util.MD5Encode(phone+"BGfOQwRAazMhLHmj3etZ1d6AzYLDww", null);
            String url="http://cz.umeol.com:6090/dm/v/cz/qureyFlowPhonecost.do";
            String data="mobile="+phone+"&type=1&userkey="+userkey;
            String hfye=SendRequest.Post(url, data, 10000);
            data="mobile="+phone+"&type=2&userkey="+userkey;
            String syll=SendRequest.Post(url, data, 10000);
            returnMessage.setCode(0);
            returnMessage.setData(hfye);
            returnMessage.setMessage(syll);
        }
        outJson(returnMessage);
    }

}
