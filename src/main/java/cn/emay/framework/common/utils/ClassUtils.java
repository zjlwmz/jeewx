package cn.emay.framework.common.utils;

public class ClassUtils extends org.apache.commons.lang3.ClassUtils{

	public static boolean isValidClass(String className){
		boolean result=false;
		try {
			ClassUtils.getClass(className);
			result=true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}
}
