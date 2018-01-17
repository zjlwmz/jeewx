package cn.emay.modules.wx.task;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.websocket.Session;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import weixin.popular.api.MessageAPI;
import weixin.popular.bean.BaseResult;
import weixin.popular.bean.message.message.TextMessage;
import cn.emay.framework.common.utils.SpringContextHolder;
import cn.emay.modules.chat.entity.ChatFriend;
import cn.emay.modules.chat.entity.ChatMyGroup;
import cn.emay.modules.chat.service.ChatFriendService;
import cn.emay.modules.chat.service.ChatMyGroupService;
import cn.emay.modules.sys.entity.User;
import cn.emay.modules.sys.manager.ClientManager;
import cn.emay.modules.sys.service.UserService;
import cn.emay.modules.wx.entity.WxFans;
import cn.emay.modules.wx.entity.WxOnlineRecord;
import cn.emay.modules.wx.im.api.ImMessageAPI;
import cn.emay.modules.wx.im.bean.message.ITextMessage;
import cn.emay.modules.wx.im.support.MessageType;
import cn.emay.modules.wx.service.WxCacheService;
import cn.emay.modules.wx.service.WxFansService;
import cn.emay.modules.wx.service.WxOnlineRecordService;
import cn.emay.modules.wx.support.WxOnlineRecordStatus;
import cn.emay.modules.wx.utils.SessionUtils;
import cn.emay.modules.wx.utils.WxOnlineRecordCacheUtils;

/**
 * 【在线客服对话】 在线客服记录单 任务
 * 
 * @author lenovo
 * 
 */
public class OnlineRecordTask implements Job {

	/**
	 * 在线客服记录单接口服务
	 */
	private WxOnlineRecordService wxOnlineRecordService = SpringContextHolder.getBean(WxOnlineRecordService.class);

	/**
	 * 微信缓存接口
	 */
	private WxCacheService wxCacheService = SpringContextHolder.getBean(WxCacheService.class);

	/**
	 * 微信粉丝service接口
	 */
	private WxFansService wxFansService = SpringContextHolder.getBean(WxFansService.class);

	/**
	 * 我的分组service接口
	 */
	private ChatMyGroupService chatMyGroupService = SpringContextHolder.getBean(ChatMyGroupService.class);

	/**
	 * 
	 * 聊天好友service接口
	 * 
	 */
	private ChatFriendService chatFriendService = SpringContextHolder.getBean(ChatFriendService.class);

	/**
	 * 用户service接口
	 */
	private UserService userService = SpringContextHolder.getBean(UserService.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		/**
		 * 对话列表【0排队等候;1开始对话】
		 */
		List<WxOnlineRecord> wxOnlineRecordList = wxOnlineRecordService.findNotFinishedWxOnlineRecord();

		/**
		 * 在线的客服人员队列
		 */
		List<String> clientsKeyList = SessionUtils.clientsKeyList;
		Map<String, Session> socketClientsMap = SessionUtils.clients;
		if (socketClientsMap.size() > 0) {
			/**
			 * 随机分配客服人员
			 */
			Random rand = new Random();
			for (WxOnlineRecord wxOnlineRecord : wxOnlineRecordList) {

				/**
				 * 对话状态 0排队等候;1开始对话;2:聊天结束
				 */
				int status = wxOnlineRecord.getStatus().intValue();

				// 判断当前是什么回复状态
				/**
				 * 答复状态 0等待回复【等待客服人员回复】 1等待提问【等待顾客回复】 2提醒顾客回复
				 */
				int replyStatus = wxOnlineRecord.getReplyStatus().intValue();

				/**
				 * 排队等候
				 */
				if (status == WxOnlineRecordStatus.QueueUp) {
					int num = rand.nextInt(socketClientsMap.size());
					String userid = clientsKeyList.get(num);
					Session session = SessionUtils.get(userid);

					/**
					 * 客服人员已经登录im聊天系统
					 */
					if (null != session) {
						wxOnlineRecord.setUserId(userid);
						wxOnlineRecord.setReplyStatus(WxOnlineRecordStatus.StartAConversation);// 开始对话
						wxOnlineRecordService.startWxOnlineRecord(wxOnlineRecord);

						/**
						 * 缓存在线对话
						 */
						WxOnlineRecordCacheUtils.putWxOnlineRecord(wxOnlineRecord);

						/**
						 * 推送客服消息 ---告知顾问您已接入到在线对话中
						 */
						User user = ClientManager.getInstance().getOnLineClientsByUserId(userid);
						if (null == user) {
							user = userService.get(userid);
							ClientManager.getInstance().addOnlineCustomerServiceClient(user);
						}
						TextMessage sendTextMessage = new TextMessage(wxOnlineRecord.getOpenid(), "在线客服开始接入！\n 客服" + user.getUpdateName() + "为您服务");
						String accessToken = wxCacheService.getToken();
						BaseResult baseResult = MessageAPI.messageCustomSend(accessToken, sendTextMessage);
						if (baseResult.isSuccess()) {
							/**
							 * 添加为客服人员为好友
							 */
							ChatMyGroup chatMyGroup = chatMyGroupService.getDefaultChatMyGroupByUserId(userid);
							String groupid = "001";
							if (null != chatMyGroup) {
								groupid = chatMyGroup.getId();
							}

							//判断是否已经是好友了
							boolean isFriend = chatFriendService.isChatFriend(wxOnlineRecord.getOpenid(), wxOnlineRecord.getUserId());
							if (!isFriend) {
								ChatFriend chatFriend = new ChatFriend();
								chatFriend.setCreateDate(new Date());
								chatFriend.setOpenid(wxOnlineRecord.getOpenid());
								chatFriend.setUserid(wxOnlineRecord.getUserId());
								chatFriend.setGroupId(groupid);
								chatFriendService.save(chatFriend);

								/**
								 * 推送给客服人员、加客服人员为好友
								 */
								WxFans wxFans = wxFansService.findWxFansByOpenid(wxOnlineRecord.getOpenid());

								try {
									/**
									 * Im 文本消息发送
									 */
									ITextMessage textMessage = new ITextMessage();
									textMessage.setUsername(wxFans.getNickname());
									textMessage.setAvatar(wxFans.getHeadimgurl());
									textMessage.setId(wxFans.getOpenid());
									textMessage.setMsgType(MessageType.text);
									textMessage.setTimestamp(System.currentTimeMillis());
									textMessage.setGroupid(groupid);
									ImMessageAPI.messageAddFriendSend(textMessage, wxOnlineRecord.getUserId());

								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}else{
							System.out.println("对话接入失败：原因 微信客服消息发送失败，请检查");
						}

					}
				}

				/**
				 * 对话中
				 */
				else if (status == 1) {
					Date current = new Date();
					Date endDate = wxOnlineRecord.getEndDate();
					// 间隔时间
					long IntervalTime = current.getTime() - endDate.getTime();
					// 如果间隔2分钟 小于5分钟
					if (IntervalTime > 2 * 60 * 1000 && IntervalTime < 5 * 60 * 1000) {
						if (replyStatus == 1) {
							wxOnlineRecord.setReplyStatus(2);
							wxOnlineRecordService.saveOrUpdate(wxOnlineRecord);

							TextMessage sendTextMessage = new TextMessage(wxOnlineRecord.getOpenid(), "您好，请问还有什么需要咨询的吗");
							String accessToken = wxCacheService.getToken();
							MessageAPI.messageCustomSend(accessToken, sendTextMessage);
						}
					}
					// 大于5分钟小于10分钟
					else if (IntervalTime > 5 * 60 * 1000 && IntervalTime < 10 * 60 * 1000) {
						if (wxOnlineRecord.getReplyStatus() == 2) {
							wxOnlineRecord.setStatus(2);
							wxOnlineRecord.setEndDate(new Date());
							wxOnlineRecordService.save(wxOnlineRecord);

							WxOnlineRecordCacheUtils.removeWxOnlineRecord(wxOnlineRecord.getOpenid());
							TextMessage sendTextMessage = new TextMessage(wxOnlineRecord.getOpenid(), "感谢你的咨询，本次对话结束");
							String accessToken = wxCacheService.getToken();
							MessageAPI.messageCustomSend(accessToken, sendTextMessage);
						}
					}

					/**
					 * 大于10分钟
					 */
					else if (IntervalTime > 10 * 60 * 1000) {
						wxOnlineRecord.setStatus(2);
						wxOnlineRecord.setEndDate(new Date());
						wxOnlineRecordService.save(wxOnlineRecord);
						WxOnlineRecordCacheUtils.removeWxOnlineRecord(wxOnlineRecord.getOpenid());
					}
				}

			}
		}

	}

}
