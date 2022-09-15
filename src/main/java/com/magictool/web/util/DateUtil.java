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

    /**
     * Calculate the two time differences,return "xx , xx , xx ,"
     * It is recommended to use replaceAll() method separated commas to obtain similar "xx day xx hour xx min" formats
     * @param endDate   结束时间
     * @param startDate 开始时间
     * @return  String
     */
    public static String getDatePoor(Date endDate, Date startDate) {
        long nd = 1000 * 24 * 60 * 60L;
        long nh = 1000 * 60 * 60L;
        long nm = 1000 * 60L;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - startDate.getTime();
        long day = diff / nd;
        long hour = diff % nd / nh;
        long min = diff % nd % nh / nm;
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
        long diff = endDate.getTime() - startDate.getTime();
        long day = diff / nd;
        long hour = diff % nd / nh;
        long min = diff % nd % nh / nm;
        if (day < 0 && hour < 0 && min < 0) {
            return null;
        }
        return (int)day;
    }
}

