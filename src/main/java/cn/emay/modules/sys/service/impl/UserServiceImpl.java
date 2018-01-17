package cn.emay.modules.sys.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.common.utils.ContextHolderUtils;
import cn.emay.framework.common.utils.NumberComparator;
import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.framework.core.constant.Globals;
import cn.emay.modules.sys.entity.Client;
import cn.emay.modules.sys.entity.Function;
import cn.emay.modules.sys.entity.RoleUser;
import cn.emay.modules.sys.entity.User;
import cn.emay.modules.sys.manager.ClientManager;
import cn.emay.modules.sys.repository.UserDao;
import cn.emay.modules.sys.service.UserService;

/**
 * 用户service接口实现
 * @author lenovo
 *
 */
@Service("userService")
@Transactional
public class UserServiceImpl extends CommonServiceImpl implements UserService {

	/**
	 * 用户DAO
	 */
	@Autowired
	private UserDao userDao;
	
	
	public User checkUserExits(User user) {
		return this.userDao.getUserByUserIdAndUserNameExits(user);
	}

	public String getUserRole(User user) {
		return this.userDao.getUserRole(user);
	}

	public void pwdInit(User user, String newPwd) {
		this.userDao.pwdInit(user, newPwd);
	}

//	public int getUsersOfThisRole(String id) {
//		
//		
//		Criteria criteria = getSession().createCriteria(RoleUser.class);
//		criteria.add(Restrictions.eq("role.id", id));
//		int allCounts = ((Long) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
//		return allCounts;
//	}

	
	public Long findRoleUser(String roleId){
		String sql="select count(*) from sys_role_user where roleid='"+roleId+"'";
		return userDao.getCountForJdbc(sql);
	}
	
	
	/**
	 * 获取权限的map
	 */
	public Map<Integer, List<Function>> getFunctionMap(User user) {
		HttpSession session = ContextHolderUtils.getSession();
		Client client = ClientManager.getInstance().getClient(session.getId());
		if (client.getFunctionMap() == null || client.getFunctionMap().size() == 0) {
			Map<Integer, List<Function>> functionMap = new HashMap<Integer, List<Function>>();
			Map<String, Function> loginActionlist = getUserFunction(user);
			if (loginActionlist.size() > 0) {
				Collection<Function> allFunctions = loginActionlist.values();
				for (Function function : allFunctions) {

					if (function.getFunctionType().intValue() == Globals.Function_TYPE_FROM.intValue()) {
						// 如果为表单或者弹出 不显示在系统菜单里面
						continue;
					}

					if (!functionMap.containsKey(function.getFunctionLevel() + 0)) {
						functionMap.put(function.getFunctionLevel() + 0, new ArrayList<Function>());
					}
					functionMap.get(function.getFunctionLevel() + 0).add(function);
				}
				// 菜单栏排序
				Collection<List<Function>> c = functionMap.values();
				for (List<Function> list : c) {
					Collections.sort(list, new NumberComparator());
				}
			}
			client.setFunctionMap(functionMap);

			// 清空变量，降低内存使用
			loginActionlist.clear();

			return functionMap;
		} else {
			return client.getFunctionMap();
		}
	}
	
	
	/**
	 * 获取用户菜单列表
	 * 
	 * @param user
	 * @return
	 */
	private Map<String, Function> getUserFunction(User user) {
		HttpSession session = ContextHolderUtils.getSession();
		Client client = ClientManager.getInstance().getClient(session.getId());

		if (client.getFunctions() == null || client.getFunctions().size() == 0) {

			Map<String, Function> loginActionlist = new HashMap<String, Function>();

			StringBuilder hqlsb1 = new StringBuilder("select distinct f from Function f,RoleFunction rf,RoleUser ru  ").append("where ru.role.id=rf.role.id and rf.function.id=f.id and ru.user.id=? ");
			StringBuilder hqlsb2 = new StringBuilder("select distinct c from Function c,RoleOrg b,UserOrg a ").append("where a.depart.id=b.depart.id and b.role.id=c.id and a.user.id=?");
			List<Function> list1 = userDao.findHql(hqlsb1.toString(), user.getId());
			List<Function> list2 = userDao.findHql(hqlsb2.toString(), user.getId());
			for (Function function : list1) {
				loginActionlist.put(function.getId(), function);
			}
			for (Function function : list2) {
				loginActionlist.put(function.getId(), function);
			}
			client.setFunctions(loginActionlist);

			// 清空变量，降低内存使用
			list2.clear();
			list1.clear();

		}
		return client.getFunctions();
	}
	
	
	@Override
	public User get(String id) {
		return commonDao.get(User.class, id);
	}

	@Override
	public Long getOrgNum(String userId) {
		return userDao.getOrgNum(userId);
	}

	
	public Map<String, Object>getUserOrgId(String userId){
		return userDao.getUserOrgId(userId);
	}

	@Override
	public User getUserByLoginName(String loginName) {
		List<User>userList=userDao.findByQueryString("from User where userName='"+loginName+"'");
		if(userList.size()>0){
			return userList.get(0);
		}
		return null;
	}

	@Override
	public List<User> findUserByRole(String roleId) {
		List<User>userList=new ArrayList<User>();
		List<RoleUser> roleUserList = userDao.findByProperty(RoleUser.class, "role.id", roleId);
		for(RoleUser roleUser:roleUserList){
			userList.add(roleUser.getUser());
		}
		return userList;
	}
	
	@Override
	public void delete(User user) {
		user.setDeleteFlag(Globals.Delete_Forbidden);
		updateEntitie(user);
		
		//删除用户与角色的中间表关系
		String roleUserSql="DELETE FROM sys_role_user WHERE userid = '"+user.getId()+"'";
		this.executeSql(roleUserSql);
		
		//删除用户与组织机构中间表关系
		String userOrgSql="DELETE FROM sys_user_org WHERE user_id = '"+user.getId()+"'";
		this.executeSql(userOrgSql);
		
	}
}
