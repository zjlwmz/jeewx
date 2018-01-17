package cn.emay.modules.sys.service.impl;

import java.util.Date;
import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.common.utils.StringUtils;
import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.sys.entity.ScheduleJob;
import cn.emay.modules.sys.repository.ScheduleJobDao;
import cn.emay.modules.sys.service.ScheduleJobService;
import cn.emay.modules.sys.support.SchedulerJobStatus;


/**
 * 定时任务service接口实现
 * @author lenovo
 *
 */
@Service
@Transactional(readOnly=true)
@Lazy(false)
public class ScheduleJobServiceImpl extends CommonServiceImpl implements ScheduleJobService {

	/**
	 * 定时任务DAO
	 */
	@Autowired
	private ScheduleJobDao scheduleJobDao;
	
	@Override
	public List<ScheduleJob> findList() {
		return scheduleJobDao.findHql("from ScheduleJob");
	}

	
	
	@Transactional
	public void save(ScheduleJob scheduleJob){
		if(StringUtils.isNotBlank(scheduleJob.getId())){
			scheduleJob.setUpdateDate(new Date());
			scheduleJobDao.updateEntitie(scheduleJob);
		}else{
			scheduleJob.setCreateDate(new Date());
			scheduleJob.setUpdateDate(scheduleJob.getCreateDate());
			scheduleJob.setStatus(SchedulerJobStatus.close);
			scheduleJobDao.save(scheduleJob);
		}
	}
	
	@Override
	public ScheduleJob findScheduleJobByNameAdndGroup(String name, String group) {
		List<ScheduleJob>scheduleJobList=scheduleJobDao.findHql("from ScheduleJob where name='"+name+"' and group='"+group+"'");
		if(scheduleJobList.size()>0){
			return scheduleJobList.get(0);
		}
		return null;
	}

	@Override
	public ScheduleJob get(String scheduleJobId) {
		return scheduleJobDao.get(ScheduleJob.class, scheduleJobId);
	}

	/**
	 * 任务关闭【0停止 1启用】
	 * 
	 */
	@Transactional
	@Override
	public void closeTask(ScheduleJob scheduleJob) throws SchedulerException {
		scheduleJob.setStatus(SchedulerJobStatus.close);
		scheduleJobDao.save(scheduleJob);
	}

	/**
	 * 开启任务（创建一个任务）【0停止 1启用】
	 * 
	 */
	@Transactional
	@Override
	public void startTask(ScheduleJob scheduleJob) throws SchedulerException {
		scheduleJob.setStatus(SchedulerJobStatus.open);
		scheduleJobDao.save(scheduleJob);
	}

	/**
	 * 查询开启的任务
	 */
	@Override
	public List<ScheduleJob> findOpenTaskSchedule() {
		return scheduleJobDao.findHql("from ScheduleJob where status="+SchedulerJobStatus.open);
	}

}
