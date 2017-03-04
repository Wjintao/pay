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
<script src="../wap/js/rem.js" type="text/javascript"></script>
<script src="../wap/js/jquery-2.0.3.min.js" type="text/javascript"></script>
<script src="../wap/js/base.js" type="text/javascript"></script>
<script type="text/javascript" src="../wap/js/jquery.md5.js"></script>
<link rel="stylesheet" href="../wap/css/base.css" type="text/css">
<link rel="stylesheet" href="../wap/css/main.css" type="text/css">
<style>
	.btn-sub.disabled{
  	background:#cbc8c8;
  	color:#6c6b6b;
	}
	.disabled { pointer-events: none; }
</style>
<script>
	$(function(){
	//getUnPurchaseOrder(); 
	//function getUnPurchaseOrder(){
		var timestamp=new Date().getTime();
		var signStr=$.md5("phone=<%=phone%>&inputTime="+timestamp+"&key=com.chinamobile.mcloudzf").toUpperCase();
		$.ajax({
			type : "POST",
			url : "<%=path%>/pay/getUnPurchase",
			data : {
				phone: '<%=phone%>',
				openId: '<%=openId%>',
				lx: '<%=lx%>',
				inputTime: timestamp,
				sign: signStr
			},
			timeout : 20000,
			dataType : "json",
			success : function(datatd) {
				if(datatd.code == 0){
					if(datatd.data!=null){
					console.log(datatd.data.length);
					//console.log(datatd.data[0][0]);
					//console.log(datatd.data[0][1]);
					var lg=datatd.data.length;
					var html1="";
					for(var i=0;i<lg;i++){
							html1=html1 +"<dd><i class='icon-radio' id='zxcv"+i+"' onclick=\"javascript:unpurshaseorder("+i+","+lg+",'"+datatd.data[i][0].aptrid+"','"+datatd.data[i][0].phone+"');\"></i>"+datatd.data[i][1].remark.split("$$")[2]+"</dd>"
						}
					$('#tddetail').append(html1);
					html1="";
					}
				}else{
					alert(datatd.message);
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
	//}
	});
	
	function unpurshaseorder(xl,lg,aptrid,phone) {
		document.getElementById("abcdefg").setAttribute("onclick","javascript:toUnPurchase("+xl+",'"+aptrid+"','"+phone+"');");
		//$('.icon-radio').toggleClass('checked');
		for(var i=0;i<lg;i++){
			if(xl==i){
			$("#zxcv"+xl).addClass('checked');
			$('.btn-submit').removeClass('disabled');
		    document.getElementById("abcdefg").disabled = false;
			//$("#remarkhy"+i).html(remark);
			}else{
			$("#zxcv"+i).removeClass('checked');
			//$("#remarkhy"+i).html("");
			}
		}
		//$('.btn-submit').removeClass('disabled');
		//document.getElementById("abcdefg").disabled = false;
	}
	
	function toUnPurchase(i,aptrid,phone) {
		$('.btn-submit').addClass('disabled');
		//$('.icon-radio').removeClass('checked');
		document.getElementById("abcdefg").setAttribute("onclick","javascript:;");
		document.getElementById("abcdefg").disabled = true;
		$('.icon-radio').addClass('disabled');
		var timestamp=new Date().getTime();
		var signStr=$.md5("phone="+phone+"&inputTime="+timestamp+"&key=com.chinamobile.mcloudzf").toUpperCase();
		$.ajax({
			type : "POST",
			url : "<%=path%>/pay/unPurchase",
			data : {
				aptrid: aptrid,
				openId: '<%=openId%>',
				phone: phone,
				inputTime: timestamp,
				sign: signStr
			},
			timeout : 20000,
			dataType : "json",
			success : function(datatd) {
				if(datatd.code == 0){
					$('#12341234xiangshouge').attr( 'style', '');
					$('#56785678').on('click',function(){
						window.location.href=datatd.data;
					});
				}else{
					alert(datatd.message);
					$("#zxcv"+i).removeClass('checked');
					$('.icon-radio').removeClass('disabled');
				}
			},
			complete : function(XMLHttpRequest,status){
				if(status=='timeout'){
					alert("请求超时，请检查网络或稍后再试");
					$("#zxcv"+i).removeClass('checked');
					$('.icon-radio').removeClass('disabled');
				}else if(status=='error'){
					alert("系统错误");
					$("#zxcv"+i).removeClass('checked');
					$('.icon-radio').removeClass('disabled');
				}
			}
		});
	}
</script>
</head>
<body>
<header>
	<a href="javascript:history.go(-1);" class="lft">
		<i class="icon-back"></i>
	</a>
	<h5>业务退订</h5>
</header>
<section>
	<dl class="tuiding" id="tddetail">
		<dt id="tddetail">尊敬的<%=phone%>用户，您将退订：</dt>
	</dl>

	<div class="but-box but-tips" style="margin-top:50px">
		<p>退订后，您建不在享受原有优惠特权</p>
		<button class="btn-sub btn-block btn-submit disabled" id="abcdefg">确认退订</button>
	</div>
</section>

<div class="cover" id="12341234xiangshouge" style="display: none;">
	<div class="alert alert-ept">
		<div class="content">
			<div class="status">
				<i class="icon-status s1"></i>
				<p>
					您的退订申请已提交
				</p>
				<p class="small">（次月生效）</p>
			</div>

			<div class="sub-box center" id="56785678">
				<span>确定</span>
			</div>
		</div>
	</div>
</div>
<script>
	/* $(function(){
		$('.sub-box').on('click',function(){
			$(this).parents('.cover').hide();
		});

		$('.icon-radio').on('click',function(){
			$(this).toggleClass('checked');
		});
	}) */
</script>
</body>
</html>