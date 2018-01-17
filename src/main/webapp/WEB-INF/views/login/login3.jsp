<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="cn.emay.framework.common.utils.SysThemesUtil,cn.emay.framework.core.enums.SysThemesEnum"%> 
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<%
String lhgdialogTheme="";
String langurl ="";
try{
	String lang = cn.emay.framework.common.utils.BrowserUtils.getBrowserLanguage(request);
	langurl = "static/mutiLang/" + lang +".js";
	SysThemesEnum sysTheme = SysThemesUtil.getSysTheme(request);
	lhgdialogTheme = SysThemesUtil.getLhgdialogTheme(sysTheme);
}catch(Exception exception){
	exception.printStackTrace();
}
%>

<html>
<head>
<title></title>
<link rel="shortcut icon" href="resources/fc/images/icon/favicon.ico">
<script src=<%=langurl%> type="text/javascript"></script>
<!--[if lt IE 9]>
   <script src="static/login/js/html5.js"></script>
  <![endif]-->
<!--[if lt IE 7]>
  <script src="static/login/js/iepng.js" type="text/javascript"></script>
  <script type="text/javascript">
	EvPNG.fix('div, ul, img, li, input'); //EvPNG.fix('包含透明PNG图片的标签'); 多个标签之间用英文逗号隔开。
</script>
  <![endif]-->
<link href="static/login/css/zice.style.css" rel="stylesheet" type="text/css" />
<link href="static/login/css/buttons.css" rel="stylesheet" type="text/css" />
<link href="static/login/css/icon.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="static/login/css/tipsy.css" media="all" />
<style type="text/css">
html {
	background-image: none;
}

label.iPhoneCheckLabelOn span {
	padding-left: 0px
}

#versionBar {
	background-color: #212121;
	position: fixed;
	width: 100%;
	height: 35px;
	bottom: 0;
	left: 0;
	text-align: center;
	line-height: 35px;
	z-index: 11;
	-webkit-box-shadow: black 0px 10px 10px -10px inset;
	-moz-box-shadow: black 0px 10px 10px -10px inset;
	box-shadow: black 0px 10px 10px -10px inset;
}

.copyright {
	text-align: center;
	font-size: 10px;
	color: #CCC;
}

.copyright a {
	color: #A31F1A;
	text-decoration: none
}

.on_off_checkbox {
	width: 0px;
}

#login .logo {
	width: 500px;
	height: 51px;
}
</style>
</head>
<body>
    <div id="alertMessage"></div>
    <div id="successLogin"></div>
    <div class="text_success"><img src="static/login/images/loader_green.gif" alt="Please wait" /> <span><t:mutiLang langKey="common.login.success.wait"/></span></div>
    <div id="login">
        <div class="ribbon" style="background-image: url(static/login/images/typelogin.png);"></div>
        <div class="inner">
            <div class="logo"><img src="static/login/images/head.png" /><img src="static/login/images/foot.png" /></div>
            <div class="formLogin">
                <form name="formLogin" id="formLogin" action="loginController.do?login" check="loginController.do?checkuser" method="post">
                    <input name="userKey" type="hidden" id="userKey" value="D1B5CC2FE46C4CC983C073BCA897935608D926CD32992B5900" />
                    <div class="tip">
                        <input class="userName" name="userName" type="text" id="userName" title="" iscookie="true" nullmsg="" />
                    </div>
                    <div class="tip">
                        <input class="password" name="password" type="password" id="password" title="" nullmsg="" />
                    </div>
                    <div>
                       	<input class="randCode" name="randCode" type="text" id="randCode" title="" value="" nullmsg="" />
                        <div>
                            <img id="randCodeImage" src="randCodeImage" />
                        </div>
                    </div>
                   
                    <div class="loginButton">
                        <div style="float: left; margin-left: -9px;">
                            <input type="checkbox" id="on_off" name="remember" checked="ture" class="on_off_checkbox" value="0" />
                            <span class="f_help"><t:mutiLang langKey="common.remember.user"/></span>
                        </div>                        
                        <div style="float: right; padding: 3px 0; margin-right: -12px;">
                            <div>
                                <ul class="uibutton-group">
                                    <li><a class="uibutton normal" href="#" id="but_login"><t:mutiLang langKey="common.login"/></a></li>
                                    <li><a class="uibutton normal" href="#" id="forgetpass"><t:mutiLang langKey="common.reset"/></a></li>
                                </ul>
                            </div>
                        </div>
                        <div class="clear"></div>
                    </div>
                    <div>
                        <div style="float: right; margin-left:-30px; margin-right: 40px;">
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div class="shadow"></div>
    </div>
    <!--Login div-->
    <div class="clear"></div>
    <div id="versionBar">
        <div class="copyright">&copy; <t:mutiLang langKey="common.copyright"/> <span class="tip"><a href="http://www.jeecg.org"  target="_blank" title=<t:mutiLang langKey="common.platform"/>>jeecg</a> <t:mutiLang langKey="common.browser.recommend"/><a href="http://www.jeecg.org" target="_blank" title=<t:mutiLang langKey="common.platform"/>> Jeecg社区</a></span></div>
    </div>
    <!-- Link JScript-->
    <script type="text/javascript" src="static/jquery/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="static/jquery/jquery.cookie.js"></script>
    <script type="text/javascript" src="static/login/js/jquery-jrumble.js"></script>
    <script type="text/javascript" src="static/login/js/jquery.tipsy.js"></script>
    <script type="text/javascript" src="static/login/js/iphone.check.js"></script>
    <script type="text/javascript" src="static/login/js/login.js"></script>
    <%=lhgdialogTheme %>
</body>
</html>