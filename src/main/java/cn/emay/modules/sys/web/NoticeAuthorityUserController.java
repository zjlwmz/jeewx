package cn.emay.modules.sys.web;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.emay.framework.common.utils.MyBeanUtils;
import cn.emay.framework.common.utils.StringUtil;
import cn.emay.framework.core.common.controller.BaseController;
import cn.emay.framework.core.common.exception.BusinessException;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.constant.Globals;
import cn.emay.framework.tag.core.easyui.TagUtil;
import cn.emay.modules.sys.entity.TSNoticeAuthorityUser;
import cn.emay.modules.sys.service.NoticeAuthorityUserServiceI;
import cn.emay.modules.sys.service.SystemService;


/**   
 * @Title: Controller
 * @Description: 通知公告用户授权
 * @author onlineGenerator
 * @date 2016-02-26 12:47:09
 * @version V1.0   
 *
 */
//@Scope("prototype")
@Controller
@RequestMapping("/noticeAuthorityUserController")
public class NoticeAuthorityUserController extends BaseController {
	/**
	 * Logger for this class
	 */
//	private static final Logger logger = Logger.getLogger(NoticeAuthorityUserController.class);

	@Autowired
	private NoticeAuthorityUserServiceI noticeAuthorityUserService;
	@Autowired
	private SystemService systemService;


	/**
	 * 通知公告用户授权列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "noticeAuthorityUser")
	public ModelAndView noticeAuthorityUser(String noticeId,HttpServletRequest request) {
		request.setAttribute("noticeId", noticeId);
		return new ModelAndView("system/user/noticeAuthorityUserList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(TSNoticeAuthorityUser noticeAuthorityUser,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TSNoticeAuthorityUser.class, dataGrid);
		//查询条件组装器
		cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, noticeAuthorityUser, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.noticeAuthorityUserService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除通知公告用户授权
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(TSNoticeAuthorityUser noticeAuthorityUser, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		noticeAuthorityUser = systemService.getEntity(TSNoticeAuthorityUser.class, noticeAuthorityUser.getId());
		message = "通知公告用户授权删除成功";
		try{
			noticeAuthorityUserService.delete(noticeAuthorityUser);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "通知公告用户授权删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除通知公告用户授权
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "通知公告用户授权删除成功";
		try{
			for(String id:ids.split(",")){
				TSNoticeAuthorityUser noticeAuthorityUser = systemService.getEntity(TSNoticeAuthorityUser.class, 
				id
				);
				noticeAuthorityUserService.delete(noticeAuthorityUser);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "通知公告用户授权删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加通知公告用户授权
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(TSNoticeAuthorityUser noticeAuthorityUser, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "通知公告用户授权添加成功";
		try{
			noticeAuthorityUserService.save(noticeAuthorityUser);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "通知公告用户授权添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 保存通知公告用户授权
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doSave")
	@ResponseBody
	public AjaxJson doSave(TSNoticeAuthorityUser noticeAuthorityUser, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "通知公告用户授权保存成功";
		try{
			if(this.noticeAuthorityUserService.checkAuthorityUser(noticeAuthorityUser.getNoticeId(), noticeAuthorityUser.getUser().getId())){
				message = "该用户已授权，请勿重复操作。";
			}else{
				noticeAuthorityUserService.save(noticeAuthorityUser);
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "通知公告用户授权保存失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新通知公告用户授权
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(TSNoticeAuthorityUser noticeAuthorityUser, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "通知公告用户授权更新成功";
		TSNoticeAuthorityUser t = noticeAuthorityUserService.get(TSNoticeAuthorityUser.class, noticeAuthorityUser.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(noticeAuthorityUser, t);
			noticeAuthorityUserService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "通知公告用户授权更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 通知公告用户授权新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(TSNoticeAuthorityUser noticeAuthorityUser, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(noticeAuthorityUser.getId())) {
			noticeAuthorityUser = noticeAuthorityUserService.getEntity(TSNoticeAuthorityUser.class, noticeAuthorityUser.getId());
			req.setAttribute("noticeAuthorityUserPage", noticeAuthorityUser);
		}
		return new ModelAndView("system/user/noticeAuthorityUser-add");
	}
	/**
	 * 通知公告用户授权编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(TSNoticeAuthorityUser noticeAuthorityUser, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(noticeAuthorityUser.getId())) {
			noticeAuthorityUser = noticeAuthorityUserService.getEntity(TSNoticeAuthorityUser.class, noticeAuthorityUser.getId());
			req.setAttribute("noticeAuthorityUserPage", noticeAuthorityUser);
		}
		return new ModelAndView("system/user/noticeAuthorityUser-update");
	}
	
	/**
	 * 用户选择页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "selectUser")
	public ModelAndView selectUser(HttpServletRequest request) {
		return new ModelAndView("system/user/userList-select");
	}
}
