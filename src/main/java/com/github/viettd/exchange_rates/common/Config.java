package com.github.viettd.exchange_rates.common;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.HierarchicalINIConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;

public class Config {

    private static final Logger LOGGER = Logger.getLogger(Config.class);

    private static final String CONFIG_FILE_PATH = "conf" + File.separator + "config.ini";
    private static CompositeConfiguration configuration;

    private static int restPort;
    private static String version;
    private static String sourceUrl;
    private static String sourceAppId;

    private static String getConfig(String section, String key) {
        return configuration.getString(String.format("%s.%s", section, key), "");
    }

    private static int getIntConfig(String section, String key) {
        return configuration.getInt(String.format("%s.%s", section, key), 0);
    }

    public static void loadConfig() {
        try {
            String conf = System.getProperty("config_file");
            File configFile;
            if (conf != null) {
                configFile = new File(conf);
            } else {
                configFile = new File(CONFIG_FILE_PATH);
            }
            if (configFile.exists()) {
                configuration = new CompositeConfiguration();
                configuration.addConfiguration(new HierarchicalINIConfiguration(configFile));
            } else {
                LOGGER.error("Configuration file doesn't exist.");
                System.exit(1);
            }
            restPort = getIntConfig("server", "port");
            version = getConfig("server", "version");
            sourceUrl = getConfig("source", "base_url");
            sourceAppId = getConfig("source", "app_id");
        } catch (Exception e) {
            LOGGER.error("Error when init configuration:", e);
            System.exit(1);
        }
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
