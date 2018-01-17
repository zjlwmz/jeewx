package cn.emay.modules.sys.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.emay.modules.sys.service.MutiLangService;
import cn.emay.modules.sys.service.SystemService;


/**
 * 系统初始化监听器,在系统启动时运行,进行一些初始化工作
 * @author laien
 *
 */
public class InitListener  implements ServletContextListener {

	
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	
	public void contextInitialized(ServletContextEvent event) {
		WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
		SystemService systemService = (SystemService) webApplicationContext.getBean("systemService");
		MutiLangService mutiLangService = (MutiLangService) webApplicationContext.getBean("mutiLangService");
		
		/**
		 * 第一部分：对数据字典进行缓存
		 */
		systemService.initAllTypeGroups();
		systemService.initAllTSIcons();
		
		
//		/**
//		 * 第二部分：自动加载新增菜单和菜单操作权限
//		 * 说明：只会添加，不会删除（添加在代码层配置，但是在数据库层未配置的）
//		 */
//		if("true".equals(ResourceUtil.getConfigByName("auto.scan.menu.flag").toLowerCase())){
//			menuInitService.initMenu();
//		}
		
		/**
		 * 第三部分：加载多语言内容
		 */
		mutiLangService.initAllMutiLang();
		
		
	}

}
