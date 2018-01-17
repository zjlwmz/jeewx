package cn.emay.modules.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.emay.framework.common.utils.StringUtils;
import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.sys.dao.DepartDao;
import cn.emay.modules.sys.entity.Depart;
import cn.emay.modules.sys.service.DepartService;

/**
 * 
 * @Title 部门service实现接口
 * @author zjlwm
 * @date 2017-2-1 下午5:30:31
 *
 */
@Service("departService")
public class DepartServiceImpl extends CommonServiceImpl implements DepartService {

	
	@Autowired
	private DepartDao departDao;
	
	@Override
	public Depart get(String id) {
		departDao.getSession().clear();
		return departDao.get(Depart.class, id);
	}


	@Override
	public List<Depart> findFirstParent() {
		String hql="from Depart where depart.id is null";
		return departDao.findByQueryString(hql);
	}


	@Override
	public void save(Depart depart) {
		departDao.getSession().clear();
		if(StringUtils.isNotBlank(depart.getId())){
			departDao.updateEntitie(depart);
		}else{
			departDao.save(depart);
		}
	}


	@Override
	public List<Depart> findUserDepart(String userId) {
		String sql="select * from sys_depart where id in (select org_id from sys_user_org where user_id=:userid)";
		@SuppressWarnings("unchecked")
		List<Depart>departList=departDao.getSession().createSQLQuery(sql).addEntity(Depart.class).setString("userid", userId).list();
		return departList;
	}

}
