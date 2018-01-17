package cn.emay.modules.chat.service;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.chat.entity.ChatGroup;

/**
 * 聊天分组service接口
 * @author lenovo
 *
 */
public interface ChatGroupService extends CommonService{

	
	/**
	 * 获取聊天分组
	 * @param id
	 * @return
	 */
	public ChatGroup get(String id);
	
	/**
	 * 获取一个默认聊天分组
	 * @return
	 */
	public ChatGroup getDefaultChatGroup();
}
