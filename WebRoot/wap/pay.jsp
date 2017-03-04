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
String sin=String.valueOf(request.getAttribute("sin"));
String openId=String.valueOf(request.getAttribute("openId"));
String channel=String.valueOf(request.getAttribute("channel"));
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
<title>空间扩容</title>
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<link rel="stylesheet" href="../wap/css/base.css" type="text/css">
<link rel="stylesheet" href="../wap/css/main.css" type="text/css">
<style>
.btn-sub.disabled{
  background:#cbc8c8;
  color:#6c6b6b;
}
/* textarea {
    border: 0 none white;
    overflow: hidden;
    padding: 3px;
    height: 30px;
    outline: none;
    text-align: center;
    background-color: #D0D0D0;
    resize: none;
    disabled:disabled;
} */
</style>
<script src="../wap/js/rem.js" type="text/javascript"></script>
<script src="../wap/js/jquery-2.0.3.min.js" type="text/javascript"></script>
<script src="../wap/js/base.js" type="text/javascript"></script>
<script src="../wap/js/jquery.md5.js" type="text/javascript"></script>
<script>
$(function(){
		if ('<%=sin%>' == '5') {
				$('#hfzf').attr( 'class', 'flexbox disabled');
				$('#hfzfts').html("话费支付（暂不支持话费支付）");
			}else if ('<%=sin%>' == '6') {
				$('#hfzf').attr( 'class', 'flexbox disabled');
				$('#hfzfts').html("话费支付（目前只支持移动用户）");			
			} else {
				$('#hfzfts').html("话费支付");
		}
		<%-- if (<%=suc%> == 2) {
			$('.cover').attr( 'style', '' )
		} --%>
		var ua = window.navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i) == 'micromessenger' && '<%=lx%>'!='3' && '<%=channel%>'=='内部渠道'){
				$('#wxzf').attr( 'class', 'flexbox');
			}else if ('<%=lx%>'=='3' || '<%=lx%>'=='4') {
				$('#wxzf').attr( 'style', 'display:none')
		}
		<%-- $('.cover').attr( 'style', '' )
		$("#prst").html("支付成功");
		var timestamp=new Date().getTime();
		var signStr=net s$.md5("phone=<%=phone%>&inputTime="+timestamp+"&key=com.chinamobile.mcloudzf").toUpperCase();
		$.ajax({
		type : "POST",
		url : "<%=path%>/pay/getTicket",
		data : {
			phone: '<%=phone%>',
			openId: '<%=openId%>',
			inputTime: timestamp,
			sign: signStr
		},
		timeout : 10000,
		dataType : "json",
		success : function(data) {
					if(data.code == 0){
						$('#touchArea').html(data.data)
						$('#gray').html("")
					}else{
						$('#rdesc').html("您已成功购买<%=name%>。"+ data.message);
						//alert(data.message);
						$('#gray').html("")
					}
		},
		complete : function(XMLHttpRequest,status){
			$("#loading").hide();
			if(status=='timeout'){
				alert(status+"支付请求超时，请检查网络或稍后再试");
			}else if(status=='error'){
				alert(status+"支付请求错误，请检查网络或稍后再试");
			}
		}
	}); --%>
		/* $("#touchArea").on({
		touchstart: function(e){
			timeOutEvent = setTimeout("copy()",500);
		}
		}) */
		
});
	
/* function copy(){
 //var Url2=document.getElementById("touchArea");
 //Url2.select(); // 选择对象
 //window.clipboardData.setData("Text", $('#touchArea').val())
 //document.execCommand("Copy","true",null); // 执行浏览器复制命令
   $("#touchArea").zclip({   
        path:'../js/ZeroClipboard.swf',   
        copy:$('#touchArea').text() 
        });
 alert("已复制好，可贴粘。");
 } */



function zfType(lx,zftype,tcid){
	if(zftype==1){
		if ('<%=sin%>' != '5'&&'<%=sin%>' != '6'){
			$("#hfzf").addClass('selected');
        	$("#wxzf").removeClass('selected');
			$('.btn-sub').removeClass('disabled');
			document.getElementById("zfid").setAttribute("onclick","javascript:toZf('"+ lx +"','"+tcid+"',"+zftype+");");
			document.getElementById("zfid").disabled = false;
			}
	}else if(zftype==2){
		if(isWeiXin() && '<%=lx%>'!='3'){
			$("#wxzf").addClass('selected');
        	$("#hfzf").removeClass('selected');
			$('.btn-sub').removeClass('disabled');
			document.getElementById("zfid").setAttribute("onclick","javascript:toZf('"+ lx +"','"+tcid+"',"+zftype+");");
			document.getElementById("zfid").disabled = false;
			}
	}
	//document.getElementById("zfid").setAttribute("onclick","javascript:toZf('"+ lx +"','"+tcid+"',"+zftype+");");
	//document.getElementById("zfid").disabled = false;
	 <%-- if ('<%=sin%>' != '5'|| isWeiXin()) {
			document.getElementById("zfid").disabled = false;
		} --%>
		else {
			document.getElementById("zfid").disabled = true;
	} 
}

function toZf(lxt,tcid,zftype){
	$('.btn-sub').addClass('disabled');
	document.getElementById("zfid").setAttribute("value","请求支付中...");
	document.getElementById("zfid").setAttribute("onclick","javascript:;");
	document.getElementById("zfid").disabled = true;
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
			cntType: 'WAP',
			openId: '<%=openId%>',
			channel: '<%=channel%>',
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
				}else if(zftype==2){ //微信支付
					if(data.code == 0){
							WeixinJSBridge.invoke('getBrandWCPayRequest',data.data,callback);
						}else{
							document.getElementById("zfid").setAttribute("value","去支付");
							alert(data.message);
						}
				}
			}else{
				alert(data.message);
			}
		},
		complete : function(XMLHttpRequest,status){
			$("#loading").hide();
			if(status=='timeout'){
				document.getElementById("zfid").setAttribute("value","去支付");
				alert(status+"支付请求超时，请检查网络或稍后再试");
			}else if(status=='error'){
				document.getElementById("zfid").setAttribute("value","去支付");
				alert(status+"支付请求错误，请检查网络或稍后再试");
			}
		}
	});
}

function callback(res){ 
     document.getElementById("zfid").setAttribute("value","去支付");
     WeixinJSBridge.log(res.err_msg); 	     	     				 
     if(res.err_msg=='get_brand_wcpay_request:ok'){ //发送成功
			var timestamp=new Date().getTime();
			var signStr=$.md5("phone=<%=phone%>&inputTime="+timestamp+"&key=com.chinamobile.mcloudzf").toUpperCase();
			$('#wxzf').removeClass('selected');
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
				success : function(data) {
					if(data.code == 0){
						<%-- if ('<%=tcId%>'=='5') { --%>
								$("#loading").hide();
								$('#prst').html("支付成功");
								$('#rdesc').html("您已购买成功并获得半年乐视会员+70M流量特惠礼包，乐视兑换码是 <span><strong id='lshyc'></strong></span>，请长按复制或截图保存券码并在“中国移动和彩云”微信后台回复“乐视会员”查看兑换攻略，兑换截止时间为2016年12月31日，逾期兑换无效。流量即时赠送，于次月1日生效，生效当月使用。");
								$('#lshyc').html(data.data)
								$('#gray').html("")
								$('#tanchuang').attr( 'style', '');
						<%-- } else if ('<%=tcId%>'=='1'){
								$("#loading").hide();
								$('#prst').html("支付成功");
								$('#rdesc').html("您已成功购买套餐并获得腾讯大礼包1份。礼包兑换码是 <span><strong id='lshyc'></strong></span>，请长按复制或截图保存券码并在“中国移动和彩云”微信后台回复“大礼包”领取礼包，体验《梦幻诛仙》获取70M流量。截止时间为2016年11月20日，逾期兑换无效。");
								$('#lshyc').html(data.data)
								$('#gray').html("")
								$('#tanchuang').attr( 'style', '');
						} --%>
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
									<%-- if ('<%=tcId%>'=='7') {
										$("#loading").hide();
							 			$('#prst').html("支付成功");
							 			$('#rdesc').html("您好，您已成功购买套餐并获得流量赠送，流量次月1日生效，限生效当月使用。获赠的Nike、adidas、Levi's等名牌商品折扣券，请关注公众号“sanse-mall”并回复“和彩云”领取。");
							 			$('#gray').html("");
										$('#tanchuang').attr( 'style', '');
									} else { --%>
										$("#loading").hide();
							 			$('#prst').html("支付成功");
							 			$('#rdesc').html("您已成功购买套餐并奖励您10元红包套餐,具体使用方法请查看：套餐说明或详情。");
							 			$('#gray').html("");
										$('#successbt').attr( 'style', '');
										$('#tanchuang').attr( 'style', '');
									/* } */
								}else{
									$("#loading").hide();
							 		$('#prst').html("支付成功");
							 		$('#rdesc').html("您已成功购买套餐并奖励您10元红包套餐,具体使用方法请查看：套餐说明或详情。");
							 		$('#gray').html("");
							 		$('#successbt').attr( 'style', '');
									$('#tanchuang').attr( 'style', '');
									/* setTimeout("$('.tanchuang').attr( 'style', 'display:none')",3000); */
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
		//setTimeout("dyp()",1000);
     	//setTimeout("$('.cover').attr( 'style', 'display:none')",3000);
     	//$("#wxzf").removeClass('selected');
     }else if(res.err_msg=='gget_brand_wcpay_request:cancel'){ //用户取消 
     	$('#prst').html("购买失败!");
     	$('#rdesc').html("您已取消支付");
     	$('#tanchuang').attr( 'style', '' )
     	setTimeout("$('#tanchuang').attr( 'style', 'display:none')",3000);
        $('#wxzf').removeClass('selected');
     }else{ //get_brand_wcpay_request:fail  发送失败 
		$('#prst').html("购买失败!");
     	$('#rdesc').html("");
     	$('#tanchuang').attr( 'style', '' )
     	setTimeout("$('#tanchuang').attr( 'style', 'display:none')",3000);
     	$('#wxzf').removeClass('selected');
     	//$("#rdesc").html(res.err_code+":"+res.err_desc);
		//alert(msg+":"+res.err_code+" err_desc="+res.err_desc+" err_msg="+res.err_msg); 	
	}
	
};

function dyp(){
		var timestamp=new Date().getTime();
		var signStr=$.md5("phone=<%=phone%>&inputTime="+timestamp+"&key=com.chinamobile.mcloudzf").toUpperCase();
		$.ajax({
				type : "POST",
				url : "<%=path%>/pay/getTicket",
				data : {
					phone: '<%=phone%>',
					openId: '<%=openId%>',
					inputTime: timestamp,
					sign: signStr
				},
				timeout : 10000,
				dataType : "json",
				success : function(data) {
					if(data.code == 0){
						$('#touchArea').html(data.data)
						$('#gray').html("")
					}else{
						$('#rdesc').html("您已成功购买<%=name%>。"+ data.message);
						//alert(data.message);
						$('#gray').html("")
					}
				},
				complete : function(XMLHttpRequest,status){
					$("#loading").hide();
					if(status=='timeout'){
						alert(status+"请求超时，请检查网络或稍后再试");
					}else if(status=='error'){
						alert(status+"请求错误，请检查网络或稍后再试");
					}
				}
		});
}

function isWeiXin(){
	var ua = window.navigator.userAgent.toLowerCase();
	if(ua.match(/MicroMessenger/i) == 'micromessenger'){
		return true;
	}else{
		return false;
	}
}

function toniiwoo() {
	window.location.href='https://www.niiwoo.com/h5/operation/loanactivityclouds/index.html?channelCode=hcyjiekuan&linkCode=120';
}
</script>
</head>
<body>
<header>
	<h5>选择支付方式</h5>
</header>

<section>
	<div class="title">
		<a href="javascript:history.go(-1);">
			<i class="icon-back back2"></i> 返回
		</a>
	</div>
	<div class="high-box">
		<!-- <i class="icon-size2 month"><em>20GB</em></i> -->
		<i class="icon-size3"><em><%=space%></em></i>
		<span class="inblock">
			<h5><%=price%>元</h5>
			<p><%=name%></p>
		</span>
	</div>
	<ul class="list-pay">
		<!-- <li class="flexbox disabled">
			<div class="cell">
				<i class="icon-pay pay1"></i><i>支付宝支付</i>
			</div>
		</li> -->
		<li class="flexbox disabled" id="wxzf" onclick="javascript:zfType('<%=lx%>','2','<%=tcId%>');">
			<div class="cell">
				<i class="icon-pay pay2"></i><i>微信支付</i>
			</div>
		</li>
		<li class="flexbox" id="hfzf" onclick="javascript:zfType('<%=lx%>','1','<%=tcId%>');">
			<div class="cell" >
				<i class="icon-pay pay3"></i><i id="hfzfts"></i>
			</div>
		</li>
	</ul>
	<!-- <div class="flexbox getcode">
		<div class="cell">
			<div class="code-box">
				<input type="text" placeholder="短信验证码">
			</div>
		</div>
		<span class="inblock">
			<button class="btn-getcode">获取验证码</button>
		</span>
	</div> -->
	<div class="but-box but-spe-box">
		<button class="btn-sub btn-block disabled" id="zfid">确认支付</button>
	</div>
	<div style="text-align: center;margin-top: 20px;"><font color="#0066FF" style="font-size: 16px;font-style: normal;text-align:justify;">温馨提示：购买的空间容量将在24小时内生效</font></div>
</section>

<div class="cover" style="display:none;" id="tanchuang">
	<div class="alert">
		<div class="content">
			<div class="tp">
				<div class="center">
					<i class="icon-gou2"></i>
					<h6 id="prst"></h6>
				</div>
				<p id="rdesc">
					<%-- 您已成功购买<%=name%>。 --%>
				</p>
				<div class="but-box" style="display:none" id="successbt">
					<button class="btn-block btn-submit" onclick="javascript:toniiwoo()">马上点击拆红包</button>
				</div>
			</div>
		</div>
		<div class="oper center">
			<p class="gray" id="gray">3秒后自动关闭</p>
		</div>
	</div>
</div>
<div class="cover" style="display:none;" id="loading">
	<div class="alert">
		<div class="content center">
			<span class="ft18">正在处理</span><i class="icon-rotate"></i>
		</div>
	</div>
</div>
<script>
	/* $(function(){
		$('.list-pay li').not('.disabled').on('click',function(){
			$(this).addClass('selected').siblings().removeClass('selected');
		});
	}) */
</script>
</body>
</html>
