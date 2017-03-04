<!DOCTYPE html>
<%@page language="java" import="java.util.*,com.pay.util.SendRequest,com.pay.util.MD5Util" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String phone=String.valueOf(request.getAttribute("phone"));
String channel=String.valueOf(request.getAttribute("channel"));
//String phone = request.getParameter("account");
if(channel==null || channel.equalsIgnoreCase("null")){
	response.sendRedirect(basePath+"web/error.jsp");
    return;
}
//phone="18841617214";
//phone="15195590084";
%>
<html>
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>主页面</title>
<link rel="stylesheet" href="../web/css/base.css">
<link rel="stylesheet" href="../web/css/main.css">
<link rel="stylesheet" href="../web/css/flexslider.css">
<style>
.btn-submit.disabled{
  background:#cbc8c8;
  color:#6c6b6b;
}
</style>
<script type="text/javascript" src="../web/js/jquery-1.10.1.js"></script>
<script type="text/javascript" src="../web/js/jquery.md5.js"></script>
<script type="text/javascript" src="../web/js/jquery.flexslider-min.js"></script>
<script>
var fzsize=0;
$(function(){
	if (<%=phone%> != null){
		$('#ponetext').val('<%=phone%>');
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
				cntType: 'WEB',
				inputTime: timestamp,
				sign: signStr
			},
			timeout : 20000,
			dataType : "json",
			success : function(data) {
				if(data.code == 0){
					if(data.data!=null){
						var list = data.data;	
						var html3="";
						var b=false;
						for(var i=0;i<list.length;i++){
							if(list[i].oprType=='02'){
								html3=html3 +"<li><a href='"+ list[i].detailUrl +"' id='ggurl'><img src='"+ list[i].picUrl+ "' alt='' id='ggid'></a></li>"
							}
							if(list[i].oprType=='01' && !b){
								$("#actitle").html(list[i].oprTitle);
								document.getElementById("acdetail").setAttribute("href",list[i].detailUrl);
								b=true;
							}
						}
						console.log(html3);
						//$("#imgslider").append(html1);
						$("#imgslider").html(html3);
						$("#flexslider").flexslider({
                    		slideshowSpeed: 3000, //展示时间间隔ms
                    		animationSpeed: 300, //滚动时间ms
                    		touch: true //是否支持触屏滑动(比如可用在手机触屏焦点图)
                		});
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
});

function tclistdetail(sin,tcid,tcname,tcspace,ll,index,nowmoney,isdc,dcmoney){
	$('.btn-submit').removeClass('disabled');
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
	if(isdc==1){
		$("#dcprice").html("原价："+dcmoney+"元");
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
	if ((<%=phone%> != null)) {
		document.all.sureForm.submit();
	} else {
		alert("请输入您的号码");
		return false;
	}
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
<form action="<%=path%>/pay/toMarketPageWeb?channel=<%=channel%>" id="confirmPhone" name="confirmPhone" method="post" style="display: none;">
		<input type="text" id="phoneStr" name="phone" value="" />
</form>
<form action="" id="sureForm" name="sureForm" method="post">
<div class="header">
	<div class="limiter">
		<div class="fl logo">
			<a href="http://caiyun.feixin.10086.cn/"><img src="../web/images/logo.png" alt=""></a>
		</div>
	</div>
</div>
<div class="main clearfix">
	<div class="limiter">
		<div class="tt wt">
			<strong>容量套餐</strong>
			<div class="telebox">
				<button class="btn-conf fr" type="button" id="confirm" onclick="javascript:confirmphone();">确认</button>
				<div class="conf">
					<input type="text" id="ponetext"  placeholder="手机号码(限移动用户)">
				</div>
			</div>
		</div>
		<div class="flexslider banner wt" id="flexslider" style="margin:0 0 15px;">
		<ul class="slides" id="imgslider">
			<!-- <li><a href="http://www.sohu.com/" id="ggurl"><img src="../web/images/20160812180853.png" alt="" id="ggid"></a></li>
			<li><a href="http://www.baidu.com/" id="ggurl"><img src="../web/images/banner2.png" alt="" id="ggid"></a></li>		
 -->		</ul>
		</div>
		<div class="block wt mt20">
			<div class="tit"><span>套餐购买</span></div>
			<div class="inner-box">
				<div class="con-tips">
					<em>选择容量套餐：</em>
				</div>
				<div>
					<ul class="list-btn list-btn4 clearfix">
					<li>
						<div class="btn-sizejs selected" id="tclistdetail0" onclick="javascript:tclistdetail('kdyzz','4','和彩云50GB空间月包','50GB',3,0,5);">
							<i class="icon-rec">赠70M</i>
							<span class="inblock">
								<em>50GB</em>
								<p>5元/月</p>
							</span>
						</div>
					</li>
					<li>
						<div class="btn-sizejs"  id="tclistdetail1" onclick="javascript:tclistdetail('kdyzo','5','和彩云50GB空间季度包','50GB',3,1,10);">
							<!-- <i class="icon-rec">赠70M</i> -->
							<span class="inblock">
								<em>50GB</em>
								<p>10元/3个月</p>
							</span>
						</div>
					</li>
					<li>
						<div class="btn-sizejs"  id="tclistdetail2" onclick="javascript:tclistdetail('kdyzy','7','和彩云100GB空间月包','100GB',3,2,8);">
							<i class="icon-rec">赠150M</i>
							<span class="inblock">
								<em>100GB</em>
								<p>8元/月</p>
							</span>
						</div>
					</li>
				</ul>
				</div>
				<div class="con-tips">
					<span class="gray" id="actitle">活动期间每次购买50GB/100GB一个月时效套餐有流量赠送  </span>
					<a href="../web/introllhd.jsp" id="acdetail"><span class="blue">详情>></span></a>
				</div>
				<div class="sub-box text-right">
					<span class="total" id="nowprice">5元</span>&nbsp;&nbsp;<span class="del" id="dcprice" style="display:none;">原价：0元</span>
					<button class="btn-submit" id="zfid" onclick="javascript:toZf('4','4','和彩云50GB空间月包','50GB','5','kdydo');">购买</button>
				</div>
			</div>
		</div>
		<div class="block wt mt10">
			<div class="tit"><span>套餐说明</span></div>
			<div class="inner-box dl-guid">
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
<input type="hidden" id="tcId" name="tcId" value=""/>
<input type="hidden" id="lx" name="lx" value=""/>
<input type="hidden" id="phone" name="phone" value="<%=phone%>"/>
<input type="hidden" id="name" name="name" value=""/>
<input type="hidden" id="space" name="space" value=""/>
<input type="hidden" id="price" name="price" value=""/>
<input type="hidden" id="sin" name="sin" value=""/>
<input type="hidden" id="flag" name="flag" value="web"/>
<input type="hidden" id="channel" name="channel" value="<%=channel%>"/>
</form>
</body>
</html>
