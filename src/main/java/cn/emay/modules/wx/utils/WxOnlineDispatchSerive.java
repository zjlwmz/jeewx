package cn.emay.modules.wx.utils;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import weixin.popular.api.MediaAPI;
import weixin.popular.api.MessageAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.media.Media;
import weixin.popular.bean.media.MediaType;
import weixin.popular.bean.message.message.ImageMessage;
import weixin.popular.bean.message.message.Message;
import weixin.popular.bean.message.message.TextMessage;
import cn.emay.framework.common.mapper.JsonMapper;
import cn.emay.framework.common.utils.StringUtils;
import cn.emay.modules.sys.entity.User;
import cn.emay.modules.sys.service.ParamsService;
import cn.emay.modules.sys.service.UserService;
import cn.emay.modules.wx.entity.WxChatLogs;
import cn.emay.modules.wx.entity.WxOnlineRecord;
import cn.emay.modules.wx.im.api.ImGroupMessageAPI;
import cn.emay.modules.wx.im.api.ImMessageAPI;
import cn.emay.modules.wx.im.bean.message.IImageMessage;
import cn.emay.modules.wx.im.bean.message.ITextMessage;
import cn.emay.modules.wx.im.request.ResMessage;
import cn.emay.modules.wx.im.request.ResMine;
import cn.emay.modules.wx.im.request.ResTo;
import cn.emay.modules.wx.im.support.MessageType;
import cn.emay.modules.wx.service.UserCacheService;
import cn.emay.modules.wx.service.WxCacheService;
import cn.emay.modules.wx.service.WxChatLogsService;
import cn.emay.modules.wx.service.WxOnlineRecordService;
import cn.emay.modules.wx.support.UserType;

/**
 * 在线客服 调度工具
 * 
 * @author lenovo
 * 
 */
@Service
public class WxOnlineDispatchSerive {

	/**
	 * 图片正则 [imageurl]
	 */
	private String imagePattrn = "img\\[([^\\\\s]+?)\\]";

	/**
	 * 微信缓存数据接口
	 */
	@Autowired
	private WxCacheService wxCacheService;

	/**
	 * 在线客服记录单service接口
	 */
	@Autowired
	private WxOnlineRecordService wxOnlineRecordService;

	/**
	 * 微信账号接口
	 */
	@Autowired
	private WxChatLogsService wxChatLogsService;

	/**
	 * 用户service接口
	 */
	@Autowired
	private UserService userService;

	/**
	 * 参数service接口
	 */
	@Autowired
	private ParamsService paramsService;

	/**
	 * 用户缓存service接口
	 */
	@Autowired
	private UserCacheService userCacheService;

	/**
	 * 添加一个等待中的在线客服通道
	 */
	public void addWxOnlineWay(String fromUserName) {
		WxOnlineRecord wxOnlineRecord = new WxOnlineRecord();
		wxOnlineRecord.setOpenid(fromUserName);
		wxOnlineRecord.setStartDate(new Date());
		wxOnlineRecord.setEndDate(wxOnlineRecord.getStartDate());
		wxOnlineRecord.setStatus(0);// 等待接入
		wxOnlineRecord.setReplyStatus(0);
		wxOnlineRecordService.createWxOnlineRecord(wxOnlineRecord);
		TextMessage sendTextMessage = new TextMessage(fromUserName, "请稍后...");
		String accessToken = wxCacheService.getToken();
		MessageAPI.messageCustomSend(accessToken, sendTextMessage);
	}

	/**
	 * 更新会话信息 发送微信客服消息
	 */
	public BaseResult sendMessage(ResMessage resMessage) {
		try {
			ResMine resMine = resMessage.getMine();
			ResTo resTo = resMessage.getTo();

			String toUserId = resTo.getId();// textMessage.getToUser();//发给谁
			String formUserId = resMine.getId();// textMessage.getFromUser();//谁发的

			String userType = userCacheService.findUserType(toUserId);

			// 发给客服人员
			if (userType.equals(UserType.cusUser)) {
				User formUser = userService.get(formUserId);

				String msgType = resMine.getMsgType();
				if (msgType.equals(MessageType.text)) {
					/**
					 * 文本消息发送
					 */
					ITextMessage itextMessage = new ITextMessage();
					itextMessage.setUsername(formUser.getRealName());
					itextMessage.setAvatar(formUser.getAvatar());
					itextMessage.setId(formUserId);
					itextMessage.setMsgType(MessageType.text);
					itextMessage.setContent(resMine.getContent());
					itextMessage.setMine(false);
					itextMessage.setTimestamp(System.currentTimeMillis());

					ImMessageAPI.messageCustomSend(itextMessage, toUserId);

					BaseResult baseResult = new BaseResult();
					baseResult.setErrcode("0");
					return baseResult;
				} else if (msgType.equals(MessageType.image)) {
					/**
					 * 文本消息发送
					 */
					IImageMessage itextMessage = new IImageMessage();
					itextMessage.setUsername(formUser.getRealName());
					itextMessage.setAvatar(formUser.getAvatar());
					itextMessage.setId(formUserId);
					itextMessage.setMsgType(MessageType.image);
					itextMessage.setContent(resMine.getContent());
					itextMessage.setMine(false);
					itextMessage.setTimestamp(System.currentTimeMillis());

					ImMessageAPI.messageCustomSend(itextMessage, toUserId);

					BaseResult baseResult = new BaseResult();
					baseResult.setErrcode("0");
					return baseResult;
				}

			}
			// 发给微信客服消息
			else if (userType.equals(UserType.weixin)) {
				WxOnlineRecord wxOnlineRecord = WxOnlineRecordCacheUtils.getWxOnlineRecord(toUserId);
				if (null != wxOnlineRecord) {

					String msgType = resMine.getMsgType();

					/**
					 * 保存对话
					 */
					String content = resMine.getContent();
					
					

					/**
					 * 发送微信客服信息
					 */
					if (msgType.equals(MessageType.text)) {
						
						WxChatLogs wxChatLogs = new WxChatLogs();
						wxChatLogs.setContent(content);
						wxChatLogs.setMessageType(msgType);
						wxChatLogs.setFromUserName(formUserId);
						wxChatLogs.setToUserName(toUserId);
						wxChatLogs.setCreateDate(new Date());
						wxChatLogs.setType("friend");
						wxChatLogs.setMine(true);

						wxChatLogs.setWxOnlineServiceId(wxOnlineRecord.getId());
						wxChatLogs.setToUserName(wxOnlineRecord.getUserId());// 对应客服对象

						wxChatLogsService.save(wxChatLogs);

						/**
						 * 更新在线客服记录单
						 */
						wxOnlineRecord.setReplyStatus(1);// 1等待提问【等待顾客回复】
						wxOnlineRecord.setEndDate(new Date());
						wxOnlineRecordService.save(wxOnlineRecord);
						WxOnlineRecordCacheUtils.putWxOnlineRecord(wxOnlineRecord);
						
						
						Pattern pattern = Pattern.compile(imagePattrn, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
						// 现在创建 matcher 对象
						Matcher matcher = pattern.matcher(content);
						while (matcher.find()) {
							/**
							 * 发现图片
							 */
							String value1 = matcher.group(0);
							String value2 = matcher.group(1);
							content = content.replace(value1, "<a href='" + value2 + "'>【图片】</a>");

						}
						String accessToken = wxCacheService.getToken();
						Message message = new TextMessage(toUserId, content);
						/**
						 * 发送客服消息
						 */
						BaseResult baseResult = MessageAPI.messageCustomSend(accessToken, message);

						/**
						 * 消息发送状态
						 */
						System.out.println(JsonMapper.toJsonString(baseResult));

						return baseResult;

					} else if (msgType.equals(MessageType.image)) {
						String accessToken = wxCacheService.getToken();

						Pattern pattern = Pattern.compile(imagePattrn, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
						// 现在创建 matcher 对象
						Matcher matcher = pattern.matcher(content);

						BaseResult baseResult = new BaseResult();
						baseResult.setErrcode("0");
						while (matcher.find()) {
							/**
							 * 发现图片
							 */
							String imageUrl = matcher.group(1);

							WxChatLogs wxChatLogs = new WxChatLogs();
							wxChatLogs.setContent(imageUrl);
							wxChatLogs.setMessageType(msgType);
							wxChatLogs.setFromUserName(formUserId);
							wxChatLogs.setToUserName(toUserId);
							wxChatLogs.setCreateDate(new Date());
							wxChatLogs.setType("friend");
							wxChatLogs.setMine(true);

							wxChatLogs.setWxOnlineServiceId(wxOnlineRecord.getId());
							wxChatLogs.setToUserName(wxOnlineRecord.getUserId());// 对应客服对象

							wxChatLogsService.save(wxChatLogs);

							/**
							 * 更新在线客服记录单
							 */
							wxOnlineRecord.setReplyStatus(1);// 1等待提问【等待顾客回复】
							wxOnlineRecord.setEndDate(new Date());
							wxOnlineRecordService.save(wxOnlineRecord);
							WxOnlineRecordCacheUtils.putWxOnlineRecord(wxOnlineRecord);
							
							
							
							URI uri = new URI(imageUrl);
							Media media = MediaAPI.mediaUpload(accessToken, MediaType.image, uri);
							if (null != media && StringUtils.isNotBlank(media.getMedia_id())) {
								Message message = new ImageMessage(toUserId, media.getMedia_id());
								/**
								 * 发送客服消息
								 */
								baseResult = MessageAPI.messageCustomSend(accessToken, message);
								/**
								 * 消息发送状态
								 */
								System.out.println(JsonMapper.toJsonString(baseResult));

							}

						}

						return baseResult;
					}

					// 创建 Pattern 对象

				}
			} else if (userType.equals(UserType.noUser)) {
				BaseResult baseResult = new BaseResult();
				baseResult.setErrcode("1");
				return baseResult;
			} else {
				BaseResult baseResult = new BaseResult();
				baseResult.setErrcode("1");
				return baseResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 发送群消息
	 * 
	 * @param resMessage
	 * @return
	 */
	public BaseResult sendGroupMessage(ResMessage resMessage) {
		try {
			String roleId = paramsService.findParamsByName("wx.role.id");
			ResMine resMine = resMessage.getMine();
			ResTo resTo = resMessage.getTo();
			List<User> userList = userService.findUserByRole(roleId);
			for (User user : userList) {
				String userId = user.getId();
				if (!resMine.getId().equals(userId)) {

					/**
					 * 文本消息发送
					 * 
					 */
					ITextMessage itextMessage = new ITextMessage();
					itextMessage.setUsername(resMine.getUsername());
					itextMessage.setName(resTo.getName());
					itextMessage.setAvatar(resMine.getAvatar());
					// itextMessage.setId(resMine.getId());
					// 群组和好友直接聊天不同，群组的id 是 组id，否则是发送人的id
					itextMessage.setId(resTo.getId());
					itextMessage.setMsgType(MessageType.text);
					itextMessage.setContent(resMine.getContent());
					itextMessage.setMine(false);
					itextMessage.setTimestamp(System.currentTimeMillis());

					ImGroupMessageAPI.messageCustomSend(itextMessage, userId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
