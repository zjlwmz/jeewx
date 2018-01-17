package cn.emay.modules.wx.service;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.wx.entity.WxFans;

/**
 * 微信粉丝service接口
 * 
 * @author zjlwm
 * 
 */
public interface WxFansService extends CommonService {
	
	public WxFans get(String id);

	public void save(WxFans wxFans);
	
	public WxFans findWxFansByOpenid(String openid);
}
