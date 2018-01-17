package cn.emay.modules.wx.web;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import weixin.popular.api.SnsAPI;
import weixin.popular.bean.sns.SnsToken;
import cn.emay.framework.common.utils.BrowserUtils;
import cn.emay.framework.common.utils.CacheUtils;
import cn.emay.framework.common.utils.DateUtils;
import cn.emay.framework.core.common.controller.BaseController;
import cn.emay.framework.core.common.exception.BusinessException;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.json.AjaxJson;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.constant.Globals;
import cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil;
import cn.emay.framework.tag.core.easyui.TagUtil;
import cn.emay.modules.sys.service.ParamsService;
import cn.emay.modules.sys.service.SystemService;
import cn.emay.modules.wx.entity.WxFans;
import cn.emay.modules.wx.entity.WxSurvey;
import cn.emay.modules.wx.entity.WxSurveyMain;
import cn.emay.modules.wx.entity.WxSurveyOption;
import cn.emay.modules.wx.entity.WxSurveyRecord;
import cn.emay.modules.wx.entity.WxSurveyRecordMember;
import cn.emay.modules.wx.entity.WxWechat;
import cn.emay.modules.wx.service.WxCacheService;
import cn.emay.modules.wx.service.WxFansService;
import cn.emay.modules.wx.service.WxSurveyMainService;
import cn.emay.modules.wx.service.WxSurveyService;


/**
 * 调查问卷
 * 
 * @author zjlWm
 * @date 2016-3-30
 */
@Controller
@RequestMapping("/weixinSurveyMainController")
public class WeixinSurveyMainController extends BaseController {

	/**
	 * 调查问卷主表
	 */
	@Autowired
	private WxSurveyMainService wxSurveyMainService;


	@Autowired
	private WxSurveyService wxSurveyService;

	@Autowired
	private WxCacheService wxCacheService;
	
	
	@Autowired
	private WxFansService wxFansService;
	
	@Autowired
	private SystemService systemService;

	/**
	 * 参数service接口
	 */
	@Autowired
	private ParamsService paramsService;
	
	
	/**
	 * 调研主题（分组）列表
	 * 
	 * @return
	 */
	@RequestMapping(params = "weixinSurveyMain")
	public ModelAndView weixinSurveyMain(HttpServletRequest request) {
		String domain=paramsService.findParamsByName("sys.domain");
		request.setAttribute("domain", domain);
		return new ModelAndView("modules/weixin/survey/mainList");
	}

	/**
	 * 调查问卷新增
	 * 
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	public ModelAndView weixinSurveyMainAdd(WxSurveyMain wxSurveyMain, HttpServletRequest req) {
		if (StringUtils.isNotBlank(wxSurveyMain.getId())) {
			wxSurveyMain = wxSurveyMainService.getEntity(WxSurveyMain.class, wxSurveyMain.getId());
			req.setAttribute("surveyMainPage", wxSurveyMain);
		}
		return new ModelAndView("modules/weixin/survey/mainForm");
	}

	/**
	 * 调查问卷查看
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "goReview")
	public ModelAndView goReview(String id, String code, String s_openid, HttpServletRequest request) {
		WxWechat wxWechat=wxCacheService.getWxWechat();
		String openId = "";
		String memberId = "";
		
		if (null == CacheUtils.get(code)) {
			SnsToken token = SnsAPI.oauth2AccessToken(wxWechat.getAppid(), wxWechat.getSecret(), code);
			if (StringUtils.isNotBlank(token.getOpenid())) {
				openId = token.getOpenid();
				Map<String, Object> params = new HashMap<String, Object>();
				WxFans wxFans=wxFansService.findWxFansByOpenid(openId);
				params.put("memberId", wxFans.getMemberId());
				params.put("openId", openId);
				CacheUtils.put(code, params);
			}
		} else {
			Map<String, Object> userInfo = (Map<String, Object>) CacheUtils.get(code);
			openId = userInfo.get("openId").toString();
			memberId = userInfo.get("memberId").toString();
		}

		if (StringUtils.isBlank(openId)) {
			request.setAttribute("message", "您好，您无法参与调查问卷，请联系管理人员！");
			return new ModelAndView("modules/weixin/survey/goReviewError");
		}
		
		// 验证请求过来的用户是否是真实用户
		// if(StringUtils.isNotBlank(openId) && openId.equals(s_openid)){
		String sql = "SELECT count(*) from weixin_survey_record t where t.mainId='" + id + "' and t.openid='" + openId + "'";
		Long count = wxSurveyMainService.getCountForJdbc(sql);
		if (count > 0) {
			return new ModelAndView("modules/weixin/survey/goReviewExist");
		} else {
			// 时间验证
			Date current = new Date();
			WxSurveyMain wxSurveyMain = wxSurveyMainService.get(WxSurveyMain.class, id);
			if (null != wxSurveyMain) {
				Date beginDate = wxSurveyMain.getBeginDate();
				Date endDate = wxSurveyMain.getEndDate();
				if (beginDate.getTime() > current.getTime()) {
					request.setAttribute("message", "抱歉，该问卷调查未开始，请等待！");
					return new ModelAndView("modules/weixin/survey/goReviewError");
				}
				if (endDate.getTime() < current.getTime()) {
					request.setAttribute("message", "抱歉，该问卷调查已经结束！");
					return new ModelAndView("modules/weixin/survey/goReviewError");
				}

				List<WxSurvey> wxSurveyList = wxSurveyMainService.findByQueryString("from WeixinSurvey where mainId='" + id + "' order by surveySort asc");
				for (WxSurvey wxSurvey : wxSurveyList) {
					List<WxSurveyOption> surveyOptionList = wxSurveyMainService.findByProperty(WxSurveyOption.class, "surveyId", wxSurvey.getId());
					wxSurvey.surveyOptionList = surveyOptionList;
				}
				request.setAttribute("weixinSurveyMain", wxSurveyMain);
				request.setAttribute("WeixinSurveyList", wxSurveyList);
				request.setAttribute("weixinSurveyCount", wxSurveyList.size());
				request.setAttribute("openid", openId);
				request.setAttribute("memberId", memberId);
				return new ModelAndView("modules/weixin/survey/goReview");
			} else {
				request.setAttribute("message", "您好，该问卷调查不存在！");
				return new ModelAndView("modules/weixin/survey/goReviewError");
			}

		}
		// }else{
		// request.setAttribute("message", "您好，请您重新进入调查问卷！");
		// return new ModelAndView("weixin/survey/goReviewError");
		// }
	}

	/**
	 * 不做认证
	 * 
	 * @param id
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "goReview2")
	public ModelAndView goReview2(String id, HttpServletRequest request) {
		WxSurveyMain wxSurveyMain = wxSurveyMainService.get(WxSurveyMain.class, id);
		List<WxSurvey> wxSurveyList = wxSurveyMainService.findByQueryString("from WeixinSurvey where mainId='" + id + "' order by surveySort asc");
		for (WxSurvey wxSurvey : wxSurveyList) {
			List<WxSurveyOption> surveyOptionList = wxSurveyMainService.findByProperty(WxSurveyOption.class, "surveyId", wxSurvey.getId());
			wxSurvey.surveyOptionList = surveyOptionList;
		}
		request.setAttribute("weixinSurveyMain", wxSurveyMain);
		request.setAttribute("WeixinSurveyList", wxSurveyList);
		request.setAttribute("weixinSurveyCount", wxSurveyList.size());
		return new ModelAndView("modules/weixin/survey/goReview");
	}

	/**
	 * 添加调研主题
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "weixinSurveyMaindoAdd")
	@ResponseBody
	public AjaxJson weixinSurveyMaindoAdd(WxSurveyMain wxSurveyMain, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String message = "调研主题添加成功";
		try {
			if (StringUtils.isNotBlank(wxSurveyMain.getId())) {
				message = "调研主题更新成功";
				wxSurveyMainService.updateEntitie(wxSurveyMain);
			} else {
				wxSurveyMainService.save(wxSurveyMain);
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
			}

		} catch (Exception e) {
			e.printStackTrace();
			if (StringUtils.isNotBlank(wxSurveyMain.getId())) {
				message = "调研主题更新失败";
			} else {
				message = "调研主题添加失败";
			}

			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "maindatagrid")
	public void datagrid(WxSurveyMain wxSurveyMain, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(WxSurveyMain.class, dataGrid);
		// 查询条件组装器
		HqlGenerateUtil.installHql(cq, wxSurveyMain, request.getParameterMap());
		try {
			// 自定义追加查询条件
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		wxSurveyMainService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除添加调研主题
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(WxSurveyMain wxSurveyMain, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		wxSurveyMain = systemService.getEntity(WxSurveyMain.class, wxSurveyMain.getId());
		String message = "调研主题删除成功";
		try {
			wxSurveyMainService.delete(wxSurveyMain);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "调研主题删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 调查问卷数据采集保存
	 * 
	 * @param openid
	 * @param mainid
	 * @param answerList  问题id_选项值（或手动输入）_问题选项id(或没有)
	 * 402881e652dac3b30152dacb47940003_挺好_fbb8d8e453c694540153c6bf09910016,fbb8d8e453c6331c0153c63b1d760012_不多呀_e4e488b956264b4c0156264c93630003,402881e556274544015627479a050001_苹果手机;三星手机_402881e556274544015627479a1a0002;402881e556274544015627479a2a0003,402881e556222fbc0156226dbd310007_uu,402881e556222fbc0156227a5de30009_北京_402881e556222fbc0156227a5e02000a
	 * @return
	 */
	@RequestMapping(params = "questionnaireSurvey")
	@ResponseBody
	public String questionnaireSurvey(String memberid, String openid, String mainid, String answerList) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if(StringUtils.isBlank(answerList)){
				return "fail";
			}
			String answerListNew=StringEscapeUtils.unescapeHtml(answerList);
			String answerArray[] = answerListNew.split(",");
			WxSurveyMain weixinSurveyMain = wxSurveyMainService.get(WxSurveyMain.class, mainid);
			int recordIndex = 0;
			for (String answerstr : answerArray) {
				String data[] = answerstr.split("_");
				String surveyId = ""; // 调查题目
				String answer = ""; // 调查答复
				String answerId = ""; // 选择答案id
				if (data.length < 2) {
					continue;
				}

				if (data.length == 2) {
					surveyId = data[0];
					answer = data[1];
				}

				if (data.length == 3) {
					surveyId = data[0];
					answer = data[1];
					answerId = data[2];
				}

				if (StringUtils.isBlank(surveyId)) {
					continue;
				}

				if (StringUtils.isBlank(answer)) {
					continue;
				}
				if (answer.length() > 200) {
					continue;
				}

				// String surveyId=data[0]; //调查题目
				// String answer=data[1]; //调查答复
				// String answerId=data[2]; //选择答案id

				WxSurveyRecord weixinSurveyRecord = new WxSurveyRecord();
				weixinSurveyRecord.setCreateDate(new Date());
				weixinSurveyRecord.setOpenid(openid);
				weixinSurveyRecord.setMemberid(memberid);
				weixinSurveyRecord.setMainId(mainid);
				weixinSurveyRecord.setSurveyId(surveyId);
				weixinSurveyRecord.setAnswer(answer);
				weixinSurveyRecord.setAnswerId(answerId);
				wxSurveyMainService.save(weixinSurveyRecord);

				recordIndex++;
			}

			if (recordIndex > 0) {
				/**
				 * 更新参与调研人次
				 */
				Long surveyCount = weixinSurveyMain.getSurveyCount();
				if (null != surveyCount) {
					weixinSurveyMain.setSurveyCount(surveyCount + 1);
				} else {
					weixinSurveyMain.setSurveyCount(1l);
				}
				wxSurveyMainService.saveOrUpdate(weixinSurveyMain);
				map.put("status", "OK");
			}

			return mapper.writeValueAsString(map);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	
	
	@RequestMapping(params = "questionnaireSurvey2")
	@ResponseBody
	public String questionnaireSurvey2(String memberid, String openid, String mainid, String answerList) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String answerArray[] = answerList.split(",");
			WxSurveyMain wxSurveyMain = wxSurveyMainService.get(WxSurveyMain.class, mainid);
			int recordIndex = 0;
			for (String answerstr : answerArray) {
				String data[] = answerstr.split("_");
				String surveyId = ""; // 调查题目
				String answer = ""; // 调查答复
				String answerId = ""; // 选择答案id
				if (data.length < 2) {
					continue;
				}

				if (data.length == 2) {
					surveyId = data[0];
					answer = data[1];
				}

				if (data.length == 3) {
					surveyId = data[0];
					answer = data[1];
					answerId = data[2];
				}

				if (StringUtils.isBlank(surveyId)) {
					continue;
				}

				if (StringUtils.isBlank(answer)) {
					continue;
				}
				if (answer.length() > 200) {
					continue;
				}

				// String surveyId=data[0]; //调查题目
				// String answer=data[1]; //调查答复
				// String answerId=data[2]; //选择答案id

				WxSurveyRecord weixinSurveyRecord = new WxSurveyRecord();
				weixinSurveyRecord.setCreateDate(new Date());
				weixinSurveyRecord.setOpenid(openid);
				weixinSurveyRecord.setMemberid(memberid);
				weixinSurveyRecord.setMainId(mainid);
				weixinSurveyRecord.setSurveyId(surveyId);
				weixinSurveyRecord.setAnswer(answer);
				weixinSurveyRecord.setAnswerId(answerId);
				wxSurveyMainService.save(weixinSurveyRecord);

				recordIndex++;
			}

			if (recordIndex > 0) {
				/**
				 * 更新参与调研人次
				 */
				Long surveyCount = wxSurveyMain.getSurveyCount();
				if (null != surveyCount) {
					wxSurveyMain.setSurveyCount(surveyCount + 1);
				} else {
					wxSurveyMain.setSurveyCount(1l);
				}
				wxSurveyMainService.saveOrUpdate(wxSurveyMain);
				map.put("status", "OK");
			}

			return mapper.writeValueAsString(map);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	
	/**
	 * 调查问卷发布
	 */
	@RequestMapping(params = "deploy")
	@ResponseBody
	public AjaxJson deploy(WxSurveyMain wxSurveyMain, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		wxSurveyMain = systemService.getEntity(WxSurveyMain.class, wxSurveyMain.getId());
		String message = "调查问卷发布成功";
		try {
			// ResourceBundle bundler = ResourceBundle.getBundle("sysConfig");
			// String url =
			// "http://wx.5dgz.com/jeewx/weixin/html.do?goPage2&page=questionnaire&mainId="+weixinSurveyMain.getId();
			// String accessToken = weixinAccountService.getAccessToken();
			// String pricUrl="http://wx.5dgz.com/jeewx";
			//
			// Media media=MediaAPI.mediaUpload(accessToken, MediaType.image,
			// new File("c:/1.jpg"));
			// String errcode=media.getErrcode();
			// if(null==errcode){
			// String media_id=media.getMedia_id();
			//
			// List<Article> articles=new ArrayList<Article>();
			// Article article=new Article();
			// article.setThumb_media_id(media_id);
			// articles.add(article);
			// MessageAPI.mediaUploadnews(accessToken, articles);
			//
			// MassMessage massMessage=new MassMPnewsMessage("");
			// MessageAPI.messageMassSendall(accessToken, massMessage);
			// }else{
			// j.setSuccess(false);
			// j.setMsg("调查问卷发布失败");
			// }
			//
		} catch (Exception e) {
			e.printStackTrace();
			message = "调查问卷推送成功失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 调查问卷 收集结果导出
	 * 
	 * @param weixinUser
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = { "exportXls2" })
	public void exportXls2(WxSurveyMain wxSurveyMain, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		response.setContentType("application/vnd.ms-excel");
		String codedFileName = null;
		OutputStream fOut = null;
		try {
			wxSurveyMain = systemService.getEntity(WxSurveyMain.class, wxSurveyMain.getId());
			codedFileName = wxSurveyMain.getSurveyTitle() + "【调查结果】";

			if (BrowserUtils.isIE(request)) {
				response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(codedFileName, "UTF-8") + ".xls");
			} else {
				String newtitle = new String(codedFileName.getBytes("UTF-8"), "ISO8859-1");
				response.setHeader("content-disposition", "attachment;filename=" + newtitle + ".xls");
			}

			HSSFWorkbook workbook = new HSSFWorkbook();
			excelPrint2(workbook, wxSurveyMain);

			fOut = response.getOutputStream();
			workbook.write(fOut);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fOut != null)
					fOut.flush();
				if (fOut != null)
					fOut.close();
			} catch (IOException localIOException1) {
			}
		}
	}

	/**
	 * 
	 * @param workbook
	 * @param weixinSurveyMain
	 * @return
	 */
	public HSSFWorkbook excelPrint2(HSSFWorkbook workbook, WxSurveyMain wxSurveyMain) {
		HSSFSheet sheet = workbook.createSheet();// 创建一个Excel的Sheet
		// 设置字体
		HSSFFont headfont = workbook.createFont();
		headfont.setFontName("黑体");
		headfont.setFontHeightInPoints((short) 22);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		// 另一个样式
		HSSFCellStyle headstyle = workbook.createCellStyle();
		headstyle.setFont(headfont);
		headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		headstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		headstyle.setLocked(true);
		headstyle.setWrapText(true);// 自动换行
		// 另一个字体样式
		HSSFFont columnHeadFont = workbook.createFont();
		columnHeadFont.setFontName("宋体");
		columnHeadFont.setFontHeightInPoints((short) 10);
		columnHeadFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		HSSFFont columnHeadFont2 = workbook.createFont();
		columnHeadFont2.setFontName("宋体");
		columnHeadFont2.setFontHeightInPoints((short) 10);

		// 列头的样式
		HSSFCellStyle columnHeadStyle = workbook.createCellStyle();
		columnHeadStyle.setFont(columnHeadFont);
		columnHeadStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		columnHeadStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		columnHeadStyle.setLocked(true);
		columnHeadStyle.setWrapText(false);

		HSSFCellStyle columnHeadStyle2 = workbook.createCellStyle();
		columnHeadStyle2.setFont(columnHeadFont2);
		columnHeadStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		columnHeadStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		columnHeadStyle2.setLocked(true);
		columnHeadStyle2.setWrapText(false);

		HSSFFont font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 10);
		// 普通单元格样式
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 左右居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);// 上下居中
		style.setWrapText(false);
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		style.setBorderLeft((short) 1);
		style.setRightBorderColor(HSSFColor.BLACK.index);
		style.setBorderRight((short) 1);
		// 另一个样式
		HSSFCellStyle centerstyle = workbook.createCellStyle();
		centerstyle.setFont(font);
		centerstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		centerstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		centerstyle.setWrapText(false);
		centerstyle.setLeftBorderColor(HSSFColor.BLACK.index);
		centerstyle.setBorderLeft((short) 1);
		centerstyle.setRightBorderColor(HSSFColor.BLACK.index);
		centerstyle.setBorderRight((short) 1);
		
		try {
			// 创建第一行
			HSSFRow row0 = sheet.createRow(0);
			// 设置行高
			row0.setHeight((short) 900);
			// 创建第一列
			HSSFCell cell0 = row0.createCell(0);
			cell0.setCellValue(new HSSFRichTextString(wxSurveyMain.getSurveyTitle()));
			cell0.setCellStyle(headstyle);
			/**
			 * 合并单元格 第一个参数：第一个单元格的行数（从0开始） 第二个参数：第二个单元格的行数（从0开始）
			 * 第三个参数：第一个单元格的列数（从0开始） 第四个参数：第二个单元格的列数（从0开始）
			 */
			CellRangeAddress range = new CellRangeAddress(0, 0, 0, 7);
			sheet.addMergedRegion(range);
			// 创建第二行
			HSSFRow row1 = sheet.createRow(1);
			HSSFCell cell1 = row1.createCell(0);
			cell1.setCellValue(new HSSFRichTextString("本次调查问卷开始时间：" + DateUtils.formatDate(wxSurveyMain.getBeginDate(), "yyyy-MM-dd") + "       结束时间：" + DateUtils.formatDate(wxSurveyMain.getBeginDate(), "yyyy-MM-dd")));
			cell1.setCellStyle(centerstyle);
			// 合并单元格
			range = new CellRangeAddress(1, 2, 0, 7);
			sheet.addMergedRegion(range);

			List<WxSurvey> weixinSurveyList = wxSurveyService.findByQueryString("from WeixinSurvey where mainId='" + wxSurveyMain.getId() + "' order by surveySort asc");
			int rowNum = 3;

			/**
			 * 问题横下展示
			 */
			System.out.println("问题横下展示-----start-------");
			HSSFRow rowSurvey = sheet.createRow(rowNum);// 问题横下排列
			for (int i = 0; i < weixinSurveyList.size(); i++) {
				WxSurvey weixinSurvey = weixinSurveyList.get(i);
				
				/**
				 * 问题列
				 */
				HSSFCell cellSurveryTitle = rowSurvey.createCell(i);
				cellSurveryTitle.setCellValue(new HSSFRichTextString(weixinSurvey.getSurveyTitle()));

				cellSurveryTitle.setCellStyle(columnHeadStyle);
				
				sheet.autoSizeColumn((short)i);
			}
			System.out.println("问题横下展示-----end-------");
			
			/**
			 * 问卷结果
			 */
			System.out.println("问卷结果-----start-------");
			List<Map<String, Object>> listOpenidMap=wxSurveyService.findForJdbc("SELECT DISTINCT t.openid FROM weixin_survey_record t WHERE t.mainId = '"+wxSurveyMain.getId()+"' ORDER BY t.createDate asc");
			for(int i=0;i<listOpenidMap.size();i++){
				HSSFRow rowSurveyResult = sheet.createRow(rowNum+1+i);// 问题横下排列
				String openid=listOpenidMap.get(i).get("openid").toString();
				String resultSql="SELECT DISTINCT "+
						"t.surveyId, "+
						"t.`answer`, "+
						"t.`memberid`, "+
						"t.`openid` "+
					"FROM "+
						"weixin_survey_record t,weixin_survey s "+
					"WHERE "+
						"t.mainId = '"+wxSurveyMain.getId()+"' "+
						"and t.surveyId=s.id "+
					"AND t.`openid` = '"+openid+"' "+
					"ORDER BY s.surveySort asc, "+
						"t.createDate ASC";
				List<Map<String, Object>>listResult=wxSurveyService.findForJdbc(resultSql);
				if(listResult.size()>0){
					for(int j=0;j<listResult.size();j++){
						String answer=listResult.get(j).get("answer").toString();
						/**
						 * 问题列
						 */
						HSSFCell cellSurveryRecord = rowSurveyResult.createCell(j);
						cellSurveryRecord.setCellValue(new HSSFRichTextString(answer));
						cellSurveryRecord.setCellStyle(columnHeadStyle);
					}
				}
				
				
				System.out.println("问卷结果查询-----index-------"+i);
				
				
			}
			System.out.println("问卷结果-----end-------");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return workbook;
	}

	/**
	 * 该导出exce格式 问题选项 排列 选项问题选下排列
	 * 
	 * @param workbook
	 * @param weixinSurveyMain
	 * @return
	 */
	public HSSFWorkbook excelPrint(HSSFWorkbook workbook, WxSurveyMain weixinSurveyMain) {
		HSSFSheet sheet = workbook.createSheet();// 创建一个Excel的Sheet
		/*
		 * sheet.createFreezePane(1, 3);// 冻结 // 设置列宽 sheet.setColumnWidth(0,
		 * 1000); sheet.setColumnWidth(1, 3500); sheet.setColumnWidth(2, 3500);
		 * sheet.setColumnWidth(3, 6500); sheet.setColumnWidth(4, 6500);
		 * sheet.setColumnWidth(5, 6500); sheet.setColumnWidth(6, 6500);
		 * sheet.setColumnWidth(7, 2500);
		 */

		// // Sheet样式
		// HSSFCellStyle sheetStyle = workbook.createCellStyle();
		// // 背景色的设定
		// sheetStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
		// // 前景色的设定
		// sheetStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		// // 填充模式
		// sheetStyle.setFillPattern(HSSFCellStyle.FINE_DOTS);
		// // 设置列的样式
		// for (int i = 0; i <= 14; i++) {
		// sheet.setDefaultColumnStyle((short) i, sheetStyle);
		// }

		// 设置字体
		HSSFFont headfont = workbook.createFont();
		headfont.setFontName("黑体");
		headfont.setFontHeightInPoints((short) 22);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		// 另一个样式
		HSSFCellStyle headstyle = workbook.createCellStyle();
		headstyle.setFont(headfont);
		headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		headstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		headstyle.setLocked(true);
		headstyle.setWrapText(true);// 自动换行
		// 另一个字体样式
		HSSFFont columnHeadFont = workbook.createFont();
		columnHeadFont.setFontName("宋体");
		columnHeadFont.setFontHeightInPoints((short) 10);
		columnHeadFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		HSSFFont columnHeadFont2 = workbook.createFont();
		columnHeadFont2.setFontName("宋体");
		columnHeadFont2.setFontHeightInPoints((short) 10);

		// 列头的样式
		HSSFCellStyle columnHeadStyle = workbook.createCellStyle();
		columnHeadStyle.setFont(columnHeadFont);
		columnHeadStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		columnHeadStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		columnHeadStyle.setLocked(true);
		columnHeadStyle.setWrapText(false);

		HSSFCellStyle columnHeadStyle2 = workbook.createCellStyle();
		columnHeadStyle2.setFont(columnHeadFont2);
		columnHeadStyle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		columnHeadStyle2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		columnHeadStyle2.setLocked(true);
		columnHeadStyle2.setWrapText(false);

		/*
		 * columnHeadStyle.setLeftBorderColor(HSSFColor.BLACK.index);// 左边框的颜色
		 * columnHeadStyle.setBorderLeft((short) 1);// 边框的大小
		 * columnHeadStyle.setRightBorderColor(HSSFColor.BLACK.index);// 右边框的颜色
		 * columnHeadStyle.setBorderRight((short) 1);// 边框的大小
		 * columnHeadStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //
		 * 设置单元格的边框为粗体
		 * columnHeadStyle.setBottomBorderColor(HSSFColor.BLACK.index); //
		 * 设置单元格的边框颜色
		 */
		// 设置单元格的背景颜色（单元格的样式会覆盖列或行的样式）
		// columnHeadStyle.setFillForegroundColor(HSSFColor.WHITE.index);

		HSSFFont font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 10);
		// 普通单元格样式
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);// 左右居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);// 上下居中
		style.setWrapText(false);
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		style.setBorderLeft((short) 1);
		style.setRightBorderColor(HSSFColor.BLACK.index);
		style.setBorderRight((short) 1);
		// style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单元格的边框为粗体
		// style.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．
		// style.setFillForegroundColor(HSSFColor.WHITE.index);// 设置单元格的背景颜色．
		// 另一个样式
		HSSFCellStyle centerstyle = workbook.createCellStyle();
		centerstyle.setFont(font);
		centerstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		centerstyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
		centerstyle.setWrapText(false);
		centerstyle.setLeftBorderColor(HSSFColor.BLACK.index);
		centerstyle.setBorderLeft((short) 1);
		centerstyle.setRightBorderColor(HSSFColor.BLACK.index);
		centerstyle.setBorderRight((short) 1);
		// centerstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //
		// 设置单元格的边框为粗体
		// centerstyle.setBottomBorderColor(HSSFColor.BLACK.index); //
		// 设置单元格的边框颜色．
		// centerstyle.setFillForegroundColor(HSSFColor.WHITE.index);//
		// 设置单元格的背景颜色．

		try {
			// 创建第一行
			HSSFRow row0 = sheet.createRow(0);
			// 设置行高
			row0.setHeight((short) 900);
			// 创建第一列
			HSSFCell cell0 = row0.createCell(0);
			cell0.setCellValue(new HSSFRichTextString(weixinSurveyMain.getSurveyTitle()));
			cell0.setCellStyle(headstyle);
			/**
			 * 合并单元格 第一个参数：第一个单元格的行数（从0开始） 第二个参数：第二个单元格的行数（从0开始）
			 * 第三个参数：第一个单元格的列数（从0开始） 第四个参数：第二个单元格的列数（从0开始）
			 */
			CellRangeAddress range = new CellRangeAddress(0, 0, 0, 7);
			sheet.addMergedRegion(range);
			// 创建第二行
			HSSFRow row1 = sheet.createRow(1);
			HSSFCell cell1 = row1.createCell(0);
			cell1.setCellValue(new HSSFRichTextString("本次调查问卷开始时间：" + DateUtils.formatDate(weixinSurveyMain.getBeginDate(), "yyyy-MM-dd") + "       结束时间：" + DateUtils.formatDate(weixinSurveyMain.getBeginDate(), "yyyy-MM-dd")));
			cell1.setCellStyle(centerstyle);
			// 合并单元格
			range = new CellRangeAddress(1, 2, 0, 7);
			sheet.addMergedRegion(range);

			List<WxSurvey> weixinSurveyList = wxSurveyService.findByProperty(WxSurvey.class, "mainId", weixinSurveyMain.getId());
			int rowNum = 3;
			for (int i = 0; i < weixinSurveyList.size(); i++) {
				WxSurvey weixinSurvey = weixinSurveyList.get(i);
				HSSFRow row_ = sheet.createRow(rowNum);
				HSSFRow row_data = sheet.createRow(rowNum + 1);
				row_.setHeight((short) 300);
				row_data.setHeight((short) 300);

				HSSFCell cell_0 = row_.createCell(0);
				cell_0.setCellValue(new HSSFRichTextString((i + 1) + ""));
				cell_0.setCellStyle(columnHeadStyle);

				HSSFCell cell_ = row_.createCell(1);
				cell_.setCellValue(new HSSFRichTextString(weixinSurvey.getSurveyTitle()));
				cell_.setCellStyle(columnHeadStyle);
				// 1单选、2多选、3填空、4下拉
				String surveyType = weixinSurvey.getSurveyType();
				if (surveyType.equals("3")) {
					// weixinSurveyService.findByQueryString(hql);
					List<WxSurveyRecord> weixinSurveyRecordList = wxSurveyService.findByQueryString("from WeixinSurveyRecord where surveyId='" + weixinSurvey.getId() + "'");
					for (int j = 0; j < weixinSurveyRecordList.size(); j++) {
						WxSurveyRecord record = weixinSurveyRecordList.get(j);
						cell_ = row_.createCell(j + 2);
						cell_.setCellValue(new HSSFRichTextString(record.getAnswer()));
						cell_.setCellStyle(columnHeadStyle2);
					}
				} else {
					List<WxSurveyOption> surveyOptionList = wxSurveyService.findByProperty(WxSurveyOption.class, "surveyId", weixinSurvey.getId());
					for (int j = 0; j < surveyOptionList.size(); j++) {
						WxSurveyOption weixinSurveyOption = surveyOptionList.get(j);
						cell_ = row_.createCell(j + 2);
						cell_.setCellValue(new HSSFRichTextString(weixinSurveyOption.getOptionName()));
						cell_.setCellStyle(columnHeadStyle2);

						HSSFCell cell_data = row_data.createCell(j + 2);
						// 问题id
						String surveyId = weixinSurvey.getId();
						// 选项id
						String surveyOptionId = weixinSurveyOption.getId();
						String sql = "SELECT count(*) from weixin_survey_record t where `surveyId` ='" + surveyId + "' and `answerId` like '%" + surveyOptionId + "%'";
						Long count = wxSurveyService.getCountForJdbc(sql);
						cell_data.setCellValue(new HSSFRichTextString(count + ""));
						cell_data.setCellStyle(columnHeadStyle2);
					}
				}

				rowNum += 4;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return workbook;
	}

	/**
	 * 调查问卷 积分插入积分流水库 MKMS_ID 编号 MKMS_MCODE 卡号 MKMS_TYPE 类别 MKMS_CYC 周期 MKMS_BV
	 * 产生积分 MKMS_TEXT 备注 MKMS_USRCD 操作者 MKMS_LUPDT 操作时间 MKMS_TYPE 类别 这一列调查问卷的话
	 * 用值 LOV
	 */
	@RequestMapping(params = "addIntegralLog")
	@ResponseBody
	public AjaxJson addIntegralLog(WxSurveyMain weixinSurveyMain, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String message = "积分流水插入成功";
		try {
			weixinSurveyMain = systemService.getEntity(WxSurveyMain.class, weixinSurveyMain.getId());
			Long integral = weixinSurveyMain.getIntegral();
			String sqlcout = "select count(*) from weixin_survey_record_memberid  where `mainId` ='" + weixinSurveyMain.getId() + "'";
			String sqlrow = "select * from weixin_survey_record_memberid  where `mainId` ='" + weixinSurveyMain.getId() + "'";
			Long count = systemService.getCountForJdbc(sqlcout);
			Long defautPageSize = 1l;
			if (count > 1000) {
				Long pageSize = count / 1000;
				Long d = count % 1000;
				if (d > 0) {
					defautPageSize = pageSize + 1;
				}
			}

			for (int i = 0; i < defautPageSize; i++) {
				List<Map<String, Object>> mapList = systemService.findForJdbc(sqlrow, i + 1, 1000);
				for (Map<String, Object> map : mapList) {
					Map<String, Object> params = new HashMap<String, Object>();
					String memberid = map.get("memberid").toString();
					String mainId = map.get("mainId").toString();
					Calendar calendar = Calendar.getInstance();
					int day = calendar.get(Calendar.DATE);
					if (day <= 14) {
						calendar.add(Calendar.MONTH, -1);
					}
					Date date = calendar.getTime();
					String mkms_cyc = DateUtils.formatDate(date, "yyyyMM");
					params.put("mkms_mcode", memberid);
					params.put("mkms_cyc", mkms_cyc);
					params.put("mkms_text", "%" + mainId + "%");
					Long mkms_cout = 1l;//rsvh_FilMapper.findmkmscout(params);

					if (mkms_cout > 0) {
						System.out.println(mkms_cout);
					} else {
						Map<String, Object> insertParams = new HashMap<String, Object>();
						insertParams.put("mkms_mcode", memberid);
						insertParams.put("mkms_cyc", mkms_cyc);
						insertParams.put("mkms_bv", integral);// 积分
						insertParams.put("mkms_text", "mianId=" + weixinSurveyMain.getId() + ",memberid=" + memberid);

//						rsvh_FilMapper.insertMkms(insertParams);
						
						
					}

				}
			}

			j.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			message = "积分流水插入失败";
			j.setSuccess(false);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 调查问卷会员记录导出
	 * 
	 * @param weixinUser
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = { "myExportXlsMember" })
	public void myExportXlsMember(WxSurveyRecordMember weixinSurveyRecordMember, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		
	}
}
