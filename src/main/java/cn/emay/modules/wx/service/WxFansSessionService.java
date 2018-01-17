package cn.emay.modules.wx.service;

import java.util.List;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.wx.entity.WxFansSession;

/**
 * 微信会话service接口
 * @author lenovo
 *
 */
public interface WxFansSessionService extends CommonService{
	
	public WxFansSession get(String id);
	
	public void save(WxFansSession wxFansSession);
	
	/**
	 * 当前会话
	 * @param openid
	 * @return
	 */
	public WxFansSession findCurrentWxFansSessionByOpenid(String openid);
	
	
	/**
	 * 48小时以内的会话数据
	 * @return
	 */
	public List<WxFansSession>findThePast48Hourse();
	
	
	/**
	 * 48小时前的数据
	 * @return
	 */
	public List<WxFansSession>find48HourseAgo();
	
	/**
	 * 更新会话剩余时间
	 * @param wxFansSession
	 */
	public void updateWxFansSessionSurplusDate(WxFansSession wxFansSession);
}
