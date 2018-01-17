package org.jeecgframework.test.demo;

import java.util.Date;

import org.junit.Test;

import cn.emay.modules.chat.entity.ChatFriend;

public class JSONHelperTest {

	/**
	 * 将对象转换为List对象
	 */
	@Test
	public void toArrayListTest(){
		ChatFriend chatFriend=new ChatFriend();
		chatFriend.setCreateDate(new Date());
		chatFriend.setGroupId("1");
		chatFriend.setOpenid("dagew");
		chatFriend.setStatus(1);
		chatFriend.setUserid("131");
//		List list=JSONHelper.toArrayList(chatFriend);
//		System.out.println(JSON.toJSONString(list));
//		
//		
	}
}
