package cn.emay.modules.sys.service;

import java.util.List;
import java.util.Map;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.sys.entity.Function;
import cn.emay.modules.sys.entity.User;
/**
 * 
 * @author  张代浩
 *
 */
public interface UserService extends CommonService{

	public User checkUserExits(User user);
	
	
	public String getUserRole(User user);
	
	public void pwdInit(User user, String newPwd);
	
	
//	/**
//	 * 判断这个角色是不是还有用户使用
//	 *@Author JueYue
//	 *@date   2013-11-12
//	 *@param id
//	 *@return
//	 */
//	public int getUsersOfThisRole(String id);
	
	/**
	 * 该角色下有多少用户
	 * @param roleId
	 * @return
	 */
	public Long findRoleUser(String roleId);
	
	/**
	 * 根据id用户用户对象
	 * @param id
	 * @return
	 */
	public User get(String id);
	
	/**
	 * 用户拥有的菜单权限
	 * @param user
	 * @return
	 */
	public Map<Integer, List<Function>> getFunctionMap(User user);
	
	
	
	/**
	 * 获取用户组织机构数量
	 */
	public Long getOrgNum(String userId);
	
	/**
	 * 获取用户组织机构id
	 * @return
	 */
	public Map<String, Object>getUserOrgId(String userId);
	
	/**
	 * 用户查找
	 * @param loginName
	 * @return
	 */
	public User getUserByLoginName(String loginName);
	
	
	
	/**
	 * 角色查找用户
	 */
	public List<User>findUserByRole(String roleId); 
	
	
	/**
	 * 用户删除
	 * @param user
	 */
	public void delete(User user);
}
