package cn.emay.modules.wx.service;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.wx.entity.FunctionLog;


/**
 * 功能日志service接口
 * @author lenovo
 *
 */
public interface FunctionLogService extends CommonService{

	void save(FunctionLog functionLog);

}
