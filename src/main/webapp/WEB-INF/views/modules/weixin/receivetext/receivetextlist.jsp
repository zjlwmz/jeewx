<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools"></t:base>
<t:datagrid name="rtList" actionUrl="receiveMessageController.do?datagrid" fit="true" fitColumns="true" idField="id" queryMode="group">
	<t:dgCol title="编号" field="id" hidden="true" ></t:dgCol>
	<t:dgCol title="昵称" field="nickName" query="true" width="80"></t:dgCol>
	<t:dgCol title="类型" field="msgType"  replace="文本_text,图片_image,音頻_voice,音乐_music,视频_video" query="true" width="50"></t:dgCol>
	<t:dgCol title="时间" field="createTime" formatter=""  width="100"></t:dgCol>
	<t:dgCol title="内容" field="content" ></t:dgCol>
	<t:dgCol title="回复内容" field="rescontent" width="100"></t:dgCol>
	<t:dgCol title="操作" field="opt" width="100"></t:dgCol>
	<t:dgDelOpt title="删除" url="receiveMessageController.do?del&id={id}" />
	<t:dgFunOpt title="快捷回复" funname="responseMessage(id)"/>
</t:datagrid>

<script type="text/javascript">
	function responseMessage(id){
		var url = "receiveMessageController.do?jumpsendmessage&id="+id;
		add("快捷回复",url,"rtList",700,400);
	}
</script>
