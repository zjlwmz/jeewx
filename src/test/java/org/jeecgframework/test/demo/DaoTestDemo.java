package org.jeecgframework.test.demo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.emay.framework.core.common.dao.impl.CommonDao;
import cn.emay.framework.core.junit.AbstractUnitTest;
import cn.emay.modules.sys.entity.User;
/**
 * DAO 测试DEMO
 * @author 许国杰
 *
 */
public class DaoTestDemo extends AbstractUnitTest{
	@Autowired
	private CommonDao commonDao ;
	@Test
	public void testGetUserRole() {
		User user = new User();
		user.setUserName("admin");
		user.setPassword("c44b01947c9e6e3f");
		User user2 = commonDao.getUserByUserIdAndUserNameExits(user);
		assert(user2.getUserName().equals(user.getUserName()));
	}
}
