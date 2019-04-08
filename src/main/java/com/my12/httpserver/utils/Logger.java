package com.my12.httpserver.utils;

/**
 * 日志记录器
 * @author my12
 */
public class Logger {
    private Logger() {

    }

    /**
     * 普通日志记录器
     *
     * @param msg
     */
    public static void log(String msg) {

        System.out.println("[INFO] " + DateUtil.getCurrentTime() + " " + msg);
    }



}
