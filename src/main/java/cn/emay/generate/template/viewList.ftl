<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>

<t:datagrid name="wxFansList" title="${functionName}" actionUrl="${urlPrefix}Controller.do?datagrid" fit="true" fitColumns="true" idField="id" queryMode="group" sortName="id" sortOrder="desc">
	<t:dgCol title="编号" field="id"  hidden="true"></t:dgCol>
	<t:dgCol title="名称" field="name" query="true" width="200"></t:dgCol>
	<t:dgToolBar title="common.add" langArg="common.language" icon="icon-add" url="${urlPrefix}Controller.do?form" funname="add"></t:dgToolBar>
   <t:dgToolBar title="common.edit" langArg="common.language" icon="icon-edit" url="${urlPrefix}Controller.do?form" funname="update"></t:dgToolBar>
</t:datagrid>
