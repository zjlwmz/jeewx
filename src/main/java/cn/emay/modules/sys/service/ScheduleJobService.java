package cn.emay.modules.sys.service;

import java.util.List;

import org.quartz.SchedulerException;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.sys.entity.ScheduleJob;


/**
 * 定时任务service接口
 * @author lenovo
 *
 */
public interface ScheduleJobService extends CommonService{

	public ScheduleJob get(String scheduleJobId);
	
	List<ScheduleJob> findList();

	/**
	 * 查询开启中任务
	 * @param status
	 * @return
	 */
	public List<ScheduleJob>findOpenTaskSchedule();
	
	/**
	 * 根据名称、分组查询任务
	 * @param name
	 * @param group
	 * @return
	 */
	public ScheduleJob findScheduleJobByNameAdndGroup(String name, String group);

	/**
	 * 任务保存
	 * @param scheduleJob
	 */
	public void save(ScheduleJob scheduleJob);
	
	/**
	 * 任务关闭
	 * @param scheduleJob
	 */
	public void closeTask(ScheduleJob scheduleJob) throws SchedulerException ;
	
	/**
	 * 任务开始
	 * @param scheduleJob
	 */
	public void startTask(ScheduleJob scheduleJob) throws SchedulerException;
	
	
	/**
	 * 任务暂停
	 */
	
	
}
