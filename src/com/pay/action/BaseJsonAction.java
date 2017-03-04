package com.pay.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.pay.constants.SysConstants;
import com.pay.model.ReturnMessage;
import com.pay.util.Date2JsonProcessor;
import com.pay.util.LogUtil;


/**输出json格式串必备jar包如下(与struts2配置无关的单独输出)：
 * json-lib-2.3-jdk15.jar（http://commons.apache.org/index.html）
 * commons-httpclient-3.1.jar（http://json-lib.sourceforge.net/）
 * ezmorph-1.0.6.jar（http://ezmorph.sourceforge.net/）
 * commons-lang-2.0.jar
 * commons-logging-1.0.4.jar
 * commons-collections-3.2.jar
 * 共六个，下载地址：http://morph.sourceforge.net/、http://www.docjar.com/
 * 参考：http://www.javaeye.com/topic/295083
 * @author 
 * @email:
 * @version
 */
@SuppressWarnings("serial")
public class BaseJsonAction extends ActionSupport implements Serializable {

	//public static Logger logger  =  Logger.getLogger(BaseJsonAction.class);//Log.getLogger();
	
	/**
	 * json对象转换的自定义配置
	 *
	 */
	private static JsonConfig defConfig = null;
	
	public static JsonConfig getDefConfig() {
		return defConfig;
	}
	public static void setDefConfig(JsonConfig defConfig) {
		BaseJsonAction.defConfig = defConfig;
	}
	/**
	 * 返回客户端的信息
	 */
	protected ReturnMessage returnMessage;
//	protected LoginUser curUser;
	// 用于返回页面的提示
	private static String info = "info";

	static{
		defConfig = new JsonConfig();
		//注册日期类型对象的处理器，其他类型对象的处理也可依此配置
		defConfig.registerJsonValueProcessor(java.util.Date.class,new Date2JsonProcessor());
	}
	public ReturnMessage getReturnMessage() {
		return returnMessage;
	}
	//@Inject
	public void setReturnMessage(ReturnMessage rm) {
		this.returnMessage = rm;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		BaseJsonAction.info = info;
	}

	public BaseJsonAction(){
		returnMessage = new ReturnMessage();
	}
	/**
	 * 获得request
	 * @return
	 */
	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	/**
	 * 获得response对象
	 * @return
	 */
	public HttpServletResponse getResponse() {
		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("text/html; charset=utf-8");
		return resp;
	}
	
	/**
	 * 输出JSON字符串
	 * @param obj
	 */
	public void outJsonString(String str) {
		outString(str);
	}

	/**
	 * 把对象转换成JSON输出
	 * @param obj
	 */
	public void outJson(Object obj) {
		outJsonString(JSONObject.fromObject(obj).toString());
	}
	/**
	 * 把对象转换成JSON输出
	 * @param obj
	 */
	public void outJson(Object obj,JsonConfig jsonConfig) {
		outJsonString(JSONObject.fromObject(obj,jsonConfig).toString());
	}
	/**
	 * 把数组转换成JSON输出
	 * @param array
	 */
	public void outJsonArray(Object array) {
		outJsonString(JSONArray.fromObject(array).toString());
	}

	/**
	 * 输出流
	 * @param str
	 */
	public void outString(String str) {
		HttpServletResponse response = this.getResponse();
		PrintWriter pw = null;
		try {
			response.setContentType("application/json;charset=UTF-8");
			pw = response.getWriter();
			pw.print(str);
			//logger.info("baseAction jsonString:"+str);
			if (!str.contains("列表成功")) {
			    LogUtil.info("baseAction jsonString:"+str);
            }
		} catch (IOException e) {
			LogUtil.error("outString method Exception!",e);
			e.printStackTrace();
			//logger.error("outString method Exception!",e);
		}finally{
			if (pw != null) {
				pw.close();
			}
		}
	}
	
	/**
	 * 把集合转换成JSON输出
	 * @param list 待转集合
	 * @param config json配置
	 *      例如有java.util.Date 类型的字段需要转换，可创建如下jsonConfig对象传入
	 *      JsonConfig jsonConfig = new JsonConfig();
	 *      jsonConfig.registerJsonValueProcessor(java.util.Date.class, 
	 *                            new Date2JsonProcessor("yyyy-MM-dd HH:mm:ss"));
	 * @param response
	 */
	public void outJsonList(List list, JsonConfig config) {
		if (config == null) {
			outJsonString(JSONArray.fromObject(list.toArray(),
							BaseJsonAction.getDefConfig()).toString());
		} else {
			outJsonString(JSONArray.fromObject(list.toArray(), config).toString());
		}

	}

	/**
	 * 输出xml字符串
	 * @param xmlStr
	 */
	public void outXMLString(String xmlStr) {
		getResponse().setContentType("application/xml;charset=UTF-8");
		outString(xmlStr);
	}

	/**
	 * 获得Session存储数据
	 * @param key
	 * @return 请求参数
	 */
	protected Object getSessionAttr(String key) {
		return this.getRequest().getSession().getAttribute(key);
	}

	/**
	 * 获得Session存储数据
	 * @param key
	 * @return 请求参数
	 */
	protected void setSessionAttr(String key, Object o) {
		this.getRequest().getSession().setAttribute(key, o);
	}

	/**
	 * 设置响应参数
	 * @param key
	 * @param o
	 */
	protected void setAttr(String key, Object o) {
		this.getRequest().setAttribute(key, o);
	}

	/**
	 * 获得参数
	 * @param key
	 * @param o
	 */
	protected Object getAttr(String key) {
		return this.getRequest().getAttribute(key);
	}

//	/**
//	 * 获得当前用户
//	 * @param key
//	 * @param o
//	 */
//	protected LoginUser getCurrentUser() {
//		return (LoginUser) this.getSessionAttr(SysConstants.SESSION_USER);
//	}

	/**
	 * 获得请求参数
	 * @param key
	 * @return 请求参数
	 */
	protected String getParam(String key) {
		return this.getRequest().getParameter(key);
	}

	/**
	 * 异常处理
	 * @param ex
	 * @return 'error'字符串
	 */
	protected String exceptionHandler(Exception ex) {
		returnMessage.setCode(SysConstants.RM_FAIL_CODE);
		returnMessage.setMessage(ex.getMessage());
		return this.ERROR;
	}

	/**
	 * 对象转字符串，当参数为null时，返回""
	 * @param 需要转换的对象
	 * @return 对象转换的字符串
	 */
	protected String $(Object o) {
		return o != null ? o.toString() : "";
	}

	protected static String xssEncode(String s) {
		if (s == null || s.isEmpty()) {
			return s;
		}
		s = s.replaceAll("&", "&amp;");
		s = s.replaceAll("\"", "&quot;");
		s = s.replaceAll("\'", "&quot;");
		s = s.replaceAll("<", "&lt;");
		s = s.replaceAll(">", "&gt;");
		s = s.replaceAll("%3C", "&lt;");
		s = s.replaceAll("%3E", "&gt;");

		s = s.replaceAll("%27", "");
		s = s.replaceAll("%22", "");
		s = s.replaceAll("%3E", "");
		s = s.replaceAll("%3C", "");
		s = s.replaceAll("%3D", "");
		s = s.replaceAll("%2F", "");
		s = regexReplace("^>", "", s);

		s = regexReplace("<([^>]*?)(?=<|$)", "&lt;$1", s);
		s = regexReplace("(^|>)([^<]*?)(?=>)", "$1$2&gt;<", s);

		s = s.replaceAll("\\|", "");
		s = s.replaceAll("alert", "");
		s = s.replaceAll("style=", "");
		s = s.replaceAll("<iframe", "");
		s = s.replaceAll("script", "");
		s = s.replaceAll("<img", "");
//		s = s.replaceAll("IMG", "");
		s = s.replaceAll("exec", "");
		s = s.replaceAll("insert", "");
		s = s.replaceAll("delete", "");
		s = s.replaceAll("update", "");
		return s;
	}
	
	private static String regexReplace(String regex_pattern,
			String replacement, String s) {
		Pattern p = Pattern.compile(regex_pattern);
		Matcher m = p.matcher(s);
		return m.replaceAll(replacement);
	}
	
	protected static boolean isValid(String origStr){
	   String afterRep = "";
	   origStr =  origStr.toLowerCase();
	   afterRep = xssEncode(origStr);
	   return afterRep.length()==origStr.length()?true:false;
	}
}
