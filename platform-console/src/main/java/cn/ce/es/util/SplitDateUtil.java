package cn.ce.es.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.ce.es.bean.KeyValueForDate;



/**
 *
 * @Title: SplitDateUtil.java
 * @Description
 * @version V1.0
 * @author yang.yu@300.cn
 * @date dat2017年9月1日 time下午1:45:06
 *
 **/
public class SplitDateUtil {

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static List<KeyValueForDate> getKeyValueForDate(String startDate, String endDate) {

		List<KeyValueForDate> list = null;
		try {
			list = new ArrayList<KeyValueForDate>();

			String firstDay = "";
			String lastDay = "";
			Date d1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSXXX").parse(startDate);// 定义起始日期

			Date d2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSXXX").parse(endDate);// 定义结束日期

			Calendar dd = Calendar.getInstance();// 定义日期实例
			dd.setTime(d1);// 设置日期起始时间
			Calendar cale = Calendar.getInstance();

			Calendar c = Calendar.getInstance();
			c.setTime(d2);

			int startDay = d1.getMinutes();
			int endDay = d2.getMinutes();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSXXX");

			KeyValueForDate keyValueForDate = null;

			while (dd.getTime().before(d2)) {// 判断是否到结束日期
				keyValueForDate = new KeyValueForDate();
				cale.setTime(dd.getTime());
				Calendar nowTime = cale;

				if (dd.getTime().equals(d1)) {
					lastDay = sdf.format(cale.getTime());
					nowTime.add(Calendar.MINUTE, 1);
					keyValueForDate.setStartDate(sdf.format(d1));
					keyValueForDate.setEndDate(sdf.format(nowTime.getTime()));

				} else if (dd.get(Calendar.HOUR) == c.get(Calendar.HOUR)
						&& dd.get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH)
						&& dd.get(Calendar.MONTH) == d2.getMonth() && dd.get(Calendar.YEAR) == c.get(Calendar.YEAR)) {
					firstDay = sdf.format(cale.getTime());

					nowTime.add(Calendar.SECOND, 59);
					lastDay = sdf.format(new Date());
					keyValueForDate.setStartDate(firstDay);
					keyValueForDate.setEndDate(sdf.format(nowTime.getTime()));
				} else {
					// cale.set(Calendar.MINUTE,1);//取第一天
					firstDay = sdf.format(cale.getTime());
					nowTime.add(Calendar.MINUTE, 1);
					keyValueForDate.setStartDate(firstDay);
					keyValueForDate.setEndDate(sdf.format(nowTime.getTime()));

				}
				list.add(keyValueForDate);
				dd.add(Calendar.MINUTE, 1);// 进行当前日期月份加1

			}

			if (endDay < startDay) {
				keyValueForDate = new KeyValueForDate();

				cale.setTime(d2);
				// cale.set(Calendar.MINUTE,1);//取第一天
				firstDay = sdf.format(cale.getTime());

				keyValueForDate.setStartDate(firstDay);
				keyValueForDate.setEndDate(sdf.format(d2));
				list.add(keyValueForDate);
			}
		} catch (ParseException e) {
			return null;
		}

		return list;
	}

	/**
	 * 
	 * @param dateTime
	 * @param type
	 *            1 5分钟，2 30分钟，3 一小时
	 * @return String[] index 0from 1to
	 */
	public static String[] returnForwadTime(String dateTime, String type) {

		long time = Long.valueOf(dateTime);
		String from = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSXXX");
		Date date = new Date(time);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String to = format.format(cal.getTime());
		// ����ʱ��

		if ("1".equals(type)) {

			cal.add(Calendar.MINUTE, -5);
			from = format.format(cal.getTime());
		}
		if ("2".equals(type)) {

			cal.add(Calendar.MINUTE, -30);
			from = format.format(cal.getTime());
		}
		if ("3".equals(type)) {

			cal.add(Calendar.MINUTE, -60);
			from = format.format(cal.getTime());
		}
		String[] strArray = { from, to };
		return strArray;

	}

	public static void main(String[] args) {
		List<KeyValueForDate> list = SplitDateUtil.getKeyValueForDate("2016-08-23T09:55:33.000000000+08:00",
				"2016-08-23T10:55:00.000000000+08:00");

		System.out.println("开始日期--------------结束日期");
		for (KeyValueForDate date : list) {
			System.out.println(date.getStartDate() + "-----" + date.getEndDate());
		}
	}

}
