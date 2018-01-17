<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>调查问卷</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript" src="plug-in/ckeditor_new/ckeditor.js"></script>
  <script type="text/javascript" src="plug-in/ckfinder/ckfinder.js"></script>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="weixinSurveyMainController.do?weixinSurveyMaindoAdd" tiptype="1">
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td align="right">
				<input type="hidden" name="id" value="${surveyMainPage.id }">
				<label class="Validform_label">
					 调研问卷:
				</label>
			</td>
			<td class="value">
				<textarea id="surveyTitle" rows="2" cols="30" datatype="*" name="surveyTitle">${surveyMainPage.surveyTitle }</textarea>
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;"> 调研问卷</label>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="Validform_label">
					 调研描述:
				</label>
			</td>
			<td class="value">
				<textarea rows="2" cols="30" id="surveyDescription" datatype="*" name="surveyDescription">${surveyMainPage.surveyDescription }</textarea>
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">调研描述</label>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="Validform_label">
					 积分奖励:
				</label>
			</td>
			<td class="value">
				<input type="text" id="integral" datatype="d" name="integral" value="${surveyMainPage.integral }"/>
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">积分奖励</label>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="Validform_label">
					开始时间:
				</label>
			</td>
			
			<td class="value">
				<input id="beginDate" name="beginDate" type="text" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width: 150px" value="<fmt:formatDate value="${surveyMainPage.beginDate }" pattern="yyyy-MM-dd HH:mm:ss"/>">
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;"> 开始时间</label>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="Validform_label">
					截止时间:
				</label>
			</td>
			<td class="value">
				<input id="endDate" name="endDate" type="text" class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" style="width: 150px" value="<fmt:formatDate value="${surveyMainPage.endDate }" pattern="yyyy-MM-dd HH:mm:ss"/>">
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">截止时间</label>
			</td>
		</tr>
	 </table>
  </t:formvalid>
</body>
<script src = "webpage/weixin/guanjia/sconfig/sConfig.js"></script>		