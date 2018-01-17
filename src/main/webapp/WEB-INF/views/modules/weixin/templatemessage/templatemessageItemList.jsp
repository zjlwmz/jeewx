<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	$('#addTemplatemessageItemBtn').linkbutton({
	    iconCls: 'icon-add'  
	});
	
	$('#delTemplatemessageItemBtn').linkbutton({
	    iconCls: 'icon-remove'  
	});
	
	$('#addTemplatemessageItemBtn').bind('click', function(){
 		 var tr =  $("#add_templatemessageItem_table_template tr").clone();
	 	 $("#add_templatemessageItem_table").append(tr);
	 	 resetTrNum('add_templatemessageItem_table');
	 	 return false;
    });
	
	$('#delTemplatemessageItemBtn').bind('click', function(){   
      	$("#add_templatemessageItem_table").find("input:checked").parent().parent().remove();   
        resetTrNum('add_templatemessageItem_table'); 
        return false;
    });
	
    $(document).ready(function(){
    	$(".datagrid-toolbar").parent().css("width","auto");
    	if(location.href.indexOf("load=detail")!=-1){
			$(":input").attr("disabled","true");
			$(".datagrid-toolbar").hide();
		}
		//将表格的表头固定
	    $("#templatemessageItem_table").createhftable({
	    	height:'300px',
			width:'auto',
			fixFooter:false
		});
    });
    
</script>
<div style="padding: 3px; height: 25px;width:auto;" class="datagrid-toolbar">
	<a id="addTemplatemessageItemBtn" href="#">添加</a> <a id="delTemplatemessageItemBtn" href="#">删除</a> 
</div>
<table border="0" cellpadding="2" cellspacing="0" id="templatemessageItem_table">
	<tr bgcolor="#E6E6E6">
		<td align="center" bgcolor="#EEEEEE">序号</td>
		<td align="center" bgcolor="#EEEEEE">操作</td>
				  <td align="left" bgcolor="#EEEEEE">
						模板项key
				  </td>
				  <td align="left" bgcolor="#EEEEEE">
						模板项标题
				  </td>
				  <td align="left" bgcolor="#EEEEEE">
						模板项颜色
				  </td>
	</tr>
	<tbody id="add_templatemessageItem_table">	
		<c:if test="${fn:length(templatemessageItemList)  <= 0 }">
				<tr>
					<td align="center"><div style="width: 25px;" name="xh">1</div></td>
					<td align="center"><input style="width:20px;"  type="checkbox" name="ck"/></td>
						<input name="templatemessageItemList[0].id" type="hidden"/>
						<input name="templatemessageItemList[0].createName" type="hidden"/>
						<input name="templatemessageItemList[0].createDate" type="hidden"/>
						<input name="templatemessageItemList[0].updateName" type="hidden"/>
						<input name="templatemessageItemList[0].updateDate" type="hidden"/>
						<input name="templatemessageItemList[0].templatemessageId" type="hidden"/>
					  <td align="left">
						  	<input name="templatemessageItemList[0].itemKey" datatype="*" maxlength="255" type="text" class="inputxt"  style="width:120px;" />
						    <label class="Validform_label" style="display: none;">模板项key</label>
						</td>
					  <td align="left">
						  	<input name="templatemessageItemList[0].itemTitle" maxlength="255" type="text" class="inputxt"  style="width:120px;" />
						    <label class="Validform_label" style="display: none;">模板项标题</label>
						</td>
					  <td align="left">
						  	<input name="templatemessageItemList[0].itemColor" maxlength="50" type="text" class="inputxt"  style="width:120px;" />
						    <label class="Validform_label" style="display: none;">模板项颜色</label>
						</td>
	   			</tr>
		</c:if>
		
		<c:if test="${fn:length(templatemessageItemList)  > 0 }">
			<c:forEach items="${templatemessageItemList}" var="poVal" varStatus="stuts">
				<tr>
					<td align="center"><div style="width: 25px;" name="xh">${stuts.index+1 }</div></td>
					<td align="center"><input style="width:20px;"  type="checkbox" name="ck" /></td>
					<input name="templatemessageItemList[${stuts.index }].id" type="hidden" value="${poVal.id }"/>
					<input name="templatemessageItemList[${stuts.index }].createName" type="hidden" value="${poVal.createName }"/>
					<input name="templatemessageItemList[${stuts.index }].createDate" type="hidden" value="${poVal.createDate }"/>
					<input name="templatemessageItemList[${stuts.index }].updateName" type="hidden" value="${poVal.updateName }"/>
					<input name="templatemessageItemList[${stuts.index }].updateDate" type="hidden" value="${poVal.updateDate }"/>
					<input name="templatemessageItemList[${stuts.index }].templatemessageId" type="hidden" value="${poVal.templatemessageId }"/>
				   	<td align="left">
					  	<input name="templatemessageItemList[${stuts.index }].itemKey" datatype="*" maxlength="255" type="text" class="inputxt"  style="width:120px;" value="${poVal.itemKey }" />
					    <label class="Validform_label" style="display: none;">模板项key</label>
				   	</td>
				   	<td align="left">
					  	<input name="templatemessageItemList[${stuts.index }].itemTitle" datatype="*" maxlength="255" type="text" class="inputxt"  style="width:120px;" value="${poVal.itemTitle }" />
					    <label class="Validform_label" style="display: none;">模板项标题</label>
				   	</td>
				   	<td align="left">
					  <input name="templatemessageItemList[${stuts.index }].itemColor"  maxlength="50"  type="text" class="inputxt"  style="width:120px;" value="${poVal.itemColor }">
					  <label class="Validform_label" style="display: none;">模板项颜色</label>
				   	</td>
	   			</tr>
			</c:forEach>
		</c:if>	
	</tbody>
</table>
