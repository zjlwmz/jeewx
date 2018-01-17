package cn.emay.modules.sys.repository;

import java.util.List;

import cn.emay.framework.core.common.dao.IGenericBaseCommonDao;
import cn.emay.modules.sys.entity.DictEntity;


/**
 * 数据字典DAO接口
 * @author lenovo
 *
 */
public interface DictDao extends IGenericBaseCommonDao{

	/**
	 * 查询系统字典
	 * @param dicCode
	 * @return
	 */
	public List<DictEntity> querySystemDict(String dicCode);
	
	/**
	 * 查询自定义字典
	 * @param dicTable
	 * @param dicCode
	 * @param dicText
	 * @return
	 */
	public List<DictEntity> queryCustomDict(String dicTable, String dicCode,String dicText);
}
