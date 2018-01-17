package cn.emay.modules.sys.service.impl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.emay.framework.common.utils.StringUtil;
import cn.emay.framework.core.annotation.config.AutoMenu;
import cn.emay.framework.core.annotation.config.AutoMenuOperation;
import cn.emay.framework.core.annotation.config.MenuCodeType;
import cn.emay.framework.core.common.service.impl.CommonServiceImpl;
import cn.emay.modules.sys.entity.Function;
import cn.emay.modules.sys.entity.Icon;
import cn.emay.modules.sys.entity.Operation;
import cn.emay.modules.sys.service.MenuInitService;
import cn.emay.modules.sys.utils.PackagesToScanUtil;

@Service("menuInitService")
@Transactional
public class MenuInitServiceImpl extends CommonServiceImpl implements
		MenuInitService {

	private static final String KEY_SPLIT = "-";//map key 分隔符
	private static final String MENU_TYPE_ID = "#";//采用控件ID方式的code前缀
	private static final String MENU_TYPE_CSS = ".";//采用Css样式方式的code前缀
	
	
	public void initMenu() {
		// 1.扫描项目下，所有class，判断带有标签@AutoMenu
		// TODO
		// 2.循环判断@AutoMenu在系统中是否存在,如果不存在进行插入，如果存在不再插入
		// 比较规则[菜单名称-等级-菜单地址：全匹配]

		// 3.加载@AutoMenu下带有标签@AutoMenuOperation所有方法

		// 4.循环@AutoMenuOperation方法标签，判断该菜单下是否有该操作码配置，如果存在不插入，不存在进行插入
		// 比较规则[菜单ID-操作码 ：全匹配]

		List<Function> functionList = this.loadAll(Function.class);
		List<Operation> operationList = this.loadAll(Operation.class);
		
		Map<String, Function> functionMap = new HashMap<String, Function>();//菜单map,key为菜单匹配规则的字符串
		Map<String, Operation> operationMap = new HashMap<String, Operation>();//菜单操作按钮map,key为菜单操作按钮匹配规则的字符串
		
		//设置菜单map
		for (Function function : functionList) {
			StringBuffer key = new StringBuffer();
			key.append(function.getFunctionName() == null ? "" : function.getFunctionName());
			key.append(KEY_SPLIT);
			key.append(function.getFunctionLevel() == null ? "" : function.getFunctionLevel());
			key.append(KEY_SPLIT);
			key.append(function.getFunctionUrl() == null ? "" : function.getFunctionUrl());
			functionMap.put(key.toString(), function);
		}
		//设置菜单操作按钮map
		for (Operation operation : operationList) {
			StringBuffer key = new StringBuffer();
			key.append(operation.getFunction() == null ? "" : operation.getFunction().getId());
			key.append(KEY_SPLIT);
			key.append(operation.getOperationcode() == null ? "" : operation.getOperationcode());
			operationMap.put(key.toString(), operation);
		}
		
		//扫描Src目录下
		Set<Class<?>> classSet = PackagesToScanUtil.getClasses(".*");
		for (Class<?> clazz : classSet) {
			//判断当前类是否设置了菜单注解
			//未设置菜单注解就算在该类的方法上设置了菜单操作按钮注解也不进行菜单操作按钮的匹配
			if (clazz.isAnnotationPresent(AutoMenu.class)) {
				AutoMenu autoMenu = clazz.getAnnotation(AutoMenu.class);
				//菜单名称必须填写，否则不进行菜单和菜单操作按钮的匹配
				if (StringUtil.isNotEmpty(autoMenu.name())) {
					StringBuffer menuKey = new StringBuffer();
					menuKey.append(autoMenu.name());
					menuKey.append(KEY_SPLIT);
					menuKey.append(autoMenu.level() == null ? "" : autoMenu.level());
					menuKey.append(KEY_SPLIT);
					menuKey.append(autoMenu.url() == null ? "" : autoMenu.url());
					
					Function function = null;
					//判断菜单map的key是否包含当前key，不包含则插入一条菜单数据
					if (!functionMap.containsKey(menuKey.toString())) {
						function = new Function();
						function.setFunctionName(autoMenu.name());
						function.setFunctionIframe(null);
						function.setFunctionLevel(Short.valueOf(autoMenu.level()));
						function.setFunctionOrder(Integer.toString(autoMenu.order()));
						function.setFunctionUrl(autoMenu.url());
						function.setParentFunction(null);
						
						String iconId = autoMenu.icon();
						if (StringUtil.isNotEmpty(iconId)) {
							Object obj = this.get(Icon.class, iconId);
							if(obj!=null){
								function.setIcon((Icon)obj);
							}else{
								function.setIcon(null);
							}
						} else {
							function.setIcon(null);
						}
						Serializable id = this.save(function);
						function.setId(id.toString());
					} else {
						function = functionMap.get(menuKey.toString());
					}
						
					//获取该类的所有方法
					Method[] methods = clazz.getDeclaredMethods();
					for(Method method : methods){
						//判断当前方法是否设置了菜单操作按钮注解
						if (method.isAnnotationPresent(AutoMenuOperation.class)) {
							AutoMenuOperation autoMenuOperation = method.getAnnotation(AutoMenuOperation.class);
							//操作码必须填写，否则不进行菜单操作按钮的匹配
							if (StringUtil.isNotEmpty(autoMenuOperation.code())) {
								StringBuffer menuOperationKey = new StringBuffer();
								menuOperationKey.append(function == null ? "" : function.getId());
								menuOperationKey.append(KEY_SPLIT);
								
								String code = "";
								//设置code前缀
								if (autoMenuOperation.codeType() == MenuCodeType.TAG) {
									code = autoMenuOperation.code();
								} else if (autoMenuOperation.codeType() == MenuCodeType.ID) {
									code = MENU_TYPE_ID + autoMenuOperation.code();
								} else if (autoMenuOperation.codeType() == MenuCodeType.CSS) {
									code = MENU_TYPE_CSS + autoMenuOperation.code();
								}
								menuOperationKey.append(code);
								
								//判断菜单操作按钮map的key是否包含当前key，不包含则插入一条菜单操作按钮数据
								if (!operationMap.containsKey(menuOperationKey.toString())) {
									Operation operation = new Operation();
									operation.setOperationname(autoMenuOperation.name());
									operation.setOperationcode(code);
									operation.setOperationicon(null);
									operation.setStatus(Short.parseShort(Integer.toString(autoMenuOperation.status())));
									operation.setFunction(function);
									
									String iconId = autoMenuOperation.icon();
									if (StringUtil.isNotEmpty(iconId)) {
										Icon icon = new Icon();
										icon.setId(iconId);
										operation.setIcon(icon);
									} else {
										operation.setIcon(null);
									}
									this.save(operation);
								}
							}
						}
					}
				}
			}
		}
	}
}
