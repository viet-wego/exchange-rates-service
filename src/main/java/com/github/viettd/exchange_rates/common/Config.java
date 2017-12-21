package com.github.viettd.exchange_rates.common;

import com.github.viettd.exchange_rates.bean.ExchangeRateListResponse;
import com.github.viettd.exchange_rates.storage.ExchangeRatesStorage;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalINIConfiguration;

import java.io.File;
import java.time.Instant;
import java.util.*;

public class Config {

    private static final String CONFIG_FILE_PATH = "conf" + File.separator + "config.ini";
    private static final int RATES_LOADING_DELAY_TIME_IN_SECONDS = 1;
    private static final int RATES_RELOADING_PERIOD_IN_SECONDS = 300;
    private static final int RATES_CACHING_TIME_IN_SECONDS = 3600;

    private static CompositeConfiguration configuration = new CompositeConfiguration();

    private static String servicePath;
    private static int restPort;
    private static String version;
    private static Set<String> apiKeys;
    private static String sourceUrl;
    private static String sourceAppId;

    private static String getConfig(String section, String key) {
        return configuration != null ? configuration.getString(String.format("%s.%s", section, key), "") : "";
    }

    private static int getIntConfig(String section, String key) {
        return configuration != null ? configuration.getInt(String.format("%s.%s", section, key), 0) : 0;
    }

    private static List<Object> getListConfig(String section, String key) {
        return configuration != null ? configuration.getList(String.format("%s.%s", section, key), new ArrayList<>()) : new ArrayList<>();
    }

    public static void loadConfig() throws ConfigurationException {
        File configFile = new File(CONFIG_FILE_PATH);
        configuration.addConfiguration(new HierarchicalINIConfiguration(configFile));

        restPort = getIntConfig("server", "port");
        servicePath = getConfig("server", "service_path");
        version = getConfig("server", "version");
        apiKeys = new HashSet<>();
        List<Object> listConfig = getListConfig("server", "api_keys");
        listConfig.forEach(obj -> apiKeys.add(obj.toString()));

        sourceUrl = getConfig("source", "url");
        sourceAppId = getConfig("source", "app_id");

        Timer reloadRatesTimer = new Timer();
        reloadRatesTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                ExchangeRateListResponse rates = ExchangeRatesStorage.getExchangeRates();
                if (rates == null || rates.getRates() == null || rates.getRates().isEmpty()
                        || Instant.now().minusSeconds(RATES_CACHING_TIME_IN_SECONDS).getEpochSecond() >= rates.getTimestamp()) {
                    ExchangeRatesStorage.loadExchangeRates();
                }
            }
        }, RATES_LOADING_DELAY_TIME_IN_SECONDS * 100, RATES_RELOADING_PERIOD_IN_SECONDS * 1000);
    }


    public static Set<String> getApiKeys() {
        return apiKeys;
    }

    public static String getServicePath() {
        return servicePath;
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
