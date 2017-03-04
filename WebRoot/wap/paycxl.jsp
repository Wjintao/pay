<!DOCTYPE html>
<%@page language="java" import="java.util.*,com.pay.util.SendRequest,com.pay.util.MD5Util" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String toUrl=String.valueOf(request.getAttribute("toUrl"));
%>
<html>
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>彩心灵支付跳转页面</title>
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<script src="../wap/js/jquery-2.0.3.min.js" type="text/javascript"></script>
<script>
$(function(){
	if (/MSIE (\d+\.\d+);/.test(navigator.userAgent) || /MSIE(\d+\.\d+);/.test(navigator.userAgent)){
    	var referLink = document.createElement('a');
    	referLink.href = '<%=toUrl%>';
    	document.body.appendChild(referLink);
    	referLink.click();
	} else {
    	window.location.href = '<%=toUrl%>';
	}
})
</script>
</head>
<body>
正在跳转中。。。
</body>
</html>
