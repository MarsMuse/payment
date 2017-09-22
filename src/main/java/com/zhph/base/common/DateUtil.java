package com.zhph.base.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;


/**
 * 日期时间工具类。
 * @Company:zhph
 * @author long.deng
 * @date 2016-6-24 下午4:55:42
 */
public class DateUtil extends DateUtils {
    
    private static String dateFormat = "yyyy-MM-dd";
    
    public static String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    
    public static String simpletimeformat ="yyyy-MM-dd HH:mm:ss";
    public static void setDateFormatString(String format) {
        dateFormat = format;
    }
    
    public static void setDateTimeFormatString(String format) {
        dateTimeFormat = format;
    }
    
    /**
     * 将一个毫秒的时间转换为中文时间字符。如传入2000会返回2秒。
     * @param time 毫秒
     * @param：@return
     * @return：String
     *  
     * @author long.deng
     * @date 2016-6-24 下午4:56:11
     * @throws
     */
    public static String timeIntervalToString(Long time) {
        if (time == null) {
            return "";
        }
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;
        
        long day = time / dd;
        long hour = (time - day * dd) / hh;
        long minute = (time - day * dd - hour * hh) / mi;
        long second = (time - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = time - day * dd - hour * hh - minute * mi - second * ss;
        
        StringBuilder str = new StringBuilder();
        if (day > 0) {
            str.append(day).append("天,");
        }
        if (hour > 0) {
            str.append(hour).append("小时,");
        }
        if (minute > 0) {
            str.append(minute).append("分钟,");
        }
        if (second > 0) {
            str.append(second).append("秒,");
        }
        if (milliSecond > 0) {
            str.append(milliSecond).append("毫秒,");
        }
        if (str.length() > 0) {
            str = str.deleteCharAt(str.length() - 1);
        }
        return str.toString();
    }
    
    /**
     * 将java date按照平台配置文件配置的日期字符格式化为字符串
     * @param date
     * @param：@return
     * @return：String
     * @author long.deng
     * @date 2016-6-24 下午4:57:17
     * @throws
     */
    public static String dateToString(Date date) {
        return dateToString(date, dateFormat);
    }
    
    /**
     * 将java date按照平台配置文件配置的日期字符格式化为字符串
     * @param date
     * @param：@return
     * @return：String
     * @author long.deng
     * @date 2016-6-24 下午4:57:37
     * @throws
     */
    public static String datetimeToString(Date date) {
        return dateToString(date, dateTimeFormat);
    }
    
    /**
     * 将java date按照传入的日期字符格式化为字符串
     * @param date
     * @param format
     * @param：@return
     * @return：String
     * @author long.deng
     * @date 2016-6-24 下午5:00:56
     * @throws
     */
    public static String dateToString(Date date, String format) {
        String target = null;
        if (date == null || "".equals(date)) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        target = simpleDateFormat.format(date);
        return target;
    }
    
    /**
     * 将日期字符串转换为java.util.Date类型.转换格式为平台配置文件中date_string.format的值
     * @param dateString 日期字符串
     * @param：@return
     * @throws ParseException
     * @return：Date
     * @author long.deng
     * @date 2016-6-24 下午5:01:24
     * @throws
     */
    public static Date stringToDate(String dateString)
        throws ParseException {
        return stringToDate(dateString, dateFormat);
    }
    
    /**
     * 将日期字符串转换为java.util.Date类型.转换格式为平台配置文件中datetime_string.format的值
     * @param dateString 日期字符串
     * @param：@return
     * @throws ParseException
     * @return：Date
     * @author long.deng
     * @date 2016-6-24 下午5:01:45
     * @throws
     */
    public static Date stringToDatetime(String dateString)
        throws ParseException {
        return stringToDate(dateString, dateTimeFormat);
    }
    
    /**
     * 用传入的日期字符串格式格式化字符。
     * @param dataString 日期字符串
     * @param format 转换格式
     * @param：@return
     * @throws ParseException
     * @return：Date
     * @author long.deng
     * @date 2016-6-24 下午5:02:17
     * @throws
     */
    public static Date stringToDate(String dataString, String format)
        throws ParseException {
        Date target = null;
        if(!StringUtils.isEmpty(dataString)){
            target = parseDate(dataString, new String[] {format});
        }
        return target;
    }

    /**
     * 根据出生日期 获取年龄
     * @param date
     * @param：@return
     * @return：String
     *  
     * @author long.deng
     * @date 2016-6-24 下午5:02:46
     * @throws
     */
    public static String getAge(Date date) {
        Date nowDate = new Date();
        Long day = (nowDate.getTime() - date.getTime()) / (24 * 60 * 60 * 1000) + 1;
        return String.valueOf(day / 365);
    }
    
    /**
     * 获取第二天的日期
     * @param：@return
     * @throws ParseException
     * @return：Date
     *  
     * @author long.deng
     * @date 2016-6-24 下午5:03:43
     * @throws
     */
    @SuppressWarnings("static-access")
    public static Date getTomorrowDate()
        throws ParseException {
        Date date = new Date();// 取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
        return stringToDate(dateToString(date));
    }
    
    /**
     * 获取第二天的日期时间
     * @param：@return
     * @throws ParseException
     * @return：Date
     *  
     * @author long.deng
     * @date 2016-6-24 下午5:04:15
     * @throws
     */
    @SuppressWarnings("static-access")
    public static Date getTomorrowDateTime()
        throws ParseException {
        Date date = new Date();// 取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
        return stringToDatetime(datetimeToString(date));
    }

    /**
     * 增加日期中某类型的某数值。如增加日期
     * @param date 日期字符串
     * @param dateType 类型
     * @param amount 数值
     * @param：@return
     * @throws ParseException
     * @return：String
     * @author long.deng
     * @date 2016-6-24 下午5:04:44
     * @throws
     */
    private static String addInteger(String date, int dateType, int amount)
        throws ParseException {
        String dateString = null;
        Date myDate = stringToDate(date);
        myDate = addInteger(myDate, dateType, amount);
        dateString = dateToString(myDate);
        return dateString;
    }

    /**
     * 增加日期中某类型的某数值。如增加日期
     * @param date 日期
     * @param dateType 类型
     * @param amount 数值
     * @param：@return
     * @return：Date
     *  
     * @author long.deng
     * @date 2016-6-24 下午5:05:07
     * @throws
     */
    private static Date addInteger(Date date, int dateType, int amount) {
        Date myDate = null;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(dateType, amount);
            myDate = calendar.getTime();
        }
        return myDate;
    }

    /**
     * 增加日期的年份。失败返回null。
     * @param date 日期
     * @param yearAmount 增加数量。可为负数
     * @param：@return
     * @throws ParseException
     * @return：String
     *  
     * @author long.deng
     * @date 2016-6-24 下午5:05:42
     * @throws
     */
    public static String addYear(String date, int yearAmount)
        throws ParseException {
        return addInteger(date, Calendar.YEAR, yearAmount);
    }

    /**
     * 增加日期的年份。失败返回null。
     * @param date 日期
     * @param yearAmount 增加数量。可为负数
     * @param：@return
     * @return：Date
     *  
     * @author long.deng
     * @date 2016-6-24 下午5:06:19
     * @throws
     */
    public static Date addYear(Date date, int yearAmount) {
        return addInteger(date, Calendar.YEAR, yearAmount);
    }

    /**
     * 增加日期的月份。失败返回null。
     * @param date 日期
     * @param yearAmount 增加数量。可为负数
     * @param：@return
     * @throws ParseException
     * @return：String
     *  
     * @author long.deng
     * @date 2016-6-24 下午5:06:40
     * @throws
     */
    public static String addMonth(String date, int yearAmount)
        throws ParseException {
        return addInteger(date, Calendar.MONTH, yearAmount);
    }

    /**
     * 增加日期的月份。失败返回null。
     * @param date 日期
     * @param yearAmount 增加数量。可为负数
     * @param：@return
     * @return：Date
     * @author long.deng
     * @date 2016-6-24 下午5:07:04
     * @throws
     */
    public static Date addMonth(Date date, int yearAmount) {
        return addInteger(date, Calendar.MONTH, yearAmount);
    }

    /**
     * 增加日期的天数。失败返回null。
     * @param date 日期字符串
     * @param dayAmount 增加数量。可为负数
     * @param：@return
     * @throws ParseException
     * @return：String
     * @author long.deng
     * @date 2016-6-24 下午5:08:17
     * @throws
     */
    public static String addDay(String date, int dayAmount)
        throws ParseException {
        return addInteger(date, Calendar.DATE, dayAmount);
    }

    /**
     * 增加日期的天数。失败返回null。
     * @param date 日期
     * @param dayAmount 增加数量。可为负数
     * @param：@return
     * @return：Date
     * @author long.deng
     * @date 2016-6-24 下午5:08:44
     * @throws
     */
    public static Date addDay(Date date, int dayAmount) {
        return addInteger(date, Calendar.DATE, dayAmount);
    }

    /**
     * 增加日期的小时。失败返回null。
     * @param date 日期字符串
     * @param* dayAmount 增加数量。可为负数
     * @param：@return
     * @throws ParseException
     * @return：String
     * @author long.deng
     * @date 2016-6-24 下午5:09:09
     * @throws
     */
    public static String addHour(String date, int hourAmount)
        throws ParseException {
        return addInteger(date, Calendar.HOUR_OF_DAY, hourAmount);
    }

    /**
     * 增加日期的小时。失败返回null。
     * @param date 日期
     * @param* dayAmount 增加数量。可为负数
     * @param：@return
     * @return：Date
     * @author long.deng
     * @date 2016-6-24 下午5:09:30
     * @throws
     */
    public static Date addHour(Date date, int hourAmount) {
        return addInteger(date, Calendar.HOUR_OF_DAY, hourAmount);
    }
    
    /**
     * 获取两个日期相差的天数
     * @param date 日期字符串
     * @param otherDate 另一个日期字符串
     * @param：@return
     * @throws ParseException
     * @return：int
     * @author long.deng
     * @date 2016-6-24 下午5:09:48
     * @throws
     */
    public static int getIntervalDays(String date, String otherDate)
        throws ParseException {
        return getIntervalDays(stringToDate(date), stringToDate(otherDate));
    }
    
    /**
     *  获取两个日期相差的天数
     * @param date
     * @param otherDate
     * @param：@return
     * @return：int
     * @author long.deng
     * @date 2016-6-24 下午5:10:26
     * @throws
     */
    public static int getIntervalDays(Date date, Date otherDate) {
        long time = Math.abs(date.getTime() - otherDate.getTime());
        return (int)time / (24 * 60 * 60 * 1000);
    }
    
    /**
     * 获取系统当前时间
     * @param：@return
     * @return：String
     * @author long.deng
     * @date 2016-6-24 下午5:11:18
     * @throws
     */
    public static String getNowTime() {
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(new Date());
    }
    
    
    /**
     * 获取系统当前时间
     * @param：@return
     * @return：String
     * @author long.deng
     * @date 2016-6-24 下午5:11:18
     * @throws
     */
    public static Date getNowDate() {
        SimpleDateFormat df = new SimpleDateFormat(simpletimeformat);
        try {
			return  df.parse(df.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return null;
    }
    
    /**
     * 获取系统当前时间
     * @param type
     * @param：@return
     * @return：String
     * @author long.deng
     * @date 2016-6-24 下午5:11:37
     * @throws
     */
    public static String getNowTime(String type) {
        SimpleDateFormat df = new SimpleDateFormat(type);
        return df.format(new Date());
    }
    
    /**
     * 两个时间比较
     * @param date1
     * @param：@return
     * @return：int 1:大于，-1小于
     * @author long.deng
     * @date 2016-6-24 下午5:12:01
     * @throws
     */
    public static int compareDateWithNow(Date date1) {
        Date date2 = new Date();
        int rnum = date1.compareTo(date2);
        return rnum;
    }

    public static String convertStringToDateString(String dateStr,
                                                   String oldFormat, String newFormat) {
        if (null!=dateStr && !"".equals(dateStr)) {
            SimpleDateFormat format = new SimpleDateFormat(oldFormat);
            try {
                Date date = format.parse(dateStr);
                format = new SimpleDateFormat(newFormat);
                return format.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
