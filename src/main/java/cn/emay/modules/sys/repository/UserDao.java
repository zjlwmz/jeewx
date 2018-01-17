package cn.emay.modules.sys.repository;

import java.util.Map;

import cn.emay.framework.core.common.dao.IGenericBaseCommonDao;
import cn.emay.modules.sys.entity.User;


/**
 * 用户DAO
 * @author lenovo
 *
 */
public interface UserDao extends IGenericBaseCommonDao{

	/**
	 * 检查用户是否存在
	 */
	public User getUserByUserIdAndUserNameExits(User user);
	
	public String getUserRole(User user);
	
	/**
	 * admin账户初始化
	 */
	public void pwdInit(User user,String newPwd);
	
	
	
	public Long getOrgNum(String userId);
	
	
	public Map<String, Object>getUserOrgId(String userId);
	
	
}
