package cn.emay.modules.sys.service;

import cn.emay.framework.core.common.service.CommonService;
import cn.emay.modules.sys.entity.Category;

public interface CategoryServiceI extends CommonService{
	/**
	 * 保存分类管理
	 * @param category
	 */
	void saveCategory(Category category);

}
