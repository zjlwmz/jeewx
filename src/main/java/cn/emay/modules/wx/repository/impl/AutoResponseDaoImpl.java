package cn.emay.modules.wx.repository.impl;

import org.springframework.stereotype.Repository;

import cn.emay.framework.core.common.dao.impl.GenericBaseCommonDao;
import cn.emay.modules.wx.entity.AutoResponse;
import cn.emay.modules.wx.repository.AutoResponseDao;


/**
 * 关键字DAO接口实现
 * @author lenovo
 *
 */
@Repository
public class AutoResponseDaoImpl extends GenericBaseCommonDao<AutoResponse, String> implements AutoResponseDao{

}
