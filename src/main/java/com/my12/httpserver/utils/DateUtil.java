package com.my12.httpserver.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 处理日期的工具类
 * @author my12
 */
public class DateUtil {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");


    private DateUtil(){

    }

    /**
     * 获取系统当前时间
     * @return String [yyyy-MM-dd HH:mm:ss SSS]
     */
    public static String getCurrentTime(){

        return dateFormat.format(new Date());
    }

}
