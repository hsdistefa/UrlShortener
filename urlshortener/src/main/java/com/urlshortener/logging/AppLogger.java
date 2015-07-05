package com.urlshortener.logging;

import com.urlshortener.AssertionException;
import com.urlshortener.config.Config;


public class AppLogger {

    // all app logging requests go through this one instance
    private static AppLogger log;

    private final Config config;

    private AppLogger(Config config) {
        this.config = config;
    }

    /**
     * This must be called before any logging can be done
     */
    public static void initialize(Config config) {
        log = new AppLogger(config);
    }

    public static void fatal(String method, String message, Object... info) {
        // TODO
    }

    public static void error(String method, String message, Object... info) {
        // TODO
    }

    public static void warn(String method, String message, Object... info) {
        // TODO
    }

    public static void info(String method, String message, Object... info) {
        // TODO
    }

    public static void doAssert(boolean flag, String method, String message,
            Object... info) throws AssertionException {

        if (!flag) {
            fatal(method, message, info);
            throw new AssertionException(method + ", " + message);
        }
    }

    public static void doFail(String method, String message, Object... info)
            throws AssertionException {
        doAssert(false, method, message, info);
    }


    public static String constructLog(String method, String message,
            Object... info) {

        // construct error message
        StringBuilder sb = new StringBuilder()
            .append(method)
            .append("; ")
            .append(message)
            .append("; ");

        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                sb.append(info[i].toString());
                if (i % 2 == 0) {
                    sb.append(": ");
                } else {
                    sb.append(", ");
                }
            }
        }

        return sb.toString();
    }
}
