package cn.emay.modules.wx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.wx.entity.AutoResponse;
import cn.emay.modules.wx.repository.AutoResponseDao;
import cn.emay.modules.wx.service.AutoResponseService;

/**
 * 自动回复信息表-关键字service 实现
 * @author lenovo
 *
 */
@Service
@Transactional(readOnly=true)
public class AutoResponseServiceImpl extends CommonServiceImpl implements AutoResponseService {

	@Autowired
	private AutoResponseDao autoResponseDao;
	
	
	@Override
	public AutoResponse findAutoResponseByKeyWord(String keyWord) {
		return autoResponseDao.findUniqueByProperty(AutoResponse.class, "keyWord", keyWord);
	}
	
}
