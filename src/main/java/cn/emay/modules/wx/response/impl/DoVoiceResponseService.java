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
import cn.emay.framework.common.utils.DoubleUtil;
import cn.emay.framework.common.utils.FileUtils;
import cn.emay.framework.common.utils.Mp3Utils;
import cn.emay.modules.sys.service.ParamsService;
import cn.emay.modules.wx.entity.ReceiveMessage;
import cn.emay.modules.wx.entity.WxChatLogs;
import cn.emay.modules.wx.entity.WxFans;
import cn.emay.modules.wx.entity.WxOnlineRecord;
import cn.emay.modules.wx.entity.WxWechat;
import cn.emay.modules.wx.im.api.ImMessageAPI;
import cn.emay.modules.wx.im.bean.message.IVoiceMessage;
import cn.emay.modules.wx.im.support.MessageCategory;
import cn.emay.modules.wx.response.ResponseService;
import cn.emay.modules.wx.service.ReceiveMessageService;
import cn.emay.modules.wx.service.WxCacheService;
import cn.emay.modules.wx.service.WxChatLogsService;
import cn.emay.modules.wx.service.WxFansService;
import cn.emay.modules.wx.service.WxOnlineRecordService;
import cn.emay.modules.wx.utils.ChangeAudioFormat;
import cn.emay.modules.wx.utils.WxOnlineRecordCacheUtils;


/**
 * 针对音频消息处理接口实现
 * @author lenovo
 *
 */
@Component
public class DoVoiceResponseService extends BaseResponseService implements ResponseService{

	private static final Logger logger = LoggerFactory.getLogger(DoVoiceResponseService.class);
	
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
	 * 聊天记录service接口
	 */
	@Autowired
	private WxChatLogsService wxChatLogsService ; 
	
	
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
			String domain=paramsService.findParamsByName("sys.domain");
			
			String fromUserName=eventMessage.getFromUserName();
			// 公众帐号
			String toUserName = eventMessage.getToUserName();
			// 消息类型
			String msgType = eventMessage.getMsgType();
			String msgId = eventMessage.getMsgId();
			
			String Format=eventMessage.getFormat();
			String MediaId=eventMessage.getMediaId();
			String MsgId=eventMessage.getMsgId();
			
			WxWechat wxWechat=wxCacheService.getWxWechat();
			
			String accessToken = wxCacheService.getToken();
			Date createdon=new Date();
			String wxPath=paramsService.findParamsByName("wx.path");
			
			String relativePathFile="upload/voice/"+DateUtils.formatDate(createdon, "yyyMMdd");
			String realPath=wxPath+relativePathFile;
			
			FileUtils.createDirectory(realPath);
			
			
			/**
			 * 音频文件读取
			 */
			byte[]pimageByte=MediaAPI.mediaGet(accessToken, MediaId).getBytes();
			File wxVoice=new File(realPath+"/"+MsgId+"."+Format);
			FileUtils.writeByteArrayToFile(wxVoice, pimageByte);
			
			String mediaTime="";
			Double width=100d;
			/**
			 * amr--->mp3
			 */
			try{
				String targetPath=realPath+"/"+MsgId+".mp3";
				/**
				 * 创建mp3文件
				 */
				FileUtils.createFile(targetPath);
				ChangeAudioFormat.changeToMp3(wxVoice.getAbsolutePath(), targetPath);
				
				int time=Mp3Utils.getMp3TrackLength(targetPath);
				mediaTime=DateUtils.mp3Time(time*1000);
				
				width=DoubleUtil.mul(DoubleUtil.add(DoubleUtil.div(time*1d, 60d), 1), 110);
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			/**
			 * 音频地址
			 */
//			String relativePath=relativePathFile+"/"+MsgId+"."+Format;
			String relativePath=relativePathFile+"/"+MsgId+".mp3";
			
			
			
			/**
			 * 是否在在线对话中
			 */
			if(null!=WxOnlineRecordCacheUtils.getWxOnlineRecord(fromUserName)){
				WxChatLogs wxChatLogs=new WxChatLogs();
				wxChatLogs.setMessageType(eventMessage.getMsgType());
				wxChatLogs.setFromUserName(fromUserName);
				String voiceUrl=domain+relativePath;
				wxChatLogs.setVoiceUrl(voiceUrl);
				wxChatLogs.setCreateDate(new Date());
				wxChatLogs.setMediaTime(mediaTime);
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
					
					/**
					 * 推送给客服人员
					 * 
					 */
					
					/**
					 * 推送给客服人员
					 * 
					 */
					WxFans wxFans=wxFansService.findWxFansByOpenid(fromUserName);
					IVoiceMessage iVoiceMessage=new IVoiceMessage(); 
					iVoiceMessage.setUsername(wxFans.getNickname());
					iVoiceMessage.setAvatar(wxFans.getHeadimgurl());
					iVoiceMessage.setId(wxFans.getOpenid());
					iVoiceMessage.setType(MessageCategory.friend);
					iVoiceMessage.setContent(wxChatLogs.getVoiceUrl());
					/**
					 * 音频时间
					 */
					iVoiceMessage.setMediaTime(mediaTime);
					
					iVoiceMessage.setWidth(width.intValue()+"px");
					iVoiceMessage.setMine(false);
					iVoiceMessage.setTimestamp(System.currentTimeMillis());
					ImMessageAPI.messageViceSend(iVoiceMessage, wxOnlineRecord.getUserId());
					
				}
			}else{
				WxFans wxFans=wxFansService.findWxFansByOpenid(fromUserName);
				if(null!=wxFans){
					try{
						// 保存接收到的信息
						ReceiveMessage receiveMessage = new ReceiveMessage();
						receiveMessage.setVoiceUrl(domain+relativePath);
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
