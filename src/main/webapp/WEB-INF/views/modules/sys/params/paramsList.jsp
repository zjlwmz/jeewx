<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<t:datagrid name="wxFansList" title="参数管理" actionUrl="paramsController.do?datagrid" fit="true" fitColumns="true" idField="id" queryMode="group" sortName="id" sortOrder="desc">
	<t:dgCol title="编号" field="id"  hidden="true"></t:dgCol>
	<t:dgCol title="参数名称" field="paramName" query="true" width="200"></t:dgCol>
	<t:dgCol title="参数值" field="paramsValue" query="true" width="200"></t:dgCol>
	<t:dgCol title="参数类型" field="paramsType" dictionary="params_type" query="true" width="200"></t:dgCol>
	<t:dgCol title="备注" field="remarks" width="150"></t:dgCol>
	<t:dgToolBar title="common.add" langArg="common.language" icon="icon-add" url="paramsController.do?form" funname="add"></t:dgToolBar>
   <t:dgToolBar title="common.edit" langArg="common.language" icon="icon-edit" url="paramsController.do?form" funname="update"></t:dgToolBar>
</t:datagrid>
