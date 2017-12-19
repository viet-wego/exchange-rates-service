package com.github.viettd.exchange_rates.common;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.log4j.Logger;

import java.io.File;

public class Config {

    private static final Logger LOGGER = Logger.getLogger(Config.class);

    private static final String CONFIG_FILE_PATH = "conf" + File.separator + "config.ini";
    private static CompositeConfiguration configuration = new CompositeConfiguration();

    private static int restPort;
    private static String version;
    private static String sourceUrl;
    private static String sourceAppId;

    private static String getConfig(String section, String key) {
        return configuration != null ? configuration.getString(String.format("%s.%s", section, key), "") : "";
    }

    private static int getIntConfig(String section, String key) {
        return configuration != null ? configuration.getInt(String.format("%s.%s", section, key), 0) : 0;
    }

    public static void loadConfig() throws ConfigurationException {
        File configFile = new File(CONFIG_FILE_PATH);
        configuration.addConfiguration(new HierarchicalINIConfiguration(configFile));

        restPort = getIntConfig("server", "port");
        version = getConfig("server", "version");
        sourceUrl = getConfig("source", "url");
        sourceAppId = getConfig("source", "app_id");
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
