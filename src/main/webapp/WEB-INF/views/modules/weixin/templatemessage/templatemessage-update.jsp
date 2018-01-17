<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>模板消息</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript" src="static/ckfinder/ckfinder.js"></script>
  <script type="text/javascript" src="${webRoot }/static/templatemessage/templatemessage.js"></script>
  <script type="text/javascript">
  $(document).ready(function(){
	$('#tt').tabs({
	   onSelect:function(title){
	       $('#tt .panel-body').css('width','auto');
		}
	});
	$(".tabs-wrap").css('width','100%');
  });
  
	//初始化下标
	/*
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
	*/
	
	
 </script>
 </head>
 <body style="overflow-x: hidden;">
  	<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" tiptype="1" action="templatemessageController.do?doUpdate">
		<input id="id" name="id" type="hidden" value="${templatemessagePage.id }">
		<table cellpadding="0" cellspacing="1" class="formtable">
			<tr>
				<td align="right">
					<label class="Validform_label">模板id:</label>
				</td>
				<td class="value">
			     	<input id="templateId" name="templateId" type="text" style="width: 150px" class="inputxt" value='${templatemessagePage.templateId}' />
					<span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">模板id</label>
				</td>
				<td align="right">
					<label class="Validform_label">模板名称:</label>
				</td>
				<td class="value">
			     	<input id="templateName" name="templateName" type="text" style="width: 150px" class="inputxt" value='${templatemessagePage.templateName}' />
					<span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">模板名称</label>
				</td>
			</tr>
			<tr>
				<td align="right">
					<label class="Validform_label">详情Url:</label>
				</td>
				<td class="value">
			     	 <input id="detailUrl" name="detailUrl" type="text" style="width: 150px" class="inputxt" value='${templatemessagePage.detailUrl}' />
					<span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">详情Url</label>
				</td>
				<td align="right">
					<label class="Validform_label">标题颜色:</label>
				</td>
				<td class="value">
			     	 <input id="topcolor" name="topcolor" type="text" style="width: 150px" class="inputxt"  value='${templatemessagePage.topcolor}' />
					<span class="Validform_checktip"></span>
					<label class="Validform_label" style="display: none;">标题颜色</label>
				</td>
			</tr>
		</table>
			
		<div style="width: auto;height: 200px;">
			<%-- 增加一个div，用于调节页面大小，否则默认太小 --%>
			<div style="width:800px;height:1px;"></div>
			<t:tabs id="tt" iframe="false" tabPosition="top" fit="false">
			 	<t:tab href="templatemessageController.do?templatemessageItemList&id=${templatemessagePage.id}" icon="icon-search" title="模板消息明细" id="templatemessageItem"></t:tab>
			</t:tabs>
	    </div>
	</t:formvalid>
		
	<!-- 添加 附表明细 模版 -->
	<table style="display:none">
		<tbody id="add_templatemessageItem_table_template">
			<tr>
			 <td align="center"><div style="width: 25px;" name="xh"></div></td>
			 <td align="center"><input style="width:20px;" type="checkbox" name="ck"/></td>
				  <td align="left">
					  	<input name="templatemessageItemList[#index#].itemKey" maxlength="255" type="text" class="inputxt"  style="width:120px;" />
					  <label class="Validform_label" style="display: none;">模板项key</label>
				  </td>
				  <td align="left">
					  	<input name="templatemessageItemList[#index#].itemTitle" maxlength="255" type="text" class="inputxt"  style="width:120px;" />
					  <label class="Validform_label" style="display: none;">模板项标题</label>
				  </td>
				  <td align="left">
					  <input name="templatemessageItemList[#index#].itemColor" maxlength="50" type="text" class="inputxt"  style="width:120px;" />
					  <label class="Validform_label" style="display: none;">模板项颜色</label>
				  </td>
			</tr>
		 </tbody>
	</table>
 </body>