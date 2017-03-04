package com.pay.service;

import java.util.HashMap;


import com.pay.model.TbPay;

public interface IReportService {
    

    /**
     * 
     * @Description: TODO 生成每日报表
     * @param    
     * @return void  
     * @throws
     * @author wujintao
     * @date 2016-9-13
     */
    void productReort();
    
    
    void pvuv(String realIp, String phone, String from, String channel);
}
