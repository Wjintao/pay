package com.pay.util;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.nio.*;
import java.nio.channels.*;
public  class UtilFunc
{

    public static void main(String args[]){
        String title=UtilFunc.getProp("email_title");
        System.out.println("title-->"+title);
    }
 

  public static String getEnvByName(String strName) {
    try {
      //String ret = "";
      Map gp = System.getenv();
      for (Iterator ith = gp.entrySet().iterator(); ith.hasNext(); )
      {
        Map.Entry mp = (Map.Entry)ith.next();
        if ((mp.getKey().toString().equals(strName))) 
            return mp.getValue().toString();
      }
      return "";
    }
    catch (Exception e)
    {
      System.out.println("execption:" + e.getMessage()); }
    return "";
  }

  public static boolean isRegularMatch(String regex,String inCode) { 
      Pattern p1 = Pattern.compile(regex); 
      Matcher m1 = p1.matcher(inCode); 
      boolean rs1 = m1.matches(); 
      return rs1 ; 
  } 

  public static String getNowYear()
  {
      SimpleDateFormat sdf=new SimpleDateFormat("yyyy");   
      return sdf.format(new java.util.Date());  

  }
  public static String getNowMonth()
  {
      SimpleDateFormat sdf=new SimpleDateFormat("MM");   
      return sdf.format(new java.util.Date());  
  }
  public static String getNowDay()
  {
      SimpleDateFormat sdf=new SimpleDateFormat("dd");   
      return sdf.format(new java.util.Date());  
  }
  
  public static String getNowHour()
  {
      SimpleDateFormat sdf=new SimpleDateFormat("HH");   
      return sdf.format(new java.util.Date());  
  }
  public static String getTime()
  {
        Calendar now = Calendar.getInstance();
        int hh = now.get(Calendar.HOUR);
        int mm = now.get(Calendar.MINUTE);
        int ss = now.get(Calendar.SECOND);
        return String.valueOf(hh)+":"+String.valueOf(mm)+":"+String.valueOf(ss);
  }
  public static String vPath(String v) {
    return v.replace("\\", "/");
  }

 

  public static String getProp(String keyname)
  {
    try
    {
//      File f = new File(System.getProperty("user.dir")+File.separator+"build.properties");
      
        File f = new File(System.getProperty("user.dir")+File.separator+"config.properties");
        //File f = new File("./config.properties");
        if (!(f.exists()))
      {
        System.out.println("\t配置文件没有找到!"+f.getAbsolutePath());
        System.exit(1);
      }
      InputStream is = new BufferedInputStream(new FileInputStream(f));
//      BufferedReader bf = new BufferedReader(new InputStreamReader(is));  
    
      Properties prop = new Properties();
//      prop.load(bf);
      prop.load(is);
      if (is != null)
        is.close();
      String tmpF = prop.getProperty(keyname).trim();
//      if(tmpF.contains("$"))
//      {
//        String f1 =tmpF.substring(0,tmpF.indexOf(File.separator));
//        String f2 =f1.substring(1,f1.length());
//        String f3 = tmpF.substring(tmpF.indexOf(File.separator)+1,tmpF.length());
//        String f4 = UtilFunc.getEnvByName(f2.toUpperCase());
//        return f4+File.separator+f3;
//      }
//      else
          return tmpF;
      
    }
    catch (Exception e)
    {
      System.out.println(keyname +"没配置"); 
        return "";
    }
  }
  public static String getFileTime(File f)
  {        
    Calendar cal = Calendar.getInstance();    
    long time = f.lastModified();    
    cal.setTimeInMillis(time); 
    SimpleDateFormat  fs=new  SimpleDateFormat("yyyyMMddHH");  
    return fs.format(cal.getTime());
    
  }
 
  public static boolean FileCpy(String src,String dest)
  {
      try
      {
            System.out.println("Coping "+src +" to "+dest);
          //File dst = new File(dest);
          //if(!dst.exists()) dst.createNewFile();
            // 获取源文件和目标文件的输入输出流   
            FileInputStream fin = new FileInputStream(src);   
            FileOutputStream fout = new FileOutputStream(dest);   
            // 获取输入输出通道   
            FileChannel fcin = fin.getChannel();   
            FileChannel fcout = fout.getChannel();   
            // 创建缓冲区   
            ByteBuffer buffer = ByteBuffer.allocate(1024);   
            while (true) {   
                // clear方法重设缓冲区，使它可以接受读入的数据   
                buffer.clear();   
                // 从输入通道中将数据读到缓冲区   
                int r = fcin.read(buffer);   
                // read方法返回读取的字节数，可能为零，如果该通道已到达流的末尾，则返回-1   
                if (r == -1) {   
                    break;   
                }   
                // flip方法让缓冲区可以将新读入的数据写入另一个通道   
                buffer.flip();   
                // 从输出通道中将数据写入缓冲区   
                fcout.write(buffer);   
            }  
          return true;
      }
      catch(Exception e)
      {
          e.printStackTrace();
          return false;
      }
  }

}