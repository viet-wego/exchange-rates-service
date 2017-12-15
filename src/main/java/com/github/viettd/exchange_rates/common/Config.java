package com.github.viettd.exchange_rates.common;

import org.apache.log4j.Logger;

public class Config {

    private static final Logger LOGGER = Logger.getLogger(Config.class);

    private static int restPort;
    private static String version;
    private static  String sourceUrl;
    private static String sourceAppId;

    public static void loadConfig() {
        restPort = 8088;
        version = "v1";
    }

    public static int getRestPort() {
        return restPort;
    }

    public static String getVersion() {
        return version;
    }

    public static String getSourceUrl() {
        return sourceUrl;
    }

    public static String getSourceAppId() {
        return sourceAppId;
    }
}
