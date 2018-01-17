
package ${packageName}.${moduleName}.web${subModuleName};

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.emay.framework.common.utils.StringUtils;
import cn.emay.framework.core.common.controller.BaseController;
import cn.emay.framework.core.common.exception.BusinessException;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.framework.core.constant.Globals;
import ${packageName}.${moduleName}.entity.Payment;
import ${packageName}.${moduleName}.service.PaymentService;
import cn.emay.modules.sys.service.SystemService;



/**
 * ${functionName}Controller
 * @author ${classAuthor}
 * @version ${classVersion}
 */
@Controller
@RequestMapping(value = "/${urlPrefix}Controller")
public class ${ClassName}Controller extends BaseController {

	private Logger logger = Logger.getLogger(${ClassName}Controller.class);

	@Autowired
	private ${ClassName}Service ${className}Service;
	
	@Autowired
	private SystemService systemService;
	
	@ModelAttribute
	public ${ClassName} get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return ${className}Service.get(id);
		}else{
			return new ${ClassName}();
		}
	}
	
	@RequestMapping(params="list")
	public String list(${ClassName} ${className}, HttpServletRequest request, HttpServletResponse response, Model model) {
		return "${viewPrefix}List";
	}

	@RequestMapping(params = "form")
	public String form(${ClassName} ${className}, Model model) {
		model.addAttribute("${className}", ${className});
		return "${viewPrefix}Form";
	}

	@ResponseBody
	@RequestMapping(params = "save")
	public AjaxJson save(${ClassName} ${className}, Model model, RedirectAttributes redirectAttributes) {
		String message = null;
		AjaxJson ajaxJson = new AjaxJson();
		try{
			if(StringUtils.isNotBlank(payment.getId())){
				message="${functionName}保存成功";
			}else{
				message="${functionName}更新成功";
			}
			${className}Service.saveOrUpdate(${className});
			ajaxJson.setMsg(message);
		}catch (Exception e) {
			logger.error("${functionName}保存异常", e);
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("${functionName}保存失败");
		}
		return ajaxJson;
		
	}
	
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(String id, RedirectAttributes redirectAttributes) {
		String message = null;
		AjaxJson ajaxJson = new AjaxJson();
		message = "${functionName}删除成功";
		try {
			${className}Service.delete(id);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "${functionName}删除失败";
			throw new BusinessException(e.getMessage());
		}
		ajaxJson.setMsg(message);
		return ajaxJson;
	}

}
