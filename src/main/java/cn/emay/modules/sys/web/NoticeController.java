package cn.emay.modules.sys.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.emay.framework.common.utils.MutiLangUtil;
import cn.emay.framework.common.utils.MyBeanUtils;
import cn.emay.framework.common.utils.ResourceUtil;
import cn.emay.framework.common.utils.StringUtil;
import cn.emay.framework.common.utils.StringUtils;
import cn.emay.framework.core.common.controller.BaseController;
import cn.emay.framework.core.common.exception.BusinessException;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.constant.Globals;
import cn.emay.framework.tag.core.easyui.TagUtil;
import cn.emay.modules.sys.entity.Notice;
import cn.emay.modules.sys.entity.TSNoticeReadUser;
import cn.emay.modules.sys.entity.User;
import cn.emay.modules.sys.service.NoticeService;
import cn.emay.modules.sys.service.SystemService;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 通知公告
 * 
 * @author alax
 * 
 */
@Controller
@RequestMapping("/noticeController")
public class NoticeController extends BaseController {
	private SystemService systemService;

	@Autowired
	private NoticeService noticeService;

	@Autowired
	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}

	/**
	 * 取得用户可读通知公告
	 * 
	 * @param user
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "getNoticeList")
	@ResponseBody
	public AjaxJson getNoticeList(HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		try {
			User user = ResourceUtil.getSessionUserName();

			String sql = "";
			sql += " SELECT * FROM  sys_notice t WHERE";
			sql += " (t.notice_level = '1' ";
			sql += " OR (t.notice_level = '2' AND EXISTS (SELECT 1 FROM sys_notice_authority_role r,sys_role_user ru WHERE r.role_id = ru.roleid AND t.id = r.notice_id AND ru.userid = '" + user.getId() + "'))";
			sql += " OR (t.notice_level = '3' AND EXISTS (SELECT 1 FROM sys_notice_authority_user u WHERE t.id = u.notice_id AND u.user_id = '" + user.getId() + "'))";
			sql += " ) AND NOT EXISTS (SELECT 1 FROM sys_notice_read_user rd WHERE t.id = rd.notice_id AND rd.user_id = '" + user.getId() + "')";

			sql += " ORDER BY t.create_time DESC ";
			List<Map<String, Object>> noticeList = systemService.findForJdbc(sql, 1, 10);

			// 将List转换成JSON存储
			JSONArray result = new JSONArray();
			if (noticeList != null && noticeList.size() > 0) {
				for (int i = 0; i < noticeList.size(); i++) {
					JSONObject jsonParts = new JSONObject();
					jsonParts.put("id", noticeList.get(i).get("id"));
					jsonParts.put("noticeTitle", noticeList.get(i).get("notice_title"));
					result.add(jsonParts);
				}
			}

			Map<String, Object> attrs = new HashMap<String, Object>();
			attrs.put("noticeList", result);

			String tip = MutiLangUtil.getMutiLangInstance().getLang("notice.tip");
			attrs.put("tip", tip);
			String seeAll = MutiLangUtil.getMutiLangInstance().getLang("notice.seeAll");
			attrs.put("seeAll", seeAll);
			j.setAttributes(attrs);

			// 获取通知公告总数
			String sql2 = "";
			sql2 += " SELECT count(*) as count FROM  sys_notice t WHERE";
			sql2 += " (t.notice_level = '1' ";
			sql2 += " OR (t.notice_level = '2' AND EXISTS (SELECT 1 FROM sys_notice_authority_role r,sys_role_user ru WHERE r.role_id = ru.roleid AND t.id = r.notice_id AND ru.userid = '" + user.getId() + "'))";
			sql2 += " OR (t.notice_level = '3' AND EXISTS (SELECT 1 FROM sys_notice_authority_user u WHERE t.id = u.notice_id AND u.user_id = '" + user.getId() + "'))";
			sql2 += " ) AND NOT EXISTS (SELECT 1 FROM sys_notice_read_user rd WHERE t.id = rd.notice_id AND rd.user_id = '" + user.getId() + "')";
			List<Map<String, Object>> resultList2 = systemService.findForJdbc(sql2);
			Object count = resultList2.get(0).get("count");
			j.setObj(count);
		} catch (Exception e) {
			j.setSuccess(false);
			e.printStackTrace();
		}
		return j;
	}

	/**
	 * 通知公告列表（阅读）
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "noticeList")
	public ModelAndView noticeList(HttpServletRequest request) {
		return new ModelAndView("system/user/noticeList");
	}

	/**
	 * 通知公告详情
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "goNotice")
	public ModelAndView noticeInfo(String id, Model model) {
		if (StringUtils.isNotBlank(id)) {
			Notice notice = systemService.getEntity(Notice.class, id);
			model.addAttribute("notice", notice);
		}
		return new ModelAndView("system/user/noticeinfo");
	}

	/**
	 * easyui AJAX请求数据 构建列表数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(Notice notice, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {

		User user = ResourceUtil.getSessionUserName();
		String sql = "";
		sql = sql + " SELECT * FROM  sys_notice t WHERE";
		sql = sql + " t.notice_level = '1' ";
		sql = sql + " OR (t.notice_level = '2' AND EXISTS (SELECT 1 FROM sys_notice_authority_role r,sys_role_user ru WHERE r.role_id = ru.roleid AND t.id = r.notice_id AND ru.userid = '" + user.getId() + "'))";
		sql = sql + " OR (t.notice_level = '3' AND EXISTS (SELECT 1 FROM sys_notice_authority_user u WHERE t.id = u.notice_id AND u.user_id = '" + user.getId() + "'))";
		sql = sql + " ORDER BY t.create_time DESC";
		
		List<Map<String, Object>> resultList = systemService.findForJdbc(sql, dataGrid.getPage(), dataGrid.getRows());
		// 将List转换成JSON存储

		List<Map<String, Object>> noticeList = new ArrayList<Map<String, Object>>();
		if (resultList != null && resultList.size() > 0) {
			for (int i = 0; i < resultList.size(); i++) {
				Map<String, Object> obj = resultList.get(i);
				Map<String, Object> n = new HashMap<String, Object>();
				n.put("id", String.valueOf(obj.get("id")));
				n.put("noticeTitle", String.valueOf(obj.get("notice_title")));
				n.put("noticeContent", String.valueOf(obj.get("notice_content")));
				n.put("createTime", String.valueOf(obj.get("create_time")));

				List<Map<String, Object>> readinfo = systemService.findForJdbc("select * from sys_notice_read_user where notice_id = ? and user_id = ? ", n.get("id"), user.getId());
				if (readinfo != null && readinfo.size() > 0) {
					n.put("isRead", "1"); // 已读
				} else {
					n.put("isRead", "0");// 未读
				}
				noticeList.add(n);
			}
		}

		dataGrid.setResults(noticeList);
		String sql2 = "";
		sql2 = sql2 + " SELECT count(*) AS count FROM  sys_notice t WHERE";
		sql2 = sql2 + " t.notice_level = '1' ";
		sql2 = sql2 + " OR (t.notice_level = '2' AND EXISTS (SELECT 1 FROM sys_notice_authority_role r,sys_role_user ru WHERE r.role_id = ru.roleid AND t.id = r.notice_id AND ru.userid = '" + user.getId() + "'))";
		sql2 = sql2 + " OR (t.notice_level = '3' AND EXISTS (SELECT 1 FROM sys_notice_authority_user u WHERE t.id = u.notice_id AND u.user_id = '" + user.getId() + "'))";
		List<Map<String, Object>> resultList2 = systemService.findForJdbc(sql2);
		Object count = resultList2.get(0).get("count");
		dataGrid.setTotal(Integer.valueOf(count.toString()));
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 阅读通知公告
	 * 
	 * @param user
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "updateNoticeRead")
	@ResponseBody
	public AjaxJson updateNoticeRead(String noticeId, HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		try {
			User user = ResourceUtil.getSessionUserName();
			TSNoticeReadUser readUser = new TSNoticeReadUser();
			readUser.setNoticeId(noticeId);
			readUser.setUserId(user.getId());
			readUser.setCreateTime(new Date());
			systemService.saveOrUpdate(readUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}

	/**
	 * 通知公告列表(管理) 页面跳转
	 * 
	 * 
	 * @return
	 */
	@RequestMapping(params = "tSNotice")
	public ModelAndView tSNotice(HttpServletRequest request) {
		return new ModelAndView("system/user/tSNoticeList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid2")
	public void datagrid2(Notice tSNotice, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(Notice.class, dataGrid);
		// 查询条件组装器
		cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tSNotice, request.getParameterMap());
		try {
			// 自定义追加查询条件
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.noticeService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除通知公告
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(Notice tSNotice, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		tSNotice = systemService.getEntity(Notice.class, tSNotice.getId());
		message = "通知公告删除成功";
		try {
			noticeService.delete(tSNotice);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "通知公告删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除通知公告
	 * 
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "通知公告删除成功";
		try {
			for (String id : ids.split(",")) {
				Notice tSNotice = systemService.getEntity(Notice.class, id);
				noticeService.delete(tSNotice);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "通知公告删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加通知公告
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(Notice tSNotice, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "通知公告添加成功";
		try {
			noticeService.save(tSNotice);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "通知公告添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 更新通知公告
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(Notice tSNotice, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "通知公告更新成功";
		Notice t = noticeService.get(Notice.class, tSNotice.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(tSNotice, t);
			noticeService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "通知公告更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 通知公告新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(Notice tSNotice, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(tSNotice.getId())) {
			tSNotice = noticeService.getEntity(Notice.class, tSNotice.getId());
			req.setAttribute("tSNoticePage", tSNotice);
		}
		return new ModelAndView("system/user/tSNotice-add");
	}

	/**
	 * 通知公告编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(Notice tSNotice, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(tSNotice.getId())) {
			tSNotice = noticeService.getEntity(Notice.class, tSNotice.getId());
			if (tSNotice.getNoticeTerm() == null) {
				tSNotice.setNoticeTerm(new Date());
			}
			req.setAttribute("tSNoticePage", tSNotice);
		}
		return new ModelAndView("system/user/tSNotice-update");
	}
}
