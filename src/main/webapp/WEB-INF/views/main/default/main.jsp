<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html >
<html>
<head>
<title><t:mutiLang langKey="jeect.platform"/></title>
<t:base type="jquery,easyui,tools,DatePicker,autocomplete"></t:base>
<script type="text/javascript" src="static/easyui/portal/jquery.portal.min.js"></script>
<link rel="stylesheet" type="text/css" href="static/easyui/portal/portal.min.css">
<link rel="shortcut icon" href="images/favicon.ico">
<script type="text/javascript" src="static/modules/sys/index/index_default.js"></script>
<style type="text/css">
a {
	color: Black;
	text-decoration: none;
}

a:hover {
	color: black;
	text-decoration: none;
}
/*update-start--Author:zhangguoming  Date:20140622 for：左侧树调整：加大宽度、更换节点图标、修改选中颜色*/
.tree-node-selected{
    background: #eaf2ff;
}
/*update-end--Author:zhangguoming  Date:20140622 for：左侧树调整：加大宽度、更换节点图标、修改选中颜色*/

.tabs {
    padding-top: 2px !important;
}
</style>

</head>
<body class="easyui-layout" style="overflow-y: hidden" scroll="no">
<input type="hidden" value='<t:mutiLang langKey="common.code"/>' id="common_code"/>
<input type="hidden" value='<t:mutiLang langKey="common.login.name"/>' id="common_login_name"/>
<input type="hidden" value='<t:mutiLang langKey="common.login.time"/>' id="common_login_time"/>
<input type="hidden" value='<t:mutiLang langKey="lang.user.online"/>' id="lang_user_online"/>
<!-- 顶部-->
<div region="north" border="false" title=" JEEWX Framework  <t:mutiLang langKey="system.version.number"/>" style="BACKGROUND: #E6E6FA; height: 85px; padding: 1px; overflow: hidden;">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
    <td align="left" style="vertical-align: text-bottom;"><img src="static/login/images/head.png;"> <img src="static/login/images/foot.png"></td>
    <td align="right" nowrap>
        <table>
            <tr>
                <td valign="top" height="50">
                    <span style="color: #CC33FF"><t:mutiLang langKey="common.user"/>:</span>
                    <span style="color: #666633">${userName }</span>
                    <span style="color: #CC33FF"><t:mutiLang langKey="current.org"/>:</span>
                    <span style="color: #666633">${currentOrgName }</span>
                    <span style="color: #CC33FF"><t:mutiLang langKey="common.role"/>:</span>
                    <span style="color: #666633">${roleName }</span>
                </td>
            </tr>
            <tr>
                <div style="position: absolute; right: 0px; bottom: 0px;">
                    <a href="javascript:void(0);" class="easyui-menubutton" menu="#layout_north_kzmbMenu" iconCls="icon-help">
                        <t:mutiLang langKey="common.control.panel"/>
                    </a>
                    <a href="javascript:void(0);" class="easyui-menubutton" menu="#layout_north_zxMenu" iconCls="icon-back">
                        <t:mutiLang langKey="common.logout"/>
                    </a>
                </div>
                <div id="layout_north_kzmbMenu" style="width: 100px; display: none;">
                    <div onclick="openwindow('<t:mutiLang langKey="common.profile"/>','userController.do?userinfo')">
                        <t:mutiLang langKey="common.profile"/>
                    </div>
                    <div class="menu-sep"></div>
                    <div onclick="add('<t:mutiLang langKey="common.change.password"/>','userController.do?changepassword','', 550, 200)">
                        <t:mutiLang langKey="common.change.password"/>
                    </div>
					<div class="menu-sep"></div>
                    <div onclick="openwindow('<t:mutiLang langKey="common.ssms.getSysInfos"/>','tSSmsController.do?getSysInfos')">
                        <t:mutiLang langKey="common.ssms.getSysInfos"/>
                    </div>
                    <div class="menu-sep"></div>
                    <div onclick="add('<t:mutiLang langKey="common.change.style"/>','userController.do?changestyle','',550,200)">
                        <t:mutiLang langKey="common.my.style"/>
                    </div>
                    
                    <div class="menu-sep" ></div>
                    <div onclick="window.open('http://yun.jeecg.org')">
                         	云应用中心
                    </div>
                    
                    <div onclick="clearLocalstorage()">
                        <t:mutiLang langKey="common.clear.localstorage"/>
                    </div>
                </div>
                <div id="layout_north_zxMenu" style="width: 100px; display: none;">
                    <div class="menu-sep"></div>
                    <div onclick="exit('loginController.do?logout','<t:mutiLang langKey="common.exit.confirm"/>',1);"><t:mutiLang langKey="common.exit"/></div>
                </div>
            </tr>
        </table>
    </td>
    <td align="right">&nbsp;&nbsp;&nbsp;</td>
</tr>
</table>
</div>
<!-- 左侧-->
<div region="west" split="true" href="loginController.do?left" title="<t:mutiLang langKey="common.navegation"/>" style="width: 200px; padding: 1px;"></div>
<!-- 中间-->
<div id="mainPanle" region="center" style="overflow: hidden;">
    <div id="maintabs" class="easyui-tabs" fit="true" border="false">
    <div class="easyui-tab" title="<t:mutiLang langKey="common.dash_board"/>" href="loginController.do?home" style="padding: 2px; overflow: hidden;"></div>
        <c:if test="${map=='1'}">
            <div class="easyui-tab" title="<t:mutiLang langKey="common.map"/>" style="padding: 1px; overflow: hidden;">
                <iframe name="myMap" id="myMap" scrolling="no" frameborder="0" src="mapController.do?map" style="width: 100%; height: 99.5%;"></iframe>
            </div>
        </c:if>
    </div>
</div>
<!-- 右侧 -->
<div collapsed="true" region="east" iconCls="icon-reload" title="<t:mutiLang langKey="common.assist.tools"/>" split="true" style="width: 190px;"
	data-options="onCollapse:function(){easyPanelCollapase()},onExpand:function(){easyPanelExpand()}">

    <div class="easyui-layout" fit="true" border="false">
		<div region="north" border="false" style="height:180px;overflow: hidden;">
			<div id="tabs" class="easyui-tabs" border="false" style="height: 240px">
				<div title='<t:mutiLang langKey="common.calendar"/>' style="padding: 0px; overflow: hidden; color: red;">
					<div id="layout_east_calendar"></div>
				</div>
			</div>
		</div>
		<div region="center" border="false" style="overflow: hidden;">
			<div id="layout_jeecg_onlinePanel" fit="true" border="false" title='<t:mutiLang langKey="common.online.user"/>'>
				<table id="layout_jeecg_onlineDatagrid"></table>
			</div>
		</div>
	</div>
</div>
<!-- 底部 -->
<div region="south" border="false" style="height: 25px; overflow: hidden;">
    <div align="center" style="color: #CC99FF; padding-top: 2px">&copy;
        <t:mutiLang langKey="common.copyright"/>
        <span class="tip">
            <a href="<t:mutiLang langKey="common.company.url" />" title="JEEWX Framework  <t:mutiLang langKey="system.version.number"/>">
            <t:mutiLang langKey="common.company.name"/>  
            <t:mutiLang langKey="system.version.number"/></a>
            (推荐谷歌浏览器，获得更快响应速度)
        </span>
    </div>
</div>
<div id="mm" class="easyui-menu" style="width: 150px;">
    <div id="mm-tabupdate"><t:mutiLang langKey="common.refresh"/></div>
    <div id="mm-tabclose"><t:mutiLang langKey="common.close"/></div>
    <div id="mm-tabcloseall"><t:mutiLang langKey="common.close.all"/></div>
    <div id="mm-tabcloseother"><t:mutiLang langKey="common.close.all.but.this"/></div>
    <div class="menu-sep"></div>
    <div id="mm-tabcloseright"><t:mutiLang langKey="common.close.all.right"/></div>
    <div id="mm-tabcloseleft"><t:mutiLang langKey="common.close.all.left"/></div>
</div>

<c:if test="${showIm }">
	<audio id="chatAudio">
		<source src="static/msg/qq_msg.mp3" type="audio/mpeg"/>
		<source src="static/msg/notify.ogg" type="audio/ogg"/>
		<source src="static/msg/notify.mp3" type="audio/mpeg"/>
		<source src="static/msg/notify.wav" type="audio/wav"/>
	</audio>
	<input type="hidden" id="ws" value="${socketUrl}"/>
	<script type="text/javascript">
	 var userid="${userid}";
	</script>
	<link rel="stylesheet" href="static/layim/layui.layim-v3.0.1/source/layui/css/layui.css" type="text/css"></link>
	<link rel="stylesheet" href="static/webchataudio/css/audio.css" type="text/css"></link>
	<script type="text/javascript" src="static/layim/layui.layim-v3.0.1/source/layui/layui.js"></script>
	<script type="text/javascript" src="static/layim/layin.main.js"></script>
</c:if>

</body>
</html>