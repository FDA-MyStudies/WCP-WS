package com.studymetadata.util;

import java.io.FileInputStream;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.studymetadata.bean.FailureResponse;
import com.sun.jersey.core.util.Base64;

/**
 * 
 * @author Mohan
 * @createdOn Jan 4, 2018 3:47:44 PM
 *
 */
public class StudyMetaDataUtil {
	
	private static final Logger LOGGER = Logger.getLogger(StudyMetaDataUtil.class.getName());

	@SuppressWarnings("rawtypes")
	protected static final  HashMap configMap = StudyMetaDataUtil.getAppProperties();

	@SuppressWarnings("rawtypes")
	protected static final HashMap authConfigMap = StudyMetaDataUtil.getAuthorizationProperties();
		
	@SuppressWarnings("unchecked")
	private static final HashMap<String, String> authPropMap = StudyMetaDataUtil.authConfigMap;


	/**
	 * Get properties defined in messageResource and application property files
	 * 
	 * @author Mohan
	 * @return HashMap
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap getAppProperties(){
		LOGGER.info("INFO: StudyMetaDataUtil - getAppProperties() :: starts");
		HashMap hm = new HashMap<String, String>();
		Enumeration<String> keys = null;
		Enumeration<Object> objectKeys = null;
		try {
			ResourceBundle rb = ResourceBundle.getBundle("messageResource");
			keys = rb.getKeys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				String value = rb.getString(key);
				hm.put(key, value);
			}
			ServletContext context = ServletContextHolder.getServletContext();
			Properties prop = new Properties();
			prop.load(new FileInputStream(context.getInitParameter("property_file_location_path")));
			objectKeys = prop.keys();
			while (objectKeys.hasMoreElements()) {
				String key = (String) objectKeys.nextElement();
				String value = prop.getProperty(key);
				hm.put(key, value);
			}
		} catch (Exception e) {
			LOGGER.error("StudyMetaDataUtil - getAppProperties() - ERROR " , e);
		} 
		LOGGER.info("INFO: StudyMetaDataUtil - getAppProperties() :: ends");
		return hm;
	}

	/**
	 * Set failure response for the provided status, title and detail
	 * 
	 * @author Mohan
	 * @param status
	 * @param title
	 * @param detail
	 * @return {@link FailureResponse}
	 */
	public static FailureResponse getFailureResponse(String status, String title, String detail){
		LOGGER.info("INFO: StudyMetaDataUtil - getFailureResponse() :: Starts");
		FailureResponse failureResponse = new FailureResponse();
		try {
			failureResponse.setResultType(StudyMetaDataConstants.FAILURE);
			failureResponse.getErrors().setStatus(status);
			failureResponse.getErrors().setTitle(title);
			failureResponse.getErrors().setDetail(detail);
		} catch (Exception e) {
			LOGGER.error("ERROR: StudyMetaDataUtil - getFailureResponse() ",e);
		}
		LOGGER.info("INFO: StudyMetaDataUtil - getFailureResponse() :: Ends");
		return failureResponse;
	}

	/**
	 * Update response details for the provided status, title, detail and response
	 * 
	 * @author Mohan
	 * @param status
	 * @param title
	 * @param detail
	 * @param response
	 */
	public static void getFailureResponse(String status, String title, String detail, HttpServletResponse response){
		LOGGER.info("INFO: StudyMetaDataUtil - getFailureResponse() :: Starts");
		try {
			response.setHeader("status", status); 
			response.setHeader("title", title);
			response.setHeader("StatusMessage", detail);
		} catch (Exception e) {
			LOGGER.error("ERROR: StudyMetaDataUtil - getFailureResponse() ",e);
		}
		LOGGER.info("INFO: StudyMetaDataUtil - getFailureResponse() :: Ends");
	}

	/**
	 * Get number of days for the provided month and year
	 * 
	 * @author Mohan
	 * @param month
	 * @param year
	 * @return {@link Integer}
	 */
	public static int noOfDaysForMonthYear(int month, int year) {
		LOGGER.info("INFO: StudyMetaDataUtil - noOfDaysForMonthYear() :: Starts");
		int numDays = 30;
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, month-1);
			numDays = calendar.getActualMaximum(Calendar.DATE);
		} catch (Exception e) {
			LOGGER.error("ERROR: StudyMetaDataUtil - noOfDaysForMonthYear() " + e);
		}
		LOGGER.info("INFO: StudyMetaDataUtil - noOfDaysForMonthYear() :: Ends");
		return numDays;
	}

	/**
	 * Get number of days between the provided start and end date's
	 * 
	 * @author Mohan
	 * @param startDate
	 * @param endDate
	 * @return {@link Integer}
	 */
	public static int noOfDaysBetweenTwoDates(String startDate, String endDate) {
		LOGGER.info("INFO: StudyMetaDataUtil - noOfDaysBetweenTwoDates() :: Starts");
		int daysdiff=0;
		try {
			long diff = StudyMetaDataConstants.SDF_DATE.parse(endDate).getTime() - StudyMetaDataConstants.SDF_DATE.parse(startDate).getTime();
			long diffDays = diff / (24 * 60 * 60 * 1000)+1;
			daysdiff = (int) diffDays;
		} catch (Exception e) {
			LOGGER.error("ERROR: StudyMetaDataUtil - noOfDaysBetweenTwoDates() " + e);
		}
		LOGGER.info("INFO: StudyMetaDataUtil - noOfDaysBetweenTwoDates() :: Ends");
		return daysdiff;
	}

	/**
	 * Get server current date
	 * 
	 * @author Mohan
	 * @return {@link String}
	 */
	public static String getCurrentDate() {
		LOGGER.info("INFO: StudyMetaDataUtil - getCurrentDate() :: Starts");
		String dateNow = "";
		try {
			Calendar currentDate = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
			dateNow = formatter.format(currentDate.getTime());
		} catch (Exception e) {
			LOGGER.error("ERROR: StudyMetaDataUtil - getCurrentDate() " + e);
		}
		LOGGER.info("INFO: StudyMetaDataUtil - getCurrentDate() :: Ends");
		return dateNow;
	}

	/**
	 * Get server current date time
	 * 
	 * @author Mohan
	 * @return {@link String}
	 */
	public static String getCurrentDateTime() {
		LOGGER.info("StudyMetaDataUtil: getCurrentDateTime() - Starts ");
		String getToday = "";
		try {
			Date today = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat(StudyMetaDataConstants.SDF_DATE_TIME_PATTERN);
			formatter.setTimeZone(TimeZone.getTimeZone("America/New_York"));
			getToday = formatter.format(today.getTime());
		} catch (Exception e) {
			LOGGER.error("StudyMetaDataUtil - getCurrentDateTime() - ERROR " , e);
		}
		LOGGER.info("StudyMetaDataUtil: getCurrentDateTime() - Ends ");
		return getToday;
	}

	/**
	 * Get formatted date for the provided date, input and output format
	 * 
	 * @author Mohan
	 * @param inputDate
	 * @param inputFormat
	 * @param outputFormat
	 * @return {@link String}
	 */
	public static String getFormattedDate1(String inputDate, String inputFormat, String outputFormat) {
		LOGGER.info("StudyMetaDataUtil: getFormattedDate1() - Starts ");
		String finalDate = "";
		java.sql.Date formattedDate = null; 
		if (inputDate != null && !"".equals(inputDate) && !"null".equalsIgnoreCase(inputDate)){
			try {
				SimpleDateFormat formatter = new SimpleDateFormat(inputFormat);
				formattedDate = new java.sql.Date(formatter.parse(inputDate).getTime());

				formatter = new SimpleDateFormat(outputFormat);
				finalDate = formatter.format(formattedDate);
			} catch (Exception e){
				LOGGER.error("StudyMetaDataUtil: getFormattedDate1() - ERROR",e);
			}
		}
		LOGGER.info("StudyMetaDataUtil: getFormattedDate1() - Ends ");
		return finalDate;
	}

	/**
	 * Get time difference in days hours minutes between two dates 
	 * 
	 * @author Mohan
	 * @param dateOne
	 * @param dateTwo
	 * @return {@link String}
	 */
	public static String getTimeDiffInDaysHoursMins(Date dateOne, Date dateTwo) {
		LOGGER.info("StudyMetaDataUtil: getTimeDiffInDaysHoursMins() - Starts ");
		String diff = "";
		try {
			long timeDiff = Math.abs(dateOne.getTime() - dateTwo.getTime());
			diff = String.format("%d Day(s) %d hour(s) %d min(s)", TimeUnit.MILLISECONDS.toDays(timeDiff), TimeUnit.MILLISECONDS.toHours(timeDiff) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(timeDiff)), 
					TimeUnit.MILLISECONDS.toMinutes(timeDiff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeDiff)));
		} catch (Exception e) {
			LOGGER.error("StudyMetaDataUtil - getTimeDiffInDaysHoursMins() - ERROR " , e);
		}
		LOGGER.info("StudyMetaDataUtil: getTimeDiffInDaysHoursMins() - Ends ");
		return diff;
	}

	/**
	 * Get time difference in days hours minutes between two dates
	 * 
	 * @author Mohan
	 * @param dateOne1
	 * @param dateTwo2
	 * @return {@link String}
	 */
	public static String getTimeDiffInDaysHoursMins(String dateOne1, String dateTwo2) {
		LOGGER.info("StudyMetaDataUtil: getTimeDiffInDaysHoursMins() - Starts ");
		String diff = "";
		try{
			Date dateOne = StudyMetaDataConstants.SDF_DATE_TIME.parse(dateOne1);
			Date dateTwo = StudyMetaDataConstants.SDF_DATE_TIME.parse(dateTwo2);
			long timeDiff = Math.abs(dateOne.getTime() - dateTwo.getTime());
			diff = String.format("%d",TimeUnit.MILLISECONDS.toMinutes(timeDiff));
		}catch(ParseException e){
			LOGGER.error("StudyMetaDataUtil - getTimeDiffInDaysHoursMins() - ERROR " , e);
		}
		LOGGER.info("StudyMetaDataUtil: getTimeDiffInDaysHoursMins() - Ends ");
		return diff;
	}

	/**
	 * Get encoded base64 string for the provided text
	 * 
	 * @author Mohan
	 * @param plainText
	 * @return {@link String}
	 */
	public static String getEncodedStringByBase64(String plainText) {
		LOGGER.info("StudyMetaDataUtil: getEncodedStringByBase64() - Starts ");
		if(StringUtils.isEmpty(plainText)){return "";}
		try {
			byte[]   bytesEncoded = Base64.encode(plainText.getBytes());
			return new String(bytesEncoded);
		} catch (Exception e) {
			LOGGER.error("StudyMetaDataUtil - getEncodedStringByBase64() - ERROR " , e);

		}
		LOGGER.info("StudyMetaDataUtil: getEncodedStringByBase64() - Ends ");
		return "";
	}
	
	/**
	 * Get decoded base64 string for the provided text
	 * 
	 * @author Mohan
	 * @param encodedText
	 * @return
	 */
	public static String getDecodedStringByBase64(String encodedText) {
		LOGGER.info("StudyMetaDataUtil: getDecodedStringByBase64() - Starts ");
		if(StringUtils.isEmpty(encodedText)){return "";}
		try {
			byte[] valueDecoded= Base64.decode(encodedText );
			return  new String(valueDecoded);
		} catch (Exception e) {
			LOGGER.error("StudyMetaDataUtil - getDecodedStringByBase64() - ERROR " , e);
		}
		LOGGER.info("StudyMetaDataUtil: getDecodedStringByBase64() - Ends ");
		return "";

	}

	/**
	 * Get encrypted string for the provided input
	 * 
	 * @author Mohan
	 * @param input
	 * @return {@link String}
	 */
	public static String getEncryptedString(String input) {
		LOGGER.info("INFO: getEncryptedString :: Starts");
		StringBuilder sb = new StringBuilder();
		String encryptValue = "";
		if(StringUtils.isNotEmpty(input)){
			encryptValue = input + StudyMetaDataConstants.PASS_SALT;
			try {
				MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
				messageDigest.update(encryptValue.getBytes("UTF-8"));
				byte[] digestBytes = messageDigest.digest();
				String hex = null;
				for (int i = 0; i < 8; i++) {
					hex = Integer.toHexString(0xFF & digestBytes[i]);
					if (hex.length() < 2)
						sb.append("0");
					sb.append(hex);
				}
			}
			catch (Exception e) {
				LOGGER.error("ERROR: getEncryptedString ",e);
			}
		}
		LOGGER.info("INFO: getEncryptedString :: Ends");
		return sb.toString();
	}


	/**
	 * Get formatted date for the provided date, input and output format
	 * 
	 * @author Mohan
	 * @param inputDate
	 * @param inputFormat
	 * @param outputFormat
	 * @return {@link String}
	 */
	public static String getFormattedDate(String inputDate, String inputFormat, String outputFormat) {
		LOGGER.info("StudyMetaDataUtil: getFormattedDate() - Starts ");
		String finalDate = "";
		java.sql.Date formattedDate = null; 
		if (inputDate != null && !"".equals(inputDate) && !"null".equalsIgnoreCase(inputDate)){
			try {
				SimpleDateFormat formatter = new SimpleDateFormat(inputFormat);
				formattedDate = new java.sql.Date(formatter.parse(inputDate).getTime());

				formatter = new SimpleDateFormat(outputFormat);
				finalDate = formatter.format(formattedDate);
			} catch (Exception e){
				LOGGER.error("ERROR: getFormattedDate ",e);
			}
		}
		LOGGER.info("StudyMetaDataUtil: getFormattedDate() - Ends ");
		return finalDate;
	}

	/**
	 * Get new date time for the provided date time and minutes 
	 * 
	 * @author Mohan
	 * @param dtStr
	 * @param minutes
	 * @return {@link String}
	 */
	public static String addMinutes(String dtStr, int minutes) {
		LOGGER.info("StudyMetaDataUtil: addMinutes() - Starts ");
		String newdateStr = "";
		try {
			SimpleDateFormat date = new SimpleDateFormat(StudyMetaDataConstants.SDF_DATE_TIME_PATTERN);
			Date dt = date.parse(dtStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			cal.add(Calendar.MINUTE, minutes);
			Date newDate = cal.getTime();
			newdateStr = date.format(newDate);
		} catch (ParseException e) {
			LOGGER.error("StudyMetaDataUtil - addMinutes() - ERROR " , e);
		}
		LOGGER.info("StudyMetaDataUtil: addMinutes() - Ends ");
		return newdateStr; 
	}

	/**
	 * Get new date time for the provided date time and days
	 * 
	 * @author Mohan
	 * @param dtStr
	 * @param days
	 * @return {@link String}
	 */
	public static String addDays(String dtStr, int days) {
		LOGGER.info("StudyMetaDataUtil: addDays() - Starts ");
		String newdateStr = "";
		try {
			Date dt = StudyMetaDataConstants.SDF_DATE_TIME.parse(dtStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			cal.add(Calendar.DATE, days);
			Date newDate = cal.getTime();
			newdateStr = StudyMetaDataConstants.SDF_DATE_TIME.format(newDate);
		} catch (ParseException e) {
			LOGGER.error("StudyMetaDataUtil - addDays() - ERROR " , e);
		}
		LOGGER.info("StudyMetaDataUtil: addDays() - Ends ");
		return newdateStr; 
	}

	/**
	 * Get new date time for the provided date time and months
	 * 
	 * @author Mohan
	 * @param dtStr
	 * @param months
	 * @return String
	 */
	public static String addMonth(String dtStr, int months) {
		LOGGER.info("StudyMetaDataUtil: addMonth() - Starts ");
		String newdateStr = "";
		try {
			Date dt = StudyMetaDataConstants.SDF_DATE.parse(dtStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			cal.add(Calendar.MONTH, months);
			Date newDate = cal.getTime();
			newdateStr =StudyMetaDataConstants.SDF_DATE.format(newDate);
		} catch (ParseException e) {
			LOGGER.error("StudyMetaDataUtil - addMonth() - ERROR " , e);
		}
		LOGGER.info("StudyMetaDataUtil: addMonth() - Ends ");
		return newdateStr;

	}

	/**
	 * Get new date time for the provided date time and years
	 * 
	 * @author Mohan
	 * @param dtStr
	 * @param years
	 * @return {@link String}
	 */
	public static String addYear(String dtStr, int years) {
		LOGGER.info("StudyMetaDataUtil: addYear() - Starts ");
		String newdateStr = "";
		try {
			Date dt = StudyMetaDataConstants.SDF_DATE.parse(dtStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			cal.add(Calendar.YEAR, years);
			Date newDate = cal.getTime();
			newdateStr = StudyMetaDataConstants.SDF_DATE.format(newDate);
		} catch (ParseException e) {
			LOGGER.error("StudyMetaDataUtil - addYear() - ERROR " , e);
		}
		LOGGER.info("StudyMetaDataUtil: addYear() - Ends ");
		return newdateStr ;
	}

	/**
	 * Get seconds for the provided date
	 * 
	 * @author Mohan
	 * @param getCurrentDate
	 * @return {@link Long}
	 */
	public static Long getDateToSeconds(String getCurrentDate) {
		LOGGER.info("StudyMetaDataUtil: getDateToSeconds() - Starts ");
		Long getInSeconds = null;
		try {
			Date dt = StudyMetaDataConstants.SDF_DATE.parse(getCurrentDate);
			getInSeconds=dt.getTime();
		} catch (ParseException e) {
			LOGGER.error("StudyMetaDataUtil - getDateToSeconds() - ERROR " , e);
		}
		LOGGER.info("StudyMetaDataUtil: getDateToSeconds() - Ends ");
		return getInSeconds;
	}


	/**
	 * Get date time for the provided seconds and timezone
	 * 
	 * @author Mohan
	 * @param value
	 * @return String
	 */
	public static String getSecondsToDate(String value) {
		LOGGER.info("StudyMetaDataUtil: getSecondsToDate() - Starts ");
		String dateText;
		long getLongValue = Long.parseLong(value);
		Date date=new Date(getLongValue);
		SimpleDateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");// yyyy-MM-dd
		df2.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		dateText = df2.format(date);
		LOGGER.info("StudyMetaDataUtil: getSecondsToDate() - Ends ");
		return dateText;
	}

	/**
	 * Get date time for the provided date and timezone
	 * 
	 * @author Mohan
	 * @param value
	 * @return String
	 */
	public static String getToDate(String value) {
		LOGGER.info("StudyMetaDataUtil: getToDate() - Starts ");
		String dateText;
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");// yyyy-MM-dd
		df2.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		dateText = df2.format(value);
		LOGGER.info("StudyMetaDataUtil: getToDate() - Ends ");
		return dateText;
	}

	/**
	 * Get current date time for UTC timezone
	 * 
	 * @author Mohan
	 * @return {@link String}
	 */
	public static String getCurrentDateTimeInUTC() {
		LOGGER.info("StudyMetaDataUtil: getCurrentDateTimeInUTC() - Starts ");
		String dateNow = null;
		final SimpleDateFormat sdf = new SimpleDateFormat(StudyMetaDataConstants.SDF_DATE_TIME_PATTERN);
		String timeZone = "UTC";
		try {
			String strDate = new Date() + "";
			if(strDate.indexOf("IST") != -1){
				timeZone = "IST";
			}
			sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
			dateNow = sdf.format(new Date());
		} catch (Exception e) {
			LOGGER.error("StudyMetaDataUtil: getCurrentDateTimeInUTC(): ERROR " + e);
		}
		LOGGER.info("StudyMetaDataUtil: getCurrentDateTimeInUTC() - Ends ");
		return dateNow;
	}

	/**
	 * Get the platform type for the provided credentials and type
	 * 
	 * @author Mohan
	 * @param authCredentials
	 * @param type
	 * @return {@link String}
	 */
	public static String platformType(String authCredentials, String type) {
		LOGGER.info("INFO: StudyMetaDataUtil - platformType() - Starts");
		String bundleIdAndAppToken = null;
		String platform = "";
		try{
			if(StringUtils.isNotEmpty(authCredentials) && authCredentials.contains("Basic")){
				final String encodedUserPassword = authCredentials.replaceFirst("Basic"+ " ", "");
				byte[] decodedBytes = Base64.decode(encodedUserPassword);
				bundleIdAndAppToken = new String(decodedBytes, "UTF-8");
				
				if(bundleIdAndAppToken.contains(":")){
					final StringTokenizer tokenizer = new StringTokenizer(bundleIdAndAppToken, ":");
					final String bundleId = tokenizer.nextToken();
					final String appToken = tokenizer.nextToken();
					
					if(authPropMap.containsKey(bundleId) && authPropMap.containsKey(appToken)){
						String appBundleId = "";
						String appTokenId = "";
						for(Map.Entry<String, String> map : authPropMap.entrySet()){
							if(map.getKey().equals(appToken)){
								appTokenId = map.getValue();
							}
							
							if(map.getKey().equals(bundleId)){
								appBundleId = map.getValue();
							}
						}
						
						if(StringUtils.isNotEmpty(appBundleId) && StringUtils.isNotEmpty(appTokenId)){
							final StringTokenizer authTokenizer = new StringTokenizer(appTokenId, ".");
							final String platformType = authTokenizer.nextToken();
							
							if(platformType.equals(StudyMetaDataConstants.STUDY_PLATFORM_ANDROID)){
								switch (type) {
									case StudyMetaDataConstants.STUDY_AUTH_TYPE_PLATFORM: 
										platform = StudyMetaDataConstants.STUDY_PLATFORM_TYPE_ANDROID;
										break;
									case StudyMetaDataConstants.STUDY_AUTH_TYPE_OS: 
										platform = StudyMetaDataConstants.STUDY_PLATFORM_ANDROID;
										break;
									case StudyMetaDataConstants.STUDY_AUTH_TYPE_BUNDLE_ID: 
										platform = bundleId;
										break;
								}
							}else{
								switch (type) {
									case StudyMetaDataConstants.STUDY_AUTH_TYPE_PLATFORM: 
										platform = StudyMetaDataConstants.STUDY_PLATFORM_TYPE_IOS;
										break;
									case StudyMetaDataConstants.STUDY_AUTH_TYPE_OS: 
										platform = StudyMetaDataConstants.STUDY_PLATFORM_IOS;
										break;
									case StudyMetaDataConstants.STUDY_AUTH_TYPE_BUNDLE_ID: 
										platform = bundleId;
										break;
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			LOGGER.error("StudyMetaDataUtil - platformType() :: ERROR", e);
		}
		LOGGER.info("INFO: StudyMetaDataUtil - platformType() - Ends");
		return platform;
	}

	/**
	 * Get properties defined in authorizationResource property file
	 * 
	 * @author Mohan
	 * @return HashMap
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap getAuthorizationProperties(){
		LOGGER.info("INFO: StudyMetaDataUtil - getAuthorizationProperties() :: Starts");
		HashMap hashMap = new HashMap<String, String>();
		ResourceBundle rb = ResourceBundle.getBundle("authorizationResource");
		Enumeration<String> keys = rb.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			String value = rb.getString(key);
			hashMap.put(key, value);
		}
		LOGGER.info("INFO: StudyMetaDataUtil - getAuthorizationProperties() :: Ends");
		return hashMap;
	}

	/**
	 * Get the day for the provided date
	 * 
	 * @author Mohan
	 * @param input
	 * @return String
	 */
	public static String getDayByDate(String input){
		LOGGER.info("StudyMetaDataUtil: getDayByDate() - Starts ");
		String actualDay = "";
		try {
			if(StringUtils.isNotEmpty(input)){
				SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date MyDate = newDateFormat.parse(input);
				newDateFormat.applyPattern(StudyMetaDataConstants.SDF_DAY);
				actualDay = newDateFormat.format(MyDate);
			}
		} catch (Exception e) {
			LOGGER.error("StudyMetaDataUtil - getDayByDate() - ERROR " , e);
		}
		LOGGER.info("StudyMetaDataUtil: getDayByDate() - Ends ");
		return actualDay;
	}

	/**
	 * Get the new date by adding days for the provided date and days
	 * 
	 * @author Mohan
	 * @param input
	 * @param days
	 * @return String
	 */
	public static String addDaysToDate(String input, int days){
		LOGGER.info("StudyMetaDataUtil: addDaysToDate() - Starts ");
		String output = "";
		try {
			Date dt = StudyMetaDataConstants.SDF_DATE.parse(input);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			cal.add(Calendar.DATE, days);
			Date newDate = cal.getTime();
			output = StudyMetaDataConstants.SDF_DATE.format(newDate);
		} catch (ParseException e) {
			LOGGER.error("StudyMetaDataUtil - addDaysToDate() - ERROR " , e);
		}
		LOGGER.info("StudyMetaDataUtil: addDaysToDate() - Ends ");
		return output;
	}

	/**
	 * Get the new date by adding weeks for the provided date and weeks
	 * 
	 * @author Mohan
	 * @param input
	 * @param weeks
	 * @return {@link String}
	 */
	public static String addWeeksToDate(String input, int weeks){
		LOGGER.info("StudyMetaDataUtil: addWeeksToDate() - Starts ");
		String output = "";
		try {
			Date dt = StudyMetaDataConstants.SDF_DATE.parse(input);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			cal.add(Calendar.WEEK_OF_MONTH, weeks);
			Date newDate = cal.getTime();
			output = StudyMetaDataConstants.SDF_DATE.format(newDate);
		} catch (ParseException e) {
			LOGGER.error("StudyMetaDataUtil - addWeeksToDate() - ERROR " , e);
		}
		LOGGER.info("StudyMetaDataUtil: addWeeksToDate() - Ends ");
		return output;
	}

	/**
	 * Get the new date by adding months for the provided date and months
	 * 
	 * @author Mohan
	 * @param input
	 * @param months
	 * @return String
	 */
	public static String addMonthsToDate(String input, int months){
		LOGGER.info("StudyMetaDataUtil: addMonthsToDate() - Starts ");
		String output = "";
		try {
			Date dt = StudyMetaDataConstants.SDF_DATE.parse(input);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			cal.add(Calendar.MONTH, months);
			Date newDate = cal.getTime();
			output = StudyMetaDataConstants.SDF_DATE.format(newDate);
		} catch (ParseException e) {
			LOGGER.error("StudyMetaDataUtil - addMonthsToDate() - ERROR " , e);
		}
		LOGGER.info("StudyMetaDataUtil: addMonthsToDate() - Ends ");
		return output;
	}

	/**
	 * Get formatted date for the provided date time, input and output format
	 * 
	 * @author Mohan
	 * @param input
	 * @param inputFormat
	 * @param outputFormat
	 * @return {@link String}
	 */
	public static String getFormattedDateTimeZone(String input, String inputFormat, String outputFormat){
		LOGGER.info("StudyMetaDataUtil: getFormattedDateTimeZone() - Starts ");
		String output = "";
		try{
			if(StringUtils.isNotEmpty(input)){
				SimpleDateFormat inputSDF = new SimpleDateFormat(inputFormat);
				Date inputDate = inputSDF.parse(input);
				SimpleDateFormat outputSDF = new SimpleDateFormat(outputFormat);
				output = outputSDF.format(inputDate);
			}
		}catch(Exception e){
			LOGGER.error("AuthenticationService - getFormattedDateTimeZone() :: ERROR", e);
		}
		LOGGER.info("StudyMetaDataUtil: getFormattedDateTimeZone() - Ends ");
		return output;
	}
	
	/**
	 * Get the new date by adding seconds for the provided date time and seconds
	 * 
	 * @author Mohan
	 * @param dtStr
	 * @param seconds
	 * @return {@link String}
	 */
	public static String addSeconds(String dtStr, int seconds) {
		LOGGER.info("StudyMetaDataUtil: addSeconds() - Starts ");
		String newdateStr = "";
		try {
			SimpleDateFormat date = new SimpleDateFormat(StudyMetaDataConstants.SDF_DATE_TIME_PATTERN);
			Date dt = date.parse(dtStr);
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			cal.add(Calendar.SECOND, seconds);
			Date newDate = cal.getTime();
			newdateStr = date.format(newDate);
		} catch (ParseException e) {
			LOGGER.error("AuthenticationService - addSeconds() :: ERROR", e);
		}
		LOGGER.info("StudyMetaDataUtil: addSeconds() - Ends ");
		return newdateStr;
	}
	
	/**
	 * Append milliseconds for the image path
	 * 
	 * @author Mohan
	 * @return {@link String}
	 */
	public static String getMilliSecondsForImagePath() {
		LOGGER.info("StudyMetaDataUtil: getMilliSecondsForImagePath() - Starts ");
		String milliSeconds;
		Calendar cal = Calendar.getInstance();
		milliSeconds = "?v="+cal.getTimeInMillis();
		LOGGER.info("StudyMetaDataUtil: getMilliSecondsForImagePath() - Ends ");
		return milliSeconds;
	}
	
	/**
	 * Get the platform bundle identifier for the provided authorization credentials
	 * 
	 * @author Mohan
	 * @param authCredentials
	 * @return {@link String}
	 */
	public static String getBundleIdFromAuthorization(String authCredentials) {
		LOGGER.info("INFO: StudyMetaDataUtil - getBundleIdFromAuthorization() - Starts");
		String bundleIdAndAppToken = null;
		String appBundleId = "";
		try{
			if(StringUtils.isNotEmpty(authCredentials) && authCredentials.contains("Basic")){
				final String encodedUserPassword = authCredentials.replaceFirst("Basic"+ " ", "");
				byte[] decodedBytes = Base64.decode(encodedUserPassword);
				bundleIdAndAppToken = new String(decodedBytes, "UTF-8");
				if(bundleIdAndAppToken.contains(":")){
					final StringTokenizer tokenizer = new StringTokenizer(bundleIdAndAppToken, ":");
					final String bundleId = tokenizer.nextToken();
					final String appToken = tokenizer.nextToken();
					if(authPropMap.containsKey(bundleId) && authPropMap.containsKey(appToken)){
						appBundleId = bundleId;
					}
				}
			}
		}catch(Exception e){
			LOGGER.error("StudyMetaDataUtil - getBundleIdFromAuthorization() :: ERROR", e);
		}
		LOGGER.info("INFO: StudyMetaDataUtil - getBundleIdFromAuthorization() - Ends");
		return appBundleId;
	}
	
	/**
	 * Replace all single quotes with escape character for the provided text
	 * 
	 * @author Mohan
	 * @param activityId
	 * @return {@link String}
	 */
	public static String replaceSingleQuotes(String activityId){
		LOGGER.info("INFO: StudyMetaDataUtil - replaceSingleQuotes() - Starts");
		String newActivityId = activityId;
		if(activityId.indexOf("'") > -1){
			newActivityId = activityId.replaceAll("'", "''");
		}
		LOGGER.info("INFO: StudyMetaDataUtil - replaceSingleQuotes() - Ends");
		return newActivityId;
	} 
}