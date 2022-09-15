package com.magictool.web.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Time tool class
 *
 * @author lijf
 */
public class DateUtil extends DateUtils {

    /**
     * 计算两次时间差，返回“xx , xx , xx ”
     * 建议使用replaceAll() 方法用逗号分隔得到类似的“xx 日 xx 小时 xx 分钟” 格式
     *
     * @param endDate   结束时间
     * @param startDate 开始时间
     * @return String
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
     * 计算两个日期之间的天数并返回
     *
     * @param endDate   结束时间
     * @param startDate 开始时间
     * @return Integer
     */
    public static Integer getDatePoorAge(Date endDate, Date startDate) {
        long nd = 1000 * 24 * 60 * 60L;
        long nh = 1000 * 60 * 60L;
        long nm = 1000 * 60L;
        long diff = endDate.getTime() - startDate.getTime();
        long day = diff / nd;
        long hour = diff % nd / nh;
        long min = diff % nd % nh / nm;
        if (day < 0 && hour < 0 && min < 0) {
            return null;
        }
        return (int) day;
    }
}

