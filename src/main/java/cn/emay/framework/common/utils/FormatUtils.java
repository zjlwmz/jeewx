package cn.emay.framework.common.utils;

import java.text.DecimalFormat;

/**
 * @Title  格式工具类
 * @author zjlwm
 * @date 2016-12-26 下午1:34:16
 *
 */
public class FormatUtils {

	/**
	 * 将double类型的数字保留两位小数（四舍五入）
	 * 
	 * @param number
	 * @return
	 */
	public static String formatNumber(double number) {
		DecimalFormat df = new DecimalFormat();
		df.applyPattern("#0.00");
		return df.format(number);
	}
	
	/**
	 * 将double转换百分比
	 * @param number
	 * @return
	 */
    public static String formatPercent(double number){
    	DecimalFormat df= new DecimalFormat("#0.0#%"); 
		return df.format(number);
    }
    
    
    public static void main(String[] args) {
    	String dd=formatPercent(1.0010);
    	System.out.println(dd);
    	
    	DecimalFormat df= new DecimalFormat("00"); 
    	System.out.println(df.format(100));
	}
}
