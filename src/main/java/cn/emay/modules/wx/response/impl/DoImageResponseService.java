package cn.emay.modules.wx.response.impl;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import weixin.popular.api.MediaAPI;
import weixin.popular.bean.message.EventMessage;
import cn.emay.framework.common.utils.DateUtils;
import cn.emay.framework.common.utils.FileUtils;
import cn.emay.modules.sys.service.ParamsService;
import cn.emay.modules.wx.entity.ReceiveMessage;
import cn.emay.modules.wx.entity.WxChatLogs;
import cn.emay.modules.wx.entity.WxFans;
import cn.emay.modules.wx.entity.WxOnlineRecord;
import cn.emay.modules.wx.entity.WxWechat;
import cn.emay.modules.wx.im.api.ImMessageAPI;
import cn.emay.modules.wx.im.bean.message.IImageMessage;
import cn.emay.modules.wx.im.support.MessageCategory;
import cn.emay.modules.wx.im.support.MessageType;
import cn.emay.modules.wx.response.ResponseService;
import cn.emay.modules.wx.service.ReceiveMessageService;
import cn.emay.modules.wx.service.WxCacheService;
import cn.emay.modules.wx.service.WxChatLogsService;
import cn.emay.modules.wx.service.WxFansService;
import cn.emay.modules.wx.service.WxOnlineRecordService;
import cn.emay.modules.wx.utils.WxOnlineRecordCacheUtils;

/**
 * 针对图片消息接口处理
 * @author lenovo
 *
 */
@Component
public class DoImageResponseService extends BaseResponseService implements ResponseService{

	private static final Logger logger = LoggerFactory.getLogger(DoImageResponseService.class);
	/**
	 * 微信缓存数据接口
	 */
	@Autowired
	private WxCacheService wxCacheService;

	
	/**
	 * 接收消息接口
	 */
	@Autowired
	private ReceiveMessageService receiveMessageService;
	
	
	/**
	 * 微信粉丝Dao接口
	 */
	@Autowired
	private WxFansService wxFansService;
	
	
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
	
	
	
	/**
	 * 配置文件
	 */
//	@Autowired
//	@Qualifier("main")
//	private Properties properties;
	
	
	/**
	 * 参数service接口
	 */
	@Autowired
	private ParamsService paramsService;
	
	
	
	@Override
	public void execute(EventMessage eventMessage) {
		try{
			
			String fromUserName=eventMessage.getFromUserName();
			// 公众帐号
			String toUserName = eventMessage.getToUserName();
			// 消息类型
			String msgType = eventMessage.getMsgType();
			String msgId = eventMessage.getMsgId();
			
			String MediaId=eventMessage.getMediaId();
			String MsgId=eventMessage.getMsgId();
			WxWechat wxWechat=wxCacheService.getWxWechat();
			
			String accessToken = wxCacheService.getToken();
			Date createdon=new Date();
//			String wxPath=properties.getProperty("wx.path");
			String wxPath=paramsService.findParamsByName("wx.path");
			
			
			String relativePathFile="upload/wx/image/"+DateUtils.formatDate(createdon, "yyyMMdd");
			
			String realPath=wxPath+relativePathFile;
			FileUtils.createDirectory(realPath);
			
			byte[]pimageByte=MediaAPI.mediaGet(accessToken, MediaId).getBytes();
			File wxmgage=new File(realPath+"/"+MsgId+".png");
			FileUtils.writeByteArrayToFile(wxmgage, pimageByte);
			
			String relativePath=relativePathFile+"/"+MsgId+".png";
			
			
			/**
			 * 是否在在线对话中
			 */
			if(null!=WxOnlineRecordCacheUtils.getWxOnlineRecord(fromUserName)){
				String domain=paramsService.findParamsByName("sys.domain");
				WxChatLogs wxChatLogs=new WxChatLogs();
				wxChatLogs.setContent(eventMessage.getContent());
				wxChatLogs.setMessageType(eventMessage.getMsgType());
				wxChatLogs.setFromUserName(fromUserName);
				wxChatLogs.setPicUrl(domain+relativePath);
				wxChatLogs.setCreateDate(new Date());
				wxChatLogsService.save(wxChatLogs);
				
				/**
				 * 是否在在线对话中
				 */
				WxOnlineRecord wxOnlineRecord=WxOnlineRecordCacheUtils.getWxOnlineRecord(fromUserName);
				if(null!=wxOnlineRecord){
					wxChatLogs.setWxOnlineServiceId(wxOnlineRecord.getId());
					wxChatLogs.setToUserName(wxOnlineRecord.getUserId());//对应客服对象
					wxOnlineRecord.setReplyStatus(0);
					wxOnlineRecord.setEndDate(new Date());
					wxOnlineRecordService.save(wxOnlineRecord);
					
					
					WxFans wxFans=wxFansService.findWxFansByOpenid(fromUserName);
					/**
					 * 推送给客服人员
					 * 
					 */
					IImageMessage iImageMessage=new IImageMessage(); 
					iImageMessage.setUsername(wxFans.getNickname());
					iImageMessage.setAvatar(wxFans.getHeadimgurl());
					iImageMessage.setId(wxFans.getOpenid());
					iImageMessage.setType(MessageCategory.friend);
					iImageMessage.setContent("img["+wxChatLogs.getPicUrl()+"]");
					iImageMessage.setMine(false);
					iImageMessage.setTimestamp(System.currentTimeMillis());
					iImageMessage.setMsgType(MessageType.image);
					ImMessageAPI.messageImageSend(iImageMessage, wxOnlineRecord.getUserId());
					
					
				}
				
			}else{
				WxFans wxFans=wxFansService.findWxFansByOpenid(fromUserName);
				if(null!=wxFans){
					try{
						// 保存接收到的信息
						ReceiveMessage receiveMessage = new ReceiveMessage();
						receiveMessage.setContent("");
						receiveMessage.setImageUrl(relativePath);
						Timestamp temp = Timestamp.valueOf(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
						receiveMessage.setCreateTime(temp);
						receiveMessage.setFromUserName(fromUserName);
						receiveMessage.setToUserName(toUserName);
						receiveMessage.setMsgId(msgId);
						receiveMessage.setMsgType(msgType);
						receiveMessage.setResponse("0");
						receiveMessage.setNickName(wxFans.getNickname());
						receiveMessage.setWechatId(wxWechat.getId());
						receiveMessageService.save(receiveMessage);
					}catch (Exception e) {
						logger.error("接收信息保存异常", e);
					}
				}
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
