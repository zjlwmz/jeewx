package cn.emay.modules.wx.response.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import weixin.popular.api.MessageAPI;
import weixin.popular.api.UserAPI;
import weixin.popular.bean.message.EventMessage;
import weixin.popular.bean.message.message.Message;
import weixin.popular.bean.message.message.TextMessage;
import weixin.popular.bean.user.User;
import cn.emay.framework.common.utils.StringUtils;
import cn.emay.modules.sys.service.ParamsService;
import cn.emay.modules.wx.entity.NewsItem;
import cn.emay.modules.wx.entity.Subscribe;
import cn.emay.modules.wx.entity.TextTemplate;
import cn.emay.modules.wx.entity.WxFans;
import cn.emay.modules.wx.entity.WxWechat;
import cn.emay.modules.wx.response.ResponseService;
import cn.emay.modules.wx.service.NewsItemService;
import cn.emay.modules.wx.service.SubscribeService;
import cn.emay.modules.wx.service.TextTemplateService;
import cn.emay.modules.wx.service.WxCacheService;
import cn.emay.modules.wx.service.WxFansService;
import cn.emay.modules.wx.utils.MessageUtil;

/**
 * 针对订阅消息处理接口
 * @author lenovo
 *
 */
@Component
public class DoDingYueEventResponseService extends BaseResponseService implements ResponseService{

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
	 * 关注提示语接口
	 */
	@Autowired
	private SubscribeService subscribeService;
	
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
	 * 参数service接口
	 */
	@Autowired
	private ParamsService paramsService;
	
	
	@Override
	public void execute(EventMessage eventMessage) {
		try{
			// 发送方帐号（open_id）
			String fromUserName = eventMessage.getFromUserName();
			WxWechat wxWechat=wxCacheService.getWxWechat();
			String token=wxCacheService.getToken();
			// 关注时，先把会员写入数据库，然后回复欢迎消息,要考虑 重新关注的情况 ；
			WxFans wxFans=wxFansService.findWxFansByOpenid(fromUserName);
			if(wxFans==null){
				wxFans = new WxFans();
			}
			wxFans.setIsFollow(true);
			wxFans.setOpenid(fromUserName);
			wxFans.setFollowtime(new Timestamp(System.currentTimeMillis()));
			User user=UserAPI.userInfo(token, fromUserName);
			if(null!=user){
				if(StringUtils.isBlank(user.getErrcode())){
					wxFans.setHeadimgurl(user.getHeadimgurl());
					wxFans.setNickname(user.getNickname());
					wxFans.setSex(user.getSex());
					wxFans.setCity(user.getCity());
					wxFans.setCountry(user.getCountry());
					wxFans.setProvince(user.getProvince());
					wxFans.setLanguage(user.getLanguage());
					wxFans.setRemark(user.getRemark());
					wxFans.setGroupid(user.getGroupid());
				}
			}
			
			wxFansService.save(wxFans);
			
			Subscribe subscribe = subscribeService.findSubscribeByWechatId(wxWechat.getId());
			if (null!=subscribe) {
				String type = subscribe.getMsgType();
				/**
				 * 【针对被动回复消息----文本消息】
				 */
				if (MessageUtil.REQ_MESSAGE_TYPE_TEXT.equals(type)) {
					TextTemplate textTemplate =textTemplateService.get(subscribe.getTemplateId());
					String content = textTemplate.getContent();
					TextMessage sendTextMessage=new TextMessage(fromUserName, content);
					sendTextMessage.getText().setContent(getMainMenu());
					String accessToken = wxCacheService.getToken();
					MessageAPI.messageCustomSend(accessToken, sendTextMessage);
				}
				/**
				 * 【针对被动回复消息----图文消息】
				 */
				else if (MessageUtil.RESP_MESSAGE_TYPE_NEWS.equals(type)) {
					String domain=paramsService.findParamsByName("sys.domain");
					List<NewsItem> newsList =newsItemService.findNewsItemByNewsTemplate(subscribe.getTemplateId());
					List<weixin.popular.bean.message.message.NewsMessage.Article> articles=new ArrayList<weixin.popular.bean.message.message.NewsMessage.Article>();
					for(NewsItem news : newsList){
						String url = "";
						if (StringUtils.isEmpty(news.getUrl())) {
							url = domain + "/newsItemController.do?newscontent&id=" + news.getId();
						} else {
							url = news.getUrl();
						}
						weixin.popular.bean.message.message.NewsMessage.Article article = new weixin.popular.bean.message.message.NewsMessage.Article(news.getTitle(), news.getDescription(), url, domain+ "/" + news.getImagePath());
						articles.add(article);
					}
					
					/**
					 * 需要推送的客服消息
					 */
					Message message=new weixin.popular.bean.message.message.NewsMessage(fromUserName,articles);
					String accessToken = wxCacheService.getToken();
					MessageAPI.messageCustomSend(accessToken, message);
					
				}
			}else{
				TextMessage sendTextMessage=new TextMessage(fromUserName, "谢谢您的关注！");
				String accessToken = wxCacheService.getToken();
				MessageAPI.messageCustomSend(accessToken, sendTextMessage);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
