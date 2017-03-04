<!DOCTYPE html>
<%@page language="java" import="java.util.*,com.pay.util.SendRequest,com.pay.util.MD5Util" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String from=String.valueOf(request.getAttribute("from"));
String channel=String.valueOf(request.getAttribute("channel"));
String openId=String.valueOf(request.getAttribute("openId"));
String phone=String.valueOf(request.getAttribute("phone"));
String tcId=String.valueOf(request.getAttribute("tcId"));
String liuliang=String.valueOf(request.getAttribute("liuliang"));
String tctype=String.valueOf(request.getAttribute("tctype"));
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
<title>空间扩容</title>
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<link rel="stylesheet" href="../wap/css/base.css" type="text/css">
<link rel="stylesheet" href="../wap/css/main.css" type="text/css">
<link rel="stylesheet" href="../wap/css/flexslider.css" type="text/css">
<style>
.btn-submit.disabled{
  background:#cbc8c8;
  color:#6c6b6b;
}
.btn-size.disabled { pointer-events: none; }
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
	if ('<%=liuliang%>' == 'OK') {
			var timestamp=new Date().getTime();
			var signStr=$.md5("phone=<%=phone%>&inputTime="+timestamp+"&key=com.chinamobile.mcloudzf").toUpperCase();
			$("#loading").show();
			setTimeout(function(){
			$.ajax({
				type : "POST",
				url : "<%=path%>/pay/getTicket",
				data : {
					phone: '<%=phone%>',
					openId: '<%=openId%>',
					tcId: '<%=tcId%>',
					inputTime: timestamp,
					sign: signStr
				},
				timeout : 20000,
				dataType : "json",
				success : function(datalshy) {
					if(datalshy.code == 0){
							if ('<%=tcId%>'=='5') {
								$("#loading").hide();
								$('#prst').html("支付成功");
								$('#rdesc').html("您已购买成功并获得半年乐视会员+70M流量特惠礼包，乐视兑换码是 <span><strong id='lshyc'></strong></span>，请长按复制或截图保存券码并在“中国移动和彩云”微信后台回复“乐视会员”查看兑换攻略，兑换截止时间为2016年12月31日，逾期兑换无效。流量即时赠送，于次月1日生效，生效当月使用。");
								$('#lshyc').html(datalshy.data)
								$('#gray').html("")
								$('#liuliang').attr( 'style', '');
							} else if ('<%=tcId%>'=='1'){
								$("#loading").hide();
								$('#prst').html("支付成功");
								$('#rdesc').html("您已成功购买套餐并获得腾讯大礼包1份。礼包兑换码是 <span><strong id='lshyc'></strong></span>，请长按复制或截图保存券码并在“中国移动和彩云”微信后台回复“大礼包”领取礼包，体验《梦幻诛仙》获取70M流量。截止时间为2016年11月20日，逾期兑换无效。");
								$('#lshyc').html(datalshy.data)
								$('#gray').html("")
								$('#liuliang').attr( 'style', '');
							}
					}else{
							$.ajax({
							type : "POST",
							url : "<%=path%>/pay/checkUFO",
							data : {
								openId: '<%=openId%>',
								phone: '<%=phone%>',
								inputTime: timestamp,
								sign: signStr
							},
							timeout : 20000,
							dataType : "json",
							success : function(datall) {
								if(datall.code == 0){
									if ('<%=tctype%>' == 'HY') {
										$("#loading").hide();
								 		$('#prst').html("支付成功");
								 		$('#rdesc').html("您好，您已成功购买套餐并获得流量赠送。空间容量将在24小时内到账，流量次月1日生效，限生效当月使用。");
								 		$('#gray').html("");
										$('#liuliang').attr( 'style', '');
										setTimeout(
					     					function() {
											$('#liuliang').attr( 'style', 'display:none');
											window.location.href='<%=path%>/pay/toWapXiChe?account=<%=phone%>&openId=<%=openId%>&wabp_result=111';
				      						},5000);
				      				} else {
				      					$("#loading").hide();
								 		$('#prst').html("支付成功");
								 		$('#rdesc').html("您好，您已成功购买套餐并获得流量赠送，流量每月25日前（含25日当天）中取的于3个工作日内赠送，25日后中取的于次月月初赠送，限生效当月使用。");
								 		$('#gray').html("");
										$('#liuliang').attr( 'style', '');
										setTimeout(
					     					function() {
											$('#liuliang').attr( 'style', 'display:none');
											window.location.href='<%=path%>/pay/toMarketPageWap?channel=<%=channel%>&phone=<%=phone%>';
				      						},5000);
				      				}
								}else{
									if ('<%=tctype%>' == 'HY') {
										$("#loading").hide();
								 		$('#prst').html("支付成功");
								 		$('#rdesc').html("");
										$('#liuliang').attr( 'style', '');
										setTimeout(function() {
											$('#liuliang').attr( 'style', 'display:none');
											window.location.href='<%=path%>/pay/toWapXiChe?account=<%=phone%>&openId=<%=openId%>&wabp_result=111';
				      					},3000);
			      					} else {
										$("#loading").hide();
								 		$('#prst').html("支付成功");
								 		$('#rdesc').html("");
										$('#liuliang').attr( 'style', '');
										setTimeout(function() {
											$('#liuliang').attr( 'style', 'display:none');
											window.location.href='<%=path%>/pay/toMarketPageWap?channel=<%=channel%>&phone=<%=phone%>';
				      					},3000);
									}
								}
							},
							
						complete : function(XMLHttpRequest,status){
							$("#loading").hide();
							if(status=='timeout'){
								alert("请求超时，请检查网络或稍后再试");
							}else if(status=='error'){
								alert("系统错误");
							}
						}
					});
				 }
				},
				complete : function(XMLHttpRequest,status){
						$("#loading").hide();
						if(status=='timeout'){
							alert("请求超时，请检查网络或稍后再试");
						}else if(status=='error'){
							alert("系统错误");
						}
					}
		})},4000);
	}
	//document.getElementById("zfid").disabled = true;
	getOperate();    
	function getOperate(){
		var timestamp=new Date().getTime();
		var signStr=$.md5("phone=<%=phone%>&inputTime="+timestamp+"&key=com.chinamobile.mcloudzf").toUpperCase();
		$.ajax({
			type : "POST",
			url : "<%=path%>/pay/getOperate",
			data : {
				phone: '<%=phone%>',
				cntType: 'WAP',
				inputTime: timestamp,
				sign: signStr
			},
			timeout : 20000,
			dataType : "json",
			success : function(dataop) {
				if(dataop.code == 0){
					if(dataop.data!=null){
						var list = dataop.data;	
						var html3="";
						var b=false;
						for(var i=0;i<list.length;i++){
							if(list[i].oprType=='02'){
								if (list[i].remark=='01') {
									html3=html3 +"<li><a href='"+ list[i].detailUrl +"' id='ggurl"+i+"' onclick='javascript:getHerf()'><img src='"+ list[i].picUrl+ "' alt='' id='ggid'></a></li>"
								}else {
									html3=html3 +"<li><a href='"+ list[i].detailUrl +"' id='ggurl"+i+"'><img src='"+ list[i].picUrl+ "' alt='' id='ggid'></a></li>"
									//document.getElementById("ggurl").setAttribute("href",list[i].detailUrl);
									//document.getElementById("ggid").setAttribute("src",list[i].picUrl);
								}
							}
							if(list[i].oprType=='01' && !b){
								//$("#actitle").html(list[i].oprTitle);
								//$("#actitle2").html(list[i].oprTitle);
								//$("#actitle3").html(list[i].oprTitle);
								//document.getElementById("acdetail").setAttribute("href",list[i].detailUrl);
								//document.getElementById("acdetail2").setAttribute("href",list[i].detailUrl);
								//document.getElementById("acdetail3").setAttribute("href",list[i].detailUrl);
								b=true;
							}
						}
						//console.log(html3);
						//$("#imgslider").append(html1);
						$("#imgslider").html(html3);
						$("#flexslider").flexslider({
                    		slideshowSpeed: 2000, 
                    		animationSpeed: 600, 
                    		animation: "fade",
                    		pauseOnHover : true,
                            pauseOnAction: false,
                            directionNav: false,
                            animationLoop: true,
                    		touch: true
                		});
					}
				}else{
					alert(dataop.message);
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

function tclistdetail(sin,tcid,tcname,tcspace,ll,index,nowmoney,isdc,dcmoney){
	$('#zfid').removeClass('disabled');
	document.getElementById("zfid").setAttribute("onclick","javascript:toZf('4','"+tcid+"','"+tcname+"','"+tcspace+"','"+nowmoney+"','"+sin+"');");
	document.getElementById("zfid").disabled = false;
	$("#dcprice").hide();
	for(var i=0;i<ll;i++){
		if(index==i){
			$("#tclistdetail"+i).addClass('selected');
		}else{
			$("#tclistdetail"+i).removeClass('selected');
		}
	}
	$("#nowprice").html(nowmoney+"元");
	/*if(nowmoney==20 && tcspace=="20GB"){
		$("#dcprice").html("<del>原价：36元</del>");
		$("#dcprice").show();
	}*/
	if(isdc==1){
		$("#dcprice").html("<del>原价："+dcmoney+"元</del>");
		$("#dcprice").show();
	}
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
	<div class="limiter">
		<div class="fl logo">
			<a href="javascript:void(0);"><img src="../web/images/logo.png" alt=""></a>
		</div>
	</div>
</header>
<form action="<%=path%>/pay/toMarketPageWap?channel=<%=channel%>" id="confirmPhone" name="confirmPhone" method="post" style="display: none;">
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
	<div class="flexslider banner2" id="flexslider" style="margin:0 0 15px;">
		<ul class="slides" id="imgslider">
			<!-- <li><a href=""><img src="images/banner.png" alt=""></a></li> -->
		</ul>
	</div>
	
	<div class="con-box">
		<div class="tab-con" id="tabrl_con">
			<div class="dt"><span>选择容量套餐:</span><a class="fr"  href="../wap/introllhd.jsp">套餐说明></a></div>
			<div class="dd" >
				<ul class="list-btnjs list-btn4">
					<li>
						<div class="btn-sizejs selected" id="tclistdetail0" onclick="javascript:tclistdetail('kdyzz','4','和彩云50GB空间月包','50GB',3,0,5);">
							<i class="icon-rec" id="tcliconrec1">赠70M流量</i>
							<span class="inblock">
								<em>50GB</em>
								<p>5元/月</p>
							</span>
						</div>
					</li>
					<li>
						<div class="btn-sizejs" id="tclistdetail1" onclick="javascript:tclistdetail('kdyzo','5','和彩云50GB空间季度包','50GB',3,1,10);">
							<!-- <i class="icon-rec" id="tcliconrec2">赠70M流量</i> -->
							<span class="inblock">
								<em>50GB</em>
								<p>10元/3个月</p>
							</span>
						</div>
					</li>
					<li>
						<div class="btn-sizejs" id="tclistdetail2" onclick="javascript:tclistdetail('kdyzy','7','和彩云100GB空间月包','100GB',3,2,8);">
							<i class="icon-rec" id="tcliconrec3">赠150M流量</i>
							<span class="inblock">
								<em>100GB</em>
								<p>8元/月</p>
							</span>
						</div>
					</li>
				</ul>
			</div>
			<div class="dd" id="aiai">
				<div class="con-tips">
					<span class="gray" id="actitle">活动期间每次购买50GB/100GB一个月时效套餐有流量赠送  </span> <a href="../wap/introllhd.jsp" id="acdetail"><span class="blue">详情>></span></a>
				</div>
			</div>
			<!-- <div>
					<a href="javascript:void(0)" onclick="javascript:unpurchase('1')" class="fr"><span class="gray">退订>></span></a>
			</div> -->
			<div class="dt">金额：</div>
			<div class="dd mt5">
				<span class="total" id="nowprice">5元</span><span class="del" id="dcprice" style="display:none;"></span>
			</div>
			<div class="but-box">
				<button class="btn-sub btn-block btn-submit" id="zfid" onclick="javascript:toZf('4','4','和彩云50GB空间月包','50GB','5','kdyzz');">去支付</button>
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
<div class="cover" id="liuliang" style="display:none">
	<div class="alert">
		<div class="content">
			<div class="tp">
				<div class="center">
					<i class="icon-gou2"></i>
					<h6 id="prst"></h6>
				</div>
				<p id="rdesc">
					您好，您已成功购买了套餐。
				</p>
			</div>
		</div>
		<div class="oper center">
			<p class="gray" id="gray"></p>
		</div>
	</div>
</div>
<div class="cover" id="loading" style="display:none;" >
	<div class="alert">
		<div class="content center">
			<span class="ft18">正在处理</span><i class="icon-rotate"></i>
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