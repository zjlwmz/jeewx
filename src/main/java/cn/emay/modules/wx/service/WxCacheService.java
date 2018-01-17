package cn.emay.modules.wx.service;

import cn.emay.modules.wx.entity.WxWechat;


/**
 * 微信token缓存
 * @author zjlWm
 * @date 2015-11-30
 */
public interface WxCacheService {

	public String getToken();
	public String getToken(String appid,String secret);
	
	public String getTicket(String AccessToken);
	
	public  WxWechat getWxWechat(String original);
	
	public WxWechat getWxWechat();
	
}
