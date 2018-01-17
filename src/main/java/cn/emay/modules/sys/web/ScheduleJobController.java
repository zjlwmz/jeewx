package cn.emay.modules.sys.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.quartz.CronExpression;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.emay.framework.common.quartz.SchedulerMananger;
import cn.emay.framework.common.utils.ClassUtils;
import cn.emay.framework.common.utils.StringUtils;
import cn.emay.framework.core.common.exception.BusinessException;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.common.model.json.DataGridReturn;
import cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil;
import cn.emay.framework.tag.core.easyui.TagUtil;
import cn.emay.modules.sys.entity.ScheduleJob;
import cn.emay.modules.sys.service.ScheduleJobService;


/**
 * 定时任务控制器
 * @author lenovo
 *
 */
@Controller
@RequestMapping("/scheduleJobController")
public class ScheduleJobController {
	
	private static final Logger logger = Logger.getLogger(ScheduleJobController.class);

	/**
	 * 定时任务service接口
	 */
	@Autowired
	private ScheduleJobService scheduleJobService;
	
	/**
	 * 任务管理
	 */
	@Autowired
	private SchedulerMananger schedulerMananger;
	
	
	@ModelAttribute("scheduleJob")
	public ScheduleJob get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return scheduleJobService.get(id);
		}else{
			return new ScheduleJob();
		}
	}
	
	
	/**
	 * 任务调度列表
	 * @param pageable
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(params="list")
	public String list(){
		return "modules/sys/timetask/timeTaskList";
	}
	
	
	/**
	 * 定时任务easyuiAJAX用户列表请求数据 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(ScheduleJob scheduleJob,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid){
		CriteriaQuery cq = new CriteriaQuery(ScheduleJob.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, scheduleJob, request.getParameterMap());
		try{
			//自定义追加查询条件
			
			cq.add();
			DataGridReturn dataGridReturn =scheduleJobService.getDataGridReturn(cq, true);
			@SuppressWarnings("unchecked")
			List<ScheduleJob>list=dataGridReturn.getRows();
			for(ScheduleJob job:list){
				schedulerMananger.getScheduleJob(job);
			}
			TagUtil.datagrid(response, dataGrid);
		}catch (Exception e) {
			logger.error("定时任务easyuiAJAX", e);
			throw new BusinessException(e.getMessage());
		}
		
		  
	}
	
	
	
	/**
	 * 任务调度添加页面
	 */
	@RequestMapping(params="form")
	public String form(String id,HttpServletRequest request, org.springframework.ui.Model modelMap){
		if(null!=id){
			ScheduleJob ScheduleJob=scheduleJobService.get(id);
			modelMap.addAttribute("timeTaskPage", ScheduleJob);
		}
		return "modules/sys/timetask/timeTaskForm";
	}
	
	
	
	/**
	 * 任务调度保存
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(ScheduleJob scheduleJob,HttpServletRequest request, HttpServletResponse response){
		String message = null;
		AjaxJson ajaxjson = new AjaxJson();
		try{
			boolean result=CronExpression.isValidExpression(scheduleJob.getCron());
			if(result){
				if(!ClassUtils.isValidClass(scheduleJob.getJobClass())){
					message="任务执行模板不存在";
					ajaxjson.setSuccess(false);
				}else{
					scheduleJobService.save(scheduleJob);
					message="任务调度保存成功";
					ajaxjson.setSuccess(true);
				}
			}else{
				message="时间表达式错误";
				ajaxjson.setSuccess(false);
			}
		}catch (Exception e) {
			logger.error("任务调度保存异常", e);
			message="任务调度保存异常";
			ajaxjson.setSuccess(false);
		}
		ajaxjson.setMsg(message);
		return ajaxjson;
	}
	
	
	/**
     * 任务关闭
     * @return
     */
    @RequestMapping(params="stopTask")
    @ResponseBody
    public AjaxJson stopTask(String id,HttpServletRequest request){
    	String message = "任务停止成功";
		AjaxJson ajax = new AjaxJson();
    	try {
    		if(StringUtils.isNotBlank(id)){
    			ScheduleJob scheduleJob=scheduleJobService.get(id);
				if(null!=scheduleJob){
					scheduleJobService.closeTask(scheduleJob);
					schedulerMananger.suspendTask(scheduleJob);
					ajax.setSuccess(true);
				}else{
					message="任务停止失败";
					ajax.setSuccess(false);
				}
    		}
		} catch (SchedulerException e) {
			ajax.setSuccess(false);
			message="任务停止失败";
			logger.error("任务停止", e);
		}
    	ajax.setMsg(message);
    	return ajax;
    }
    
    
    
    /**
     * 任务开启
     * @return
     */
    @RequestMapping(params="startTask")
    @ResponseBody
    public AjaxJson startTask(String id,HttpServletRequest request){
    	String message = "任务开始成功";
		AjaxJson ajax = new AjaxJson();
    	try {
    		if(StringUtils.isNotBlank(id)){
    			ScheduleJob scheduleJob=scheduleJobService.get(id);
				if(null!=scheduleJob){
					scheduleJobService.startTask(scheduleJob);
					schedulerMananger.openTaskSchedule(scheduleJob);
					ajax.setSuccess(true);
				}else{
					message="任务开始失败";
					ajax.setSuccess(false);
				}
    		}
		} catch (SchedulerException e) {
			ajax.setSuccess(false);
			message="任务开始失败";
			logger.error("任务开始失败", e);
		}
    	ajax.setMsg(message);
    	return ajax;
    }
    
    
    /**
     * 立即运行任务一次
     * @return
     */
    @RequestMapping(params="runingTask")
    @ResponseBody
    public AjaxJson runingTask(String id,HttpServletRequest request){
    	String message = null;
		AjaxJson ajax = new AjaxJson();
		message = "任务运行成功";
    	try {
    		if(StringUtils.isNotBlank(id)){
    			ScheduleJob scheduleJob=scheduleJobService.get(id);
				if(null!=scheduleJob){
					schedulerMananger.runthetask(scheduleJob);
					ajax.setSuccess(true);
				}else{
					message = "立即运行任务一次失败";
					ajax.setSuccess(false);
				}
    		}
		} catch (SchedulerException e) {
			ajax.setSuccess(false);
			message = "立即运行任务一次失败";
			logger.error("立即运行任务一次", e);
		}
    	ajax.setMsg(message);
    	return ajax;
    }
    
    
    /**
     * 任务删除
     * @param scheduleJob
     * @return
     */
    @RequestMapping(params="delete")
    @ResponseBody
    public AjaxJson delete(String id,HttpServletRequest request){
    	String message = null;
		AjaxJson j = new AjaxJson();
		message = "任务删除成功";
    	try{
    		if(StringUtils.isNotBlank(id)){
    			ScheduleJob scheduleJob=scheduleJobService.get(id);
				if(null!=scheduleJob){
					schedulerMananger.deleTask(scheduleJob);
					scheduleJobService.delete(scheduleJob);
				}else{
					message = "没有可删除数据";
	        		j.setSuccess(false);
				}
    		}else{
    			message = "没有可删除数据";
        		j.setSuccess(false);
    		}
    	}catch (Exception e) {
    		message = "任务删除失败";
    		j.setSuccess(false);
			logger.error("任务删除", e);
		}
    	j.setMsg(message);
    	return j;
    }
}
