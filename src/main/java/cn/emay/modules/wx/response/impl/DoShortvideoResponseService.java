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
import cn.emay.framework.common.media.MediaPreview;
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
 * 小视屏消息处理接口
 * @author lenovo
 *
 */
@Component
public class DoShortvideoResponseService extends BaseResponseService implements ResponseService{

	private static final Logger logger = LoggerFactory.getLogger(DoShortvideoResponseService.class);
	
	
	/**
	 * 微信缓存数据接口
	 */
	@Autowired
	private WxCacheService wxCacheService;
	
	
	/**
	 * 聊天记录service接口
	 */
	@Autowired
	private WxChatLogsService wxChatLogsService ; 
	
	
	/**
	 * 微信粉丝Dao接口
	 */
	@Autowired
	private WxFansService wxFansService;
	
	
	/**
	 * 接收消息接口
	 */
	@Autowired
	private ReceiveMessageService receiveMessageService;
	
	
	
	/**
	 * 在线客服记录单接口服务
	 */
	@Autowired
	private WxOnlineRecordService wxOnlineRecordService;

	
	/**
	 * 参数service接口
	 */
	@Autowired
	private ParamsService paramsService;
	
	
	@Override
	public void execute(EventMessage eventMessage) {
		try{
			// 公众帐号
			String toUserName = eventMessage.getToUserName();
			// 消息类型
			String msgType = eventMessage.getMsgType();
			String msgId = eventMessage.getMsgId();
						
			WxWechat wxWechat=wxCacheService.getWxWechat();
			
			String MediaId=eventMessage.getMediaId();
			String MsgId=eventMessage.getMsgId();
			String accessToken = wxCacheService.getToken();
			
			Date createdon=new Date();
			
			//微信存储路径
			String wxPath=paramsService.findParamsByName("wx.path");
			
			
			String shortvideoPath="upload/shortvideo/"+DateUtils.formatDate(createdon, "yyyMMdd")+"/";
			String realPath=wxPath+shortvideoPath;
			FileUtils.createDirectory(realPath);
			
			/**
			 * 视频保存到本地
			 */
			byte[]pimageByte=MediaAPI.mediaGet(accessToken, MediaId).getBytes();
			String shortvideoFileName=MsgId+".mp4";
			String shortVideoRealPath=realPath+shortvideoFileName;
			File wxmgage=new File(shortVideoRealPath);
			FileUtils.writeByteArrayToFile(wxmgage, pimageByte);
		
			/**
			 * 视频截取缩略图
			 */
			String sysFfmpegPath=paramsService.findParamsByName("sys.ffmpegPath");
			MediaPreview.processImg(shortVideoRealPath,sysFfmpegPath);
		
			String domain=paramsService.findParamsByName("sys.domain");
			
			//路徑相對地址
			String relativePath=shortvideoPath+shortvideoFileName;
			
			
			/**
			 * 是否在在线对话中
			 */
			String fromUserName = eventMessage.getFromUserName();
			WxOnlineRecord wxOnlineRecord=WxOnlineRecordCacheUtils.getWxOnlineRecord(fromUserName);
			if(null!=wxOnlineRecord){
				/**
				 * 保存对话
				 */
				WxChatLogs wxChatLogs=new WxChatLogs();
				
				/**
				 * 短视频地址
				 */
				String shortVideoUrl=domain+shortvideoPath+shortvideoFileName;
				wxChatLogs.setShortVideoUrl(shortVideoUrl);
				wxChatLogs.setMessageType(eventMessage.getMsgType());
				wxChatLogs.setFromUserName(fromUserName);
				wxChatLogs.setCreateDate(new Date());
				wxChatLogs.setType("friend");//朋友
				wxChatLogs.setWxOnlineServiceId(wxOnlineRecord.getId());
				wxChatLogs.setToUserName(wxOnlineRecord.getUserId());//对应客服对象
				wxChatLogsService.save(wxChatLogs);
				
				
				/**
				 * 更新在线客服记录单
				 */
				wxOnlineRecord.setReplyStatus(0);//0等待回复
				wxOnlineRecord.setEndDate(new Date());
				wxOnlineRecordService.save(wxOnlineRecord);
				WxOnlineRecordCacheUtils.putWxOnlineRecord(wxOnlineRecord);
			
				
				
				
				/**
				 * 推送给客服人员
				 * 
				 */
				WxFans wxFans=wxFansService.findWxFansByOpenid(fromUserName);
				IImageMessage iImageMessage=new IImageMessage(); 
				iImageMessage.setUsername(wxFans.getNickname());
				iImageMessage.setAvatar(wxFans.getHeadimgurl());
				iImageMessage.setId(wxFans.getOpenid());
				iImageMessage.setType(MessageCategory.friend);
				iImageMessage.setContent(wxChatLogs.getPicUrl());
				iImageMessage.setMine(false);
				iImageMessage.setTimestamp(System.currentTimeMillis());
				iImageMessage.setMsgType(MessageType.shortvideo);
				ImMessageAPI.messageShortVideoSend(iImageMessage, wxOnlineRecord.getUserId());
				
//				
//				/**
//				 * 推送给客服人员
//				 * 
//				 */
//				Map<String,Object>textMap=new HashMap<String, Object>();
//				Map<String,Object>textData=new HashMap<String, Object>();
//				WxFans wxFans=wxFansService.findWxFansByOpenid(fromUserName);
//				textData.put("username", wxFans.getNickname());
//				textData.put("avatar", wxFans.getHeadimgurl());
//				textData.put("id", wxFans.getOpenid());
//				textData.put("type", "friend");
//				textData.put("content", shortVideoUrl);
//				textData.put("mine", false);
//				textData.put("timestamp", System.currentTimeMillis());
//				textData.put("msg_type", wxChatLogs.getMessageType());//短视频
//				
//				textMap.put("emit", "msg");
//				textMap.put("data", textData);
//				WebSocketUtils.sendMessage(JsonMapper.getInstance().toJson(textMap), wxOnlineRecord.getUserId());
			}else{
				WxFans wxFans=wxFansService.findWxFansByOpenid(fromUserName);
				if(null!=wxFans){
					try{
						// 保存接收到的信息
						ReceiveMessage receiveMessage = new ReceiveMessage();
						receiveMessage.setShortvideoUrl(domain+relativePath);
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
			
		}
	}

}
