package cn.emay.modules.sys.service;

import java.util.List;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.sys.entity.Function;

/**
 * 菜单service接口
 * @author zjlwm
 * @date 2016-12-4 下午6:13:37
 *
 */
public interface FunctionService extends CommonService{

	public Function get(String id);
	
	public void update(Function function);
	
	public List<Function>findFunctionByParent(String parentId);
}
