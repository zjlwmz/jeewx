/**
 * 
 */
package cn.emay.framework.common.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.emay.modules.sys.entity.ScheduleJob;

//import com.thinkgem.jeesite.common.utils.SpringContextHolder;
//import com.thinkgem.jeesite.modules.shop.entity.ScheduleJob;

/**
 * 定时任务运行工厂类
 * 
 * User: liyd
 * Date: 14-1-3
 * Time: 上午10:11
 */
public class QuartzJobFactory implements Job{

//	private TaskMethod taskMethod=SpringContextHolder.getBean(TaskMethod.class);
	/* (non-Javadoc)
	 * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
	 */
	@Override
	public void execute(JobExecutionContext context)throws JobExecutionException {
		System.out.println("任务成功运行");
        ScheduleJob scheduleJob = (ScheduleJob)context.getMergedJobDataMap().get("scheduleJob");
        System.out.println("任务名称 = [" + scheduleJob.getName() + "]");
        
	}

}
