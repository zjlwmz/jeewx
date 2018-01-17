package cn.emay.modules.wx.im.api;

import java.io.IOException;

import javax.websocket.Session;

import com.alibaba.fastjson.JSON;

import cn.emay.modules.wx.im.bean.message.IMessage;
import cn.emay.modules.wx.im.support.MessageCategory;
import cn.emay.modules.wx.im.support.MessageType;
import cn.emay.modules.wx.utils.SessionUtils;


/**
 * 
 * @Title 群消息发送工具类
 * @author zjlwm
 * @date 2017-2-13 下午2:19:36
 *
 */
public class ImGroupMessageAPI {

	
	/**
	 * 发送客服文本消息
	 * @param messageJson
	 * @param userid
	 * @throws IOException 
	 */
	public static void messageCustomSend(String messageJson,String userid) throws IOException{
		Session session=SessionUtils.get(userid);
		if(null!=session){
			session.getBasicRemote().sendText(messageJson);
		}
	}
	
	/**
	 * 发送客服消息
	 * @param message
	 * @param userid
	 * @throws IOException
	 */
	public static void messageCustomSend(IMessage message,String userid) throws IOException{
		Session session=SessionUtils.get(userid);
		if(null!=session){
			message.setType(MessageCategory.group);
			String messageJson=JSON.toJSONString(message);
			session.getBasicRemote().sendText(messageJson);
		}
	}
	
	
	
	/**
	 * 图片消息
	 * @param message
	 * @param userid
	 * @throws IOException
	 */
	public static void messageImageSend(IMessage message,String userid) throws IOException{
		Session session=SessionUtils.get(userid);
		if(null!=session){
			message.setType(MessageCategory.group);
			message.setMsgType(MessageType.image);
			String messageJson=JSON.toJSONString(message);
			session.getBasicRemote().sendText(messageJson);
		}
	}
	
	
}
