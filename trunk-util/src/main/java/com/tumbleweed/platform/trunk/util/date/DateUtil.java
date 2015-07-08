package com.tumbleweed.platform.trunk.util.date;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.TimeZone;

@SuppressWarnings("rawtypes")
public class DateUtil {

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Calendar currentCalendar = Calendar.getInstance();
    private static DecimalFormat decimalFormat = new DecimalFormat("#.00");

    static {
        currentCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
    }

    /**
     * 获取当前系统时间
     * 
     * @return
     */
    public static Date currentDateTime() {
        currentCalendar.setTimeInMillis(System.currentTimeMillis());

        return currentCalendar.getTime();
    }

    /**
     * 转换日期格式
     * 
     * @return 如：2013-09-09
     */
    public static String dateToString(Date date, boolean withTime) {

        if (date == null) {
            currentCalendar.setTimeInMillis(System.currentTimeMillis());
            date = currentCalendar.getTime();
        }

        return withTime ? dateTimeFormat.format(date) : dateFormat.format(date);
    }

    /**
     * 转换日期格式
     * 
     * @return 如：2013-09-09
     */
    public static Date stringToDate(String sdate) {

        try {
            return isNullOrEmpty(sdate) ? currentDateTime() : dateFormat
                    .parse(sdate);
        } catch (ParseException e) {
            return currentDateTime();
        }
    }

    /**
     * 比较两个日期型(不包含时间)
     *
     * @param dateOne
     * @param dateTwo
     * @return dateOne是否在dateTwo日期之前
     */
    public static boolean before(String dateOne, String dateTwo) {
        return stringToDate(dateOne).before(stringToDate(dateTwo));
    }

    /**
     * 比较两个日期型(不包含时间)
     *
     * @param dateOne
     * @param dateTwo
     * @return dateOne是否在dateTwo日期之后
     */
    public static boolean after(String dateOne, String dateTwo) {
        return stringToDate(dateOne).after(stringToDate(dateTwo));
    }

    /**
     * 获取指定年度开始日期
     *
     * @param year 如:2013
     * @return 如：2013-01-01
     */
    public static String getYearBegin(String year) {
        try {
            if (!isNullOrEmpty(year)) {
                currentCalendar.set(Calendar.YEAR, Integer.parseInt(year));
            } else {
                currentCalendar.setTimeInMillis(System.currentTimeMillis());
            }
        } catch (NumberFormatException e) {
            currentCalendar.setTimeInMillis(System.currentTimeMillis());
        }
        currentCalendar.set(Calendar.MONTH, 0);
        currentCalendar.set(Calendar.DATE, 1);
        return dateToString(currentCalendar.getTime(), false);
    }

    /**
     * 获取指定年度结束日期
     *
     * @param year 如:2013
     * @return 如：2013-12-31
     */
    public static String getYearEnd(String year) {
        try {
            if (!isNullOrEmpty(year)) {
                currentCalendar.set(Calendar.YEAR, Integer.parseInt(year));
            } else {
                currentCalendar.setTimeInMillis(System.currentTimeMillis());
            }
        } catch (NumberFormatException e) {
            currentCalendar.setTimeInMillis(System.currentTimeMillis());
        }
        currentCalendar.set(Calendar.MONTH, 11);
        currentCalendar.set(Calendar.DATE, 31);
        return dateToString(currentCalendar.getTime(), false);
    }

    /**
     * 获取当前年度开始日期
     *
     * @return 如：2013-01-01
     */
    public static String getYearBegin() {
        return getYearBegin(null);
    }

    /**
     * 获取当前年度结束日期
     *
     * @return 如：2013-12-31
     */
    public static String getYearEnd() {
        return getYearEnd(null);
    }

    /**
     * 获取当前日期年月日的字串
     *
     * @return 如：2013-09-09
     */
    public static String currentDateString() {
        currentCalendar.setTimeInMillis(System.currentTimeMillis());

        return dateFormat.format(currentCalendar.getTime());
    }

    /**
     * 获取当前日期时间的字串
     *
     * @return 如：2013-09-09 09:09:09
     */
    public static String currentDateTimeString() {
        currentCalendar.setTimeInMillis(System.currentTimeMillis());

        return dateTimeFormat.format(currentCalendar.getTime());
    }

    /**
     * 获取当前日期字串的系统标识
     *
     * @param withMonth 是否包含月度
     * @return 如：201300 201309
     */
    public static String getDateFlag(boolean withMonth) {
        currentCalendar.setTimeInMillis(System.currentTimeMillis());

        return getDateFlag(currentCalendar.getTime(), withMonth);
    }

    /**
     * 获取指定日期字串的月度标识
     *
     * @param dateString yyyy-MM-dd
     * @return 如：201309
     */
    public static String getDateFlag(String dateString) {

        return (!isNullOrEmpty(dateString)) ? dateString.replaceAll("-", "")
                .substring(0, 6) : getDateFlag(true);
    }

    /**
     * 获取指定日期字串的标识
     *
     * @param dateString yyyy-MM-dd
     * @param withMonth  是否包含月度
     * @return 如：201300 201309
     */
    public static String getDateFlag(String dateString, boolean withMonth) {

        return (!isNullOrEmpty(dateString)) ? dateString.replaceAll("-", "")
                .substring(0, withMonth ? 6 : 4) + (withMonth ? "" : "00")
                : getDateFlag(withMonth);
    }

    /**
     * 获取指定日期的月度标识
     *
     * @param date
     * @return 如：201309
     */
    public static String getDateFlag(Date date) {

        return getDateFlag(date, true);
    }

    /**
     * 获取当前日期字串的标识
     *
     * @param date
     * @param withMonth 是否包含月度
     * @return 如：201300 201309
     */
    public static String getDateFlag(Date date, boolean withMonth) {

        return (date != null) ? dateFormat.format(date).replaceAll("-", "")
                .substring(0, withMonth ? 6 : 4)
                + (withMonth ? "" : "00") : getDateFlag(withMonth);
    }

    /**
     * 判断是否为空的集合
     *
     * @param c Collection
     * @return true false
     */
    public static boolean isNullOrEmpty(Collection c) {
        return (c == null || c.size() == 0);
    }

    /**
     * 判断是否为空的字符串(去除空白后)
     *
     * @param s String
     * @return true false
     */
    public static boolean isNullOrEmpty(String s) {
        return (s == null || s.trim().length() == 0);
    }
    
    /**
     * 判断是否为非数字形式字符串
     * 
     * @param n
     * @return true|false
     */
    public static boolean isNullOrNaN(String n){
        if(isNullOrEmpty(n)){
              return true;
        }else{
            try {
                new BigDecimal(n);
            } catch (NumberFormatException e) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获得当前年度标识
     *
     * @return 如:201300
     */
    public static String getCurrentYear() {
        currentCalendar.setTimeInMillis(System.currentTimeMillis());

        return currentCalendar.get(Calendar.YEAR) + "00";
    }

    /**
     * 获得当前月度标识
     *
     * @return 形如:9 10
     */
    public static int getCurrentMonth() {
        currentCalendar.setTimeInMillis(System.currentTimeMillis());

        return currentCalendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获得当前月度标识
     *
     * @param withFill 是否填充两位
     * @return 如:9 10 ; 09 10
     */
    public static String getCurrentMonth(boolean withFill) {
        int month = getCurrentMonth();

        return withFill ? month > 9 ? "" + month : "0" + month : "" + month;
    }

    /**
     * 填充月度
     *
     * @param month
     * @return 两位月度标识
     */
    public static String fillMonth(int month) {
        return month > 9 ? "" + month : "0" + month;
    }

    /**
     * 获得下一年度标识
     *
     * @return 如:201300
     */
    public static String getNextYear(String yearFlag) {

        int year = Integer.parseInt(yearFlag.substring(0, 4));
        currentCalendar.setTimeInMillis(System.currentTimeMillis());
        currentCalendar.set(Calendar.YEAR, year + 1);

        return currentCalendar.get(Calendar.YEAR) + "00";
    }

    /**
     * 获得下一月份的月度标识
     *
     * @return 如:201309
     */
    public static String getNextMonthDateFlag() {

        currentCalendar.setTimeInMillis(System.currentTimeMillis());
        currentCalendar.add(Calendar.MONTH, 1);

        return getDateFlag(currentCalendar.getTime(), true);
    }

    /**
     * 按两位小数格式格式化数值
     *
     * @param number 数值
     * @param digit 小数位(大于零)
     * @return 带小数位字串
     */
    public static String format(double number, int digit) throws ParseException {
        if (digit > -1) {
            if (digit == 2) {
                return decimalFormat.format(number);
            } else {
                StringBuilder buf = new StringBuilder("#.");
                for (int i = 0; i < digit; i++) {
                    buf.append('0');
                }

                return new DecimalFormat(buf.toString()).format(number);
            }
        }
        return decimalFormat.format(number);
    }

    /**
     * 格式化数值字串
     *
     * @param number 数值
     * @return
     */
    public static String format(double number) throws ParseException {
        return decimalFormat.format(number);
    }

    /**
     * 按两位小数格式转换字串
     *
     * @return 数值
     */
    public static Number parse(String source) throws ParseException {
        return decimalFormat.parse(source);
    }
}
