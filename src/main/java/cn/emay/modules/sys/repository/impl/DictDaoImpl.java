package cn.emay.modules.sys.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.emay.framework.core.common.dao.impl.GenericBaseCommonDao;
import cn.emay.modules.sys.entity.DictEntity;
import cn.emay.modules.sys.entity.Type;
import cn.emay.modules.sys.repository.DictDao;

/**
 * 数据字典DAO接口实现
 * @author lenovo
 *
 */
@Repository
public class DictDaoImpl extends GenericBaseCommonDao<Type, String>  implements DictDao{


	@Override
	public List<DictEntity> querySystemDict(String dicCode) {
		List<Type>typeList=this.findByQueryString("from Type  t where  t.typegroup.typegroupcode='"+dicCode+"'");
		List<DictEntity>dictEntityList=new ArrayList<DictEntity>();
		for(Type type:typeList){
			DictEntity dictEntity=new DictEntity();
			dictEntity.setTypename(type.getTypename());
			dictEntity.setTypecode(type.getTypecode());
			dictEntityList.add(dictEntity);
		}
		return dictEntityList;
	}


	@Override
	public List<DictEntity> queryCustomDict(String dicTable, String dicCode, String dicText) {
		String sql="select DISTINCT "+dicTable+" as TYPECODE , "+dicText+" as TYPENAME from "+dicTable+" order by ${dicCode}";
		List<Type>typeList=this.findListbySql(sql);
		List<DictEntity>dictEntityList=new ArrayList<DictEntity>();
		for(Type type:typeList){
			DictEntity dictEntity=new DictEntity();
			dictEntity.setTypename(type.getTypename());
			dictEntity.setTypecode(type.getTypecode());
			dictEntityList.add(dictEntity);
		}
		return dictEntityList;
	}

	
}
