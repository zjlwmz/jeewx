package cn.emay.modules.chat.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.chat.entity.ChatFriend;
import cn.emay.modules.chat.repository.ChatFriendDao;
import cn.emay.modules.chat.service.ChatFriendService;

/**
 * 聊天好友service接口实现
 * @author lenovo
 *
 */
@Transactional(readOnly=true)
@Service
public class ChatFriendServiceImpl extends CommonServiceImpl implements ChatFriendService{

	/**
	 * 聊天好友DAO接口
	 *
	 */
	@Autowired
	private ChatFriendDao chatFriendDao;
	
	
	/**
	 * 保存好友关系
	 */
	@Transactional(readOnly=false)
	@Override
	public void save(ChatFriend chatFriend) {
		boolean isChatFriend=isChatFriend(chatFriend.getOpenid(),chatFriend.getUserid());
		if(!isChatFriend){
			chatFriendDao.save(chatFriend);
		}
	}
	

	
	@Override
	public boolean isChatFriend(String openid, String userid) {
		Long count=chatFriendDao.getCountForJdbc("select count(*) from chat_friend where 1=1 and ( openid='"+openid+"' and userid='"+userid+"' )");
		if(count>0){
			return true;
		}
		return false;
	}



	@Override
	public List<ChatFriend> findChatFriendByGroupIdAndUserId(String groupid, String userid) {
		List<ChatFriend>chatFriendList=chatFriendDao.findByQueryString("from ChatFriend where groupId='"+groupid+"' and (openid='"+userid+"' or userid='"+userid+"')");
		return chatFriendList;
	}
	
	

}
