<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>${functionName}</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
 </head>
 <body style="overflow-y: hidden" scroll="no">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="${urlPrefix}Controller.do?save">
			<input id="id" name="id" type="hidden" value="">
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							名称:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="langKey" name="name" datatype="*">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				</tr>
			</table>
		</t:formvalid>
 </body>
