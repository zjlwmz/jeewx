package cn.emay.modules.chat.repository.impl;

import org.springframework.stereotype.Repository;

import cn.emay.framework.core.common.dao.impl.GenericBaseCommonDao;
import cn.emay.modules.chat.entity.ChatGroup;
import cn.emay.modules.chat.repository.ChatGroupDao;


/**
 * 分组DAO接口实现
 * @author lenovo
 *
 */
@Repository
public class ChatGroupDaoImpl extends GenericBaseCommonDao<ChatGroup, String> implements ChatGroupDao{

}
