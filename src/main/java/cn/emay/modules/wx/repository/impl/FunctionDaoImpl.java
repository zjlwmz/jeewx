package cn.emay.modules.wx.repository.impl;

import org.springframework.stereotype.Repository;

import cn.emay.framework.core.common.dao.impl.GenericBaseCommonDao;
import cn.emay.modules.sys.entity.Function;
import cn.emay.modules.wx.repository.FunctionDao;


/**
 * @Title 功能菜单
 * @author zjlwm
 * @date 2017-1-14 下午9:46:56
 *
 */
@Repository
public class FunctionDaoImpl extends GenericBaseCommonDao<Function, String> implements FunctionDao{

}
