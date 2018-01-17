<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
	<t:base type="jquery,easyui,tools,DatePicker"></t:base>
	<div class="easyui-layout" fit="true">
		  <div region="center" style="padding:1px;">
			  <t:datagrid name="templatemessageList" checkbox="true" fitColumns="false" title="模板消息" actionUrl="templatemessageController.do?datagrid" idField="id" fit="true" queryMode="group">
				   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
				   <t:dgCol title="模板名称"  field="templateName"  hidden="false" query="true" queryMode="single"  width="120"></t:dgCol>
				   <t:dgCol title="模板编号"  field="templateId"  hidden="false" query="true" queryMode="single"  width="120"></t:dgCol>
				   <t:dgCol title="详情Url"  field="detailUrl"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
				   <t:dgCol title="标题颜色"  field="topcolor"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
				   
				   <t:dgCol title="创建日期"  field="createDate" formatter="yyyy-MM-dd hh:mm:ss" hidden="false"  queryMode="single"  width="150"></t:dgCol>
				   <t:dgCol title="修改日期"  field="updateDate" formatter="yyyy-MM-dd hh:mm:ss" hidden="false"  queryMode="single"  width="150"></t:dgCol>
				   
				   
				   <t:dgCol title="创建人名称"  field="createName"  hidden="true"  queryMode="single"  width="100"></t:dgCol>
				   <t:dgCol title="修改人名称"  field="updateName"  hidden="true"  queryMode="single"  width="100"></t:dgCol>
				   <t:dgCol title="微信帐户id"  field="wechatId"  hidden="true"  queryMode="single"  width="150"></t:dgCol>
				   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
				   <t:dgDelOpt title="删除" url="templatemessageController.do?doDel&id={id}" />
				   <t:dgToolBar title="录入" icon="icon-add" url="templatemessageController.do?goAdd" funname="add"></t:dgToolBar>
				   <t:dgToolBar title="编辑" icon="icon-edit" url="templatemessageController.do?goUpdate" funname="update"></t:dgToolBar>
				   <t:dgToolBar title="批量删除"  icon="icon-remove" url="templatemessageController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>
				   <t:dgToolBar title="查看" icon="icon-search" url="templatemessageController.do?goUpdate" funname="detail"></t:dgToolBar>
			  </t:datagrid>
		  </div>
	 </div>
 <script type="text/javascript">
 $(document).ready(function(){
 		//给时间控件加上样式
 		$("#templatemessageListtb").find("input[name='createDate']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 		$("#templatemessageListtb").find("input[name='updateDate']").attr("class","Wdate").attr("style","height:20px;width:90px;").click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
 });
 
//导入
function ImportXls() {
	openuploadwin('Excel导入', 'templatemessageController.do?upload', "templatemessageList");
}

//导出
function ExportXls() {
	JeecgExcelExport("templatemessageController.do?exportXls","templatemessageList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("templatemessageController.do?exportXlsByT","templatemessageList");
}
 </script>