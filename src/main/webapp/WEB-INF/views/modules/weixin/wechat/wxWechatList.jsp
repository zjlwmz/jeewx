<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<t:datagrid name="wxWechatList" title="微信账号" actionUrl="wxWechatController.do?datagrid" fit="true" fitColumns="true" idField="id" queryMode="group" sortName="id" sortOrder="desc">
	<t:dgCol title="编号" field="id"  hidden="true"></t:dgCol>
	<t:dgCol title="公众号名称" field="name" width="100"></t:dgCol>
	<t:dgCol title="公众号类型" field="level" dictionary="wx_wechat_type"></t:dgCol>
	<t:dgCol title="token" field="token"></t:dgCol>
	<t:dgCol title="encodingaeskey" field="encodingaeskey"></t:dgCol>
	<t:dgCol title="原始ID" field="original"></t:dgCol>
	<t:dgCol title="微信号" field="username"></t:dgCol>
	<t:dgCol title="appid" field="appid"></t:dgCol>
	<t:dgCol title="secret" field="secret"></t:dgCol>
	<t:dgCol title="操作" field="opt" width="100"></t:dgCol>
	<t:dgDelOpt title="删除" url="wxWechatController.do?doDel&id={id}" />
	<t:dgToolBar title="创建公众账号" icon="icon-add" url="wxWechatController.do?form" funname="add" operationCode="add"></t:dgToolBar>
	<t:dgToolBar title="编辑" icon="icon-edit" url="wxWechatController.do?form" funname="update"></t:dgToolBar>
</t:datagrid>
</script>

