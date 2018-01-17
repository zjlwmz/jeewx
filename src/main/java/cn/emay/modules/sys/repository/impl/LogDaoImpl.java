package cn.emay.modules.sys.repository.impl;

import org.springframework.stereotype.Repository;

import cn.emay.framework.core.common.dao.impl.GenericBaseCommonDao;
import cn.emay.modules.sys.entity.Log;
import cn.emay.modules.sys.repository.LogDao;


/**
 * 日志DAO接口实现
 * @author lenovo
 *
 */
@Repository
public class LogDaoImpl extends GenericBaseCommonDao<Log, String>  implements LogDao{

}
