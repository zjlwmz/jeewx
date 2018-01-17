package cn.emay.modules.wx.response.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import weixin.popular.bean.message.EventMessage;
import cn.emay.modules.wx.entity.FunctionLog;
import cn.emay.modules.wx.response.ResponseService;
import cn.emay.modules.wx.service.FunctionLogService;

/**
 * 针对网页跳转消息处理接口
 * @author lenovo
 *
 */
@Component
public class DoMyViewEventService  extends BaseResponseService implements ResponseService{

	/**
	 * 功能日志接口
	 */
	@Autowired
	private FunctionLogService functionLogService;
	
	@Override
	public void execute(EventMessage eventMessage) {
		String eventKey = eventMessage.getEventKey();
		/**
		 * 微官网
		 */
		if ("http://www.5dgz.com/wap".equals(eventKey)) {
			try {
				FunctionLog functionLog = new FunctionLog();
				functionLog.setFunctionName("微官网");
				functionLog.setEventKey(eventKey);
				functionLog.setOpenid(eventMessage.getFromUserName());
				functionLog.setCredateDate(new Date());
				functionLogService.save(functionLog);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
