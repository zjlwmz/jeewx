<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>调查问卷</title>
    <link href="template/Install/default/css/common.css?v=1.01" rel="stylesheet" type="text/css" />
	<link href="template/Install/default/css/style.css?v=1.01" rel="stylesheet" type="text/css" />
	<!-- Mobile Devices Support @begin -->
	<meta content="no-cache,must-revalidate" http-equiv="Cache-Control">
	<meta content="no-cache" http-equiv="pragma">
	<meta content="0" http-equiv="expires">
	<meta content="telephone=no, address=no" name="format-detection">
	<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
	<meta name="apple-mobile-web-app-capable" content="yes"> <!-- apple devices fullscreen -->
	<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
	<!-- Mobile Devices Support @end -->
	<style type="text/css">
		body {
			background-color: #efeff4;
			font-family: Microsoft YaHei, Helvitica, Verdana, Tohoma, Arial,
				san-serif;
			margin: 0;
			overflow-x: hidden;
			padding: 0;
			color: #666666;
		}
		.newsRe{
			text-align: center;
			padding: 20%;
		}
	</style>

  </head>
  
  <body>
   <div class="newsRe p15 b_tb ">
   		  <img src="webpage/weixin/idea/survey/images/investor_success.png" style="max-width: 50%"></img>
   		 <div style="margin-bottom: 10px;">
   		 	您已完成问卷调查！
   		 </div>
   </div>
  <script type="text/javascript">
  	document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
	    WeixinJSBridge.call('hideToolbar');
	    WeixinJSBridge.call('hideOptionMenu');
	});
  </script>
  </body>
</html>
