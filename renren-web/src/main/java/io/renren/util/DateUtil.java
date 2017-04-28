package io.renren.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	private static SimpleDateFormat dateSdf = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat dateTimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void main(String[] args) {
		System.out.println(dateTimeSdf.format(new Date()));
		System.out.println("getCurrDayStr=" + getCurrDayStr());
		System.out.println("getCurrDayBefore=" + getCurrDayBefore(1));
		System.out.println("getCurrDayBefore=" + getCurrDayBefore("20170423", 1));
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
	public static String getCurrDayBefore(String date, int days) {
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(dateSdf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
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
	public static String getCurrDayBefore(int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -days);
		return dateSdf.format(cal.getTime());
	}
}
