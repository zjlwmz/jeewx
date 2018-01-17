package cn.emay.modules.sys.repository.impl;

import org.springframework.stereotype.Repository;

import cn.emay.framework.core.common.dao.impl.GenericBaseCommonDao;
import cn.emay.modules.sys.entity.Params;
import cn.emay.modules.sys.repository.ParamsDao;

@Repository
public class ParamsDaoImpl extends GenericBaseCommonDao<Params, String>  implements ParamsDao{

}
