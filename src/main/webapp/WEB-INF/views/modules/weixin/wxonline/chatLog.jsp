<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
  <head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width" />
	<link rel="stylesheet" href="${ webRoot}/static/layim/layui/css/layui.css" type="text/css"></link>
	<script type="text/javascript" src="${ webRoot}/static/layim/source/layui/layui.js"></script>
	<style type="text/css">
        .layim-chat-main {
            height: auto !important;
        }
        .loading{
        	text-align: center;
        }
    </style>
  </head>
  
  <body>
  		<div class="layim-chat-main"><ul></ul></div>
    	<div id="page" style="text-align:center"></div>
	    <script type="text/javascript">
		    	layui.use(['layim', 'laypage'], function (layim) {
		            var laypage = layui.laypage, $ = layui.jquery;
		            function getPage(curr) {
		            	var chatMainUl=$(".layim-chat-main ul");
		            	chatMainUl.empty().append($("<img src=\"${ webRoot}/static/layim/layui/css/pc/layer/default/loading-2.gif\"/>"));
		            	chatMainUl.addClass("loading");
		            	$.ajax({
		        			url : "${ webRoot}/wxOnlineRecordController/im/chatlog.do?id=${userid}&type=${type}&page="+curr,
		        			dataType : 'json',
		        			headers : {
		        				'Content-Type' : "application/json; charset=UTF-8"
		        			},
		        			type : "POST",
		        			success : function(response, status, request) {
		        				chatMainUl.empty();
		        				chatMainUl.removeClass("loading");
		        				
		        				var results=response.list;
		        				var pageCount=response.pageCount;
		        				$(results).each(function(index,obj){
		        					var mine=obj.mine;
		        					var htmlList=[];
		        					if(mine){
		        						htmlList.push("<li class='layim-chat-mine'>");
		        					}else{
		        						htmlList.push("<li class>");
		        					}
		        					
		        					htmlList.push( "<div class=\"layim-chat-user\">");
		        					htmlList.push("<img src='"+obj.avatar+"'>");
		        					if(mine){
		        						htmlList.push("<cite><i>"+obj.timestamp+"</i>"+obj.userName+"</cite>");
		        					}else{
		        						htmlList.push("<cite>"+obj.userName+"<i>"+obj.timestamp+"</i></cite>");
		        					}
		        					htmlList.push("</div>");
		        					
		        					
	        						if(obj.messageType=="text"){
	        							htmlList.push("<div class=\"layim-chat-text\">"+obj.content+"</div>");
	        						}else if(obj.messageType=="image"){
	        							htmlList.push("<div class=\"layim-chat-text layim-chat-image\"><img class=\"layui-layim-photos\" src="+obj.content+"/></div>");
	        						}else if(obj.messageType=="voice"){
	        							htmlList.push("<div class=\"layim-chat-text\"><audio controls=\"controls\"><source src="+obj.content+" /> </audio></div>");
	        						}else if(obj.messageType=="shortvideo" || obj.messageType=="video"){
	        							htmlList.push("<div class=\"layim-chat-text\">"+
					        								"<video controls=\"controls\" autoplay=\"autoplay\">"+
					        									"<source src="+obj.content+" type=\"video/mp4\" />"+
					        								"</video>"+
					        						   "</div>");
	        						}
		        						
		        					htmlList.push("</li>");
		        					chatMainUl.append($(htmlList.join("")));
		        				});
		        				
		        				
		        				laypage({
		        					cont: 'page', //容器。值支持id名、原生dom对象，
		        					pages: pageCount, //总页数
		        					first: false,
		        					last: false,
		        					curr: curr || 1, //当前页
		        					jump: function (e, first) { //触发分页后的回调
		        						if (!first) { //点击跳页触发函数自身
		        							getPage(e.curr);
		        						}
		        					}
		        				});
		        				
		        			},
		        			error : function() {
		        				
		        			}
		        		});
		            }
		            getPage(1);
		
		        });
	    </script>
  </body>
</html>
