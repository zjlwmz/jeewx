package cn.emay.modules.wx.im.api;

import java.io.IOException;

import javax.websocket.Session;

import cn.emay.modules.wx.im.bean.message.IMessage;
import cn.emay.modules.wx.im.bean.message.ITextMessage;
import cn.emay.modules.wx.im.request.ResMessage;
import cn.emay.modules.wx.im.request.ResMine;
import cn.emay.modules.wx.im.request.ResTo;
import cn.emay.modules.wx.im.support.MessageCategory;
import cn.emay.modules.wx.im.support.MessageType;
import cn.emay.modules.wx.utils.SessionUtils;

import com.alibaba.fastjson.JSON;



/**
 * 一对一消息工具发送
 * web socket 消息推送api
 * @author lenovo
 *
 */
public class ImMessageAPI {

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
			message.setType(MessageCategory.friend);
			String messageJson=JSON.toJSONString(message);
			session.getBasicRemote().sendText(messageJson);
		}
	}
	
	
	/**
	 * 好友添加通知
	 * @param message
	 * @param userid
	 * @throws IOException
	 */
	public static void messageAddFriendSend(IMessage message,String userid) throws IOException{
		Session session=SessionUtils.get(userid);
		if(null!=session){
			message.setType(MessageCategory.friend);
			message.setAddType(MessageCategory.friend);
			message.setGroupid(message.getGroupid());
			String messageJson=JSON.toJSONString(message);
			session.getBasicRemote().sendText(messageJson);
		}
	}
	
	
	/**
	 * 发送系统消息
	 */
	public static void messageSystemSend(String content,ResMessage resMessage) throws IOException{
		ResMine resMine=resMessage.getMine();
		ResTo resTo=resMessage.getTo();
		Session session=SessionUtils.get(resMine.getId());
		if(null!=session){
			ITextMessage textMessage=new ITextMessage();
			textMessage.setContent(content);
			textMessage.setSystem(true);
			textMessage.setMsgType(MessageType.text);
			textMessage.setType(MessageCategory.friend);
			textMessage.setId(resTo.getId());
			String messageJson=JSON.toJSONString(textMessage);
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
			message.setType(MessageCategory.friend);
			message.setMsgType(MessageType.image);
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
	public static void messageViceSend(IMessage message,String userid) throws IOException{
		Session session=SessionUtils.get(userid);
		if(null!=session){
			message.setType(MessageCategory.friend);
			message.setMsgType(MessageType.voice);
			String messageJson=JSON.toJSONString(message);
			session.getBasicRemote().sendText(messageJson);
		}
	}
	
	
	/**
	 * 视频消息
	 * @param message
	 * @param userid
	 * @throws IOException
	 */
	public static void messageVedioSend(IMessage message,String userid) throws IOException{
		Session session=SessionUtils.get(userid);
		if(null!=session){
			message.setType(MessageCategory.friend);
			message.setMsgType(MessageType.video);
			String messageJson=JSON.toJSONString(message);
			session.getBasicRemote().sendText(messageJson);
		}
	}
	
	/**
	 * 小视频消息
	 * @param message
	 * @param userid
	 * @throws IOException
	 */
	public static void messageShortVideoSend(IMessage message,String userid) throws IOException{
		Session session=SessionUtils.get(userid);
		if(null!=session){
			message.setType(MessageCategory.friend);
			message.setMsgType(MessageType.shortvideo);
			String messageJson=JSON.toJSONString(message);
			session.getBasicRemote().sendText(messageJson);
		}
	}
	
	
	
	
}
