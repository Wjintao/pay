package com.pay.service;


    import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pay.quartz.QuartzJob;
import com.pay.util.DES;
import com.pay.util.ExcelToHtml;
import com.pay.util.UtilFunc;
import com.sun.org.apache.bcel.internal.generic.NEW;
      
    /** 
     * <p> 
     * Title: 使用javamail发送邮件 
     * </p> 
     */  
    public class MailService {
        private static final Logger logger = LoggerFactory.getLogger(MailService.class);
        String to = "";// 收件人  
        String from = "";// 发件人  
        String host = "";// smtp主机  
        String username = "";  
        String password = "";  
        String filename = "";// 附件文件名  
        String subject = "";// 邮件主题  
        String content = "";// 邮件正文  
        String recipientStr="";
        Vector file = new Vector();// 附件文件集合  
        String cc = "";

        public String getCc() {
            return cc;
        }

        public void setCc(String cc) {
            this.cc = cc;
        }
        /** 
         * <br> 
         * 方法说明：默认构造器 <br> 
         * 输入参数： <br> 
         * 返回类型： 
         */  
        public MailService() {  
        }  
      
        /** 
         * <br> 
         * 方法说明：构造器，提供直接的参数传入 <br> 
         * 输入参数： <br> 
         * 返回类型： 
         */  
        public MailService(String to, String from, String smtpServer,  
                String username, String password, String subject, String content) {  
            this.to = to;  
            this.from = from;  
            this.host = smtpServer;  
            this.username = username;  
            this.password = password;  
            this.subject = subject;  
            this.content = content;  
        }  
      
        /** 
         * <br> 
         * 方法说明：设置邮件服务器地址 <br> 
         * 输入参数：String host 邮件服务器地址名称 <br> 
         * 返回类型： 
         */  
        public void setHost(String host) {  
            this.host = host;  
        }  
      
        /** 
         * <br> 
         * 方法说明：设置登录服务器校验密码 <br> 
         * 输入参数： <br> 
         * 返回类型： 
         */  
        public void setPassWord(String pwd) {  
            this.password = pwd;  
        }  
      
        /** 
         * <br> 
         * 方法说明：设置登录服务器校验用户 <br> 
         * 输入参数： <br> 
         * 返回类型： 
         */  
        public void setUserName(String usn) {  
            this.username = usn;  
        }  
      
        /** 
         * <br> 
         * 方法说明：设置邮件发送目的邮箱 <br> 
         * 输入参数： <br> 
         * 返回类型： 
         */  
        public void setTo(String to) {  
            this.to = to;  
        }  
      
        /** 
         * <br> 
         * 方法说明：设置邮件发送源邮箱 <br> 
         * 输入参数： <br> 
         * 返回类型： 
         */  
        public void setFrom(String from) {  
            this.from = from;  
        }  
      
        /** 
         * <br> 
         * 方法说明：设置邮件主题 <br> 
         * 输入参数： <br> 
         * 返回类型： 
         */  
        public void setSubject(String subject) {  
            this.subject = subject;  
        }  
      
        /** 
         * <br> 
         * 方法说明：设置邮件内容 <br> 
         * 输入参数： <br> 
         * 返回类型： 
         */  
        public void setContent(String content) {  
            this.content = content;  
        }  
        
        public void setRecipientStr(String recipientStr) {
            this.recipientStr = recipientStr;
        }

        
        @Override
        public String toString() {
            return "MailService [to=" + to + ", from=" + from + ", host=" + host + ", username="
                    + username + ", password=" + password + ", filename=" + filename + ", subject="
                    + subject + ", content=" + content + ", recipientStr=" + recipientStr
                    + ", file=" + file + ", cc=" + cc + "]";
        }

        /** 
         * <br> 
         * 方法说明：把主题转换为中文 <br> 
         * 输入参数：String strText <br> 
         * 返回类型： 
         */  
        /*public String transferChinese(String strText) {  
            try {  
                strText = MimeUtility.encodeText(new String(strText.getBytes(),  
                        "UTF-8"), "UTF-8", "B");  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
            return strText;  
        }*/  

        public String transferChinese(String strText)
        {
          try
          {
            strText = MimeUtility.encodeText(new String(strText.getBytes(), 
              "GB2312"), "GB2312", "B");
          } catch (Exception e) {
            e.printStackTrace();
          }
          return strText;
        }
        
        /** 
         * <br> 
         * 方法说明：往附件组合中添加附件 <br> 
         * 输入参数： <br> 
         * 返回类型： 
         */  
        public void attachfile(String fname) {  
            file.addElement(fname);  
        }  
      
        /** 
         * <br> 
         * 方法说明：发送邮件 <br> 
         * 输入参数： <br> 
         * 返回类型：boolean 成功为true，反之为false 
         * @throws MessagingException 
         * @throws AddressException 
         * @throws UnsupportedEncodingException 
         */  
        public boolean sendMail() throws AddressException, MessagingException, UnsupportedEncodingException {  
      
            // 构造mail session  
            logger.info("start send email..."); 
            Properties props = new Properties() ;  
            props.put("mail.smtp.host", this.host);  
            props.put("mail.smtp.auth", "true");  
            Session session = Session.getDefaultInstance(props,  
                    new Authenticator() {  
                        public PasswordAuthentication getPasswordAuthentication() {  
                            return new PasswordAuthentication(MailService.this.username, MailService.this.password);  
                        }  
                    });  
            //Session session = Session.getDefaultInstance(props);  
//          Session session = Session.getDefaultInstance(props, null);  
      
            try {  
                // 构造MimeMessage 并设定基本的值  
                MimeMessage msg = new MimeMessage(session);  
                //MimeMessage msg = new MimeMessage();  
                msg.setFrom(new InternetAddress(this.from));  
               
                if (this.cc.trim().length() > 1) {
                    if (this.cc.indexOf(";") > 0) {
                        String ary[] = this.cc.split(";");
                        for (int i = 0; i < ary.length; i++) {
                            msg.addRecipients(Message.RecipientType.CC, InternetAddress.parse(ary[i]));
                        }
                    } else
                        msg.addRecipients(Message.RecipientType.CC, InternetAddress.parse(this.cc));
                }


                if (this.recipientStr.trim().length() > 1) {
                    if (this.recipientStr.indexOf(";") > 0) {
                        String ary[] = this.recipientStr.split(";");
                        for (int i = 0; i < ary.length; i++) {
                            msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(ary[i]));
                        }
                    } else
                        msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientStr));
                }
                //msg.addRecipients(Message.RecipientType.TO, address); //这个只能是给一个人发送email  
                //msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(to)) ;  
                this.subject = transferChinese(this.subject);  
                msg.setSubject(this.subject,"text/html;charset=UTF-8");  
      
                // 构造Multipart  
                Multipart mp = new MimeMultipart();  
      
                // 向Multipart添加正文  
                MimeBodyPart mbpContent = new MimeBodyPart();  
                mbpContent.setContent(this.content, "text/html;charset=UTF-8");  
                  
                // 向MimeMessage添加（Multipart代表正文）  
                mp.addBodyPart(mbpContent);  
      
                // 向Multipart添加附件  
                Enumeration efile = this.file.elements();  
                while (efile.hasMoreElements()) {  
      
                    MimeBodyPart mbpFile = new MimeBodyPart();  
                    this.filename = efile.nextElement().toString();  
                    FileDataSource fds = new FileDataSource(this.filename);  
                    mbpFile.setDataHandler(new DataHandler(fds));  
                    //这个方法可以解决附件乱码问题。
                    String filename= new String(fds.getName().getBytes(),"ISO-8859-1");  
      
                    mbpFile.setFileName(filename);  
                    // 向MimeMessage添加（Multipart代表附件）  
                    mp.addBodyPart(mbpFile);  
      
                }  
      
                this.file.removeAllElements();  
                // 向Multipart添加MimeMessage  
                msg.setContent(mp);  
                msg.setSentDate(new Date());  
                msg.saveChanges() ;  
                // 发送邮件  
                  
                Transport transport = session.getTransport("smtp");  
                transport.connect(this.host, this.username, this.password);  
                transport.sendMessage(msg, msg.getAllRecipients());  
                transport.close();  
            } catch (Exception mex) {  
                mex.printStackTrace();  
                logger.info("sendmail exception:", mex);
                return false;  
            }  
                return true;     
        }  
      
       
          
        /** 
         * <br> 
         * 方法说明：主方法，用于测试 <br> 
         * 输入参数： <br> 
         * 返回类型： 
         * @throws Exception 
         */  
        public static void main(String[] args) throws Exception {  
            MailService sendmail = new MailService();
            ExcelToHtml excelToHtml=new ExcelToHtml();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowTimeString=dateFormat.format(cal.getTime());
            String nowTimeString1=dateFormat2.format(cal.getTime());
            String nowTimeString2=dateFormat2.format(new Date());
            sendmail.setUserName(UtilFunc.getProp("n")); 
            sendmail.setPassWord(DES.decrypt((UtilFunc.getProp("p")), "htxl~1ok"));
            sendmail.setHost(UtilFunc.getProp("h"));  
            sendmail.setRecipientStr(UtilFunc.getProp("t"));
            sendmail.setCc(UtilFunc.getProp("c"));
            sendmail.setFrom(UtilFunc.getProp("f"));  
            sendmail.setSubject(nowTimeString+UtilFunc.getProp("s"));  
            sendmail.setContent(nowTimeString1+"~"+nowTimeString2+UtilFunc.getProp("s")+excelToHtml.read());  
            sendmail.attachfile("C:\\ReportYunPay\\空间购买2016-09-13.xls");  
//            sendmail.attachfile(UtilFunc.getProp"c:\\export_2014-01-01 000000_2019-01-01 000000.xls");   

      
        }  
 
}
