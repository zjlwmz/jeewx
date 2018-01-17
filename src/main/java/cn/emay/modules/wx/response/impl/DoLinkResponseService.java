package cn.emay.modules.wx.response.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import weixin.popular.api.MessageAPI;
import weixin.popular.bean.message.EventMessage;
import weixin.popular.bean.message.message.TextMessage;
import cn.emay.modules.wx.entity.WxChatLogs;
import cn.emay.modules.wx.entity.WxOnlineRecord;
import cn.emay.modules.wx.response.ResponseService;
import cn.emay.modules.wx.service.WxCacheService;
import cn.emay.modules.wx.service.WxChatLogsService;
import cn.emay.modules.wx.service.WxOnlineRecordService;
import cn.emay.modules.wx.utils.WxOnlineRecordCacheUtils;

/**
 * 针对链接消息
 */
@Component
public class DoLinkResponseService extends BaseResponseService implements ResponseService{

	/**
	 * 微信缓存数据接口
	 */
	@Autowired
	private WxCacheService wxCacheService;
	
	/**
	 * 在线客服记录单接口服务
	 */
	@Autowired
	private WxOnlineRecordService wxOnlineRecordService;
	
	/**
	 * 聊天记录service接口
	 */
	@Autowired
	private WxChatLogsService wxChatLogsService; 
	
	@Override
	public void execute(EventMessage eventMessage) {
	
		String fromUserName = eventMessage.getFromUserName();
		
		/**
		 * 是否在在线对话中
		 */
		if(null!=WxOnlineRecordCacheUtils.getWxOnlineRecord(fromUserName)){
			WxChatLogs wxChatLogs=new WxChatLogs();
			wxChatLogs.setContent(eventMessage.getContent());
			wxChatLogs.setMessageType(eventMessage.getMsgType());
			wxChatLogs.setFromUserName(fromUserName);
			WxOnlineRecord wxOnlineRecord =wxOnlineRecordService.getCurrentWxOnlineRecord(fromUserName);
			if(null!=wxOnlineRecord){
				wxChatLogs.setToUserName(wxOnlineRecord.getUserId());//对应客服对象
			}
			wxChatLogsService.save(wxChatLogs);
		}else{
			TextMessage sendTextMessage=new TextMessage(fromUserName, getMainMenu());
			String accessToken = wxCacheService.getToken();
			MessageAPI.messageCustomSend(accessToken, sendTextMessage);
		}
			
		
	}

}
