package cn.emay.modules.wx.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.wx.service.ExpandconfigService;


/**
 * 扩展接口service接口实现
 * @author lenovo
 *
 */
@Service
@Transactional(readOnly=true)
public class ExpandconfigServiceImpl extends CommonServiceImpl implements ExpandconfigService {

}
