package cn.emay.modules.chat.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.common.utils.StringUtils;
import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.chat.entity.ChatMyGroup;
import cn.emay.modules.chat.repository.ChatMyGroupDao;
import cn.emay.modules.chat.service.ChatMyGroupService;
import cn.emay.modules.wx.support.MyGroupList;


/**
 * 我的聊天分组servie接口实现
 * @author lenovo
 *
 */
@Service
@Transactional(readOnly=true)
public class ChatMyGroupServiceImpl extends CommonServiceImpl implements ChatMyGroupService {

	/**
	 * 聊天分组DAO接口
	 */
	@Autowired
	private ChatMyGroupDao chatMyGroupDao;
	
	@Override
	public ChatMyGroup get(String id) {
		return chatMyGroupDao.get(ChatMyGroup.class, id);
	}

	@Transactional
	@Override
	public void save(ChatMyGroup chatMyGroup) {
		if(StringUtils.isNotBlank(chatMyGroup.getId())){
			 chatMyGroupDao.updateEntitie(chatMyGroup);
		}else{
			 chatMyGroupDao.save(chatMyGroup);
		}
	}

	@Override
	public List<ChatMyGroup> findChatMyGroupByUserId(String userId) {
		return chatMyGroupDao.findByQueryString("from ChatMyGroup where userId='"+userId+"'");
	}

	@Override
	public ChatMyGroup getDefaultChatMyGroupByUserId(String userId) {
		List<ChatMyGroup>chatMyGroupList=chatMyGroupDao.findByQueryString("from ChatMyGroup where groupId="+MyGroupList.CustomerServiceGroup+" and userId='"+userId+"'");
		if(chatMyGroupList.size()>0){
			return chatMyGroupList.get(0);
		}
		return null;
	}

	
	
	/**
	 * 创建默认的聊天分组
	 */
	@Transactional(readOnly=false)
	@Override
	public void createDefaultChatMyGroupByUserId(String userId) {
		List<ChatMyGroup>chatMyGroupList=findChatMyGroupByUserId(userId);
		if(chatMyGroupList.size()==0){
			ChatMyGroup chatMyGroup=new ChatMyGroup();
			chatMyGroup.setCreateDate(new Date());
			chatMyGroup.setGroupId(MyGroupList.CustomerServiceGroup);
			chatMyGroup.setName("我的客户");
			chatMyGroup.setUserId(userId);
			chatMyGroupDao.save(chatMyGroup);
		}
	}
	
	
}
