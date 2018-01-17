package cn.emay.modules.sys.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.sys.service.TimeTaskServiceI;

@Service("timeTaskService")
@Transactional
public class TimeTaskServiceImpl extends CommonServiceImpl implements TimeTaskServiceI {
	
}