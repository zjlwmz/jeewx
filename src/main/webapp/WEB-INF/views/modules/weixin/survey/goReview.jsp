<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>调查问卷</title>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
	<!-- Mobile Devices Support @begin -->
	<meta content="no-cache,must-revalidate" http-equiv="Cache-Control">
	<meta content="no-cache" http-equiv="pragma">
	<meta content="0" http-equiv="expires">
	<meta content="telephone=no, address=no" name="format-detection">
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<meta name="apple-mobile-web-app-capable" content="yes"> <!-- apple devices fullscreen -->
	<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
	<!-- Mobile Devices Support @end -->
	<meta name="keywords" content="">
	<meta name="description" content="">
	<script type="text/javascript" src="plug-in/jquery/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="webpage/weixin/idea/survey/js/common.js"></script>
	<link type="text/css" rel="stylesheet" href="webpage/weixin/idea/survey/css/bootstrap.css">
	<link rel="stylesheet" type="text/css" href="webpage/weixin/idea/survey/css/survey.css" media="all">
	<script type="text/javascript" src="webpage/weixin/idea/survey/js/bootstrap.js"></script>
	<script type="text/javascript" src="webpage/weixin/idea/survey/js/jquery.touchwipe.js"></script>
	<script type="text/javascript" src="webpage/weixin/idea/survey/js/alert.js"></script>
	<style>
	/*重定义bootstrap样式*/
	select, textarea, input[type="text"], input[type="password"], input[type="datetime"], input[type="datetime-local"], input[type="date"], input[type="month"], input[type="time"], input[type="week"], input[type="number"], input[type="email"], input[type="url"], input[type="search"], input[type="tel"], input[type="color"], .uneditable-input{width:100%; margin-bottom:0; box-sizing:border-box; -webkit-box-sizing:border-box; -moz-box-sizing:border-box;}
	input[type="text"], input[type="password"], input[type="datetime"], input[type="datetime-local"], input[type="date"], input[type="month"], input[type="time"], input[type="week"], input[type="number"], input[type="email"], input[type="url"], input[type="search"], input[type="tel"], input[type="color"], .uneditable-input{height:30px;}
	.input-append, .input-prepend{width:100%; margin-bottom:0;}
	select{padding:0 5px; line-height:28px; -webkit-appearance:button;}
	.checkbox.inline{margin-top:0;}
	.checkbox.inline + .checkbox.inline {margin-left:0;}
	.checkbox input[type="checkbox"]{filter:alpha(opacity:0); opacity:0;}
	.file{position:relative;}
	.file input[type="file"]{position:absolute; top:0; left:0; width:100%; filter:alpha(opacity:0); opacity:0;}
	.file button{width:100%; text-align:left;}
	.form-item{border-left:3px #ED2F2F solid; padding-left:10px; height:30px; line-height:30px; background:#F7F7F7; margin-bottom:10px;}
	.main{
  		background: #f5f5f5 !important;
	}
	.title{
		  color: #000 !important;
	}
	.tip{
	 color: #000 !important;
	}
	</style>
    <script type="text/javascript">
	function dosurvey(){
		var obj = $.ajax({
		url : 'weixinSurveyController.do?doSurvey&shopSymbol=shop',
		type : 'post',
		dataType:"JSON",
		data : $("#surveyform").serialize(),
		success:function(data){
			if(data.success){
				alert("调研结束，谢谢您的参与。");
				location.href="weixinSurveyController.do?goSurveyResultShow&id="+$("#mainid").val();
			}
		}
	});
}
		

</script>
</head>
<body>
<div class="main" style="height: 949px;">
	<div style="margin-bottom: 5px;">
		<img src="webpage/weixin/idea/survey/images/back.jpg"></img>
	</div>
	<div class="content-w1 survey-title" style="display: none;">
		<div class="content-w2">
			<div class="content">
				<div class="title">${weixinSurveyMain.surveyTitle }</div>
				<img class="connect" src="webpage/weixin/idea/survey/images/connect.png">
				<div class="desc-cont"><p>${weixinSurveyMain.surveyDescription }</p></div>
				<!-- 提交调研超过设定的次数，不显示按钮 -->
				<a class="next-btn" href="javascript:void(0)" style="text-decoration:none;">马上开始</a>
			</div>
		</div>
	</div>
	<form action="" method="post" id="surveyform">
		<div class="question" style="overflow: hidden; display: block;">
			<!-- 调研列表开始 -->
			<c:forEach items="${WeixinSurveyList }" var="weixinSurvey" varStatus="surveystatus">
				<div class="xuan" style="display: none;" title="${weixinSurvey.surveyType }" sur="${weixinSurvey.id }">
					<div class="title"><span>${surveystatus.index+1}/${weixinSurveyCount}</span> ${weixinSurvey.surveyTitle }</div>
					<div class="tip">
						<c:if test="${weixinSurvey.surveyType==1 }">注：本题最多能选择1个答案！</c:if>
						<c:if test="${weixinSurvey.surveyType==2 }">注：本题可以选择多个答案！</c:if>
						<c:if test="${weixinSurvey.surveyType==3 }">注：本题需要输入答案！</c:if>
						<c:if test="${weixinSurvey.surveyType==4 }">注：本题需要下拉选择！</c:if>
					</div>
					<c:if test="${weixinSurvey.surveyType==1 || weixinSurvey.surveyType==2}">
						<div class="options" title="${weixinSurvey.surveyType }" sur="${weixinSurvey.id }">
							<c:forEach items="${weixinSurvey.surveyOptionList }" var="surveyOption">
								<div class="option">
									<img class="oimg" src="webpage/weixin/idea/survey/images/option_bg.png">
									<img class="oimg-sel" src="webpage/weixin/idea/survey/images/option_sel_bg.png">
									<div class="text">
										<div class="otext">${surveyOption.optionName }</div><div class="ohide" style="display: none">${surveyOption.id }</div>
									</div>
								</div>
							</c:forEach>
							<input type="hidden" name="answer" class="hidden" />
						</div>
					</c:if>
					
					<c:if test="${weixinSurvey.surveyType==3}">
						<div class="options" title="${weixinSurvey.surveyType }" sur="${weixinSurvey.id }">
							<div style="padding: 10px;">
								<input type="text" class="optionText"/>
							</div>
							<input type="hidden" name="answer" class="hidden" />
						</div>
						
					</c:if>
					
					<c:if test="${weixinSurvey.surveyType==4}">
						<div class="options" title="${weixinSurvey.surveyType }" sur="${weixinSurvey.id }" style="padding: 10px;">
							<select class="optionSelect">
								<c:forEach items="${weixinSurvey.surveyOptionList }" var="surveyOption">
									<option value="${surveyOption.id }">${surveyOption.optionName }</option>
								</c:forEach>
							</select>
							<input type="hidden" name="answer" class="hidden" />
						</div>
					</c:if>
					<div class="tip" style="padding-right:20px">${weixinSurvey.surveyDescription }</div>
					<div class="submit" title="${weixinSurvey.surveyTitle }">
						<img src="webpage/weixin/idea/survey/images/next_btn.png">
						<span>下一题</span>
					</div>
				</div>
			</c:forEach>
			
			<div class="xuan" style="display: none;">
				<div class="content-w2">
					<div class="content">
						<div class="title" style="font-size:16px;font-weight:bold;color:#000;"></div>
						<img class="connect" src="webpage/weixin/idea/survey/images/connect(1).png">
						<div style="text-align: left;">感谢您的支持，新时代将一如既往地为您提供更专业、更及时、更真诚地服务！</div>
					</div>
				</div>
			</div>
				
		</div>
		<div id="hiddenarea">
			<input type="hidden" id="accountid" name="accountid" value="NULL-NULL-NULL-NULL-NULL-NULL-NULL-NULL-NULL" />
			<input type="hidden" id="openid" name="openid" value="${openid}" />
			<input type="hidden" id="mainid" name="mainid" value="${weixinSurveyMain.id }" />
		</div>
	</form>
	<div>
	</div>
	<script>
	document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
		WeixinJSBridge.call('hideToolbar');
		WeixinJSBridge.call('hideOptionMenu');
	});
	var openid="${openid}";
	var memberId="${memberId}";
	$(function(){
		$(".content-w1,.question").hide();
		$(".content-w1").css("display", "inline-block");
		$(".xuan").hide();
		
		$(".next-btn").click(function(){
			$(".survey-title,.question").hide();
			$(".question").show();	
		});
		
		$(".xuan:first").show();	
		$(".option").on("click",function(){
			var $option = $(this);
			var parent=$(this).parent();
			var type=parent.attr("title");	
			var surveyid =parent.attr("sur");
			if(type=='1'){
				//单选赋值
				parent.find('.option').removeClass('option-sel');
				$option.toggleClass("option-sel");
				var otext = parent.find(".option-sel .otext").html();
				var oid = parent.find(".option-sel .ohide").html();
				parent.find(".hidden").val(surveyid+"_"+otext+"_"+oid);	
				 
			}else if(type=='2'){
				//多选赋值
				$option.toggleClass("option-sel");
				//答案内容数组
				var valarr=new Array();
				//答案id数组
				var idarr=new Array();
				parent.find(".option-sel .otext").each(function(index){
					valarr[index]=$(this).html();
				});
				//通过样式表获取对应的文本内容
				parent.find(".option-sel .ohide").each(function(index){
					idarr[index]=$(this).html();		
				});
				var valstr=valarr.join(";");//;
				valstr = surveyid+"_"+valstr;
				var idstr = idarr.join(";");
				idstr = valstr+"_"+idstr;
				parent.find(".hidden").val(idstr);
					
			}else if(type=='3'){
				//填空
			}
			else if(type=='4'){
				//下拉选择
			}
			//$("#myaudio")[0].play();
		});
		
		
		
		
		$(".submit").click(function(){
			var parent=$(this).parent();
			var maxsel = $(this).attr("title");
			var $btn = $(this);
			var type=parent.attr("title");	
			if($btn.hasClass("disabled")) return;
			var $answer = parent.find(".options .option-sel");
			
			//填空
			if(type==3){
				var answerValue=parent.find(".optionText").val();
				if(answerValue.replace(/^ +| +$/g,'')==''){
		   	 		 alert("请输入答案!");
		        	 return;
	   	 		}
	   	 		
	   	 		if(answerValue.length>200){
	   	 			alert("输入答案过长！");
		        	 return;
	   	 		}
			}
			
			if(type!=3 && type!=4){
				//parent.find(".hidden").val(valstr);	
				//设置答案
				if($answer.size() == 0){
					alert("请选择一个答案!");
					return;
				}
			}else{
				//获取当前题目的ID
				var surveyid = parent.attr("sur");
				//设置值
				var otext = parent.find(".optionText").val();
				parent.find(".hidden").val(surveyid+"_"+otext);	
			}
			
			//下拉选择
			if(type==4){
				//获取当前题目的ID
				var surveyid = parent.attr("sur");
				var text=parent.find('select option:selected').text();
				var selectid=parent.find(".optionSelect").val();
				parent.find(".hidden").val(surveyid+"_"+text+"_"+selectid);	
				
			}
			
			
			if($answer.size() > maxsel){
				alert("本题最多只能选择个"+maxsel+"答案!");
				return;
			}
			
			var size=$(".question .xuan").length;
			var index=$(".question .xuan").index($(this).parents(".xuan"));
			
			//完成按钮
			if(index==size-3){
			
				parent.next().find(".submit span").text("完成");
				
			}else if(index==size-2){
			
				//调查问卷提交
				var answer=$("#surveyform").find("[name='answer']");
				var answerList=[];
				
				$(answer).each(function(index,obj){
					answerList.push($(obj).val());
				});
				
				var mainid=$("#mainid").val();
				
				$.ajax({
				  	method:"POST",
				  	url:"weixinSurveyMainController.do?questionnaireSurvey",
				 	data:{
					  	answerList : answerList.join("&#44;"),//,
					  	mainid : mainid,
					  	openid : openid,
					  	memberid : memberId
					},
					success:function(){
					  
					}
					
				});
				
			}
			$(".xuan").hide();
			parent.next().show();
		});
		
		$("#suggest").focus(function(){
		
			$(this).val("");
			
		});
		
		$(".next-step").click(function(){
		
			var parent=$(this).parent().parent().parent();
			$(".xuan").hide();
			parent.next().show();
			
		});
		
		
		$("#finish").click(function(){
		
			$("#form").submit();
			
		});
	});
	//调整高度
	$(function() {
		$(".main").height($(window).height());
	});
	$(window).resize(function(){
		$(".main").height($(window).height());
	});
	</script>
	<div id="footer"></div>
	<script type="text/javascript">
	$(function() {
		$(".user-box .box-item").each(function(i) {
			i = i +1;
			if(i%3 == 0) $(this).css("border-right", "0");
		});
		$(window).scroll(function(){
			$(".menu-button").find("i").removeClass("icon-minus-sign").addClass("icon-plus-sign");
			$(".menu-main").hide();
		});
		$(".menu-main a").click(function(){ $(".menu-main").hide(); });
	
		//控制tab宽度
		var profile_tab = $(".nav-tabs li");
		profile_tab.css({"width": 100/profile_tab.length+"%", "text-align": "center"});
	
		//手机表单处理
		$(".form-table").delegate(".checkbox input[type='checkbox']", "click", function(){
			$(this).parent().toggleClass("btn-inverse");
		});
		$(".form-table").delegate(".file input[type='file']", "change", function(){
			var a = $(this).next("button");
			a.html(a.html() +' '+  $(this).val());
		});
	
		//处理固定横向导航条
		var navbarFixedTop = false, navbarFixedBottom = false;
		navbarFixedTop = $(".navbar").hasClass("navbar-fixed-top");
		navbarFixedBottom = $(".navbar").hasClass("navbar-fixed-bottom");
		if(navbarFixedTop) $("body").css("padding-top", "41px");
		if(navbarFixedBottom) $("body").css("padding-bottom", "41px");
	});
	</script>
	</div>
  </body>
</html>
