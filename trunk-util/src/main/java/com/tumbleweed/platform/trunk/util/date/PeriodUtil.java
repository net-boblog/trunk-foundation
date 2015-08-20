package com.tumbleweed.platform.trunk.util.date;

import org.apache.commons.lang.StringUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 一些杂七杂八的工具方法的集合
 * @date 2014年2月21日
 */
public class PeriodUtil {

    public static final List<String> MONTH_CODE_LIST = getMonthCodeList();

    public static String[] getAgoYear(String nowYear) throws Exception {
        String[] years = new String[3];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

        Date date = sdf.parse(nowYear.substring(0, 4));
        String year = sdf.format(date);//获得今年
        date.setYear(date.getYear() - 1);
        String lYear = sdf.format(date);//获得去年
        date.setYear(date.getYear() - 1);
        String blYear = sdf.format(date);//获得前年

        years[0] = year;
        years[1] = lYear;
        years[2] = blYear;

        return years;

    }

    /**
     * 获取当前月的编码,1月为01,2月为02...12月为12
     *
     * @return
     */
    public static String getCurrentMonthCode() {
        return MONTH_CODE_LIST.get(Calendar.getInstance().get(Calendar.MONTH));
    }


    /**
     * 获取当前年份的编码
     *
     * @return
     */
    public static String getCurrentYearCode() {
        return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    }

    /**
     * 获取一个月度Period的集合
     *
     * @param yearPeriod 年度Period
     * @param start      开始月份 1-12
     * @param end        结束月份 1-12
     * @return
     */
    public static Collection<String> getMonthPeriods(String yearPeriod, int start, int end) {
        Collection<String> monthPeriods = new ArrayList<String>(end - start + 1);
        for (int month = start; month <= end; month++) {
            monthPeriods.add(getMonthPeriod(yearPeriod, month));
        }
        return monthPeriods;
    }

    /**
     * 获取某个月份之前的 Periods集合
     *
     * @param yearPeriod
     * @param end
     * @return
     */
    public static Collection<String> getBeforeMonthPeriods(String yearPeriod, int end) {
        return getMonthPeriods(yearPeriod, 1, end - 1);
    }

    /**
     * 获取当前月的的十进制整数编码,1月为1,2月为2...12月为12
     *
     * @return
     */
    public static Integer getCurrentMonthIntCode() {
        return getMonthIntCode(getCurrentMonthCode());
    }

    /**
     * 获取月份的整数数值
     *
     * @param month
     * @return
     */
    public static Integer getMonthIntCode(String month) {
        return Integer.parseInt(month, 10);
    }

    /**
     * 获取月度
     *
     * @param period 未知的Period,格式一般为 201401(yyyymm)
     * @return 月份 如01
     */
    public static String getMonth(String period) {
        if (period == null || period.length() != 6) {
            return null;
        }

        return period.substring(4, 6);
    }

    /**
     * 获取年度度Period
     *
     * @param period 未知的Period,格式一般为 201400(yyyymm)
     * @return 年度Period, 例如:201400
     */
    public static String getYearPeriod(String period) {
        if (period == null || period.length() < 4) {
            return null;
        }

        return period.substring(0, 4).concat("00");
    }

    /**
     * 获取月度Period
     *
     * @param yearPriod 年度Period,格式一般为 201400(yyyymm)
     * @param month     月份,格式为 01,02,03,04,05,06,07,08,09,10,11,12
     * @return 月度Period, 例如:201402
     */
    public static String getMonthPeriod(String yearPriod, String month) {
        // TODO 需要添加对month和yearPeriod参数进行判断的功能，使结果总是yyyymm的格式
        if (month == null) {
            return null;
        }

        return getMonthPeriod(yearPriod, Integer.parseInt(month));
    }

    /**
     * 获取一年中第一个月度的月度Period
     *
     * @param period 年度或月度Period
     * @return 第一个月的月度Period, 例如:201401
     */
    public static String getFirstMonthPeriod(String period) {
        return getYearPeriod(period).concat("01");
    }

    /**
     * 获取月度Period
     *
     * @param yearPriod 年度Period,格式一般为 201400(yyyymm)
     * @param month     月份,格式为 01,02,03,04,05,06,07,08,09,10,11,12
     * @return 月度Period, 例如:201402
     */
    public static String getMonthPeriod(String yearPriod, Integer month) {
        String tempMonth = "00";
        if (month != null && month.intValue() < 10) {
            tempMonth = "0" + month;
        } else if (month != null) {
            tempMonth = month.toString();
        }
        return yearPriod.substring(0, 4).concat(tempMonth);
    }

    /**
     * 是否为一个月度Period
     *
     * @param period
     * @return
     */
    public static boolean isMonthPeriod(String period) {
        if (StringUtils.isBlank(period)) {
            return false;
        }

        if (period.length() != 6) {
            return false;
        }

        if (MONTH_CODE_LIST.contains(period.substring(4, 6))) {
            return true;
        }

        return false;
    }

    /**
     * 是否为一个年度Period
     *
     * @param period
     * @return
     */
    public static boolean isYearPeriod(String period) {
        if (StringUtils.isBlank(period)) {
            return false;
        }

        if (period.length() != 6) {
            return false;
        }

        if (period.endsWith("00")) {
            return true;
        }

        return false;
    }

    private static List<String> getMonthCodeList() {
        List<String> months = new ArrayList<String>();
        months.add("01");
        months.add("02");
        months.add("03");
        months.add("04");
        months.add("05");
        months.add("06");
        months.add("07");
        months.add("08");
        months.add("09");
        months.add("10");
        months.add("11");
        months.add("12");
        return months;
    }


    /**
     * 获取参数yearPeriod的去年的yearPeriod
     *
     * @param yearPeriod
     * @return
     */
    public static String getLastYearPeriod(String yearPeriod) {
        return Long.toString(Long.parseLong(getYearPeriod(yearPeriod)) - 100);
    }

    /**
     * * 获取参数yearPeriod的前年的yearPeriod
     *
     * @param yearPeriod
     * @return
     */
    public static String getBeforeLastYearPeriod(String yearPeriod) {
        return Long.toString(Long.parseLong(getYearPeriod(yearPeriod)) - 100);
    }
    
    /**
     * 一年所有的月份 yyyy-MM-dd
     * @param year
     * @return
     */
    public static String getFillDate(String year){
        String fillYearAndMonth = "";
        DecimalFormat df = new DecimalFormat("00");
        for (int i = 1; i <= 12; i++) {
            if(i == 1){
                fillYearAndMonth = year+df.format(i);
            }else{
                fillYearAndMonth += ","+year+df.format(i);
            }
        }
        return fillYearAndMonth;
    }
    
    /**
     * 一年所有的月份 yyyy-MM-dd
     * @param year
     * @return
     */
    public static Collection<String> getFillDateMonth(String year){
        Collection<String> fillYearAndMonth = new ArrayList<String>();
        DecimalFormat df = new DecimalFormat("00");
        for (int i = 1; i <= 12; i++) {
            fillYearAndMonth.add(year+df.format(i));
        }
        return fillYearAndMonth;
    }
    
    /**
     * 获取一个期间段
     * @param sPeriod
     * @param ePeriod
     * @return
     */
    public static String getFillDateSlot(String sPeriod,String ePeriod){
        String fillYearAndMonth = "";
        String year = sPeriod.substring(0, 4);
        Integer s = Integer.parseInt(sPeriod.substring(4, 6));
        Integer e = Integer.parseInt(ePeriod.substring(4, 6));
        DecimalFormat df = new DecimalFormat("00");
        for (int i = s; i <= e; i++) {
            if(i == s){
                fillYearAndMonth = "'"+year+df.format(i)+"'";
            }else{
                fillYearAndMonth += ",'"+year+df.format(i)+"'";
            }
        }
        return fillYearAndMonth;
    }

    /**
     * 获取一个时间段但是剔除特殊日期
     * @return
     */
    public static Object getDefectDateSlot(String sPeriod,String ePeriod,Integer month){
        String fillYearAndMonth = "";
        String year = sPeriod.substring(0, 4);
        Integer s = Integer.parseInt(sPeriod.substring(4, 6));
        Integer e = Integer.parseInt(ePeriod.substring(4, 6));
        DecimalFormat df = new DecimalFormat("00");
        for (int i = s; i <= e; i++) {
            if(i != month.intValue()){
                if(i == s){
                    fillYearAndMonth = year+df.format(i)+",";
                }else if(i == e){
                    fillYearAndMonth += year+df.format(i);
                }else{
                    fillYearAndMonth += year+df.format(i)+",";
                }
            }
        }
        String lastString = fillYearAndMonth.substring(fillYearAndMonth.length()-1);
        if(lastString.equals(",")){
            fillYearAndMonth = fillYearAndMonth.substring(0,fillYearAndMonth.length()-1);
        }
        return fillYearAndMonth;
    }
    
    /**
     * 根据传入的六位期间，获取月份，并转换为中文
     * @param period  期间（例如：201501）
     * @return
     */
    public static String getMonthToConvertPeriod(String period){
        if(period == null || period.equals("")){
            return "";
        }
        String monthString = period.substring(4,6);
        if(monthString == null || monthString.equals("")){
            return "";
        }
        if(monthString.contains("0")){
            monthString = monthString.substring(1,2);
        }
        String result = "";
        int month = Integer.valueOf(monthString);
        switch (month) {
            case 1:
                result = "一月";
                break;
            case 2:
                result = "二月";
                break;
            case 3:
                result = "三月";
                break;
            case 4:
                result = "四月";
                break;
            case 5:
                result = "五月";
                break;
            case 6:
                result = "六月";
                break;
            case 7:
                result = "七月";
                break;
            case 8:
                result = "八月";
                break;
            case 9:
                result = "九月";
                break;
            case 10:
                result = "十月";
                break;
            case 11:
                result = "十一月";
                break;
            case 12:
                result = "十二月";
                break;
            default:
                break;
        }
        return result;
    }
}
