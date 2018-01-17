package cn.emay.modules.chat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.chat.entity.ChatGroup;
import cn.emay.modules.chat.repository.ChatGroupDao;
import cn.emay.modules.chat.service.ChatGroupService;


/**
 * 聊天分组servie接口实现
 * @author lenovo
 *
 */
@Service
@Transactional(readOnly=true)
public class ChatGroupServiceImpl extends CommonServiceImpl implements ChatGroupService {

	/**
	 * 聊天分组DAO接口
	 */
	@Autowired
	private ChatGroupDao chatGroupDao;
	
	@Override
	public ChatGroup get(String id) {
		return chatGroupDao.get(ChatGroup.class, id);
	}

	@Override
	public ChatGroup getDefaultChatGroup() {
		return null;
	}

}
