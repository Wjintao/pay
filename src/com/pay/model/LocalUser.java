package com.pay.model;

import java.io.Serializable;
import java.util.Date;

public class LocalUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5747617675710088841L;
	//用户统一通行证号
	private String userId;
	//用户号码
	private Long mobile;
	//用户名称
	private String userName;
	
	//用户图片
	private String bigIco;
	
	private String smallIco;
	//用户唯一标识
	private String singleFlag;
	//最后更新时间
	private Date  lastTime;
	//总持仓价值
	private Long stockAssets;
	//用户总福分
	private Long score;
	//可用福分
	private Long useAbleScore = 0l;
	//总盈利
	private Long totalProfit;
	//用户类型
	private int userType;
	//是否首次登录
	private int isFirstLogin ;
//	private PushingMessage message;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Long getMobile() {
		return mobile;
	}
	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getBigIco() {
		return bigIco;
	}
	public void setBigIco(String bigIco) {
		this.bigIco = bigIco;
	}
	public String getSmallIco() {
		return smallIco;
	}
	public void setSmallIco(String smallIco) {
		this.smallIco = smallIco;
	}
	public String getSingleFlag() {
		return singleFlag;
	}
	public void setSingleFlag(String singleFlag) {
		this.singleFlag = singleFlag;
	}
	public Long getStockAssets() {
		return stockAssets;
	}
	public void setStockAssets(Long stockAssets) {
		this.stockAssets = stockAssets;
	}
	public Long getScore() {
		return score;
	}
	public void setScore(Long score) {
		this.score = score;
	}
	public Long getTotalProfit() {
		return totalProfit;
	}
	public void setTotalProfit(Long totalProfit) {
		this.totalProfit = totalProfit;
	}

	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public Long getUseAbleScore() {
		return useAbleScore;
	}
	public void setUseAbleScore(Long useAbleScore) {
		this.useAbleScore = useAbleScore;
	}
	public Date getLastTime() {
		return lastTime;
	}
	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}
	public int getIsFirstLogin() {
		return isFirstLogin;
	}
	public void setIsFirstLogin(int isFirstLogin) {
		this.isFirstLogin = isFirstLogin;
	}
//	public PushingMessage getMessage() {
//		return message;
//	}
//	public void setMessage(PushingMessage message) {
//		this.message = message;
//	}

}
