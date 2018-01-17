<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<t:datagrid name="wxFansList" title="微信粉丝" width="1000px" actionUrl="wxFansController.do?datagrid" fit="true" fitColumns="true" idField="id" queryMode="group" sortName="id" sortOrder="desc">
	<t:dgCol title="编号" field="id"  hidden="true"></t:dgCol>
	<t:dgCol title="openid" field="openid"></t:dgCol>
	<t:dgCol title="是否关注" field="isFollow" dictionary="wx_isFollow" align="center"></t:dgCol>
	<t:dgCol title="最后关注时间" field="followtime" formatter="yyyy-MM-dd hh:mm:ss" align="center"></t:dgCol>
	<t:dgCol title="最后取消关注时间" field="unfollowtime" formatter="yyyy-MM-dd hh:mm:ss" align="center"></t:dgCol>
	<t:dgCol title="昵称" field="nickname"></t:dgCol>
	<t:dgCol title="更新时间" field="updatedOn"  formatter="yyyy-MM-dd hh:mm:ss" align="center"></t:dgCol>
	<t:dgCol title="头像" field="headimgurl"  image="true"  imageSize="40,40"></t:dgCol>
	<t:dgToolBar title="查看" icon="icon-edit" url="wxFansController.do?form" funname="update"></t:dgToolBar>
	<t:dgToolBar title="同步" icon="icon-edit" url="wxFansController.do?synchronization" funname="synchronization(this)"></t:dgToolBar>
</t:datagrid>
<script type="text/javascript" src="${webRoot }/static/msg/mask.js"></script>
<script type="text/javascript">

function synchronization(_this){
	MaskUtil.mask('请稍后...');
	$.ajax({
			url:"wxFansController.do?synchronization",
			type:"GET",
			dataType:"JSON",
			success:function(data){
				MaskUtil.unmask();
				$("#wxFansList").datagrid("reload");
				if(data.success){
					tip(data.msg);
				}
			}
		});
		
		
}

</script>

