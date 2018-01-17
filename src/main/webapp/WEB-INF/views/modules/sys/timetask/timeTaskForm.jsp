<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>定时任务管理</title>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="div" action="scheduleJobController.do?save">
	<input id="id" name="id" type="hidden" value="${timeTaskPage.id }">
	<fieldset class="step">
		<div class="form">
			<label class="Validform_label">任务名称:</label> 
			<input class="inputxt" id="name" name="name" value="${timeTaskPage.name}" datatype="*"> 
			<span class="Validform_checktip"></span>
		</div>
		<div class="form">
			<label class="Validform_label">任务分组 :</label> 
			<input class="inputxt" id="group" name="group" value="${timeTaskPage.group}" datatype="*"> 
			<span class="Validform_checktip"></span>
		</div>
		<div class="form">
			<label class="Validform_label">时间表达式:</label> 
			<input class="inputxt" id="cron" name="cron" value="${timeTaskPage.cron}" datatype="*"> 
			<span class="Validform_checktip"></span>
		</div>
		<div class="form">
			<label class="Validform_label">任务执行器:</label> 
			<input class="inputxt" id="jobClass" name="jobClass" value="${timeTaskPage.jobClass}" datatype="*"> 
			<span class="Validform_checktip"></span>
		</div>
		<div class="form">
			<label class="Validform_label">任务描述:</label> 
			<input class="inputxt" id="desc" name="desc" value="${timeTaskPage.desc}" datatype="*"> 
			<span class="Validform_checktip"></span>
		</div>
	</fieldset>
</t:formvalid>
</body>