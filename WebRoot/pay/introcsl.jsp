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
	<title>和彩云传输量套餐说明</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
	<script src="js/rem.js" type="text/javascript"></script>
	<link rel="stylesheet" href="css/base.css" type="text/css">
	<link rel="stylesheet" href="css/main.css" type="text/css">
</head>
<body>
<dl class="desc">
	<dt>
		<i class="btn-num">1</i>
		什么是和彩云传输量
	</dt>
	<dd>
		和彩云微信公众号为用户提供跨网盘文件传输功能（详见“我的文件”-“网盘搬家”），支持用户将文件从一个网盘直接迁移到另一个网盘。若迁移了10G文件，则和彩云传输量为10G。
	</dd>
</dl>

<dl class="desc">
	<dt>
		<i class="btn-num">2</i>
		和彩云传输量套餐何时生效
	</dt>
	<dd>
		用户支付成功后48小时内生效。购买的传输量永久有效。
	</dd>
</dl>

<dl class="desc">
	<dt>
		<i class="btn-num">3</i>
		和彩云传输量套餐允许叠加吗
	</dt>
	<dd>
		和彩云传输量套餐支持用户重复购买、叠加购买。
	</dd>
</dl>

<dl class="desc">
	<dt>
		<i class="btn-num">4</i>
		如果文件传输失败会耗费和彩云传输量吗
	</dt>
	<dd>
		文件传输失败不会耗费和彩云传输量。
	</dd>
</dl>

<script src="js/jquery-2.0.3.min.js" type="text/javascript"></script>
</body>
</html>

