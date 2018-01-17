/**
 * 
 */
package cn.emay.framework.common.quartz;

import java.util.HashMap;
import java.util.Map;

import cn.emay.modules.sys.entity.ScheduleJob;
import cn.emay.modules.sys.support.SchedulerJobStatus;

/**
 * @author Administrator
 *
 */
public class QuartzTest {
	private static Map<String, ScheduleJob> jobMap = new HashMap<String, ScheduleJob>();
	static {
		for (int i = 0; i < 5; i++) {
			ScheduleJob job = new ScheduleJob();
			job.setId(i+"");
			job.setName("data_import" + i);
			job.setGroup("dataWork");
			job.setStatus(SchedulerJobStatus.open);
			job.setCron("0/5 * * * * ?");
			job.setDesc("数据导入任务");
			addJob(job);
		}
	}
	/**
	 * 添加任务
	 * @param scheduleJob
	 */
	public static void addJob(ScheduleJob scheduleJob) {
		jobMap.put(scheduleJob.getGroup() + "_" + scheduleJob.getName(), scheduleJob);
	}
}
