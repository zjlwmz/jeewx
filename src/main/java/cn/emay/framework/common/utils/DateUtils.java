/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package cn.emay.framework.common.utils;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * 
 * @author ThinkGem
 * @version 2014-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	private static String[] parsePatterns = { "yyyy-MM-dd",
			"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", "yyyy/MM/dd",
			"yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd",
			"yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM" };

	private final static long minuteTime = 60 * 1000;		// 1分钟
    private final static long hourTime = 60 * minuteTime;	// 1小时
    private final static long dayTime = 24 * hourTime;		// 1天
    private final static long monthTime = 31 * dayTime;		// 月
    private final static long yearTime = 12 * monthTime;	// 年
    
    
    // 各种时间格式
 	public static final SimpleDateFormat date_sdf = new SimpleDateFormat("yyyy-MM-dd");
 	// 各种时间格式
 	public static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
 	// 各种时间格式
 	public static final SimpleDateFormat date_sdf_wz = new SimpleDateFormat("yyyy年MM月dd日");
 	public static final SimpleDateFormat time_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
 	public static final SimpleDateFormat yyyymmddhhmmss = new SimpleDateFormat("yyyyMMddHHmmss");
 	public static final SimpleDateFormat short_time_sdf = new SimpleDateFormat("HH:mm");
 	public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 	
 	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}

	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}

	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}

	/**
	 * 日期型字符串转化为日期 格式 { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
	 * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy.MM.dd",
	 * "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null) {
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * 
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (24 * 60 * 60 * 1000);
	}

	/**
	 * 获取过去的小时
	 * 
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 60 * 1000);
	}

	/**
	 * 获取过去的分钟
	 * 
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime() - date.getTime();
		return t / (60 * 1000);
	}

	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * 
	 * @param timeMillis
	 * @return
	 */
	public static String formatDateTime(long timeMillis) {
		long day = timeMillis / (24 * 60 * 60 * 1000);
		long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
		long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60
				* 1000 - min * 60 * 1000 - s * 1000);
		return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "."
				+ sss;
	}

	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}

	/**
	 * 得到指定月的天数
	 * */
	public static int getMonthLastDay(int year, int month) {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	public static int getMonthDay(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DATE, -1);
		int j = cal.get(Calendar.DAY_OF_MONTH);
		return j;
	}

	/**
	 * 取得当月天数
	 * */
	public static int getCurrentMonthLastDay() {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
		a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	
	
	
	/**
     * 返回文字描述的日期
     * 
     * @param date
     * @return
     */
    public static String getTimeFormatText(Date date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > yearTime) {
            r = (diff / yearTime);
            return r + "年前";
        }
        if (diff > monthTime) {
            r = (diff / monthTime);
            return r + "个月前";
        }
        if (diff > dayTime) {
            r = (diff / dayTime);
            return r + "天前";
        }
        if (diff > hourTime) {
            r = (diff / hourTime);
            return r + "个小时前";
        }
        if (diff > minuteTime) {
            r = (diff / minuteTime);
            return r + "分钟前";
        }
        return "刚刚";
    }
	
    
	public static Long dateDiff(Date startTime, Date endTime) {
		long ns = 1000;// 一秒钟的毫秒数
		long diff = endTime.getTime() - startTime.getTime();
		long day = diff / dayTime;// 计算差多少天
		long hour = diff % dayTime / hourTime + day * 24;// 计算差多少小时
		long min = diff % dayTime % hourTime / minuteTime + day * 24 * 60;// 计算差多少分钟
		long sec = diff % dayTime % hourTime % minuteTime / ns;// 计算差多少秒
		// 输出结果
		System.out.println("时间相差：" + day + "天" + (hour - day * 24) + "小时" + (min - day * 24 * 60) + "分钟" + sec + "秒。");
		System.out.println("hour=" + hour + ",min=" + min);
		return 0l;
	}
    
	
	
	/**
	 * 秒转时间
	 * @param diff
	 * @example 26分钟38秒
	 * @return
	 */
	public static String dateDiff(long diff) {
		StringBuffer diffStr=new StringBuffer();
		long ns = 1000;// 一秒钟的毫秒数
		long day = diff / dayTime;// 计算差多少天
		if(day>0){
			diffStr.append(day+"天");
		}
		long hour = diff % dayTime / hourTime + day * 24;// 计算差多少小时
		if(hour>0){
			if((hour - day * 24)>0){
				diffStr.append((hour - day * 24)+"小时");
			}
		}
		long min = diff % dayTime % hourTime / minuteTime + day * 24 * 60;// 计算差多少分钟
		if(min>0){
			if((min - day * 24 * 60)>0){
				diffStr.append((min - day * 24 * 60) + "分钟");
			}
			
		}
		long sec = diff % dayTime % hourTime % minuteTime / ns;// 计算差多少秒
		if(sec>0){
			diffStr.append(sec + "秒");
		}
		return diffStr.toString();
	}
    
	
	/**
	 * mp3时间
	 * @param diff
	 * 26:38
	 * @return
	 */
	public static String mp3Time(long diff) {
		StringBuffer diffStr=new StringBuffer();
		long ns = 1000;// 一秒钟的毫秒数
		long day = diff / dayTime;// 计算差多少天
		if(day>0){
			diffStr.append(day+" ");
		}
		long hour = diff % dayTime / hourTime + day * 24;// 计算差多少小时
		if(hour>0){
			if((hour - day * 24)>0){
				diffStr.append((hour - day * 24)+":");
			}
		}
		long min = diff % dayTime % hourTime / minuteTime + day * 24 * 60;// 计算差多少分钟
		if(min>0){
			if((min - day * 24 * 60)>0){
				diffStr.append((min - day * 24 * 60) + ":");
			}
			
		}else{
			diffStr.append("00:");
		}
		long sec = diff % dayTime % hourTime % minuteTime / ns;// 计算差多少秒
		if(sec>0){
			DecimalFormat df= new DecimalFormat("00"); 
			diffStr.append(df.format(sec));
		}else{
			diffStr.append("00");
		}
		return diffStr.toString();
	}
	
	
	
	/**
	 * 指定毫秒数的时间戳
	 * 
	 * @param millis
	 *            毫秒数
	 * @return 指定毫秒数的时间戳
	 */
	public static Timestamp getTimestamp(long millis) {
		return new Timestamp(millis);
	}
	
	/**
	 * 系统当前的时间戳
	 * 
	 * @return 系统当前的时间戳
	 */
	public static Timestamp getTimestamp() {
		return new Timestamp(new Date().getTime());
	}
	
	public static Timestamp parseTimestamp(String datestr, String pattern) throws ParseException {
		Date date=parseDate(datestr, pattern);
		return new Timestamp(date.getTime());
	}
	
//	public static Timestamp gettimestamp() {
//		Date dt = new Date();
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String nowTime = df.format(dt);
//		java.sql.Timestamp buydate = java.sql.Timestamp.valueOf(nowTime);
//		return buydate;
//	}
	
	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		dateDiff(parseDate("2010/3/6"), parseDate("2010/3/6"));
		String dd=dateDiff(1598000);
		System.out.println(dd);
		
		String dd2=mp3Time(2*1000);
		System.out.println(dd2);
		
		
		// System.out.println(formatDate(parseDate("2010/3/6")));
		// System.out.println(getDate("yyyy年MM月dd日 E"));
		// long time = new Date().getTime()-parseDate("2012-11-19").getTime();
		// System.out.println(time/(24*60*60*1000));
	}

	
}
