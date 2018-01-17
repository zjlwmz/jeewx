package cn.emay.modules.sys.service;

import java.util.List;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.sys.entity.Role;

/**
 * 角色用户Service接口
 * 
 * @author zjlwm
 * @date 2016-12-4 下午9:40:49
 * 
 */
public interface RoleUserService extends CommonService {

	/**
	 * 根据用户Id删除
	 * 
	 * @param userId
	 */
	public void deleteByUserId(String userId);

	/**
	 * 用户角色查找
	 * 
	 * @param userId
	 * @return
	 */
	public List<Role> findRoleByUser(String userId);
}
