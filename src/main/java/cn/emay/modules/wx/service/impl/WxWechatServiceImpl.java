package cn.emay.modules.wx.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.wx.entity.WxWechat;
import cn.emay.modules.wx.repository.WxWechatDao;
import cn.emay.modules.wx.service.WxWechatService;

/**
 * 微信账号service接口 实现
 * @author zjlwm
 *
 */
@Service
@Transactional(readOnly=true)
public class WxWechatServiceImpl extends CommonServiceImpl implements WxWechatService {
	
	@Autowired
	private WxWechatDao wxWechatDao;
	
	@Override
	public WxWechat get(String wechatId) {
		return wxWechatDao.get(WxWechat.class, wechatId);
	}

	@Transactional
	@Override
	public void save(WxWechat wxWechat) {
		wxWechatDao.save(wxWechat);
	}

	@Transactional
	@Override
	public WxWechat saveOrUpdate(WxWechat wxWechat) {
		if(StringUtils.isNotBlank(wxWechat.getId())){
			this.updateEntitie(wxWechat);
		}else{
			this.save(wxWechat);
		}
		return wxWechat;
	}

	@Override
	public WxWechat findWxWechat() {
		List<WxWechat>wxWechatList=wxWechatDao.findHql("from WxWechat");
		if(wxWechatList.size()>0){
			return wxWechatList.get(0);
		}
		return null;
	}


	@Override
	public WxWechat findByOriginal(String original) {
		return null;
	}

}
