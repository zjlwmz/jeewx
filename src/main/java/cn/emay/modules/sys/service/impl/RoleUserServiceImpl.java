package cn.emay.modules.sys.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.sys.entity.Role;
import cn.emay.modules.sys.service.RoleUserService;

/**
 * 角色用户Service接口实现
 * @author zjlwm
 * @date 2016-12-4 下午9:41:41
 *
 */
@Service("roleUserService")
public class RoleUserServiceImpl extends CommonServiceImpl implements RoleUserService{

	
	@Override
	public void deleteByUserId(String userId) {
		String sql="DELETE FROM sys_role_user WHERE userid = '"+userId+"'";
		this.executeSql(sql);
	}

	@Override
	public List<Role> findRoleByUser(String userId) {
		String sql="select * from sys_role where id in (select roleid from sys_role_user where userid=:userid)";
		@SuppressWarnings("unchecked")
		List<Role>roleList=commonDao.getSession().createSQLQuery(sql).addEntity(Role.class).setString("userid", userId).list();
		return roleList;
	}

}
