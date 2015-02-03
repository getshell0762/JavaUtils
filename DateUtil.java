package com.elanbase.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import javax.sql.RowSet;

import com.elanbase.db.DbAccess;
import com.elanbase.db.DbUtil;
import com.elanbase.exceptions.DAOException;
import com.elanbase.properties.SystemProperties;
import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * 日期工具类，提供有关日期操作方面的方法。
 * 
 * @author 曾小平
 * @version 1.0
 */

public class DateUtil {

	/**
	 * 时间格式
	 */
	public final static String TIME_FORMAT = "HH:mm:ss:SS";

	/**
	 * TIMESTAMP的时间格式
	 */
	public final static String TIME_FORMAT_TIMES = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 缺省短日期格式
	 */
	public final static String DEFAULT_SHORT_DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * 缺省短日期格式
	 */
	public final static String DEFAULT_SHORT_DATE_FORMAT_ZH = "yyyy年M月d日";

	/**
	 * 缺省长日期格式
	 */
	public final static String DEFAULT_LONG_DATE_FORMAT = DEFAULT_SHORT_DATE_FORMAT
			+ " " + TIME_FORMAT;

	/**
	 * Java能支持的最小日期字符串（yyyy-MM-dd）。
	 */
	public final static String JAVA_MIN_SHORT_DATE_STR = "1970-01-01";

	/**
	 * Java能支持的最小日期字符串（yyyy-MM-dd HH:mm:ss:SS）。
	 */
	public final static String JAVA_MIN_LONG_DATE_STR = "1970-01-01 00:00:00:00";

	/**
	 * Java能支持的最小的Timestamp。
	 */
	public final static Timestamp JAVA_MIN_TIMESTAMP = convertStrToTimestamp(JAVA_MIN_LONG_DATE_STR);

	/**
	 * 把字符串转换为Timestamp类型，对于短日期格式，自动把时间设为系统当前时间。
	 * 
	 * @return Timestamp
	 * @see #convertStrToTimestamp(String,boolean)
	 */
	public static Timestamp convertStrToTimestamp(String dateStr) {
		return convertStrToTimestamp(dateStr, false);
	}

	/**
	 * 把字符串转换为Timestamp类型，对于短日期格式，自动时间设为0。
	 * 
	 * @return Timestamp
	 * @see #convertStrToTimestamp(String,boolean)
	 */
	public static Timestamp convertStrToTimestampZero(String dateStr) {
		return convertStrToTimestamp(dateStr, true);
	}

	/**
	 * 把字符串转换为Timestamp类型。
	 * 
	 * @param dateStr -
	 *            日期字符串，只支持"yyyy-MM-dd"和"yyyy-MM-dd HH:mm:ss:SS"两种格式。
	 *            如果为"yyyy-MM-dd"，系统会自动取得当前时间补上。
	 * @param addZeroTime -
	 *            当日期字符串为"yyyy-MM-dd"这样的格式时，addZeroTime为true表示
	 *            用0来设置HH:mm:ss:SS，否则用当前Time来设置。
	 * @return Timestamp
	 */
	private static Timestamp convertStrToTimestamp(String dateStr,
			boolean addZeroTime) {
		if (dateStr == null) {
			return null;
		}

		String dStr = dateStr.trim();
		if (dStr.indexOf(" ") == -1) {
			if (addZeroTime) {
				dStr = dStr + " 00:00:00:00";
			} else {
				dStr = dStr + " " + getCurrDateStr(DateUtil.TIME_FORMAT);
			}
		}

		Date utilDate = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				DEFAULT_LONG_DATE_FORMAT);

		try {
			utilDate = simpleDateFormat.parse(dStr);
		} catch (Exception ex) {
			throw new RuntimeException("DateUtil.convertStrToTimestamp(): "
					+ ex.getMessage());
		}

		return new Timestamp(utilDate.getTime());
	}

	/**
	 * 把yyyy-MM-dd HH:mm:ss:SS类型的字符型转换成timestamp
	 * 
	 * @param dateStr
	 * @return
	 */
	public static Timestamp convertStringToTimestamp(String dateStr) {
		if (dateStr == null || dateStr.trim().equals("")) {
			return null;
		}

		Date utilDate = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				TIME_FORMAT_TIMES);

		try {
			utilDate = simpleDateFormat.parse(dateStr);			
		} catch (Exception ex) {
			throw new RuntimeException("DateUtil.convertStringToTimestamp(): "
					+ ex.getMessage());
		}

		return new Timestamp(utilDate.getTime());
	}
	
	/**
	 * @author dameng
	 * 把timestamp转成sqlDate
	 */
	public static java.sql.Date convertTimeToDate(Timestamp ts){
		if (ts == null ) {
			return null;
		}
		try {
			long lms = ts.getTime(); 
			Date date = new Date(lms); 
			return DateUtil.convertToSqlDate(date);
			
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * 得到系统当前时间的Timestamp对象
	 * 
	 * @return 系统当前时间的Timestamp对象
	 */
	public static Timestamp getCurrTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * 得到系统当前时间的Date对象
	 * 
	 * @return 系统当前时间的sqlDate对象
	 */
	public  static java.sql.Date getNowDate(){
		java.sql.Date  oNowDate=new java.sql.Date(new java.util.Date().getTime());//得到系统当前时间
		return oNowDate;
	}
	
	public static java.util.Date getNowTime(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式，不显示毫秒
		Timestamp now = new Timestamp(System.currentTimeMillis());//获取系统当前时间
		return now;
	}
	
	/**
	 * <p>
	 * 取得当前日期，并将其转换成格式为"dateFormat"的字符串 例子：假如当前日期是 2003-09-24 9:19:10，则：
	 * 
	 * <pre>
	 *            getCurrDateStr(&quot;yyyyMMdd&quot;)=&quot;20030924&quot;
	 *            getCurrDateStr(&quot;yyyy-MM-dd&quot;)=&quot;2003-09-24&quot;
	 *            getCurrDateStr(&quot;yyyy-MM-dd HH:mm:ss&quot;)=&quot;2003-09-24 09:19:10&quot;
	 * </pre>
	 * 
	 * </p>
	 * 
	 * @param dateFormat
	 *            String 日期格式字符串
	 * @return String
	 */
	public static String getCurrDateStr(String dateFormat) {
		return convertDateToStr(new Date(), dateFormat);
	}

	/**
	 * 将日期类型转换成指定格式的日期字符串
	 * 
	 * @param date
	 *            待转换的日期
	 * @param dateFormat
	 *            日期格式字符串
	 * @return String
	 */
	public static String convertDateToStr(Date date, String dateFormat) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(date);
	}

	/**
	 * 将指定格式的字符串转换成日期类型
	 * 
	 * @param date
	 *            待转换的日期字符串
	 * @param dateFormat
	 *            日期格式字符串
	 * @return Date
	 */
	public static Date convertStrToDate(String dateStr, String dateFormat) {
		if (dateStr == null || dateStr.equals("")) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			return sdf.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("DateUtil.convertStrToDate():"
					+ e.getMessage());
		}
	}
	

	
	/**
	 * 将指定格式的字符串转换成日期类型
	 * 
	 * @param date
	 *            待转换的日期字符串
	 * @param dateFormat
	 *            日期格式字符串
	 * @return Date
	 */
	public static Date convertStrToDateNoError(String dateStr, String dateFormat) {
		if (dateStr == null || dateStr.equals("")) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			return sdf.parse(dateStr);
		} catch (Exception e) {
			throw new RuntimeException("DateUtil.convertStrToDate():"
					+ e.getMessage());
		}
	}
	
	/**
	 * 将指定格式的字符串转换成日期类型(Sql)
	 * @param date
	 *            待转换的日期字符串
	 * @param dateFormat
	 *            日期格式字符串
	 * @return Date
	 */
	public static java.sql.Date convertStrToSqlDate(String dateStr, String dateFormat) {
		if (dateStr == null || dateStr.equals("")) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		try {
			java.util.Date uDate=sdf.parse(dateStr);
			return DateUtil.convertToSqlDate(uDate);
		} catch (Exception e) {
			throw new RuntimeException("DateUtil.convertStrToDate():"
					+ e.getMessage());
		}
	}
	
	/**    
	* 检验输入是否为正确的日期格式(不含秒的任何情况)    
	* @param sourceDate    
	* @return    
	*/     
	public static boolean checkDate(String sourceDate ){     
		if(sourceDate==null){     
			return false;     
		}     
		try {     
			SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_SHORT_DATE_FORMAT);     
			dateFormat.setLenient(false);     
			dateFormat.parse(sourceDate);     
			return true;     
		} 
		catch (Exception e) {   
			e.printStackTrace();
		}     
		return false;     
	}
	
	/**    
	* 检验输入是否为正确的日期格式(不含秒的任何情况),不扑捉异常    
	* @param sourceDate    
	* @return    
	*/     
	public static boolean isRightDate(String sourceDate,String format){     
		if(sourceDate==null){     
			return false;     
		}     
		try {     
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);     
			dateFormat.setLenient(false);     
			dateFormat.parse(sourceDate);     
			return true;     
		} 
		catch (Exception e) {   
			//e.printStackTrace();
		}     
		return false;     
	}
	

	/**
	 * 把java.util.Date转换为java.sql.Date。
	 * 
	 * @param date
	 * @return
	 */
	public static java.sql.Date convertToSqlDate(Date date) {
		if (date == null) {
			return null;
		}

		String dateStr = convertDateToStr(date, DEFAULT_SHORT_DATE_FORMAT);
		return java.sql.Date.valueOf(dateStr);
	}

	/**
	 * 计算两个日期之间的相隔的年、月、日、小时、分钟、秒。 注意：只有计算相隔天数、小时、分钟、秒是准确的，相隔年和月都是近似值，
	 * 按一年365天，一月30天计算，忽略闰年和闰月的差别。
	 * 
	 * @param datepart
	 *            两位的格式字符串，yy表示年，MM表示月，dd表示日，HH表示小时、mm表示分钟、ss表示秒
	 * @param startdate
	 *            开始日期
	 * @param enddate
	 *            结束日期
	 * @return double 如果enddate>startdate，返回一个大于0的实数，否则返回一个小于等于0的实数
	 */
	public static double dateDiff(String datepart, Date startdate, Date enddate) {
		if (datepart == null || datepart.equals("")) {
			throw new IllegalArgumentException("DateUtil.dateDiff()方法非法参数值："
					+ datepart);
		}

		double distance = (enddate.getTime() - startdate.getTime())
				/ (60 * 60 * 24 * 1000);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(enddate.getTime() - startdate.getTime());
		if (datepart.equals("yy")) {
			distance = distance / 365;
		} else if (datepart.equals("MM")) {
			distance = distance / 30;
		} else if (datepart.equals("dd")) {
			distance = (enddate.getTime() - startdate.getTime())
					/ (60 * 60 * 24 * 1000);
		} else if (datepart.equals("HH")) {
			distance = (enddate.getTime() - startdate.getTime())
					/ (60 * 60 * 1000);
		} else if (datepart.equals("mm")) {
			distance = (enddate.getTime() - startdate.getTime()) / (60 * 1000);
		} else if (datepart.equals("ss")) {
			distance = (enddate.getTime() - startdate.getTime()) / (1000);
		} else {
			throw new IllegalArgumentException("DateUtil.dateDiff()方法非法参数值："
					+ datepart);
		}
		return distance;
	}

	/**
	 * 把日期对象加减年、月、日后得到新的日期对象
	 * 
	 * @param depart
	 *            年、月、日
	 * @param number
	 *            加减因子
	 * @param date
	 *            需要加减年、月、日的日期对象
	 * @return Date 新的日期对象
	 */
	public static Date addDate(String datepart, int number, Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (datepart.equals("yy")) {
			cal.add(Calendar.YEAR, number);
		} else if (datepart.equals("MM")) {
			cal.add(Calendar.MONTH, number);
		} else if (datepart.equals("dd")) {
			cal.add(Calendar.DATE, number);
		} else {
			throw new IllegalArgumentException("DateUtil.addDate()方法非法参数值："
					+ datepart);
		}

		return cal.getTime();
	}

	/**
	 * 计算"开始日期+工作日"后的新日期
	 * 
	 * @param date
	 *            开始日期
	 * @param offset
	 *            工作日
	 * @return
	 */
	public static Date addWorkDay(Date date, int offset) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int i = 0;
		while (i != offset) {
			calendar.add(Calendar.DATE, 1);
			if (calendar.get(calendar.DAY_OF_WEEK) == 7
					|| calendar.get(calendar.DAY_OF_WEEK) == 1) {
				continue;
			}
			i++;
		}
		return calendar.getTime();
	}

	/**
	 * 根据个两个日期得到他们相差几个工作日 没完成
	 * 
	 * @param date
	 * @return
	 * @throws DAOException
	 */
	public static double getWorkingDays(java.sql.Date fromDate,
			String fromWeek, java.sql.Date toDate, String toWeek) {
		// 求得两个日期的相差的天数
		double diffDays = DateUtil.dateDiff("dd", fromDate, toDate) + 1;

		// 相差的工作日天数
		double workDays = 0;

		// 余数
		double mod = diffDays % 7;

		// 除数
		double chushu = diffDays / 7;

		// 取整数
		String temp = String.valueOf(chushu);
		temp = temp.substring(0, temp.indexOf("."));

		// 整数
		double zhengshu = 0;
		// 如果余数为0和1，则整数不变
		if (mod == 0 || mod == 1) {
			zhengshu = Double.parseDouble(temp);
		} else {
			// 如果余数为2-6，则整数+1
			zhengshu = Double.parseDouble(temp) + 1;
		}

		workDays = diffDays - 2 * zhengshu;

		// 如果是星期六，则要加减一
		if (fromWeek != null && fromWeek.trim().equals("7")) {
			workDays = workDays - 1;
		} else if (toWeek != null && toWeek.trim().equals("7")) {
			workDays = workDays + 1;
		}

		return workDays;
	}

	public static void CreateDateSql(String startDate, String endDate) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
		try {

			Date sda = df.parse(startDate);
			Date eda = df.parse(endDate);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sda);

			while (calendar.getTime().compareTo(eda) <= 0) {
				if (calendar.get(Calendar.DAY_OF_WEEK) == 1
						|| calendar.get(Calendar.DAY_OF_WEEK) == 7) {
					System.out.println("insert into BASE_WORKDAY values('"
							+ df.format(calendar.getTime()) + "',0)");
				} else {
					System.out.println("insert into BASE_WORKDAY values('"
							+ df.format(calendar.getTime()) + "',1)");
				}
				calendar.add(Calendar.DATE, 1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 计算两个日期相隔的年份
	 * @param startdate
	 * @param enddate
	 * @return
	 */
	public static int dateDiffYear(Date startdate, Date enddate) {
		int starVal=new Integer(new SimpleDateFormat("yyyy").format(startdate)).intValue();
		int endVal=new Integer(new SimpleDateFormat("yyyy").format(enddate)).intValue();
		return starVal-endVal;
	}

	/**
	 * 计算两个日期相隔几个工作日
	 * 
	 * @param startdate
	 * @param enddate
	 * @return
	 */
	public static double dateDiffWorkDay(Date startdate, Date enddate) {

		double distance = 0;

		Calendar startCal = Calendar.getInstance();
		Calendar endCal = Calendar.getInstance();
		startCal.setTime(startdate);
		endCal.setTime(enddate);

		boolean ifContinue = true;

		if (startCal.equals(endCal))
			return 0;

		while (ifContinue) {
			startCal.add(Calendar.DATE, 1);
			// 如果不等于结束日期，则相隔天数+1
			if (!startCal.equals(endCal)) {
				// 如果是周末，则不计算进去
				if (startCal.get(startCal.DAY_OF_WEEK) == 7
						|| startCal.get(startCal.DAY_OF_WEEK) == 1) {
					continue;
				}
				distance++;
			} else {
				if (startCal.get(startCal.DAY_OF_WEEK) == 7
						|| startCal.get(startCal.DAY_OF_WEEK) == 1) {
					ifContinue = false;
					continue;
				}
				distance++;
				ifContinue = false;
			}
		}

		return distance;
	}

	/**
	 * 计算工作日的间隔，此处的剩余天数为负日时，没有排除周末及节假日；getDaysDiff2（）方法有排除
	 * 
	 * @param date
	 * @return
	 * @throws DAOException
	 */
	public static double getDaysDiff(String date) throws DAOException {
		// 查询语句。
		StringBuffer querySql = new StringBuffer(100);
		querySql.append(" SELECT getWorkday(" + DbUtil.fieldValue(date)
				+ ") FROM dual");

		// 数据库记录集合。
		RowSet rs = null;

		double diffDays = 0;
		try {
			// 得到会计人员信息集合。
			rs = DbAccess.executeRowSetQuery(querySql.toString());

			// 如果记录为空，返回NULL。
			if (rs.next()) {
				diffDays = rs.getDouble(1);
			}

		} catch (SQLException ex) {
			throw new DAOException(ex);
		} finally {
			// 关闭数据库记录集合。
			DbUtil.closeRs(rs);
		}

		return diffDays;
	}

	/**
	 * 计算工作日的间隔，剩余天数/距取证天数为负或正时，都有排除周末及节假日；
	 * 
	 * @param date
	 * @return
	 * @throws DAOException
	 */
	public static double getDaysDiff2(String date) throws DAOException {
		// 查询语句。
		StringBuffer querySql = new StringBuffer(100);
		querySql.append(" SELECT getLastWorkDay(" + DbUtil.fieldValue(date)
				+ ") FROM dual");

		// 数据库记录集合。
		RowSet rs = null;

		double diffDays = 0;
		try {
			// 得到会计人员信息集合。
			rs = DbAccess.executeRowSetQuery(querySql.toString());

			// 如果记录为空，返回NULL。
			if (rs.next()) {
				diffDays = rs.getDouble(1);
			}

		} catch (SQLException ex) {
			throw new DAOException(ex);
		} finally {
			// 关闭数据库记录集合。
			DbUtil.closeRs(rs);
		}

		return diffDays;
	}
	/**
	 * 计算两个日期之间的工作日间隔，排除周末及节假日；
	 * 余伟能,  2008-01-23
	 * @param date
	 * @return
	 * @throws DAOException
	 */
	public static double getDaysDiff3(String startDate, String endDate) throws DAOException {
		// 查询语句。
		StringBuffer querySql = new StringBuffer(100);
		querySql.append(" SELECT getBetweenWorkDay(" + DbUtil.fieldValue(startDate)
				+ "," +  DbUtil.fieldValue(endDate) + ") FROM dual");

		// 数据库记录集合。
		RowSet rs = null;

		double diffDays = 0;
		try {
			// 得到会计人员信息集合。
			rs = DbAccess.executeRowSetQuery(querySql.toString());

			// 如果记录为空，返回NULL。
			if (rs.next()) {
				diffDays = rs.getDouble(1);
			}

		} catch (SQLException ex) {
			throw new DAOException(ex);
		} finally {
			// 关闭数据库记录集合。
			DbUtil.closeRs(rs);
		}

		return diffDays;
	}
	
	/**
	 * 增加工作日
	 * 
	 * @param date
	 * @return
	 * @throws DAOException
	 */
	public static String addWorkDays(String date, int days) throws DAOException {
		// 查询语句。
		StringBuffer querySql = new StringBuffer(100);
		querySql.append(" SELECT ADDWORKDAY(" + DbUtil.fieldValue(date) + ","
				+ DbUtil.fieldValue(days) + ") FROM dual");

		// 数据库记录集合。
		RowSet rs = null;

		String returnDays = null;

		try {
			// 得到会计人员信息集合。
			rs = DbAccess.executeRowSetQuery(querySql.toString());

			// 如果记录为空，返回NULL。
			if (rs.next()) {
				returnDays = rs.getString(1);
			}

		} catch (SQLException ex) {
			throw new DAOException(ex);
		} finally {
			// 关闭数据库记录集合。
			DbUtil.closeRs(rs);
		}

		return returnDays;
	}
	
	/**
	 * 返回参数date所在周的周五对应的日期。added龙剑云20080131
	 * @param date
	 * @return
	 */
	public static String getFriday(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(c.getTime());
	}
	
	/**
	 * 技能考试取得days日后的工作日期。
	 * added by 龙剑云20080219
	 * @param date
	 * @param days
	 * @return
	 * @throws DAOException
	 */
	public static String getSkillWorkDay(String date, int days) throws DAOException {
		// 查询语句。存储过程AddSkillExamDate过滤了周五、六、日及节假日。
		StringBuffer querySql = new StringBuffer(100);
		querySql.append(" SELECT AddSkillExamDate(" + DbUtil.fieldValue(date) + ","
				+ DbUtil.fieldValue(days) + ") FROM dual");

		// 数据库记录集合。
		RowSet rs = null;

		String returnDays = null;

		try {
			// 得到会计人员信息集合。
			rs = DbAccess.executeRowSetQuery(querySql.toString());

			// 如果记录为空，返回NULL。
			if (rs.next()) {
				returnDays = rs.getString(1);
			}

		} catch (SQLException ex) {
			throw new DAOException(ex);
		} finally {
			// 关闭数据库记录集合。
			DbUtil.closeRs(rs);
		}

		return returnDays;
	}
	
	 /**
	  *method 将字符串类型的日期转换为一个timestamp（时间戳记java.sql.Timestamp）
	  *@param dateString 需要转换为timestamp的字符串
	  *@return dataTime timestamp
	  *@author zzp
	  */
	 public final static java.sql.Date string2Date(String dateString)
	  throws java.lang.Exception {
	  if( StringUtil.isNullOrBlank(dateString)){
			 return null;
	  }
	  DateFormat dateFormat;
	  dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	  dateFormat.setLenient(false);
	  java.util.Date timeDate = dateFormat.parse(dateString);//util类型
	  java.sql.Date dateTime = new java.sql.Date(timeDate.getTime());//sql类型
	  return dateTime;
	 }
	 
	 
	 /** 
	  * @author dameng
	  *  取得指定月份的第一天 
	  *  @param strdate String * @return String 
	  */ 
	 public final static String getMonthBegin(String strdate) { 
		 java.util.Date date = DateUtil.convertStrToDate(strdate,DateUtil.DEFAULT_SHORT_DATE_FORMAT); 
		 return formatDateByFormat(date,"yyyy-MM") + "-01"; 
	 } 
	 
	 /** 
	  * @author dameng
	  *  取得指定月份的最后一天天 
	  *  @param strdate String * @return String 
	  */ 	 
	 public final static String getMouthEnd(String date) {
		 
		 Calendar a=Calendar.getInstance();
		 a.setTime(DateUtil.convertStrToDateNoError(date, DateUtil.DEFAULT_SHORT_DATE_FORMAT));
		 a.set(Calendar.DATE, 1); //把日期设置为当月第一天

		 a.roll(Calendar.DATE, -1); //日期回滚一天，也就是最后一天
		 
		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		 //int MaxDate=a.get(Calendar.DATE);
		 return df.format(a.getTime());
	 }

	 
	 /** 
	  * 常用的格式化日期 
	  * @param date Date 
	  * @return String 
	  */ 
	 public final static String formatDate(java.util.Date date) { 
		 return formatDateByFormat(date,"yyyy-MM-dd"); 
		 } 
	 
	 /** 
	  * 常用的格式化日期 
	  * @param date Date 
	  * @return String 
	  */ 
	 public final static String formatDate(java.util.Date date,String formatStr) { 
		 return formatDateByFormat(date,formatStr); 
		 } 
	 /** 
	  * 以指定的格式来格式化日期@param date Date 
	  *@param format String * @return String 
	  */ 
	 public final static String formatDateByFormat(java.util.Date date,String format) { 
		 String result = ""; 
		 if(date != null) { 
			 try { SimpleDateFormat sdf = new SimpleDateFormat(format); 
			 result = sdf.format(date); } 
			 catch(Exception ex) { 
				 ex.printStackTrace(); } } 
		 return result; 
	 }
	 
	 public final static String getStringDateToSqlDate(java.sql.Date date) {
		 if(date == null) {
			 return null;
		 }
		 java.util.Date uDate = new Date(date.getTime());
		 
		 return DateUtil.convertDateToStr(uDate, DEFAULT_SHORT_DATE_FORMAT);
	 }

	/**
	 * 测试
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//定义格式，不显示毫秒
//		Timestamp now = new Timestamp(System.currentTimeMillis());//获取系统当前时间
//
//		Date ss=DateUtil.convertTimeToDate(now);
//
//		System.out.println("ss="+DateUtil.convertToSqlDate(ss));
//		
//		String string="410000000123456";
//		System.out.println(string.substring(string.length()-6));
//		File srcFile=new File("src/MerPrK_808080102699877_20101026152409.key");
//		System.out.println("===="+srcFile.exists());
//		String key=SystemProperties.getDEFAULT_ADMIN_ID_KEY();;
//		System.out.println("@@@@@@@@@@key="+key);
		System.out.println("==="+DateUtil.getNowTime());
		Date dd=DateUtil.getNowDate();
		System.out.println(DateUtil.formatDate(dd,"yyyy"));
		//String operdatePath="onlineTeach.properties";
		//Properties t = new Properties();
//		try {
//		//	t.load(new FileInputStream(operdatePath));
//		//	String start_date=t.getProperty("start.time");
//		//	System.out.println(start_date);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}
	

}