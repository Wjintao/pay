<!DOCTYPE html>
<%@page language="java" import="java.util.*,com.pay.util.SendRequest,com.pay.util.MD5Util" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String phone=String.valueOf(request.getAttribute("phone"));
String name=String.valueOf(request.getAttribute("name"));
String mysign=String.valueOf(request.getAttribute("mysign"));
String sin=String.valueOf(request.getAttribute("sin"));
String openId=String.valueOf(request.getAttribute("openId"));
String lx=String.valueOf(request.getAttribute("lx"));
String suc = request.getParameter("suc");
if(mysign==null || !mysign.equals("1")){
	response.sendRedirect(basePath+"wap/error.jsp");
    return;
}
%>
<html>
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>业务退订</title>
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<script type="text/javascript" src="../wap/js/rem.js"></script>
<script type="text/javascript" src="../wap/js/jquery-2.0.3.min.js"></script>
<script type="text/javascript" src="../wap/js/base.js"></script>
<script type="text/javascript" src="../wap/js/jquery.md5.js"></script>
<link rel="stylesheet" href="../wap/css/base.css" type="text/css">
<link rel="stylesheet" href="../wap/css/main.css" type="text/css">
<link rel="stylesheet" href="../wap/css/huiyuan.css" type="text/css">
<style>
/* 	.btn-sub.disabled{
  	background:#cbc8c8;
  	color:#6c6b6b;
	}
	.disabled { pointer-events: none; } */
</style>
<script>
function unpurchase(lx){
	var timestamp=new Date().getTime();
		var signStr=$.md5("phone=<%=phone%>&inputTime="+timestamp+"&key=com.chinamobile.mcloudzf").toUpperCase();
		$.ajax({
			type : "POST",
			url : "<%=path%>/pay/toUnpurchase",
			data : {
			    openId: '<%=openId%>',
				phone: '<%=phone%>',
				lx: lx,
				inputTime: timestamp,
				sign: signStr
			},
			timeout : 20000,
			dataType : "json",
			success : function(datau) {
				if(datau.code == 0){
					if(datau.data!=null){
						window.location.href=datau.data;
					}
				}else{
					$('#messagetd').html(datau.message);
					$('#tuiding').attr( 'style', '');
				}
			},
			complete : function(XMLHttpRequest,status){
				if(status=='timeout'){
					alert("请求超时，请检查网络或稍后再试");
				}else if(status=='error'){
					alert("系统错误");
				}
			}
		});
	}
</script>
</head>
<body class="h100">
<header>
	<a href="javascript:history.go(-1);" class="lft">
		<i class="icon-back"></i>
	</a>
	<h5>会员特权</h5>
</header>
<section>
	<div class="back-line"><a class="lft" href="javascript:history.go(-1);"><i class="icon-back-gray"></i><span>返回</span></a></div>
	<div class="liberty-container">
		<table>
			<tr class="dis">
				<td colspan="2"></td>
			</tr>
			<tr class="nodis">
				<td><span>车靓靓洗车</span></td>
				<td><a href="<%=path%>/pay/toWapXiChe?account=<%=phone%>&openId=<%=openId%>&wabp_result=111"><span><font color="#fff">详细查看</font></span></a></td>
			</tr>
			<tr class="dis">
				<td colspan="2"></td>
			</tr>
			<tr class="nodis">
				<td><span>通通券会员特权</span></td>
				<td><a href="<%=path%>/pay/toWapXiChe?account=<%=phone%>&openId=<%=openId%>&wabp_result=333"><span><font color="#fff">详细查看</font></span></a></td>
			</tr>
			<!-- <tr class="dis">
				<td colspan="2"></td>
			</tr>
			<tr class="nodis">
				<td><span>车靓靓洗车</span></td>
				<td><span>详细查看</span></td>
			</tr>
			<tr class="dis">
				<td colspan="2"></td>
			</tr>
			<tr class="nodis">
				<td><span>通通券会员特权</span></td>
				<td><span>详细查看</span></td>
			</tr> -->
		</table>
	</div>
</section>
<footer class="footer-line">
	<a class="btn-submit btn-tuiding" href="javascript:void(0)" onclick="javascript:unpurchase('3')">退订</a>
</footer>
</body>
</html>