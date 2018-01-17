<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>查找-90专属LayIm</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit">
<link rel="stylesheet" href="${ webRoot}/static/layim/layui/css/layui.css" type="text/css"></link>
<script type="text/javascript" src="${ webRoot}/static/layim/source/layui/layui.js"></script>
<style type="text/css">
#friend{ width:;}
.layim-members-list { position: relative; width:; padding: 10px 10px 0; border: 0px solid #D9D9D9; background-color: #fff; background-color: rgba(255,255,255,.9); box-shadow: none; overflow: hidden; font-size: 0 }
.friend-li { display: block; vertical-align: top; font-size: 14px; width: 100%; height: 48px; margin: 10px 0; cursor:pointer; float:left; width:25%; }
.friend-li img { width: 48px; height: 48px; border-radius: 4px; -moz-border-radius:4px; -webkit-border-radius:4px; float: left; margin-right: 20px; }
.friend-li img:hover{ border:1px solid rgb(63, 221, 134);}
.friend-li p { text-align: left; margin: 0; }
.friend-li .layui-icon { font-size: 14px; }
.friend-li p.remark { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.layui-form-area{ width:410px;}
#Refused{ width:408px; height:80px;}
.group{ height:35px; width:410px;}
#up-remark{ width:190px; border-radius:2px; border:1px solid #CCC; height:22px; padding-left:5px;}
.layim-status-check{ color:#0F3; padding-left:15px; font-size:14px; cursor:pointer}
#friendpage{ margin:10px auto;text-align: center;}
.layim-status-haoyouqingqiu{ color:#fff;}
.layim-status-male{ color:#aacce2;}
.layim-status-female{ color:#e5bcc3;}
.friend-user-add{ background-color:#68ade4; color:#fff; padding: 2px 4px;border-radius: 4px; -moz-border-radius:4px; -webkit-border-radius:4px; font-size:12px;}
.layim-status-add{}
</style>
</head>

<body>
<div class="layui-tab" style="margin-top:-1px">
    <ul class="layui-tab-title" style="left: 0px;">
        <li class="layui-tab-this">找人</li>
        <li class="">找群</li>
    </ul>
     <div class="layui-tab-content">
     	<!--人-->
        <div class="layui-tab-item layui-show">
        	<form class="layui-form">
                        <li class="layui-form-li">
                            <input type="text" style="width:500px" placeholder="输入好友号吗/昵称/关键词/邮箱/手机" />
                            <div class="layui-form-label layui-form-checkbox">
                                <label>在线</label>
                                <input type="checkbox" name="love[yinyue]">
                            </div>
                        </li>
                        <li class="layui-form-li">
                            <div class="layui-form-label">
                                <label>省份</label>
                                <select required name="province">
                                    <option value="浙江省">浙江省</option>
                                    <option value="江西省">江西省</option>
                                    <option value="上海市">上海市</option>
                                    <option value="福建省">福建省</option>
                                    <option value="湖北省">湖北省</option>
                                    <option value="江苏省">江苏省</option>
                                    <option value="安徽省">安徽省</option>
                                    <option value="北京市">北京市</option>
                                    <option value="香港">香港</option>
                                    <option value="澳门">澳门</option>
                                    <option value="台湾省">台湾省</option>
                                </select>
                            </div>
                            <div class="layui-form-label">
                                <label>姓别</label>
                                <select required name="province">
                                    <option value="0">不限</option>
                                    <option value="1">男</option>
                                    <option value="2">女</option>
                                </select>
                            </div>
                            <div class="layui-form-label">
                                <label>年龄</label>
                                <select required name="province">
                                    <option value="0">不限</option>
                                    <option value="17">18岁以下</option>
                                    <option value="18-22">18-22岁</option>
                                    <option value="23-28">23-28岁</option>
                                    <option value="29-35">29-35岁</option>
                                    <option value="36">35岁以上</option>
                                </select>
                            </div>
                            <button class="layui-form-button">查找</button>
                        </li>

                    </form>
            <div id="friend">
                <ul class="layim-members-list" id="members-view">
                    
                </ul>
                <div id="friendpage"></div>
            </div>
        </div>
        <!--群-->
        <div class="layui-tab-item">2</div>

    </div>    
</div>
<script id="userlist" type="text/html">
	{{# for(var i = 0, len = d.length; i < len; i++){ }}	
	<li class="friend-li">
		<img src="{{d[i].avatar}}">		
		<p>{{d[i].username}}
		{{# if(d[i].status=='online'){ }}
			<span class="layui-icon layim-status-online">&#xe617;</span>
		{{# }else{ }}
			<span class="layui-icon layim-status-hide">&#xe60f;</span>
		{{# } }}
		</p>
		<p>
		{{# if(d[i].sex=='男'){ }}
			<span class="layui-icon layim-status-male">&#xe612;</span>
		{{# }else{ }}
			<span class="layui-icon layim-status-female">&#xe612;</span>
		{{# } }}
		<span>中国·重庆</span>
		<button data-id="{{d[i].id}}" data-avatar="{{d[i].avatar}}" data-status="{{d[i].status}}" data-username="{{d[i].username}}" data-sign="{{d[i].remark}}" data-type="notice" class="friend-user-add"><span class="layui-icon layim-status-add">&#xe608;</span>添加</button>
		</p>
		<!--p class="remark">{{d[i].remark}}</p-->
	</li>
	{{# } }}
	
</script>
<script>
layui.use(['layim','laypage', 'layout', 'form'], function(){
  var layim=layui.layim,
  $ = layui.jquery,
  layer = layui.layer,
  laytpl = layui.laytpl,
  laypage = layui.laypage,
  layout = layui.layout, 
  form = layui.form,
  islogin=0,
  userdata={};
  /*
  $.ajax({ 
	  type: 'POST', 
	  url: "user/ajax_login.php",
	  dataType: "json", 
	  //data: data, 
	  success: function(res){
    	islogin = res.msg;
		
  	  }, 
	  async:false 
  });
  
 
  if(!islogin || islogin=='0'){
	layer.open({
		type: 2,
		title:false,
		area: ['300px', '290px'],
		fix: false, //不固定
		closeBtn: 0,
		maxmin: false,
		content: 'app/index.html?burl='+window.location.href
	});
	return false;  
  }
  */
	  
	layout.tab();
	form.checkbox();
	form.select();
	
   //展示非好友的列表
  function getuserjons(curr){
	  var row='10';
	  $.ajax({ 
		  type: 'POST', 
		  url: "${ webRoot}/wxOnlineRecordController/im/findUserList.do",
		  data:{start: (curr-1)*row || 0,total:row}, 
		  dataType: "json",
		  success: function(res){
			userdata = res;
		  }, 
		  async:false 
	  });
  
	  
	  
	  //显示分页
	laypage({
		cont: 'friendpage', //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
		pages: userdata.pageCount,
		curr: curr || 1, //当前页
		jump: function(obj, first){ //触发分页后的回调
			if(!first){ //点击跳页触发函数自身，并传递当前页：obj.curr
				getuserjons(obj.curr);
			}
		}

	});

	var gettpl = document.getElementById('userlist').innerHTML;
	laytpl(gettpl).render(userdata.list, function(html){
		$("#members-view").html(html);
		
	});
	  
  }	
  getuserjons();
  
  //添加好友列表
	$(document).on("click",".friend-user-add",function(){
	  var addlistdata = $(this).data(),sendres={};
	  sendres['mine']=parent.layui.layim.cache().mine;
	  $.post("user/get_group_list.php?dopost=list",function(res){			  
		  var groupidoption = '';
		  for(var key in res){
			 groupidoption += '<option value="'+res[key].gid+'">'+res[key].gname+'</option>';
		  }	  
		  var index = layer.open({
				title:['<span class="layui-icon layim-status-haoyouqingqiu">&#xe612;</span>&nbsp;'+sendres['mine']['username']+'-添加好友','height:30px; line-height:30px; background: rgb(33, 150, 243);'],
				closeBtn:0,
				area: ['450px', ''],
				content:'<div class="layui-form-li"><div class="layui-form-label layui-form-area"><label>申请理由</label><textarea required="" name="content" id="Refused"></textarea></div><div class="layui-form-label layui-form-area"><label>添加到分组</label><select required="" name="groupid" class="input group">'+groupidoption+'</select></div></div>', 
				btn:['添加好友','算了吧'],
				yes: function(index){
					sendres['mine']['content']=$("#Refused").val();
					addlistdata['name']=addlistdata['username'];
					sendres['to']=addlistdata;               
					$.post("user/pm.php?dopost=send",{data:sendres},function(){});
					addlistdata['groupid']=$(".group").find("option:selected").val();
					addlistdata['content']=sendres['mine']['content'];
					$.post("user/grouporfriend.php?dopost=agree",{data:addlistdata},function(){});                      
					layer.close(index);
					addlistdata['type']='friend';
					parent.layim.addList(addlistdata);                 
				}
		  });
	  },'json');
		 // addlistdata['groupid']=1;
		// layim.addList(addlistdata);  
	}); 
 
});
</script>
</body>
</html>
