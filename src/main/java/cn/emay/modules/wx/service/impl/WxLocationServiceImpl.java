package cn.emay.modules.wx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.wx.entity.WxLocation;
import cn.emay.modules.wx.repository.WxLocationDao;
import cn.emay.modules.wx.service.WxLocationService;

/**
 * 微信位置上报
 * @author lenovo
 *
 */
@Service
@Transactional(readOnly=true)
public class WxLocationServiceImpl extends CommonServiceImpl implements WxLocationService {

	@Autowired
	private WxLocationDao wxLocationDao;
	
	@Transactional
	@Override
	public void save(WxLocation weixinLocation) {
		wxLocationDao.save(weixinLocation);
	}

}
