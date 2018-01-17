package cn.emay.modules.map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 新时代地图控制器
 * @author lenovo
 *
 */
@Controller
@RequestMapping("/xsdMapController")
public class XsdMapController {

	@RequestMapping(params="wxLocation")
	public String wxLocation(HttpServletRequest request) {
		return "modules/map/xsd/wxLocation";
	}
}
