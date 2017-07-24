package io.renren.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.CronExpression;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.spi.OperableTrigger;

public class CronExpressionUtil {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * @param args
	 * @throws ParseException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws ParseException, InterruptedException {
		String cron = "* * * * * ?";
//		cron = "0 25 05 06 06 ? 2017";
//		cron = "0 00 9-18/1 ? * 2,3,4,5,6";
		cron = "0 00 9-18/1 ? * 2,3,4,5,6";
		cron = "0 00 09-16/1 ? * 2,3,4,5,6";
		long l1 = System.currentTimeMillis();
		System.err.println(nextRunTimes(cron, 50));
		long l2 = System.currentTimeMillis();
		System.err.println("耗时：" + (l2 - l1));
		// CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
		// cronTriggerImpl.setCronExpression("0 0 15 5 * ?");//这里写要准备猜测的cron表达式
		// Calendar calendar = Calendar.getInstance();
		// Date now = calendar.getTime();
		// calendar.add(Calendar.YEAR,
		// 2);//把统计的区间段设置为从现在到2年后的今天（主要是为了方法通用考虑，如那些1个月跑一次的任务，如果时间段设置的较短就不足20条)
		// List<Date> dates =
		// TriggerUtils.computeFireTimesBetween(cronTriggerImpl, null, now,
		// calendar.getTime());//这个是重点，一行代码搞定~~
		// System.out.println(dates.size());
		// SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd
		// HH:mm:ss");
		// for(int i =0;i < dates.size();i ++){
		// if(i >19){//这个是提示的日期个数
		// break;
		// }
		// System.out.println(dateFormat.format(dates.get(i)));
		// }
	}

	/**
	 * 下次执行时间
	 * 
	 * @param cron
	 * @return
	 * @throws ParseException
	 */
	public static List<String> nextRunTimes(String cron, int times) throws ParseException {
		CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
		cronTriggerImpl.setCronExpression(cron);// 这里写要准备猜测的cron表达式
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		calendar.add(Calendar.DAY_OF_MONTH, 10000);// 把统计的区间段设置为从现在到2年后的今天（主要是为了方法通用考虑，如那些1个月跑一次的任务，如果时间段设置的较短就不足20条)
		List<String> ret = new ArrayList<String>();
		try {
			List<Date> dates = computeFireTimesBetween(cronTriggerImpl, null, now, calendar.getTime());// 这个是重点，一行代码搞定~~
			for (int i = 0; i < dates.size(); i++) {
				ret.add(sdf.format(dates.get(i)));
				System.err.println("++" + sdf.format(dates.get(i)));
				if(ret.size() == times){
					break;
				}
			}
		} catch (Exception e) {
			return ret;
		}
		return ret;
	}

	public static List<Date> computeFireTimesBetween(OperableTrigger trigg, org.quartz.Calendar cal, Date from,
			Date to) {
		LinkedList<Date> lst = new LinkedList<Date>();

		OperableTrigger t = (OperableTrigger) trigg.clone();

		if (t.getNextFireTime() == null) {
			t.setStartTime(from);
			t.setEndTime(to);
			t.computeFirstFireTime(cal);
		}

		while (true) {
			Date d = t.getNextFireTime();
			if (d != null) {
				if (d.before(from)) {
					t.triggered(cal);
					continue;
				}
				if (d.after(to)) {
					break;
				}
				lst.add(d);
				if (lst.size() >= 50) {
					break;
				}
				t.triggered(cal);
			} else {
				break;
			}
		}

		return java.util.Collections.unmodifiableList(lst);
	}

	/**
	 * 下次执行时间
	 * 
	 * @param cron
	 * @return
	 * @throws ParseException
	 */
	public static String nextRunTime(String cron) throws ParseException {
		CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
		cronTriggerImpl.setCronExpression(cron);// 这里写要准备猜测的cron表达式
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		calendar.add(Calendar.DAY_OF_MONTH, 1);// 把统计的区间段设置为从现在到2年后的今天（主要是为了方法通用考虑，如那些1个月跑一次的任务，如果时间段设置的较短就不足20条)
		try {
			List<Date> dates = TriggerUtils.computeFireTimesBetween(cronTriggerImpl, null, now, calendar.getTime());// 这个是重点，一行代码搞定~~
			if (dates.size() > 0) {
				return sdf.format(dates.get(0));
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	public static boolean isRight(String cron) {
		try {
			List<String> list = nextRunTimes(cron, 5);
			if (list == null || list.size() == 0) {
				return false;
			}
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	/**
	 * cron表达式执行的时间区间，返回毫秒
	 * 
	 * @param cron
	 * @return
	 * @throws ParseException
	 */
	public static long getIntervalTime(String cron) throws ParseException {
		CronExpression cronExpression = new CronExpression(cron);
		Date[] dateArr = new Date[10];
		Date currDate = new Date();
		for (int i = 0; i < dateArr.length; i++) {
			Date nextDate = cronExpression.getNextValidTimeAfter(currDate);
			dateArr[i] = nextDate;
			currDate = nextDate;
		}
		Map<Long, Integer> countMap = new HashMap<Long, Integer>();
		for (int i = 0; i < dateArr.length; i++) {
			if (i == dateArr.length - 1) {
				break;
			}
			long first = dateArr[i].getTime();
			long second = dateArr[i + 1].getTime();
			if (countMap.containsKey(second - first)) {
				Integer integer = countMap.get(second - first);
				countMap.put(second - first, integer + 1);
			} else {
				countMap.put(second - first, 1);
			}
		}
		Iterator<Entry<Long, Integer>> iterator = countMap.entrySet().iterator();
		int maxSize = 0;
		long timeInterval = 0;
		while (iterator.hasNext()) {
			Entry<Long, Integer> next = iterator.next();
			Integer value = next.getValue();
			if (value > maxSize) {
				maxSize = value;
				timeInterval = next.getKey();
			}
		}
		return timeInterval;
	}
}
