package io.renren.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class DateUtil {

	private static SimpleDateFormat dateSdf = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat dateTimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat dateSdf2 = new SimpleDateFormat("yyyy-MM-dd");

	public static void main(String[] args) {
//		System.out.println(dateTimeSdf.format(new Date()));
//		System.out.println("getCurrDayStr=" + getCurrDayStr());
//		System.out.println("getCurrDayBefore=" + getCurrDayBefore(1));
//		System.out.println("getCurrDayBefore=" + getCurrDayBefore("20170423", 1, null));
		
//		System.out.println("getNextMinutes=" + getNextMinutes("2017-06-09 17:00", 30, "yyyy-MM-dd HH:mm"));
		
//		System.err.println("+++++++++" + getWeekOfDate(new Date()));
		
		System.err.println(differentDaysByMillisecond("2017-05-29", "2017-06-01"));
	}
	
	/**
	 * 获取当前日期是星期几<br>
	 * 
	 * @param dt
	 * @return 当前日期是星期几
	 */
	public static String getWeekOfDate(Date dt) {
	    String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(dt);
	    int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
	    if (w < 0)
	        w = 0;
	    return weekDays[w];
	}

	public static String formatDate(Date date) {
		return dateSdf.format(date);
	}

	public static String formatDate(String date) {
		return dateSdf.format(parseDate(date));
	}

	public static String formatDateTime(Date dateTime) {
		return dateTimeSdf.format(dateTime);
	}

	public static String formatDateTime(String dateTime) {
		return dateTimeSdf.format(parseDate(dateTime));
	}

	public static Date parseDate(String date) {
		try {
			return dateSdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date parseDateTime(String dateTime) {
		try {
			return dateTimeSdf.parse(dateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 返回当天日期字符串
	 * 
	 * @return 20170425
	 */
	public static String getCurrDayStr() {
		return dateSdf.format(new Date());
	}

	/**
	 * 返回指定日期10天前日期
	 * 
	 * @param date
	 *            指定日期
	 * @param days
	 *            多少天前
	 * @return
	 */
	public static String getCurrDayBefore(String date, int days, String sdf) {
		if (StringUtils.isEmpty(date)) {
			return "";
		}
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateSdf2 = null;

		try {
			if (sdf != null && sdf.length() > 0) {
				dateSdf2 = new SimpleDateFormat(sdf);
				cal.setTime(dateSdf2.parse(date));
			} else {
				cal.setTime(dateSdf.parse(date));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.add(Calendar.DATE, -days);
		if (sdf != null && sdf.length() > 0) {
			return dateSdf2.format(cal.getTime());
		} else {
			return dateSdf.format(cal.getTime());
		}

	}

	public static String getNextMinutes(String date, int minutes, String sdf) {
		if (StringUtils.isEmpty(date)) {
			return "";
		}
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateSdf2 = null;
		try {
			dateSdf2 = new SimpleDateFormat(sdf);
			cal.setTime(dateSdf2.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.add(Calendar.MINUTE, minutes);
		return dateSdf2.format(cal.getTime());

	}
	
	public static String getHourBefore(String date, String dateSdf__,int hours, String retSdf) {
		if (StringUtils.isEmpty(date)) {
			return "";
		}
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateSdf2 = null;

		try {
			if (dateSdf__ != null && dateSdf__.length() > 0) {
				dateSdf2 = new SimpleDateFormat(dateSdf__);
				cal.setTime(dateSdf2.parse(date));
			} else {
				cal.setTime(dateSdf.parse(date));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.add(Calendar.HOUR_OF_DAY, -hours);
		if (retSdf != null && retSdf.length() > 0) {
			return new SimpleDateFormat(retSdf).format(cal.getTime());
		} else {
			return dateSdf.format(cal.getTime());
		}

	}

	public static String getHourBefore(String date, int hours, String sdf) {
		if (StringUtils.isEmpty(date)) {
			return "";
		}
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateSdf2 = null;

		try {
			if (sdf != null && sdf.length() > 0) {
				dateSdf2 = new SimpleDateFormat(sdf);
				cal.setTime(dateSdf2.parse(date));
			} else {
				cal.setTime(dateSdf.parse(date));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.add(Calendar.HOUR_OF_DAY, -hours);
		if (sdf != null && sdf.length() > 0) {
			return dateSdf2.format(cal.getTime());
		} else {
			return dateSdf.format(cal.getTime());
		}

	}

	public static String getMinutesBefore(String date, int minutes, String sdf) {
		if (StringUtils.isEmpty(date)) {
			return "";
		}
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateSdf2 = null;

		try {
			if (sdf != null && sdf.length() > 0) {
				dateSdf2 = new SimpleDateFormat(sdf);
				cal.setTime(dateSdf2.parse(date));
			} else {
				cal.setTime(dateSdf.parse(date));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		cal.add(Calendar.MINUTE, -minutes);
		if (sdf != null && sdf.length() > 0) {
			return dateSdf2.format(cal.getTime());
		} else {
			return dateSdf.format(cal.getTime());
		}

	}

	/**
	 * 返回指定日期10天前日期
	 * 
	 * @param days
	 *            多少天前
	 * @return
	 */
	public static String getCurrDayBefore(int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -days);
		return dateSdf.format(cal.getTime());
	}
	
	/**
	 * 返回指定日期10天前日期
	 * 
	 * @param days
	 *            多少天前
	 * @return
	 */
	public static String getCurrDayBefore(int days, String sdf) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -days);
		return new SimpleDateFormat(sdf).format(cal.getTime());
	}
	
	/**
	 * 获取日期月份最后一天
	 * @param date 201707
	 * @return
	 */
	public static String getLastDayOfMonth(String date) {
		int year = Integer.parseInt(date.substring(0,4));
		int month = Integer.parseInt(date.substring(4,6));
		return getLastDayOfMonth(year , month);
	}
	/**
	 * 获取某月的最后一天
	 */
	public static String getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		cal.set(Calendar.MONTH, month - 1);
		// 获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		// 设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		// 格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String lastDayOfMonth = sdf.format(cal.getTime());

		return lastDayOfMonth;
	}
	
	/**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1,Date date2){
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }
    
    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param 2017-05-25
     * @param 2017-05-27
     * @return
     */
    public static int differentDaysByMillisecond(String dateStr1,String dateStr2){
    	Date date1 = null;
    	Date date2 = null;
		try {
			date1 = dateSdf2.parse(dateStr1);
			date2 = dateSdf2.parse(dateStr2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }
}
