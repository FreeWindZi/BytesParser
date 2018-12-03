package com.sht.bytesparser.log;

import java.util.logging.Logger;

public class LoggerUtil  {

    private static LogAdapter log = new JavaLog();

    public static void setAdapter(LogAdapter log) {
        LoggerUtil.log = log;
    }
    public static void e(String message, Object... args) {
        log.e(message, args);
    }
    public static void w(String message, Object... args) {
        log.w(message, args);
    }
    public static void i(String message, Object... args) {
        log.i(message, args);
    }
    public static void d(String message, Object... args) {
        log.d(message, args);
    }
    public static void v(String message, Object... args) {
        log.v(message, args);
    }
}
