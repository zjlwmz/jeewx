package cn.emay.modules.wx.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.wx.entity.Subscribe;
import cn.emay.modules.wx.service.SubscribeService;
/**
 * 关注提示语service接口实现
 * @author lenovo
 *
 */
@Service
@Transactional(readOnly=true)
public class SubscribeServiceImpl extends CommonServiceImpl implements SubscribeService {

	@Override
	public Subscribe findSubscribeByWechatId(String wechatId) {
		return null;
	}

}
