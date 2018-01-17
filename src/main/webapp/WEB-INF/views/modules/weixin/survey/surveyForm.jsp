<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>订单信息</title>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
</head>
<script type="text/javascript">
	$(document).ready(function(){
	
		 var mainId="${weixinSurveypage.mainId}";
		 if(null!=mainId && ""!=mainId){
			$("[name='mainId']").val(mainId);
		 }
		
		 $("#surveyType").change(function(){
			    var type=$("#surveyType").val();
		  		if(type==1){
		  			$("#surveyoptiondiv").show();
		  		}
				if(type==2){
					$("#surveyoptiondiv").show();
				}
				if(type==3){
					$("#surveyoptiondiv").hide();
					$(".optiontemplate").removeAttr("nullmsg");
				}
				if(type==4){
					$("#surveyoptiondiv").show();
				}
		  });
		
	});
  //初始化下标
	function resetTrNum(tableId) {
		$tbody = $("#"+tableId+"");
		$tbody.find('>tr').each(function(i){
			$(':input, select', this).each(function(){
				var $this = $(this), name = $this.attr('name'), val = $this.val();
				if(name!=null){
					if (name.indexOf("#index#") >= 0){
						$this.attr("name",name.replace('#index#',i));
					}else{
						var s = name.indexOf("[");
						var e = name.indexOf("]");
						var new_name = name.substring(s+1,e);
						$this.attr("name",name.replace(new_name,i));
					}
				}
			});
		});
	}
 </script>
<body>
<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" tiptype="1" action="weixinSurveyController.do?save">
	<input id="id" name="id" type="hidden" value="${weixinSurveypage.id }">
	<table cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td align="right"><label class="Validform_label">调研题目: </label></td>
			<td class="value"><input class="inputxt" id="surveyTitle" name="surveyTitle" datatype="*" value="${weixinSurveypage.surveyTitle}"></td>
			<td align="right"><label class="Validform_label"> 调研问卷: </label></td>
			<td class="value">
				<select name="mainId">
					<c:forEach items="${surveyMainList }" var="main">
						<option value="${main.id }">${main.surveyTitle }</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td align="right"><label class="Validform_label"> 题目类型 </label></td>
			<td class="value">
			<t:dictSelect field="surveyType" id="surveyType"
				typeGroupCode="topic_type" hasLabel="false" defaultVal="${weixinSurveypage.surveyType==null ? 1 : weixinSurveypage.surveyType}"></t:dictSelect></td>
			<td align="right"><label class="Validform_label"> 排序: </label></td>
			<td class="value"><input nullmsg="排序不能为空" errormsg="只能输入数字" class="inputxt" id="surveySort" name="surveySort" value="${weixinSurveypage.surveySort}" datatype="n"></td>
		</tr>
	</table>
	
	<div id="surveyoptiondiv">
		<div style="width: auto; height: 200px;"><%-- 增加一个div，用于调节页面大小，否则默认太小 --%>
			<div style="width: 690px; height: 1px;"></div>
			<t:tabs id="tt" iframe="false" tabPosition="top" fit="false">
				<t:tab href="weixinSurveyController.do?surveyOptionList&id=${weixinSurveypage.id}" icon="icon-search" title="投票选项明细" id="Product"></t:tab>
			</t:tabs>
		</div>
	</div>
	
</t:formvalid>
<!-- 添加 产品明细 模版 -->
<table style="display: none">
	<tbody id="add_jeecgOrderProduct_table_template">
		<tr>
			<td align="center"><input style="width: 20px;" type="checkbox" name="ck" /></td>
			<td align="left"><input class="optiontemplate" nullmsg="请输入选项标题！"  name="surveyOptionList[#index#].optionName" type="text" style="width: 220px;"></td>
			<td align="left"><input class="optiontemplate" name="surveyOptionList[#index#].optionSort" type="text" style="width: 120px;"></td>
		</tr>
	</tbody>
	<tbody id="add_jeecgOrderCustom_table_template">
		<tr>
			<td align="center"><input style="width: 20px;" type="checkbox" name="ck" /></td>
			<td align="left">
				<input name="surveyOptionList[#index#].optionName" type="text" style="width: 220px;">
			</td>
			<td align="left"><input name="surveyOptionList[#index#].optionSort"  type="text" style="width: 120px;"></td>
		</tr>
	</tbody>
</table>

</body>