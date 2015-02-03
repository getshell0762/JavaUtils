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
 * ���ڹ����࣬�ṩ�й����ڲ�������ķ�����
 * 
 * @author ��Сƽ
 * @version 1.0
 */

public class DateUtil {

	/**
	 * ʱ���ʽ
	 */
	public final static String TIME_FORMAT = "HH:mm:ss:SS";

	/**
	 * TIMESTAMP��ʱ���ʽ
	 */
	public final static String TIME_FORMAT_TIMES = "yyyy-MM-dd HH:mm:ss";

	/**
	 * ȱʡ�����ڸ�ʽ
	 */
	public final static String DEFAULT_SHORT_DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * ȱʡ�����ڸ�ʽ
	 */
	public final static String DEFAULT_SHORT_DATE_FORMAT_ZH = "yyyy��M��d��";

	/**
	 * ȱʡ�����ڸ�ʽ
	 */
	public final static String DEFAULT_LONG_DATE_FORMAT = DEFAULT_SHORT_DATE_FORMAT
			+ " " + TIME_FORMAT;

	/**
	 * Java��֧�ֵ���С�����ַ�����yyyy-MM-dd����
	 */
	public final static String JAVA_MIN_SHORT_DATE_STR = "1970-01-01";

	/**
	 * Java��֧�ֵ���С�����ַ�����yyyy-MM-dd HH:mm:ss:SS����
	 */
	public final static String JAVA_MIN_LONG_DATE_STR = "1970-01-01 00:00:00:00";

	/**
	 * Java��֧�ֵ���С��Timestamp��
	 */
	public final static Timestamp JAVA_MIN_TIMESTAMP = convertStrToTimestamp(JAVA_MIN_LONG_DATE_STR);

	/**
	 * ���ַ���ת��ΪTimestamp���ͣ����ڶ����ڸ�ʽ���Զ���ʱ����Ϊϵͳ��ǰʱ�䡣
	 * 
	 * @return Timestamp
	 * @see #convertStrToTimestamp(String,boolean)
	 */
	public static Timestamp convertStrToTimestamp(String dateStr) {
		return convertStrToTimestamp(dateStr, false);
	}

	/**
	 * ���ַ���ת��ΪTimestamp���ͣ����ڶ����ڸ�ʽ���Զ�ʱ����Ϊ0��
	 * 
	 * @return Timestamp
	 * @see #convertStrToTimestamp(String,boolean)
	 */
	public static Timestamp convertStrToTimestampZero(String dateStr) {
		return convertStrToTimestamp(dateStr, true);
	}

	/**
	 * ���ַ���ת��ΪTimestamp���͡�
	 * 
	 * @param dateStr -
	 *            �����ַ�����ֻ֧��"yyyy-MM-dd"��"yyyy-MM-dd HH:mm:ss:SS"���ָ�ʽ��
	 *            ���Ϊ"yyyy-MM-dd"��ϵͳ���Զ�ȡ�õ�ǰʱ�䲹�ϡ�
	 * @param addZeroTime -
	 *            �������ַ���Ϊ"yyyy-MM-dd"�����ĸ�ʽʱ��addZeroTimeΪtrue��ʾ
	 *            ��0������HH:mm:ss:SS�������õ�ǰTime�����á�
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
	 * ��yyyy-MM-dd HH:mm:ss:SS���͵��ַ���ת����timestamp
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
	 * ��timestampת��sqlDate
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
	 * �õ�ϵͳ��ǰʱ���Timestamp����
	 * 
	 * @return ϵͳ��ǰʱ���Timestamp����
	 */
	public static Timestamp getCurrTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * �õ�ϵͳ��ǰʱ���Date����
	 * 
	 * @return ϵͳ��ǰʱ���sqlDate����
	 */
	public  static java.sql.Date getNowDate(){
		java.sql.Date  oNowDate=new java.sql.Date(new java.util.Date().getTime());//�õ�ϵͳ��ǰʱ��
		return oNowDate;
	}
	
	public static java.util.Date getNowTime(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�����ʽ������ʾ����
		Timestamp now = new Timestamp(System.currentTimeMillis());//��ȡϵͳ��ǰʱ��
		return now;
	}
	
	/**
	 * <p>
	 * ȡ�õ�ǰ���ڣ�������ת���ɸ�ʽΪ"dateFormat"���ַ��� ���ӣ����統ǰ������ 2003-09-24 9:19:10����
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
	 *            String ���ڸ�ʽ�ַ���
	 * @return String
	 */
	public static String getCurrDateStr(String dateFormat) {
		return convertDateToStr(new Date(), dateFormat);
	}

	/**
	 * ����������ת����ָ����ʽ�������ַ���
	 * 
	 * @param date
	 *            ��ת��������
	 * @param dateFormat
	 *            ���ڸ�ʽ�ַ���
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
	 * ��ָ����ʽ���ַ���ת������������
	 * 
	 * @param date
	 *            ��ת���������ַ���
	 * @param dateFormat
	 *            ���ڸ�ʽ�ַ���
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
	 * ��ָ����ʽ���ַ���ת������������
	 * 
	 * @param date
	 *            ��ת���������ַ���
	 * @param dateFormat
	 *            ���ڸ�ʽ�ַ���
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
	 * ��ָ����ʽ���ַ���ת������������(Sql)
	 * @param date
	 *            ��ת���������ַ���
	 * @param dateFormat
	 *            ���ڸ�ʽ�ַ���
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
	* ���������Ƿ�Ϊ��ȷ�����ڸ�ʽ(��������κ����)    
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
	* ���������Ƿ�Ϊ��ȷ�����ڸ�ʽ(��������κ����),����׽�쳣    
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
	 * ��java.util.Dateת��Ϊjava.sql.Date��
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
	 * ������������֮���������ꡢ�¡��ա�Сʱ�����ӡ��롣 ע�⣺ֻ�м������������Сʱ�����ӡ�����׼ȷ�ģ��������¶��ǽ���ֵ��
	 * ��һ��365�죬һ��30����㣬������������µĲ��
	 * 
	 * @param datepart
	 *            ��λ�ĸ�ʽ�ַ�����yy��ʾ�꣬MM��ʾ�£�dd��ʾ�գ�HH��ʾСʱ��mm��ʾ���ӡ�ss��ʾ��
	 * @param startdate
	 *            ��ʼ����
	 * @param enddate
	 *            ��������
	 * @return double ���enddate>startdate������һ������0��ʵ�������򷵻�һ��С�ڵ���0��ʵ��
	 */
	public static double dateDiff(String datepart, Date startdate, Date enddate) {
		if (datepart == null || datepart.equals("")) {
			throw new IllegalArgumentException("DateUtil.dateDiff()�����Ƿ�����ֵ��"
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
			throw new IllegalArgumentException("DateUtil.dateDiff()�����Ƿ�����ֵ��"
					+ datepart);
		}
		return distance;
	}

	/**
	 * �����ڶ���Ӽ��ꡢ�¡��պ�õ��µ����ڶ���
	 * 
	 * @param depart
	 *            �ꡢ�¡���
	 * @param number
	 *            �Ӽ�����
	 * @param date
	 *            ��Ҫ�Ӽ��ꡢ�¡��յ����ڶ���
	 * @return Date �µ����ڶ���
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
			throw new IllegalArgumentException("DateUtil.addDate()�����Ƿ�����ֵ��"
					+ datepart);
		}

		return cal.getTime();
	}

	/**
	 * ����"��ʼ����+������"���������
	 * 
	 * @param date
	 *            ��ʼ����
	 * @param offset
	 *            ������
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
	 * ���ݸ��������ڵõ��������������� û���
	 * 
	 * @param date
	 * @return
	 * @throws DAOException
	 */
	public static double getWorkingDays(java.sql.Date fromDate,
			String fromWeek, java.sql.Date toDate, String toWeek) {
		// ����������ڵ���������
		double diffDays = DateUtil.dateDiff("dd", fromDate, toDate) + 1;

		// ���Ĺ���������
		double workDays = 0;

		// ����
		double mod = diffDays % 7;

		// ����
		double chushu = diffDays / 7;

		// ȡ����
		String temp = String.valueOf(chushu);
		temp = temp.substring(0, temp.indexOf("."));

		// ����
		double zhengshu = 0;
		// �������Ϊ0��1������������
		if (mod == 0 || mod == 1) {
			zhengshu = Double.parseDouble(temp);
		} else {
			// �������Ϊ2-6��������+1
			zhengshu = Double.parseDouble(temp) + 1;
		}

		workDays = diffDays - 2 * zhengshu;

		// ���������������Ҫ�Ӽ�һ
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
	 * ��������������������
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
	 * �������������������������
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
			// ��������ڽ������ڣ����������+1
			if (!startCal.equals(endCal)) {
				// �������ĩ���򲻼����ȥ
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
	 * ���㹤���յļ�����˴���ʣ������Ϊ����ʱ��û���ų���ĩ���ڼ��գ�getDaysDiff2�����������ų�
	 * 
	 * @param date
	 * @return
	 * @throws DAOException
	 */
	public static double getDaysDiff(String date) throws DAOException {
		// ��ѯ��䡣
		StringBuffer querySql = new StringBuffer(100);
		querySql.append(" SELECT getWorkday(" + DbUtil.fieldValue(date)
				+ ") FROM dual");

		// ���ݿ��¼���ϡ�
		RowSet rs = null;

		double diffDays = 0;
		try {
			// �õ������Ա��Ϣ���ϡ�
			rs = DbAccess.executeRowSetQuery(querySql.toString());

			// �����¼Ϊ�գ�����NULL��
			if (rs.next()) {
				diffDays = rs.getDouble(1);
			}

		} catch (SQLException ex) {
			throw new DAOException(ex);
		} finally {
			// �ر����ݿ��¼���ϡ�
			DbUtil.closeRs(rs);
		}

		return diffDays;
	}

	/**
	 * ���㹤���յļ����ʣ������/��ȡ֤����Ϊ������ʱ�������ų���ĩ���ڼ��գ�
	 * 
	 * @param date
	 * @return
	 * @throws DAOException
	 */
	public static double getDaysDiff2(String date) throws DAOException {
		// ��ѯ��䡣
		StringBuffer querySql = new StringBuffer(100);
		querySql.append(" SELECT getLastWorkDay(" + DbUtil.fieldValue(date)
				+ ") FROM dual");

		// ���ݿ��¼���ϡ�
		RowSet rs = null;

		double diffDays = 0;
		try {
			// �õ������Ա��Ϣ���ϡ�
			rs = DbAccess.executeRowSetQuery(querySql.toString());

			// �����¼Ϊ�գ�����NULL��
			if (rs.next()) {
				diffDays = rs.getDouble(1);
			}

		} catch (SQLException ex) {
			throw new DAOException(ex);
		} finally {
			// �ر����ݿ��¼���ϡ�
			DbUtil.closeRs(rs);
		}

		return diffDays;
	}
	/**
	 * ������������֮��Ĺ����ռ�����ų���ĩ���ڼ��գ�
	 * ��ΰ��,  2008-01-23
	 * @param date
	 * @return
	 * @throws DAOException
	 */
	public static double getDaysDiff3(String startDate, String endDate) throws DAOException {
		// ��ѯ��䡣
		StringBuffer querySql = new StringBuffer(100);
		querySql.append(" SELECT getBetweenWorkDay(" + DbUtil.fieldValue(startDate)
				+ "," +  DbUtil.fieldValue(endDate) + ") FROM dual");

		// ���ݿ��¼���ϡ�
		RowSet rs = null;

		double diffDays = 0;
		try {
			// �õ������Ա��Ϣ���ϡ�
			rs = DbAccess.executeRowSetQuery(querySql.toString());

			// �����¼Ϊ�գ�����NULL��
			if (rs.next()) {
				diffDays = rs.getDouble(1);
			}

		} catch (SQLException ex) {
			throw new DAOException(ex);
		} finally {
			// �ر����ݿ��¼���ϡ�
			DbUtil.closeRs(rs);
		}

		return diffDays;
	}
	
	/**
	 * ���ӹ�����
	 * 
	 * @param date
	 * @return
	 * @throws DAOException
	 */
	public static String addWorkDays(String date, int days) throws DAOException {
		// ��ѯ��䡣
		StringBuffer querySql = new StringBuffer(100);
		querySql.append(" SELECT ADDWORKDAY(" + DbUtil.fieldValue(date) + ","
				+ DbUtil.fieldValue(days) + ") FROM dual");

		// ���ݿ��¼���ϡ�
		RowSet rs = null;

		String returnDays = null;

		try {
			// �õ������Ա��Ϣ���ϡ�
			rs = DbAccess.executeRowSetQuery(querySql.toString());

			// �����¼Ϊ�գ�����NULL��
			if (rs.next()) {
				returnDays = rs.getString(1);
			}

		} catch (SQLException ex) {
			throw new DAOException(ex);
		} finally {
			// �ر����ݿ��¼���ϡ�
			DbUtil.closeRs(rs);
		}

		return returnDays;
	}
	
	/**
	 * ���ز���date�����ܵ������Ӧ�����ڡ�added������20080131
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
	 * ���ܿ���ȡ��days�պ�Ĺ������ڡ�
	 * added by ������20080219
	 * @param date
	 * @param days
	 * @return
	 * @throws DAOException
	 */
	public static String getSkillWorkDay(String date, int days) throws DAOException {
		// ��ѯ��䡣�洢����AddSkillExamDate���������塢�����ռ��ڼ��ա�
		StringBuffer querySql = new StringBuffer(100);
		querySql.append(" SELECT AddSkillExamDate(" + DbUtil.fieldValue(date) + ","
				+ DbUtil.fieldValue(days) + ") FROM dual");

		// ���ݿ��¼���ϡ�
		RowSet rs = null;

		String returnDays = null;

		try {
			// �õ������Ա��Ϣ���ϡ�
			rs = DbAccess.executeRowSetQuery(querySql.toString());

			// �����¼Ϊ�գ�����NULL��
			if (rs.next()) {
				returnDays = rs.getString(1);
			}

		} catch (SQLException ex) {
			throw new DAOException(ex);
		} finally {
			// �ر����ݿ��¼���ϡ�
			DbUtil.closeRs(rs);
		}

		return returnDays;
	}
	
	 /**
	  *method ���ַ������͵�����ת��Ϊһ��timestamp��ʱ�����java.sql.Timestamp��
	  *@param dateString ��Ҫת��Ϊtimestamp���ַ���
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
	  java.util.Date timeDate = dateFormat.parse(dateString);//util����
	  java.sql.Date dateTime = new java.sql.Date(timeDate.getTime());//sql����
	  return dateTime;
	 }
	 
	 
	 /** 
	  * @author dameng
	  *  ȡ��ָ���·ݵĵ�һ�� 
	  *  @param strdate String * @return String 
	  */ 
	 public final static String getMonthBegin(String strdate) { 
		 java.util.Date date = DateUtil.convertStrToDate(strdate,DateUtil.DEFAULT_SHORT_DATE_FORMAT); 
		 return formatDateByFormat(date,"yyyy-MM") + "-01"; 
	 } 
	 
	 /** 
	  * @author dameng
	  *  ȡ��ָ���·ݵ����һ���� 
	  *  @param strdate String * @return String 
	  */ 	 
	 public final static String getMouthEnd(String date) {
		 
		 Calendar a=Calendar.getInstance();
		 a.setTime(DateUtil.convertStrToDateNoError(date, DateUtil.DEFAULT_SHORT_DATE_FORMAT));
		 a.set(Calendar.DATE, 1); //����������Ϊ���µ�һ��

		 a.roll(Calendar.DATE, -1); //���ڻع�һ�죬Ҳ�������һ��
		 
		 DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		 //int MaxDate=a.get(Calendar.DATE);
		 return df.format(a.getTime());
	 }

	 
	 /** 
	  * ���õĸ�ʽ������ 
	  * @param date Date 
	  * @return String 
	  */ 
	 public final static String formatDate(java.util.Date date) { 
		 return formatDateByFormat(date,"yyyy-MM-dd"); 
		 } 
	 
	 /** 
	  * ���õĸ�ʽ������ 
	  * @param date Date 
	  * @return String 
	  */ 
	 public final static String formatDate(java.util.Date date,String formatStr) { 
		 return formatDateByFormat(date,formatStr); 
		 } 
	 /** 
	  * ��ָ���ĸ�ʽ����ʽ������@param date Date 
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
	 * ����
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�����ʽ������ʾ����
//		Timestamp now = new Timestamp(System.currentTimeMillis());//��ȡϵͳ��ǰʱ��
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