package cn.emay.modules.wx.service;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.wx.entity.AutoResponse;


/**
 * 自动回复信息表-关键字service
 * @author lenovo
 *
 */
public interface AutoResponseService extends CommonService{

	public AutoResponse findAutoResponseByKeyWord(String keyWord);
	
}
