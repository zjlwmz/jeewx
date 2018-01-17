package cn.emay.modules.chat.repository.impl;

import org.springframework.stereotype.Repository;

import cn.emay.framework.core.common.dao.impl.GenericBaseCommonDao;
import cn.emay.modules.chat.entity.ChatFriend;
import cn.emay.modules.chat.repository.ChatFriendDao;

/**
 * 聊天好友DAO接口实现
 * @author lenovo
 *
 */
@Repository
public class ChatFriendDaoImpl extends GenericBaseCommonDao<ChatFriend, String> implements ChatFriendDao{

}
