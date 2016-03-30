package org.spat.wf.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static final long ONEDAY_MILLISECONDS = 1000 * 3600 * 24;
	public static final long ONEHOUR_MILLISECONDS = 1000 * 3600;

	public static final String DATETIME_FORMATSTR = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMATSTR = "yyyy-MM-dd";

	public static Date getToday() {
		return getDate(getNow());
	}

	/**
	 * 取得日期部分
	 * 
	 * @param now
	 * @return
	 */
	public static Date getDate(Date now) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();

	}

	public static Date getNow() {
		return new Date();
	}

	public static Date addYear(Date date, int year) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, year);
		return cal.getTime();
	}

	public static Date addMonth(Date date, int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, month);
		return cal.getTime();
	}

	public static Date addDay(Date date, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, day);
		return cal.getTime();
	}

	public static Date addWeek(Date date, int week) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.WEEK_OF_YEAR, week);
		return cal.getTime();
	}

	public static Date addHour(Date date, int hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, hour);
		return cal.getTime();
	}

	public static Date addMinute(Date date, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minute);
		return cal.getTime();
	}

	public static Date addSecond(Date date, int second) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, second);
		return cal.getTime();
	}

	public static String formatDate(Date date, String formatStr) {
		SimpleDateFormat today_format = new SimpleDateFormat(formatStr);
		return today_format.format(date);
	}

	public static Date parseDate(String dateStr, String formatStr) throws Exception {
		SimpleDateFormat datetime_format = new SimpleDateFormat(formatStr);
		return datetime_format.parse(dateStr);
	}

	/**
	 * day1-day2
	 * @param day1
	 * @param day2
	 * @return
	 */
	public static long betweenDay(Date day1, Date day2) {
		day1 = getDate(day1);
		day2 = getDate(day2);
		return (day1.getTime() - day2.getTime()) / ONEDAY_MILLISECONDS;
	}
	
	public static Date getyyyyMMddHHmmssDate(String date) throws ParseException {
		return new SimpleDateFormat("yyyyMMddHHmmss").parse(date);
	}

	public static String getyyyyMMddHHmmssStr(Date date) throws ParseException {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
	}

	public static long getyyyyMMddHHmmId(Date date) throws ParseException {
		return Long
				.parseLong(new SimpleDateFormat("yyyyMMddHHmm").format(date));
	}

	public static long getyyyyMMddId(Date date) throws ParseException {
		return Long.parseLong(new SimpleDateFormat("yyyyMMdd").format(date));
	}

	public static Date getyyyyMMddDate(String date) throws ParseException {
		return new SimpleDateFormat("yyyyMMdd").parse(date);
	}

	public static String getyyyyMMdd_HHmmss_SSSStr(Date date) {
		return new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(date);
	}

	public static String getyyyyMMddStr(Date date) {
		return new SimpleDateFormat("yyyyMMdd").format(date);
	}

	public static String getNormalDateStr(Date date) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	public static Date getDateFromNormalStr(String str) throws Exception {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
	}

	public static String getChineseDate(String str) {
		String resultStr = str.substring(0, 4) + "年" + str.substring(4, 6)
				+ "月" + str.substring(6, 8) + "日";
		return resultStr;
	}

	public static Date getDateBeforeOrAfterHours(Date curDate, int iHour) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(curDate);
		cal.add(11, iHour);
		return cal.getTime();
	}
	
	public static Date getDateBeforeOrAfterMinutes(Date curDate, int Minute) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(curDate);
		cal.add(Calendar.MINUTE, Minute);
		return cal.getTime();
	}
	

	public static String getDateyyyyMMddAfterNMonth(int n) {
		Calendar c = Calendar.getInstance();
		c.add(2, n);
		Date date = c.getTime();
		return new SimpleDateFormat("yyyyMMdd").format(date);
	}
	
}
