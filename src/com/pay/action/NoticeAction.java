package com.pay.action;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;

import com.pay.service.IPayOrderService;
import com.pay.util.StringUtil;


public class NoticeAction extends BaseJsonAction {

    @Inject
    private IPayOrderService payOrderServiceImpl;


    /**
     * 
     * @Description: TODO 券、卡、票展示
     * @param
     * @return void
     * @throws
     * @author wujintao
     * @date 2016-9-19
     */
    public void getTicket() {

        try {
            this.getRequest().setCharacterEncoding("utf-8");
            String phone = this.getRequest().getParameter("phone");
            String openId = this.getRequest().getParameter("openId");
            String tcId = this.getRequest().getParameter("tcId");
            String inputTime = this.getRequest().getParameter("inputTime");
            String sign = this.getRequest().getParameter("sign");
            if (StringUtil.isEmpty(phone) || StringUtil.isEmpty(openId) || StringUtil.isEmpty(tcId)
                    || StringUtil.isEmpty(inputTime) || StringUtil.isEmpty(sign)) {
                returnMessage.setCode(-1);
                returnMessage.setData(-1);
                returnMessage.setMessage("获取套餐列表请求参数不能为空！");
            } else {
                returnMessage = payOrderServiceImpl.getTicket(phone, openId, tcId,inputTime, sign);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        outJson(returnMessage);
    }
    
    /**
     * 
     * @Description: TODO 流量赠送活动每天首单支付成功提示校验接口
     * @param    
     * @return void  
     * @throws
     * @author wujintao
     * @date 2016-10-12
     */
    public void checkUFO() {
        try {
            this.getRequest().setCharacterEncoding("utf-8");
            String phone = this.getRequest().getParameter("phone");
            String openId = this.getRequest().getParameter("openId");
            String inputTime = this.getRequest().getParameter("inputTime");
            String sign = this.getRequest().getParameter("sign");
            if (StringUtil.isEmpty(phone) || StringUtil.isEmpty(openId)
                    || StringUtil.isEmpty(inputTime) || StringUtil.isEmpty(sign)) {
                returnMessage.setCode(-1);
                returnMessage.setData(-1);
                returnMessage.setMessage("获取套餐列表请求参数不能为空！");
            } else {
                returnMessage = payOrderServiceImpl.getIsUFO(phone, openId, inputTime, sign);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        outJson(returnMessage);
    }

}
