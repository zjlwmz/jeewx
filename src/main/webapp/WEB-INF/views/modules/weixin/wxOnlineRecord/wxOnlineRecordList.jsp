<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<t:datagrid name="wxFansList" title="客服记录" width="1000px" actionUrl="wxOnlineRecordController.do?datagrid" fit="true" fitColumns="true" idField="id" queryMode="group" sortName="id" sortOrder="desc">
	<t:dgCol title="编号" field="id"  hidden="true"></t:dgCol>
	<t:dgCol title="openid" field="openid"></t:dgCol>
	<t:dgCol title="开始时间" field="startDate" formatter="yyyy-MM-dd hh:mm:ss" align="center"></t:dgCol>
	<t:dgCol title="结束时间" field="endDate" formatter="yyyy-MM-dd hh:mm:ss" align="center"></t:dgCol>
	<t:dgCol title="状态" field="status" query="true" replace="排队等候_0,开始对话_1,对话结束_2" align="center"></t:dgCol>
	<t:dgCol title="答复状态"  query="true"  replace="等待答复_0,等待提问_1,提醒顾客回复_2" field="replyStatus"></t:dgCol>
</t:datagrid>

