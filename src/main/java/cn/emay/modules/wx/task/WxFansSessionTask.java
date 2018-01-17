package cn.emay.modules.wx.task;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.emay.framework.common.utils.SpringContextHolder;
import cn.emay.modules.wx.entity.WxFansSession;
import cn.emay.modules.wx.service.WxFansSessionService;

/**
 * 微信粉丝剩余会话时间任务
 * 
 * @author lenovo
 */
public class WxFansSessionTask implements Job {

	/**
	 * 
	 * 微信会话service接口
	 * 
	 */
	private WxFansSessionService wxFansSessionService = SpringContextHolder.getBean(WxFansSessionService.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			/**
			 * 动态刷新过去48小时内的会话
			 */
			List<WxFansSession> wxFansSessionList = wxFansSessionService.findThePast48Hourse();
			for (WxFansSession wxFansSession : wxFansSessionList) {
				wxFansSessionService.updateWxFansSessionSurplusDate(wxFansSession);
			}

			/**
			 * 过去48小时之前的数据进行处理
			 */
			List<WxFansSession> wxFansSessionAgoList = wxFansSessionService.find48HourseAgo();
			for (WxFansSession wxFansSession : wxFansSessionAgoList) {
				wxFansSessionService.updateWxFansSessionSurplusDate(wxFansSession);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
