package cn.emay.modules.sys.service;

import java.util.List;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.sys.entity.Depart;


/**
 * @Title 部门service接口
 * @author zjlwm
 * @date 2017-2-1 下午5:29:15
 */
public interface DepartService extends CommonService{

	public Depart get(String id);

	public void save(Depart depart);
	
	public List<Depart>findFirstParent();
	
	/**
	 * 根据用户ID查找部门列表
	 * @param userId
	 * @return
	 */
	public List<Depart>findUserDepart(String userId);
	
}

