package cn.emay.modules.wx.service;

import javax.servlet.http.HttpServletRequest;


/**
 * 微信接收service
 * @author lenovo
 *
 */
public interface WechatReceiveService{

	/**
	 * 微信接口对接
	 * @param request
	 * @return
	 */
	public String coreService(HttpServletRequest request);
	
	
	/**
	 * 微信接口对接 扩展
	 * 采用数据集中收集处理
	 * @param request
	 * @return
	 */
	public String coreServiceExtend(HttpServletRequest request);
}
