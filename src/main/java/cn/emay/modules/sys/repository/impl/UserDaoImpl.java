package cn.emay.modules.sys.repository.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import cn.emay.framework.common.utils.PasswordUtil;
import cn.emay.framework.core.common.dao.impl.GenericBaseCommonDao;
import cn.emay.modules.sys.entity.RoleUser;
import cn.emay.modules.sys.entity.User;
import cn.emay.modules.sys.repository.UserDao;


/**
 * 用户DAO接口实现
 * @author lenovo
 *
 */
@Repository
public class UserDaoImpl extends GenericBaseCommonDao<User, String>  implements UserDao {

	/**
	 * 检查用户是否存在
	 * */
	@SuppressWarnings("unchecked")
	public User getUserByUserIdAndUserNameExits(User user) {
		String password = PasswordUtil.encrypt(user.getUserName(), user.getPassword(), PasswordUtil.getStaticSalt());
		String query = "from User u where u.userName = :username and u.password=:passowrd";
		Query queryObject = getSession().createQuery(query);
		queryObject.setParameter("username", user.getUserName());
		queryObject.setParameter("passowrd", password);
		List<User> users = queryObject.list();

		if (users != null && users.size() > 0) {
			return users.get(0);
		} else {
			queryObject = getSession().createQuery(query);
			queryObject.setParameter("username", user.getUserName());
			queryObject.setParameter("passowrd", user.getPassword());
			users = queryObject.list();
			if(users != null && users.size() > 0){
				return users.get(0);
			}
		}
		return null;
	}
	
	
	public String getUserRole(User user) {
		String userRole = "";
		List<RoleUser> sRoleUser = findByProperty(RoleUser.class, "user.id", user.getId());
		for (RoleUser tsRoleUser : sRoleUser) {
			userRole += tsRoleUser.getRole().getRoleCode() + ",";
		}
		return userRole;
	}
	
	
	/**
	 * admin账户初始化
	 */
	public void pwdInit(User user,String newPwd){
		String query ="from User u where u.userName = :username ";
		Query queryObject = getSession().createQuery(query);
		queryObject.setParameter("username", user.getUserName());
		@SuppressWarnings("unchecked")
		List<User> users =  queryObject.list();
		if(null != users && users.size() > 0){
			user = users.get(0);
			String pwd = PasswordUtil.encrypt(user.getUserName(), newPwd, PasswordUtil.getStaticSalt());
			user.setPassword(pwd);
			save(user);
		}
		
	}
	
	public Long getOrgNum(String userId){
		String sql="select count(1) from sys_user_org where user_id = '" + userId + "'";
		return this.getCountForJdbc(sql);
	}
	
	
	public Map<String, Object>getUserOrgId(String userId){
		String sql="select org_id as orgId from sys_user_org where user_id='"+userId+"'";
		Map<String, Object> data= this.findOneForJdbc(sql);
		return data;
	}
}
