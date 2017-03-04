<!DOCTYPE html>
<%@page language="java" import="java.util.*,com.pay.util.SendRequest,com.pay.util.MD5Util" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String tcId=String.valueOf(request.getAttribute("tcId"));
String lx=String.valueOf(request.getAttribute("lx"));
String phone=String.valueOf(request.getAttribute("phone"));
String name=String.valueOf(request.getAttribute("name"));
String space=String.valueOf(request.getAttribute("space"));
String price=String.valueOf(request.getAttribute("price"));
String mysign=String.valueOf(request.getAttribute("mysign"));
String channel=String.valueOf(request.getAttribute("channel"));
String sin=String.valueOf(request.getAttribute("sin"));
if(mysign==null || !mysign.equals("1")){
	response.sendRedirect(basePath+"web/error.jsp");
    return;
}

/*String tcId=request.getParameter("tcId");
String lx=request.getParameter("lx");
String phone=request.getParameter("phone");
String name=request.getParameter("name");
String space=request.getParameter("space");
String price=request.getParameter("price");*/
%>
<html>
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>支付页</title>
<link rel="stylesheet" href="../web/css/base.css">
<link rel="stylesheet" href="../web/css/main.css">
<style>
.btn-submit.disabled{
  background:#cbc8c8;
  color:#6c6b6b;
}
</style>
<script type="text/javascript" src="../web/js/jquery-1.10.1.js"></script>
<script type="text/javascript" src="../web/js/jquery.md5.js"></script>
<script type="text/javascript" src="../web/js/qrcode.js"></script>
<script type="text/javascript" src="../web/js/jquery.qrcode.js"></script>
<script>
$(function(){
	if ('<%=sin%>' == '5') {
			$('#hfzhifu').attr( 'style', 'display:none' )
	}
	
	if ('<%=lx%>' == '4') {
			$('#wxzhifu').attr( 'style', 'display:none' )
	}
});

function zfType(zftype,tcid){
	if(zftype==1){
		$("#hfzf").addClass('on');
		$("#wxzf").removeClass('on');
	}else if(zftype==2){
		$("#wxzf").addClass('on');
		$("#hfzf").removeClass('on');
	}
	$('.btn-submit').removeClass('disabled');
	document.getElementById("zfid").setAttribute("onclick","javascript:toZf('<%=lx%>','"+tcid+"',"+zftype+");");
	document.getElementById("zfid").disabled = false;
}

function toZf(lxt,tcid,zftype){
	//$('.btn-submit').addClass('disabled');
	document.getElementById("zfid").setAttribute("value","请求支付中...");
	//document.getElementById("zfid").setAttribute("onclick","javascript:;");
	//document.getElementById("zfid").disabled = true;
	$("#loading").show();
	var timestamp=new Date().getTime();
	var signStr=$.md5("phone=<%=phone%>&inputTime="+timestamp+"&key=com.chinamobile.mcloudzf").toUpperCase();
	$.ajax({
		type : "POST",
		url : "<%=path%>/pay/surePay",
		data : {
			tcId: tcid,
			lx: lxt,
			phone: '<%=phone%>',
			channel: '<%=channel%>',
			cntType: 'WEB',
			zfType: zftype,
			inputTime: timestamp,
			sign: signStr
		},
		timeout : 10000,
		dataType : "json",
		success : function(data) {
			if(data.code == 0){
				if(zftype==1){
					//话费支付
					if (/MSIE (\d+\.\d+);/.test(navigator.userAgent) || /MSIE(\d+\.\d+);/.test(navigator.userAgent)){
    					var referLink = document.createElement('a');
    					referLink.href = data.data;
    					document.body.appendChild(referLink);
    					referLink.click();
					} else {
    					window.location.href=data.data;
					}
				}else if(zftype==2){ //微信二维码支付
					$("#loading").hide();
					$("#div_div").show();
					$("#qrcodeshow").qrcode(utf16to8(data.data));
				}
			}else{
				alert(data.message);
			}
		},
		complete : function(XMLHttpRequest,status){
			$("#loading").hide();
			if(status=='timeout'){
				document.getElementById("zfid").setAttribute("value","去支付");
				alert("支付请求超时，请检查网络或稍后再试");
			}else if(status=='error'){
				document.getElementById("zfid").setAttribute("value","去支付");
				alert("支付请求错误，请检查网络或稍后再试");
			}
		}
	});
}

function utf16to8(str) {
	var out, i, len, c;
	out = "";
	len = str.length;
	for (i = 0; i < len; i++) {
		c = str.charCodeAt(i);
		if ((c >= 0x0001) && (c <= 0x007F)) {
			out += str.charAt(i);
		} else if (c > 0x07FF) {
			out += String.fromCharCode(0xE0 | ((c >> 12) & 0x0F));
			out += String.fromCharCode(0x80 | ((c >> 6) & 0x3F));
			out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
		} else {
			out += String.fromCharCode(0xC0 | ((c >> 6) & 0x1F));
			out += String.fromCharCode(0x80 | ((c >> 0) & 0x3F));
		}
	}
	return out;
}
</script>
</head>
<body>
<div class="header">
	<div class="limiter">
		<div class="fl logo"><a href="index1.html"><img src="../web/images/logo.png" alt=""></a></div>
	</div>
</div>
<div class="main clearfix">
	<div class="limiter">
		<div class="pay-title">
			<div class="lmt">
				<h5>选择支付方式</h5>
				<p>应付金额：<span class="high"><%=price%>元</span></p>
			</div>
		</div>		
		<div class="pay-doc">
			<div class="lmt">
				<div class="tb-pay-tt">
					订单详情
				</div>
				<table class="tb-pay">
					<tr><th>产品类型</th><th>购买账号</th><th>购买容量</th><th>购买金额</th></tr>
					<tr><td><%=name%></td><td><%=phone%></td><td><%=space%></td><td><%=price%>元</td></tr>
				</table>
				<dl class="mt15 pay-pad" id="hfzhifu">
					<dt>话费支付（目前只支持移动用户）</dt>
					<dd>
						<label class="label" id="hfzf" onclick="javascript:zfType(1,'<%=tcId%>');">
							<input type="radio" name="zftype">
							<i class="icon-pay pay1"></i>
							<i>话费支付</i>
						</label>
					</dd>
				</dl>
				<dl class="mt15 pay-pad" id="wxzhifu">
					<dt>第三方支付</dt>
					<dd>
						<!-- <label class="label">
							<input type="radio" name="type">
							<i class="icon-pay pay2"></i>
							<i>支付宝支付</i>
						</label> -->
						<label class="label" id="wxzf" onclick="javascript:zfType(2,'<%=tcId%>');">
							<input type="radio" name="zftype">
							<i class="icon-pay pay3"></i>
							<i>微信支付</i>
						</label>
					</dd>
				</dl>
				<div class="center mtb"><button class="btn-submit btn-line disabled" id="zfid">确认支付</button></div>
			</div>
		</div>
	</div>
</div>
<div class="footer">
	<div class="limiter">
		<div class="copyright">
				<span>京IPC备05002571</span>
				<span>中国移动通信版权所有</span>
			</div>
	</div>
</div>
<div class="cover" style="display:none;" id="loading">
	<div class="alert">
		<div class="content center">
			<span class="ft18">正在跳转</span><i class="icon-rotate"></i>
		</div>
	</div>
</div>
<div class="cover" style="display:none;" id="div_div">
	<div class="alert">
		<div id="qrcodeshow" class="content center"></div>
		<div class="content center">请扫描二维码完成支付！</div>
	</div>
</div>
</body>
</html>
