<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
  <t:datagrid name="weixinSurveyList" checkbox="true" fitColumns="false" title="系统配置" actionUrl="weixinSurveyController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="主键"  field="id"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人名称"  field="createName"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建日期"  field="createDate"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="修改人名称"  field="updateName"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="修改日期"  field="updateDate"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
   
   <t:dgCol title="调查问卷"  field="mainId" query="true"  width="120" replace="${surveyMainReplace}"></t:dgCol>
   <t:dgCol title="调用项目"  field="surveyTitle"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="调研类型"  field="surveyType"  dictionary="topic_type" hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="参与调研人数"  field="surveyCount"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="调研描述"  field="surveyDescription"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="weixinSurveyController.do?doDel&id={id}" />
   <t:dgToolBar title="录入" icon="icon-add" url="weixinSurveyController.do?goadd" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="weixinSurveyController.do?goadd" funname="update"></t:dgToolBar>
   <t:dgToolBar title="查看" icon="icon-search" url="weixinSurveyController.do?goadd" funname="detail"></t:dgToolBar>
  </t:datagrid>
  </div>
 </div>
 <script src = "webpage/weixin/guanjia/sconfig/sConfigList.js"></script>		
 <script type="text/javascript">
 $(document).ready(function(){
 		//给时间控件加上样式
 });
 
//导入
function ImportXls() {
	openuploadwin('Excel导入', 'sConfigController.do?upload', "sConfigList");
}

//导出
function ExportXls() {
	JeecgExcelExport("sConfigController.do?exportXls","sConfigList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("sConfigController.do?exportXlsByT","sConfigList");
}
 </script>