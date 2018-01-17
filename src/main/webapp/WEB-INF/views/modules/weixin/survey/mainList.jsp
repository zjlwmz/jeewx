<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:1px;">
	  <t:datagrid name="weixinSurveyList" checkbox="true" fitColumns="false" title="系统配置" actionUrl="weixinSurveyMainController.do?maindatagrid" idField="id" fit="true" queryMode="group">
		   <t:dgCol title="主键"  field="id"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
		   <t:dgCol title="创建人名称"  field="createName"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
		   <t:dgCol title="创建日期"  field="createDate"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
		   <t:dgCol title="修改人名称"  field="updateName"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
		   <t:dgCol title="修改日期"  field="updateDate"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
		   <t:dgCol title="调查项目"  field="surveyTitle"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
		   <t:dgCol title="参与调研人数"  field="surveyCount"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
		   <t:dgCol title="调查描述"  field="surveyDescription"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
		   <t:dgCol title="状态"  field="statement"  hidden="false"  queryMode="single"  width="120"></t:dgCol>
		   <t:dgCol title="积分"  field="integral"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
		   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
		   <t:dgDelOpt title="删除" url="weixinSurveyMainController.do?doDel&id={id}" />
		   <t:dgToolBar title="录入" icon="icon-add" url="weixinSurveyMainController.do?addorupdate" funname="add"></t:dgToolBar>
		   <t:dgToolBar title="编辑" icon="icon-edit" url="weixinSurveyMainController.do?addorupdate" funname="update"></t:dgToolBar>
		   <t:dgToolBar title="预览" icon="icon-search" url="weixinSurveyMainController.do?goReview2" funname="review"></t:dgToolBar>
		   <t:dgToolBar title="获取调查问卷url" icon="icon-ok" url="weixinSurveyMainController.do?Push" funname="getUrl"></t:dgToolBar>
		   <t:dgToolBar title="收集数据导出" icon="icon-putout" funname="myExportXls"></t:dgToolBar>
		   <t:dgToolBar title="插入积分流水" icon="icon-undo"  funname="insertKmks"></t:dgToolBar>
		   <t:dgToolBar title="调查会员id导出" icon="icon-putout" funname="myExportXlsMember"></t:dgToolBar>
	  </t:datagrid>
  </div>
  
  
  
 </div>
 <script src = "webpage/weixin/guanjia/sconfig/sConfigList.js"></script>	
 <script type="text/javascript" src="${webRoot }/plug-in/msg/mask.js"></script>	
 <script type="text/javascript">
 $(document).ready(function(){
 		//给时间控件加上样式
 });
 
 
 function insertKmks(){
 	var rowsData = $('#weixinSurveyList').datagrid('getSelections');
	if(rowsData.length==0){
 	  tip("请选择一个问卷。");
 	  return;
 	}
 	if (rowsData.length>1) {
		tip("请选择一个问卷。");
		return;
	}
	var row = $('#weixinSurveyList').datagrid('getSelected');
	if(row){
		MaskUtil.mask('正在加载数据,请稍后...');
		$.ajax({
	    		url : "weixinSurveyMainController.do?addIntegralLog",
	    		type:"GET",
	    		dataType:"JSON",
				data : {
					id : row.id 
				},
	    		success:function(data){
	    			MaskUtil.unmask();
	    			if(data.success){
	    				tip(data.msg);
	    			}else{
	    				tip(data.msg);
	    			}
	    		}
	    });
	}
	
	
 }
 
//导出
function myExportXls() {
	var rowsData = $('#weixinSurveyList').datagrid('getSelections');
	if(rowsData.length==0){
 	  tip("请选择一个问卷。");
 	  return;
 	}
 	if (rowsData.length>1) {
		tip("请选择一个问卷。");
		return;
	}
	var row = $('#weixinSurveyList').datagrid('getSelected');
	window.location.href = "weixinSurveyMainController.do?exportXls2&id="+row.id;
}



//导出调查问卷会员结果
function myExportXlsMember() {
	var rowsData = $('#weixinSurveyList').datagrid('getSelections');
	if(rowsData.length==0){
 	  tip("请选择一个问卷。");
 	  return;
 	}
 	if (rowsData.length>1) {
		tip("请选择一个问卷。");
		return;
	}
	var row = $('#weixinSurveyList').datagrid('getSelected');
	window.location.href = "weixinSurveyMainController.do?myExportXlsMember&mainId="+row.id;
}


 function getUrl(){
 	var rowsData = $('#weixinSurveyList').datagrid('getSelections');
 	var row = $('#weixinSurveyList').datagrid('getSelected');
 	if(rowsData.length==0){
 	  tip("请选择一个问卷。");
 	  return;
 	}
 	if (rowsData.length>1) {
		tip("请选择一个问卷。");
		return;
	}
		var baseUrl="${domain}";
 		var content=baseUrl+"/weixin/html.do?goPage2&page=questionnaire&mainId="+row.id;
 		$.dialog({
	           content: content,
	           lock : true,
	           title:"获取调查问卷Url",
	           zIndex:2100,
	           width:700,
	           height: 200,
	           parent:windowapi,
	           cache:false,
		       cancelVal: '关闭',
		       cancel: function(){
		    	   
		       } 
	   });
 	
 }
 
 function calculate(){
	 var row = $('#weixinSurveyList').datagrid('getSelected');
	 var url = "weixinSurveyMainController.do?goSurveyMainCalculate";
	  if(row){
		  url = url+"&id="+row.id;
		  window.open(url);
		  //add("投票统计",url,"weixinVoteList","100%","100%");
	  }else{
		  tip("请选择一个问卷进行统计。");
	  }
 }
 
 function review(){
	 var row = $('#weixinSurveyList').datagrid('getSelected');
	 var url = "weixinSurveyMainController.do?goReview2";
	  if(row){
		  url = url+"&id="+row.id;
		  window.open(url);
		  //add("投票统计",url,"weixinVoteList","100%","100%");
	  }else{
		  tip("请选择一个问卷进行预览。");
	  }
 }
 
  //发布
 function begin(){
	  var url = "weixinSurveyMainController.do?deploy&statement=1";
		var row = $('#weixinSurveyList').datagrid('getSelected');
		    if (row) {
		    	$.ajax({
		    		url:url,
		    		type:"GET",
		    		dataType:"JSON",
					data : {
						id : row.id 
					},
		    		success:function(data){
		    			if(data.success){
		    				tip(data.msg);
		    				$('#weixinSurveyList').datagrid('reload'); 
		    			}
		    		}
		    	});
		    }else{
		    	tip("请至少选择一条数据进行操作。");
		    }
 }
 
 function over(){
		var row = $('#weixinSurveyList').datagrid('getSelected');
		    var url = "weixinSurveyMainController.do?deploy&statement=2";
		    if (row) {
		    	$.ajax({
		    		url:url,
		    		type:"GET",
		    		dataType:"JSON",
					data : {
						id: row.id
					},
		    		success:function(data){
		    			if(data.success){
		    				tip(data.msg);
		    				$('#weixinSurveyList').datagrid('reload'); 
		    			}
		    		}
		    	});
		    }else{
		    	tip("请至少选择一条数据进行操作。");
		    }
 }
 
 
 //结束
 
 
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