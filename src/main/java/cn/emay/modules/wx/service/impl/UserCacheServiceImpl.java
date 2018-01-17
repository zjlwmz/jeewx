package cn.emay.modules.wx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.modules.sys.entity.User;
import cn.emay.modules.sys.service.UserService;
import cn.emay.modules.wx.entity.WxFans;
import cn.emay.modules.wx.service.UserCacheService;
import cn.emay.modules.wx.service.WxFansService;
import cn.emay.modules.wx.support.UserType;


/**
 * 
 * @Title 用户缓存【微信用户、系统客服用户接口实现】
 * @author zjlwm
 * @date 2017-2-12 上午9:19:17
 *
 */
@Service
@Transactional(readOnly=true)
public class UserCacheServiceImpl implements UserCacheService{

	/**
	 * 微信粉丝service接口
	 */
	@Autowired
	private WxFansService wxFansService;
	
	
	/**
	 * 用户service接口
	 */
	@Autowired
	private UserService userService;
	
	@Override
	public String findUserType(String userId) {
		WxFans wxFans=wxFansService.findWxFansByOpenid(userId);
		if(null!=wxFans){
			return UserType.weixin;
		}
		User user=userService.get(userId);
		if(user!=null){
			return UserType.cusUser;
		}
		return UserType.noUser;
	}

}
