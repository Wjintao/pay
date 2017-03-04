package com.pay.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.pay.model.ReturnMessage;
import com.pay.service.IWabpCxlService;
import com.pay.service.WabpCaiXinLing;

@Repository("wabpCxlServiceImpl")
public class WabpCxlServiceImpl implements IWabpCxlService{
    private static final Logger logger = LoggerFactory.getLogger(WabpCxlServiceImpl.class);
    private ReturnMessage returnMessage = null;
    
    @Override
    public ReturnMessage getResultQR(String result) {
        returnMessage = new ReturnMessage();
        WabpCaiXinLing wabpCaiXinLing = new WabpCaiXinLing();
        String resultRQ=wabpCaiXinLing.submitXmlQR(result);
        if (resultRQ==null || "".equals(resultRQ)) {
            returnMessage.setCode(1);
            returnMessage.setData(1);
            returnMessage.setMessage("彩心灵订单确认接口无返回");
        } else {
            //out.print(new ByteArrayInputStream(noticeStr.getBytes(Charset.forName("GBK"))));
            returnMessage.setCode(0);
            returnMessage.setData("彩心灵订单确认接口成功返回");
            returnMessage.setMessage(resultRQ);
        }
        return returnMessage;
    }

    @Override
    public ReturnMessage getResultTB(String result) {
        returnMessage = new ReturnMessage();
        WabpCaiXinLing wabpCaiXinLing = new WabpCaiXinLing();
        String resultTB=wabpCaiXinLing.submitXmlTB(result);
        if (resultTB==null || "".equals(resultTB)) {
            returnMessage.setCode(1);
            returnMessage.setData(1);
            returnMessage.setMessage("彩心灵订单确认接口无返回");
        } else {
            //out.print(new ByteArrayInputStream(noticeStr.getBytes(Charset.forName("GBK"))));
            returnMessage.setCode(0);
            returnMessage.setData("彩心灵订单确认接口成功返回");
            returnMessage.setMessage(resultTB);
        }
        return returnMessage;
    }

}
