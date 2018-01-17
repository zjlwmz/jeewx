<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<t:datagrid name="chatMyGroupList" title="我的聊天分组" actionUrl="chatMyGroupController.do?datagrid" fit="true" fitColumns="true" idField="id" queryMode="group" sortName="id" sortOrder="desc">
	<t:dgCol title="编号" field="id"  hidden="true"></t:dgCol>
	<t:dgCol title="分组名称" field="name" width="100"></t:dgCol>
	<t:dgCol title="默认分组" field="isdefault" width="100" replace="是_0,否_1"></t:dgCol>
	<t:dgCol title="创建时间" field="createDate" formatter="yyyy-MM-dd hh:mm:ss"></t:dgCol>
	<t:dgCol title="操作" field="opt" width="100"></t:dgCol>
	<t:dgDelOpt title="删除" url="chatMyGroupController.do?doDel&id={id}" />
	<t:dgToolBar title="添加分组" icon="icon-add" url="chatMyGroupController.do?form" funname="add" operationCode="add"></t:dgToolBar>
	<t:dgToolBar title="编辑" icon="icon-edit" url="chatMyGroupController.do?form" funname="update"></t:dgToolBar>
</t:datagrid>
