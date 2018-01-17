package org.jeecgframework.test.demo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.emay.framework.core.junit.AbstractUnitTest;
import cn.emay.modules.sys.entity.User;
import cn.emay.modules.sys.service.UserService;
/**
 * Service 单元测试Demo
 * @author  许国杰
 */
public class ServiceTestDemo extends AbstractUnitTest{
	@Autowired
	private UserService userService;
	
	@Test
	public void testCheckUserExits() {
		User user = new User();
		user.setUserName("admin");
		user.setPassword("c44b01947c9e6e3f");
		User u = userService.checkUserExits(user);
		assert(u.getUserName().equals(user.getUserName()));
	}

	@Test
	public void testGetUserRole() {
		User user = new User();
		user.setId("8a8ab0b246dc81120146dc8181950052");
		String roles = userService.getUserRole(user);
		assert(roles.equals("admin,"));
	}

}
