package cn.emay.modules.wx.response.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import weixin.popular.api.MessageAPI;
import weixin.popular.bean.message.EventMessage;
import weixin.popular.bean.message.message.Message;
import weixin.popular.bean.message.message.TextMessage;
import cn.emay.framework.common.utils.StringUtils;
import cn.emay.modules.jms.service.ConsumerProducerService;
import cn.emay.modules.sys.service.ParamsService;
import cn.emay.modules.wx.entity.FunctionLog;
import cn.emay.modules.wx.entity.MenuEntity;
import cn.emay.modules.wx.entity.NewsItem;
import cn.emay.modules.wx.entity.TextTemplate;
import cn.emay.modules.wx.entity.WxFans;
import cn.emay.modules.wx.response.ResponseService;
import cn.emay.modules.wx.service.FunctionLogService;
import cn.emay.modules.wx.service.MenuEntityService;
import cn.emay.modules.wx.service.NewsItemService;
import cn.emay.modules.wx.service.TextTemplateService;
import cn.emay.modules.wx.service.WxCacheService;
import cn.emay.modules.wx.service.WxFansService;
import cn.emay.modules.wx.utils.MessageUtil;
import cn.emay.modules.wx.utils.WxOnlineRecordCacheUtils;
import cn.emay.modules.wx.utils.WxOnlineDispatchSerive;

/**
 * 针对自定义菜单点击事件处理接口
 * @author lenovo
 *
 */
@Component
public class DoMyMenuEventService extends BaseResponseService implements ResponseService{

	
	/**
	 * 微信缓存数据接口
	 */
	@Autowired
	private WxCacheService wxCacheService;
	
	/**
	 * 微信粉丝接口
	 */
	@Autowired
	private WxFansService wxFansService;
	
	
	/**
	 * 文本消息模板接口
	 */
	@Autowired
	private TextTemplateService textTemplateService;
	
	
	
	/**
	 * 图文消息模板接口
	 */
	@Autowired
	private NewsItemService newsItemService;
	
	
	/**
	 * 微信菜单接口
	 */
	@Autowired
	private MenuEntityService menuEntityService;
	
	
	
	/**
	 * 功能日志service接口
	 */
	@Autowired
	private FunctionLogService functionLogService;
	
	
	/**
	 * 在线客服 调度工具
	 */
	@Autowired
	private WxOnlineDispatchSerive wxOnlineDispatchSerive; 
	
	
	/**
	 * 微信客服消息service接口
	 */
	@Autowired
	private ConsumerProducerService consumerProducerService;
	
	
	
	
	/**
	 * 微信客服消息队列目标
	 */
	@Autowired
	@Qualifier("weixinResponseQueue")
	private Destination weixinResponseQueue;
	
	
	
	
	
	
	/**
	 * 参数service接口
	 */
	@Autowired
	private ParamsService paramsService;
	
	
	@Override
	public void execute(EventMessage eventMessage) {
		try{
			/**
			 * 事件KEY值、设置的跳转URL
			 */
			final String eventKey = eventMessage.getEventKey();
			// 发送方帐号（open_id）
			final String fromUserName = eventMessage.getFromUserName();
			
			// 自定义菜单CLICK类型
			final MenuEntity menuEntity =menuEntityService.findMenuEntityByMenuKey(eventKey);
			if (menuEntity != null && StringUtils.isNotEmpty(menuEntity.getTemplateId())) {
				String type = menuEntity.getMsgType();
				/**
				 * 确定点击菜单返回消息类型--文本类型
				 */
				if (MessageUtil.REQ_MESSAGE_TYPE_TEXT.equals(type)) {
					TextTemplate textTemplate =textTemplateService.get(menuEntity.getTemplateId());
					String content = textTemplate.getContent();
					WxFans wxFans=wxFansService.findWxFansByOpenid(fromUserName);
					//假如已经关注的粉丝、数据库里面没有存在、则重新保存
					if(null==wxFans){
						wxFans =new WxFans();
						wxFans.setIsFollow(true);
						wxFans.setOpenid(fromUserName);
						wxFans.setFollowtime(new Timestamp(System.currentTimeMillis()));
						wxFansService.save(wxFans);
					}
					
					/**
					 * 菜单名称
					 */
					String menuName = menuEntity.getName();

					//开始会话
					if(textTemplate.getTemplateName().equals("开始会话")){
						/**
						 * 是否在在线对话中
						 */
						if(null!=WxOnlineRecordCacheUtils.getWxOnlineRecord(fromUserName)){
							TextMessage sendTextMessage=new TextMessage(fromUserName, "您好，请问有什么需要为您服务的！");
							String accessToken = wxCacheService.getToken();
							MessageAPI.messageCustomSend(accessToken, sendTextMessage);
						}else{
							/**
							 * 建立一个在线客服客服会话
							 */
							wxOnlineDispatchSerive.addWxOnlineWay(fromUserName);
							
							consumerProducerService.sendMessage(weixinResponseQueue, "一个微信用户来了："+fromUserName);
						}
					}else{
						TextMessage sendTextMessage=new TextMessage(fromUserName, content);
						String accessToken = wxCacheService.getToken();
						MessageAPI.messageCustomSend(accessToken, sendTextMessage);
					}
					/**
					 * 日志记录
					 */
					FunctionLog functionLog = new FunctionLog();
					functionLog.setFunctionName(menuName);
					functionLog.setEventKey(eventKey);
					functionLog.setOpenid(fromUserName);
					functionLog.setCredateDate(new Date());
					functionLogService.save(functionLog);
					
					
					
					
				} 
				//微信菜单点击-返回图文消息
				else if (MessageUtil.RESP_MESSAGE_TYPE_NEWS.equals(type)) {
					List<NewsItem> newsList =newsItemService.findNewsItemByNewsTemplate(menuEntity.getTemplateId());
					String domain=paramsService.findParamsByName("sys.domain");
					List<weixin.popular.bean.message.message.NewsMessage.Article> articles=new ArrayList<weixin.popular.bean.message.message.NewsMessage.Article>();
					for(NewsItem news : newsList){
						String url = "";
						if (StringUtils.isEmpty(news.getUrl())) {
							url = domain + "/newsItemController.do?newscontent&id=" + news.getId();
						} else {
							url = news.getUrl();
						}
						weixin.popular.bean.message.message.NewsMessage.Article article = new weixin.popular.bean.message.message.NewsMessage.Article(news.getTitle(), news.getDescription(), url, domain + "/" + news.getImagePath());
						articles.add(article);
					}
					
					/**
					 * 需要推送的客服消息
					 */
					Message message=new weixin.popular.bean.message.message.NewsMessage(fromUserName,articles);
					String accessToken = wxCacheService.getToken();
					MessageAPI.messageCustomSend(accessToken, message);
					
					
				} 
				
				//微信菜单点击返回消息--扩展类型
				else if ("expand".equals(type)) {
					/*
					WeixinExpandconfigEntity expandconfigEntity = weixinExpandconfigService.getEntity(WeixinExpandconfigEntity.class, menuEntity.getTemplateId());
					String className = expandconfigEntity.getClassname();
					KeyServiceI keyService = (KeyServiceI) Class.forName(className).newInstance();
					respMessage = keyService.excute("", textMessage, request);
					*/
					
				} 
				//模板消息
				else if (MessageUtil.RESP_MESSAGE_TYPE_TEMPLATE.equals(type)) {

					// 如果该用户未在微信用户表中，则补录进去；
					// SaveWeixinUser(fromUserName);
					// 判断用户有没有绑定会员卡号 ，没绑定则提示文本消息 。绑定了则推送模板消息
					WxFans wxFans=wxFansService.findWxFansByOpenid(fromUserName);
					doTemplateMessageResponse(menuEntity.getTemplateId(), wxFans);
				}
			}else{
				TextMessage sendTextMessage=new TextMessage(fromUserName, "抱歉！没有找到对应的数据，请联系管理员！");
				String accessToken = wxCacheService.getToken();
				MessageAPI.messageCustomSend(accessToken, sendTextMessage);
			}
		}catch (Exception e) {
			
		}
	}
	
	
	/**
	 * 模板消息回复
	 */
	public void doTemplateMessageResponse(String templatemessageId,WxFans wxFans) {
//		Templatemessage templatemessage=templatemessageService.get(templatemessageId);
//		System.out.println(templatemessage);
	}

}
