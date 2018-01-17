package cn.emay.modules.wx.service;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.wx.entity.Subscribe;

/**
 * 关注提示语service接口
 * @author lenovo
 *
 */
public interface SubscribeService extends CommonService{

	
	/**
	 * 查询关注回复提示语
	 */
	public Subscribe findSubscribeByWechatId(String wechatId);
	
	
}
