<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
	<div region="center" style="padding:0px;border:0px">
		<t:datagrid name="timeTaskList" title="任务调度" actionUrl="scheduleJobController.do?datagrid"  idField="id" fit="true" sortName="createDate" sortOrder="desc">
			<t:dgCol title="id" field="id" hidden="true"></t:dgCol>
			<t:dgCol title="任务名称" field="name"></t:dgCol>
			<t:dgCol title="任务分组" field="group"></t:dgCol>
			<t:dgCol title="任务状态" field="status" dictionary="quartz_status" align="center"></t:dgCol>
			<t:dgCol title="运行状态" field="state" dictionary="quartz_state" align="center"></t:dgCol>
			<t:dgCol title="任务运行时间表达式" field="cron"></t:dgCol>
			<t:dgCol title="任务描述" field="desc"></t:dgCol>
			<t:dgCol title="任务执行模板" field="jobClass" width="100"></t:dgCol>
			<t:dgCol title="创建时间" field="createDate" formatter="yyyy-MM-dd hh:mm:ss" hidden="true"></t:dgCol>
			<t:dgCol title="更新时间" field="updateDate" formatter="yyyy-MM-dd hh:mm:ss"></t:dgCol>
			<t:dgCol title="操作" field="opt" width="150"></t:dgCol>
			
			<t:dgConfOpt title="开启任务" exp="status#eq#0" url="scheduleJobController.do?startTask&id={id}&isStart=1" message="确认开启任务" />
			<t:dgConfOpt title="关闭任务" exp="status#eq#1" url="scheduleJobController.do?stopTask&id={id}&isStart=0" message="确认关闭任务" />
			<t:dgConfOpt title="立即运行" exp="status#eq#1" url="scheduleJobController.do?runingTask&id={id}" message="立即运行" />
			
			<t:dgDelOpt title="common.delete" url="scheduleJobController.do?delete&id={id}" />
			<t:dgToolBar title="新增" icon="icon-add" url="scheduleJobController.do?form" funname="add"></t:dgToolBar>
			<t:dgToolBar title="编辑" icon="icon-edit" url="scheduleJobController.do?form" funname="update"></t:dgToolBar>
		</t:datagrid>
	</div>
</div>
