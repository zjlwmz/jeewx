package cn.emay.modules.wx.service;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.wx.entity.WxLocation;


/**
 * 微信位置上报service
 * @author lenovo
 *
 */
public interface WxLocationService extends CommonService{

	public void save(WxLocation wxLocation );
}
