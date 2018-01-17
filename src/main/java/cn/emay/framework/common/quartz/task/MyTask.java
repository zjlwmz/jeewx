package cn.emay.framework.common.quartz.task;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 我的第一个任务调度
 * @author lenovo
 *
 */
public class MyTask implements Job{
	
	private static final Logger logger = LoggerFactory.getLogger(MyTask.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// 此任务仅打印日志便于调试、观察
        logger.debug(this.getClass().getName() + " trigger...");
		int m=0;
		for(int i=0;i<100000;i++){
			m+=i;
		}
		System.out.println(m);
		System.out.println("任务完成----"+System.currentTimeMillis());
	}

}
