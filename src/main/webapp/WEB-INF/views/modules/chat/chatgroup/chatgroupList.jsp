<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<t:datagrid name="chatGroupList" title="群组管理" actionUrl="chatGroupController.do?datagrid" fit="true" fitColumns="true" idField="id" queryMode="group" sortName="id" sortOrder="desc">
	<t:dgCol title="编号" field="id"  hidden="true"></t:dgCol>
	<t:dgCol title="群组名称" field="name" width="100"></t:dgCol>
	<t:dgCol title="群组头像" field="avatar"></t:dgCol>
	<t:dgCol title="创建时间" field="createDate"></t:dgCol>
	<t:dgDelOpt title="删除" url="chatGroupController.do?doDel&id={id}" />
	<t:dgToolBar title="增加" icon="icon-add" url="chatGroupController.do?form" funname="add" operationCode="add"></t:dgToolBar>
	<t:dgToolBar title="编辑" icon="icon-edit" url="chatGroupController.do?form" funname="update"></t:dgToolBar>
</t:datagrid>

