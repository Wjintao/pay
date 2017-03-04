package com.pay.model;

import java.io.Serializable;


/**
 * 返回客户端信息
 * @author lurongfu
 *
 */

public class ReturnMessage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8034059528667182056L;
	private int code;
	private String message;
	private Object data;
	
	public ReturnMessage(){
		
	}
	public ReturnMessage(int code,String message){
		this.code=code;
		this.message=message;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
}
