package com.pay.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * 登录用户对象
 * 
 * @author RankHe
 * 
 */
public class LoginUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8830550847534342386L;
	// 用户登录后平台返回的唯一识别码
	private String ticket;
	// 用户编号
	private String userId;
	// 等级
	private String rank;
	// 用户姓名
	private String user_name;
	// 用户的省份
	private String user_pro;
	// 用户的地市
	private String user_city;
	// 用户的号码
	private String phoneNm;
	//
	private String mailAdd;
	// 用户昵称
	private String nickname;
	// 用户的ip地址
	private String ip;
	// 用户的来源
	private String userOrigin;
	// 登录方式 web|wap
	private String comeFrom = "WEB";
	// 活动编号
	private String active_id;
	// 登录id
	private String loginId;
	// 登录时间
	private String logintime;
	// 最后登录的时间
	private String lastlogintime;
	// 是否黑名单
	private boolean isBlack; 
	// 当天抽奖次数
	private int drawCount = 0;

	//最后报活时间
	private Date keepAliveTime;

	private boolean firstLogin;
	
	LocalUser localUser;
		
	public LocalUser getLocalUser() {
		return localUser;
	}

	public void setLocalUser(LocalUser localUser) {
		this.localUser = localUser;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_pro() {
		return user_pro;
	}

	public void setUser_pro(String user_pro) {
		this.user_pro = user_pro;
	}

	public String getUser_city() {
		return user_city;
	}

	public void setUser_city(String user_city) {
		this.user_city = user_city;
	}

	public String getPhoneNm() {
		if(StringUtils.isEmpty(phoneNm)){
			phoneNm="0";
		}
		return phoneNm;
	}

	public void setPhoneNm(String phoneNm) {
		this.phoneNm = phoneNm;
	}

	public String getMailAdd() {
		return mailAdd;
	}

	public void setMailAdd(String mailAdd) {
		this.mailAdd = mailAdd;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUserOrigin() {
		return userOrigin;
	}

	public void setUserOrigin(String userOrigin) {
		this.userOrigin = userOrigin;
	}

	

	public String getComeFrom() {
		return comeFrom;
	}

	public void setComeFrom(String comeFrom) {
		this.comeFrom = comeFrom;
	}

	public String getActive_id() {
		return active_id;
	}

	public void setActive_id(String active_id) {
		this.active_id = active_id;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getLogintime() {
		return logintime;
	}

	public void setLogintime(String logintime) {
		this.logintime = logintime;
	}

	public String getLastlogintime() {
		return lastlogintime;
	}

	public void setLastlogintime(String lastlogintime) {
		this.lastlogintime = lastlogintime;
	}

	public boolean isBlack() {
		return isBlack;
	}

	public void setBlack(boolean isBlack) {
		this.isBlack = isBlack;
	}

	public int getDrawCount() {
		return drawCount;
	}

	public void setDrawCount(int drawCount) {
		this.drawCount = drawCount;
	}


	public Date getKeepAliveTime() {
		return keepAliveTime;
	}

	public void setKeepAliveTime(Date keepAliveTime) {
		this.keepAliveTime = keepAliveTime;
	}

	public boolean isFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(boolean firstLogin) {
		this.firstLogin = firstLogin;
	}


}
