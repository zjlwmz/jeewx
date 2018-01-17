package cn.emay.modules.wx.task;

import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.emay.framework.common.utils.SpringContextHolder;
import cn.emay.modules.sys.entity.Client;
import cn.emay.modules.sys.entity.User;
import cn.emay.modules.sys.manager.ClientManager;
import cn.emay.modules.sys.service.UserService;

/**
 * 刷新在线客服人员列表任务
 * 
 * @author lenovo
 * 
 */
public class OnlineCustomerServerTask implements Job {

	/**
	 * 用户接口对象
	 */
	private UserService userService = SpringContextHolder.getBean(UserService.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		List<Client> onlines = new ArrayList<Client>();
		onlines.addAll(ClientManager.getInstance().getAllClient());

		/**
		 * 增加在线
		 */
		for (Client client : onlines) {
			User user = client.getUser();
			if (null != user) {
				String roleCodeName = userService.getUserRole(user);
				if (roleCodeName.contains("customservice")) {
					ClientManager.getInstance().addOnlineCustomerServiceClient(user);
				}
			}

		}

		/**
		 * 删除不在线的
		 */
		List<User> onlineCustomerServiceList = new ArrayList<User>();
		onlineCustomerServiceList.addAll(ClientManager.getInstance().getOnlineCustomerServiceClient());
		for (User User : onlineCustomerServiceList) {
			boolean isExit = false;
			for (Client client : onlines) {
				User user = client.getUser();
				if (null != user && user.getId().equals(User.getId())) {
					isExit = true;
				}
			}
			if (!isExit) {
				ClientManager.getInstance().removeOnlineCustomerServiceClient(User);
			}
		}

	}

}
