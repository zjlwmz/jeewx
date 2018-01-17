<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>

<!-- Link JScript-->
<script type="text/javascript" src="static/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="static/jquery/jquery.cookie.js"></script>
<script type="text/javascript" src="static/login/js/jquery-jrumble.js"></script>
<script type="text/javascript" src="static/login/js/jquery.tipsy.js"></script>
<script type="text/javascript" src="static/login/js/iphone.check.js"></script>
<script type="text/javascript" src="static/login/js/login.js"></script>
<script type="text/javascript">
	    //判断如果当前页面不为主框架，则将主框架进行跳转
	  	var tagert_URL = "<%=request.getContextPath()%>/loginController.do?login";
	    if(self==top){
	    	window.location.href = tagert_URL;
	    }else{
	    	top.location.href = tagert_URL;
	    }
	  </script>
</body>
</html>