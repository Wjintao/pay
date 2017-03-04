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
	<title>关于暂停购买的说明</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
	<script src="js/rem.js" type="text/javascript"></script>
	<link rel="stylesheet" href="css/base.css" type="text/css">
	<link rel="stylesheet" href="css/main.css" type="text/css">
</head>
<body>
<header>
	<!--<a href="../wap/main.jsp" class="lft">
		<i class="icon-back"></i>
	</a>  -->
	<h5>关于暂停购买的说明</h5>
</header>
<section>
	<div class="title bb">
		<a href="javascript:history.go(-1);">
			<i class="icon-back back2"></i> 返回
		</a>
	</div>
	<div class="tab-con guid-bd">
			<dl>
					<dd>
						您好！由于近期使用网盘搬家的用户猛增，导致我们系统负荷过大。我们正在紧急调派设备、优化系统。360云盘关闭时间将近，为保障现有用户体验，我们暂时关闭了传输量套餐购买。
					</dd>
					<dd>
						&nbsp;
					</dd>
					<dd>
						在此期间，每位用户仍有5G免费传输量供体验，建议只将重要的个人文件搬至和彩云。
					</dd>
					<dd>
						&nbsp;
					</dd>
					<dd>
						已付费用户购买的传输量永久有效，购买时获赠的流量也将正常到账。有任何问题请随时联系微信客服。
					</dd>
			</dl>
		</div>
</section>

<script src="js/jquery-2.0.3.min.js" type="text/javascript"></script>
<script src="js/base.js" type="text/javascript"></script>
</body>
</html>


