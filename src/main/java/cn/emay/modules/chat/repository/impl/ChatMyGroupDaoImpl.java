package cn.emay.modules.chat.repository.impl;

import org.springframework.stereotype.Repository;

import cn.emay.framework.core.common.dao.impl.GenericBaseCommonDao;
import cn.emay.modules.chat.entity.ChatMyGroup;
import cn.emay.modules.chat.repository.ChatMyGroupDao;


/**
 * 
 * 我的分组DAO接口实现
 * @author lenovo
 *
 */
@Repository
public class ChatMyGroupDaoImpl extends GenericBaseCommonDao<ChatMyGroup, String> implements ChatMyGroupDao{

}
