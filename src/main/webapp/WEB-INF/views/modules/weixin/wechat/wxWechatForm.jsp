<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>微信公众帐号信息</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript" src="static/ckfinder/ckfinder.js"></script>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="wxWechatController.do?save" tiptype="1">
		<input id="id" name="id" type="hidden" value="${wxWechatPage.id }">
		<input id="addtoekntime" name="addtoekntime" type="hidden"  />
		<input id="accountaccesstoken" name="accountaccesstoken" type="hidden"/>
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							公众帐号名称:
						</label>
					</td>
					<td class="value">
					    <input id="accountname" name="name" type="text" style="width: 150px" class="inputxt" datatype="*" value="${wxWechatPage.name}"/>
						<span class="Validform_checktip"></span>
						<label class="Validform_label" style="display: none;">公众帐号名称</label>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							公众帐号TOKEN:
						</label>
					</td>
					<td class="value">
					     <input id="token" name="token" type="text" style="width: 150px" class="inputxt" datatype="*" value="${wxWechatPage.token}"/>
						 <span class="Validform_checktip"></span>
					     <label class="Validform_label" style="display: none;">公众帐号TOKEN</label>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							公众微信号:
						</label>
					</td>
					<td class="value">
					    <input id="username" name="username" type="text" style="width: 150px" class="inputxt" value="${wxWechatPage.username}"/>
						<span class="Validform_checktip"></span>
						<label class="Validform_label" style="display: none;">公众微信号</label>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							原始ID:
						</label>
					</td>
					<td class="value">
					     <input id="original" name="original" type="text" style="width: 150px" class="inputxt" datatype="*" value="${wxWechatPage.original}" />
						 <span class="Validform_checktip"></span>
						 <label class="Validform_label" style="display: none;">原始ID</label>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							公众号类型:
						</label>
					</td>
					<td class="value">
					    <t:dictSelect field="level" typeGroupCode="wx_wechat_type" hasLabel="false" defaultVal="${wxWechatPage.level }"></t:dictSelect>
						<span class="Validform_checktip"></span>
						<label class="Validform_label" style="display: none;">公众号类型</label>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							电子邮箱:
						</label>
					</td>
					<td class="value">
					    <input id="email" name="email" type="text" style="width: 150px" class="inputxt" >
						<span class="Validform_checktip"></span>
						<label class="Validform_label" style="display: none;">电子邮箱</label>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							公众帐号描述:
						</label>
					</td>
					<td class="value">
					    <input id="remarks" name="remarks" type="text" style="width: 150px" class="inputxt" >
						<span class="Validform_checktip"></span>
						<label class="Validform_label" style="display: none;">公众帐号描述</label>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							公众帐号APPID:
						</label>
					</td>
					<td class="value">
					    <input id="appid" name="appid" type="text" style="width: 150px" class="inputxt" datatype="*" value="${wxWechatPage.appid}"/>
						<span class="Validform_checktip"></span>
						<label class="Validform_label" style="display: none;">公众帐号APPID</label>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							公众帐号APPSECRET:
						</label>
					</td>
					<td class="value">
					    <input id="secret" name="secret" type="text" style="width: 150px" class="inputxt"  datatype="*" value="${wxWechatPage.secret}" />
						<span class="Validform_checktip"></span>
						<label class="Validform_label" style="display: none;">公众帐号APPSECRET</label>
					</td>
				</tr>
			</table>
		</t:formvalid>
 </body>
