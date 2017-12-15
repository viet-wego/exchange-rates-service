package com.github.viettd.exchange_rates.utils;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.servlet.http.HttpServletRequest;

public class Utils {

    public static void appendLog(StringBuilder logBuilder, String strLog) {
        logBuilder.append(strLog).append("\t");
    }

    public static void appendExceptionLog(StringBuilder logBuilder, String message, Throwable exception) {
        logBuilder.append(message).append("\terror:").append(ExceptionUtils.getStackTrace(exception));
    }

    public static String getClientIp(HttpServletRequest req) {
        String clientIp = "";
        if (req.getHeader("HTTP_X_FORWARDED_FOR") != null) {
            clientIp = req.getHeader("HTTP_X_FORWARDED_FOR");
        } else if (req.getHeader("X-Forwarded-For") != null) {
            clientIp = req.getHeader("X-Forwarded-For");
        } else if (req.getHeader("REMOTE_ADDR") != null) {
            clientIp = req.getHeader("REMOTE_ADDR");
        }
        if ("".equals(clientIp)) {
            clientIp = req.getRemoteAddr();
        }
        return clientIp;
    }

    public static String getUrl(HttpServletRequest req) {
        String requestUri = req.getRequestURI();
        String queryString = req.getQueryString();

        if (StringUtils.isBlank(queryString)) {
            return requestUri;
        } else {
            return requestUri + "?" + queryString;
        }
    }
}
