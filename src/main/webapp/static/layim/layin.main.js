layui.use(['layim','jquery','audio','sound','vedio'], function(layim){

  var autoReplay = [
    '您好，我现在有事不在，一会再和您联系。', 
    '你没发错吧？face[微笑] ',
    '洗澡中，请勿打扰，偷窥请购票，个体四十，团体八折，订票电话：一般人我不告诉他！face[哈哈] ',
    '你好，我是主人的美女秘书，有什么事就跟我说吧，等他回来我会转告他的。face[心] face[心] face[心] ',
    'face[威武] face[威武] face[威武] face[威武] ',
    '<（@￣︶￣@）>',
    '你要和我说话？你真的要和我说话？你确定自己想说吗？你一定非说不可吗？那你说吧，这是自动回复。',
    'face[黑线]  你慢慢说，别急……',
    '(*^__^*) face[嘻嘻] ，是贤心吗？'
  ];
  
  //基础配置
  layim.config({

    //初始化接口
    init: {
      url: 'wxOnlineRecordController/im/getList.do',
      data: {
    	  
      }
    },
    //简约模式（不显示主面板）
    //,brief: true
    //查看群员接口
    members: {
      url: 'wxOnlineRecordController/im/getMembers.do',
      data: {
    	  
      }
    },
    uploadImage: {
      url: 'wxOnlineRecordController/im/uploadImage.do', //（返回的数据格式见下文）
      type: 'POST' //默认post
    },
    uploadFile: {
      url: 'wxOnlineRecordController/im/uploadFile.do', //（返回的数据格式见下文）
      type: 'POST' //默认post
    },
    //,skin: ['aaa.jpg'] //新增皮肤
    //,isfriend: false //是否开启好友
    //,isgroup: false //是否开启群组
    chatLog: 'wxOnlineRecordController/im/chatlogview.do', //聊天记录地址
    find: 'wxOnlineRecordController/im/find.do',
    timeout:5000
    //,copyright: true //是否授权
  });

  /*
  layim.chat({
    name: '在线客服-小苍'
    ,type: 'kefu'
    ,avatar: 'http://tva3.sinaimg.cn/crop.0.0.180.180.180/7f5f6861jw1e8qgp5bmzyj2050050aa8.jpg'
    ,id: -1
  });
  layim.chat({
    name: '在线客服-心心'
    ,type: 'kefu'
    ,avatar: 'http://tva1.sinaimg.cn/crop.219.144.555.555.180/0068iARejw8esk724mra6j30rs0rstap.jpg'
    ,id: -2
  });
  */
  //layim.setChatMin();

  //监听发送消息
  layim.on('sendMessage', function(data){
    //var To = data.to;
    //console.log(data);
    //发送消息倒Socket服务
    //socket.send(JSON.stringify({type:'chatMessage',msgtype:"text",content: data.mine.content,toUser:To.id,fromUser:userid}));
    socket.send(JSON.stringify(data));
  });

  //监听在线状态的切换事件
  layim.on('online', function(data){
    console.log(data);
  });
   
  //layim建立就绪
  layim.on('ready', function(res){
  
    
  });

  //监听查看群员
  layim.on('members', function(data){
    console.log(data);
  });
  
  //监听聊天窗口的切换
  layim.on('chatChange', function(data){
    console.log(data);
  });
  
  
  var socketUrl=$("#ws").val();
  //建立WebSocket通讯
  var socket = new WebSocket('ws://'+socketUrl+'/websocket/'+userid);
  
  //发送一个消息
  try{
	  //socket.send('Hi Server, I am LayIM!');
  }catch(e){
	  
  }
  
 
   
 //连接成功时触发
  socket.onopen = function(){
    socket.send("{type:'onopen',content:'连接成功',userid:'"+userid+"'}");
  };
  
  //监听收到的消息
  socket.onmessage = function(res){
    //res为接受到的值，如 {"emit": "messageName", "data": {}}
    //emit即为发出的事件名，用于区分不同的消息
	//layui.sound.prompt();
    var data=res.data;
    var messageJson=eval('('+res.data+')');
    var addType=messageJson.addType;
    if(null!=addType){
    	//添加好友
    	layim.addList(messageJson);
    }else if(messageJson.type=='friend'){
    	//var thatChat = thisChat(), ul = thatChat.elem.find('.layim-chat-main ul');
    	//ul.append(laytpl(elemChatMain).render(data));
    	layim.getMessage(messageJson);
    }else if(messageJson.type=='group'){
    	layim.getMessage(messageJson);
    }else if(messageJson.type=='system'){
	    //	layim.getMessage({
		//   		 username:'系统消息'
		//   		,avatar:""
		//   		,id:messageJson.id
		//   		,type:messageJson.type
		//   		,system:true//系统消息
		//   		,content:"系统提示"
		//   	});
    }
   
    
  };
  
  
});