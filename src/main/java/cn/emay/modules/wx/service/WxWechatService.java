package cn.emay.modules.wx.service;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.wx.entity.WxWechat;


/**
 * 微信账号service接口
 * @author zjlwm
 *
 */
public interface WxWechatService extends CommonService{

	public WxWechat get(String wechatId);
	
	public void save(WxWechat wxWechat);
	
	public WxWechat saveOrUpdate(WxWechat wxWechat);
	
	public WxWechat findWxWechat();
	
	public WxWechat findByOriginal(String original);
}
