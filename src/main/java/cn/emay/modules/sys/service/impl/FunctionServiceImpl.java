package cn.emay.modules.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.sys.entity.Function;
import cn.emay.modules.sys.service.FunctionService;
import cn.emay.modules.wx.repository.FunctionDao;


/**
 * 菜单service接口实例
 * @author zjlwm
 * @date 2016-12-4 下午6:14:55
 *
 */
@Service("functionService")
@Transactional(readOnly=true)
public class FunctionServiceImpl extends CommonServiceImpl implements FunctionService{

	@Autowired
	private FunctionDao functionDao;
	
	
	@Transactional(readOnly=false)
	public Function get(String id){
		Function function = functionDao.getEntity(Function.class,id);
		return function;
	}
	
	@Transactional(readOnly=false)
	public void update(Function function){
		functionDao.getSession().clear();
		this.updateEntitie(function);
	}
	
	@Override
	public List<Function> findFunctionByParent(String parentId) {
		return functionDao.findByProperty(Function.class, "parentFunction.id",parentId);
	}

}
