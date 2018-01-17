package cn.emay.modules.wx.service;


/**
 * 
 * @Title 用户缓存【微信用户、系统客服用户接口】
 * @author zjlwm
 * @date 2017-2-12 上午9:13:24
 *
 */
public interface UserCacheService {

	/**
	 * 查询用户类型
	 * @param userId
	 * @return
	 */
	public String findUserType(String userId);
}
