package cn.emay.modules.chat.service;

import java.util.List;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.chat.entity.ChatFriend;


/**
 * 聊天好友service接口
 * @author lenovo
 *
 */
public interface ChatFriendService extends CommonService{

	/**
	 * 添加为好友
	 * @param chatFriend
	 */
	public void save(ChatFriend chatFriend);
	
	/**
	 * 是否是好友
	 * @param chatFriend
	 * @return
	 */
	public boolean isChatFriend(String openid,String userid);
	
	
	/**
	 * 好友列表查询
	 * @param userid
	 * @return
	 */
	public List<ChatFriend>findChatFriendByGroupIdAndUserId(String groupid,String userid);
	
}
