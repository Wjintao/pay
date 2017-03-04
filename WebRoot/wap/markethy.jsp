<!DOCTYPE html>
<%@page language="java" import="java.util.*,com.pay.util.SendRequest,com.pay.util.MD5Util" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String from=String.valueOf(request.getAttribute("from"));
String channel=String.valueOf(request.getAttribute("channel"));
String openId=String.valueOf(request.getAttribute("openId"));
String phone=String.valueOf(request.getAttribute("phone"));
//String phone = request.getParameter("account");
if(from!=null && from.equalsIgnoreCase("WAP")){
	if(channel==null || channel.equalsIgnoreCase("null")){
		response.sendRedirect(basePath+"wap/error.jsp");
	    return;
	}
} else{
	response.sendRedirect(basePath+"wap/error.jsp");
	return;
}
%>
<html>
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>车靓靓会员套餐</title>
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<link rel="stylesheet" href="../wap/css/base.css" type="text/css">
<link rel="stylesheet" href="../wap/css/main.css" type="text/css">
<link rel="stylesheet" href="../wap/css/flexslider.css" type="text/css">
<style>
.btn-submit.disabled{
  background:#cbc8c8;
  color:#6c6b6b;
}
</style>
<script type="text/javascript" src="../wap/js/rem.js"></script>
<script type="text/javascript" src="../wap/js/jquery-2.0.3.min.js"></script>
<script type="text/javascript" src="../wap/js/base.js"></script>
<script type="text/javascript" src="../wap/js/jquery.md5.js"></script>
<script type="text/javascript" src="../wap/js/jquery.flexslider-min.js"></script>
<script>
var fzsize=0;
$(function(){
	if (<%=phone%> != null){
		$('#ponetext').val('<%=phone%>');
	}
	document.getElementById("zfid").disabled = true;
	getTcDetailHY();    
	function getTcDetailHY(){
		var timestamp=new Date().getTime();
		var signStr=$.md5("phone=<%=phone%>&inputTime="+timestamp+"&key=com.chinamobile.mcloudzf").toUpperCase();
		$.ajax({
			type : "POST",
			url : "<%=path%>/pay/getTcDetail",
			data : {
				objid: "123zxcv",
				phone: '<%=phone%>',
				inputTime: timestamp,
				sign: signStr
			},
			timeout : 20000,
			dataType : "json",
			success : function(datahy) {
				if(datahy.code == 0){
					if(datahy.data!=null){
						var listhy = datahy.data;	
						var html4="";
						var sizehy=0;
						//var tcArray=new Array();
						for(var i=0;i<listhy.length;i++){
							if(listhy[i].actype=='01'){
							html4=html4+"<li><div class='btn-size recom' id='tcthy"+sizehy+"' onclick=\"javascript:tcthy('"+listhy[i].sin+"',"+listhy.length+","+sizehy+",'"+listhy[i].objId+"','"+listhy[i].tcname+"','"+listhy[i].tcpace+"',"+listhy[i].tcpriceNow/100+",'"+listhy[i].tcname+"',1,"+listhy[i].tcprice/100+");\"><p><em>"+listhy[i].remark.split("$$")[0]+"</em>"+listhy[i].remark.split("$$")[1]+"<span class='fr'>"+listhy[i].tcpriceNow/100+"元/"+parseDmy(1,listhy[i].tctime)+"</span></p><span class='gray' id='remarkhy"+sizehy+"'>"+listhy[i].tcname+"</span></div></li>"
							}else{
							html4=html4+"<li><div class='btn-size' id='tcthy"+sizehy+"' onclick=\"javascript:tcthy('"+listhy[i].sin+"',"+listhy.length+","+sizehy+",'"+listhy[i].objId+"','"+listhy[i].tcname+"','"+listhy[i].tcpace+"',"+listhy[i].tcpriceNow/100+",'"+listhy[i].tcname+"');\"><p><em>"+listhy[i].remark.split("$$")[0]+"</em>"+listhy[i].remark.split("$$")[1]+"<span class='fr'>"+listhy[i].tcpriceNow/100+"元/"+parseDmy(1,listhy[i].tctime)+"</span></p><span class='gray' id='remarkhy"+sizehy+"'></span></div></li>"
							}
							sizehy++;
							//fzsize=sizecs+1;
						}

						$("#tclisthy").html(html4);					
						html4="";
					}
				}else{
					alert(datahy.message);
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
});




function tcthy(sin,ll,index,tcid,tcname,tcspace,nowmoney,remark,isdc,dcmoney){
	$('#zfid').removeClass('disabled');
	document.getElementById("zfid").setAttribute("onclick","javascript:toZf('3','"+tcid+"','"+remark+"','"+tcspace+"','"+nowmoney+"','"+sin+"');");
	document.getElementById("zfid").disabled = false;
	for(var i=0;i<ll;i++){
		if(index==i){
			$("#tcthy"+i).addClass('selected');
			$("#remarkhy"+i).html(remark);
		}else{
			$("#tcthy"+i).removeClass('selected');
			$("#remarkhy"+i).html("");
		}
	}
	$("#nowpricehy").html(nowmoney+"元");
	if(isdc==1){
		$("#dcpricehy").html("<del>原价："+dcmoney+"元</del>");
		$("#dcpricehy").show();
	}else{
		$("#dcpricehy").hide();
	}
}


function parseDmy(type,num){
	var ret="";
	if(type==1){
		if(num==1){
			ret="月";
		}else if(num==3){
			ret="季度";
		}else if(num==6){
			ret="半年";
		}else if(num==12){
			ret="年";
		}else{
			ret=num+"月";
		}
	}else{
		if(num==1){
			ret="一个月";
		}else if(num==3){
			ret="一季度";
		}else if(num==6){
			ret="半年";
		}else if(num==12){
			ret="一年";
		}else{
			ret=num+"个月";
		}
	}
	return ret;
}


function parseDll(num){
	var ret="";
	if(num==1024){
		ret="1G";
	}else{
		ret=num+"M";
	}
	return ret;
}

function toZf(lxt,tcid,tcname,tcspace,tcprice,sin){
	if ((<%=phone%> == null)) {
			alert("请输入您的号码");
			return false;
	}
	$('.btn-submit').addClass('disabled');
	document.getElementById("zfid").setAttribute("onclick","javascript:;");
	document.getElementById("zfid").disabled = true;
	$("#tcId").val(tcid);
	$("#lx").val(lxt);
	$("#name").val(tcname);
	$("#space").val(tcspace);
	$("#price").val(tcprice);
	$("#sin").val(sin);
	$("#sureForm").attr("action","<%=path%>/pay/tosure");
	document.all.sureForm.submit();
}
function mytequan() {
	if ((<%=phone%> == null)) {
			alert("请输入您的号码");
			return false;
		}else {
		window.location.href='<%=path%>/pay/toMarketPageWxc?phone=<%=phone%>&openId=<%=openId%>&wabp_result=000&channel=<%=channel%>';
	}
}

function unpurchase(lx){
		if ((<%=phone%> == null)) {
			alert("请输入您的号码");
			return false;
		}
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
					$('#messagetd').html("您暂无订购服务");
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
	
function confirmphone(){
		var phoneStr=$('#ponetext').val();
		$('#phoneStr').val(phoneStr);
		//手机号码正则式
		var mobileRex = /^((134[0-8]{1})|(13[0,1,2,3,5,6,7,8,9]\d)|(15[0,1,2,3,5,6,7,8,9]\d)|(17[0,7])\d|(18\d\d)|(14\d\d)|(106\d))\d{7}$/;
		if (mobileRex.test(phoneStr)) {
			$('#confirmPhone').submit();
		} else {
			alert("您输入的号码有误");
			return false;
		}
}
</script>
</head>
<body>
<header>
	<!-- <a href="http://caiyun.feixin.10086.cn/" class="lft">
		<i class="icon-back"></i>
	</a> -->
	<h5>车靓靓限时送220M流量</h5>
</header>
<form action="<%=path%>/pay/toMarketPageWxc?channel=<%=channel%>" id="confirmPhone" name="confirmPhone" method="post" style="display: none;">
		<input type="text" id="phoneStr" name="phone" value="" />
</form>
<form action="" id="sureForm" name="sureForm" method="post">
<section>
	<div class="telebox">
		<button class="btn-conf fr" type="button" id="confirm" onclick="javascript:confirmphone();">确定</button>
		<div class="conf">
		<input type="text" id="ponetext" placeholder="手机号码(限移动用户)" />
		</div>
	</div>
	<div class="banner2" id="flexslider">
		<ul id="imgslider">
			<li><a href="javascript:void(0)"><img src="../wap/images/hyxcwll.jpg" alt="和彩云洗车送流量"></a></li>
		</ul>
	</div>
	<div class="con-box">
	<div class="tab-con" id="tabrl_con">
		<div class="tab-con" id="tabhyl_con">
			<div class="dt">选择会员套餐：<a class="fr" href="../wap/introhy.jsp">套餐说明></a></div>
			<div class="dd mt10">
				<ul class="list-btn list-btn-blk" id="tclisthy">
					<!-- <li>
						<div class="btn-size selected recom">
							<p><em>高级会员</em>(车靓靓) <a class="fr">10元/月</a></p>
							<span class="gray">和彩云容量5GB+车靓靓免费汽车特权+优惠洗车折扣特权</span>
						</div>
					</li> -->
				</ul>
			</div>
			<div class="dd mt5">
				<div class="con-tips">
					查看我的特权 <a href="javascript:void(0)" onclick="javascript:mytequan()" href="<%=path%>/pay/toWapXiChe?account=<%=phone%>&openId=<%=openId%>&wabp_result=111"><span class="blue">详情>></span></a>
					<!-- <a href="javascript:void(0)" onclick="javascript:unpurchase('3')" class="fr"><span class="gray">退订>></span></a> -->
				</div>
			</div>
			<div class="dd mt5">
				<!-- <div class="con-tips">
					<span class="gray" id="actitle4">购买会员权益即可获赠220M流量   </span> <a href="../wap/introllhyhd.jsp" id="acdetail4"><span class="blue">详情>></span></a>
				</div> -->
			</div>
			<div class="dt">金额：</div>
			<div class="dd mt5">
				<span class="total" id="nowpricehy">0元</span> <del id="dcpricehy"></del>
			</div>
			<div class="but-box">
				<button class="btn-sub btn-block btn-submit disabled" id="zfid">去支付</button>
			</div>
			<%-- <div style="text-align: center;margin-top: 15px;"><a href="<%=path%>/pay/toWapXiChe?account=<%=phone%>&openId=<%=openId%>&wabp_result=222"><font color="#E40077" style="font-size: 16px;font-style: normal;text-align:justify;">查看我的特权</font></a></div> --%>
		</div>
	</div>
	</div>
</section>
<div class="cover" id="tuiding" style="display:none">
	<div class="alert alert-ept">
		<div class="content">
			<div class="status">
				<i class="icon-status s2"></i>
				<p id="messagetd">
				</p>
			</div>

			<div class="sub-box center">
				<span>确定</span>
			</div>
		</div>
	</div>
</div>

<input type="hidden" id="tcId" name="tcId" value=""/>
<input type="hidden" id="lx" name="lx" value=""/>
<input type="hidden" id="phone" name="phone" value="<%=phone%>"/>
<input type="hidden" id="name" name="name" value=""/>
<input type="hidden" id="space" name="space" value=""/>
<input type="hidden" id="price" name="price" value=""/>
<input type="hidden" id="sin" name="sin" value=""/>
<input type="hidden" id="flag" name="flag" value="wap"/>
<input type="hidden" id="openId" name="openId" value="<%=openId%>"/>
<input type="hidden" id="channel" name="channel" value="<%=channel%>"/>
</form>
<script>
	$(function(){
		$('.sub-box').on('click',function(){
			$('#tuiding').attr( 'style', 'display:none');
		});
	});
</script>
</body>
</html>