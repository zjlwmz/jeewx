package cn.emay.modules.wx.socket;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import weixin.popular.bean.BaseResult;
import cn.emay.framework.common.utils.SpringContextHolder;
import cn.emay.modules.wx.im.api.ImMessageAPI;
import cn.emay.modules.wx.im.request.ResMessage;
import cn.emay.modules.wx.im.request.ResTo;
import cn.emay.modules.wx.utils.SessionUtils;
import cn.emay.modules.wx.utils.WxOnlineRecordCacheUtils;
import cn.emay.modules.wx.utils.WxOnlineDispatchSerive;

import com.alibaba.fastjson.JSON;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint("/websocket/{userid}")
public class WebSocket {
	//静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
	private static int onlineCount = 0;

	//concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
	private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<WebSocket>();

	//与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;
	
	/**
	 * 在线客服 调度工具接口
	 */
	private static WxOnlineDispatchSerive wxOnlineDispatchSerive=SpringContextHolder.getBean(WxOnlineDispatchSerive.class);
	
	/**
	 * 连接建立成功调用的方法
	 * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	@OnOpen
	public void onOpen(@PathParam("userid") String userid,Session session,EndpointConfig config){
		this.session = session;
		webSocketSet.add(this);     //加入set中
		addOnlineCount();           //在线数加1
		System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
		SessionUtils.put(userid, session); 
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose(@PathParam("userid") String userid,Session session,CloseReason reason){
		webSocketSet.remove(this);  //从set中删除
		subOnlineCount();           //在线数减1
		System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
		SessionUtils.remove(userid);
		WxOnlineRecordCacheUtils.removeWxOnlineRecord(userid);
	}

	/**
	 * 收到客户端消息后调用的方法
	 * @param message 客户端发送过来的消息
	 * @param session 可选的参数
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		try{
			System.out.println("来自客户端的消息:" + message);
			ResMessage resMessage=JSON.parseObject(message, ResMessage.class);
			if(null!=resMessage){
				ResTo resTo =resMessage.getTo();
				//个人消息
				if(resTo.getType().equals("friend")){
					
					/**
					 * 发送微信客服消息
					 */
					BaseResult baseResult =wxOnlineDispatchSerive.sendMessage(resMessage);
					if(null!=baseResult){
						if(baseResult.getErrcode().equals("0")){
							System.out.println("------------【消息发送成功】--------------------");
						}else{
							System.out.println("------------【消息发送失败】--------------------");
							ImMessageAPI.messageSystemSend("消息发送失败："+baseResult.getErrcode(), resMessage);
						}
					}else{
						ImMessageAPI.messageSystemSend("客户不在线", resMessage);
						System.out.println("------------【客户不在线】--------------------");
					}
				}
				//群消息
				else if(resTo.getType().equals("group")){
					wxOnlineDispatchSerive.sendGroupMessage(resMessage);
				}
			}
			
		}catch (Exception e) {
//			e.printStackTrace();
		}
		
		
	}

	/**
	 * 发生错误时调用
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(@PathParam("userid") String userid,Session session, Throwable error){
		System.out.println("有一连接关闭！发生错误");
		SessionUtils.remove(userid);
	}

	/**
	 * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
	 * @param message
	 * @throws IOException
	 */
	public void sendMessage(String message) throws IOException{
		this.session.getBasicRemote().sendText(message);
	}

	
	
	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	public static synchronized void addOnlineCount() {
		WebSocket.onlineCount++;
	}

	public static synchronized void subOnlineCount() {
		WebSocket.onlineCount--;
	}
}
