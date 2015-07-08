package com.tumbleweed.platform.trunk.util.date;

import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class DateTimeUtil {
    public static final long SECOND = 1000L;
    public static final long MINUTE = 60 * SECOND;
    public static final long HOUR = 60 * MINUTE;
    public static final long DAY = 24 * HOUR;
    public static final long WEEK = 7 * DAY;
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATETIME_LONG_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_FORMAT_CHINESE = "yyyy年MM月dd日";
    public static final String YEAR_MONTH_DAY_FORMAT = "yyyyMMdd";
    public static final String YEAR_MONTH_FORMAT = "yyyyMM";
    public static final String YEAR_FORMAT = "yyyy";
    public static final String MONTH_DAY_FORMAT = "MMdd";
    public static final String MONTH_FORMAT = "MM";
    public static final String DAY_FORMAT = "dd";

    /**
     * 得到一个年,月,日 的Date对象
     * 
     * @param y
     *            年
     * 
     * @param m
     *            月
     * 
     * @param d
     *            日
     * 
     * @return
     */
    public static Date date(int y, int m, int d) {
        return date(y, m - 1, d, 0, 0, 0);
    }

    /**
     * 得到一个年,月,日,时,分,秒 的Date对象
     * 
     * @param y
     *            年
     * 
     * @param m
     *            月
     * 
     * @param d
     *            日
     * 
     * @param h
     *            时
     * 
     * @param ms
     *            分
     * 
     * @param s
     *            秒
     * 
     * @return
     */
    public static Date date(int y, int m, int d, int h, int ms, int s) {
        Calendar cal = Calendar.getInstance();
        cal.set(y, m, d, h, ms, s);
        return cal.getTime();
    }

    public static Date removeTime(Date d) {
        return setTime(d, 0, 0, 0);
    }

    /**
     * 设置时间
     * 
     * @param d
     * @param h
     * @param m
     * @param s
     * @return
     */
    public static Date setTime(Date d, int h, int m, int s) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.set(Calendar.HOUR_OF_DAY, h);
        cal.set(Calendar.MINUTE, m);
        cal.set(Calendar.SECOND, s);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static String formatDate(String format, Date d) {
        return new SimpleDateFormat(format).format(d);
    }

    public static String formatDate(Date d) {
        if (d == null) {
            return null;
        }
        return new SimpleDateFormat(DATE_FORMAT).format(d);
    }

    public static String formatDateTime(Date d) {
        if (d == null) {
            return null;
        }
        return new SimpleDateFormat(DATETIME_FORMAT).format(d);
    }

    public static String formatPeriod(long period) {
        StringBuffer buf = new StringBuffer(20);
        long d;
        long r = period;
        d = r / DateTimeUtil.DAY;
        r = r % DateTimeUtil.DAY;
        if (d != 0) {
            buf.append(d).append("d");
        }
        d = r / DateTimeUtil.HOUR;
        r = r % DateTimeUtil.HOUR;
        if (d != 0) {
            buf.append(d).append("h");
        }
        d = r / DateTimeUtil.MINUTE;
        r = r % DateTimeUtil.MINUTE;
        if (d != 0) {
            buf.append(d).append("m");
        }
        d = r / DateTimeUtil.SECOND;
        r = r % DateTimeUtil.SECOND;
        if (d != 0 || r != 0) {
            buf.append(d);
            if (r != 0) {
                buf.append(".").append(r);
            }
            buf.append("s");
        }
        return buf.toString();
    }

    /**
     * 根据传入的格式,将一个字符转换成个的Date对象,如果转换失败则返回默认的Date
     * 
     * @param v
     *            要转换的字符串
     * 
     * @param fm
     *            格式
     * @param def
     *            默认的Date对象
     * @return
     */
    public static Date parse(String v, String fm, Date def) {
        if(StringUtils.isEmpty(v)){
            return def;
        }
        try {
            return new SimpleDateFormat(fm).parse(v);
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * yyyy-MM-dd
     */
    public static Date parseDate(String v, Date def) {
        return parse(v, "yyyy-MM-dd", def);
    }

    /**
     * yyyy-MM-dd HH:mm:ss.SSS
     */
    public static Date parseLongDateTime(String v, Date def) {
        return parse(v, DATETIME_LONG_FORMAT, def);
    }

    /**
     * yyyy-MM-dd HH:mm:ss.SSS
     */
    public static Date parseLongDateTime(String v, long def) {
        return parse(v, DATETIME_LONG_FORMAT, new Date(def));
    }

    public static long parseDate(String v, long def) {
        Date r = parse(v, "yyyy-MM-dd", new Date(def));
        return r.getTime();
    }

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static Date parseDateTime(String v, Date def) {
        return parse(v, "yyyy-MM-dd HH:mm:ss", def);
    }

    public static long parseDateTime(String v, long def) {
        Date r = parse(v, "yyyy-MM-dd HH:mm:ss", new Date(def));
        return r.getTime();
    }

    /**
     * 得到一个星期的第一天的时间,默认这个星期的第一天为星期日 00:00:00
     * 
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        return getFirstDayOfWeek(date, Calendar.SUNDAY);
    }

    /**
     * 得到某一天的该星期的第一日 00:00:00
     * 
     * @param date
     * @param firstDayOfWeek
     *            一个星期的第一天为星期几
     * 
     * @return
     */
    public static Date getFirstDayOfWeek(Date date, int firstDayOfWeek) {
        Calendar cal = Calendar.getInstance();
        if (date != null){
            cal.setTime(date);
        }
        cal.setFirstDayOfWeek(firstDayOfWeek);
        cal.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 得到一个星期的最后一日的时间,默认这个星期的第一天为星期天 00:00:00
     * 
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        return getFirstDayOfWeek(date, Calendar.SUNDAY);
    }

    /**
     * 得到指定日期相关的周的最后一日 00:00:00
     * 
     * @param date
     * @param firstDayOfWeek
     * @return
     */
    public static Date getLastDayOfWeek(Date date, int firstDayOfWeek) {
        Calendar cal = Calendar.getInstance();
        if (date != null){
            cal.setTime(date);
        }
        cal.setFirstDayOfWeek(firstDayOfWeek);
        cal.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);
        cal.add(Calendar.DAY_OF_WEEK, 6);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 得到指定日期的在这个月的第一日. 00:00:00
     * 
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null){
            cal.setTime(date);
        }
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 得到本年指定日期的相关月份的最后一日 00:00:00
     * 
     * @param date
     * @return
     */
    public static Date getLastDayOfMonthThisYear(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null){
            cal.setTime(date);
        }
        cal.set(Calendar.DAY_OF_MONTH,
        cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 判断是否为合法的时间格式
     * 
     * @param date
     * @return
     */
    public static boolean isDateTimeFormat(String date) {
        if (date == null) {
            return false;
        }
        if (date.trim().replaceAll("\\s+", " ").length() > DateTimeUtil.DATETIME_FORMAT
                .length()) {
            return false;
        }
        if (!Pattern
                .matches(
                        "[12]{1}[09]{1}\\d{2}-[01]?[0-9]?-[0-3]?[0-9]?\\s+[0-5]?[0-9]?:[0-5]?[0-9]?:[0-5]?[0-9]?",
                        date)) {
            return false;
        }
        return true;
    }

    /**
     * 判断传入的字符串是否为指定日期格式的合法格式
     * 
     * @param date
     *            日期字符串(如:1999,2008等)
     * @param format
     *            格式(yyyyMMdd,yyyy-MM-dd等格式)
     * @return true - 合法格式,false - 非法格式
     */
    public static boolean isRightDate(String date, String format) {
        if (date == null || format == null) {
            return false;
        }

        try {
            new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    // 每月的天数

//    public static final int[] dayArray = new int[] { 31, 28, 31, 30, 31, 30,
//            31, 31, 30, 31, 30, 31};

    /**
     * 将日期制定格式化
     * 
     * @param date
     * @param pattern
     * @return
     * @author 
     */
    public static String formatDate(Date date, String pattern) {
        return (new SimpleDateFormat(pattern)).format(date);
    }

    /**
     * 将日期制定格式化(长日期格式 [yyyy-MM-dd HH:mm:ss.SSS])
     * 
     * @param date
     * @param pattern
     */
    public static String formatLongDateTime(Date date) {
        return formatDate(date, DATETIME_LONG_FORMAT);
    }

    /**
     * 将日期制定格式化(长日期格式 [yyyy-MM-dd HH:mm:ss.SSS])
     * 
     * @param date
     * @param pattern
     */
    public static String formatLongDateTime(long time) {
        return formatDate(new Date(time), DATETIME_LONG_FORMAT);
    }

    /**
     * 判定指定年份是否是闰年
     * 
     * 
     * @param year
     *            年
     * 
     * @return
     * @author 
     */
    public static boolean isLeapYear(int year) {
        boolean leapYear;
        if((year % 4==0 && year % 100!= 0)|| year%400 == 0) {
            leapYear = true;
        }else{
            leapYear =  false;
        }
        return leapYear;
    }

    /**
     * 取得指定日期的所处月份的最后一天
     * 
     * 
     * @param date
     *            指定日期。
     * 
     * @return 指定日期的所处月份的最后一天
     * 
     * @author 
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar gc = Calendar.getInstance();
        gc.setTime(date);
        gc.add(Calendar.MONTH, 1);
        gc.add(Calendar.DAY_OF_MONTH, -1 * (gc.get(Calendar.DAY_OF_MONTH)));

        gc.set(Calendar.HOUR_OF_DAY, 23);
        gc.set(Calendar.MINUTE, 59);
        gc.set(Calendar.SECOND, 59);
        return new Date(gc.getTimeInMillis());
    }

    /**
     * 给日期+00:00:00
     * 
     * @param date
     * @return
     */
    public static Date getStartDateByDate(Date date) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.set(Calendar.HOUR, 0);
        gc.set(Calendar.MINUTE, 0);
        gc.set(Calendar.SECOND, 0);
        gc.set(Calendar.MILLISECOND, 0);
        return new Date(gc.getTimeInMillis());
    }

    /**
     * 给日期+23:59:0059
     * 
     * @param date
     * @return
     */
    public static Date getEndDateByDate(Date date) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.set(Calendar.HOUR_OF_DAY, 23);
        gc.set(Calendar.MINUTE, 59);
        gc.set(Calendar.SECOND, 59);
        gc.set(Calendar.MILLISECOND, 999);
        return new Date(gc.getTimeInMillis());
    }
    
    /**
     * 给指定日期+N小时+N分钟
     * 
     * @param date
     * @param hour 小时
     * @param minute 分
     * @return
     */
    public static Date getEndDateByHour(Date date,int hour,int minute) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        gc.add(Calendar.HOUR_OF_DAY, hour);
        gc.add(Calendar.MINUTE, minute);
        return new Date(gc.getTimeInMillis());
    }

    /**
     * 取得指定日期
     * 
     * @param year
     *            年
     * 
     * @param month
     *            月
     * 
     * @param day
     *            日
     * 
     * @return
     * @author 
     */
    public static synchronized Date getDateTime(int year, int month, int day) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        if (isLeapYear(year) && month == 1) {// 润年的2月

            day = day + 1;
        }
        gc.set(year, month, day);
        return new Date(gc.getTimeInMillis());
    }

    /**
     * 获取服务器当前日期
     * 
     * 
     * @return Date 服务器当前日期
     */
    public static Date getNowDateByServer() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 获取服务器当前年
     * 
     * @return Integer 服务器当前年
     */
    public static Integer getNowYearByServer() {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(getNowDateByServer());
        return gc.get(Calendar.YEAR);
    }

    /**
     * 获取服务器当前月
     * 
     * @return Integer 服务器当前月
     */
    public static Integer getNowMonthByServer() {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(getNowDateByServer());
        return gc.get(Calendar.MONTH);
    }

    /**
     * 获取服务器当前日
     * 
     * @return Integer 服务器当前日
     */
    public static Integer getNowDayByServer() {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(getNowDateByServer());
        return gc.get(Calendar.DAY_OF_WEEK_IN_MONTH);
    }

    /**
     * 把一个Date对象格式化成一个“yyyy-MM-dd”日期字符串（不包括时间）
     * 
     * 
     * @param tm
     *            Date
     * @return String
     * @author 
     */
    public static String formatDateToString(Date tm) {
        if (tm == null) {
            return "";
        }
        return formatDate(tm, DATE_FORMAT);
    }

    /**
     * 把服务器日期格式化成一个date_format日期字符串（不包括时间）
     * 
     * @param format
     *            String 格式化样式
     * 
     * @return String
     * @author 
     */
    public static String formatDateToString(String format) {
        return formatDate(getNowDateByServer(), format);
    }

    /**
     * 把一个Date对象格式化成一个date_format日期字符串（不包括时间）
     * 
     * @param tm
     *            Date
     * @param date_format
     *            String 格式化样式
     * 
     * @return String
     * @author 
     */
    public static String formatDateToString(Date tm, String format) {
        if (tm == null) {
            return "";
        }
        return formatDate(tm, format);
    }

    /**
     * 取上月
     * 
     * 
     * @param sMonth
     *            String 月份，格式为:yyyymm
     * @return String
     */
    public static String getLastMonth(String sMonth) {
        String sTmpYear, sTmpMonth;
        int iYear, iMonth;
        iYear = Integer.parseInt(sMonth.substring(0, 4));
        iMonth = Integer.parseInt(sMonth.substring(4, 6));
        if (iMonth == 1) {
            iYear--;
            iMonth = 12;
        } else {
            iMonth--;
        }
        sTmpYear = "0000" + Integer.toString(iYear);
        sTmpMonth = "00" + Integer.toString(iMonth);
        return sTmpYear.substring(sTmpYear.length() - 4, sTmpYear.length())
                + sTmpMonth.substring(sTmpMonth.length() - 2,
                        sTmpMonth.length());
    }

    /**
     * 取下月
     * 
     * 
     * @param sMonth
     *            String 月份，格式为:yyyymm
     * @return String
     */
    public static String getNextMonth(String sMonth) {
        String sTmpYear, sTmpMonth;
        int iYear, iMonth;
        iYear = Integer.parseInt(sMonth.substring(0, 4));
        iMonth = Integer.parseInt(sMonth.substring(4, 6));
        if (iMonth == 12) {
            iYear++;
            iMonth = 1;
        } else {
            iMonth++;
        }
        sTmpYear = "0000" + Integer.toString(iYear);
        sTmpMonth = "00" + Integer.toString(iMonth);
        return sTmpYear.substring(sTmpYear.length() - 4)
                + sTmpMonth.substring(sTmpMonth.length() - 2);
    }

    /**
     * @param sMonth传入的年月
     * 
     * @param shiftValue需要增加或减少的月数
     * 
     * @return 需要的年月
     */
    public static String getRelativeMonth(String sMonth, int shiftValue) {
        int tempMonth = Integer.parseInt(sMonth.substring(0, 4)) * 12
                + Integer.parseInt(sMonth.substring(4, 6));
        tempMonth = tempMonth + shiftValue;
        if (tempMonth % 12 == 0) {
            return (tempMonth / 12 - 1) + "12";
        } else {
            String sTmpMonth = "00" + tempMonth % 12;
            return (tempMonth / 12)
                    + sTmpMonth.substring(sTmpMonth.length() - 2);
        }
    }

    /**
     * 取得两个月份之间相差的月数
     * 
     * 
     * @param begMonth
     *            String 格式为'yyyymm'
     * @param endMonth
     *            String 格式为'yyyymm'
     * @return int
     */
    public static int calcuMonth(String begMonth, String endMonth) {
        int result = 0;
        //
        if ((begMonth == null || begMonth.length() != 6)
                || (endMonth == null || endMonth.length() != 6)) {
            return result;
        }
        result = 12
                * (Integer.parseInt(endMonth.substring(0, 4)) - Integer
                        .parseInt(begMonth.substring(0, 4)))
                + Integer.parseInt(endMonth.substring(4))
                - Integer.parseInt(begMonth.substring(4));
        return result;
    }

    /**
     * 获取dateTime的00期间
     * 
     * @param dateTime
     *            日期
     * @return dateTime的00期间
     */
    public static String getYearFirstAcctTermNoByDate(Date dateTime) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(dateTime);
        return gc.get(Calendar.YEAR) + "00";
    }

    /**
     * 得到当前时间本年的第一天
     * @author zhg
     * @param dateTime 时间
     * @return Date
     * */
    public static Date getFirstDateForYear(Date dateTime) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(dateTime);
        gc.set(Calendar.MONTH, Calendar.JANUARY);
        gc.set(Calendar.DAY_OF_MONTH, 1);
        gc.set(Calendar.HOUR_OF_DAY, 0);
        gc.set(Calendar.MINUTE, 0);
        gc.set(Calendar.SECOND, 0);
        gc.set(Calendar.MILLISECOND, 0);
        return gc.getTime();
    }

    /**
     * 获取年
     * 
     * 
     * @param dateTime
     *            日期
     * @return 年
     */
    public static int getYearByDate(Date dateTime) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(dateTime);
        return gc.get(Calendar.YEAR);
    }

    /**
     * 获取月
     * 
     * 
     * @param dateTime
     *            日期
     * @return 月
     */
    public static int getMonthByDate(Date dateTime) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(dateTime);
        return gc.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日
     * 
     * 
     * @param dateTime
     *            日期
     * @return 日
     */
    public static int getDayByDate(Date dateTime) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(dateTime);
        return gc.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得下一天
     * 
     * 
     * @param dateTime
     * @return
     */
    public static Date getNextDay(Date dateTime) {
        Calendar gc = Calendar.getInstance();
        gc.setTime(dateTime);
        gc.add(Calendar.DAY_OF_MONTH, +1);
        return gc.getTime();
    }

    /**
     * 根据日期获取会计区间
     * 
     * @param time
     *            时间
     * @return 会计区间
     */
    public static String createAcctTermNoByDate(Date time) {
        String acctTermNo = "";
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(time);
        acctTermNo += gc.get(Calendar.YEAR);
        acctTermNo += gc.get(Calendar.MONTH) >= 9 ? (gc.get(Calendar.MONTH) + 1)
                : ("0" + (gc.get(Calendar.MONTH) + 1));
        return acctTermNo;
    }

    /**
     * 根据日期取得年月字符串
     * 
     * 
     * @param time
     * @return
     */
    public static String getYearMonth(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        return String.valueOf(cal.get(Calendar.YEAR))
                + ((cal.get(Calendar.MONTH) >= 9) ? String.valueOf(cal
                        .get(Calendar.MONTH) + 1) : "0"
                        + String.valueOf(cal.get(Calendar.MONTH) + 1));
    }

    /**
     * 根据年月生成会计期间
     * 
     * @param year
     *            年
     * 
     * @param month
     *            月
     * 
     * @retu+rn 会计区间
     */
    public static Date conversionDate(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 0);
        return c.getTime();
    }

    /**
     * 获得某会计期间所在月份的第一天
     * 
     * 
     * @param AcctTermNo
     * @return
     */
    public static Date getFirstDayOfMonthByAcctTermNo(String acctTermNo) {
        int year = Integer.parseInt(acctTermNo.substring(0, 4));
        int month = Integer.parseInt(acctTermNo.substring(4, 6));
        Date date = date(year, month, 1);
        return getStartDateByDate(date);
    }

    /**
     * 判断结束日期大于起始日期 返回 false
     * 
     * @param startDate
     *            起始日期
     * @param endDate
     *            结束日期
     * @return
     */
    public static boolean compareDate(Date startDate, Date endDate){
        if (endDate.after(startDate)) {
            return true;
        }
        return false;
    }

    /**
     * 根据开始时间和结束时间,得到之间的所有时间(按月份)
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<Date> getBetweenDates(Date startDate, Date endDate){
        if (startDate == null){
            if (endDate != null){
                return Arrays.asList(new Date[] {endDate});
            }
        } else {
            if (endDate == null){
                return Arrays.asList(new Date[] {startDate});
            }
        }
        List<Date> dates = new ArrayList<Date>();
        if (startDate.getTime() > endDate.getTime()) {
            return dates;
        }

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int startYear = 0;
        int startMonth = 0;
        int endYear = endCal.get(Calendar.YEAR);
        int endMonth = endCal.get(Calendar.MONTH);

        while (true) {
            dates.add(new Date(startCal.getTime().getTime()));
            startYear = startCal.get(Calendar.YEAR);
            startMonth = startCal.get(Calendar.MONTH);

            if (startYear == endYear && startMonth == endMonth) {
                break;
            }
            startCal.add(Calendar.MONTH, 1);
        }
        return dates;
    }

    /**
     * 根据开始时间和结束时间,得到之间的所有时间(按月份)
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<String> getBetweenDates(Date startDate, Date endDate,
            String dateFormat) {
        List<String> lst = new ArrayList<String>();
        List<Date> listDate = getBetweenDates(startDate, endDate);
        if (listDate != null && listDate.size() > 0) {
            for (Date d : listDate) {
                lst.add(formatDate(d, dateFormat));
            }
        }
        return lst;
    }

    /**
     * 增加月份,返回新日期
     * 
     * 
     * @param sYearMonthDay
     *            Date 年月份
     * 
     * @param months
     *            int 月数
     * @return
     */
    public static Date addMonth(Date sYearMonthDay, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(sYearMonthDay);
        cal.add(Calendar.MONTH, months);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取传入日期月份+months 所在月的会计区间
     * 
     * @param time
     *            日期
     * @param months
     *            月
     * @return 会计期间
     */
    public static String getUpAcctTermNoForDate(Date time, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        cal.add(Calendar.MONTH, months);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return createAcctTermNoByDate(cal.getTime());
    }

    /**
     * 获取传入日期的所在年的1月1日
     * 
     * 
     * @param dateTime
     *            日期
     * @return Date
     */
    public static Date getFirstYearDay(Date dateTime) {
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(dateTime);
        gc.get(Calendar.YEAR);
        gc.set(gc.get(Calendar.YEAR), 0, 1);
        return gc.getTime();
    }

    /**
     * 生成新的时间
     * 
     * @param oriDateTime
     *            Date 原来的时间
     * 
     * @param type
     *            int 滚动类型 GregorianCalendar
     * @param rolls
     *            int 滚动的数量
     * 
     * @return Date
     * @add by dingji
     */
    public static Date genNewDateTime(Date oriDateTime, int type, int rolls) {
        GregorianCalendar c1 = new GregorianCalendar();
        c1.setTime(oriDateTime);
        c1.add(type, rolls);
        return c1.getTime();
    }

    /**
     * 添加天
     * 
     * 
     * @param date
     * @param day
     * @return date
     */
    public static Date addDays(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    /**
     * 获取某日期的年月
     * 
     * @param date
     * @return String yyyyMM
     */
    @SuppressWarnings("deprecation")
    public static String getYearMonthToYYYYMM(Date date) {
        String yearMonth = "";
        if (date.getMonth() + 1 < 10) {
            yearMonth = (date.getYear() + 1900) + "0" + (date.getMonth() + 1);
        } else {
            yearMonth = (date.getYear() + 1900) + "" + (date.getMonth() + 1);
        }
        return yearMonth;
    }

    /**
     * 获得上月日期(yyyyMM)
     * 
     * @param yyyymm
     * @return
     */
    public static String getPreMonthYYYYMM(String yyyymm) {
        String str = yyyymm.substring(0, 4) + "-" + yyyymm.substring(4, 6)
                + "-01";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = new Date(df.parse(str).getTime());
            return getYearMonthToYYYYMM(genNewDateTime(date,
                    GregorianCalendar.MONTH, -1));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String stringYearMonthByAcctTermNo(String accTermNO, int month) {
        String str = accTermNO.substring(0, 4) + "-"
                + accTermNO.substring(4, 6) + "-01";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = new Date(df.parse(str).getTime());
            return getYearMonthToYYYYMM(genNewDateTime(date,
                    GregorianCalendar.MONTH, month));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回两个时间相差多少天
     * 
     * @param startDate
     * @param endDate
     * @return int 天数
     */
    public static int getBetweenDays(Date startDate, Date endDate) {
        return Math.abs(new Long((removeTime(startDate).getTime() - removeTime(
                endDate).getTime())
                / DAY).intValue());
    }

    /**
     * 得到月初
     * 
     * @param year
     * @param month
     *            (1--12)
     * @return java.util.Date
     * @add by dj
     */
    public static Date getStartMonthDate(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getTime();
    }

    /**
     * 得到月末
     * 
     * @param year
     * @param month
     *            (1--12)
     * @return java.util.Date
     * @add by dj
     */
    public static Date getEndMonthDate(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(year, month - 1, day);
        return calendar.getTime();
    }

    public static Date getEndMonthDate(String yearmonth) {
        int year = Integer.parseInt(yearmonth.substring(0, 4));
        int month = Integer.parseInt(yearmonth.substring(4, 6));
        return getEndMonthDate(year, month);
    }

    /**
     * 得到月末最后时间
     * 
     * @param yearmonth
     *            yyyyMM
     * @return java.util.Date
     */
    public static Date getEndMonthDateTime(String yearmonth) {
        Date date = getEndMonthDate(yearmonth);
        return getEndDateByDate(date);
    }

    /**
     * 计算二个时间段的月数之差
     * 
     * @param endDate
     *            结束时间, startDate 开始时间
     * @return 月数
     * @author tiack
     */
    public static long betweenMonths(Date endDate, Date startDate) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(endDate);
        long endmonths = cal.get(Calendar.YEAR) * 12 + cal.get(Calendar.MONTH);
        cal.setTime(startDate);
        long startmonths = cal.get(Calendar.YEAR) * 12
                + cal.get(Calendar.MONTH);
        return endmonths - startmonths;
    }

    /**
     * 计算二个时间段的之差（年，月，日之差）
     * 
     * @param enddate
     * @param startdate
     * @param flag
     *            Y:年,M:月,D:日
     * @return
     */
    public static long betweenDate(Date enddate, Date startdate, String flag) {
        if ("Y".equalsIgnoreCase(flag)) {
            return Math.abs(getYearByDate(enddate) - getYearByDate(startdate));
        } else if ("M".equalsIgnoreCase(flag)) {
            return betweenMonths(enddate, startdate);
        } else if ("D".equalsIgnoreCase(flag)) {
            return getBetweenDays(startdate, enddate);
        }
        return 0;
    }

    /**
     * 增加月数
     * 
     * @param source
     *            日期
     * @param months
     *            月数
     * @return 新日期
     * @author tiack
     */
    public static Date addMonths(Date source, int months) {
        Date newDate = (Date) source.clone();
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(newDate);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

    /**
     * 增加年数
     * 
     * @param source
     *            日期
     * @param years
     *            年数
     * @return 新日期
     * @author tiack
     */
    public static Date addYears(Date source, int years) {
        Date newDate = (Date) source.clone();
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(newDate);
        cal.add(Calendar.YEAR, years);
        return cal.getTime();
    }

    /**
     * 获得本年的12区间
     * 
     * @param date
     * @return
     */
    public static String getCurrentLastAcctTerm(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return String.valueOf(cal.get(Calendar.YEAR)) + "12";
    }

    /**
     * 获得上一年的12区间
     * 
     * @param date
     * @return
     */
    public static String getPreviousLastAcctTerm(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return String.valueOf(cal.get(Calendar.YEAR) - 1) + "12";
    }

    
    @SuppressWarnings("unused")
    public static Date max(Date... dates) {
        if (dates == null | dates.length == 0) {
            return null;
        }
        if (dates.length == 1) {
            return dates[0];
        }
        Date maxDate = dates[0];
        for (Date d : dates) {
            if (d == null) {
                continue;
            }
            if (maxDate == null) {
                maxDate = d;
            }
            if (maxDate == null) {
                continue;
            }
            if (maxDate.compareTo(d) < 0) {
                maxDate = d;
            }
        }
        return maxDate;
    }

    /**
     * 得到当前时区的零时区的时间
     * 
     * @return
     */
    public static Date getGMTDate() {
        Date d = new Date();
        return new Date(d.getTime()
                - TimeZone.getDefault().getOffset(System.currentTimeMillis()));
    }

    /**
     * 得到当前时区的某毫秒数的零时区时间
     * 
     * @param date
     * @return
     */
    public static Date getGMTDate(long date) {
        Date d = new Date(date);
        return new Date(d.getTime()
                - TimeZone.getDefault().getOffset(System.currentTimeMillis()));
    }

    /**
     * 得到当前时区的某毫秒数的正八时区(GMT+0800)的时间
     * 
     * @param date
     * @return
     */
    public static Date getGMTPlus800Date(long date) {
        Date d = new Date(date);
        return new Date(d.getTime()
                - TimeZone.getDefault().getOffset(System.currentTimeMillis())
                + (HOUR * 8));
    }

    /**
     * 得到当前时区的正八时区(GMT+0800)的时间
     * 
     * @return
     */
    public static Date getGMTPlus800Date() {
        return getGMTPlus800Date(new Date().getTime());
    }

    
    @SuppressWarnings("deprecation")
    public static boolean isSameDay(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            return false;
        }
        if (d1.getYear() == d2.getYear() && d1.getMonth() == d2.getMonth()
                && d1.getDate() == d2.getDate()) {
            return true;
        }

        return false;
    }

    @SuppressWarnings("deprecation")
    public static boolean isSameMonth(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            return false;
        }
        if (d1.getYear() == d2.getYear() && d1.getMonth() == d2.getMonth()) {
            return true;
        }
        return false;
    }

}
