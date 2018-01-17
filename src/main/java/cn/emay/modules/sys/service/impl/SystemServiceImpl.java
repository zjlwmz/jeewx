package cn.emay.modules.sys.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.emay.framework.common.utils.BrowserUtils;
import cn.emay.framework.common.utils.ContextHolderUtils;
import cn.emay.framework.common.utils.DateUtils;
import cn.emay.framework.common.utils.MutiLangUtil;
import cn.emay.framework.common.utils.ResourceUtil;
import cn.emay.framework.common.utils.StringUtil;
import cn.emay.framework.common.utils.oConvertUtils;
import cn.emay.framework.core.common.hibernate.qbc.CriteriaQuery;
import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.sys.entity.DatalogEntity;
import cn.emay.modules.sys.entity.DictEntity;
import cn.emay.modules.sys.entity.Function;
import cn.emay.modules.sys.entity.Icon;
import cn.emay.modules.sys.entity.Log;
import cn.emay.modules.sys.entity.Role;
import cn.emay.modules.sys.entity.RoleFunction;
import cn.emay.modules.sys.entity.RoleUser;
import cn.emay.modules.sys.entity.Type;
import cn.emay.modules.sys.entity.Typegroup;
import cn.emay.modules.sys.entity.User;
import cn.emay.modules.sys.repository.DictDao;
import cn.emay.modules.sys.service.SystemService;
import cn.emay.modules.sys.utils.UserCacheUtils;

@Service("systemService")
@Transactional
public class SystemServiceImpl extends CommonServiceImpl implements SystemService {
	

	@Autowired
	private DictDao dictDao;

	public List<DictEntity> queryDict(String dicTable, String dicCode,String dicText){
		List<DictEntity> dictList = null;
		//step.1 如果没有字典表则使用系统字典表
		if(StringUtil.isEmpty(dicTable)){
			dictList = dictDao.querySystemDict(dicCode);
			for(DictEntity t:dictList){
				t.setTypename(MutiLangUtil.getMutiLangInstance().getLang(t.getTypename()));
			}
		}else {
			dicText = StringUtil.isEmpty(dicText, dicCode);
			dictList = dictDao.queryCustomDict(dicTable, dicCode, dicText);
		}
		return dictList;
	}

	/**
	 * 添加日志
	 */
	public void addLog(String logcontent, Short loglevel, Short operatetype) {
		HttpServletRequest request = ContextHolderUtils.getRequest();
		String broswer = BrowserUtils.checkBrowse(request);
		Log log = new Log();
		log.setLogcontent(logcontent);
		log.setLoglevel(loglevel);
		log.setOperatetype(operatetype);
		log.setNote(oConvertUtils.getIp());
		log.setBroswer(broswer);
		log.setOperatetime(DateUtils.getTimestamp());
		log.setUser(ResourceUtil.getSessionUserName());
		commonDao.save(log);
	}

	/**
	 * 根据类型编码和类型名称获取Type,如果为空则创建一个
	 *
	 * @param typecode
	 * @param typename
	 * @return
	 */
	public Type getType(String typecode, String typename, Typegroup typegroup) {
		List<Type> ls = commonDao.findHql("from Type where typecode = ? and typegroupid = ?",typecode,typegroup.getId());
		Type actType = null;
		if (ls == null || ls.size()==0) {
			actType = new Type();
			actType.setTypecode(typecode);
			actType.setTypename(typename);
			actType.setTypegroup(typegroup);
			commonDao.save(actType);
		}else{
			actType = ls.get(0);
		}
		return actType;

	}

	/**
	 * 根据类型分组编码和名称获取TypeGroup,如果为空则创建一个
	 *
	 * @param typecode
	 * @param typename
	 * @return
	 */
	public Typegroup getTypeGroup(String typegroupcode, String typgroupename) {
		Typegroup typegroup = commonDao.findUniqueByProperty(Typegroup.class, "typegroupcode", typegroupcode);
		if (typegroup == null) {
			typegroup = new Typegroup();
			typegroup.setTypegroupcode(typegroupcode);
			typegroup.setTypegroupname(typgroupename);
			commonDao.save(typegroup);
		}
		return typegroup;
	}


	public Typegroup getTypeGroupByCode(String typegroupCode) {
		Typegroup typegroup = commonDao.findUniqueByProperty(Typegroup.class, "typegroupcode", typegroupCode);
		return typegroup;
	}


	public void initAllTypeGroups() {
		List<Typegroup> typeGroups = this.commonDao.loadAll(Typegroup.class);
		for (Typegroup tsTypegroup : typeGroups) {
			ResourceUtil.allTypeGroups.put(tsTypegroup.getTypegroupcode().toLowerCase(), tsTypegroup);
			List<Type> types = this.commonDao.findByProperty(Type.class, "typegroup.id", tsTypegroup.getId());
			ResourceUtil.allTypes.put(tsTypegroup.getTypegroupcode().toLowerCase(), types);
		}
	}


	public void refleshTypesCach(Type type) {
		Typegroup tsTypegroup = type.getTypegroup();
		Typegroup typeGroupEntity = this.commonDao.get(Typegroup.class, tsTypegroup.getId());
		List<Type> types = this.commonDao.findByProperty(Type.class, "typegroup.id", tsTypegroup.getId());
		ResourceUtil.allTypes.put(typeGroupEntity.getTypegroupcode().toLowerCase(), types);
	}


	public void refleshTypeGroupCach() {
		ResourceUtil.allTypeGroups.clear();
		List<Typegroup> typeGroups = this.commonDao.loadAll(Typegroup.class);
		for (Typegroup typegroup : typeGroups) {
			ResourceUtil.allTypeGroups.put(typegroup.getTypegroupcode().toLowerCase(), typegroup);
		}
	}

	/**
	 * 根据角色ID 和 菜单Id 获取 具有操作权限的按钮Codes
	 * @param roleId
	 * @param functionId
	 * @return
	 */
	public Set<String> getOperationCodesByRoleIdAndFunctionId(String roleId, String functionId) {
		Set<String> operationCodes = new HashSet<String>();
		Role role = commonDao.get(Role.class, roleId);
		CriteriaQuery cq1 = new CriteriaQuery(RoleFunction.class);
		cq1.eq("role.id", role.getId());
		cq1.eq("function.id", functionId);
		cq1.add();
		List<RoleFunction> rFunctions = getListByCriteriaQuery(cq1, false);
		if (null != rFunctions && rFunctions.size() > 0) {
			RoleFunction tsRoleFunction = rFunctions.get(0);
			if (null != tsRoleFunction.getOperation()) {
				String[] operationArry = tsRoleFunction.getOperation().split(",");
				for (int i = 0; i < operationArry.length; i++) {
					operationCodes.add(operationArry[i]);
				}
			}
		}
		return operationCodes;
	}

	/**
	 * 根据用户ID 和 菜单Id 获取 具有操作权限的按钮Codes
	 * @param roleId
	 * @param functionId
	 * @return
	 */
	public Set<String> getOperationCodesByUserIdAndFunctionId(String userId, String functionId) {
		Set<String> operationCodes = new HashSet<String>();
		List<RoleUser> rUsers = findByProperty(RoleUser.class, "user.id", userId);
		for (RoleUser ru : rUsers) {
			Role role = ru.getRole();
			CriteriaQuery cq1 = new CriteriaQuery(RoleFunction.class);
			cq1.eq("role.id", role.getId());
			cq1.eq("function.id", functionId);
			cq1.add();
			List<RoleFunction> rFunctions = getListByCriteriaQuery(cq1, false);
			if (null != rFunctions && rFunctions.size() > 0) {
				RoleFunction tsRoleFunction = rFunctions.get(0);
				if (null != tsRoleFunction.getOperation()) {
					String[] operationArry = tsRoleFunction.getOperation().split(",");
					for (int i = 0; i < operationArry.length; i++) {
						operationCodes.add(operationArry[i]);
					}
				}
			}
		}
		return operationCodes;
	}

	public void flushRoleFunciton(String id, Function newFunction) {
		Function functionEntity = this.getEntity(Function.class, id);
		if (functionEntity.getIcon() == null || !StringUtil.isNotEmpty(functionEntity.getIcon().getId())) {
			return;
		}
		
		Icon oldIcon = this.getEntity(Icon.class, functionEntity.getIcon().getId());
		if (!oldIcon.getIconClas().equals(newFunction.getIcon().getIconClas())) {
			// 刷新缓存
			HttpSession session = ContextHolderUtils.getSession();
			User user =UserCacheUtils.getCurrentUser();
			List<RoleUser> rUsers = this.findByProperty(RoleUser.class, "user.id", user.getId());
			for (RoleUser ru : rUsers) {
				Role role = ru.getRole();
				session.removeAttribute(role.getId());
			}
		}
	}

    public String generateOrgCode(String id, String pid) {

        int orgCodeLength = 2; // 默认编码长度
        if ("3".equals(ResourceUtil.getOrgCodeLengthType())) { // 类型2-编码长度为3，如001
            orgCodeLength = 3;
        }


        String  newOrgCode = "";
        if(!StringUtils.hasText(pid)) { // 第一级编码
            String sql = "select max(t.org_code) orgCode from sys_depart t where t.parentdepartid is null";
            Map<String, Object> pOrgCodeMap = commonDao.findOneForJdbc(sql);
            if(pOrgCodeMap.get("orgCode") != null) {
                String curOrgCode = pOrgCodeMap.get("orgCode").toString();
                newOrgCode = String.format("%0" + orgCodeLength + "d", Integer.valueOf(curOrgCode) + 1);
            } else {
                newOrgCode = String.format("%0" + orgCodeLength + "d", 1);
            }
        } else { // 下级编码
            String sql = "select max(t.org_code) orgCode from sys_depart t where t.parentdepartid = ?";
            Map<String, Object> orgCodeMap = commonDao.findOneForJdbc(sql, pid);
            if(orgCodeMap.get("orgCode") != null) { // 当前基本有编码时
                String curOrgCode = orgCodeMap.get("orgCode").toString();
                String pOrgCode = curOrgCode.substring(0, curOrgCode.length() - orgCodeLength);
                String subOrgCode = curOrgCode.substring(curOrgCode.length() - orgCodeLength, curOrgCode.length());
                newOrgCode = pOrgCode + String.format("%0" + orgCodeLength + "d", Integer.valueOf(subOrgCode) + 1);
            } else { // 当前级别没有编码时
                String pOrgCodeSql = "select max(t.org_code) orgCode from sys_depart t where t.id = ?";
                Map<String, Object> pOrgCodeMap = commonDao.findOneForJdbc(pOrgCodeSql, pid);
                String curOrgCode = pOrgCodeMap.get("orgCode").toString();
                newOrgCode = curOrgCode + String.format("%0" + orgCodeLength + "d", 1);
            }
        }

        return newOrgCode;
    }

	public Set<String> getOperationCodesByRoleIdAndruleDataId(String roleId,
			String functionId) {
		Set<String> operationCodes = new HashSet<String>();
		Role role = commonDao.get(Role.class, roleId);
		CriteriaQuery cq1 = new CriteriaQuery(RoleFunction.class);
		cq1.eq("role.id", role.getId());
		cq1.eq("function.id", functionId);
		cq1.add();
		List<RoleFunction> rFunctions = getListByCriteriaQuery(cq1, false);
		if (null != rFunctions && rFunctions.size() > 0) {
			RoleFunction tsRoleFunction = rFunctions.get(0);
			if (null != tsRoleFunction.getDataRule()) {
				String[] operationArry = tsRoleFunction.getDataRule().split(",");
				for (int i = 0; i < operationArry.length; i++) {
					operationCodes.add(operationArry[i]);
				}
			}
		}
		return operationCodes;
	}

	public Set<String> getOperationCodesByUserIdAndDataId(String userId,
			String functionId) {
		// TODO Auto-generated method stub
		Set<String> dataRulecodes = new HashSet<String>();
		List<RoleUser> rUsers = findByProperty(RoleUser.class, "user.id", userId);
		for (RoleUser ru : rUsers) {
			Role role = ru.getRole();
			CriteriaQuery cq1 = new CriteriaQuery(RoleFunction.class);
			cq1.eq("role.id", role.getId());
			cq1.eq("function.id", functionId);
			cq1.add();
			List<RoleFunction> rFunctions = getListByCriteriaQuery(cq1, false);
			if (null != rFunctions && rFunctions.size() > 0) {
				RoleFunction tsRoleFunction = rFunctions.get(0);
				if (null != tsRoleFunction.getDataRule()) {
					String[] operationArry = tsRoleFunction.getDataRule().split(",");
					for (int i = 0; i < operationArry.length; i++) {
						dataRulecodes.add(operationArry[i]);
					}
				}
			}
		}
		return dataRulecodes;
	}
	/**
	 * 加载所有图标
	 * @return
	 */
	public  void initAllTSIcons() {
		List<Icon> list = this.loadAll(Icon.class);
		for (Icon tsIcon : list) {
			ResourceUtil.allTSIcons.put(tsIcon.getId(), tsIcon);
		}
	}
	/**
	 * 更新图标
	 * @param icon
	 */
	public  void upTSIcons(Icon icon) {
		ResourceUtil.allTSIcons.put(icon.getId(), icon);
	}
	/**
	 * 更新图标
	 * @param icon
	 */
	public  void delTSIcons(Icon icon) {
		ResourceUtil.allTSIcons.remove(icon.getId());
	}

	@Override
	public void addDataLog(String tableName, String dataId, String dataContent) {

		int versionNumber = 0;

		Integer integer = commonDao.singleResult("select max(versionNumber) from TSDatalogEntity where tableName = '" + tableName + "' and dataId = '" + dataId + "'");
		if (integer != null) {
			versionNumber = integer.intValue();
		}

		DatalogEntity tsDatalogEntity = new DatalogEntity();
		tsDatalogEntity.setTableName(tableName);
		tsDatalogEntity.setDataId(dataId);
		tsDatalogEntity.setDataContent(dataContent);
		tsDatalogEntity.setVersionNumber(versionNumber + 1);
		commonDao.save(tsDatalogEntity);
	}

}
