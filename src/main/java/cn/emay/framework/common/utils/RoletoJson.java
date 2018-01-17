package cn.emay.framework.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import cn.emay.framework.core.common.model.json.ComboBox;
import cn.emay.modules.sys.entity.Role;

/**
 * 
 * @author 张代浩
 * 
 */
public class RoletoJson {

	/**
	 * 手工拼接JSON
	 */
	public static String getComboBoxJson(List<Role> list, List<Role> roles) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		for (Role node : list) {
			if (roles.size() > 0) {
				buffer.append("{\"id\":" + node.getId() + ",\"text\":\"" + node.getRoleName() + "\"");
				for (Role node1 : roles) {
					if (node.getId() == node1.getId()) {
						buffer.append(",\"selected\":true");
					}
				}
				buffer.append("},");
			} else {
				buffer.append("{\"id\":" + node.getId() + ",\"text\":\"" + node.getRoleName() + "\"},");
			}

		}
		buffer.append("]");

		// 将,\n]替换成\n]

		String tmp = buffer.toString();
		tmp = tmp.replaceAll(",]", "]");
		return tmp;

	}

	/**
	 * 根据模型生成JSON
	 */
	public static List<ComboBox> getComboBox(List<Role> list, List<Role> roles) {
		StringBuffer buffer = new StringBuffer();
		List<ComboBox> comboxBoxs = new ArrayList<ComboBox>();
		buffer.append("[");
		for (Role node : list) {
			ComboBox box = new ComboBox();
			box.setId(node.getId().toString());
			box.setText(node.getRoleName());
			if (roles.size() > 0) {
				for (Role node1 : roles) {
					if (node.getId() == node1.getId()) {
						box.setSelected(true);
					}
				}
			}
			comboxBoxs.add(box);
		}
		return comboxBoxs;

	}

	/**
	 * 方法描述: 值替换工具 作 者： zym
	 * 
	 * @param objList
	 * @param perFieldName
	 * @param sufFieldName
	 * @return 格式：old_new,old2_new2 返回类型： String
	 */
	public static String listToReplaceStr(List<?> objList, String perFieldName, String sufFieldName) {
		List<String> strList = new ArrayList<String>();
		for (Object object : objList) {
			String perStr = null;
			String sufStr = null;
			try {
				perStr = (String) PropertyUtils.getProperty(object, perFieldName);
				sufStr = (String) PropertyUtils.getProperty(object, sufFieldName);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			strList.add(perStr + "_" + sufStr);
		}
		return StringUtils.join(strList, ",");
	}
}
