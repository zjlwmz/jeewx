package cn.emay.modules.sys.repository.impl;

import org.springframework.stereotype.Repository;

import cn.emay.framework.core.common.dao.impl.GenericBaseCommonDao;
import cn.emay.modules.sys.entity.ScheduleJob;
import cn.emay.modules.sys.repository.ScheduleJobDao;

/**
 * 定时任务DAO接口实现
 * @author lenovo
 *
 */
@Repository
public class ScheduleJobDaoImpl extends GenericBaseCommonDao<ScheduleJob, String>  implements ScheduleJobDao {
	

	
}
