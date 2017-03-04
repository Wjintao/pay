package com.pay.service;

import com.pay.model.ReturnMessage;

public interface IWabpCxlService {

    /**
     * 
     * @Description: TODO 彩心灵请求订单确认接口
     * @param @param result
     * @param @return   
     * @return ReturnMessage  
     * @throws
     * @author wujintao
     * @date 2016-11-18
     */
    ReturnMessage  getResultQR(String result);
    
    /**
     * 
     * @Description: TODO 彩心灵请求同步确认接口
     * @param @param result
     * @param @return   
     * @return ReturnMessage  
     * @throws
     * @author wujintao
     * @date 2016-11-19
     */
    ReturnMessage  getResultTB(String result);
}
