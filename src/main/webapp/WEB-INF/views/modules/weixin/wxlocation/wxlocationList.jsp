<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools"></t:base>
<t:datagrid name="rtList" actionUrl="wxLocationController.do?datagrid" fit="true" fitColumns="true" idField="id" queryMode="group">
	<t:dgCol title="编号" field="id" hidden="true" ></t:dgCol>
	<t:dgCol title="昵称" field="nickName" query="true" width="80"></t:dgCol>
	<t:dgCol title="纬度" field="latitude"  width="50"></t:dgCol>
	<t:dgCol title="经度" field="longitude" width="50"></t:dgCol>
	<t:dgCol title="精度" field="precision"  width="50"></t:dgCol>
	<t:dgCol title="时间" field="createDate" width="100"  formatter="yyyy-MM-dd hh:mm:ss" ></t:dgCol>
	
	<t:dgCol title="操作" field="opt" width="100"></t:dgCol>
	<t:dgDelOpt title="删除" url="receiveMessageController.do?del&id={id}" />
</t:datagrid>

<script type="text/javascript">
</script>
