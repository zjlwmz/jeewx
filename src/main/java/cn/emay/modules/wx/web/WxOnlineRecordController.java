package cn.emay.modules.wx.web;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.emay.framework.common.persistence.Page;
import cn.emay.framework.common.utils.DateUtils;
import cn.emay.framework.common.utils.FileUtils;
import cn.emay.framework.common.utils.IdGen;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.model.json.DataGrid;
import cn.emay.framework.core.extend.hqlsearch.HqlGenerateUtil;
import cn.emay.framework.tag.core.easyui.TagUtil;
import cn.emay.framework.tag.vo.datatable.SortDirection;
import cn.emay.modules.chat.dto.MessageImageDto;
import cn.emay.modules.chat.entity.ChatFriend;
import cn.emay.modules.chat.entity.ChatMyGroup;
import cn.emay.modules.chat.service.ChatFriendService;
import cn.emay.modules.chat.service.ChatMyGroupService;
import cn.emay.modules.sys.entity.User;
import cn.emay.modules.sys.service.ParamsService;
import cn.emay.modules.sys.service.UserService;
import cn.emay.modules.sys.utils.UserCacheUtils;
import cn.emay.modules.wx.dto.chat.ChatUserDTO;
import cn.emay.modules.wx.dto.im.MyImDataDto;
import cn.emay.modules.wx.dto.im.MyImDto;
import cn.emay.modules.wx.dto.im.MyImFriendDto;
import cn.emay.modules.wx.dto.im.MyImFriendUserInfoDto;
import cn.emay.modules.wx.dto.im.MyImGroupDto;
import cn.emay.modules.wx.dto.im.MyImMineDto;
import cn.emay.modules.wx.dto.members.MembersDataDto;
import cn.emay.modules.wx.dto.members.MembersDto;
import cn.emay.modules.wx.dto.members.MembersMemberDto;
import cn.emay.modules.wx.dto.members.MembersOwnerDto;
import cn.emay.modules.wx.dto.wechat.WechatDTOBase;
import cn.emay.modules.wx.entity.WxChatLogs;
import cn.emay.modules.wx.entity.WxFans;
import cn.emay.modules.wx.entity.WxOnlineRecord;
import cn.emay.modules.wx.service.WxChatLogsService;
import cn.emay.modules.wx.service.WxFansService;
import cn.emay.modules.wx.service.WxOnlineRecordService;

/**
 * 在线客服控制器
 * 
 * @author lenovo
 * 
 */
@Controller
@RequestMapping("/wxOnlineRecordController")
public class WxOnlineRecordController {

	/**
	 * 聊天记录接口
	 */
	@Autowired
	private WxChatLogsService wxChatLogsService;

	/**
	 * 粉丝接口
	 */
	@Autowired
	private WxFansService wxFansService;

	/**
	 * 我的分组service接口
	 */
	@Autowired
	private ChatMyGroupService chatMyGroupService;

	/**
	 * 好友servce接口
	 */
	@Autowired
	private ChatFriendService chatFriendService;

	/**
	 * 参数service接口
	 */
	@Autowired
	private ParamsService paramsService;

	/**
	 * 用户service接口
	 */
	@Autowired
	private UserService userService;

	
	/**
	 * 
	 * 在线客服记录单接口服务
	 *
	 */
	@Autowired
	private WxOnlineRecordService wxOnlineRecordService;
	
	
	/**
	 * 客服记录列表
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list() {
		return new ModelAndView("modules/weixin/wxOnlineRecord/wxOnlineRecordList");
	}
	
	
	
	
	/**
	 * 查询信息列表
	 * @param textTemplate
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	@ResponseBody
	public void datagrid(WxOnlineRecord wxOnlineRecord, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(WxOnlineRecord.class, dataGrid);
		User user=UserCacheUtils.getCurrentUser();
		wxOnlineRecord.setUserId(user.getId());
		HqlGenerateUtil.installHql(cq, wxOnlineRecord);
		wxOnlineRecordService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	
	
	
	/**
	 * 获取我的im信息
	 */
	@RequestMapping(value = "im/getList")
	@ResponseBody
	public MyImDto getMyImList() {

		MyImDto myImDto = new MyImDto();
		try {
			myImDto.setCode("0");
			myImDto.setMsg("");
			MyImDataDto data = new MyImDataDto();
			/**
			 * 我的信息
			 */
			MyImMineDto mine = new MyImMineDto();

			User user = UserCacheUtils.getCurrentUser();
			/**
			 * 创建默认分组
			 */
			chatMyGroupService.createDefaultChatMyGroupByUserId(user.getId());

			mine.setAvatar(user.getAvatar());
			mine.setId(user.getId());
			mine.setSign(user.getMySignature());
			mine.setStatus("online");
			mine.setUsername(user.getUserName());
			data.setMine(mine);

			/**
			 * 好友列表
			 */
			List<MyImFriendDto> friendList = new ArrayList<MyImFriendDto>();// 分组列表
			List<ChatMyGroup> chatMyGroupList = chatMyGroupService.findChatMyGroupByUserId(user.getId());
			for (ChatMyGroup chatMyGroup : chatMyGroupList) {

				MyImFriendDto friend = new MyImFriendDto();
				friend.setGroupname(chatMyGroup.getName());
				friend.setId(chatMyGroup.getId());
				/**
				 * 分组下面的好友列表
				 */
				List<MyImFriendUserInfoDto> list = new ArrayList<MyImFriendUserInfoDto>();
				List<ChatFriend> chatFriendList = chatFriendService.findChatFriendByGroupIdAndUserId(chatMyGroup.getId(), user.getId());
				for (ChatFriend chatFriend : chatFriendList) {
					MyImFriendUserInfoDto myImFriendUserInfoDto = new MyImFriendUserInfoDto();
					String openid = chatFriend.getOpenid();// 申请人
					WxFans wxFans = wxFansService.findWxFansByOpenid(openid);
					myImFriendUserInfoDto.setId(openid);
					if (null != wxFans) {
						myImFriendUserInfoDto.setUsername(wxFans.getNickname());
						myImFriendUserInfoDto.setAvatar(wxFans.getHeadimgurl());
						myImFriendUserInfoDto.setSign(wxFans.getNickname());
					}
					list.add(myImFriendUserInfoDto);
				}

				friend.setList(list);

				friendList.add(friend);

			}

			data.setFriend(friendList);

			/**
			 * 客服群组信息
			 */
			List<MyImGroupDto> group = new ArrayList<MyImGroupDto>();
			MyImGroupDto myImGroupDto = new MyImGroupDto();
			// 客服群名称
			String groupName = paramsService.findParamsByName("wx.cus.im_group_name");
			myImGroupDto.setGroupname(groupName);
			// 客服群id
			String groupId = paramsService.findParamsByName("wx.cus.im_group_id");
			myImGroupDto.setId(groupId);
			// 客服群头像
			String groupAvatar = paramsService.findParamsByName("wx.cus.im_group_avatar");
			myImGroupDto.setAvatar(groupAvatar);

			group.add(myImGroupDto);
			data.setGroup(group);

			myImDto.setData(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return myImDto;
	}

	/**
	 * 群组成员
	 */
	@RequestMapping(value = "im/getMembers")
	@ResponseBody
	public MembersDto getMembers() {
		MembersDto membersDto = new MembersDto();
		try {
			membersDto.setCode("0");
			membersDto.setMsg("");
			MembersDataDto data = new MembersDataDto();
			/**
			 * 群信息
			 */
			// 客服群 群主名称
			String groupOwnerName = paramsService.findParamsByName("wx.cus.im_group_owner_name");
			// 客服群 群主id
			String groupOwnerId = paramsService.findParamsByName("wx.cus.im_group_owner_id");
			// 客服群 群主头像
			String groupOwnerAvatar = paramsService.findParamsByName("wx.cus.im_group_owner_avatar");
			// 客服群 签名
			String groupOwnerSign = paramsService.findParamsByName("wx.cus.im_group_owner_sign");

			MembersOwnerDto membersOwnerDto = new MembersOwnerDto();
			membersOwnerDto.setUsername(groupOwnerName);
			membersOwnerDto.setId(groupOwnerId);
			membersOwnerDto.setAvatar(groupOwnerAvatar);
			membersOwnerDto.setSign(groupOwnerSign);

			data.setMembersOwnerDto(membersOwnerDto);

			String roleId = paramsService.findParamsByName("wx.role.id");
			List<User> userList = userService.findUserByRole(roleId);
			/**
			 * 群成员信息
			 */
			List<MembersMemberDto> list = new ArrayList<MembersMemberDto>();
			for (User user : userList) {

				MembersMemberDto membersMemberDto = new MembersMemberDto();
				membersMemberDto.setUsername(user.getRealName());
				membersMemberDto.setId(user.getId());
				membersMemberDto.setAvatar(user.getAvatar());
				membersMemberDto.setSign(user.getMySignature());
				list.add(membersMemberDto);
			}
			data.setList(list);
			membersDto.setData(data);
		} catch (Exception e) {

		}
		return membersDto;
	}

	/**
	 * 查找好友页面
	 * 
	 * @param modelMap
	 * @param id
	 *            好友用户id
	 * @param type
	 *            类型 friend朋友、group 群组
	 * @return
	 */
	@RequestMapping(value = "im/find")
	public ModelAndView chaFindview(ModelMap modelMap, String id, String type) {
		modelMap.put("userid", id);
		modelMap.put("type", type);
		return new ModelAndView("modules/chat/find");
	}

	@RequestMapping(value = "im/findUserList")
	@ResponseBody
	public Page<ChatUserDTO> findUserList() {
		// int pageSize=50;
		Page<ChatUserDTO> pageMap = new Page<ChatUserDTO>();

		List<ChatUserDTO> ChatUserDTOList = new ArrayList<ChatUserDTO>();
		ChatUserDTO chatUserDTO = new ChatUserDTO();
		chatUserDTO.setUsername("等待等待");
		chatUserDTO.setAvatar("http://qzapp.qlogo.cn/qzapp/101328493/9AA18976AE9F6E374F02E1B5A434BE18/100");
		chatUserDTO.setSex("女");
		chatUserDTO.setRemark("");
		chatUserDTO.setId("13");
		chatUserDTO.setStatus("online");
		ChatUserDTOList.add(chatUserDTO);

		chatUserDTO = new ChatUserDTO();
		chatUserDTO.setUsername("呐喊");
		chatUserDTO.setAvatar("http://qzapp.qlogo.cn/qzapp/101328493/6422DE88E53540CF5BDFBA89D0D939F5/100");
		chatUserDTO.setSex("女");
		chatUserDTO.setRemark("");
		chatUserDTO.setId("14");
		chatUserDTO.setStatus("online");
		ChatUserDTOList.add(chatUserDTO);

		chatUserDTO = new ChatUserDTO();
		chatUserDTO.setUsername("等待等待");
		chatUserDTO.setAvatar("http://qzapp.qlogo.cn/qzapp/101328493/9AA18976AE9F6E374F02E1B5A434BE18/100");
		chatUserDTO.setSex("女");
		chatUserDTO.setRemark("");
		chatUserDTO.setId("15");
		chatUserDTO.setStatus("online");
		ChatUserDTOList.add(chatUserDTO);

		chatUserDTO = new ChatUserDTO();
		chatUserDTO.setUsername("等待等待");
		chatUserDTO.setAvatar("http://qzapp.qlogo.cn/qzapp/101328493/9AA18976AE9F6E374F02E1B5A434BE18/100");
		chatUserDTO.setSex("女");
		chatUserDTO.setRemark("");
		chatUserDTO.setId("16");
		chatUserDTO.setStatus("online");
		ChatUserDTOList.add(chatUserDTO);

		chatUserDTO = new ChatUserDTO();
		chatUserDTO.setUsername("等待等待");
		chatUserDTO.setAvatar("http://qzapp.qlogo.cn/qzapp/101328493/9AA18976AE9F6E374F02E1B5A434BE18/100");
		chatUserDTO.setSex("女");
		chatUserDTO.setRemark("");
		chatUserDTO.setId("17");
		chatUserDTO.setStatus("online");
		ChatUserDTOList.add(chatUserDTO);

		pageMap.setList(ChatUserDTOList);
		pageMap.setCount(10);
		return pageMap;
	}

	/**
	 * 聊天记录页面
	 * 
	 * @param modelMap
	 * @param id
	 *            好友用户id
	 * @param type
	 *            类型 friend朋友、group 群组
	 * @return
	 */
	@RequestMapping(value = "im/chatlogview")
	public ModelAndView chatlogview(ModelMap modelMap, String id, String type) {
		modelMap.put("userid", id);
		modelMap.put("type", type);
		return new ModelAndView("modules/weixin/wxonline/chatLog");
	}

	/**
	 * 聊天记录
	 * 
	 * @param id
	 *            好友用户id
	 * @param type
	 *            类型 friend朋友、group 群组
	 * @param page
	 *            分页数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "im/chatlog")
	@ResponseBody
	public Page<WechatDTOBase> chatlog(String id, String type, Integer page) {
		int pageSize = 50;
		Page<WechatDTOBase> pageMap = new Page<WechatDTOBase>();
		DataGrid dataGrid = new DataGrid();
		CriteriaQuery criteriaQuery = new CriteriaQuery(WxChatLogs.class, dataGrid);
		// 群组
		if (type.equals("group")) {

		}
		// 朋友
		else if (type.equals("friend")) {
			criteriaQuery.setCurPage(page);
			criteriaQuery.setPageSize(pageSize);
			criteriaQuery.eq("type", "friend");// 朋友
			criteriaQuery.add(Restrictions.or(Restrictions.eq("toUserName", id), Restrictions.eq("fromUserName", id)));
			criteriaQuery.addOrder("createDate", SortDirection.desc);
			criteriaQuery.add();
			wxChatLogsService.findWxChatPage(criteriaQuery);
		}

		/**
		 * 当前用户
		 */
		User user = UserCacheUtils.getCurrentUser();

		DataGrid resultDataGrid = criteriaQuery.getDataGrid();
		List<WechatDTOBase> wechatDTOBaseList = new ArrayList<WechatDTOBase>();
		List<WxChatLogs> resultWxChat = resultDataGrid.getResults();
		for (WxChatLogs wxChat : resultWxChat) {
			WechatDTOBase wechatDTOBase = new WechatDTOBase();
			wechatDTOBase.setType("0");
			wechatDTOBase.setMessageType(wxChat.getMessageType());
			if (wxChat.getMessageType().equals("text")) {
				wechatDTOBase.setContent(wxChat.getContent());
			} else if (wxChat.getMessageType().equals("image")) {
				wechatDTOBase.setPicUrl(wxChat.getPicUrl());
				wechatDTOBase.setContent(wechatDTOBase.getPicUrl());
			} else if (wxChat.getMessageType().equals("voice")) {
				wechatDTOBase.setVoiceUrl(wxChat.getVoiceUrl());
				wechatDTOBase.setContent(wxChat.getVoiceUrl());
			} else if (wxChat.getMessageType().equals("shortvideo")) {
				wechatDTOBase.setShortVideoUrl(wxChat.getShortVideoUrl());
				wechatDTOBase.setContent(wxChat.getShortVideoUrl());
			} else if (wxChat.getMessageType().equals("video")) {
				wechatDTOBase.setVideoUrl(wxChat.getVideoUrl());
				wechatDTOBase.setContent(wxChat.getVideoUrl());
			}

			boolean mine = wxChat.isMine();
			wechatDTOBase.setMine(mine);
			// 是否我发送的消息，如果为true，则会显示在右方
			if (mine) {
				wechatDTOBase.setUserName(user.getUserName());
				wechatDTOBase.setAvatar("http://tp1.sinaimg.cn/1571889140/180/40030060651/1");
			} else {
				WxFans wxFans = wxFansService.findWxFansByOpenid(wxChat.getFromUserName());
				if (null != wxFans) {
					wechatDTOBase.setUserName(wxFans.getNickname());
					wechatDTOBase.setAvatar(wxFans.getHeadimgurl());
				}
			}
			String timestamp = DateUtils.formatDate(wxChat.getCreateDate(), "yyyy-MM-dd HH:mm:ss");
			wechatDTOBase.setTimestamp(timestamp);
			wechatDTOBaseList.add(wechatDTOBase);
		}
		int total = resultDataGrid.getTotal();
		int pageCount = 0;
		if (total % pageSize == 0) {
			pageCount = total / pageSize;
		} else {
			pageCount = total / pageSize + 1;
		}

		pageMap.setCount(pageCount);
		pageMap.setList(wechatDTOBaseList);

		return pageMap;
	}

	/**
	 * 图片上传
	 */
	@ResponseBody
	@RequestMapping(value = "im/uploadImage", method = RequestMethod.POST)
	public MessageImageDto uploadImage(@RequestParam MultipartFile file, HttpServletRequest request) {
		MessageImageDto messageImageDto = new MessageImageDto();
		Map<String, String> data = new HashMap<String, String>();
		try {
			String domain=paramsService.findParamsByName("sys.domain");
			if (null != file) {
				String upload = request.getSession().getServletContext().getRealPath("upload");
				String directory = DateUtils.getDate("yyyyMMddHHmmss");
				String realPath = upload + "/chat/image/" + directory;
				FileUtils.createDirectory(realPath);
				String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")).toLowerCase();
				String fileName = IdGen.uuid() + suffix;
				File target = new File(realPath + "/" + fileName);

				String src = domain + "upload/chat/image/" + directory + "/" + fileName;
				FileUtils.copyInputStreamToFile(file.getInputStream(), target);
				messageImageDto.setCode("0");
				data.put("src", src);
				messageImageDto.setData(data);
			}
		} catch (Exception e) {
			messageImageDto.setCode("1");// 图片保存异常
			messageImageDto.setMsg("图片保存异常");
			e.printStackTrace();
		}
		return messageImageDto;
	}

	/**
	 * 文件上传
	 */
	@ResponseBody
	@RequestMapping(value = "im/uploadFile", method = RequestMethod.POST)
	public MessageImageDto uploadFile(@RequestParam MultipartFile file, HttpServletRequest request) {
		MessageImageDto messageImageDto = new MessageImageDto();
		Map<String, String> data = new HashMap<String, String>();
		try {
			String domain=paramsService.findParamsByName("sys.domain");
			if (null != file) {
				String upload = request.getSession().getServletContext().getRealPath("upload");
				String directory = DateUtils.getDate("yyyyMMddHHmmss");
				String realPath = upload + "/chat/file/" + directory;
				FileUtils.createDirectory(realPath);
				String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")).toLowerCase();
				String fileName = IdGen.uuid() + suffix;
				File target = new File(realPath + "/" + fileName);

				String src = domain + "upload/chat/file/" + directory + "/" + fileName;
				FileUtils.copyInputStreamToFile(file.getInputStream(), target);
				messageImageDto.setCode("0");
				data.put("src", src);
				data.put("name", file.getName());
				messageImageDto.setData(data);
			}
		} catch (Exception e) {
			messageImageDto.setCode("1");// 文件保存异常
			messageImageDto.setMsg("文件保存异常");
			e.printStackTrace();
		}
		return messageImageDto;
	}

}
