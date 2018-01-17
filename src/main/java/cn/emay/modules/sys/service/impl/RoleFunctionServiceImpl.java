package cn.emay.modules.sys.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.sys.entity.RoleFunction;
import cn.emay.modules.sys.service.RoleFunctionService;


/**
 * 
 * @author zjlwm
 * @date 2016-12-4 下午6:25:03
 *
 */
@Service("roleFunctionService")
public class RoleFunctionServiceImpl extends CommonServiceImpl implements RoleFunctionService{

	@Override
	public List<RoleFunction> findRoleFunctionByOperation(String operation) {
		String hql = "from RoleFunction rolefun where rolefun.operation like '%" + operation + "%'";
		List<RoleFunction> roleFunctions = findByQueryString(hql);
		return roleFunctions;
	}

}
