/*
 * HISTORY:
 * 2005-04-13 - Mark Kalbach - Webster Bank Project Source Incident 282
 *      Modifications to allow convertDate() and GetDateString() to return ISO-8601 formatted dates
 *      with a time zone offset.
 * 2005-04-13 - Mark Kalbach - Webster Bank Project Source Incident 4618
 *      Added adjustToLocalTimeZone() method.
 * 2006-07-11 - Ming-Fang Wang for IB incident 7112, 7113
 *      Made modification to getDate, getDateString methods and added fixIfxDateTimeTZone method,
 *      to allow IFX DateTime <=> string parsing conversion done accurately. If previously a program
 *      used IFX_DATETIME_FORMAT pattern, the program does not need to change. But in the future,
 *      the pattern should be IFX_DATATIME_FORMAT_TZ, not IFX_DATETIME_FORMAT. After the change,
 *      an IFX DateTime string that carries time zone and fraction of second will be correctly converted 
 *      to a Date object.
 */

package com.fnis.xes.services.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;

import com.fnf.jef.formatter.ParseException;
import com.fnf.jef.formatter.StringDateFormatterParser;
import com.fnf.xes.framework.ServiceException;
import com.fnf.xes.framework.dhm.conversion.Utility;
import com.fnis.xes.services.IdConstants;
import org.joda.time.DateTime;

/**
 * Provides functionality for manipulating date/time formats, typically to
 * translating dates between host system formats and IFX-compliant formats.
 * 
 * @author SMohan
 * @author Mark Kalbach
 */
public class DateUtil {

	public static final String IFX_DATE_FORMAT_WITH_TZOFFSET = "yyyy-MM-ddZ";

	public static final String IFX_DATE_FORMAT = "yyyy-MM-dd";

	public static final String MMDDYYYY_WITH_SLASHES = "MM/dd/yyyy";

	public static final String YYYYMM_WITH_DASHES = "yyyy-MM";

	public static final String YYYYMMDD = "yyyyMMdd";
	
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

	public static final String IFX_DATETIME_FORMAT_TZ = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ";

	public static final String IFX_DATETIME_FORMAT = "yyyy-MM-dd'T'hh:mm:ss.000000-00:00";

	public static final String DD_MON_YYYY = "dd-MMM-yyyy";

	// commented to update the format to only Date without time. This date
	// format is only used by the UCS
	// host adapters. This was changed to overcome the oracle driver problem.
	// public static final String DD_MON_YYYY_WITH_TIME = "dd-MMM-yyyy
	// HH:mm:ss";
	public static final String DD_MON_YYYY_WITH_TIME = "dd-MMM-yyyy";

	public static final String YYYY_MM_DD_WITH_TIME = "yyyy-MM-dd HH:mm:ss";

	public static final String IB_DATETIME_FORMAT = "yyyy-MM-dd'T'hh:mm:ss";

	public static final String MMDDYYYY = "MMddyyyy";

	public static final String IFX_TIME_FORMAT_HH = "HH:mm:ss.000000Z";
	
	public static final String MM_YYYY_DD = "MM-yyyy-dd";
	public static final Calendar startPoint = new GregorianCalendar(1840, 11, 31, 0, 0, 0);
	public static final long startPointMillis = startPoint.getTimeInMillis();
	public static final long MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;

	public static final String CC_EXP_DT_FORMAT = "MM/yy"; 
	/**
	 * Converts date from inputFormat to outputFormat. Basically a wrapper for
	 * com.fnf.jef.formatter.StringDateFormatterParser.
	 * 
	 * @param inputDate
	 *            Date string to be converted
	 * @param inputFormat
	 *            A <code>java.text.SimpleDateFormat</code> pattern
	 *            representing the format of inputDate
	 * @param outputFormat
	 *            A <code>java.text.SimpleDateFormat</code> pattern
	 *            representing the format of the String to be returned.
	 * 
	 * @return A String containing the date in the format specified by
	 *         outputFormat.
	 * 
	 * @throws ServiceException
	 *             if null or zero-length input is given or if a date parsing
	 *             error occurs.
	 */
	public static String convertDate(String inputDate, String inputFormat,
			String outputFormat) throws ServiceException {
		if (!isValidDate(inputDate))
			return null;
		if (inputDate == null || inputDate.length() == 0) {
			throw new ServiceException(IdConstants.DATE_UTIL_EMPTY_INPUT,
					"Cannot convert empty input date.");
		}
		if (inputFormat.equals(IFX_DATETIME_FORMAT)
				|| inputFormat.equals(IFX_DATETIME_FORMAT_TZ)) {
			String pattern = "(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2}).*[a-zA-Z0-9]";
			if (!Pattern.matches(pattern, inputDate)) {
				throw new ServiceException(IdConstants.DATE_UTIL_PARSE_ERROR,
						"Error parsing date.");
			}
			inputDate = fixIfxDateTimeTZone(inputDate);
			inputFormat = IFX_DATETIME_FORMAT_TZ;
		}

		String convertedDate = "";
		try {
			StringDateFormatterParser dateParser = new StringDateFormatterParser();
			String parseString = "storePattern=" + outputFormat
					+ "&formatPattern=" + inputFormat;
			convertedDate = (String) dateParser.parse(inputDate, parseString,
					Locale.getDefault());
		} catch (ParseException e) {
			throw new ServiceException(IdConstants.DATE_UTIL_PARSE_ERROR,
					"Error parsing date.", e);
		}

		// If pattern in outputFormat contained a RFC 822 time zone offset,
		// ensure a colon is
		// inserted therein.
		if (outputFormat.equals(IFX_DATETIME_FORMAT_TZ)) {
			convertedDate = fixFraction(convertedDate);
			convertedDate = addColonToTimeZone(convertedDate);
		} else if (outputFormat.substring(outputFormat.length() - 1)
				.equals("Z")) {
			convertedDate = addColonToTimeZone(convertedDate);
		}
		return convertedDate;
	}

	/**
	 * Returns a java.util.Date object constructed from an input string that
	 * matches the given pattern.
	 */
	public static Date getDate(String dateString, String pattern)
			throws ServiceException {
		if (!isValidDate(dateString))
			return null;

		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat();
			if (pattern.equals(IFX_DATETIME_FORMAT)
					|| pattern.equals(IFX_DATETIME_FORMAT_TZ)) {
				dateString = fixIfxDateTimeTZone(dateString);
				pattern = IFX_DATETIME_FORMAT_TZ;
			}
			sdf.applyPattern(pattern);
			sdf.setLenient(false);
			date = sdf.parse(dateString);
		} catch (java.text.ParseException e) {
			throw new ServiceException(IdConstants.DATE_UTIL_PARSE_ERROR,
					"Error parsing date.", e);
		}

		return date;
	}

	/**
	 * Returns a java.util.Date object constructed from an input string that
	 * matches the given pattern.
	 */
	public static java.sql.Date getSqlDate(String dateString, String pattern)
			throws ServiceException {
		if (!isValidDate(dateString))
			return null;

		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat();
			if (pattern.equals(IFX_DATETIME_FORMAT)
					|| pattern.equals(IFX_DATETIME_FORMAT_TZ)) {
				dateString = fixIfxDateTimeTZone(dateString);
				pattern = IFX_DATETIME_FORMAT_TZ;
			}
			sdf.applyPattern(pattern);
			sdf.setLenient(false);
			date = sdf.parse(dateString);
		} catch (java.text.ParseException e) {
			throw new ServiceException(IdConstants.DATE_UTIL_PARSE_ERROR,
					"Error parsing date.", e);
		}

		return new java.sql.Date(date.getTime());
	}
	
	/**
	 * Converts a <code>java.util.Date</code> object to a String, with an
	 * applied formatting pattern. Basically a wrapper for
	 * <code>java.text.SimpleDateFormat</code>.
	 * 
	 * @param inputDate
	 *            Date object to be formatted into a String.
	 * @param pattern
	 *            a java.text.SimpleDateFormat pattern representing the format
	 *            of the String to be returned.
	 */
	public static String getDateString(Date inputDate, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		if (pattern.equals(IFX_DATETIME_FORMAT)) {
			pattern = IFX_DATETIME_FORMAT_TZ;
		}
		sdf.applyPattern(pattern);
		String output = sdf.format(inputDate);

		// If pattern contained a RFC 822 time zone offset, ensure a colon is
		// inserted therein.

		if (pattern.equals(IFX_DATETIME_FORMAT_TZ)) { // need to fix the
			// fraction
			output = fixFraction(output);
			output = addColonToTimeZone(output);
		} else if (pattern.substring(pattern.length() - 1).equals("Z")) {
			output = addColonToTimeZone(output);
		}
		return output;
	}

	/**
	 * Returns the same year, month, day 'date' as the input Calendar no matter
	 * which time zone the input Calendar has.
	 * 
	 * @param cal
	 * @return
	 */
	public static Date getDateThisTimeZone(Calendar cal) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		c.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		c.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	/**
	 * Returns the same year, month, day 'date' as the input Calendar no matter
	 * which time zone the input Calendar has.
	 * 
	 * @param cal
	 * @return Calendar
	 */
	public static Calendar getCalendarThisTimeZone(Calendar cal) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		c.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		c.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
		return c;
	}

	/**
	 * Modifies a Calendar object by setting its internal time and time zone so
	 * the year, month, day, hour, minute and second do not change, but its time
	 * zone is becomes the local time zone. For instance, if the internal time
	 * in the passed Calendar object is 16-May-2005 00:00:00 GMT, the internal
	 * time in the returned Calendar object will be 16-May-2005 00:00:00 EST
	 * (assuming the local server resides in the United States Eastern time
	 * zone.) Note that this method differs from Calendar.setTimeZone(), which
	 * does not change the Calendar's internal time, but merely sets its time
	 * zone (e.g. 16-May-2005 00:00:00 GMT becomes 15-May-2005 17:00:00 EST).
	 * 
	 * @param cal
	 *            Calendar object whose internal time is to be adjusted to the
	 *            local time zone
	 * @return Calendar object whose year, month, day, hour, minute and second
	 *         match that of the passed Calendar object, and whose time zone is
	 *         that of the local server.
	 */
	public static Calendar adjustToLocalTimeZone(Calendar cal) {
		Calendar localCal = new GregorianCalendar(cal.get(Calendar.YEAR), cal
				.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), cal
				.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal
				.get(Calendar.SECOND));
		cal.setTime(localCal.getTime());
		cal.setTimeZone(localCal.getTimeZone());
		return cal;
	}

	/*
	 * The method to convert Calendar object to String object of pattern
	 * (CCYYMMDD)
	 * 
	 * @param cal - Calendar object @return String - converted date of pattern
	 * 'CCYYMMDD' @exception ServiceException
	 */
	public static String getDateString(Calendar cal) throws ServiceException {

		if (cal == null) {
			throw new ServiceException(IdConstants.DATE_UTIL_EMPTY_INPUT,
					"Cannot convert empty input date.");
		}
		// Get the month from the Calendar object and increament 1
		// as for the month Calender object starts from 0.

		String sMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);

		// If length of sMonth, pad a zero to it.
		if (sMonth.length() == 1)
			sMonth = "0" + sMonth;

		// Get the Day_Of_Month from the Calendar object.
		String sDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

		// If length of sDay, pad a zero to it.
		if (sDay.length() == 1)
			sDay = "0" + sDay;

		// Get the Year from the Calendar object.
		String sYear = String.valueOf(cal.get(Calendar.YEAR));

		// return the concatenation of the Year, Month, Day values to the caller
		StringBuffer retDt = new StringBuffer();
		retDt.append(sYear).append(sMonth).append(sDay);

		// return sYear + sMonth + sDay;
		return retDt.toString();
	}

	/*
	 * The method to convert Calendar object to String object of pattern
	 * (CCYYMMDD)
	 * 
	 * @param cal - Calendar object @return String - converted date of pattern
	 * 'CCYYMMDD' @exception ServiceException
	 */
	public static String getDateString(Calendar cal, String dateFormat)
			throws ServiceException {

		Date date = getDateThisTimeZone(cal);
		return getDateString(date, dateFormat);
		/*
		 * if (dateFormat.equals(IFX_DATETIME_FORMAT)) {
		 * dateFormat=IFX_DATETIME_FORMAT_TZ; } SimpleDateFormat sdf = new
		 * SimpleDateFormat(dateFormat); String strDate = sdf.format(date); if
		 * (dateFormat.equals(IFX_DATETIME_FORMAT_TZ)) {
		 * strDate=fixFraction(strDate); } return strDate;
		 */
	}

	/**
	 * Inserts a colon two characters from the end of the passed String. Used to
	 * insure the time zone offset in a date String containing a trailing RFC
	 * 822 4-digit time zone offset, without a colon, e.g. 2005-04-18-0500,
	 * contains a colon, e.g. 2005-04-18-05:00. Dates retrieved via other
	 * methods in this class using the {@link #IFX_DATE_FORMAT_WITH_TZOFFSET}
	 * pattern will have such a trailing RFC 822 4-digit time zone offset.
	 * 
	 * @param dateString
	 *            String containing a date with a trailing RFC 822 4-digit time
	 *            zone.
	 */

	private static String addColonToTimeZone(String dateString) {
		return dateString.substring(0, dateString.length() - 2) + ":"
				+ dateString.substring(dateString.length() - 2);
	}

	/**
	 * Returns a Calender object constructed from an input string that matches
	 * the given pattern.
	 * 
	 * @param dateString,
	 *            a String object
	 * @param pattern,
	 *            a String object, indicates out date format
	 * @return a Calendar object
	 * @exception ServiceException
	 */
	public static Calendar getCalendar(String dateString, String pattern)
			throws ServiceException {

		if (!isValidDate(dateString))
			return null;
		Calendar cal;
		try {
			cal = Calendar.getInstance();
			if (pattern.equals(IFX_DATETIME_FORMAT)) {
				pattern = IFX_DATETIME_FORMAT_TZ;
				dateString = fixIfxDateTimeTZone(dateString);
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			/*
			 * setting the calendar object on the date format objects prevents
			 * time zone offset translation from occurring.
			 */
			dateFormat.setCalendar(cal);

			// Specify the date/time parsing is not to be lenient.
			dateFormat.setLenient(false);
			// Parse the dateString to Date object
			Date date = dateFormat.parse(dateString);
			cal.setTime(date);
		} catch (java.text.ParseException e) {
			throw new ServiceException(IdConstants.DATE_UTIL_PARSE_ERROR,
					"Error parsing date.", e);
		}
		return cal;
	}

	/**
	 * This method returns a date time string that is suitable for using the
	 * IFX_DATETIME_FORMAT_TZ formatting pattern. An input string must be a
	 * valid IFX date time string which can have these forms: yyyy-MM-dd,
	 * yyyy-MM-ddTHH:mm, yyyy-MM-ddTHH:mm:ss, yyyy-MM-ddTHH:mm:ss.SSSSSS,
	 * yyyy-MM-ddTHH:mm:ss.SSSSSSZ yyyy-MM-ddTHH:mmZ, yyyy-MM-ddTHH:mm:ssZ
	 * 
	 * @param strDateTime
	 *            a string in one of the above form
	 * @return a string that is suitable to parse using the
	 *         IFX_DATETIME_FORMAT_TZ pattern with SimpleDateFormat program.
	 */
	public static String fixIfxDateTimeTZone(String strDateTime) {
		strDateTime = strDateTime.trim();
		int i = strDateTime.length();
		String str;
		if (i == 10) { // yyyy-MM-dd
			//str = "T00:00:00.000" + getTimeZone();
			str = "T00:00:00.000" + "-0000";
			strDateTime = strDateTime + str;
		} else if (i == 16) { // yyyy-MM-ddTHH:mm
			str = ":00.000" + getTimeZone();
			strDateTime = strDateTime + str;
		} else if (i == 19) { // yyyy-MM-ddTHH:mm:ss
			str = ".000" + getTimeZone();
			strDateTime = strDateTime + str;
		} else if (i == 22) { // yyyy-MM-ddTHH:mm:ss.SZ
			// remove the last 'Z'
			strDateTime = strDateTime.substring(0, 21) + "00";
			strDateTime = strDateTime + getTimeZone();
		} else if (i == 23) { // yyyy-MM-ddTHH:mm:ss.SSZ
			// remove the last 'Z'
			strDateTime = strDateTime.substring(0, 22) + "0";
			strDateTime = strDateTime + getTimeZone();
		} else if (i == 24) { // yyyy-MM-ddTHH:mm:ss.SSSZ
			// remove the last 'Z'
			strDateTime = strDateTime.substring(0, 23);
			strDateTime = strDateTime + getTimeZone();
		} else if (i == 26) { // yyyy-MM-ddTHH:mm:ss.SSSSSS
			// remove the last 3 digits of fraction of second
			strDateTime = strDateTime.substring(0, 23);
			strDateTime = strDateTime + getTimeZone();
		} else if (i == 32) {// yyyy-MM-ddTHH:mm:ss.SSSSSSZ
			// remove the last 3 digits of fraction of second
			strDateTime = strDateTime.substring(0, 23)
					+ strDateTime.substring(26);
			// remove the colon separating hour and minute figures in the time
			// zone
			i = strDateTime.lastIndexOf(':');
			strDateTime = strDateTime.substring(0, i)
					+ strDateTime.substring(i + 1);
		} else if (i == 17) { // yyyy-MM-ddTHH:mmZ
			strDateTime = strDateTime.substring(0, 16) + ":00.000"
					+ strDateTime.substring(16);
			// remove the colon separating hour and minute figures in the time
			// zone
			i = strDateTime.lastIndexOf(':');
			strDateTime = strDateTime.substring(0, i)
					+ strDateTime.substring(i + 1);
		} else if (i == 25) { // yyyy-MM-ddTHH:mm:ssZ
			strDateTime = strDateTime.substring(0, 19) + ".000"
					+ strDateTime.substring(19);
			// remove the colon separating hour and minute figures in the time
			// zone
			i = strDateTime.lastIndexOf(':');
			strDateTime = strDateTime.substring(0, i)
					+ strDateTime.substring(i + 1);
		}
		return strDateTime;
	}

	private static String getTimeZone() {
		SimpleDateFormat sdf = new SimpleDateFormat("Z");
		Date dt = new Date();
		return sdf.format(dt);
	}

	private static String fixFraction(String strIFXDateTime) {
		// get the last 3 digits of the fraction
		String str = strIFXDateTime.substring(23, 26);
		strIFXDateTime = strIFXDateTime.substring(0, 20) + str + "000"
				+ strIFXDateTime.substring(26);
		return strIFXDateTime;
	}

	public static boolean isValidDate(String dateString) {
		if (dateString == null || dateString.trim().equals("")
				|| dateString.trim().equals("0")
				|| dateString.trim().equals("00000000")) {
			return false;
		}
		return true;
	}
	
	/**
	 * Converts a Java Date object into a long representing the number of days
	 * since December 31st, 1840.
	 * 
	 * @author lzap
	 * @param d The Date to convert
	 * @return long The number of days since December 31st, 1840
	 */
	public static long toProfileDate(Date d) {
		DateTime dateTime = new DateTime(d);
		long dstOffset = dateTime.getZone().getOffset(d.getTime());
		long difference = d.getTime() - startPointMillis + dstOffset;
		return difference / MILLISECONDS_IN_DAY;
	}
	
	public static long toProfileDate(String ifxDate) throws ServiceException {
		Date date = getDate(ifxDate, IFX_DATE_FORMAT);
		return toProfileDate(date);
	}
	
	
	/**
	 * Returns a java.util.Date object constructed from an input string that
	 * matches the given pattern.
	 */
	public static Timestamp getSqlTimestamp(String dateString, String pattern)
			throws ServiceException {
		if (!isValidDate(dateString))
			return null;

		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat();
			if (pattern.equals(IFX_DATETIME_FORMAT)
					|| pattern.equals(IFX_DATETIME_FORMAT_TZ)) {
				dateString = fixIfxDateTimeTZone(dateString);
				pattern = IFX_DATETIME_FORMAT_TZ;
			}
			sdf.applyPattern(pattern);
			sdf.setLenient(false);
			date = sdf.parse(dateString);
		} catch (java.text.ParseException e) {
			throw new ServiceException(IdConstants.DATE_UTIL_PARSE_ERROR,
					"Error parsing date.", e);
		}

		return new java.sql.Timestamp(date.getTime());
	}

	public static String getDateString(Date inputDate, String timeValue, String pattern) {
		String dateString = null;
		if(inputDate!=null) {
			Date dateValue = inputDate;
			if(StringUtils.isNotEmpty(timeValue)) {
				String[] timeArr = timeValue.split(":");
				dateValue = DateUtils.setHours(dateValue, NumberUtils.createNumber(timeArr[0]).intValue());
				dateValue = DateUtils.setMinutes(dateValue, NumberUtils.createNumber(timeArr[1]).intValue());
				dateValue = DateUtils.setSeconds(dateValue, NumberUtils.createNumber(timeArr[2]).intValue());
			}
			dateString = getDateString(dateValue, pattern);
		}
		return dateString;
	}
	
	/**
	 * 
	 * @param cal - value in IFX Date Format
	 * @return - Profile julian Date 
	 */
	public static String getJulianDate(Calendar cal) {
		if(cal != null){
			return DateUtil.toProfileDate(cal.getTime())+"";
		}
		return "";
	}
	/**
	 * 
	 * @param date
	 * @return
	 * @throws ServiceException
	 */
	public static String getJulianDate(String date)throws ServiceException {
		return Utility.date_to_julian(DateUtil.getDateString(DateUtil.getDate(date,DateUtil.IFX_DATETIME_FORMAT), DateUtil.IFX_DATE_FORMAT));
	}
	
	/**
	 * 
	 * @param rsValue
	 * @param ifxPattern
	 * @return
	 */
	public static String getDateString(String rsValue, String ifxPattern) throws ServiceException {
		return DateUtil.getDateString(DateUtil.getDate(rsValue,DateUtil.IFX_DATE_FORMAT), ifxPattern); 
	}
	/**
	 * @param julianDt
	 * @return
	 */
	public static Calendar fromJulianToDate(String julianDt) throws ServiceException {
		if (julianDt == null || julianDt.length() == 0) {
			return null;
		}
		int day = Integer.parseInt(julianDt);
		Calendar theDate = (Calendar) startPoint.clone();
		theDate.add(Calendar.DAY_OF_YEAR, day);
		
		return DateUtil.getCalendar(DateUtil.getDateString(theDate.getTime(), IFX_DATE_FORMAT), DateUtil.IFX_DATE_FORMAT);
	}
}
