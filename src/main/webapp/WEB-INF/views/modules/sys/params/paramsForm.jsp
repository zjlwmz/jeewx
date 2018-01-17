<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title><t:mutiLang langKey="lang.maintain"/></title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
 </head>
 <body style="overflow-y: hidden" scroll="no">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="paramsController.do?save">
			<input id="id" name="id" type="hidden" value="${params.id }">
			<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							参数类型:
						</label>
					</td>
					<td class="value">
						<t:dictSelect field="paramsType" typeGroupCode="params_type" hasLabel="false" defaultVal="${params.paramsType }" datatype="*"></t:dictSelect>
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							参数名称:
						</label>
					</td>
					<td class="value">
						<input class="inputxt" id="langKey" name="paramName" value="${params.paramName}" datatype="*">
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							参数值:
						</label>
					</td>
					<td class="value">
						<textarea name="paramsValue" datatype="*"  style="width: 250px;height: 100px;">${params.paramsValue }</textarea>
						<span class="Validform_checktip"></span>
					</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							备注
						</label>
					</td>
					<td class="value">
						<textarea name="remarks" style="width: 250px;height: 100px;">${params.remarks }</textarea>
					</td>
				</tr>
			</table>
		</t:formvalid>
 </body>