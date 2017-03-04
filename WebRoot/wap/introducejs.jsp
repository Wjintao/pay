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
	<title>套餐说明</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
	<script src="js/rem.js" type="text/javascript"></script>
	<link rel="stylesheet" href="css/base.css" type="text/css">
	<link rel="stylesheet" href="css/main.css" type="text/css">
</head>
<body>
<header>
	<!--<a href="../wap/main.jsp" class="lft">
		<i class="icon-back"></i>
	</a>-->
	<h5>和彩云容量套餐说明</h5>
</header>
<section>
	 <div class="title bb">
		<a href="javascript:history.go(-1);">
			<i class="icon-back back2"></i> 返回
		</a>
	</div> 
	<div class="tab-con guid-bd">
			<dl>
					<dt>
						1 什么是和彩云容量套餐
					</dt>
					<dd>
						和彩云容量套餐是和彩云为用户提供的付费扩容服务，用户可以通过购买容量套餐包获得对应容量使用权限。
					</dd>
				</dl>
				<dl>
					<dt>
						2 和彩云容量套餐何时生效
					</dt>
					<dd>
						用户完成容量套餐购买支付后24小时内生效。
					</dd>
				</dl>
				<dl>
					<dt>
						3 什么是容量套餐有效期
					</dt>
					<dd>
						根据用户订购所选的套餐，容量有效期按照一个月（30天）或一年（365天）来计算。例如：您于2016-7-20购买了一个月有效的20G套餐，则可使用至2016-8-20 23:59:59;您于2016-7-28购买了一年有效的100G套餐，则可使用至2017-7-28 23:59:59。
					</dd>
				</dl>
				<dl>
					<dt>
						4 容量到期后怎么处理
					</dt>
					<dd>
						您付费购买的容量到期后，可以自由选择是否续费。如果不续费，将收回到期的付费容量。容量回收后有两种情况：A、实际存储文件容量小于免费获得的容量，您的功能不受任何影响；B、实际存储文件容量等于或大于免费容量，我们不会删除文件，暂停您的上传，保存、离线下载等写入功能，其他功能可以正常使用。
					</dd>
				</dl>
				<dl>
					<dt>
						5 和彩云容量套餐允许叠加吗
					</dt>
					<dd>
						和彩云容量套餐支持用户重复购买、叠加购买，每次购买的容量有效期从购买时间开始计算。
					</dd>
				</dl>
				<dl>
					<dt>
						6 我购买了容量套餐，但容量一直没增加怎么办
					</dt>
					<dd>
						您购买的容量在24小时内生效，如果逾时仍未增加，请将问题和您的账号直接回复和彩云微信服务号，我们的客服会尽快帮您处理。其他关于购买容量的问题也可以直接回复和彩云微信服务号。
					</dd>
				</dl>
				<dl>
					<dt>
						7 保存到和彩云的数据安全吗
					</dt>
					<dd>
						我们承诺，对已经保存在和彩云的数据，我们会竭力保证数据的安全以及用户的正常使用（国家法律法规禁止的除外）。我们会不定期推出各种容量优惠活动，敬请关注和彩云。
					</dd>
				</dl>
		</div>
</section>

<script src="js/jquery-2.0.3.min.js" type="text/javascript"></script>
<script src="js/base.js" type="text/javascript"></script>
</body>
</html>


