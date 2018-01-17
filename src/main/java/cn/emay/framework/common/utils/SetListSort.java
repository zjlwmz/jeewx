package cn.emay.framework.common.utils;

import java.util.Comparator;

import cn.emay.modules.sys.entity.Function;


/**
 * @ClassName: SetListSort
 * @Description: TODO(int比较器)
 * @author 张代浩
 * @date 2013-1-31 下午06:19:03
 * 
 */
@SuppressWarnings("rawtypes")
public class SetListSort implements Comparator {
	/**
	 * 菜单排序比较器
	 */
	public int compare(Object o1, Object o2) {
		Function c1 = (Function) o1;
		Function c2 = (Function) o2;
		if (c1.getFunctionOrder() != null && c2.getFunctionOrder() != null) {
			int c1order = oConvertUtils.getInt(c1.getFunctionOrder().substring(c1.getFunctionOrder().indexOf("fun") + 3));
			int c2order = oConvertUtils.getInt(c2.getFunctionOrder().substring(c2.getFunctionOrder().indexOf("fun")) + 3);
			if (c1order > c2order) {
				return 1;
			} else {
				return -1;
			}
		} else {
			return 1;
		}

	}
}