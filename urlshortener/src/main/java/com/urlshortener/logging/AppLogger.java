package com.urlshortener.logging;


public class AppLogger {

    public AppLogger() {
    }

    public void fatal(String method, String message, String... info) {
        // TODO
    }

    public void error(String method, String message, String... info) {
        // TODO
    }

    public void warn(String method, String message, String... info) {
        // TODO
    }

    public void info(String method, String message, String... info) {
        // TODO
    }

    public void doAssert(boolean flag, String method, String message,
            String... info) throws AssertionException {

        if (!flag) {
            fatal(method, message, info);
            throw new AssertionException(method + ", " + message);
        }
    }


    public static String constructLog(String method, String message,
            String... info) {

        // construct error message
        StringBuilder sb = new StringBuilder()
            .append(method)
            .append("; ")
            .append(message)
            .append("; ");

        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                sb.append(info[i]);
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
