package com.github.viettd.exchange_rates.utils;


import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

import static org.apache.commons.codec.Charsets.UTF_8;

public class Utils {

    private static final Logger LOGGER = Logger.getLogger(Utils.class);

    public static String callGetApi(String url) {
        String response = "";
        try {
            RequestConfig conf = RequestConfig.custom()
                    .setSocketTimeout(90000)
                    .setConnectTimeout(90000)
                    .setConnectionRequestTimeout(90000)
                    .build();
            CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(conf).build();
            HttpUriRequest req = new HttpGet(url);
            try (CloseableHttpResponse res = client.execute(req)) {
                HttpEntity entity = res.getEntity();
                InputStream inputStream = entity.getContent();
                response = IOUtils.toString(inputStream, UTF_8);
            }
        } catch (Exception e) {
            LOGGER.error("callGetApi error:", e);
        }
        return response;
    }

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
