package cn.emay.modules.sys.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.emay.framework.common.utils.DateUtils;
import cn.emay.framework.common.utils.MyClassLoader;
import cn.emay.framework.common.utils.StringUtil;
import cn.emay.framework.common.utils.oConvertUtils;
import cn.emay.framework.core.common.model.common.UploadFile;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.modules.sys.entity.Document;
import cn.emay.modules.sys.entity.Type;
import cn.emay.modules.sys.entity.Typegroup;
import cn.emay.modules.sys.service.SystemService;

/**
 * 
 * @Title 图片上传控制器
 * @author zjlwm
 * @date 2017-2-9 上午11:12:44
 * 
 */
@Controller
@RequestMapping("/imageController")
public class ImageController {

	@Autowired
	private SystemService systemService;

	/**
	 * 保存文件信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "upload", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson upload(MultipartHttpServletRequest request, HttpServletResponse response) {
		AjaxJson ajaxJson = new AjaxJson();
		Map<String, Object> attributes = new HashMap<String, Object>();
		Typegroup typegroup = systemService.getTypeGroup("fieltype", "文档分类");
		Type tsType = systemService.getType("files", "附件", typegroup);
		String fileKey = oConvertUtils.getString(request.getParameter("fileKey"));// 文件ID
		String documentTitle = oConvertUtils.getString(request.getParameter("documentTitle"));// 文件标题
		Document document = new Document();
		if (StringUtil.isNotEmpty(fileKey)) {
			document.setId(fileKey);
			document = systemService.getEntity(Document.class, fileKey);
			document.setDocumentTitle(documentTitle);

		}
		document.setSubclassname(MyClassLoader.getPackPath(document));
		document.setCreatedate(DateUtils.getTimestamp());
		document.setType(tsType);
		UploadFile uploadFile = new UploadFile(request, document);
		uploadFile.setCusPath("files");
		uploadFile.setSwfpath("swfpath");
		document = systemService.uploadFile(uploadFile);
		attributes.put("url", document.getRealpath());
		attributes.put("fileKey", document.getId());
		attributes.put("name", document.getAttachmenttitle());
		attributes.put("viewhref", "commonController.do?openViewFile&fileid=" + document.getId());
		attributes.put("delurl", "commonController.do?delObjFile&fileKey=" + document.getId());
		ajaxJson.setMsg("文件添加成功");
		ajaxJson.setAttributes(attributes);

		return ajaxJson;
	}

}
