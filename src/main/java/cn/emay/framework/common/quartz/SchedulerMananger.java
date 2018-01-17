/**
 * 
 */
package cn.emay.framework.common.quartz;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import cn.emay.framework.common.utils.StringUtils;
import cn.emay.modules.sys.entity.ScheduleJob;
import cn.emay.modules.sys.service.ScheduleJobService;

/**
 * 任务管理
 * 
 * @author Zjlwm
 * @version 2014-06-04
 * 
 */
public class SchedulerMananger {

	/**
	 * 定时任务service接口
	 */
	@Autowired
	public ScheduleJobService scheduleJobService;

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	public void initTask() throws SchedulerException {
		// 这里获取任务信息数据
		List<ScheduleJob> jobList = scheduleJobService.findOpenTaskSchedule();//查询开启任务列表
		for (ScheduleJob job : jobList) {
			this.openTaskSchedule(job);
		}
	}

	/**
	 * 创建任务
	 * 
	 * @throws SchedulerException
	 */
	@SuppressWarnings("unchecked")
	public void openTaskSchedule(ScheduleJob job) throws SchedulerException {
		// schedulerFactoryBean 由spring创建注入
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		// 这里获取任务信息数据
		TriggerKey triggerKey = TriggerKey.triggerKey(job.getName(), job.getGroup());
		// 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
		// 不存在，创建一个
		if (null == trigger) {
			Class<Job> jobClass = null;
			try {
				jobClass = (Class<Job>) Class.forName(job.getJobClass());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			if (null == jobClass) {
				return;
			}
			
			// 具体任务
			JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(job.getName(), job.getGroup()).build();
			jobDetail.getJobDataMap().put("scheduleJob", job);
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCron());

			// 按新的cronExpression表达式构建一个新的trigger
			trigger = TriggerBuilder.newTrigger().withIdentity(job.getName(), job.getGroup()).withSchedule(scheduleBuilder).build();
			
			// 交由Scheduler安排触发
			scheduler.scheduleJob(jobDetail, trigger);

		} else {
			// Trigger已存在，那么更新相应的定时设置
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCron());

			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		}
	}

	/**
	 * 计划中的任务列表
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	public List<ScheduleJob> scheduledTasks() throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
		Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
		List<ScheduleJob> jobList = new ArrayList<ScheduleJob>();
		for (JobKey jobKey : jobKeys) {
			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
			for (Trigger trigger : triggers) {
				ScheduleJob job = new ScheduleJob();
				ScheduleJob dbjob = scheduleJobService.findScheduleJobByNameAdndGroup(jobKey.getName(), jobKey.getGroup());
				if (null != dbjob && StringUtils.isNotEmpty(dbjob.getId())) {
					job.setId(dbjob.getId());
				}
				job.setName(jobKey.getName());
				job.setGroup(jobKey.getGroup());
				job.setDesc("触发器:" + trigger.getKey());
				Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
				job.setState(triggerState.name());
				if (trigger instanceof CronTrigger) {
					CronTrigger cronTrigger = (CronTrigger) trigger;
					String cronExpression = cronTrigger.getCronExpression();
					job.setCron(cronExpression);
				}
				jobList.add(job);
			}
		}
		return jobList;
	}

	/**
	 * 运行中的任务列表
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	public List<ScheduleJob> runningTask() throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
		List<ScheduleJob> jobList = new ArrayList<ScheduleJob>(executingJobs.size());
		for (JobExecutionContext executingJob : executingJobs) {
			ScheduleJob job = new ScheduleJob();
			JobDetail jobDetail = executingJob.getJobDetail();
			JobKey jobKey = jobDetail.getKey();
			Trigger trigger = executingJob.getTrigger();
			ScheduleJob dbjob = scheduleJobService.findScheduleJobByNameAdndGroup(jobKey.getName(), jobKey.getGroup());
			if (null != dbjob && StringUtils.isNotEmpty(dbjob.getId())) {
				job.setId(dbjob.getId());
			}
			job.setName(jobKey.getName());
			job.setGroup(jobKey.getGroup());
			job.setDesc("触发器:" + trigger.getKey());
			Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
			job.setState(triggerState.name());
			if (trigger instanceof CronTrigger) {
				CronTrigger cronTrigger = (CronTrigger) trigger;
				String cronExpression = cronTrigger.getCronExpression();
				job.setCron(cronExpression);
			}
			jobList.add(job);
		}

		return jobList;
	}

	
	/**
	 * 获取一个任务的运行状态
	 * @throws SchedulerException 
	 */
	public ScheduleJob getScheduleJob(ScheduleJob scheduleJob) throws SchedulerException{
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey =new TriggerKey(scheduleJob.getName(), scheduleJob.getGroup());
		Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
		if(null!=triggerState){
			scheduleJob.setState(triggerState.name());
		}
		return scheduleJob;
	}
	
	
	
	
	/**
	 * 暂停任务
	 * 
	 * @throws SchedulerException
	 */
	public void suspendTask(ScheduleJob scheduleJob) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(scheduleJob.getName(), scheduleJob.getGroup());
		scheduler.pauseJob(jobKey);
	}

	/**
	 * 恢复任务
	 * 
	 * @throws SchedulerException
	 */
	public void recoveryTask(ScheduleJob scheduleJob) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(scheduleJob.getName(), scheduleJob.getGroup());
		scheduler.resumeJob(jobKey);
	}

	/**
	 * 任务删除 </br> &nbsp;&nbsp;删除任务后，所对应的trigger也将被删除
	 * 
	 * @throws SchedulerException
	 */
	public void deleTask(ScheduleJob scheduleJob) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(scheduleJob.getName(), scheduleJob.getGroup());
		scheduler.deleteJob(jobKey);
	}

	/**
	 * 立即运行任务</br>
	 * &nbsp;&nbsp;这里的立即运行，只会运行一次，方便测试时用。quartz是通过临时生成一个trigger的方式来实现的
	 * ，这个trigger将在本次任务运行完成之后自动删除。trigger的key是随机生成的
	 * 
	 * @throws SchedulerException
	 */
	public void runthetask(ScheduleJob scheduleJob) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(scheduleJob.getName(), scheduleJob.getGroup());
		scheduler.triggerJob(jobKey);
	}

	/**
	 * 更新任务的时间表达式
	 * 
	 * @throws SchedulerException
	 */
	public void updateCron(ScheduleJob scheduleJob) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();

		TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getName(), scheduleJob.getGroup());

		// 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

		// 表达式调度构建器
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCron());

		// 按新的cronExpression表达式重新构建trigger
		trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

		// 按新的trigger重新设置job执行
		scheduler.rescheduleJob(triggerKey, trigger);
	}
}

/**
 * http://www.meiriyouke.net/?p=140 trigger各状态说明：
 * 
 * None：Trigger已经完成，且不会在执行，或者找不到该触发器，或者Trigger已经被删除 NORMAL:正常状态 PAUSED：暂停状态
 * COMPLETE：触发器完成，但是任务可能还正在执行中 BLOCKED：线程阻塞状态 ERROR：出现错误
 */
