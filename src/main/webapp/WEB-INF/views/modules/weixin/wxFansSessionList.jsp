<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<t:datagrid name="wxFansSessionList" title="微信会话" width="1000px" actionUrl="wxFansSessionController.do?datagrid" fit="true" fitColumns="true" idField="id" queryMode="group" sortName="id" sortOrder="desc">
	<t:dgCol title="编号" field="id"  hidden="true"></t:dgCol>
	<t:dgCol title="openid" field="openid"></t:dgCol>
	<t:dgCol title="会话开始时间" field="startSessionTime" formatter="yyyy-MM-dd hh:mm:ss" align="center"></t:dgCol>
	<t:dgCol title="更新时间" field="endSessionTime"  formatter="yyyy-MM-dd hh:mm:ss" align="center"></t:dgCol>
	<t:dgCol title="会话剩余时间" field="residualTime" align="center"></t:dgCol>
</t:datagrid>

