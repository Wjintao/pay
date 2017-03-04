package com.pay.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.pay.activemq.MessageSender;
import com.pay.dao.TbPvuvDAO;
import com.pay.dao.TbTcdetailDAO;
import com.pay.dao.TbTctypeDAO;
import com.pay.dao.TbWebpPayDAO;
import com.pay.dao.TbWxpayDAO;
import com.pay.model.TbPvuv;
import com.pay.model.TbTcdetail;
import com.pay.model.TbWebpPay;
import com.pay.model.TbWxpay;
import com.pay.quartz.QuartzJob;
import com.pay.service.IReportService;
import com.pay.util.StringUtil;


@Transactional
@Repository("reportServiceImpl")
public class ReportServiceImpl implements IReportService {
    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Autowired
    TbWebpPayDAO tbWebpPayDAO;
    @Autowired
    TbWxpayDAO tbWxpayDAO;
    @Autowired
    TbTcdetailDAO tbTcdetailDAO;
    @Autowired
    TbTctypeDAO tbTctypeDAO;
    @Autowired
    TbPvuvDAO tbPvuvDAO;
    @Autowired
    MessageSender messageSender;
  

    @Override

    public void productReort() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowTimeString=dateFormat.format(cal.getTime());
            //System.err.println(nowTimeString);
            // 获取query对象
            List listWe= tbTcdetailDAO.findByTctypeRelId("123qwer");
            List listWc= tbTcdetailDAO.findByTctypeRelId("123asdf");
            List listWh= tbTcdetailDAO.findByTctypeRelId("123zxcv");
            String tcString=(listWe.toString().replace(",", "','").replace("[", "'").replace("]", "'").replace(" ", ""));
            String csString=(listWc.toString().replace(",", "','").replace("[", "'").replace("]", "'").replace(" ", ""));
            String hyString=(listWh.toString().replace(",", "','").replace("[", "'").replace("]", "'").replace(" ", ""));
            logger.info(tcString);
            logger.info(csString);
            logger.info(hyString);
            List<TbWebpPay> listWebPay= tbWebpPayDAO.findPayedKJ(nowTimeString,tcString);
            List<TbWebpPay> listWebPaycs= tbWebpPayDAO.findPayedKJ(nowTimeString,csString);
            List<TbWebpPay> listWebPayhy= tbWebpPayDAO.findPayedKJHY(nowTimeString,hyString);
            //System.out.println(listWebPay.toString());
            List<TbWxpay> listWxPay=tbWxpayDAO.findPayedKJ(nowTimeString,tcString);
            List<TbWxpay> listWxPaycs=tbWxpayDAO.findPayedKJ(nowTimeString,csString);
            String[] title = {"手机号", "支付方式", "商户内部订单号", "套餐名称", "套餐价格(分)", "套餐容量", "下单时间","营销","渠道","渠道细分"};
            String[] titlehy = {"手机号", "支付方式", "商户内部订单号", "套餐名称", "套餐价格(分)", "套餐容量", "下单时间","营销","渠道","订购类型","渠道细分"};
            // 创建Excel工作簿
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 创建一个工作表sheet
            HSSFSheet sheet = workbook.createSheet("空间购买话费支付");
            HSSFSheet sheetwx = workbook.createSheet("空间购买微信支付"); 
            HSSFSheet sheetcs = workbook.createSheet("传输量购买话费支付");
            HSSFSheet sheetcswx = workbook.createSheet("传输量购买微信支付"); 
            HSSFSheet sheethy = workbook.createSheet("会员购买话费支付");
            // 创建第一行
            HSSFRow row = sheet.createRow(0);
            HSSFRow rowwx = sheetwx.createRow(0);
            HSSFRow rowcs = sheetcs.createRow(0);
            HSSFRow rowcswx = sheetcswx.createRow(0);
            HSSFRow rowhy = sheethy.createRow(0);
            HSSFCell cell = null;
            HSSFCell cellwx = null;
            HSSFCell cellcs = null;
            HSSFCell cellcswx = null;
            HSSFCell cellhy = null;
            // 插入第一行数据 id,name,sex
            for (int i = 0; i < title.length; i++) {
                cellwx = rowwx.createCell(i);
                cellwx.setCellValue(title[i]);
                cellcswx = rowcswx.createCell(i);
                cellcswx.setCellValue(title[i]);
            }
            for (int i = 0; i < titlehy.length; i++) {
                cell = row.createCell(i);
                cell.setCellValue(titlehy[i]);
                cellcs = rowcs.createCell(i);
                cellcs.setCellValue(titlehy[i]);
                cellhy = rowhy.createCell(i);
                cellhy.setCellValue(titlehy[i]);
            }
            // 追加空间购买数据
            for (int i = 0; i<=(listWebPay.size()-1); i++) {
                HSSFRow nextrow = sheet.createRow(i+1);
                HSSFCell cell2 = nextrow.createCell(0);
                cell2.setCellValue(listWebPay.get(i).getPhone());
                cell2 = nextrow.createCell(1);
                cell2.setCellValue("话费支付");
                cell2 = nextrow.createCell(2);
                cell2.setCellValue(listWebPay.get(i).getAptrid());
                cell2 = nextrow.createCell(3);
                TbTcdetail tbTcdetail=tbTcdetailDAO.findById(listWebPay.get(i).getTcdetailRelId());
                cell2.setCellValue(tbTcdetail.getTcname());
                cell2 = nextrow.createCell(4);
                cell2.setCellValue(tbTcdetail.getTcpriceNow());
                cell2 = nextrow.createCell(5);
                cell2.setCellValue(tbTcdetail.getTcpace());
                cell2 = nextrow.createCell(6);
                if ("新用户订购".equalsIgnoreCase(listWebPay.get(i).getRemark())) {
                    cell2.setCellValue(dateFormat.format(listWebPay.get(i).getUpdateTime()));
                } else {
                    cell2.setCellValue(dateFormat.format(listWebPay.get(i).getCreateTime()));
                }
                cell2 = nextrow.createCell(7);
                if (tbTcdetail.getActype().equalsIgnoreCase("01")) {
                    cell2.setCellValue("活动");
                } else {
                    cell2.setCellValue("正常");
                }
                cell2 = nextrow.createCell(8);
                cell2.setCellValue(listWebPay.get(i).getQuDao());
                cell2 = nextrow.createCell(9);
                cell2.setCellValue(listWebPay.get(i).getRemark());
                cell2 = nextrow.createCell(10);
                cell2.setCellValue(listWebPay.get(i).getSin());
            }
            
            for (int i = 0; i<=(listWxPay.size()-1); i++) {
                HSSFRow nextrow = sheetwx.createRow(i+1);
                HSSFCell cell2 = nextrow.createCell(0);
                cell2.setCellValue(listWxPay.get(i).getPhone());
                cell2 = nextrow.createCell(1);
                cell2.setCellValue("微信支付");
                cell2 = nextrow.createCell(2);
                cell2.setCellValue(listWxPay.get(i).getOutTradeNo());
                cell2 = nextrow.createCell(3);
                TbTcdetail tbTcdetail=tbTcdetailDAO.findById(listWxPay.get(i).getTcdetailRelId());
                cell2.setCellValue(tbTcdetail.getTcname());
                cell2 = nextrow.createCell(4);
                cell2.setCellValue(tbTcdetail.getTcpriceNow());
                cell2 = nextrow.createCell(5);
                cell2.setCellValue(tbTcdetail.getTcpace());
                cell2 = nextrow.createCell(6);
                cell2.setCellValue(dateFormat.format(listWxPay.get(i).getCreateTime()));
                cell2 = nextrow.createCell(7);
                if (tbTcdetail.getActype().equalsIgnoreCase("01")) {
                    cell2.setCellValue("活动");
                } else {
                    cell2.setCellValue("正常");
                }
                cell2 = nextrow.createCell(8);
                if (listWxPay.get(i).getTradeType().equalsIgnoreCase("NATIVE")) {
                    cell2.setCellValue("WEB");
                } else {
                    cell2.setCellValue("WAP");
                }
                cell2 = nextrow.createCell(9);
                cell2.setCellValue(listWxPay.get(i).getRemark());
            }
            // 追加传输量购买数据
            for (int i = 0; i<=(listWebPaycs.size()-1); i++) {
                HSSFRow nextrow = sheetcs.createRow(i+1);
                HSSFCell cell2 = nextrow.createCell(0);
                cell2.setCellValue(listWebPaycs.get(i).getPhone());
                cell2 = nextrow.createCell(1);
                cell2.setCellValue("话费支付");
                cell2 = nextrow.createCell(2);
                cell2.setCellValue(listWebPaycs.get(i).getAptrid());
                cell2 = nextrow.createCell(3);
                TbTcdetail tbTcdetail=tbTcdetailDAO.findById(listWebPaycs.get(i).getTcdetailRelId());
                cell2.setCellValue(tbTcdetail.getTcname());
                cell2 = nextrow.createCell(4);
                cell2.setCellValue(tbTcdetail.getTcpriceNow());
                cell2 = nextrow.createCell(5);
                cell2.setCellValue(tbTcdetail.getTcpace());
                cell2 = nextrow.createCell(6);
                cell2.setCellValue(dateFormat.format(listWebPay.get(i).getCreateTime()));
                cell2 = nextrow.createCell(7);
                if (tbTcdetail.getActype().equalsIgnoreCase("01")) {
                    cell2.setCellValue("活动");
                } else {
                    cell2.setCellValue("正常");
                }
                cell2 = nextrow.createCell(8);
                cell2.setCellValue(listWebPaycs.get(i).getQuDao());
                cell2 = nextrow.createCell(9);
                cell2.setCellValue(listWebPaycs.get(i).getSin());
            }
            
            for (int i = 0; i<=(listWxPaycs.size()-1); i++) {
                HSSFRow nextrow = sheetcswx.createRow(i+1);
                HSSFCell cell2 = nextrow.createCell(0);
                cell2.setCellValue(listWxPaycs.get(i).getPhone());
                cell2 = nextrow.createCell(1);
                cell2.setCellValue("微信支付");
                cell2 = nextrow.createCell(2);
                cell2.setCellValue(listWxPaycs.get(i).getOutTradeNo());
                cell2 = nextrow.createCell(3);
                TbTcdetail tbTcdetail=tbTcdetailDAO.findById(listWxPaycs.get(i).getTcdetailRelId());
                cell2.setCellValue(tbTcdetail.getTcname());
                cell2 = nextrow.createCell(4);
                cell2.setCellValue(tbTcdetail.getTcpriceNow());
                cell2 = nextrow.createCell(5);
                cell2.setCellValue(tbTcdetail.getTcpace());
                cell2 = nextrow.createCell(6);
                cell2.setCellValue(dateFormat.format(listWxPaycs.get(i).getCreateTime()));
                cell2 = nextrow.createCell(7);
                if (tbTcdetail.getActype().equalsIgnoreCase("01")) {
                    cell2.setCellValue("活动");
                } else {
                    cell2.setCellValue("正常");
                }
                cell2 = nextrow.createCell(8);
                if (listWxPaycs.get(i).getTradeType().equalsIgnoreCase("NATIVE")) {
                    cell2.setCellValue("WEB");
                } else {
                    cell2.setCellValue("WAP");
                }
                cell2 = nextrow.createCell(9);
                cell2.setCellValue(listWxPaycs.get(i).getRemark());
            }
            // 追加会员购买数据
            for (int i = 0; i<=(listWebPayhy.size()-1); i++) {
                HSSFRow nextrow = sheethy.createRow(i+1);
                HSSFCell cell2 = nextrow.createCell(0);
                cell2.setCellValue(listWebPayhy.get(i).getPhone());
                cell2 = nextrow.createCell(1);
                cell2.setCellValue("话费支付");
                cell2 = nextrow.createCell(2);
                cell2.setCellValue(listWebPayhy.get(i).getAptrid());
                cell2 = nextrow.createCell(3);
                TbTcdetail tbTcdetail=tbTcdetailDAO.findById(listWebPayhy.get(i).getTcdetailRelId());
                cell2.setCellValue(tbTcdetail.getTcname());
                cell2 = nextrow.createCell(4);
                cell2.setCellValue(tbTcdetail.getTcpriceNow());
                cell2 = nextrow.createCell(5);
                cell2.setCellValue(tbTcdetail.getTcpace());
                cell2 = nextrow.createCell(6);
                cell2.setCellValue(dateFormat.format(listWebPayhy.get(i).getCreateTime()));
                cell2 = nextrow.createCell(7);
                if (tbTcdetail.getActype().equalsIgnoreCase("01")) {
                    cell2.setCellValue("活动");
                } else {
                    cell2.setCellValue("正常");
                }
                cell2 = nextrow.createCell(8);
                cell2.setCellValue(listWebPayhy.get(i).getQuDao());
                cell2 = nextrow.createCell(9);
                cell2.setCellValue(listWebPayhy.get(i).getRemark());
                cell2 = nextrow.createCell(10);
                cell2.setCellValue(listWebPayhy.get(i).getSin());
            }
            // 创建一个文件
            DateFormat dateFormatFile = new SimpleDateFormat("yyyy-MM-dd");
            String fiString="C:\\ReportYunPay\\"+dateFormatFile.format(cal.getTime())+".xls";
            logger.info(fiString);
            File file = new File(fiString);
            try {               
                file.createNewFile();
                // 将Excel内容存盘
                FileOutputStream stream = FileUtils.openOutputStream(file);
                workbook.write(stream);
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void pvuv(String realIp, String phone, String from, String channel) {
        // TODO Auto-generated method stub
        TbPvuv tbPvuv=new TbPvuv();
        tbPvuv.setChannel(channel);
        tbPvuv.setCreateTime(new Date());
        tbPvuv.setDataStatus(1);
        tbPvuv.setForm(from);
        tbPvuv.setObjId(StringUtil.getUUID());
        tbPvuv.setPhone(phone);
        tbPvuv.setRealIp(realIp);
        tbPvuv.setRemark("");
        tbPvuv.setUpdateTime(new Date());
        messageSender.sendObjectMessage(tbPvuv);
    }
    
    
}
