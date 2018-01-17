package cn.emay.modules.sys.service;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.sys.entity.Params;

/**
 * 
 * @Title 参数service接口
 * @author zjlwm
 * @date 2017-2-7 下午2:47:58
 *
 */
public interface ParamsService extends CommonService{

	/**
	 * 查找参数名称存在与否
	 * @param name
	 * @return
	 */
	public long findParamsByNameCount(String name);
	
	/**
	 * 参数查找
	 * @param name
	 * @return
	 */
	public String findParamsByName(String name);
	
	
	
	/**
	 * 参数保存
	 */
	public void saveOrUpdate(Params params);
}
