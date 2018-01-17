package cn.emay.modules.wx.service.impl;

import java.nio.charset.Charset;
import java.sql.Timestamp;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import weixin.popular.bean.message.EventMessage;
import weixin.popular.util.SignatureUtil;
import weixin.popular.util.StreamUtils;
import weixin.popular.util.XMLConverUtil;
import cn.emay.framework.common.utils.CacheUtils;
import cn.emay.modules.wx.entity.WxFansSession;
import cn.emay.modules.wx.entity.WxWechat;
import cn.emay.modules.wx.response.impl.DoDingYueEventResponseService;
import cn.emay.modules.wx.response.impl.DoImageResponseService;
import cn.emay.modules.wx.response.impl.DoLinkResponseService;
import cn.emay.modules.wx.response.impl.DoLocationResponseService;
import cn.emay.modules.wx.response.impl.DoMyMenuEventService;
import cn.emay.modules.wx.response.impl.DoMyViewEventService;
import cn.emay.modules.wx.response.impl.DoShortvideoResponseService;
import cn.emay.modules.wx.response.impl.DoTextResponseService;
import cn.emay.modules.wx.response.impl.DoUnsubscribeService;
import cn.emay.modules.wx.response.impl.DoVideoResponseService;
import cn.emay.modules.wx.response.impl.DoVoiceResponseService;
import cn.emay.modules.wx.response.impl.DoWxLocationService;
import cn.emay.modules.wx.service.WechatReceiveService;
import cn.emay.modules.wx.service.WxCacheService;
import cn.emay.modules.wx.service.WxFansSessionService;
import cn.emay.modules.wx.utils.MessageUtil;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;


/**
 * 微信公众号对接service接口
 * @author zjlWm
 * @date 2015-11-29
 */
@Service
@Transactional(readOnly=true)
public class WechatReceiveServiceImpl implements WechatReceiveService {

	private static final Logger logger = LoggerFactory.getLogger(WechatReceiveService.class);
	
	
	/**
	 * 线程池
	 * 处理微信请求的分发
	 * 
	 */
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	/**
	 * 微信粉丝会话service
	 */
	@Autowired
	private WxFansSessionService wxFansSessionService;
	
	
	/**
	 * 微信缓存数据接口
	 */
	@Autowired
	private WxCacheService wxCacheService;
	
	/**
	 * 提供接收和推送给公众平台消息的加解密接口
	 */
	private WXBizMsgCrypt wxBizMsgCrypt = null;
	
	
	
	/**
	 * 针对连接相应接口
	 */
	@Autowired
	private DoLinkResponseService doLinkResponseService;
	
	/**
	 * 针对音频相应接口
	 */
	@Autowired
	private DoVoiceResponseService doVoiceResponseService;
	
	
	/**
	 * 针对视频消息接口
	 */
	@Autowired
	private DoVideoResponseService doVideoResponseService;
	
	
	/**
	 * 针对小视屏消息处理接口
	 */
	@Autowired
	private DoShortvideoResponseService doShortvideoResponseService;
	
	/**
	 * 针对取消关注事件消息处理接口
	 */
	@Autowired
	private DoUnsubscribeService doUnsubscribeService;
	
	/**
	 * 微信位置消息上报处理接口
	 */
	@Autowired
	private DoWxLocationService doWxLocationService;
	
	/**
	 * 针对文本消息处理接口
	 */
	@Autowired
	private DoTextResponseService doTextResponseService;
	
	/**
	 * 针对订阅事件处理接口
	 */
	@Autowired
	private DoDingYueEventResponseService dingYueEventResponseService;
	
	
	/**
	 * 针对图片消息处理接口
	 */
	@Autowired
	private DoImageResponseService doImageResponseService;
	
	
	/**
	 * 针对地址位置消息处理接口
	 */
	@Autowired
	private DoLocationResponseService doLocationResponseService;
	
	/**
	 * 针对自定义菜单点击事件处理接口
	 */
	@Autowired
	private DoMyMenuEventService doMyMenuEventService;
	
	
	/**
	 * 针对网页跳转消息处理接口
	 */
	@Autowired
	private DoMyViewEventService doMyViewEventService;
	
	
	
	/**
	 * 微信公众号对接 接口
	 */
	@Override
	public String coreService(HttpServletRequest request) {
		String respMessage = "success";
		try{
			ServletInputStream inputStream = request.getInputStream();
			String signature = request.getParameter("signature");
	        String timestamp = request.getParameter("timestamp");
	        String nonce = request.getParameter("nonce");
			 //加密模式
	        String encrypt_type = request.getParameter("encrypt_type");
	        String msg_signature = request.getParameter("msg_signature");
	        
	       //获取XML数据（含加密参数）
	        
	        
            String postbody = StreamUtils.copyToString(inputStream, Charset.forName("utf-8"));
	       //加密方式
	        boolean isAes = "aes".equals(encrypt_type);
	        
			WxWechat wechat=wxCacheService.getWxWechat();
			/**
			 * 有效请求
			 */
			boolean isValidRequest=true;
			/**
			 * xml请求解析数据
			 */
		    EventMessage eventMessageXML = null;
	        if(isAes){
	        	/**
				 * 数据加密解密处理
				 */
				if(null==wxBizMsgCrypt){
					try {
						String encodingToken=wechat.getToken();
						String encodingAesKey=wechat.getEncodingaeskey();
						String appId=wechat.getAppid();
		                wxBizMsgCrypt = new WXBizMsgCrypt(encodingToken, encodingAesKey, appId);
		            } catch (AesException e) {
		                e.printStackTrace();
		            }
				}
	            try {
	                //获取XML数据（含加密参数）
	                //解密XML 数据
	            	String xmlData = wxBizMsgCrypt.decryptMsg(msg_signature, timestamp, nonce, postbody);
	                //XML 转换为bean 对象
	            	eventMessageXML = XMLConverUtil.convertToObject(EventMessage.class, xmlData);
	            } catch (AesException e) {
	                e.printStackTrace();
	            }
	        }else{
	            //验证请求签名
	            if(!signature.equals(SignatureUtil.generateEventMessageSignature(wechat.getToken(),timestamp,nonce))){
	                System.out.println("The request signature is invalid");
	                return "success";
	            }

	            if(inputStream!=null){
	                //XML 转换为bean 对象
	                eventMessageXML = XMLConverUtil.convertToObject(EventMessage.class,postbody);
	            }
	        }
	        
	        
	        // 消息类型
	        String msgType = eventMessageXML.getMsgType();//requestMap.get("MsgType");
	        String msgId = eventMessageXML.getMsgId();//requestMap.get("MsgId");
	        
	        
	        String key = eventMessageXML.getFromUserName() + "__"+ eventMessageXML.getToUserName() + "__"+ eventMessageXML.getMsgId() + "__"+ eventMessageXML.getCreateTime();
	        
	        /**
	         * 消息key
	         */
	        if(CacheUtils.get(key)!=null){
	        	return respMessage;
	        }else{
	        	CacheUtils.put(key, eventMessageXML.getMsgId());
	        }
	        final EventMessage eventMessage=eventMessageXML;
	        
			// 发送方帐号（open_id）
			final String fromUserName = eventMessage.getFromUserName();//requestMap.get("FromUserName");
			final String toUserName=eventMessage.getToUserName();
			
			
			//消息内容
			String content = eventMessage.getContent();//requestMap.get("Content");
			logger.info("------------微信客户端发送请求---------------------   |   fromUserName:"+fromUserName+"   |   ToUserName:"+toUserName+"   |   msgType:"+msgType+"   |   msgId:"+msgId+"   |   content:"+content);
			//根据微信ID,获取配置的全局的数据权限ID
			logger.info("-toUserName--------"+toUserName);
			
			String wechatId = wechat.getId();
			logger.info("-wechatId--------"+wechatId);

			
			// 【微信触发类型】文本消息
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				/**
				 * 异步线程处理
				 */
				taskExecutor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							doTextResponseService.execute(eventMessage);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				
			}
			
			// 【微信触发类型】图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				/**
				 * 异步线程处理
				 */
				taskExecutor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							doImageResponseService.execute(eventMessage);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
			// 【微信触发类型】地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				/**
				 * 异步线程处理
				 */
				taskExecutor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							doLocationResponseService.execute(eventMessage);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
			
			// 【微信触发类型】链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				/**
				 * 异步线程处理
				 */
				taskExecutor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							doLinkResponseService.execute(eventMessage);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				
				
			}
			// 【微信触发类型】音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				/**
				 * 异步线程处理
				 */
				taskExecutor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							doVoiceResponseService.execute(eventMessage);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
			
			//【微信触发类型】视频消息
			else if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VIDEO)){
				/**
				 * 异步线程处理
				 */
				taskExecutor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							doVideoResponseService.execute(eventMessage);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
			
			//【微信触发类型】小视频消息
			else if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_SHORTVIDEO)){
				/**
				 * 异步线程处理
				 */
				taskExecutor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							doShortvideoResponseService.execute(eventMessage);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
			
			
			// 【微信触发类型】事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String event = eventMessage.getEvent();
				// 订阅
				if (event.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					/**
					 * 异步线程处理
					 */
					taskExecutor.execute(new Runnable() {
						@Override
						public void run() {
							try {
								dingYueEventResponseService.execute(eventMessage);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					
					
				}
				// 取消订阅
				else if (event.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					isValidRequest=false;
					/**
					 * 异步线程处理
					 */
					taskExecutor.execute(new Runnable() {
						@Override
						public void run() {
							try {
								doUnsubscribeService.execute(eventMessage);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
				
				// 自定义菜单点击事件
				else if (event.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					/**
					 * 异步线程处理
					 */
					taskExecutor.execute(new Runnable() {
						@Override
						public void run() {
							try {
								doMyMenuEventService.execute(eventMessage);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					
				}
				// 点击微信菜单跳转到网页
				else if (event.equals(MessageUtil.EVENT_TYPE_VIEW)) {
					/**
					 * 异步线程处理
					 */
					taskExecutor.execute(new Runnable() {
						@Override
						public void run() {
							try {
								doMyViewEventService.execute(eventMessage);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					
					
				}
				// 模板消息推送成功后，推送成功状态返回
				else if (event.equals(MessageUtil.EVENT_TYPE_TEMPLATESENDJOBFINISH)) {
					/**
					 * 异步线程处理
					 */
					taskExecutor.execute(new Runnable() {
						@Override
						public void run() {
							try {
//								System.out.println(JsonUtil.toJSONString(requestMap));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					
				}
				
				//用户同意上报地理位置后，每次进入公众号会话时，都会在进入时上报地理位置，上报地理位置
				else if(event.equals(MessageUtil.EVENT_TYPE_LOCATION)){
					/**
					 * 异步线程处理
					 */
					taskExecutor.execute(new Runnable() {
						@Override
						public void run() {
							try{
								doWxLocationService.execute(eventMessage);
							}catch (Exception e) {
								logger.error("上报地理位置异常", e);
							}
						}
					});
				}
				
				/**
				 * 有效请求、更新微信粉丝会话
				 */
				if(isValidRequest){
					taskExecutor.execute(new Runnable() {

						@Override
						public void run() {
							WxFansSession wxFansSession=wxFansSessionService.findCurrentWxFansSessionByOpenid(eventMessage.getFromUserName());
							Long surplusDate=24*60*60*1000*1L;
							if(null==wxFansSession){
								wxFansSession=new WxFansSession();
								wxFansSession.setOpenid(eventMessage.getFromUserName());
								short status=1;
								wxFansSession.setSatus(status);
								wxFansSession.setSurplusDate(surplusDate);
								/**
								 * 开始会话时间
								 */
								wxFansSession.setStartSessionTime(new Timestamp(System.currentTimeMillis()));
								/**
								 * 更新会话时间
								 */
								wxFansSession.setEndSessionTime(wxFansSession.getStartSessionTime());
							}else{
								/**
								 * 更新会话时间
								 */
								wxFansSession.setEndSessionTime(new Timestamp(System.currentTimeMillis()));
								
								/**
								 * 更新会话时间
								 */
								wxFansSession.setSurplusDate(surplusDate);
							}
							wxFansSessionService.save(wxFansSession);
						}
					});
					
				}
			}
		}catch (Exception e) {
			logger.error("微信请求--", e);
		}
		return respMessage;
	}



	
	
	
	
	
	@SuppressWarnings("unused")
	@Override
	public String coreServiceExtend(HttpServletRequest request) {
		String respMessage = "success";
		try{
			ServletInputStream inputStream = request.getInputStream();
			String signature = request.getParameter("signature");
	        String timestamp = request.getParameter("timestamp");
	        String nonce = request.getParameter("nonce");
			 //加密模式
	        String encrypt_type = request.getParameter("encrypt_type");
	        String msg_signature = request.getParameter("msg_signature");
	        
	       //获取XML数据（含加密参数）
	        
	        
            String postbody = StreamUtils.copyToString(inputStream, Charset.forName("utf-8"));
	       //加密方式
	        boolean isAes = "aes".equals(encrypt_type);
	        
			WxWechat wechat=wxCacheService.getWxWechat();
			/**
			 * 有效请求
			 */
			boolean isValidRequest=true;
			/**
			 * xml请求解析数据
			 */
		    EventMessage eventMessageXML = null;
	        if(isAes){
	        	/**
				 * 数据加密解密处理
				 */
				if(null==wxBizMsgCrypt){
					try {
						String encodingToken=wechat.getToken();
						String encodingAesKey=wechat.getEncodingaeskey();
						String appId=wechat.getAppid();
		                wxBizMsgCrypt = new WXBizMsgCrypt(encodingToken, encodingAesKey, appId);
		            } catch (AesException e) {
		                e.printStackTrace();
		            }
				}
	            try {
	                //获取XML数据（含加密参数）
	                //解密XML 数据
	            	String xmlData = wxBizMsgCrypt.decryptMsg(msg_signature, timestamp, nonce, postbody);
	                //XML 转换为bean 对象
	            	eventMessageXML = XMLConverUtil.convertToObject(EventMessage.class, xmlData);
	            } catch (AesException e) {
	                e.printStackTrace();
	            }
	        }else{
	            //验证请求签名
	            if(!signature.equals(SignatureUtil.generateEventMessageSignature(wechat.getToken(),timestamp,nonce))){
	                System.out.println("The request signature is invalid");
	                return "success";
	            }

	            if(inputStream!=null){
	                //XML 转换为bean 对象
	                eventMessageXML = XMLConverUtil.convertToObject(EventMessage.class,postbody);
	            }
	        }
		}catch (Exception e) {
			
		}
		
		return respMessage;
	}
	
	
//	/**
//	 * 默认返回值
//	 * @param requestMap
//	 */
//	public void doDefaultResponse(String fromUserName){
//		TextMessage sendTextMessage=new TextMessage(fromUserName, getMainMenu());
//		sendTextMessage.getText().setContent(defaultWelcome);
//		String accessToken = wxCacheService.getToken();
//		MessageAPI.messageCustomSend(accessToken, sendTextMessage);
//	}
	
//	
//	/**
//	 * 针对链接消息
//	 */
//	public void doLinkResponse(EventMessage eventMessage){
//		String fromUserName = eventMessage.getFromUserName();
//		TextMessage sendTextMessage=new TextMessage(fromUserName, getMainMenu());
//		sendTextMessage.getText().setContent(defaultWelcome);
//		String accessToken = wxCacheService.getToken();
//		MessageAPI.messageCustomSend(accessToken, sendTextMessage);
//	}
	
//	
//	/**
//	 * 针对图片消息
//	 * @param requestMap
//	 * @throws IOException 
//	 */
//	public void doImageResponse(EventMessage eventMessage,String wxPath) throws IOException{
////		String PicUrl=requestMap.get("PicUrl");
//		String MediaId=eventMessage.getMediaId();
//		String MsgId=eventMessage.getMsgId();
//		String accessToken = wxCacheService.getToken();
//		
//		Date createdon=new Date();
//		String realPath=wxPath+"/image/"+DateUtils.formatDate(createdon, "yyyMMdd");
//		FileUtils.createDirectory(realPath);
//		
//		byte[]pimageByte=MediaAPI.mediaGet(accessToken, MediaId).getBytes();
//		File wxmgage=new File(realPath+"/"+MsgId+".png");
//		FileUtils.writeByteArrayToFile(wxmgage, pimageByte);
//	}
	
	
//	/**
//	 * 针对音频消息
//	 * @param requestMap
//	 * @throws IOException 
//	 */
//	public void doVoiceResponse(EventMessage eventMessage,String wxPath) throws IOException{
//		String Format=eventMessage.getFormat();
//		String MediaId=eventMessage.getMediaId();
//		String MsgId=eventMessage.getMsgId();
//		String accessToken = wxCacheService.getToken();
//		
//		Date createdon=new Date();
//		String realPath=wxPath+"/voice/"+DateUtils.formatDate(createdon, "yyyMMdd");
//		FileUtils.createDirectory(realPath);
//		
//		byte[]pimageByte=MediaAPI.mediaGet(accessToken, MediaId).getBytes();
//		File wxmgage=new File(realPath+"/"+MsgId+"."+Format);
//		FileUtils.writeByteArrayToFile(wxmgage, pimageByte);
//	}
//	
//	
//	/**
//	 * 针对视频消息
//	 * @param requestMap
//	 * @throws IOException 
//	 */
//	public void doVideoResponse(EventMessage eventMessage,String wxPath) throws IOException{
//		String MediaId=eventMessage.getMediaId();
//		String MsgId=eventMessage.getMsgId();
//		String accessToken = wxCacheService.getToken();
//		
//		Date createdon=new Date();
//		String realPath=wxPath+"/video/"+DateUtils.formatDate(createdon, "yyyMMdd");
//		FileUtils.createDirectory(realPath);
//		
//		
//		byte[]pimageByte=MediaAPI.mediaGet(accessToken, MediaId).getBytes();
//		File wxmgage=new File(realPath+"/"+MsgId+".mp4");
//		FileUtils.writeByteArrayToFile(wxmgage, pimageByte);
//	}
	
	
//	/**
//	 * 针对小视频消息
//	 * @param requestMap
//	 * @throws IOException 
//	 */
//	public void doShortvideoResponse(EventMessage eventMessage,String wxPath) throws IOException{
//		String MediaId=eventMessage.getMediaId();
//		String MsgId=eventMessage.getMsgId();
//		String accessToken = wxCacheService.getToken();
//		
//		Date createdon=new Date();
//		String realPath=wxPath+"/shortvideo/"+DateUtils.formatDate(createdon, "yyyMMdd");
//		FileUtils.createDirectory(realPath);
//		
//		
//		byte[]pimageByte=MediaAPI.mediaGet(accessToken, MediaId).getBytes();
//		File wxmgage=new File(realPath+"/"+MsgId+".mp4");
//		FileUtils.writeByteArrayToFile(wxmgage, pimageByte);
//	}
//	
//	
//	/**
//	 * 针对地理位置消息
//	 */
//	public void doLocationResponse(EventMessage eventMessage){
//		String fromUserName = eventMessage.getFromUserName();
//		// 公众帐号
//		String toUserName =eventMessage.getToUserName();
//		// 默认回复此文本消息
//		TextMessageResp textMessage = new TextMessageResp();
//		textMessage.setToUserName(fromUserName);
//		textMessage.setFromUserName(toUserName);
//		textMessage.setCreateTime(new Date().getTime());
//		textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
//		textMessage.setContent(getMainMenu());
//		eventMessage.getLocation_X();//
//		Double Location_X = Double.parseDouble(eventMessage.getLocation_X());
//		Double Location_Y = Double.parseDouble(eventMessage.getLocation_Y());
//		String Scale = eventMessage.getScale();
//		doMyMapEvent2(toUserName, fromUserName, Location_X, Location_Y, Scale);
//		
//	}
	
	
	
//	/**
//	 * 针对文本消息
//	 * 
//	 * @param content
//	 * @param toUserName
//	 * @param textMessage
//	 * @param bundler
//	 * @param sys_accountId
//	 * @param respMessage
//	 * @param fromUserName
//	 * @param request
//	 * @throws Exception
//	 */
//	public void doTextResponse(EventMessage eventMessage) throws Exception {
//		// 根据微信ID,获取配置的全局的数据权限ID
//		String content = eventMessage.getContent();
//		String fromUserName = eventMessage.getFromUserName();
//		// 公众帐号
//		String toUserName = eventMessage.getToUserName();
//		// 消息类型
//		String msgType = eventMessage.getMsgType();
//		String msgId = eventMessage.getMsgId();
//		WxWechat wxWechat=wxCacheService.getWxWechat();
//		WxFans wxFans=wxFansService.findWxFansByOpenid(fromUserName);
//		if(null!=wxFans){
//			try{
//				// 保存接收到的信息
//				ReceiveText receiveText = new ReceiveText();
//				receiveText.setContent(content);
//				Timestamp temp = Timestamp.valueOf(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
//				receiveText.setCreateTime(temp);
//				receiveText.setFromUserName(fromUserName);
//				receiveText.setToUserName(toUserName);
//				receiveText.setMsgId(msgId);
//				receiveText.setMsgType(msgType);
//				receiveText.setResponse("0");
//				receiveText.setNickName(wxFans.getNickname());
//				receiveText.setAccountId(toUserName);
//				receiveTextService.save(receiveText);
//			}catch (Exception e) {
//				logger.error("接收信息保存异常", e);
//			}
//			
//		}
//		
//		
//		// =================================================================================================================
//		// Step.1 判断关键字信息中是否管理该文本内容。有的话优先采用数据库中的回复
////		LogUtil.info("------------微信客户端发送请求--------------Step.1 判断关键字信息中是否管理该文本内容。有的话优先采用数据库中的回复---");
//		/**
//		 * 查询获取的值，是否又对应关键字
//		 */
//		AutoResponse autoResponse =autoResponseService.findAutoResponseByKeyWord(content);
//		// 根据系统配置的关键字信息，返回对应的消息
//		if (autoResponse != null) {
//			/**
//			 * 返回数据类型----文本
//			 */
//			String resMsgType = autoResponse.getMsgType();
//			if (MessageUtil.REQ_MESSAGE_TYPE_TEXT.equals(resMsgType)) {
//				// 根据返回消息key，获取对应的文本消息返回给微信客户端
//				TextTemplate textTemplate = textTemplateService.getTextTemplate(wxWechat.getId(), autoResponse.getTemplateName());
//				
//				/**
//				 * 需要推送的客服消息
//				 */
//				TextMessage sendTextMessage=new TextMessage(fromUserName, textTemplate.getContent());
//				String accessToken =wxCacheService.getToken();
//				MessageAPI.messageCustomSend(accessToken, sendTextMessage);
//			} 
//			/**
//			 * 返回数据类型----图文
//			 */
//			else if (MessageUtil.RESP_MESSAGE_TYPE_NEWS.equals(resMsgType)) {
//				ResourceBundle bundler = ResourceBundle.getBundle("sysConfig");
//				
//				List<NewsItem> newsList =newsItemService.findNewsItemByNewsTemplate(autoResponse.getResContent());
//				List<weixin.popular.bean.message.message.NewsMessage.Article> articles=new ArrayList<weixin.popular.bean.message.message.NewsMessage.Article>();
//				for(NewsItem news : newsList){
//					String url = "";
//					if (StringUtils.isNotBlank(news.getUrl())) {
//						url = bundler.getString("domain") + "/newsItemController.do?newscontent&id=" + news.getId();
//					} else {
//						url = news.getUrl()+"&openid="+fromUserName;
//					}
//					weixin.popular.bean.message.message.NewsMessage.Article article = new weixin.popular.bean.message.message.NewsMessage.Article(news.getTitle(), news.getDescription(), url, bundler.getString("domain") + "/" + news.getImagePath());
//					articles.add(article);
//				}
//				
//				/**
//				 * 需要推送的客服消息
//				 */
//				Message message=new weixin.popular.bean.message.message.NewsMessage(fromUserName,articles);
//				String accessToken = wxCacheService.getToken();
//				MessageAPI.messageCustomSend(accessToken, message);
//				
//			}
//		}else {
//			/**
//			 * 返回默认值
//			 */
//			TextMessage sendTextMessage=new TextMessage(fromUserName, defaultWelcome);
//			String accessToken = wxCacheService.getToken();
//			MessageAPI.messageCustomSend(accessToken, sendTextMessage);
//			
//			
//			
//			/*
//			// Step.2 通过微信扩展接口（支持二次开发，例如：翻译，天气）
//			LogUtil.info("------------微信客户端发送请求--------------Step.2  通过微信扩展接口（支持二次开发，例如：翻译，天气）---");
//			List<WeixinExpandconfigEntity> weixinExpandconfigEntityLst = weixinExpandconfigService.findByQueryString("FROM WeixinExpandconfigEntity");
//			if (weixinExpandconfigEntityLst.size() != 0) {
//				for (WeixinExpandconfigEntity wec : weixinExpandconfigEntityLst) {
//					boolean findflag = false;// 是否找到关键字信息
//					// 如果已经找到关键字并处理业务，结束循环。
//					if (findflag) {
//						break;// 如果找到结束循环
//					}
//					String[] keys = wec.getKeyword().split(",");
//					for (String k : keys) {
//						if (content.indexOf(k) != -1) {
//							String className = wec.getClassname();
//							KeyServiceI keyService = (KeyServiceI) Class.forName(className).newInstance();
////							respMessage = keyService.excute(content, textMessage, request);
//							findflag = true;// 改变标识，已经找到关键字并处理业务，结束循环。
//							break;// 当前关键字信息处理完毕，结束当前循环
//						}
//					}
//				}
//			}
//			*/
//			
//			
//		}
//	}


//	/**
//	 * 针对【关注--取消】事件消息
//	 * @param requestMap
//	 */
//	public void doUnsubscribe(EventMessage eventMessage){
//		String fromUserName = eventMessage.getFromUserName();
//		// 标注订阅状态为取消订阅
//		WxFans wxFans=wxFansService.findWxFansByOpenid(fromUserName);
//		if(null!=wxFans){
//			wxFans.setIsFollow(false);
//			wxFans.setUnfollowtime(new Timestamp(System.currentTimeMillis()));
//			wxFansService.save(wxFans);
//		}
//	}
	
	
//	/**
//	 * 针对【关注--订阅】事件消息
//	 * 
//	 */
//	public void doDingYueEventResponse(EventMessage eventMessage) {
//		// 发送方帐号（open_id）
//		String fromUserName = eventMessage.getFromUserName();
//		WxWechat wxWechat=wxCacheService.getWxWechat();
//		// 关注时，先把会员写入数据库，然后回复欢迎消息,要考虑 重新关注的情况 ；
//		saveWeixinUser(fromUserName);
//		
//		Subscribe subscribe = subscribeService.findSubscribeByWechatId(wxWechat.getId());
//		if (null!=subscribe) {
//			String type = subscribe.getMsgType();
//			/**
//			 * 【针对被动回复消息----文本消息】
//			 */
//			if (MessageUtil.REQ_MESSAGE_TYPE_TEXT.equals(type)) {
//				TextTemplate textTemplate =textTemplateService.get(subscribe.getTemplateId());
//				String content = textTemplate.getContent();
//				TextMessage sendTextMessage=new TextMessage(fromUserName, content);
//				sendTextMessage.getText().setContent(defaultWelcome);
//				String accessToken = wxCacheService.getToken();
//				MessageAPI.messageCustomSend(accessToken, sendTextMessage);
//			}
//			/**
//			 * 【针对被动回复消息----图文消息】
//			 */
//			else if (MessageUtil.RESP_MESSAGE_TYPE_NEWS.equals(type)) {
//				ResourceBundle bundler = ResourceBundle.getBundle("sysConfig");
//				List<NewsItem> newsList =newsItemService.findNewsItemByNewsTemplate(subscribe.getTemplateId());
//				List<weixin.popular.bean.message.message.NewsMessage.Article> articles=new ArrayList<weixin.popular.bean.message.message.NewsMessage.Article>();
//				for(NewsItem news : newsList){
//					String url = "";
//					if (StringUtils.isEmpty(news.getUrl())) {
//						url = bundler.getString("domain") + "/newsItemController.do?newscontent&id=" + news.getId();
//					} else {
//						url = news.getUrl();
//					}
//					weixin.popular.bean.message.message.NewsMessage.Article article = new weixin.popular.bean.message.message.NewsMessage.Article(news.getTitle(), news.getDescription(), url, bundler.getString("domain") + "/" + news.getImagePath());
//					articles.add(article);
//				}
//				
//				/**
//				 * 需要推送的客服消息
//				 */
//				Message message=new weixin.popular.bean.message.message.NewsMessage(fromUserName,articles);
//				String accessToken = wxCacheService.getToken();
//				MessageAPI.messageCustomSend(accessToken, message);
//				
//			}
//		}else{
//			TextMessage sendTextMessage=new TextMessage(fromUserName, "谢谢您的关注！");
//			String accessToken = wxCacheService.getToken();
//			MessageAPI.messageCustomSend(accessToken, sendTextMessage);
//		}
//	}

//	/**
//	 * 
//	 * @param requestMap
//	 * @param textMessage
//	 * @param bundler
//	 * @param respMessage
//	 * @param toUserName
//	 * @param fromUserName
//	 * @param respContent
//	 * @param sys_accountId
//	 * @param request
//	 * @return
//	 * @throws ClassNotFoundException
//	 * @throws IllegalAccessException
//	 * @throws InstantiationException
//	 */
//	public void doMyMenuEvent(EventMessage eventMessage,HttpServletRequest request) throws Exception {
//		/**
//		 * 事件KEY值、设置的跳转URL
//		 */
//		final String eventKey = eventMessage.getEventKey();
//		// 发送方帐号（open_id）
//		final String fromUserName = eventMessage.getFromUserName();
//		
//		// 自定义菜单CLICK类型
//		final MenuEntity menuEntity =menuEntityService.findMenuEntityByMenuKey(eventKey);
//		if (menuEntity != null && StringUtils.isNotEmpty(menuEntity.getTemplateId())) {
//			String type = menuEntity.getMsgType();
//			/**
//			 * 确定点击菜单返回消息类型--文本类型
//			 */
//			if (MessageUtil.REQ_MESSAGE_TYPE_TEXT.equals(type)) {
//				TextTemplate textTemplate =textTemplateService.get(menuEntity.getTemplateId());
//				String content = textTemplate.getContent();
//				WxFans wxFans=wxFansService.findWxFansByOpenid(fromUserName);
//				//假如已经关注的粉丝、数据库里面没有存在、则重新保存
//				if(null==wxFans){
//					wxFans=saveWeixinUserSimple(fromUserName);
//				}
//				
//				/**
//				 * 菜单名称
//				 */
//				String menuName = menuEntity.getName();
//
//				
//				/**
//				 * 日志记录
//				 */
//				if(!menuName.equals("信息变更")){
//					FunctionLog functionLog = new FunctionLog();
//					functionLog.setFunctionName(menuName);
//					functionLog.setEventKey(eventKey);
//					functionLog.setOpenid(fromUserName);
//					functionLog.setCredateDate(new Date());
//					functionSave(functionLog);
//				}
//				TextMessage sendTextMessage=new TextMessage(fromUserName, content);
//				String accessToken = wxCacheService.getToken();
//				MessageAPI.messageCustomSend(accessToken, sendTextMessage);
//				
//				
//			} 
//			//微信菜单点击-返回图文消息
//			else if (MessageUtil.RESP_MESSAGE_TYPE_NEWS.equals(type)) {
//				ResourceBundle bundler = ResourceBundle.getBundle("sysConfig");
//				List<NewsItem> newsList =newsItemService.findNewsItemByNewsTemplate(menuEntity.getTemplateId());
//				
//				List<weixin.popular.bean.message.message.NewsMessage.Article> articles=new ArrayList<weixin.popular.bean.message.message.NewsMessage.Article>();
//				for(NewsItem news : newsList){
//					String url = "";
//					if (StringUtils.isEmpty(news.getUrl())) {
//						url = bundler.getString("domain") + "/newsItemController.do?newscontent&id=" + news.getId();
//					} else {
//						url = news.getUrl();
//					}
//					weixin.popular.bean.message.message.NewsMessage.Article article = new weixin.popular.bean.message.message.NewsMessage.Article(news.getTitle(), news.getDescription(), url, bundler.getString("domain") + "/" + news.getImagePath());
//					articles.add(article);
//				}
//				
//				/**
//				 * 需要推送的客服消息
//				 */
//				Message message=new weixin.popular.bean.message.message.NewsMessage(fromUserName,articles);
//				String accessToken = wxCacheService.getToken();
//				MessageAPI.messageCustomSend(accessToken, message);
//				
//				
//			} 
//			//开始会话
//			else if(MessageUtil.RESP_MESSAGE_TYPE_SESSION.equals(type)){
//				System.out.println("------------------开始会话-----------------------");
//			}
//			//微信菜单点击返回消息--扩展类型
//			else if ("expand".equals(type)) {
//				/*
//				WeixinExpandconfigEntity expandconfigEntity = weixinExpandconfigService.getEntity(WeixinExpandconfigEntity.class, menuEntity.getTemplateId());
//				String className = expandconfigEntity.getClassname();
//				KeyServiceI keyService = (KeyServiceI) Class.forName(className).newInstance();
//				respMessage = keyService.excute("", textMessage, request);
//				*/
//				
//			} 
//			//模板消息
//			else if (MessageUtil.RESP_MESSAGE_TYPE_TEMPLATE.equals(type)) {
//
//				// 如果该用户未在微信用户表中，则补录进去；
//				// SaveWeixinUser(fromUserName);
//				// 判断用户有没有绑定会员卡号 ，没绑定则提示文本消息 。绑定了则推送模板消息
//				WxFans wxFans=wxFansService.findWxFansByOpenid(fromUserName);
//				doTemplateMessageResponse(menuEntity.getTemplateId(), wxFans);
//			}
//		}else{
//			TextMessage sendTextMessage=new TextMessage(fromUserName, "抱歉！没有找到对应的数据，请联系管理员！");
//			String accessToken = wxCacheService.getToken();
//			MessageAPI.messageCustomSend(accessToken, sendTextMessage);
//		}
//	}

	
//	/**
//	 * 网页跳转
//	 * @param requestMap
//	 * @param toUserName
//	 * @param fromUserName
//	 */
//	public void doMyViewEvent(EventMessage eventMessage, String toUserName, String fromUserName) {
//		String eventKey = eventMessage.getEventKey();
//		/**
//		 * 微官网
//		 */
//		if ("http://www.5dgz.com/wap".equals(eventKey)) {
//			try {
//				FunctionLog functionLog = new FunctionLog();
//				functionLog.setFunctionName("微官网");
//				functionLog.setEventKey(eventKey);
//				functionLog.setOpenid(fromUserName);
//				functionLog.setCredateDate(new Date());
//				functionLogService.save(functionLog);
//			} catch (Exception e) {
//				logger.info("微信请求FunctionLog", e);
//			}
//		}
//	}

	
	
//	@SuppressWarnings("unchecked")
//	public void doMyMapEvent2(String toUserName, String fromUserName, Double x, Double y, String sacle) {
//		try {
//			String locationCity = testGetRequest(y, x);
//			if (StringUtils.isNotBlank(locationCity)) {
//				Map<String, Object> result = getStroebyMap(x, y, locationCity);
//				Double dis = Double.parseDouble(result.get("dis").toString());
//				double d = dis / 1000;
//				/**
//				 * 查询到门店数据
//				 */
//				if (null != result.get("mloc")) {
//					Map<String, String> mloc = (Map<String, String>) result.get("mloc");
//					String MLOC_REGNM = (mloc.get("MLOC_REGNM") == null ? "" : mloc.get("MLOC_REGNM")).toString();
//					String MLOC_ADDR1 = mloc.get("MLOC_ADDR1");
//					String MLOC_TELNO = mloc.get("MLOC_TELNO");
//					String MLOC_GRNPF = mloc.get("MLOC_GRNPF");
//					double mloc_x = Double.parseDouble(MLOC_GRNPF.split(",")[0]);
//					double mloc_y = Double.parseDouble(MLOC_GRNPF.split(",")[1]);
//					
//					
//					
//					List<weixin.popular.bean.message.message.NewsMessage.Article> articles=new ArrayList<weixin.popular.bean.message.message.NewsMessage.Article>();
//					weixin.popular.bean.message.message.NewsMessage.Article article=new weixin.popular.bean.message.message.NewsMessage.Article(MLOC_REGNM, "地址：" + MLOC_ADDR1 + "\n" + "电话：" + MLOC_TELNO + "\n" + "距离：" + d + "公里", "http://api.map.baidu.com/marker?location=" + mloc_y + "," + mloc_x + "&title=" + MLOC_REGNM + "&content=" + MLOC_ADDR1 + "&output=html", "http://api.map.baidu.com/staticimage?width=400&height=300&center=" + mloc_x + "," + mloc_y + "&zoom=" + 15 + "&markers=" + mloc_x + "," + mloc_y + "&markerStyles=l");
//					articles.add(article);
//					
//					/**
//					 * 需要推送的客服消息
//					 */
//					Message message=new weixin.popular.bean.message.message.NewsMessage(fromUserName,articles);
//					String accessToken = wxCacheService.getToken();
//					MessageAPI.messageCustomSend(accessToken, message);
//				}else{
//					TextMessage sendTextMessage=new TextMessage(fromUserName, "抱歉，您周边未查询到专营店！");
//					sendTextMessage.getText().setContent(defaultWelcome);
//					String accessToken = wxCacheService.getToken();
//					MessageAPI.messageCustomSend(accessToken, sendTextMessage);
//				}
//				try {
//					FunctionLog functionLog = new FunctionLog();
//					functionLog.setFunctionName("附近店铺");
//					functionLog.setEventKey("LOCATION");
//					functionLog.setOpenid(fromUserName);
//					functionLog.setCredateDate(new Date());
//					functionLogService.save(functionLog);
//				} catch (Exception e) {
//					logger.info("附近店铺查询异常", e);
//				}
//			}
//		} catch (Exception e) {
//			logger.info("微信请求doMyMapEvent", e);
//		}
//	}
	
	
//	/**
//	 * 位置监听事件
//	 */
//	@SuppressWarnings("unchecked")
//	public String doMyMapEvent(String toUserName, String fromUserName, Double x, Double y, String sacle) {
//		String respMessage = null;
//		try {
//			String locationCity = testGetRequest(y, x);
//			if (StringUtils.isNotBlank(locationCity)) {
//				Map<String, Object> result = getStroebyMap(x, y, locationCity);
//				Double dis = Double.parseDouble(result.get("dis").toString());
//				double d = dis / 1000;
//				if (null != result.get("mloc")) {
//					Map<String, Object> mloc = (Map<String, Object>) result.get("mloc");
//					String MLOC_REGNM = (mloc.get("MLOC_REGNM") == null ? "" : mloc.get("MLOC_REGNM")).toString();
//					String MLOC_ADDR1 = mloc.get("MLOC_ADDR1").toString();
//					String MLOC_TELNO = mloc.get("MLOC_TELNO").toString();
//					String MLOC_GRNPF = mloc.get("MLOC_GRNPF").toString();
//					double mloc_x = Double.parseDouble(MLOC_GRNPF.split(",")[0]);
//					double mloc_y = Double.parseDouble(MLOC_GRNPF.split(",")[1]);
//					NewsMessageResp newsmessage = new NewsMessageResp();
//					newsmessage.setFromUserName(toUserName);
//					newsmessage.setToUserName(fromUserName);
//					newsmessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
//					newsmessage.setArticleCount(1);
//					List<Article> articles = new ArrayList<Article>();
//					Article art = new Article();
//					art.setTitle(MLOC_REGNM);
//					art.setDescription("地址：" + MLOC_ADDR1 + "\n" + "电话：" + MLOC_TELNO + "\n" + "距离：" + d + "公里");
//					art.setPicUrl("http://api.map.baidu.com/staticimage?width=400&height=300&center=" + mloc_x + "," + mloc_y + "&zoom=" + 15 + "&markers=" + mloc_x + "," + mloc_y + "&markerStyles=l");
//					art.setUrl("http://api.map.baidu.com/marker?location=" + mloc_y + "," + mloc_x + "&title=" + MLOC_REGNM + "&content=" + MLOC_ADDR1 + "&output=html");
//					articles.add(art);
//					newsmessage.setArticles(articles);
//					respMessage = MessageUtil.newsMessageToXml(newsmessage);
//				}
//			}
//		} catch (Exception e) {
//			logger.info("微信请求doMyMapEvent", e);
//		}
//		return respMessage;
//	}

//	/**
//	 * 格式化月份显示
//	 */
//	String formatMonth(String month) {
//		if (month.length() == 6) {
//			return month.substring(0, 4) + "年" + month.substring(4) + "月";
//		}
//		return "";
//	}

	
//	/**
//	 * 日志保存
//	 */
//	public void functionSave(final FunctionLog functionLog){
//		
//		taskExecutor.execute(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					functionLogService.save(functionLog);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//		
//	}
	
	
//	/**
//	 * 模板消息回复
//	 */
//	public void doTemplateMessageResponse(String templatemessageId,WxFans wxFans) {
//		Templatemessage templatemessage=templatemessageService.get(templatemessageId);
//		System.out.println(templatemessage);
//	}

	
//	/**
//	 * 微信粉丝用户保存
//	 * @param openid
//	 * @return
//	 */
//	public WxFans saveWeixinUserSimple(String openid){
//		try{
//			WxFans wxFans =new WxFans();
//			wxFans.setIsFollow(true);
//			wxFans.setOpenid(openid);
//			wxFans.setFollowtime(new Timestamp(System.currentTimeMillis()));
//			wxFansService.save(wxFans);
//			return wxFans;
//		}catch (Exception e) {
//			logger.error("微信粉丝用户"+openid+"保存异常", e);
//		}
//		return null;
//	}
	
	
//	/**
//	 * 保存关注者
//	 * 
//	 * @param openid
//	 * @return
//	 */
//	public String saveWeixinUser(String openid) {
//		WxFans wxFans=wxFansService.findWxFansByOpenid(openid);
//		try {
//			if (wxFans == null) {
//				wxFans = new WxFans();
//				wxFans.setIsFollow(true);
//				wxFans.setOpenid(openid);
//				wxFans.setFollowtime(new Timestamp(System.currentTimeMillis()));
//				wxFansService.save(wxFans);
//			}
//		} catch (Exception e) {
//			logger.info("微信关注用户保存:" + wxFans.getOpenid(), e);
//		}
//		return "";
//
//	}
	
	
	
//	/**
//	 * 欢迎语
//	 * 
//	 * @return
//	 */
//	public static String getMainMenu() {
//		// 复杂字符串文本读取，采用文件方式存储
//		String html = new FreemarkerHelper().parseTemplate("/weixin/welcome.ftl", null);
//		return html;
//	}
//
//	@SuppressWarnings("unchecked")
//	public static String getGeocoder(Double x, Double y) {
//		String locationCity = "";
//		try {
//			URL url = new URL("http://api.map.baidu.com/geocoder/v2/?ak=" + ResourceUtil.getConfigByName("ak") + "&location=" + x + "," + y + "&output=json&pois=0");
//			HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
//			urlcon.connect(); // 获取连接
//			InputStream is = urlcon.getInputStream();
//			BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
//			StringBuffer bs = new StringBuffer();
//			String l = null;
//			while ((l = buffer.readLine()) != null) {
//				bs.append(l).append("/n");
//			}
//
//			if (StringUtils.isNotBlank(bs.toString())) {
//				JSONObject json = JSONObject.fromObject(bs.toString());
//				if (json.getInt("status") == 0) {
//					Map<String, Object> result = (Map<String, Object>) json.get("result");
//					Map<String, Object> addressComponent = (Map<String, Object>) result.get("addressComponent");
//					locationCity = addressComponent.get("city").toString();
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return locationCity;
//	}

//	@SuppressWarnings("unchecked")
//	public static String testGetRequest(double x, double y) throws IllegalStateException, IOException {
//		String locationCity = null;
//		HttpClient client = new HttpClient();
//		StringBuilder sb = new StringBuilder();
//		InputStream ins = null;
//		// Create a method instance.
//		GetMethod method = new GetMethod("http://api.map.baidu.com/geocoder/v2/?ak=" + ResourceUtil.getConfigByName("ak") + "&location=" + x + "," + y + "&output=json&pois=0");
//		// Provide custom retry handler is necessary
//		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
//		try {
//			// Execute the method.
//			int statusCode = client.executeMethod(method);
//			if (statusCode == HttpStatus.SC_OK) {
//				ins = method.getResponseBodyAsStream();
//				byte[] b = new byte[1024];
//				int r_len = 0;
//				while ((r_len = ins.read(b)) > 0) {
//					sb.append(new String(b, 0, r_len, method.getResponseCharSet()));
//				}
//			} else {
//				System.err.println("Response Code: " + statusCode);
//			}
//		} catch (HttpException e) {
//			System.err.println("Fatal protocol violation: " + e.getMessage());
//		} catch (IOException e) {
//			System.err.println("Fatal transport error: " + e.getMessage());
//		} finally {
//			method.releaseConnection();
//			if (ins != null) {
//				ins.close();
//			}
//		}
//		if (StringUtils.isNotBlank(sb.toString())) {
//			JSONObject json = JSONObject.fromObject(sb.toString());
//			if (json.getInt("status") == 0) {
//				Map<String, Object> result = (Map<String, Object>) json.get("result");
//				Map<String, Object> addressComponent = (Map<String, Object>) result.get("addressComponent");
//				locationCity = addressComponent.get("city").toString();
//			}
//		}
//		return locationCity;
//	}

//	/**
//	 * 获取最近店铺
//	 * 
//	 * @param response
//	 * @param lon
//	 * @param lat
//	 * @param cityname
//	 */
//	public Map<String, Object> getStroebyMap(Double lon, Double lat, String cityname) {
//		Map<String, Object> result = new HashMap<String, Object>();
//		return result;
//	}

}
