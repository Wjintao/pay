<!DOCTYPE html>
<%@page language="java" import="java.util.*,com.pay.util.SendRequest,com.pay.util.MD5Util" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String phone=String.valueOf(request.getAttribute("phone"));
String openId=String.valueOf(request.getAttribute("openId"));
String token=String.valueOf(request.getAttribute("token"));
if(phone==null || phone.equalsIgnoreCase("null") || openId==null || openId.equalsIgnoreCase("null")
  		|| token==null || token.equalsIgnoreCase("null")){
	response.sendRedirect(basePath+"pay/error.jsp");
    return;
}
//phone="15820712910";
//phone="113060916061";
String userkey=MD5Util.MD5Encode(phone+"BGfOQwRAazMhLHmj3etZ1d6AzYLDww", null);
String url="http://cz.umeol.com:6090/dm/v/cz/qureyFlowPhonecost.do";
String data="mobile="+phone+"&type=1&userkey="+userkey;
String hfye=SendRequest.Post(url, data, 10000);
data="mobile="+phone+"&type=2&userkey="+userkey;
String syll=SendRequest.Post(url, data, 10000);
System.out.println("话费余额："+hfye+"|剩余流量："+syll);
%>
<html>
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>选择套餐</title>
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<link rel="stylesheet" href="css/base.css?r=<%=new Random().nextInt()%>" type="text/css">
<link rel="stylesheet" href="css/main.css?r=<%=new Random().nextInt()%>" type="text/css">
<style>
.btn-submit.disabled{
  background:#cbc8c8;
  color:#6c6b6b;
}
.txt-dvd2{
  overflow: hidden;
  margin-right: -20px;
}
.txt-dvd2 li{
  float: left;
  width: 48%;
  font-size: 12px;
  line-height: 1;
}
.txt-dvd2 li:last-child{
  border-left: 1px solid #ddd;
  padding-left: 18px;
  width: 52%;
}
.txt-dvd2 li em{
  font-size: 24px;
}
</style>
<script src="js/rem.js" type="text/javascript"></script>
<script src="js/jquery-2.0.3.min.js" type="text/javascript"></script>
<script src="js/base.js" type="text/javascript"></script>
<script type="text/javascript" src="js/jquery.md5.js"></script>
<script>
$(function(){
	document.getElementById("zfid").disabled = true;
	var ye='<%=hfye%>';
	var yeobj = eval('(' + ye + ')'); 
    if(yeobj.status=='1'){
    	$("#hfye").html("<em>"+yeobj.msg+"</em>元");
    }else{
      	$("#hfye").html("<em>--</em>");
    }
    
	var ll='<%=syll%>';
	var llobj = eval('(' + ll + ')'); 
    if(llobj.status=='1'){
    	$("#syll").html("<em>"+parseFloat(llobj.msg).toFixed(2)+"</em>M");
    }else{
    	$("#syll").html("<em>--</em>");
    }
});

function selLx(type,lxid){
	//$('.type-box .btn-default').removeClass('selected');
	$('.btn-default').removeClass('selected');
	$("#"+lxid).addClass('selected');
	$("#yfje").html("0元");
	$('.slt-box').hide();
	$("#slt_"+lxid).show();
	document.getElementById("ryes").setAttribute("onclick","javascript:toIndex("+type+");");
	$('.btn-submit').addClass('disabled');
	document.getElementById("zfid").setAttribute("onclick","javascript:;");
	document.getElementById("zfid").disabled = true;
}

function toIntroduce(index){
	if(index==2){
		window.location.href="<%=basePath%>pay/introcsl";
	}else{
		window.location.href="<%=basePath%>pay/introduce";
	}
}

function selTc(lx,tcid,emid,num,name){
	$('.tbcell .btn-default').removeClass('selected');
	$("#"+emid).addClass('selected');
	$("#yfje").html(num+"元");
	$('.btn-submit').removeClass('disabled');
	document.getElementById("zfid").setAttribute("onclick","javascript:toZf('"+lx+"','"+tcid+"');");
	document.getElementById("zfid").disabled = false;
	$("#tcxc").html("<span class='gray'>套餐详情：</span>"+name);
}

function toZf(lxt,tcid){
	$('.btn-submit').addClass('disabled');
	document.getElementById("zfid").setAttribute("value","请求支付中...");
	document.getElementById("zfid").setAttribute("onclick","javascript:;");
	document.getElementById("zfid").disabled = true;
	var nn=48;
	if(lxt=='1'){
		nn=24;
	}
	$("#rdesc").html("完成支付后，容量将于<span class='org'>"+nn+"小时内</span>生效");
	var timestamp=new Date().getTime();
	var signStr=$.md5("phone=<%=phone%>&inputTime="+timestamp+"&key=com.chinamobile.mcloudzf").toUpperCase();
	$.ajax({
		type : "POST",
		url : "<%=path%>/pay/prepay",
		data : {
			tcId: tcid,
			lx: lxt,
			phone: '<%=phone%>',
			openId: '<%=openId%>',
			inputTime: timestamp,
			sign: signStr
		},
		timeout : 10000,
		dataType : "json",
		success : function(data) {
			if(data.code == 0){
				WeixinJSBridge.invoke('getBrandWCPayRequest',data.data,callback);
			}else{
				document.getElementById("zfid").setAttribute("value","去支付");
				$('.tbcell .btn-default').removeClass('selected');
				$("#yfje").html("0元");
				alert(data.message);
			}
		},
		complete : function(XMLHttpRequest,status){
			if(status=='timeout'){
				document.getElementById("zfid").setAttribute("value","去支付");
				$('.tbcell .btn-default').removeClass('selected');
				$("#yfje").html("0元");
				/*if (window.confirm("支付请求超时，请检查网络或稍后再试")) {
					return true;
				} else {
					return false;
				}*/
				alert("支付请求超时，请检查网络或稍后再试");
			}else if(status=='error'){
				document.getElementById("zfid").setAttribute("value","去支付");
				$('.tbcell .btn-default').removeClass('selected');
				$("#yfje").html("0元");
				alert("支付请求错误，请检查网络或稍后再试");
			}
		}
	});
	
}

function callback(res){ 
     document.getElementById("zfid").setAttribute("value","去支付");
     $('.tbcell .btn-default').removeClass('selected');
     $('#ryes').removeClass('disabled');
     $("#yfje").html("0元");
     WeixinJSBridge.log(res.err_msg); 	     	     				 
     if(res.err_msg=='get_brand_wcpay_request:ok'){ //发送成功
     	$("#prst").html("购买成功!");
     	$("#showrst").show();
     }else if(res.err_msg=='gget_brand_wcpay_request:cancel'){ //用户取消 
     	$("#prst").html("购买失败!");
     	$("#rdesc").html("您已取消支付");
     }else{ //get_brand_wcpay_request:fail  发送失败 
		$("#prst").html("购买失败!");
     	$("#rdesc").html("");
     	//$("#rdesc").html(res.err_code+":"+res.err_desc);
		//alert(msg+":"+res.err_code+" err_desc="+res.err_desc+" err_msg="+res.err_msg); 	
	}
	
};

function toIndex(type){
	if(type==1){
		window.location.href="http://caiyun.feixin.10086.cn:7070/portal/login.jsp?account=<%=phone%>&token=<%=token%>";
	}else{
		$("#showrst").hide();
	}
}
</script>
</head>
<body class="gray-bg">
<div class="block mg type-box">
	当前和彩云账号：<%=phone%>
	<div class="mt10">
		<ul class="txt-dvd2">
			<li>
				<span class="gray">话费余额：</span><br/>
				<span id="hfye"><em></em>元</span>
			</li>
			<li>
				<span class="gray">剩余流量：</span><br/>
				<span id="syll"><em></em>M</span>
			</li>
		</ul>
	</div>
</div>
<div class="block mg type-box">
	<div class="gray">选择类型</div>
	<div class="mt5">
		<i class="btn-default selected" onclick="javascript:selLx(1,'lx1');" id="lx1">购买容量</i><i style="width:160px" class="btn-default" onclick="javascript:selLx(2,'lx2');" id="lx2">购买网盘搬家传输量</i>
	</div>
</div>
<!--<div class="block mg type-box">
	当前和彩云账号：<=phone%>
	<div class="mt5 gray">选择类型</div>
	<div class="mt5">
		<i class="btn-default selected" onclick="javascript:selLx(1,'lx1');" id="lx1">购买容量</i><i style="width:160px" class="btn-default" onclick="javascript:selLx(2,'lx2');" id="lx2">购买网盘搬家传输量</i>
	</div>
</div>  -->
<div class="slt-box mg" id="slt_lx1">
	<div class="tt">
		选择容量套餐
		<a href="javascript:toIntroduce(1);" class="fr link"><span class="gray">套餐说明</span></a>
	</div>
	<ul class="list-s">
		<li>
			<div class="label"><i>20G</i></div>
			<div class="tbcell">
				<span class="blk"><span class="btn-default" onclick="javascript:selTc(1,'a234567890abcdefgs939kdidkjekdlk','tc1','3','和彩云20G容量单月套餐');" id="tc1">3元/月</span></span>
				<span class="blk"><span class="btn-default" onclick="javascript:selTc(1,'b234567890abcdefgs939kdidkjekdlk','tc2','20','和彩云20G容量单年套餐');" id="tc2">20元/年</span></span>
			</div>
		</li>
		<li>
			<div class="label"><i>50G</i></div>
			<div class="tbcell">
				<span class="blk"><span class="btn-default" onclick="javascript:selTc(1,'c234567890abcdefgs939kdidkjekdlk','tc3','5','和彩云50G容量单月套餐');" id="tc3">5元/月</span></span>
				<span class="blk"><span class="btn-default"  onclick="javascript:selTc(1,'d234567890abcdefgs939kdidkjekdlk','tc4','35','和彩云50G容量单年套餐');" id="tc4">35元/年</span></span>
			</div>
		</li>
		<li>
			<div class="label"><i>100G</i></div>
			<div class="tbcell">
				<span class="blk"><span class="btn-default" onclick="javascript:selTc(1,'e234567890abcdefgs939kdidkjekdlk','tc5','8','和彩云100G容量单月套餐');" id="tc5">8元/月</span></span>
				<span class="blk"><span class="btn-default" onclick="javascript:selTc(1,'f234567890abcdefgs939kdidkjekdlk','tc6','65','和彩云100G容量单年套餐');" id="tc6">65元/年</span></span>
			</div>
		</li>
		<li>
			<div class="label"><i>1T</i></div>
			<div class="tbcell">
				<span class="blk"><span class="btn-default" onclick="javascript:selTc(1,'g234567890abcdefgs939kdidkjekdlk','tc7','42','和彩云1T容量单月套餐');" id="tc7">42元/月</span></span>
				<span class="blk"><span class="btn-default" onclick="javascript:selTc(1,'h234567890abcdefgs939kdidkjekdlk','tc8','320','和彩云1T容量单年套餐');" id="tc8">320元/年</span></span>
			</div>
		</li>
	</ul>
</div>
<div class="slt-box mg none" id="slt_lx2">
	<div class="tt">
		选择传输量套餐
		<a href="javascript:toIntroduce(2);" class="fr link"><span class="gray">套餐说明</span></a>
	</div>
	<ul class="list-s list-s-special">
		<li>
			<div class="label"><i>200G</i></div>
			<div class="tbcell">
				<span class="blk"><span class="btn-default" onclick="javascript:selTc(2,'2a34567890abcdefgs939kdidkjekdlk','tc10','6','和彩云200G传输量套餐');" id="tc10">6元</span></span>
			</div>
		</li>
		<li>
			<div class="label"><i>500G</i></div>
			<div class="tbcell">
				<span class="blk"><span class="btn-default" onclick="javascript:selTc(2,'3a34567890abcdefgs939kdidkjekdlk','tc11','12','和彩云500G传输量套餐');" id="tc11">12元</span></span>
			</div>
		</li>
		<li>
			<div class="label"><i>1T</i></div>
			<div class="tbcell">
				<span class="blk"><span class="btn-default" onclick="javascript:selTc(2,'5a34567890abcdefgs939kdidkjekdlk','tc13','25','和彩云1T传输量套餐');" id="tc13">25元</span></span>
			</div>
		</li>
		<li>
			<div class="label"><i>2T</i></div>
			<div class="tbcell">
				<span class="blk"><span class="btn-default" onclick="javascript:selTc(2,'4a34567890abcdefgs939kdidkjekdlk','tc12','50','和彩云2T传输量套餐');" id="tc12">50元</span></span>
			</div>
		</li>
	</ul>
</div>
<div class="block mg">
	应付金额：<span class="org" id="yfje">0元</span>
</div>
<section style="padding-bottom:30px;">
<div class="but-box">
	<input type="button" class="btn-submit disabled" value="去支付" id="zfid" onclick="javascript:;">
</div>
</section>
<div class="cover" style="display:none;" id="showrst">
	<div class="alert">
		<div class="tt" id="prst">购买成功!</div>
		<div class="content">
			<dl class="taocan">
				<dd>
					<span class="gray">购买账号：</span>
					<%=phone%>
				</dd>
				<dd id="tcxc">
					<span class="gray">套餐详情：</span>
					和彩云套餐
				</dd>
				<dd>
					<span class="gray ft12" id="rdesc">
						完成支付后，容量将于<span class="org">24小时内</span>生效
					</span>
				</dd>
			</dl>

			<div class="but-box">
				<input type="button" class="btn-submit" value="确定" onclick="javascript:toIndex(1);" id="ryes">
			</div>
		</div>
	</div>
</div>
</body>
</html>