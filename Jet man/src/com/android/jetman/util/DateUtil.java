/*****************************************
 * Copyright (c) 2009, Á•ûÂ∑ûÊï∞Á†Å(ÈáëÁ®ã)ÁßëÊäÄÊúâÈôêÂÖ¨Âè∏
 * All rights reserved
 *
 * Êñ? ‰ª? Âê? : DateUtil.java
 * Êë?   Ë¶? : 
 * Áâ?   Êú? : 
 * ‰Ω?   ËÄ? : anfeng
 * ÂàõÂª∫Êó•Êúü : 2008-9-23
 *****************************************/
package com.android.jetman.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Date Utility Class This is used to convert Strings to Dates and Timestamps
 * 
 * <p>
 * <a href="DateUtil.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a> Modified by
 *         <a href="mailto:dan@getrolling.com">Dan Kibler </a> to correct time
 *         pattern. Minutes should be mm not MM (MM is month).
 * @version $Revision: 1.2 $ $Date: 2006/09/28 06:45:21 $
 */
public class DateUtil {
	// ~ Static fields/initializers
	// =============================================

	private static String defaultDatePattern = null;

	private static String timePattern = "HH:mm";

	// ~ Methods
	// ================================================================

	/**
	 * Return default datePattern (yyyy-MM-dd)
	 * 
	 * 
	 * @return a string representing the date pattern on the UI
	 */
	public static synchronized String getDatePattern() {
		// Locale locale = LocaleContextHolder.getLocale();
		// try {
		// defaultDatePattern =
		// ResourceBundle.getBundle(""ApplicationResources";", locale)
		// .getString("date.format");
		// } catch (MissingResourceException mse) {
		defaultDatePattern = "yyyy-MM-dd HH:mm:ss";
		// }

		return defaultDatePattern;
	}

	/**
	 * This method attempts to convert an Oracle-formatted date in the form
	 * dd-MMM-yyyy to mm/dd/yyyy.
	 * 
	 * @param aDate
	 *            date from database as a string
	 * @return formatted string for the ui
	 */
	public static final String getDate(Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate != null) {
			df = new SimpleDateFormat(getDatePattern());
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * This method generates a string representation of a date/time in the
	 * format you specify on input
	 * 
	 * @param aMask
	 *            the date pattern the string is in
	 * @param strDate
	 *            a string representation of a date
	 * @return a converted Date object
	 * @see java.text.SimpleDateFormat
	 * @throws ParseException
	 */
	public static final Date convertStringToDate(String aMask, String strDate)
			throws ParseException {
		SimpleDateFormat df = null;
		Date date = null;
		if (aMask != null && !aMask.equals("") && strDate != null
				&& !strDate.equals("")) {
			df = new SimpleDateFormat(aMask);
			date = df.parse(strDate);
		}

		return (date);
	}

	/**
	 * This method returns the current date time in the format: MM/dd/yyyy HH:MM
	 * a
	 * 
	 * @param theTime
	 *            the current time
	 * @return the current date/time
	 */
	public static String getTimeNow(Date theTime) {
		return getDateTime(timePattern, theTime);
	}

	/**
	 * This method returns the current date in the format: MM/dd/yyyy
	 * 
	 * @return the current date
	 * @throws ParseException
	 */
	public static Calendar getToday() throws ParseException {
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat(getDatePattern());

		// This seems like quite a hack (date -> string -> date),
		// but it works ;-)
		String todayAsString = df.format(today);
		Calendar cal = new GregorianCalendar();
		cal.setTime(convertStringToDate(todayAsString));

		return cal;
	}

	/**
	 * This method generates a string representation of a date's date/time in
	 * the format you specify on input
	 * 
	 * @param aMask
	 *            the date pattern the string is in
	 * @param aDate
	 *            a date object
	 * @return a formatted string representation of the date
	 * 
	 * @see java.text.SimpleDateFormat
	 */
	public static final String getDateTime(String aMask, Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate != null && aMask != null && !aMask.equals("")) {
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * This method generates a string representation of a date based on the
	 * System Property 'dateFormat' in the format you specify on input
	 * 
	 * @param aDate
	 *            A date to convert
	 * @return a string representation of the date
	 */
	public static final String convertDateToString(Date aDate) {
		return getDateTime(getDatePattern(), aDate);
	}

	/**
	 * This method converts a String to a date using the datePattern
	 * 
	 * @param strDate
	 *            the date to convert (in format MM/dd/yyyy)
	 * @return a date object
	 * 
	 * @throws ParseException
	 */
	public static Date convertStringToDate(String strDate) {
		Date aDate = null;

		try {
			aDate = convertStringToDate(getDatePattern(), strDate);
		} catch (ParseException pe) {
			pe.printStackTrace();
			// throw new ParseException(pe.getMessage(), pe.getErrorOffset());
			return aDate;
		}

		return aDate;
	}

	public static String addDays(Date dt, int n) {
		try {
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(dt);
			rightNow.add(Calendar.DATE, n);
			Date dt1 = rightNow.getTime();
			return convertDateToString(dt1);
		} catch (Exception ex) {
			return "";
		}
	}

	public static long dateDiff(Date date1, Date date2) {
		long l = date2.getTime() - date1.getTime();
		long d = l / (24 * 60 * 60 * 1000);
		return d;

	}

	public static Date addDataDays(Date dt, int n) {
		try {
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(dt);
			rightNow.add(Calendar.DATE, n);
			Date dt1 = rightNow.getTime();
			return dt1;
		} catch (Exception ex) {
			return null;
		}
	}

	public static Date addDataMinutes(Date dt, int n) {
		try {
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(dt);
			rightNow.add(Calendar.MINUTE, n);
			Date dt1 = rightNow.getTime();
			return dt1;
		} catch (Exception ex) {
			return null;
		}
	}

	public static Date strToDate(String string) {
		DateFormat df = new SimpleDateFormat(getDatePattern());
		try {
			return df.parse(string);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date convertStringToyyMMdd(String strDate) {
		Date aDate = null;

		try {

			aDate = convertStringToDate("yyyy-MM-dd", strDate);
		} catch (ParseException pe) {
			pe.printStackTrace();
			// throw new ParseException(pe.getMessage(), pe.getErrorOffset());
			return aDate;
		}

		return aDate;
	}

	public static Date convertStringToRightDate(String strDate) {
		Date aDate = null;

		try {

			aDate = convertStringToDate("yyyyMMdd", strDate);
		} catch (ParseException pe) {
			pe.printStackTrace();
			// throw new ParseException(pe.getMessage(), pe.getErrorOffset());
			return aDate;
		}

		return aDate;
	}

	public static Date convert15DigitStringToDate(String strDate) {
		Date aDate = null;

		try {
			if (strDate != null && !strDate.equals("")) {
				strDate = strDate.substring(0, strDate.length() - 3);
				aDate = convertStringToDate("yyyyMMddHHmm", strDate);
			}
		} catch (Exception pe) {
			pe.printStackTrace();
			return aDate;
		}
		return aDate;
	}

	public static Date convert17DigitStringToDate(String strDate) {
		Date aDate = null;

		try {
			if (strDate != null && !strDate.equals("")) {
				strDate = strDate.substring(0, strDate.length() - 3);
				aDate = convertStringToDate("yyyyMMddHHmmss", strDate);
			}
		} catch (Exception pe) {
			pe.printStackTrace();
			return aDate;
		}

		return aDate;
	}

	public static Date convertStringToYMDate(String strDate) {
		Date aDate = null;

		try {

			aDate = convertStringToDate("yyyyMM", strDate);
		} catch (ParseException pe) {
			pe.printStackTrace();
			// throw new ParseException(pe.getMessage(), pe.getErrorOffset());
			return aDate;
		}

		return aDate;
	}

	public static final String convertDateToYYYYMMdd(Date aDate) {
		return getDateTime("yyyyMMdd", aDate);
	}

	public static final String convertDateToYYYYMMddHHmmSS(Date aDate) {
		return getDateTime("yyyyMMddHHmmss", aDate);
	}

	public static final String convertDateToYYYYMMddHHmmzzz(Date aDate) {
		if (aDate == null) {
			return null;
		}
		return getDateTime("yyyyMMddHHmm", aDate) + "+08";
	}

	public static final String convertDateToYYYYMMddHHmm(Date aDate) {
		if (aDate == null) {
			return null;
		}
		return getDateTime("yyyyMMddHHmm", aDate);
	}

	public static final String date2String(Date aDate) {
		if (aDate == null) {
			return "";
		}
		return getDateTime("yyyy-MM-dd HH:mm", aDate);
	}

	public static final String convertDateToYYYYMMddHHmmsszzz(Date aDate) {
		if (aDate == null) {
			return null;
		}
		return getDateTime("yyyyMMddHHmmss", aDate) + "+08";
	}

	public static final String convertDateToYYYYMMddHHmmssSSS(Date aDate) {
		if (aDate == null) {
			return null;
		}
		return getDateTime("yyyyMMddHHmmssSSS", aDate);
	}

	public static final String convertDateToYYYYMMddHH(Date aDate) {
		if (aDate == null) {
			return null;
		}
		return getDateTime("yyyyMMddHH", aDate);
	}

	public static void main(String args[]) {

	}

}
