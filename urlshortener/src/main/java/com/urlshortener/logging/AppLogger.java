package com.urlshortener.logging;

import com.urlshortener.config.Config;


public class AppLogger {

    private final Config config;

    public AppLogger(Config config) {
        this.config = config;
    }

    public void fatal(String method, String message, Object... info) {
        // TODO
    }

    public void error(String method, String message, Object... info) {
        // TODO
    }

    public void warn(String method, String message, Object... info) {
        // TODO
    }

    public void info(String method, String message, Object... info) {
        // TODO
    }

    public void doAssert(boolean flag, String method, String message,
            Object... info) throws AssertionException {

        if (!flag) {
            fatal(method, message, info);
            throw new AssertionException(method + ", " + message);
        }
    }

    public void doFail(String method, String message, Object... info)
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
