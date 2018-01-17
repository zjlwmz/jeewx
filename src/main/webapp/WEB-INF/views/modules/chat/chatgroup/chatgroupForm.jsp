<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>微信公众帐号信息</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript" src="static/ckfinder/ckfinder.js"></script>
  <script type="text/javascript" charset="utf-8" src="static/Formdesign/js/ueditor/ueditor.config.js"></script>
  <script type="text/javascript" charset="utf-8" src="static/Formdesign/js/ueditor/ueditor.all.js"> </script>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="chatGroupController.do?save" tiptype="1">
		<input id="id" name="id" type="hidden" value="${chatGroup.id }">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							群组名称:
						</label>
					</td>
					<td class="value">
					    <input id="name" name="name" type="text" style="width: 150px" class="inputxt" datatype="*" value="${chatGroup.name}"/>
						<span class="Validform_checktip"></span>
						<label class="Validform_label" style="display: none;">群组名称</label>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							群组头像:
						</label>
					</td>
					<td class="value">
					    <t:upload name="fiels" id="file_upload" extend="*.png;*.jpg;*.jpeg;" buttonText="添加文件" formId="uploadForm" uploader="tFinanceController.do?saveFiles" />
						<span class="Validform_checktip"></span>
						<label class="Validform_label" style="display: none;">群组头像</label>
					</td>
				</tr>
			</table>
		</t:formvalid>
		<form action="" id ="uploadForm"> <input type="hidden" value="${chatGroup.avatar}" id="avatar" name="avatar" /> </form>
 </body>
