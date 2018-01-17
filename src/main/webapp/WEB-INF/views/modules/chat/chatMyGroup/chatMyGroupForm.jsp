<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>聊天分组</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript" src="static/ckfinder/ckfinder.js"></script>
  <script type="text/javascript">
  	   $(document).ready(function(){
  		   var isdefault="${chatMyGroup.isdefault}";
  		   $("[name='isdefault']").removeAttr("checked");
		   $("[name='isdefault'][value='"+isdefault+"']").attr("checked",'checked'); 
  	   });
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="chatMyGroupController.do?save" tiptype="1">
		<input id="id" name="id" type="hidden" value="${chatMyGroup.id }">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							分组名称:
						</label>
					</td>
					<td class="value">
					    <input id="name" name="name" type="text" style="width: 150px" class="inputxt" datatype="*" value="${chatMyGroup.name}"/>
						<span class="Validform_checktip"></span>
						<label class="Validform_label" style="display: none;">分组名称</label>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							是否默认分组:
						</label>
					</td>
					<td class="value">
						是&nbsp;<input type="radio" name="isdefault" datatype="*" value="0" checked="checked"/>&nbsp;&nbsp;
						不是&nbsp;<input type="radio" name="isdefault" datatype="*" value="1"/>
					</td>
				</tr>
			</table>
		</t:formvalid>
 </body>
