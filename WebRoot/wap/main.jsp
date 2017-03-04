<!DOCTYPE html>
<%@page language="java" import="java.util.*,com.pay.util.SendRequest,com.pay.util.MD5Util" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String openId=String.valueOf(request.getAttribute("openId"));
String token=String.valueOf(request.getAttribute("token"));
String phone=String.valueOf(request.getAttribute("phone"));
String from=String.valueOf(request.getAttribute("from"));
String tcId=String.valueOf(request.getAttribute("tcId"));
String liuliang=String.valueOf(request.getAttribute("liuliang"));
String tctype=String.valueOf(request.getAttribute("tctype"));
String toXiCheUrl=String.valueOf(request.getAttribute("toXiCheUrl"));
//String phone = request.getParameter("account");
if(from!=null && from.equalsIgnoreCase("WAP")){
	if(phone==null || phone.equalsIgnoreCase("null")){
		response.sendRedirect(basePath+"wap/error.jsp");
	    return;
	}
}else{
	/*if(phone==null || phone.equalsIgnoreCase("null")){
		response.sendRedirect(basePath+"wap/error.jsp");
	    return;
	}*/
	if(phone==null || phone.equalsIgnoreCase("null") || openId==null || openId.equalsIgnoreCase("null")
	  		|| token==null || token.equalsIgnoreCase("null")){
		response.sendRedirect(basePath+"wap/error.jsp");
	    return;
	}
}

/* String userkey=MD5Util.MD5Encode(phone+"BGfOQwRAazMhLHmj3etZ1d6AzYLDww", null);
String url="http://cz.umeol.com:6090/dm/v/cz/qureyFlowPhonecost.do";
String data="mobile="+phone+"&type=1&userkey="+userkey;
String hfye=SendRequest.Post(url, data, 10000);
data="mobile="+phone+"&type=2&userkey="+userkey;
String syll=SendRequest.Post(url, data, 10000);
System.out.println("话费余额："+hfye+"|剩余流量："+syll); */
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
	/*$('.tab li').on('click',function(){
		$(this).addClass('active').siblings().removeClass('active');
		$('.tab-con').hide().eq($(this).index()).show();
	});
	 $('.list-btn .btn-size').on('click',function(){
		$(this).addClass('selected');
		$(this).parents('li').siblings().find('.btn-size').removeClass('selected');
	});*/  
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
				      				} <%-- else if('<%=tcId%>'=='7'){
				      					$("#loading").hide();
								 		$('#prst').html("支付成功");
								 		$('#rdesc').html("您好，您已成功购买套餐并获得流量赠送，流量次月1日生效，限生效当月使用。获赠的Nike、adidas、Levi's等名牌商品折扣券，请关注公众号“sanse-mall”并回复“和彩云”领取。");
								 		$('#gray').html("");
										$('#liuliang').attr( 'style', '');
				      				} --%>else{
				      					$("#loading").hide();
								 		$('#prst').html("支付成功");
								 		$('#rdesc').html("您已成功购买套餐并奖励您10元红包套餐,具体使用方法请查看：套餐说明或详情。");
										$('#successbt').attr( 'style', '');
										$('#liuliang').attr( 'style', '');
										/* setTimeout(
					     					function() {
											$('#liuliang').attr( 'style', 'display:none');
											window.location.href=datall.data;
				      						},5000); */
				      				}
								}else{
									if ('<%=tctype%>' == 'HY') {
										$("#loading").hide();
								 		$('#prst').html("支付成功");
								 		$('#rdesc').html("您已成功购买套餐并奖励您10元红包套餐,具体使用方法请查看：套餐说明或详情。");
								 		$('#successbt').attr( 'style', '');
										$('#liuliang').attr( 'style', '');
										setTimeout(function() {
											$('#liuliang').attr( 'style', 'display:none');
											window.location.href='<%=path%>/pay/toWapXiChe?account=<%=phone%>&openId=<%=openId%>&wabp_result=111';
				      					},5000);
			      					} else {
										$("#loading").hide();
								 		$('#prst').html("支付成功");
								 		$('#rdesc').html("您已成功购买套餐并奖励您10元红包套餐,具体使用方法请查看：套餐说明或详情。");
								 		$('#successbt').attr( 'style', '');
										$('#liuliang').attr( 'style', '');
										/* setTimeout(function() {
											$('#liuliang').attr( 'style', 'display:none');
											window.location.href=datall.data;
				      					},3000); */
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
		
	document.getElementById("zfid").disabled = true;
	document.getElementById("zfidcs").disabled = true;
	document.getElementById("zfidhy").disabled = true;
	getOperate();    
	function getTcDetail(){
		var timestamp=new Date().getTime();
		var signStr=$.md5("phone=<%=phone%>&inputTime="+timestamp+"&key=com.chinamobile.mcloudzf").toUpperCase();
		$.ajax({
			type : "POST",
			url : "<%=path%>/pay/getTcDetail",
			data : {
				objid: "123qwer",
				phone: '<%=phone%>',
				inputTime: timestamp,
				sign: signStr
			},
			timeout : 20000,
			dataType : "json",
			success : function(data) {
				if(data.code == 0){
					if(data.data!=null){
						var list = data.data;	
						var html1="";
						var html2="";
						var size=0;
						var b=false;
						var tcArray=new Array();
						for(var i=0;i<list.length;i++){
							for(var j=0;j<i;j++){
								//if(list[i].tctime==list[j].tctime){
								if(list[i].tcpace==list[j].tcpace){
									b=true;
								}
							}
							if(!b){
								if(size==2){
									//html1=html1+"<ul class='list-btn list-btn4' id='tctlist"+size+"'></ul>";
									//html2=html2+"<li><div class='btn-size selected' id='tct"+size+"' onclick='javascript:tct("+list.length+","+size+");'><span class='inblock'><em>"+parseDmy(2,list[i].tctime)+"</em></span></div></li>";
									html1=html1+"<li style='padding: 10px 0.5%;'><div class='btn-size selected' id='tct"+size+"' onclick='javascript:tct("+list.length+","+size+");'><i id='tctllzs"+size+"'></i><span class='inblock'><em>"+list[i].tcpace+"</em><p>"+list[i].tcprice/100+"元/"+parseDmy(1,list[i].tctime)+"</p></span></div></li>";
									html2=html2+"<ul class='list-btn list-btn3' id='ttime"+size+"'></ul>";
								}else{
									//html1=html1+"<ul class='list-btn list-btn4 clearfix' id='tctlist"+size+"' style='display:none;'></ul>";
									//html2=html2+"<li><div class='btn-size' id='tct"+size+"' onclick='javascript:tct("+list.length+","+size+");'><span class='inblock'><em>"+parseDmy(2,list[i].tctime)+"</em></span></div></li>";
									html1=html1+"<li style='padding: 10px 0.5%;'><div class='btn-size' id='tct"+size+"' onclick='javascript:tct("+list.length+","+size+");'><i id='tctllzs"+size+"'></i><span class='inblock'><em>"+list[i].tcpace+"</em><p>"+list[i].tcprice/100+"元/"+parseDmy(1,list[i].tctime)+"</p></span></div></li>";
									html2=html2+"<ul class='list-btn list-btn3' id='ttime"+size+"' style='display:none;'></ul>";
								}
								//tcArray[size]=list[i].tctime;
								tcArray[size]=list[i].tcpace;
								size++;
							}
							b=false;
						}
						fzsize=size;
						$("#tclist").html(html1);
						$("#tctimelist").html(html2);
						/////////////////////////////
						html2="";
						var x;
						for (x in tcArray){
							var tt=tcArray[x];
							for(var n=0;n<list.length;n++){
								if(tt==list[n].tcpace){
									if(list[n].actype=='01'){
										//html2=html2+"<li><div class='btn-size recom' id='tclistdetail"+n+"' onclick=\"javascript:tclistdetail('"+list[n].sin+"','"+list[n].objId+"','"+list[n].tcname+"','"+list[n].tcpace+"',"+list.length+","+n+","+list[n].tcpriceNow/100+");\"><span class='inblock'><em>"+parseDmy(2,list[n].tctime)+"</em></span></div></li>";
										//$("#tct"+x).addClass('recom');
										html2=html2+"<li><div class='btn-size' id='tclistdetail"+n+"' onclick=\"javascript:tclistdetail('"+list[n].sin+"','"+list[n].objId+"','"+list[n].tcname+"','"+list[n].tcpace+"',"+list.length+","+n+","+list[n].tcpriceNow/100+");\"><i class='icon-rec' id='tcliconrec"+n+"'>赠"+parseDll(list[n].sortbyPrice)+"流量</i><span class='inblock'><em>"+parseDmy(2,list[n].tctime)+"</em></span></div></li>";
										$("#tctllzs"+x).html("赠流量");
										$("#tctllzs"+x).addClass('icon-rec');
									}else if(list[n].actype=='02'){
										html2=html2+"<li><div class='btn-size' id='tclistdetail"+n+"' onclick=\"javascript:tclistdetail('"+list[n].sin+"','"+list[n].objId+"','"+list[n].tcname+"','"+list[n].tcpace+"',"+list.length+","+n+","+list[n].tcpriceNow/100+",1,"+list[n].tcprice/100+");\"><span class='inblock'><em>"+parseDmy(2,list[n].tctime)+"</em></span></div></li>";
									}else{
										html2=html2+"<li><div class='btn-size' id='tclistdetail"+n+"' onclick=\"javascript:tclistdetail('"+list[n].sin+"','"+list[n].objId+"','"+list[n].tcname+"','"+list[n].tcpace+"',"+list.length+","+n+","+list[n].tcpriceNow/100+");\"><span class='inblock'><em>"+parseDmy(2,list[n].tctime)+"</em></span></div></li>";
									}
									//$("#tcliconrec6" ).html("特惠大礼包");
									//$("#tclistdetail4" ).addClass('selected');
									//$("#nowprice").html("10元");
									//document.getElementById("zfid").disabled = false;
									//$("#zfid").removeClass('disabled');
								}
							}
							$("#ttime"+x).html(html2);
							html2="";
						}
						/*html1="";
						var x;
						for (x in tcArray){
							var tt=tcArray[x];
							for(var n=0;n<list.length;n++){
								if(tt==list[n].tctime){
									if(list[n].actype=='01'){
										html1=html1+"<li><div class='btn-size recom' id='tclistdetail"+n+"' onclick=\"javascript:tclistdetail('"+list[n].sin+"','"+list[n].objId+"','"+list[n].tcname+"','"+list[n].tcpace+"',"+list.length+","+n+","+list[n].tcpriceNow/100+");\"><span class='inblock'><em>"+list[n].tcpace+"</em><p>"+list[n].tcprice/100+"元/"+ parseDmy(1,list[n].tctime)+"</p></span></div></li>";
										$("#tct"+x).addClass('recom');
									}else if(list[n].actype=='02'){
										html1=html1+"<li><div class='btn-size' id='tclistdetail"+n+"' onclick=\"javascript:tclistdetail('"+list[n].sin+"','"+list[n].objId+"','"+list[n].tcname+"','"+list[n].tcpace+"',"+list.length+","+n+","+list[n].tcpriceNow/100+",1,"+list[n].tcprice/100+");\"><span class='inblock'><em>"+list[n].tcpace+"</em><p>"+list[n].tcprice/100+"元/"+parseDmy(1,list[n].tctime)+"</p></span></div></li>";
									}else{
										html1=html1+"<li><div class='btn-size' id='tclistdetail"+n+"' onclick=\"javascript:tclistdetail('"+list[n].sin+"','"+list[n].objId+"','"+list[n].tcname+"','"+list[n].tcpace+"',"+list.length+","+n+","+list[n].tcpriceNow/100+");\"><span class='inblock'><em>"+list[n].tcpace+"</em><p>"+list[n].tcprice/100+"元/"+parseDmy(1,list[n].tctime)+"</p></span></div></li>";
									}
								}
							}
							$("#tctlist"+x).html(html1);
							html1="";
						}*/
						getTcDetailCS();
						
					}
				}else{
					alert(data.message);
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
					getTcDetail();
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
	
	function getTcDetailCS(){
		var timestamp=new Date().getTime();
		var signStr=$.md5("phone=<%=phone%>&inputTime="+timestamp+"&key=com.chinamobile.mcloudzf").toUpperCase();
		$.ajax({
			type : "POST",
			url : "<%=path%>/pay/getTcDetail",
			data : {
				objid: "123asdf",
				phone: '<%=phone%>',
				inputTime: timestamp,
				sign: signStr
			},
			timeout : 20000,
			dataType : "json",
			success : function(datacs) {
				if(datacs.code == 0){
					if(datacs.data!=null){
						var listcs = datacs.data;	
						var html1="";
						var sizecs=0;
						//var tcArray=new Array();
						for(var i=0;i<listcs.length;i++){
							if(listcs[i].actype=='01'){
							html1=html1+"<li><div class='btn-size disabled recom' id='tctcs"+sizecs+"' onclick=\"javascript:tctcs('"+listcs[i].sin+"',"+listcs.length+","+sizecs+",'"+listcs[i].objId+"','"+listcs[i].tcname+"','"+listcs[i].tcpace+"',"+listcs[i].tcpriceNow/100+",'"+listcs[i].remark+"');\"><p><em>"+listcs[i].tcpace+"</em> <span class='fr'>"+listcs[i].tcprice/100+"元</span></p><span class='gray' id='remark"+sizecs+"'></span></div></li>"
							}else{
							html1=html1+"<li><div class='btn-size disabled' id='tctcs"+sizecs+"' onclick=\"javascript:tctcs('"+listcs[i].sin+"',"+listcs.length+","+sizecs+",'"+listcs[i].objId+"','"+listcs[i].tcname+"','"+listcs[i].tcpace+"',"+listcs[i].tcpriceNow/100+",'"+listcs[i].remark+"');\"><p><em>"+listcs[i].tcpace+"</em> <span class='fr'>"+listcs[i].tcprice/100+"元</span></p><span class='gray' id='remark"+sizecs+"'></span></div></li>"
							}
							sizecs++;
							//fzsize=sizecs+1;
						}

						$("#tclistcs").html(html1);					
						html1="";
					}
					getTcDetailHY();
				}else{
					alert(datacs.message);
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
								$("#remarkhy"+i).html(listhy[i].tcname);
							}else{
								html4=html4+"<li><div class='btn-size' id='tcthy"+sizehy+"' onclick=\"javascript:tcthy('"+listhy[i].sin+"',"+listhy.length+","+sizehy+",'"+listhy[i].objId+"','"+listhy[i].tcname+"','"+listhy[i].tcpace+"',"+listhy[i].tcpriceNow/100+",'"+listhy[i].tcname+"');\"><p><em>"+listhy[i].remark.split("$$")[0]+"</em>"+listhy[i].remark.split("$$")[1]+"<span class='fr'>"+listhy[i].tcpriceNow/100+"元/"+parseDmy(1,listhy[i].tctime)+"</span></p><span class='gray' id='remarkhy"+sizehy+"'></span></div></li>"
								$("#remarkhy"+i).html(listhy[i].tcname);
							}
							sizehy++;
							//fzsize=sizecs+1;
						}

						$("#tclisthy").html(html4);					
						html4="";
						//getHfllcx();
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

	function getHfllcx(){
		var timestamp=new Date().getTime();
		var signStr=$.md5("phone=<%=phone%>&inputTime="+timestamp+"&key=com.chinamobile.mcloudzf").toUpperCase();
			$.ajax({
				type : "POST",
				url : "<%=path%>/pay/getHfllcx",
				data : {
					phone: '<%=phone%>',
					inputTime: timestamp,
					sign: signStr
				},
				timeout : 20000,
				dataType : "json",
				success : function(datacx) {
					if(datacx.code == 0){
						var ye='datacx.data';
						var yeobj = eval('(' + ye + ')'); 
					    if(yeobj.status=='1'){
					    	$("#hfye").prepend("<em>"+yeobj.msg+"</em>");
					    }else{
					      	$("#hfye").prepend("<em>--</em>");
					    }
					    
						var ll=datacx.message;
						var llobj = eval('(' + ll + ')'); 
						console.log(llobj);
					    if(llobj.status=='1'){
					    	$("#syll").prepend("<em>"+parseFloat(llobj.msg).toFixed(2)+"</em>");
					    }else{
					    	$("#syll").prepend("<em>--</em>");
					    }
					}else{
						$("#hfye").prepend("<em>--</em>");
						$("#syll").prepend("<em>--</em>");
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

function tabClick(tabid){
	$('.tab li').removeClass('active');
	$("#"+tabid).addClass('active');
	$('.tab-con').hide();
	$("#"+tabid+"_con").show();
}

function tct(ll,index){
	$('#zfid').addClass('disabled');
	document.getElementById("zfid").setAttribute("onclick","javascript:;");
	document.getElementById("zfid").disabled = true;
	for(var i=0;i<fzsize;i++){
		if(index==i){
				$("#tct"+i).addClass('selected');
				$("#ttime"+i).show();
				$('#aiai').hide();
				//$("#tctlist"+i).show();
		}else{
			$("#tct"+i).removeClass('selected');
			$("#ttime"+i).hide();
			$('#aiai').hide();
			//$("#tctlist"+i).hide();
		}
	}
	for(var i=0;i<ll;i++){
		$("#tclistdetail"+i).removeClass('selected');
	}
	 if (index==1) {
		$('#actitle').html("活动期间每次购买50GB一个月时效套餐有流量赠送 ");
		$('#acdetail').attr('href','../wap/introllhd.jsp');
		$('#aiai').show();
	} else  if (index==2) {
		$('#actitle').html("活动期间每次购买100GB一个月时效套餐有流量赠送 ");
		$('#acdetail').attr('href','../wap/introllhd.jsp');
		$('#aiai').show();
	}  
	$("#nowprice").html("0元");
	$("#dcprice").hide();
}

function tctcs(sin,ll,index,tcid,tcname,tcspace,nowmoney,remark){
	$('#zfidcs').removeClass('disabled');
	document.getElementById("zfidcs").setAttribute("onclick","javascript:toZf('2','"+tcid+"','"+tcname+"','"+tcspace+"','"+nowmoney+"','"+sin+"');");
	document.getElementById("zfidcs").disabled = false;
	for(var i=0;i<ll;i++){
		if(index==i){
			$("#tctcs"+i).addClass('selected');
			$("#remark"+i).html(remark);
		}else{
			$("#tctcs"+i).removeClass('selected');
			$("#remark"+i).html("");
		}
	}
	$("#nowpricecs").html(nowmoney+"元");
	$("#dcpricecs").hide();
}

function tcthy(sin,ll,index,tcid,tcname,tcspace,nowmoney,remark,isdc,dcmoney){
	$('#zfidhy').removeClass('disabled');
	document.getElementById("zfidhy").setAttribute("onclick","javascript:toZf('3','"+tcid+"','"+remark+"','"+tcspace+"','"+nowmoney+"','"+sin+"');");
	document.getElementById("zfidhy").disabled = false;
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

function tclistdetail(sin,tcid,tcname,tcspace,ll,index,nowmoney,isdc,dcmoney){
	$('#zfid').removeClass('disabled');
	document.getElementById("zfid").setAttribute("onclick","javascript:toZf('1','"+tcid+"','"+tcname+"','"+tcspace+"','"+nowmoney+"','"+sin+"');");
	document.getElementById("zfid").disabled = false;
	$("#dcprice").hide();
	for(var i=0;i<ll;i++){
		if(index==i){
			$("#tclistdetail"+i).addClass('selected');
		}else{
			$("#tclistdetail"+i).removeClass('selected');
		}
	}
	if (index==6) {
		$('#actitle').html("活动期间每次购买100GB一个月时效套餐有流量赠送 ");
		$('#acdetail').attr('href','../wap/introllhd.jsp');
		$('#aiai').show();
	} else  if (index==7 || index==8) {
		$('#aiai').hide();
	}
	if (index==3) {
		$('#actitle').html("活动期间每次购买50GB一个月时效套餐有流量赠送 ");
		$('#acdetail').attr('href','../wap/introllhd.jsp');
		$('#aiai').show();
	} else  if (index==4 || index==5) {
		$('#aiai').hide();
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
	
function getHerf(){
	if (<%=phone%> == null) {
		return false;
	}else {
		window.location.href='http://link.weibo.10086.cn:18081/yunpay/pay/tosure?tcId=7&lx=1&phone=<%=phone%>&name=和彩云100GB空间月包&space=100GB&price=8&flag=WAP&sin=kdyzy&openId=<%=openId%>&channel=';
	}
}

function toniiwoo() {
	window.location.href='https://www.niiwoo.com/h5/operation/loanactivityclouds/index.html?channelCode=hcyjiekuan&linkCode=120';
}
</script>
</head>
<body>
<header>
	<!-- <a href="http://caiyun.feixin.10086.cn/" class="lft">
		<i class="icon-back"></i>
	</a> -->
	<h5>空间扩容</h5>
</header>
<form action="" id="sureForm" name="sureForm" method="post">
<section>
	<div class="title">
		账号：<%=phone%>
		<!-- <div class="mt10">
			<ul class="txt-dvd2">
				<li>
					<span class="gray">话费余额：</span>
					<p id="hfye"><small>元</small></p>
				</li>
				<li>
					<span class="gray">剩余流量：</span>
					<p id="syll"><small>M</small></p>
				</li>
			</ul>
		</div> -->
	</div>
	<div class="flexslider banner2" id="flexslider" style="margin:0 0 15px;">
		<ul class="slides" id="imgslider">
			<!-- <li><a href=""><img src="images/banner.png" alt=""></a></li> -->
		</ul>
	</div>

	<ul class="tab tab-dvd3">
		<li class="active" onclick="javascript:tabClick('tabrl');" id="tabrl"><span>容量套餐</span></li>
		<li onclick="javascript:tabClick('tabcsl');" id="tabcsl"><span>传输量套餐</span></li>
		<li onclick="javascript:tabClick('tabhyl');" id="tabhyl"><span>会员套餐</span></li>	
	</ul>
	
	<div class="con-box">
		<div class="tab-con" id="tabrl_con">
			<div class="dt"><span class="green">选择容量套餐:</span><a class="fr"  href="../wap/introduce.jsp">套餐说明></a></div>
			<div class="dd" >
				<ul class="list-btn list-btn4" id="tclist">
					<!-- <li>
						<div class="btn-size selected recom">
							<span class="inblock">
								<em>20GB</em>
								<p>3元/月</p>
							</span>
						</div>
					</li>
					<li>
						<div class="btn-size">
							<span class="inblock">
								<em>50GB</em>
								<p>5元/月</p>
							</span>
						</div>
					</li>
					<li>
						<div class="btn-size">
							<span class="inblock">
								<em>100GB</em>
								<p>8元/月</p>
							</span>
						</div>
					</li>
					<li>
						<div class="btn-size">
							<span class="inblock">
								<em>1TB</em>
								<p>42元/月</p>
							</span>
						</div>
					</li> -->
				</ul>
			</div>
			<div class="dd" style="display:none">
				<div class="con-tips" >
					<span class="gray" id="actitle2"></span> <a href="" id="acdetail2"><span class="blue">详情>></span></a>
				</div>
			</div>
			<div class="dt">选择时效：</div>
			<div class="dd" id="tctimelist">
				<!-- <ul class="list-btn list-btn3" >
					<li>
						<div class="btn-size">
							<span class="inblock">
								<em>1个月</em>
							</span>
						</div>
					</li>
					<li>
						<div class="btn-size">
							<span class="inblock">
								<em>3个月</em>
							</span>
						</div>
					</li>
					<li>
						<div class="btn-size selected recom">
							<span class="inblock">
								<em>1年</em>
							</span>
						</div>
					</li> 
				</ul>-->
			</div>
			<div class="dd" id="aiai">
				<div class="con-tips">
					<span class="gray" id="actitle">活动期间每次购买100GB一个月时效套餐有流量赠送  </span> <a href="../wap/introllhd.jsp" id="acdetail"><span class="blue">详情>></span></a>
				</div> 
			</div>
			<div>
					<a href="javascript:void(0)" onclick="javascript:unpurchase('1')" class="fr"><span class="gray">退订>></span></a>
			</div>
			<div class="dt">金额：</div>
			<div class="dd mt5">
				<span class="total" id="nowprice">0元</span><span class="del" id="dcprice" style="display:none;"></span>
			</div>
			<div class="but-box">
				<!-- 	<button class="btn-sub btn-block btn-submit disabled" id="zfid" onclick="javascript:toZf('1','5','和彩云50GB空间季度包','50GB','10','kdyzo');">去支付</button>  -->		
				<button class="btn-sub btn-block btn-submit disabled" id="zfid" >去支付</button>
 			</div>
		</div>

		<div class="tab-con none" id="tabcsl_con">
			<!-- <div class="dt">传输量套餐：<a class="fr" href="../wap/introcsl.jsp">套餐说明></a></div> -->
			<div class="dt">传输量套餐：<a class="fr" href="../wap/introcsl.jsp"><font color="#FF6100"><strong>关于暂停购买的说明></strong></font></a></div>
			<div class="dd mt10" >
				<ul class="list-btn list-btn-blk" id="tclistcs">
					<!-- <li>
						<div class="btn-size selected recom">
							<p><em>100GB</em> <a class="fr">3元</a></p>
							<span class="gray">支付成功后48小时内生效;购买的传输量永久有效。</span>
						</div>
					</li>
					<li>
						<div class="btn-size">
							<p><em>200GB</em> <span class="fr">6元</span></p>
						</div>
					</li>
					<li>
						<div class="btn-size">
							<p><em>500GB</em> <span class="fr">12元</span></p>
						</div>
					</li>
					<li>
						<div class="btn-size">
							<p><em>2T</em> <span class="fr">50元</span></p>
						</div>
					</li> -->
				</ul> 
			</div>
			<div class="dd mt5">
				<!-- <div class="con-tips">
					<span class="gray" id="actitle3">购买任意套餐，均有流量赠送 详情>></span> <a href="../wap/introllhd.jsp" id="acdetail3"><span class="blue"></span></a>
				</div> -->
			</div>
			<div class="dt">金额：</div>
			<div class="dd mt5">
				<span class="total" id="nowpricecs">0元</span><span class="del" id="dcpricecs" style="display:none;"></span>
			</div>
			<div class="but-box">
				<button class="btn-sub btn-block btn-submit disabled" id="zfidcs">去支付</button>
			</div>
		</div>
		
		<div class="tab-con none" id="tabhyl_con">
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
					查看我的特权 <a href="<%=path%>/pay/toWapXiChe?account=<%=phone%>&openId=<%=openId%>&wabp_result=111"><span class="blue">详情>></span></a>
					<a href="javascript:void(0)" onclick="javascript:unpurchase('3')" class="fr"><span class="gray">退订>></span></a>
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
				<button class="btn-sub btn-block btn-submit disabled" id="zfidhy">去支付</button>
			</div>
			<%-- <div style="text-align: center;margin-top: 15px;"><a href="<%=path%>/pay/toWapXiChe?account=<%=phone%>&openId=<%=openId%>&wabp_result=222"><font color="#E40077" style="font-size: 16px;font-style: normal;text-align:justify;">查看我的特权</font></a></div> --%>
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
			<div id="successbt" class="but-box" style="display:none">
				<button class="btn-block btn-submit" onclick="javascript:toniiwoo()">马上点击拆红包</button>
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