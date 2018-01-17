/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.emay.framework.common.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.emay.framework.common.config.Global;
import cn.emay.framework.common.utils.FileUtils;
import cn.emay.modules.sys.entity.User;
import cn.emay.modules.sys.utils.UserCacheUtils;

import com.ckfinder.connector.ConnectorServlet;

/**
 * CKFinderConnectorServlet
 * 
 * @author ThinkGem
 * @version 2014-06-25
 */
public class CKFinderConnectorServlet extends ConnectorServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		prepareGetResponse(request, response, false);
		super.doGet(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		prepareGetResponse(request, response, true);
		super.doPost(request, response);
	}

	private void prepareGetResponse(final HttpServletRequest request, final HttpServletResponse response, final boolean post) throws ServletException {
		String command = request.getParameter("command");
		String type = request.getParameter("type");
		User user = UserCacheUtils.getCurrentUser();
		if (user == null) {
			return;
		}
		// 初始化时，如果startupPath文件夹不存在，则自动创建startupPath文件夹
		if ("Init".equals(command)) {
			String startupPath = request.getParameter("startupPath");// 当前文件夹可指定为模块名
			if (startupPath != null) {
				String[] ss = startupPath.split(":");
				if (ss.length == 2) {
					String realPath = Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + user.getId() + "/" + ss[0] + ss[1];
					FileUtils.createDirectory(FileUtils.path(realPath));
				}
			}
		}
		// 快捷上传，自动创建当前文件夹，并上传到该路径
		else if ("QuickUpload".equals(command) && type != null) {
			String currentFolder = request.getParameter("currentFolder");// 当前文件夹可指定为模块名
			String realPath = Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL + user.getId() + "/" + type + (currentFolder != null ? currentFolder : "");
			FileUtils.createDirectory(FileUtils.path(realPath));
		}
		// System.out.println("------------------------");
		// for (Object key : request.getParameterMap().keySet()){
		// System.out.println(key + ": " +
		// request.getParameter(key.toString()));
		// }
	}

}
