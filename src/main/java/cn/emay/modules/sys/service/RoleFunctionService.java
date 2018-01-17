package cn.emay.modules.sys.service;

import java.util.List;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.sys.entity.RoleFunction;

/**
 * 
 * @author zjlwm
 * @date 2016-12-4 下午6:24:08
 *
 */
public interface RoleFunctionService extends CommonService{

	
	public List<RoleFunction> findRoleFunctionByOperation(String operation);
}
