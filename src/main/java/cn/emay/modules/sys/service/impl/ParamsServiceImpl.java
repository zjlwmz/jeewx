package cn.emay.modules.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.sys.entity.Params;
import cn.emay.modules.sys.repository.ParamsDao;
import cn.emay.modules.sys.service.ParamsService;

/**
 * 
 * @Title 参数service接口实现
 * @author zjlwm
 * @date 2017-2-7 下午2:49:07
 *
 */
@Service
public class ParamsServiceImpl extends CommonServiceImpl implements ParamsService{

	@Autowired
	private ParamsDao paramsDao;
	
	@Override
	public long findParamsByNameCount(String name) {
		String sql="SELECT count(*) from sys_params t where t.param_name='"+name+"'";
		return paramsDao.getCountForJdbc(sql);
	}

	
	@Cacheable(value = "sysParams",key="#name")//使用了一个缓存名叫 sysCache
	public String findParamsByName(String name) {
		Params params=paramsDao.findUniqueByProperty(Params.class, "paramName", name);
		if(null!=params){
			return params.getParamsValue();
		}
		return "";
	}


	@CacheEvict(value="sysParams",key="#params.paramName")
	@Override
	public void saveOrUpdate(Params params) {
		 paramsDao.saveOrUpdate(params);
	}

}
