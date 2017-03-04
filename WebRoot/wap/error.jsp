<!DOCTYPE html>
<%@page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
	<meta charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	<title>非法请求</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
	<script src="js/rem.js" type="text/javascript"></script>
	<link rel="stylesheet" href="css/base.css" type="text/css">
	<link rel="stylesheet" href="css/main.css" type="text/css">
</head>
<body>
<dl class="desc">
非法请求！
</dl>
<script src="js/jquery-2.0.3.min.js" type="text/javascript"></script>
</body>
</html>

