package cn.emay.framework.core.common.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import cn.emay.framework.common.utils.ExceptionUtil;

/**
 * spring mvc异常捕获类
 * 
 */
@Component
public class MyExceptionHandler implements HandlerExceptionResolver {

	private static final Logger logger = Logger
			.getLogger(MyExceptionHandler.class);

	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		String exceptionMessage = ExceptionUtil.getExceptionMessage(ex);
		logger.error(exceptionMessage);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("exceptionMessage", exceptionMessage);
		model.put("ex", ex);
		return new ModelAndView("common/error", model);
	}
}
