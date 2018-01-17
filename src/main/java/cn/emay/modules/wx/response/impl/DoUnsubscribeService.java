package cn.emay.modules.wx.response.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import weixin.popular.bean.message.EventMessage;
import cn.emay.modules.wx.entity.WxFans;
import cn.emay.modules.wx.response.ResponseService;
import cn.emay.modules.wx.service.WxFansService;


/**
 * 针对取消关注事件处理接口实现
 * @author lenovo
 *
 */
@Component
public class DoUnsubscribeService extends BaseResponseService implements ResponseService {

	/**
	 * 微信粉丝Dao接口
	 */
	@Autowired
	private WxFansService wxFansService;
	
	@Override
	public void execute(EventMessage eventMessage) {
		String fromUserName = eventMessage.getFromUserName();
		// 标注订阅状态为取消订阅
		WxFans wxFans=wxFansService.findWxFansByOpenid(fromUserName);
		if(null!=wxFans){
			wxFans.setIsFollow(false);
			wxFans.setUnfollowtime(new Timestamp(System.currentTimeMillis()));
			wxFansService.save(wxFans);
		}
	}

}
