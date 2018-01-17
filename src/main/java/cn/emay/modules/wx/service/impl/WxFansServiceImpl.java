package cn.emay.modules.wx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.common.utils.StringUtils;
import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.wx.entity.WxFans;
import cn.emay.modules.wx.repository.WxFansDao;
import cn.emay.modules.wx.service.WxFansService;

/**
 * 微信粉丝service接口实现
 * @author zjlwm
 *
 */
@Transactional(readOnly=true)
@Service
public class WxFansServiceImpl extends CommonServiceImpl implements WxFansService {
	
	/**
	 * 微信粉丝DAO接口
	 */
	@Autowired
	private WxFansDao wxFansDao;
	
	@Override
	public WxFans get(String id) {
		return wxFansDao.get(WxFans.class, id);
	}

	@Transactional(readOnly=false)
	public synchronized void save(WxFans wxFans) {
		if(StringUtils.isNotBlank(wxFans.getId())){
			wxFansDao.updateEntitie(wxFans);
		}else{
			wxFansDao.save(wxFans);
		}
	}

	@Override
	public WxFans findWxFansByOpenid(String openid) {
		return wxFansDao.findUniqueByProperty(WxFans.class, "openid", openid);
	}

}
