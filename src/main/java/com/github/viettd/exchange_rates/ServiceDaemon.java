package com.github.viettd.exchange_rates;

import com.github.viettd.exchange_rates.common.Config;
import com.github.viettd.exchange_rates.common.RestServer;
import org.apache.log4j.Logger;

public class ServiceDaemon {

    private static final Logger LOGGER = Logger.getLogger(ServiceDaemon.class);
    private static RestServer restServer = null;

    public static void main(String... args) {
        try {
            Config.loadConfig();
            restServer = RestServer.getInstance();
            new Thread(restServer).start();
            Runtime.getRuntime().addShutdownHook(new Thread(ServiceDaemon::shutdownServer, "Stop Server Hook"));
        } catch (Exception e) {
            LOGGER.error("Start service error", e);
            System.exit(3);
        }
    }

    private static void shutdownServer() {
        try {
            LOGGER.info("Waiting for services stopping...");
            if (restServer != null) {
                restServer.stop();
            }
            LOGGER.info("Server was shut down.");
        } catch (Exception e) {
            LOGGER.error("Shut down server error", e);
        }
    }
}
