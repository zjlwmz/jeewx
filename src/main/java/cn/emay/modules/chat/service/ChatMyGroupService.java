package cn.emay.modules.chat.service;

import java.util.List;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.chat.entity.ChatMyGroup;

/**
 * 我的分组service接口
 * @author lenovo
 *
 */
public interface ChatMyGroupService extends CommonService{

	public ChatMyGroup get(String id);
	
	public void save(ChatMyGroup chatMyGroup);
	
	/**
	 * 根据用户id查询聊天分组
	 * @param userId
	 * @return
	 */
	public List<ChatMyGroup> findChatMyGroupByUserId(String userId);
	
	
	
	/**
	 * 获取默认客服存储访客聊天分组
	 * @param userId
	 * @return
	 */
	public ChatMyGroup getDefaultChatMyGroupByUserId(String userId);
	
	
	/**
	 * 创建默认的几个聊天分组
	 */
	public void createDefaultChatMyGroupByUserId(String userId);
}
