package com.pay.util;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	public static String getUUID(){    
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");    
        return uuid;    
    }    
	
	public static boolean isEmpty(String str){    
		if(str!=null && !str.equalsIgnoreCase("")){
			return false;
		}else{
			return true;
		}
	}    
	
	   /**
     * 校验字符串是否是大于0的数字
     * @param string
     * @return
     */
    public static boolean isNum(String string){
        Pattern pattern = Pattern.compile("[1-9]{1}\\d*");
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }
}
