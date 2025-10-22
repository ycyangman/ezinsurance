/**
 * 
 */
package ezinsurance.support.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;


public abstract class DateUtils {
	
	public static final int DASH_DATE_TYPE = 0; /* yyyy-MM-dd   */
	public static final int SLASH_DATE_TYPE= 1; /* yyyy/MM/dd   */
	public static final int EMPTY_DATE_TYPE= 2; /* yyyyMMdd     */
	public static final int KOR_DATE_TYPE  = 3; /* yyyy년 MM월 dd일*/
	public static final int DOT_DATE_TYPE  = 4; /* yyyy.MM.dd   */
	public static final int BLANK_DATE_TYPE= 5; /* yyyy MM dd   */

	/** 시간문자열 유형을 나타내는 상수 : HH:mm:ss*/
	public static final int FULL_TIME_TYPE= 0;
	/** 시간문자열 유형을 나타내는 상수 : HH:mm*/
	public static final int MIN_TIME_TYPE= 1;
	/** 시간문자열 유형을 나타내는 상수 : HH*/
	public static final int HOUR_TIME_TYPE= 2;
	/** 시간문자열 유형을 나타내는 상수 : a hh:mm:ss*/
	public static final int AMPM_TIME_TYPE= 3;
	/** 시간문자열 유형을 나타내는 상수 : a hh:mm*/
	public static final int AMPM_MIN_TIME_TYPE= 4;
	/** 시간문자열 유형을 나타내는 상수 : a hh*/
	public static final int AMPM_HOUR_TIME_TYPE= 5;
	/** 시간문자열 유형을 나타내는 상수 : HHmmss*/
	public static final int NOCOLON_TIME_TYPE= 6;

	/** 날자 변환 포맷 종류
     *  "yyyy-MM-dd", "yyyy/MM/dd", "yyyyMMdd", "yyyy년 MM월 dd일", "yyyy.MM.dd", "yyyy MM dd"
     */
    private static String formatListDate[]= { "yyyy-MM-dd", "yyyy/MM/dd", "yyyyMMdd", "yyyy년 MM월 dd일", "yyyy.MM.dd", "yyyy MM dd"};
    /** 시간 변환 포맷 종류
     *  "HH:mm:ss", "HH:mm", "HH", "a hh:mm:ss", "a hh:mm", "a hh", "HHmmss"
     */
    private static String formatListTime[]= { "HH:mm:ss", "HH:mm", "HH", "a hh:mm:ss", "a hh:mm", "a hh", "HHmmss"};

	public static final String DEFAULT_DATE_DELIMITER = "-";
	public static final String DEFAULT_TIME_DELIMITER = "-";

	public static final String DEFAULT_DATE_FORMAT = "yyyy"
			+ DEFAULT_DATE_DELIMITER + "MM" + DEFAULT_DATE_DELIMITER + "dd";

	static final long ONE_DAY = 1000 * 60 * 60 * 24; // 1일
	static final long ONE_HOUR = 1000 * 60 * 60; // 1 시간
	static final long ONE_MIN = 1000 * 60; // 1 분

	    /**
     * <pre>현재날짜를 주어진 포맷으로 리턴한다. 
     * System.out.println(DateUtils.currentDate(0));
     * 결과는 "yyyy-MM-dd" 포맷에 맞춘 결과값이 나온다. </pre>
     * @param format<br>포맷
     * DateUtils.DASH_TYPE  "yyyy-MM-dd", <br>DateUtils.SLASH_TYPE  "yyyy/MM/dd",<br> DateUtils.EMPTY_TYPE "yyyyMMdd",<br> DateUtils.KOR_TYPE "yyyy년 MM월 dd일", <br>DateUtils.DOT_TYPE "yyyy.MM.dd", <br>DateUtils.BLANK_TYPE "yyyy MM dd"
     * @return String로 변환된 값
     */
    public static String getCurrentDate( int format)
    {
        if( format>= formatListDate.length|| format< 0) return null;
        SimpleDateFormat simpledateformat= new SimpleDateFormat( formatListDate[format]);
        Calendar calendar= Calendar.getInstance();
        simpledateformat.setCalendar( calendar);
        String s= simpledateformat.format( simpledateformat.getCalendar().getTime());
        return s;
    }

    /**
     * <pre>주어진 날짜를 지정된 포맷으로 변환하여 리턴한다.
     * System.out.println(DateUtils.getDate("20120301",0);
     * 결과는 yyyy-MM-dd 포맷으로 변환된 2012-03-01 </pre>
     * @param date 기준 날짜, 8자리 String (예 : "20120301")
     * @param format<br>
     * DateUtils.DASH_TYPE  "yyyy-MM-dd", <br>DateUtils.SLASH_TYPE  "yyyy/MM/dd",<br> DateUtils.EMPTY_TYPE "yyyyMMdd",<br> DateUtils.KOR_TYPE "yyyy년 MM월 dd일", <br>DateUtils.DOT_TYPE "yyyy.MM.dd", <br>DateUtils.BLANK_TYPE "yyyy MM dd"
     * @return String로 변환된 날짜
     */
    public static String getDate( String date, int format)
    {
        return getDate( Integer.parseInt( date.substring( 0, 4)), Integer.parseInt( date.substring( 4, 6)), 
        		Integer.parseInt( date.substring( 6)), format);
    }

    /**
     * <pre>주어진 날짜를 지정된 포맷으로 변환하여 리턴한다.
     * System.out.println(DateUtils.getDate(2012, 3, 1, 0));
     * 결과는 yyyy-MM-dd 포맷으로 변환된 2012-03-01 </pre>
     * @param year 연도, int 값 (예 : 2012 )
     * @param month 월, int 값 (예 : 3 )
     * @param day 일, int 값 (예 : 1 )
     * @param format<br>
     * DateUtils.DASH_TYPE  "yyyy-MM-dd", <br>DateUtils.SLASH_TYPE  "yyyy/MM/dd",<br> DateUtils.EMPTY_TYPE "yyyyMMdd",<br> DateUtils.KOR_TYPE "yyyy년 MM월 dd일", <br>DateUtils.DOT_TYPE "yyyy.MM.dd", <br>DateUtils.BLANK_TYPE "yyyy MM dd"
     * @return String로 변환된 날짜
     */
    public static String getDate( int year, int month, int day, int format)
    {
        SimpleDateFormat simpledateformat= new SimpleDateFormat( formatListDate[format]);
        Calendar calendar= Calendar.getInstance();
        calendar.set( year, month- 1, day);
        simpledateformat.setCalendar( calendar);
        String s= simpledateformat.format( simpledateformat.getCalendar().getTime());
        return s;
    }

    /**
     * <pre>현재시간을 주어진 포맷으로 리턴한다. 
     * System.out.println(DateUtils.currentTime(1));
     * 결과는 "HH:mm" 포맷에 맞춘 결과값이 나온다.</pre>
     * @param format 
     * DateUtils.FULL_TYPE "HH:mm:ss", <br>DateUtils.MIN_TYPE "HH:mm",<br>DateUtils.HOUR_TYPE "HH",<br>    DateUtils.AMPM_TYPE "a hh:mm:ss",<br>    DateUtils.AM_MIN_TYPE "a hh:mm",<br>DateUtils.AMHOUR_TYPE "a hh",<br> DateUtils.NOCOLON_TYPE "HHmmss"
     *     
     * @return String로 변환된 값
     */
    public static String getCurrentTime( int format)
    {
        if( format>= formatListTime.length|| format< 0) return null;
        SimpleDateFormat simpledateformat= new SimpleDateFormat( formatListTime[format]);
        Calendar calendar= Calendar.getInstance();
        simpledateformat.setCalendar( calendar);
        String s= simpledateformat.format( simpledateformat.getCalendar().getTime());
        return s;
    }

    /**
     * 전달된 문자열이 유효할 날자인지 판단한다.
     * @param inString 검사할 문자열
     * @param format<br>포맷
     * DateUtils.DASH_TYPE  "yyyy-MM-dd", <br>DateUtils.SLASH_TYPE  "yyyy/MM/dd",<br> DateUtils.EMPTY_TYPE "yyyyMMdd",<br> DateUtils.KOR_TYPE "yyyy년 MM월 dd일", <br>DateUtils.DOT_TYPE "yyyy.MM.dd", <br>DateUtils.BLANK_TYPE "yyyy MM dd"
     * @return 유효할 날자인지 여부
     */
    public static boolean isValidDate( String inString, int format)
    {
    	SimpleDateFormat simpledateformat= new SimpleDateFormat( formatListDate[format]);
    	if( inString.length()!= simpledateformat.toPattern().length())
    		return false;
    	simpledateformat.setLenient( false);
    	try
		{
			simpledateformat.parse( inString);
		}
		catch( ParseException e)
		{
			return false;
		}
    	return true;
    }

	/**
     * System-time(millisecond단위)를 형식화된 문자열로 변환한다.
     * @param milliseconds 입력할 milliseconds
     * @param pattern 사용할 포맷패턴
     * @return 패턴에 맞추어 변환된 문자열
     */
    public static String getString( long milliseconds, String pattern)
    {
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis( milliseconds);
        return new SimpleDateFormat( pattern).format( calendar.getTime()).toString();
    }

	public static Timestamp getTimestamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static Timestamp getTimestamp(String datetime) throws Exception {
		if(!StringUtils.hasText(datetime)) {
			return null;
		}
		String pattern;
		if (datetime.length() == 8) {
			pattern = "yyyyMMdd";
		} else if (datetime.length() == 14) {
			pattern = "yyyyMMddHHmmss";
		} else {
			throw new Exception("입력된 날짜가 기본 포맷에 어긋납니다.");
		}
		return getTimestamp(datetime, pattern);
	}

	public static Timestamp getTimestamp(String datetime, String pattern)
			throws Exception {
		if(!StringUtils.hasText(datetime)) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat();
		format.applyPattern(pattern);
		Date d = format.parse(datetime);
		return new Timestamp(d.getTime());
	}

	public static String toString(Timestamp t, String pattern) {
		if(t == null) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date(t.getTime()));
	}

	public static String toString(Timestamp t) {
		return toString(t, DEFAULT_DATE_FORMAT);
	}

	/**
	 * 두 날짜 사이의 일수를 계산한다. ex) getDateAdd("20110811", "20110817") = 6
	 * 
	 * @param from
	 *            날짜1
	 * @param to
	 *            날짜2
	 * @return 일수
	 * @throws Exception
	 */
	public static int getDateDiff(String from, String to) throws Exception {
		long f = getTimestamp(from).getTime();
		long t = getTimestamp(to).getTime();

		return (int) ((t - f) / ONE_DAY);
	}

	/**
	 * 날짜에 일수를 더한다
	 * 
	 * @param date
	 *            날짜
	 * @param days
	 *            더할 일수
	 * @return
	 * @throws Exception
	 */
	public static String getDateAdd(String date, int days) throws Exception {
		return getDateAdd(date, "yyyyMMdd", days);
	}

	/**
	 * 날짜에 일수를 더한다
	 * 
	 * @param date
	 *            날짜
	 * @param pattern
	 *            날짜 패턴
	 * @param days
	 *            더할 일수
	 * @return 계산된 날짜
	 * @throws Exception
	 */
	public static String getDateAdd(String date, String pattern, int days)
			throws Exception {
		long d = getTimestamp(date, pattern).getTime();
		d += days * ONE_DAY;
		SimpleDateFormat format = new SimpleDateFormat(pattern);

		return format.format(new Date(d));
	}
	
	public static String getCurDtm() {
        SimpleDateFormat simpledateformat= new SimpleDateFormat( "yyyyMMddHHmmss");
        Calendar calendar= Calendar.getInstance();
        simpledateformat.setCalendar( calendar);
        String s= simpledateformat.format( simpledateformat.getCalendar().getTime());
        return s;
    }

	public static void main(String args[]) throws Exception {
		System.out.println(getDateDiff("20110811", "20110817"));
		System.out.println(getDateAdd("20110811", 3));
		System.out.println(getDateAdd("2011-08-11", "yyyy-MM-dd", 3));
	}
}
