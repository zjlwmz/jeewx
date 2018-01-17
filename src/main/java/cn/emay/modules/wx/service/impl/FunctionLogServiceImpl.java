package cn.emay.modules.wx.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.wx.entity.FunctionLog;
import cn.emay.modules.wx.service.FunctionLogService;

/**
 * 功能日志service接口实现
 * @author lenovo
 *
 */
@Service
@Transactional(readOnly=true)
public class FunctionLogServiceImpl extends CommonServiceImpl implements FunctionLogService {

	@Override
	public void save(FunctionLog functionLog) {

	}

}
