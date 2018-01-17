/**
 * 
 */
package cn.emay.framework.common.quartz;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * quartz 初始化方法
 * @author Zjlwm
 * @version 2014-05-31
 *
 */
public class QuartzInit implements ApplicationListener<ContextRefreshedEvent>{

	@Autowired
	private SchedulerMananger schedulerMananger;
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
//		 //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
		if(event.getApplicationContext().getParent() == null){//root application context 没有parent，他就是老大.
			System.out.println("===================spring初始完成了...===================");
			System.out.println("==================="+schedulerMananger+"===================");
			try {
				schedulerMananger.initTask();
				System.out.println("quartz 初始化任务完成......");
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
//			
	      }
	}

}
