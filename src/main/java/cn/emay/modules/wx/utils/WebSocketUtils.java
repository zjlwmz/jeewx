package cn.emay.modules.wx.utils;

import java.io.IOException;

import javax.websocket.Session;


/**
 * webSocket 工具类
 * @author lenovo
 *
 */
public class WebSocketUtils {

	/**
	 * 消息发送
	 * @param message
	 * @param userid
	 * @throws IOException
	 */
	public static void sendMessage(String message,String userid) throws IOException{
		if(null!=SessionUtils.get(userid)){
			Session session=SessionUtils.get(userid);
			session.getBasicRemote().sendText(message);
		}
	}
}
