package com.pay.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 日志工具，将一般日志与错误日志分开
 * @author lurongfu
 *
 */
public class LogUtil {

	/**
	 * 积分接口日志
	 */
	public static final String JF_API = "JfApi";
	/**
	 * 错误日志
	 */
	public static final String Error = "errorlog";
	/**
	 * 一般信息
	 */
	public static final String INFO = "infolog";
	
	private static Map<String,Logger> logMap = new HashMap<String,Logger>();
	
	public static void info(String message){
		Logger log = null;
		if(!logMap.containsKey(LogUtil.INFO)){
			synchronized (logMap) {
				if(!logMap.containsKey(LogUtil.INFO)){
					log = Logger.getLogger(LogUtil.INFO);
					logMap.put(LogUtil.INFO, log);
				}
			}
		}
		log = logMap.get(LogUtil.INFO);
		log.info(message);
	}
	public static void error(String message,Throwable throwable){
		Logger log = null;
		if(!logMap.containsKey(LogUtil.Error)){
			synchronized (logMap) {
				if(!logMap.containsKey(LogUtil.Error)){
					log = Logger.getLogger(LogUtil.Error);
					logMap.put(LogUtil.Error, log);
				}
			}
		}
		log = logMap.get(LogUtil.Error);
		log.error(message,throwable);
	}
}
