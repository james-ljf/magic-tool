package com.magictool.web.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Time tool class
 * @author lijf
 */
public class DateUtil extends DateUtils {
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * Get now datetime
     * @return Date()
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * Get current date
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static String dateTimeNow(String format) {
        return parseDateToStr(format, new Date());
    }

    public static String dateTime(Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static String dateTimeSecond(Date date) {
        return parseDateToStr(YYYY_MM_DD_HH_MM_SS, date);
    }

    public static String parseDateToStr(final String format, Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static Date dateTime(final String format, String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * yyyy/MM/dd formats datetime
     * @return String
     */
    public static String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * yyyyMMdd formats datetime
     * @return  String
     */
    public static String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * Convert String type to date type
     * @param str   datetime
     * @return  Date
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Calculate the two time differences,return "xx , xx , xx ,"
     * It is recommended to use replaceAll() method separated commas to obtain similar "xx day xx hour xx min" formats
     * @param endDate   结束时间
     * @param startDate 开始时间
     * @return  String
     */
    public static String getDatePoor(Date endDate, Date startDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        if (day < 0 && hour < 0 && min < 0) {
            return null;
        }
        return day + "," + hour + "," + min + ",";
    }

    /**
     * Calculate the number of days between two dates and return
     * @param endDate   结束时间
     * @param startDate 开始时间
     * @return  Integer
     */
    public static Integer getDatePoorAge(Date endDate, Date startDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        if (day < 0 && hour < 0 && min < 0) {
            return null;
        }
        return (int)day;
    }
}

