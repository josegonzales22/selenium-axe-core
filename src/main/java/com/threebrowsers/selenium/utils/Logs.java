package com.threebrowsers.selenium.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Logs {
    private static final Logger log = LogManager.getLogger("AUTOMATION");

    public static void trace(String message) {
        log.trace(message);
    }

    public static void debug(String message) {
        log.debug(message);
    }

    public static void info(String message) {
        log.info(message);
    }

    public static void error(String message) {
        log.error(message);
    }

    public static void warning(String message) {
        log.warn(message);
    }

    public static void fatal(String message) {
        log.fatal(message);
    }

    public static void trace(String format, Object... args) {
        log.trace(format, args);
    }

    public static void debug(String format, Object... args) {
        log.debug(format, args);
    }

    public static void info(String format, Object... args) {
        log.info(format, args);
    }

    public static void warning(String format, Object... args) {
        log.warn(format, args);
    }

    public static void error(String format, Object... args) {
        log.error(format, args);
    }

    public static void fatal(String format, Object... args) {
        log.fatal(format, args);
    }
}